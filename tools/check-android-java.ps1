param([string]$JavaHome = 'D:\gxy_code\_toolchain\jdk17', [string]$SdkRoot = 'D:\gxy_code\_toolchain\android-sdk')
# Compile-only check of all app Java sources against android-35. Gradle-generated
# BuildConfig/R are stubbed here; this is not a packaged build or device test.
$ErrorActionPreference = 'Stop'
$androidRoot = Split-Path -Parent $PSScriptRoot | Join-Path -ChildPath 'android'
$out = Join-Path $androidRoot 'build/javac-check'
$stub = Join-Path $androidRoot 'build/javac-check-src/com/jibao/kitchen'
New-Item -ItemType Directory -Force -Path $out, $stub | Out-Null
Set-Content -Encoding ascii -Path (Join-Path $stub 'BuildConfig.java') -Value 'package com.jibao.kitchen; final class BuildConfig { static final String VERSION_NAME="check"; static final int VERSION_CODE=0; }'
Set-Content -Encoding ascii -Path (Join-Path $stub 'R.java') -Value 'package com.jibao.kitchen; final class R { static final class drawable { static final int ic_notification=0; } }'
$sources = @(Get-ChildItem -LiteralPath (Join-Path $androidRoot 'app/src/main/java/com/jibao/kitchen') -Filter '*.java' | ForEach-Object FullName)
$sources += (Join-Path $stub 'BuildConfig.java'), (Join-Path $stub 'R.java')
& (Join-Path $JavaHome 'bin/javac.exe') -encoding UTF-8 -nowarn -classpath (Join-Path $SdkRoot 'platforms/android-35/android.jar') -d $out @sources
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
Write-Output "Android Java compile check: $($sources.Count - 2) app sources compiled against android-35."
