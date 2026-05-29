@echo off
REM ============================================================
REM  ejecutar_pruebas.bat
REM  Corre todas las pruebas unitarias con JUnit 4 Console
REM ============================================================
setlocal enabledelayedexpansion

set SCRIPT_DIR=%~dp0
cd /d "%SCRIPT_DIR%"

call "%SCRIPT_DIR%set_javafx.bat" || exit /b 1

set "TEST_JARS=%SCRIPT_DIR%lib\test\sqlite-jdbc-3.44.0.0.jar;%SCRIPT_DIR%lib\test\junit-4.13.2.jar;%SCRIPT_DIR%lib\test\hamcrest-core-1.3.jar"

echo.
echo ============================================================
echo  EJECUTANDO PRUEBAS UNITARIAS - Gestor de Inventario
echo ============================================================
echo.

java --module-path "%JAVAFX_LIB%" --add-modules javafx.controls,javafx.fxml ^
     -cp "bin;%TEST_JARS%" ^
     org.junit.runner.JUnitCore app.InventoryManagerTest

echo.
echo ============================================================
echo  Pruebas finalizadas
echo ============================================================
endlocal
