@echo off

set /p JDK_PATH="Enter the full path to the JDK (e.g., C:\Program Files\Java\jdk-17): "

if not exist "%JDK_PATH%\bin\java.exe" (
    echo Error: The specified JDK path is invalid or java.exe not found.
    pause
    exit /b 1
)

set JAVA_HOME=%JDK_PATH%
set PATH=%JAVA_HOME%\bin;%PATH%
set relativePath=%~dp0\out\production\Ecosystem

echo Launching the Ecosystem program using the JDK at path %JAVA_HOME%
java -cp %relativePath% Ecosystem

pause
endlocal