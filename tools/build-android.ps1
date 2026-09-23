[CmdletBinding()]
param(
    [string]$ToolchainRoot = 'D:\gxy_code\_toolchain',
    [string]$JavaHome = '',
    [string]$SdkRoot = '',
    [string]$GradleExecutable = '',
    [string]$GradleUserHome = '',
    [switch]$PrepareOnly,
    [switch]$CandidateOnly,
    [switch]$AllowNetwork
)

$ErrorActionPreference = 'Stop'
Set-StrictMode -Version Latest
$projectRoot = [IO.Path]::GetFullPath((Join-Path $PSScriptRoot '..'))
$androidRoot = Join-Path $projectRoot 'android'
$release = Get-Content -LiteralPath (Join-Path $androidRoot 'release.json') -Raw -Encoding UTF8 | ConvertFrom-Json
if ($release.applicationId -ne 'com.jibao.kitchen' -or $release.versionCode -lt 1 -or $release.versionName -notmatch '^\d+\.\d+\.\d+$') { throw 'Invalid Android release identity or version.' }
if (-not $JavaHome) { $JavaHome = Join-Path $ToolchainRoot 'jdk17' }
if (-not $SdkRoot) { $SdkRoot = Join-Path $ToolchainRoot 'android-sdk' }
if (-not $GradleExecutable) { $GradleExecutable = Join-Path $ToolchainRoot 'gradle-8.9\bin\gradle.bat' }
if (-not $GradleUserHome) { $GradleUserHome = Join-Path $ToolchainRoot 'gradle-home' }

function Invoke-Checked([string]$Executable, [string[]]$Arguments) {
    & $Executable @Arguments
    if ($LASTEXITCODE -ne 0) { throw "Command failed (exit $LASTEXITCODE): $([IO.Path]::GetFileName($Executable))" }
}
function Write-Json([string]$Path, $Value) {
    [IO.File]::WriteAllText($Path, ($Value | ConvertTo-Json -Depth 15) + "`n", [Text.UTF8Encoding]::new($false))
}
function Get-Sha256([string]$Path) {
    $stream = [IO.File]::OpenRead($Path)
    $hash = [Security.Cryptography.SHA256]::Create()
    try { return [BitConverter]::ToString($hash.ComputeHash($stream)).Replace('-','').ToLowerInvariant() }
    finally { $stream.Dispose(); $hash.Dispose() }
}
function New-PrivatePassword {
    $bytes = New-Object byte[] 36
    $random = [Security.Cryptography.RandomNumberGenerator]::Create()
    try { $random.GetBytes($bytes) } finally { $random.Dispose() }
    return [Convert]::ToBase64String($bytes)
}

