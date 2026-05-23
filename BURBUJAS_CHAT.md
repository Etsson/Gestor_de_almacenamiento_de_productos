# ✅ BURBUJAS DE CHAT - ESTILO WHATSAPP

## 🎨 Cambios realizados:

Se ha modificado la interfaz del chat para mostrar los mensajes como **burbujas estilo WhatsApp**:

### Características:
✅ **Fondo celeste** (#ADD8E6) - Color de WhatsApp
✅ **Letra tamaño 12** - Legible
✅ **Burbujas redondeadas** - Efecto visual profesional
✅ **Bordes redondeados** - Radio de 10 px
✅ **Alineación automática:**
   - Mensajes propios → Derecha
   - Mensajes de otros → Izquierda
✅ **Nombre del usuario** - Visible en mensajes recibidos
✅ **Padding interno** - 10 px para espaciado
✅ **Scroll automático** - Baja al último mensaje

---

## 📝 Cambios en ClienteGUI.java:

### 1. Nuevas variables:
```java
private VBox panelMensajes;        // Contenedor de burbujas
private ScrollPane scrollMensajes; // Para scroll automático
```

### 2. Nuevo método: `agregarMensajeBurbuja()`
```java
private void agregarMensajeBurbuja(String nombre, String mensaje, boolean esPropio)
```
- **nombre**: Quién envía el mensaje
- **mensaje**: Contenido del mensaje
- **esPropio**: true si lo envió el usuario actual, false si es de otros

### 3. Estilos de burbujas:
```
Color de fondo: #ADD8E6 (Celeste)
Tamaño letra: 12
Padding: 10 px
Border radius: 10 px
```

### 4. Métodos actualizados:
- `crearPanelChat()` - Usa VBox en lugar de TextArea
- `enviarMensaje()` - Usa `agregarMensajeBurbuja()`
- `onMensajeRecibido()` - Parsea y muestra con estilo
- `onTramaRecibida()` - Muestra respuestas del servidor
- `conectarAlServidor()` - Mensajes de información
- `conectarAutomatico()` - Conexión automática

---

## 🚀 Cómo usar:

### Compilar:
```bash
compile.bat
```

O con el nuevo script:
```bash
compile_burbujas.bat
```

### Ejecutar:
```bash
java --module-path lib --add-modules javafx.controls,javafx.fxml -cp bin;lib\* app.App
```

---

## 💬 Ejemplo visual:

```
┌─────────────────────────────────┐
│ INFO: Conectado como: usuario1  │  ← Izquierda (otros)
└─────────────────────────────────┘

                    ┌──────────────────┐
                    │ Hola cómo estás? │  ← Derecha (propio)
                    └──────────────────┘

┌──────────────────────┐
│ usuario2: Bien, gracias │  ← Izquierda con nombre
└──────────────────────┘
```

---

## 🔧 Personalización:

Para cambiar el color de las burbujas, edita la línea en `agregarMensajeBurbuja()`:

```java
"-fx-background-color: #ADD8E6;" // Cambia este color
```

Ejemplos:
- Verde claro: `#C8E6C9`
- Azul oscuro: `#1976D2`
- Gris: `#E0E0E0`
- Rosa: `#F48FB1`

---

## 📋 Checklist:

- [x] Burbujas con fondo celeste
- [x] Letra tamaño 12
- [x] Bordes redondeados
- [x] Alineación derecha/izquierda
- [x] Nombre del usuario visible
- [x] Scroll automático
- [x] Compatibilidad con login
- [x] Compatibilidad con servidor/cliente

---

¡Ahora tu chat se ve como **WhatsApp**! 🎉
