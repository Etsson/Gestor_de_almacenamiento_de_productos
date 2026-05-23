# ChatFX - Sistema de Chat en Tiempo Real

Un sistema de chat multi-cliente en Java con JavaFX.

## Características

- Servidor centralizado con interfaz gráfica
- Múltiples clientes conectados simultáneamente
- Mensajería en tiempo real
- 🔐 **Autenticación con usuario y contraseña (SQLite)** ✨ NUEVO
- **Registro de nuevos usuarios** ✨ NUEVO
- Interfaz moderna con JavaFX
- Threading seguro y gestión de errores

## Estructura

**/src/main/java/**
- **app/App.java** - Punto de entrada principal
- **app/LoginGUI.java** - Pantalla de login/registro *(NUEVO)*
- **app/UsuarioManager.java** - Gestión de autenticación *(NUEVO)*
- **app/DatabaseInit.java** - Inicialización BD SQLite *(NUEVO)*
- **server/Servidor.java** - Servidor sin GUI
- **server/ServidorGUI.java** - Servidor con interfaz JavaFX
- **client/Cliente.java** - Lógica del cliente
- **client/ClienteGUI.java** - Cliente con interfaz JavaFX

## Requisitos

- Java JDK 11+
- JavaFX SDK
- SQLite JDBC Driver (incluido en `lib/`)

## Compilación y Ejecución

### Compilar:
```bash
# Opción 1: Usar script (Windows)
compile.bat

# Opción 2: Comando manual
javac -d bin -cp lib\* ^
  src/main/java/app/*.java ^
  src/main/java/server/*.java ^
  src/main/java/client/*.java ^
  src/main/java/protocol/*.java
```

### Ejecutar:
```bash
java --module-path lib --add-modules javafx.controls,javafx.fxml ^
  -cp bin;lib\* app.App
```

## Uso

1. **Ejecuta la aplicación** → Se abre la pantalla de Login
2. **¿Nuevo usuario?** → Haz clic en "Registrarse"
   - Ingresa usuario y contraseña
   - Confirma la contraseña
   - Haz clic en "Registrarse"
3. **¿Usuario existente?** → Inicia sesión
   - Ingresa usuario y contraseña
   - Haz clic en "Iniciar Sesión"
4. **Tras autenticarte** → Se abre ClienteGUI
   - Tu usuario ya está pre-llenado y protegido
   - Conecta al servidor (IP: localhost, Puerto: 12345)
   - ¡Comienza a chatear!

## Puertos

- Servidor escucha en puerto **12345**

## Documentación adicional

- **[SETUP_LOGIN.md](./SETUP_LOGIN.md)** - Instrucciones de instalación detalladas
- **[LOGIN_IMPLEMENTADO.md](./LOGIN_IMPLEMENTADO.md)** - Detalles de cambios y características
- **[VERIFICACION.md](./VERIFICACION.md)** - Checklist de implementación

## Base de datos

La base de datos SQLite se crea automáticamente en:
```
./chatfx.db
```

### Estructura de usuarios:
```sql
CREATE TABLE usuarios (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  usuario TEXT UNIQUE NOT NULL,
  contraseña TEXT NOT NULL,
  email TEXT,
  fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)
```

Las contraseñas se almacenan hasheadas con SHA-256 por seguridad.

## Cambios recientes (v2.0)

✅ Autenticación con usuario y contraseña  
✅ Base de datos SQLite persistente  
✅ Registro de nuevos usuarios  
✅ Hash seguro de contraseñas (SHA-256)  
✅ Interfaz de login mejorada  
✅ Validación de campos  

## Próximos pasos (planificados)

- [ ] Validación de email
- [ ] Recuperación de contraseña
- [ ] Autenticación en servidor
- [ ] BCrypt para contraseñas (más seguro que SHA-256)
- [ ] Sistema de roles y permisos
