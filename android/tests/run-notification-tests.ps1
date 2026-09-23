param([string]$JavaHome = 'D:\gxy_code\_toolchain\jdk17')
$ErrorActionPreference = 'Stop'
$taskRoot = Split-Path -Parent $PSScriptRoot
$taskClasses = Join-Path $taskRoot 'build/notification-test-classes'
New-Item -ItemType Directory -Force -Path $taskClasses | Out-Null
$taskSources = @(Get-ChildItem -LiteralPath (Join-Path $PSScriptRoot 'notifications') -Recurse -Filter '*.java' | ForEach-Object FullName)
$taskSources += @(Get-ChildItem -LiteralPath (Join-Path $PSScriptRoot 'aosp-json') -Recurse -Filter '*.java' | ForEach-Object FullName)
$taskSources += @('HatchScheduler.java','HatchAlarmReceiver.java','RestoreAlarmsReceiver.java') | ForEach-Object { Join-Path $taskRoot "app/src/main/java/com/jibao/kitchen/$_" }
& (Join-Path $JavaHome 'bin/javac.exe') -encoding UTF-8 -d $taskClasses @taskSources
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
& (Join-Path $JavaHome 'bin/java.exe') -cp $taskClasses com.jibao.kitchen.HatchSchedulerTest
exit $LASTEXITCODE
