@echo off
set APP_HOME=%~dp0
set JAR=%APP_HOME%gradle\wrapper\gradle-wrapper.jar
if not exist "%JAR%" (
  echo gradle-wrapper.jar is missing. Open the project in Android Studio or regenerate the wrapper with: gradle wrapper
  exit /b 1
)
java -jar "%JAR%" %*
