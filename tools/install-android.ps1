[CmdletBinding()]
param(
    [string]$Apk = '',
    [string]$Serial = '',
    [string]$Adb = 'D:\gxy_code\_toolchain\android-sdk\platform-tools\adb.exe',
    [string]$BackupRoot = '',
    [string]$LegacyBackup = '',
    [switch]$Launch,
    [switch]$BackupOnly,
    [switch]$CheckOnly
)
$ErrorActionPreference = 'Stop'
Set-StrictMode -Version Latest
# All installation entry points use this backup gate. No skip-backup switch.
$arguments = @((Join-Path $PSScriptRoot 'safe_android_update.py'),'--adb',$Adb)
if ($Apk) { $arguments += @('--apk',$Apk) }
if ($Serial) { $arguments += @('--serial',$Serial) }
if ($BackupRoot) { $arguments += @('--backup-root',$BackupRoot) }
if ($LegacyBackup) { $arguments += @('--legacy-backup',$LegacyBackup) }
if ($Launch) { $arguments += '--launch' }
if ($BackupOnly) { $arguments += '--backup-only' }
if ($CheckOnly) { $arguments += '--check-only' }
& python @arguments
if ($LASTEXITCODE -ne 0) { throw 'Safe update stopped. Read the message above; existing backups are retained.' }
