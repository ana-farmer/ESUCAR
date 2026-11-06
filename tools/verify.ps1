param([string[]]$Tasks = @("assembleDebug", "testDebugUnitTest", "lintDebug"))
$previousJava = $env:JAVA_HOME
$previousOptions = $env:JAVA_TOOL_OPTIONS
try {
    if (-not $env:JAVA_HOME) { $env:JAVA_HOME = "C:/Program Files/Android/Android Studio/jbr" }
    $buildTemp = Join-Path $env:USERPROFILE ".codex/esucar-jtmp"
    New-Item -ItemType Directory -Force -Path $buildTemp | Out-Null
    $env:JAVA_TOOL_OPTIONS = "$previousOptions -Djdk.net.unixdomain.tmpdir=`"$buildTemp`" -Djava.io.tmpdir=`"$buildTemp`""
    Push-Location (Split-Path $PSScriptRoot -Parent)
    try { & ./gradlew.bat @Tasks --console=plain; $buildExit = $LASTEXITCODE } finally { Pop-Location }
} finally {
    $env:JAVA_HOME = $previousJava
    $env:JAVA_TOOL_OPTIONS = $previousOptions
}
exit $buildExit
