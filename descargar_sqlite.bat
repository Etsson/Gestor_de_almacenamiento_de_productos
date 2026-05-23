@echo off
echo [*] Descargando SQLite JDBC...

cd /d "%~dp0"

python descargar_sqlite.py

if exist "lib\sqlite-jdbc-3.44.0.0.jar" (
    echo.
    echo [✓] SQLite JDBC descargado exitosamente
) else (
    echo.
    echo [✗] Error: No se pudo descargar SQLite JDBC
    echo [*] Intenta descargar manualmente desde:
    echo     https://github.com/xerial/sqlite-jdbc/releases/download/3.44.0.0/sqlite-jdbc-3.44.0.0.jar
    echo [*] Y coloca el archivo en la carpeta: lib\
    pause
)
