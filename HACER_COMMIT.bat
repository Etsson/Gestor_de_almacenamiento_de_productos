@echo off
echo [*] Haciendo commit de la implementacion del login...
echo.

cd /d "%~dp0"

echo [1] Agregando archivos...
git add -A

echo [2] Creando commit...
git commit -m "feat: Sistema de login completamente implementado

Cambios realizados:
- Agregar DatabaseInit.java para gestionar BD SQLite
- Agregar UsuarioManager.java con autenticacion SHA-256
- Agregar LoginGUI.java con interfaz de login/registro
- Modificar App.java para abrir LoginGUI primero
- Modificar ClienteGUI.java para aceptar usuario autenticado
- Agregar scripts de compilacion y descarga
- Agregar documentacion completa

Caracteristicas:
- Base de datos SQLite persistente
- Hash SHA-256 para contraseñas
- Registro y login funcional
- Validacion de campos
- Usuario autenticado en ClienteGUI

Co-authored-by: Copilot <223556219+Copilot@users.noreply.github.com>"

echo.
if %ERRORLEVEL% EQU 0 (
    echo [+] Commit realizado exitosamente
) else (
    echo [-] Error en el commit (puede ser normal si no hay cambios)
)

echo.
echo [*] Estado del repositorio:
git status --short

