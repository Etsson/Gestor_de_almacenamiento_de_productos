@echo off
setlocal enabledelayedexpansion

set "SCRIPT_DIR=%~dp0"
cd /d "%SCRIPT_DIR%"

call "%SCRIPT_DIR%set_javafx.bat" || exit /b 1

set "LIB_CP=%SCRIPT_DIR%bin;%SCRIPT_DIR%lib\*"

echo [*] Ejecutando app.App con JavaFX desde %JAVAFX_LIB%
java --enable-native-access=ALL-UNNAMED --module-path "%JAVAFX_LIB%" --add-modules javafx.controls,javafx.fxml -cp "%SCRIPT_DIR%bin;%SCRIPT_DIR%lib\*" app.App
