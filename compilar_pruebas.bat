@echo off
REM ============================================================
REM  compilar_pruebas.bat
REM  Compila las fuentes principales + las pruebas unitarias
REM ============================================================
setlocal enabledelayedexpansion

set SCRIPT_DIR=%~dp0
cd /d "%SCRIPT_DIR%"

call "%SCRIPT_DIR%set_javafx.bat" || exit /b 1

REM Classpath: SQLite + JUnit + Hamcrest (para compilar los tests)
set "TEST_JARS=%SCRIPT_DIR%lib\test\sqlite-jdbc-3.44.0.0.jar;%SCRIPT_DIR%lib\test\junit-4.13.2.jar;%SCRIPT_DIR%lib\test\hamcrest-core-1.3.jar"

REM Crear carpeta bin si no existe
if not exist bin mkdir bin

echo.
echo [*] Compilando fuentes principales...
javac --module-path "%JAVAFX_LIB%" --add-modules javafx.controls,javafx.fxml ^
      -d bin -encoding UTF-8 ^
      -cp "%TEST_JARS%" ^
      src\main\java\app\*.java

if %ERRORLEVEL% NEQ 0 (
    echo [X] Error compilando fuentes principales
    exit /b 1
)
echo [OK] Fuentes principales compiladas

echo.
echo [*] Compilando pruebas unitarias...
javac --module-path "%JAVAFX_LIB%" --add-modules javafx.controls,javafx.fxml ^
      -d bin -encoding UTF-8 ^
      -cp "bin;%TEST_JARS%" ^
      src\test\java\app\InventoryManagerTest.java

if %ERRORLEVEL% NEQ 0 (
    echo [X] Error compilando pruebas
    exit /b 1
)
echo [OK] Pruebas compiladas

echo.
echo [OK] Compilacion completada. Ejecuta: ejecutar_pruebas.bat
endlocal
