@echo off
setlocal enabledelayedexpansion

set "SCRIPT_DIR=%~dp0"
cd /d "%SCRIPT_DIR%"

if defined JAVAFX_LIB (
  if exist "%JAVAFX_LIB%\javafx.base.jar" (
    echo [i] JAVAFX_LIB already set to %JAVAFX_LIB%
    endlocal & set "JAVAFX_LIB=%JAVAFX_LIB%"
    exit /b 0
  )
)

for %%P in (
  "%SCRIPT_DIR%lib"
  "%UserProfile%\Downloads\openjfx-26.0.1_windows-x64_bin-sdk\javafx-sdk-26.0.1\lib"
  "%UserProfile%\OneDrive\Escritorio\ChatJFX version2\Projectos-JFX\ChatFX\lib"
  "%UserProfile%\OneDrive\Escritorio\Projectos-JFX\ChatFX\lib"
  "C:\javafx-sdk-23.0.2\lib"
  "C:\javafx-sdk-22.0.2\lib"
  "C:\javafx-sdk-21.0.0\lib"
  "%ProgramFiles%\Java\javafx-sdk\lib"
  "%ProgramFiles(x86)%\Java\javafx-sdk\lib"
) do (
  if exist "%%~P\javafx.base.jar" (
    echo [i] Usando JavaFX SDK en %%~P
    endlocal & set "JAVAFX_LIB=%%~P"
    exit /b 0
  )
)

for /d %%D in ("%UserProfile%\Downloads\*javafx*","%UserProfile%\OneDrive\Escritorio\*javafx*") do (
  if exist "%%~D\lib\javafx.base.jar" (
    echo [i] Usando JavaFX SDK en %%~D\lib
    endlocal & set "JAVAFX_LIB=%%~D\lib"
    exit /b 0
  )
)

echo [!] No se encontró JavaFX en rutas conocidas.
echo     Copia el SDK de JavaFX en "lib\" o define JAVAFX_LIB.
echo.
echo Ejemplo:
echo     set JAVAFX_LIB=C:\Users\dyang\Downloads\openjfx-26.0.1_windows-x64_bin-sdk\javafx-sdk-26.0.1\lib
exit /b 1