$buildLock = [IO.File]::Open((Join-Path $androidRoot '.build.lock'),[IO.FileMode]::OpenOrCreate,[IO.FileAccess]::ReadWrite,[IO.FileShare]::None)
try {
if (-not $PrepareOnly) {
    & (Join-Path $PSScriptRoot 'verify-release.ps1') -JavaHome $JavaHome
    if (-not $?) { throw 'Release verification failed. Signing and compilation have not started.' }
}
Invoke-Checked 'node' @((Join-Path $androidRoot 'package-runtime.mjs'))
if ($PrepareOnly) { Write-Output 'Runtime assets prepared; signing and compilation were not started.'; return }

$keytool = Join-Path $JavaHome 'bin\keytool.exe'
$apkSigner = Join-Path $SdkRoot 'build-tools\35.0.0\apksigner.bat'
$aapt = Join-Path $SdkRoot 'build-tools\35.0.0\aapt.exe'
foreach ($required in @($keytool,$GradleExecutable,$apkSigner,$aapt,(Join-Path $SdkRoot 'platforms\android-35\android.jar'))) {
    if (-not (Test-Path -LiteralPath $required -PathType Leaf)) { throw "Missing build tool: $required" }
}
foreach ($javaClass in @('MainActivity','HatchAlarmReceiver','RestoreAlarmsReceiver')) {
    if (-not (Test-Path -LiteralPath (Join-Path $androidRoot "app\src\main\java\com\jibao\kitchen\$javaClass.java") -PathType Leaf)) { throw "Android implementation is not ready: $javaClass.java" }
}
$releasesRoot = Join-Path $projectRoot 'artifacts\releases'
$stem = "chick-kitchen-$($release.versionName)"
$finalApk = Join-Path $releasesRoot "$stem.apk"
$metadataPath = Join-Path $releasesRoot "$stem.release.json"
if ((Test-Path -LiteralPath $finalApk) -or (Test-Path -LiteralPath $metadataPath)) { throw 'This release already exists. Increment versionCode and versionName in android/release.json before making a new release.' }
if (Test-Path -LiteralPath $releasesRoot) {
    foreach ($oldFile in Get-ChildItem -LiteralPath $releasesRoot -Filter '*.release.json' -File) {
        $oldRelease = Get-Content -LiteralPath $oldFile.FullName -Raw -Encoding UTF8 | ConvertFrom-Json
        if ($oldRelease.applicationId -eq $release.applicationId -and [int]$oldRelease.versionCode -ge [int]$release.versionCode) { throw 'versionCode must be higher than every previously released version.' }
    }
}

$signingRoot = Join-Path $projectRoot '.signing'
$keyPath = Join-Path $signingRoot 'chick-kitchen-release.jks'
$secretsPath = Join-Path $signingRoot 'signing-secrets.json'
$identityPath = Join-Path $androidRoot 'release-identity.json'
$hadIdentity = Test-Path -LiteralPath $identityPath
$hadKey = Test-Path -LiteralPath $keyPath
$hadSecrets = Test-Path -LiteralPath $secretsPath
if (($hadIdentity -and (-not $hadKey -or -not $hadSecrets)) -or ($hadKey -ne $hadSecrets)) { throw 'The permanent signing key or its credentials are missing. Restore .signing from your secure backup; a new identity will not be generated.' }
if (-not $hadKey -and (Test-Path -LiteralPath $releasesRoot) -and @(Get-ChildItem -LiteralPath $releasesRoot -File | Where-Object { $_.Extension -eq '.apk' -or $_.Name -like '*.release.json' }).Count -gt 0) { throw 'Earlier releases exist. Restore their permanent signing key instead of creating another identity.' }

$environmentNames = @('JAVA_HOME','ANDROID_HOME','ANDROID_SDK_ROOT','GRADLE_USER_HOME','CHICK_SIGNING_FILE','CHICK_SIGNING_STOREPASS','CHICK_SIGNING_ALIAS','CHICK_SIGNING_KEYPASS')
$oldEnvironment = @{}
foreach ($name in $environmentNames) { $oldEnvironment[$name] = [Environment]::GetEnvironmentVariable($name,'Process') }
$certificateTemp = Join-Path $androidRoot '.release-certificate.der'
try {
    $env:JAVA_HOME = $JavaHome
    $env:ANDROID_HOME = $SdkRoot
    $env:ANDROID_SDK_ROOT = $SdkRoot
    $env:GRADLE_USER_HOME = $GradleUserHome
    if (-not $hadKey) {
        New-Item -ItemType Directory -Path $signingRoot -Force | Out-Null
        $secrets = [ordered]@{ alias='jibao-release'; storePassword=(New-PrivatePassword); keyPassword=(New-PrivatePassword) }
        Write-Json $secretsPath $secrets
    } else { $secrets = Get-Content -LiteralPath $secretsPath -Raw -Encoding UTF8 | ConvertFrom-Json }
    $env:CHICK_SIGNING_FILE = $keyPath
    $env:CHICK_SIGNING_STOREPASS = $secrets.storePassword
    $env:CHICK_SIGNING_ALIAS = $secrets.alias
    $env:CHICK_SIGNING_KEYPASS = $secrets.keyPassword
    if (-not $hadKey) {
        $capturePreference = $ErrorActionPreference
        try {
            $ErrorActionPreference = 'Continue'
            $keyOutput = & $keytool '-genkeypair' '-keystore' $keyPath '-storetype' 'JKS' '-storepass:env' 'CHICK_SIGNING_STOREPASS' '-keypass:env' 'CHICK_SIGNING_KEYPASS' '-alias' $secrets.alias '-keyalg' 'RSA' '-keysize' '3072' '-validity' '36500' '-dname' 'CN=Jibao Kitchen, OU=Personal Game, O=Jibao Kitchen, C=CN' '-noprompt' 2>&1
        } finally { $ErrorActionPreference = $capturePreference }
        if ($LASTEXITCODE -ne 0) { throw 'Signing key generation failed. Inspect and restore .signing before retrying; no existing identity was replaced.' }
    }
    $capturePreference = $ErrorActionPreference
    try {
        $ErrorActionPreference = 'Continue'
        $certificateOutput = & $keytool '-exportcert' '-keystore' $keyPath '-storepass:env' 'CHICK_SIGNING_STOREPASS' '-alias' $secrets.alias '-file' $certificateTemp 2>&1
    } finally { $ErrorActionPreference = $capturePreference }
    if ($LASTEXITCODE -ne 0) { throw 'Cannot read the permanent signing certificate.' }
    $certificateSha256 = (Get-Sha256 $certificateTemp)
    if ($hadIdentity) {
        $identity = Get-Content -LiteralPath $identityPath -Raw -Encoding UTF8 | ConvertFrom-Json
        if ($identity.applicationId -ne $release.applicationId -or $identity.certificateSha256 -ne $certificateSha256) { throw 'Signing identity does not match the original release. Build stopped to protect future updates.' }
    } else {
        Write-Json $identityPath ([ordered]@{ applicationId=$release.applicationId; certificateSha256=$certificateSha256; keyAlias=$secrets.alias; createdAt=[DateTime]::UtcNow.ToString('o'); note='Public certificate identity only. Keep this file in source control; private material belongs in .signing.' })
    }
    $localProperties = 'sdk.dir=' + $SdkRoot.Replace('\','/').Replace(':','\:') + "`n"
    [IO.File]::WriteAllText((Join-Path $androidRoot 'local.properties'),$localProperties,[Text.UTF8Encoding]::new($false))
    $gradleArguments = @('-p',$androidRoot,'--no-daemon','--console=plain')
    if (-not $AllowNetwork) { $gradleArguments += '--offline' }
    $gradleArguments += @('clean',':app:assembleRelease')
    Invoke-Checked $GradleExecutable $gradleArguments
    $builtApk = Join-Path $androidRoot 'app\build\outputs\apk\release\app-release.apk'
    if (-not (Test-Path -LiteralPath $builtApk -PathType Leaf)) { throw 'The current build produced no signed APK. No release artifact was published.' }
    $capturePreference = $ErrorActionPreference
    try {
        $ErrorActionPreference = 'Continue'
        $verifyOutput = & $apkSigner 'verify' '--verbose' '--print-certs' $builtApk 2>&1
    } finally { $ErrorActionPreference = $capturePreference }
    if ($LASTEXITCODE -ne 0) { throw 'APK signature verification failed. No release artifact was published.' }
    $verifiedText = $verifyOutput -join "`n"
    $signedDigest = [regex]::Match($verifiedText,'Signer #1 certificate SHA-256 digest:\s*([a-fA-F0-9]+)').Groups[1].Value.ToLowerInvariant()
    if ($signedDigest -ne $certificateSha256) { throw 'Built APK certificate differs from the permanent release identity.' }
    $capturePreference = $ErrorActionPreference
    try {
        $ErrorActionPreference = 'Continue'
        $packageOutput = & $aapt 'dump' 'badging' $builtApk 2>&1
    } finally { $ErrorActionPreference = $capturePreference }
    if ($LASTEXITCODE -ne 0) { throw 'Cannot inspect the newly built APK manifest.' }
    $packageLine = [regex]::Match(($packageOutput -join "`n"),"package: name='([^']+)' versionCode='([^']+)' versionName='([^']+)'")
    if (-not $packageLine.Success -or $packageLine.Groups[1].Value -ne $release.applicationId -or [int]$packageLine.Groups[2].Value -ne [int]$release.versionCode -or $packageLine.Groups[3].Value -ne $release.versionName) { throw 'Built APK package or version does not match the requested release.' }
    $runtimeManifest = Get-Content -LiteralPath (Join-Path $androidRoot 'generated-assets\runtime-manifest.json') -Raw -Encoding UTF8 | ConvertFrom-Json
    Add-Type -AssemblyName System.IO.Compression.FileSystem
    $apkArchive = [IO.Compression.ZipFile]::OpenRead($builtApk)
    try {
        $actualAssets = @($apkArchive.Entries | Where-Object { $_.FullName.StartsWith('assets/') -and -not $_.FullName.EndsWith('/') } | ForEach-Object { $_.FullName.Substring(7) } | Sort-Object)
        $expectedAssets = @(@($runtimeManifest.assets | ForEach-Object { $_.path }) + @('web/app-version.json','runtime-manifest.json') | Sort-Object)
        if (@(Compare-Object $expectedAssets $actualAssets).Count -gt 0) { throw 'APK runtime files differ from the verified offline asset manifest.' }
    } finally { $apkArchive.Dispose() }
    Invoke-Checked 'python' @((Join-Path $PSScriptRoot 'check_apk_runtime.py'),$builtApk)
    $apkHash = (Get-Sha256 $builtApk)
    $metadata = [ordered]@{
        schema=1; applicationId=$release.applicationId; versionName=$release.versionName; versionCode=$release.versionCode
        file="$stem.apk"; sha256=$apkHash; bytes=(Get-Item -LiteralPath $builtApk).Length
        certificateSha256=$certificateSha256; builtAt=[DateTime]::UtcNow.ToString('o')
        minSdk=26; targetSdk=35; runtimeContentHash=$runtimeManifest.contentHash; runtimeFiles=$runtimeManifest.files
        notes=$release.notes; installMode='Same applicationId and release signature; install with replacement, never uninstall to update.'
    }
    if ($CandidateOnly) {
        Write-Json (Join-Path $androidRoot '.candidate-release.json') $metadata
        Write-Output "Verified build candidate: $builtApk"
        Write-Output "Candidate APK SHA-256: $apkHash"
        Write-Output 'No final release artifact or release-history record was published.'
        return
    }
    New-Item -ItemType Directory -Path $releasesRoot -Force | Out-Null
    Copy-Item -LiteralPath $builtApk -Destination $finalApk
    [IO.File]::WriteAllText((Join-Path $releasesRoot "$stem.sha256"),"$apkHash  $stem.apk`n",[Text.UTF8Encoding]::new($false))
    Write-Json $metadataPath $metadata
    Write-Output "Release ready: $finalApk"
    Write-Output "APK SHA-256: $apkHash"
    Write-Output "Permanent signing certificate SHA-256: $certificateSha256"
} finally {
    if (Test-Path -LiteralPath $certificateTemp) { Remove-Item -LiteralPath $certificateTemp -Force }
    foreach ($name in $environmentNames) { [Environment]::SetEnvironmentVariable($name,$oldEnvironment[$name],'Process') }
}
} finally { $buildLock.Dispose() }
