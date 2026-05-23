# 🎯 COMENZAR AQUÍ - Instrucciones Rápidas

## Paso 1: Descargar dependencia

Ejecuta el script:
```bash
descargar_sqlite.bat
```

O descarga manualmente:
- Ve a: https://github.com/xerial/sqlite-jdbc/releases/download/3.44.0.0/sqlite-jdbc-3.44.0.0.jar
- Coloca el archivo en: `lib/sqlite-jdbc-3.44.0.0.jar`

## Paso 2: Compilar

Ejecuta el script:
```bash
compile.bat
```

O comando manual:
```bash
javac -d bin -cp lib\* src\main\java\app\*.java src\main\java\server\*.java src\main\java\client\*.java src\main\java\protocol\*.java
```

## Paso 3: Ejecutar

```bash
java --module-path lib --add-modules javafx.controls,javafx.fxml -cp bin;lib\* app.App
```

## Paso 4: Usar la aplicación

1. **Primer usuario:**
   - Clic en "Registrarse"
   - Usuario: `usuario1`
   - Contraseña: `123456`
   - Confirmar: `123456`
   - Clic en "Registrarse"

2. **Iniciar sesión:**
   - Usuario: `usuario1`
   - Contraseña: `123456`
   - Clic en "Iniciar Sesión"

3. **En el Chat:**
   - IP: `localhost`
   - Puerto: `12345`
   - Clic en "Conectar"

---

## 📚 Documentación

- **[README_NUEVO.md](./README_NUEVO.md)** - Visión general del proyecto
- **[SETUP_LOGIN.md](./SETUP_LOGIN.md)** - Instrucciones de instalación
- **[LOGIN_IMPLEMENTADO.md](./LOGIN_IMPLEMENTADO.md)** - Características del login
- **[RESUMEN_IMPLEMENTACION.md](./RESUMEN_IMPLEMENTACION.md)** - Resumen completo
- **[VERIFICACION.md](./VERIFICACION.md)** - Checklist de verificación

---

## 🆘 Troubleshooting

### Error: "Driver SQLite no encontrado"
- Ejecuta: `descargar_sqlite.bat`
- Verifica que `lib/sqlite-jdbc-3.44.0.0.jar` exista

### Error: "No se encontró JavaFX"
- Verifica que tienes JavaFX SDK instalado
- Asegúrate que `lib/` contiene los archivos de JavaFX

### Error de compilación
- Limpia la carpeta `bin/` si existe
- Verifica que la ruta de los archivos es correcta
- Intenta recompilar con: `compile.bat`

### Error de conexión al servidor
- Verifica que el servidor está corriendo (puerto 12345)
- Usa IP: `localhost` para servidor local
- Usa IP: `127.0.0.1` como alternativa

---

## ✅ Checklist de Setup

- [ ] Descargar SQLite JDBC con `descargar_sqlite.bat`
- [ ] Compilar con `compile.bat`
- [ ] Ejecutar con el comando de Paso 3
- [ ] Registrar usuario de prueba
- [ ] Iniciar sesión
- [ ] Conectar al chat
- [ ] Probar mensajes

---

## 🔑 Características principales

✅ **Autenticación segura** con usuario y contraseña
✅ **Base de datos SQLite** persistente
✅ **Registro de usuarios** fácil
✅ **Hash SHA-256** para contraseñas
✅ **Interfaz moderna** con JavaFX
✅ **Chat en tiempo real** multi-usuario

---

¡Listo para comenzar! 🚀
