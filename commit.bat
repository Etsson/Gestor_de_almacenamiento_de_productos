@echo off
cd /d "%~dp0"

git add -A

git commit -m "feat: Agregar sistema de login con usuario y contraseña

- Crear DatabaseInit.java para gestionar BD SQLite
- Crear UsuarioManager.java con autenticacion y hash SHA-256
- Crear LoginGUI.java con interfaz de login/registro
- Modificar App.java para mostrar LoginGUI primero
- Modificar ClienteGUI.java para aceptar usuario autenticado
- Agregar sqlite-jdbc como dependencia
- Agregar documentacion de setup

Co-authored-by: Copilot <223556219+Copilot@users.noreply.github.com>"

echo.
echo [✓] Cambios commitidos exitosamente
