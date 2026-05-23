@echo off
setlocal enabledelayedexpansion

set SCRIPT_DIR=%~dp0
cd /d "%SCRIPT_DIR%"

call "%SCRIPT_DIR%set_javafx.bat" || exit /b 1

set "LIB_CP=%SCRIPT_DIR%lib\*"

echo [*] Compilando ChatFX con Login...
echo.

REM Crear carpeta bin si no existe
if not exist bin mkdir bin

REM Limpiar compilaciones anteriores
del /Q bin\*.class 2>nul

REM Compilar todas las clases
echo [*] Compilando clases de la aplicación...
javac --module-path "%JAVAFX_LIB%" --add-modules javafx.controls,javafx.fxml -d bin -encoding UTF-8 -cp "%LIB_CP%" ^
  src\main\java\app\*.java

if %ERRORLEVEL% NEQ 0 (
    echo [✗] Error compilando app
    exit /b 1
)
echo [✓] OK

echo.
echo [✓] Compilacion completada exitosamente!
echo.
echo Archivos compilados: bin\
echo.
dir /B bin\app\*.class 2>nul | find /c ".class" > nul && (
    echo [✓] Clases compiladas:
    dir /B bin\app\*.class
)

endlocal
