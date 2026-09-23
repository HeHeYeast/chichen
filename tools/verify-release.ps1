[CmdletBinding()]
param([string]$JavaHome = 'D:\gxy_code\_toolchain\jdk17')

$ErrorActionPreference = 'Stop'
Set-StrictMode -Version Latest
$verificationRoot = [IO.Path]::GetFullPath((Join-Path $PSScriptRoot '..'))
$verificationDirectory = Join-Path $verificationRoot 'android\build'
New-Item -ItemType Directory -Path $verificationDirectory -Force | Out-Null
$verificationLog = Join-Path $verificationDirectory 'release-verification.log'
$verificationResult = Join-Path $verificationDirectory 'release-verification.json'
$verificationChecks = [Collections.Generic.List[object]]::new()
$verificationRequiredChecks = 9
[IO.File]::WriteAllText($verificationLog,"Release verification started: $([DateTime]::UtcNow.ToString('o'))`n",[Text.UTF8Encoding]::new($false))
$verificationShell = (Get-Process -Id $PID).Path

function Invoke-ReleaseCheck([string]$Name, [string]$Executable, [string[]]$Arguments) {
    $previousErrorPreference = $ErrorActionPreference
    try {
        # Windows PowerShell wraps normal native stderr (e.g. unittest progress)
        # as ErrorRecord. The process exit code is the required pass/fail signal.
        $ErrorActionPreference = 'Continue'
        $output = @(& $Executable @Arguments 2>&1)
        $resultCode = $LASTEXITCODE
    } finally { $ErrorActionPreference = $previousErrorPreference }
    $lines = @($output | ForEach-Object { $_.ToString() })
    [IO.File]::AppendAllText($verificationLog,"`n=== $Name (exit $resultCode) ===`n" + ($lines -join "`n") + "`n",[Text.UTF8Encoding]::new($false))
    $verificationChecks.Add([ordered]@{name=$Name;exitCode=$resultCode;passed=($resultCode -eq 0)})
    if ($resultCode -ne 0) { $lines | Write-Output; throw "Release check failed: $Name. No release may be built until this is fixed." }
    Write-Output "$Name : passed"
    $lines | Where-Object { $_ -match '^# (tests|pass|fail)\b|assertions|断言|Missing|missing|characters|字符|coverage' } | Write-Output
}

Push-Location $verificationRoot
try {
    # Read-only generation checks fail stale outputs instead of regenerating
    # them silently and hiding an unreviewed content change.
    Invoke-ReleaseCheck 'Runtime content generated outputs' 'node' @('tools/build-runtime-content.mjs','--check')
    Invoke-ReleaseCheck 'Native content generated outputs' 'node' @('android/tests/build-native-content.mjs','--check')
    Invoke-ReleaseCheck 'Runtime asset availability' 'node' @('--test','tests/runtime-packaging.test.mjs')
    Invoke-ReleaseCheck 'Game and interface tests' 'npm.cmd' @('test')
    Invoke-ReleaseCheck 'Chinese font coverage' 'python' @((Join-Path $verificationRoot 'tools\build-font.py'),'--check')
    Invoke-ReleaseCheck 'Native save persistence' $verificationShell @('-NoProfile','-ExecutionPolicy','Bypass','-File',(Join-Path $verificationRoot 'android\tests\run-save-tests.ps1'),'-JavaHome',$JavaHome)
    Invoke-ReleaseCheck 'Native hatch notifications' $verificationShell @('-NoProfile','-ExecutionPolicy','Bypass','-File',(Join-Path $verificationRoot 'android\tests\run-notification-tests.ps1'),'-JavaHome',$JavaHome)
    Invoke-ReleaseCheck 'Backup and update simulations' 'python' @('-m','unittest','discover','-s','tools/tests','-v')
    Invoke-ReleaseCheck 'Browser UI regression' 'node' @('tools/verify-ui.mjs','--release')
    Write-Output "Release preflight passed: all $verificationRequiredChecks required checks succeeded (22 browser suites: legacy seven, Works A-L, book navigation, takeover UI and compatible rollback)."
} finally {
    Pop-Location
    $result = [ordered]@{checkedAt=[DateTime]::UtcNow.ToString('o');passed=($verificationChecks.Count -eq $verificationRequiredChecks -and @($verificationChecks | Where-Object {-not $_.passed}).Count -eq 0);expectedCount=$verificationRequiredChecks;completedCount=$verificationChecks.Count;checks=@($verificationChecks.ToArray())}
    [IO.File]::WriteAllText($verificationResult,($result | ConvertTo-Json -Depth 5) + "`n",[Text.UTF8Encoding]::new($false))
}
