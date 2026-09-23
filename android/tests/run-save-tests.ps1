param([string]$JavaHome = 'D:\gxy_code\_toolchain\jdk17')
$ErrorActionPreference = 'Stop'
$taskRoot = Split-Path -Parent $PSScriptRoot
$taskClasses = Join-Path $taskRoot 'build/save-test-classes'
$taskData = Join-Path $taskRoot 'build/save-test-data'
New-Item -ItemType Directory -Force -Path $taskClasses | Out-Null
$taskShared = Join-Path $taskRoot 'build/web-command-fixtures.json'
& node (Join-Path $PSScriptRoot 'build-save-fixtures.mjs') $taskShared
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
$taskSources = @(Get-ChildItem -LiteralPath (Join-Path $PSScriptRoot 'saves') -Recurse -Filter '*.java' | ForEach-Object FullName)
$taskSources += @(Get-ChildItem -LiteralPath (Join-Path $PSScriptRoot 'aosp-json') -Recurse -Filter '*.java' | ForEach-Object FullName)
$taskSources += Join-Path $taskRoot 'app/src/main/java/com/jibao/kitchen/SaveRepository.java'
$taskSources += Join-Path $taskRoot 'app/src/main/java/com/jibao/kitchen/SaveBridge.java'
$taskSources += Join-Path $taskRoot 'app/src/main/java/com/jibao/kitchen/RuntimeContent.java'
$taskSources += Join-Path $taskRoot 'app/src/main/java/com/jibao/kitchen/BusinessSaveValidator.java'
& (Join-Path $JavaHome 'bin/javac.exe') -encoding UTF-8 -d $taskClasses @taskSources
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
& (Join-Path $JavaHome 'bin/java.exe') -cp $taskClasses com.jibao.kitchen.SaveRepositoryTest $taskData (Join-Path $taskRoot '../tests/fixtures/save-contract.json') $taskShared
exit $LASTEXITCODE
