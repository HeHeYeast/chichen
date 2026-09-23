[CmdletBinding()]
param([switch]$Install, [string]$Serial = '', [string]$LegacyBackup = '')
$ErrorActionPreference = 'Stop'
# Snapshot creation verifies every archived file before building anything.
& python (Join-Path $PSScriptRoot 'source_snapshot.py')
if ($LASTEXITCODE -ne 0) { throw 'Source snapshot failed; build/update stopped.' }
& (Join-Path $PSScriptRoot 'build-android.ps1')
if (-not $?) { throw 'Build or release verification failed; update stopped.' }
if ($Install) {
    & (Join-Path $PSScriptRoot 'install-android.ps1') -Serial $Serial -LegacyBackup $LegacyBackup -Launch
    if (-not $?) { throw 'Safe update stopped. Existing save snapshots are retained.' }
} else {
    Write-Output 'Release built and verified. Device installation was not requested.'
}
