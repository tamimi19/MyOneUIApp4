@echo off
SETLOCAL

REM ------------------------------------------------------------------------------
REM Gradle start up script for Windows
REM ------------------------------------------------------------------------------

if not "%JAVA_HOME%" == "" goto findJavaFromJavaHome

set JavaFromPath=java
for %%i in ("%JavaFromPath%") do set JavaFromPath=%%~$PATH:i
if not "%JavaFromPath%" == "" goto haveJava

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.
echo.
exit /b 1

:findJavaFromJavaHome
if exist "%JAVA_HOME%\bin\java.exe" goto ok
echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.
echo.

:ok
set "APP_HOME=%~dp0%"
if exist "%APP_HOME%\gradle\wrapper\gradle-wrapper.jar" goto wrapper
echo.
echo ERROR: Gradle wrapper not found.
echo The system cannot find the file specified.
echo.
exit /b 1

:wrapper
"%JAVA_HOME%\bin\java" "-Dorg.gradle.appname=%~n0" "-classpath" "%APP_HOME%gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*
