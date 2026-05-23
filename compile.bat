@echo off
REM Script de compilación para ChatFX con Login

setlocal enabledelayedexpansion

set SCRIPT_DIR=%~dp0
cd /d "%SCRIPT_DIR%"

call "%SCRIPT_DIR%set_javafx.bat" || exit /b 1

set "LIB_CP=%SCRIPT_DIR%lib\*"

REM Crear carpeta bin si no existe
if not exist bin mkdir bin

REM Compilar todas las clases del proyecto
 echo [*] Compilando Mercado con sistema de inventario...

 javac --module-path "%JAVAFX_LIB%" --add-modules javafx.controls,javafx.fxml -d bin -cp "%LIB_CP%" ^
   src\main\java\app\*.java

if %ERRORLEVEL% EQU 0 (
    echo [✓] Compilación exitosa!
    echo.
    echo [*] Para ejecutar:
    echo     run.bat
    echo.
    echo [*] O bien:
    echo     java --module-path lib --add-modules javafx.controls,javafx.fxml -cp bin;lib\* app.App
) else (
    echo [✗] Error en la compilación
    exit /b 1
)

endlocal
