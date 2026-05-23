# Dependencias Requeridas para ChatFX con Login

## SQLite JDBC Driver

Para que el sistema de login funcione, necesitas descargar el driver SQLite JDBC.

### Opción 1: Descargar manual
1. Ve a: https://github.com/xerial/sqlite-jdbc/releases
2. Descarga: `sqlite-jdbc-3.44.0.0.jar` (o la versión más reciente)
3. Coloca el archivo en la carpeta: `lib/`

### Opción 2: Usar Maven (si tienes Maven instalado)
Ejecuta en la carpeta del proyecto:
```bash
mvn dependency:copy-dependencies -DoutputDirectory=lib
```

### Opción 3: Comando directo
```bash
cd lib
curl -L -o sqlite-jdbc-3.44.0.0.jar https://github.com/xerial/sqlite-jdbc/releases/download/3.44.0.0/sqlite-jdbc-3.44.0.0.jar
```

## Compilación

Una vez tengas el JAR de SQLite, compila con:

```bash
javac -d bin -cp lib/* src/main/java/app/*.java src/main/java/server/*.java src/main/java/client/*.java src/main/java/protocol/*.java
```

## Ejecución

```bash
java --module-path lib --add-modules javafx.controls,javafx.fxml -cp bin:lib/* app.App
```

## Estructura actualizada

Después de agregar el login:

```
src/main/java/
├── app/
│   ├── App.java                  (modificado - abre LoginGUI)
│   ├── DatabaseInit.java         (nuevo)
│   ├── UsuarioManager.java       (nuevo)
│   └── LoginGUI.java             (nuevo)
├── client/
│   ├── ClienteGUI.java           (modificado - acepta usuario autenticado)
│   └── Cliente.java
├── server/
│   ├── ServidorGUI.java
│   └── Servidor.java
└── protocol/
    ├── Frame.java
    ├── FrameParser.java
    └── TransactionProcessor.java
```

## Características del Login

✅ Base de datos SQLite persistente
✅ Registro de nuevos usuarios
✅ Login con validación de contraseña
✅ Hash SHA-256 de contraseñas
✅ Integración con ClienteGUI

Después del login exitoso:
- El usuario se conecta automáticamente al chat
- Su nombre de usuario está pre-llenado y no puede ser modificado
