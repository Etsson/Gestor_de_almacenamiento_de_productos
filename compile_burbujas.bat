@echo off
setlocal enabledelayedexpansion

set SCRIPT_DIR=%~dp0
cd /d "%SCRIPT_DIR%"

call "%SCRIPT_DIR%set_javafx.bat" || exit /b 1

set "LIB_CP=%SCRIPT_DIR%lib\*"

REM Crear carpeta bin si no existe
if not exist bin mkdir bin

echo [*] Compilando el sistema de inventario...
echo.

echo [*] Compilando...
javac --module-path "%JAVAFX_LIB%" --add-modules javafx.controls,javafx.fxml -d bin -encoding UTF-8 -cp "%LIB_CP%" ^
  src\main\java\app\*.java 2>&1 | head -50

if %ERRORLEVEL% EQU 0 (
    echo.
    echo [✓] Compilación exitosa!
    echo [✓] Los mensajes ahora aparecerán como burbujas celestes
) else (
    echo.
    echo [✗] Error en la compilación
    exit /b 1
)

endlocal
