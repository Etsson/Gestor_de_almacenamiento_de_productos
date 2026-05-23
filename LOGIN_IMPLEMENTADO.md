# ✅ Login implementado en ChatFX

## Resumen de cambios

Se ha agregado un sistema completo de **autenticación con usuario y contraseña** usando **SQLite**.

### Archivos creados:

#### 1. **DatabaseInit.java** (app package)
- Inicializa la base de datos SQLite
- Crea tabla `usuarios` con campos: id, usuario, contraseña, email
- Gestiona la conexión a la BD

#### 2. **UsuarioManager.java** (app package)
- Singleton para gestionar autenticación
- Métodos:
  - `login(usuario, contraseña)` - Valida credenciales
  - `registrar(usuario, contraseña, email)` - Crea nuevo usuario
  - `usuarioExiste(usuario)` - Verifica disponibilidad
- Hash SHA-256 para contraseñas

#### 3. **LoginGUI.java** (app package)
- Pantalla JavaFX de login/registro
- Interfaz limpia y moderna
- Funciones:
  - ✅ Iniciar sesión con validación
  - ✅ Registrar nuevo usuario
  - ✅ Confirmación de contraseña en registro
  - ✅ Mensajes de error/éxito

### Archivos modificados:

#### 1. **App.java**
- Cambiado para abrir LoginGUI primero
- Ya no muestra menú de selección servidor/cliente
- Cierra la BD al salir

#### 2. **ClienteGUI.java**
- Ahora acepta constructor con parámetro: `ClienteGUI(String usuario)`
- Usuario autenticado se pre-rellena en el campo
- Campo usuario es no-editable después del login
- Mantiene compatibilidad con constructor sin parámetros

### Dependencias requeridas:

```
lib/sqlite-jdbc-3.44.0.0.jar
```

Ya está descargado automáticamente.

## Flujo de uso:

1. 👤 Usuario ejecuta la app → Abre LoginGUI
2. 🔑 Usuario puede:
   - **Iniciar sesión** si ya tiene cuenta
   - **Registrarse** si es nuevo
3. ✅ Tras autenticarse → Se abre ClienteGUI automáticamente
4. 💬 Usuario ya está logueado y puede chatear

## Base de datos:

La BD se crea automáticamente en:
```
./chatfx.db
```

Estructura:
```sql
CREATE TABLE usuarios (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  usuario TEXT UNIQUE NOT NULL,
  contraseña TEXT NOT NULL,
  email TEXT,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)
```

## Compilación:

```bash
javac -d bin -cp lib\* ^
  src\main\java\app\*.java ^
  src\main\java\server\*.java ^
  src\main\java\client\*.java ^
  src\main\java\protocol\*.java
```

O usar el script:
```bash
compile.bat
```

## Ejecución:

```bash
java --module-path lib --add-modules javafx.controls,javafx.fxml -cp bin;lib\* app.App
```

## Características de seguridad:

✅ Contraseñas hasheadas con SHA-256
✅ Validación de usuario duplicado
✅ Confirmación de contraseña en registro
✅ Campos requeridos validados
✅ Sin almacenamiento de contraseñas en texto plano

## Próximos pasos opcionales:

- Agregar validación de email
- Implementar "Olvidé mi contraseña"
- Agregar autenticación en servidor
- Implementar tokens de sesión
- Agregar BCrypt en lugar de SHA-256 (más seguro)
