# INSTRUCCIONES DE USO - ChatFX Fase 2

## 🎯 Resumen de Cambios - Fase 2

Esta es la **Fase 2** del proyecto ChatFX. Se ha transformado de un simple chat en un **sistema de transacciones bancarias con protocolo de tramas estructurado**.

### ✨ Nuevas Características
- ✅ Protocolo de comunicación estructurado con tramas
- ✅ Operaciones bancarias (Depósitos, Retiros, Consultas, Transferencias)
- ✅ Validación completa de datos
- ✅ Manejo avanzado de errores
- ✅ Base de datos simulada de cuentas
- ✅ Auditoría de transacciones

## Estructura del Proyecto

```
ChatFX/
├── src/main/java/
│   ├── app/
│   │   └── App.java                          # Punto de entrada principal
│   ├── client/
│   │   ├── Cliente.java                      # Lógica del cliente (ACTUALIZADO)
│   │   └── ClienteGUI.java                   # GUI del cliente (ACTUALIZADO)
│   ├── server/
│   │   ├── Servidor.java                     # Servidor central (ACTUALIZADO)
│   │   └── ServidorGUI.java                  # GUI Servidor (ACTUALIZADO)
│   └── protocol/                             # ⭐ NUEVO PAQUETE
│       ├── Frame.java                        # Representación de tramas
│       ├── FrameParser.java                  # Parser y validación
│       └── TransactionProcessor.java         # Procesador de operaciones
├── bin/                                      # Archivos compilados
├── lib/                                      # Librerías JavaFX
├── resources/
│   └── styles.css                            # Estilos CSS
├── PROTOCOLO.md                              # ⭐ NUEVO - Especificación del protocolo
├── GUIA_RAPIDA.md                            # ⭐ NUEVO - Guía de inicio rápido
├── README_FASE2.md                           # ⭐ NUEVO - Documentación completa
├── INSTRUCCIONES.md                          # Este archivo
└── README.md                                 # Documentación original
```

## 🚀 Cómo Usar - Paso a Paso

### 1. Compilar el Proyecto (Primera vez)

```bash
cd ChatFX
javac -d bin src/main/java/**/*.java
```

### 2. Ejecutar el Servidor

```bash
java -cp bin server.ServidorGUI
```

**El servidor:**
- ✅ Se inicia automáticamente
- ✅ Escucha en puerto 12345
- ✅ Crea el procesador de transacciones
- ✅ Muestra log de operaciones en tiempo real
- ✅ Visualiza estado de cuentas bancarias

### 3. Ejecutar Clientes

```bash
java -cp bin client.ClienteGUI
```

Puedes abrir múltiples ventanas de cliente (pestañas independientes)

---

## 📋 Operaciones Disponibles

### 1. DEPÓSITO
- **Parámetros:** Cuenta, Monto
- **Validaciones:** Cuenta debe existir, Monto > 0
- **Resultado:** Nuevo saldo de la cuenta
- **Ejemplo:**
  ```
  Cuenta: 12345
  Monto: 500
  Resultado: Saldo anterior $5,000 → Nuevo saldo $5,500
  ```

### 2. RETIRO  
- **Parámetros:** Cuenta, Monto
- **Validaciones:** Cuenta existe, Monto > 0, Saldo suficiente
- **Resultado:** Nuevo saldo de la cuenta
- **Ejemplo:**
  ```
  Cuenta: 12345
  Monto: 200
  Resultado: Saldo anterior $5,500 → Nuevo saldo $5,300
  ```

### 3. CONSULTA
- **Parámetros:** Cuenta
- **Validaciones:** Cuenta debe existir
- **Resultado:** Titular y saldo actual
- **Ejemplo:**
  ```
  Cuenta: 12345
  Resultado: Juan Pérez - Saldo: $5,300
  ```

### 4. TRANSFERENCIA
- **Parámetros:** Cuenta Origen, Cuenta Destino, Monto
- **Validaciones:** Ambas cuentas existen, Saldo suficiente, Cuentas diferentes
- **Resultado:** Nuevos saldos en ambas cuentas
- **Ejemplo:**
  ```
  Origen: 12345 (Saldo: $5,300)
  Destino: 54321 (Saldo: $3,500)
  Monto: 500
  Resultado:
    Origen: $4,800
    Destino: $4,000
  ```

---

## 💰 Cuentas de Prueba Predefinidas

El sistema viene con 3 cuentas listas para usar:

```
┌──────────┬──────────────┬────────────┐
│ Cuenta   │ Titular      │ Saldo      │
├──────────┼──────────────┼────────────┤
│ 12345    │ Juan Pérez   │ $5,000.00  │
│ 54321    │ María García │ $3,500.00  │
│ 99999    │ Carlos López │ $10,000.00 │
└──────────┴──────────────┴────────────┘
```

**Úsalas en tus pruebas:**
- Deposita en 12345
- Retira de 54321
- Transfiere entre ellas

---

## 🔄 Protocolo de Comunicación

El sistema usa un protocolo de **tramas estructuradas**.

### Formato Básico
```
<INICIO>|TIPO:operacion|CAMPO1:valor1|CAMPO2:valor2|FIN
```

### Ejemplos Reales

**Solicitud de Depósito:**
```
<INICIO>|TIPO:DEPOSITO|CUENTA:12345|MONTO:500.00|FIN
```

**Respuesta del Servidor (Éxito):**
```
<INICIO>|TIPO:RESPUESTA|ESTADO:OK|MENSAJE:Depósito realizado exitosamente|CUENTA:12345|MONTO_DEPOSITADO:500.00|SALDO_ANTERIOR:5000.00|SALDO_NUEVO:5500.00|CODIGO:OPERACION_EXITOSA|FIN
```

**Respuesta del Servidor (Error):**
```
<INICIO>|TIPO:RESPUESTA|ESTADO:ERROR|MENSAJE:Saldo insuficiente|CODIGO:SALDO_INSUFICIENTE|FIN
```

Para más detalles, ver [PROTOCOLO.md](PROTOCOLO.md)

---

## 🖥️ Interfaz del Cliente

```
┌─────────────────────────────────────────────────────────┐
│ CONEXIÓN                                                 │
│ Nombre: [Usuario1]  IP: [localhost]  Puerto: [12345]   │
│ [Conectar] [Desconectar]        Estado: Conectado      │
├─────────────────────────────────────────────────────────┤
│ CHAT                              │ OPERACIONES BANCARIAS│
│                                   │                      │
│ [Mensajes recibidos...]           │ Cuenta: [12345]     │
│                                   │ Monto:  [500.00]    │
│                                   │                      │
│ [RESPUESTA] ✓ Éxito              │ [DEPÓSITO] [RETIRO] │
│ [RESPUESTA] ✗ Error              │ [CONSULTA]          │
│                                   │                      │
│                                   │ Cuentas Disponibles:│
│                                   │ • 12345 - Juan P.   │
│                                   │ • 54321 - María G.  │
│                                   │ • 99999 - Carlos L. │
├─────────────────────────────────────────────────────────┤
│ ENVÍO                                                    │
│ [Escribe mensaje...]                            [Enviar]│
└─────────────────────────────────────────────────────────┘
```

---

## 🖥️ Interfaz del Servidor

```
┌─────────────────────────────────────────────────────────┐
│ [Iniciar] [Detener] [Abrir Cliente] [Limpiar] Estado:  │
├─────────────────────────────────────────────────────────┤
│                     │ Clientes    │ Cuentas Bancarias  │
│ LOG DE TRANSACCIONES│ Conectados  │                     │
│                     │             │ Cuenta: 12345       │
│[INFO] Escuchando... │ Total: 1    │ Titular: Juan P.    │
│[CONEXIÓN] Usuario1  │ • Usuario1  │ Saldo: $5,500.00   │
│[TRANSACCION] Depó.  │             │                     │
│[DESCONEXIÓN] User1  │ [Actualizar]│ Cuenta: 54321       │
│                     │             │ Titular: María G.   │
│                     │             │ Saldo: $3,500.00   │
│                     │             │                     │
│                     │             │ Cuenta: 99999       │
│                     │             │ Titular: Carlos L.  │
│                     │             │ Saldo: $10,000.00  │
└─────────────────────────────────────────────────────────┘
```

---

## 🔍 Ejemplos de Uso

### Ejemplo 1: Depósito Exitoso

**Cliente:**
1. Ingresa Cuenta: `12345`
2. Ingresa Monto: `500`
3. Haz clic en `[DEPÓSITO]`

**Resultado en Cliente:**
```
[RESPUESTA] ✓ Depósito realizado exitosamente | Saldo: $5,500.00
```

**Resultado en Servidor:**
```
[TRANSACCION] Usuario: Usuario1 | Tipo: DEPOSITO | Estado: OK | Saldo: 5500.00
```

---

### Ejemplo 2: Error - Saldo Insuficiente

**Cliente:**
1. Ingresa Cuenta: `54321` (María tiene $3,500)
2. Ingresa Monto: `5000`
3. Haz clic en `[RETIRO]`

**Resultado:**
```
[RESPUESTA] ✗ Saldo insuficiente [SALDO_INSUFICIENTE]
Disponible: $3,500.00
Solicitado: $5,000.00
```

---

### Ejemplo 3: Consulta de Saldo

**Cliente:**
1. Ingresa Cuenta: `99999`
2. Haz clic en `[CONSULTA]`

**Resultado:**
```
[RESPUESTA] ✓ Consulta realizada exitosamente | Saldo: $10,000.00
```

---

## ❌ Troubleshooting

| Problema | Causa | Solución |
|----------|-------|----------|
| "Connection refused" | Servidor no está corriendo | Ejecuta `java -cp bin server.ServidorGUI` |
| "Trama mal formada" | Formato de protocolo incorrecto | Verifica que incluya `<INICIO>` y `\|FIN` |
| "Cuenta no encontrada" | Número de cuenta inválido | Usa 12345, 54321 o 99999 |
| "Monto debe ser > 0" | Monto negativo | Ingresa un número positivo |
| "Saldo insuficiente" | No hay dinero suficiente | Consulta el saldo primero |

---

## 📚 Documentación Relacionada

- [PROTOCOLO.md](PROTOCOLO.md) - Especificación completa del protocolo
- [GUIA_RAPIDA.md](GUIA_RAPIDA.md) - Guía de inicio rápido (5 minutos)
- [README_FASE2.md](README_FASE2.md) - Documentación detallada de Fase 2
- [README.md](README.md) - Información general del proyecto

---

## 🔒 Notas Importantes

⚠️ **Esto es un sistema SIMULADO con propósitos educativos**

En un sistema real, se necesitaría:
- 🔐 Autenticación y contraseñas
- 🔒 Encriptación SSL/TLS
- 📋 Base de datos persistente
- 📊 Auditoría completa
- ⏱️ Rate limiting

---

## 🎓 Objetivos Educativos Cubiertos

✅ Arquitectura cliente-servidor  
✅ Comunicación por sockets  
✅ Protocolos de aplicación  
✅ Validación de datos  
✅ Manejo de excepciones  
✅ Concurrencia con threads  
✅ Interfaz gráfica JavaFX  
✅ Diseño modular del código  

---

**Versión:** 2.0 (Fase 2)  
**Fecha:** Enero 2025  
**Estado:** Completo ✓

¡Felicidades! Ahora tienes un sistema bancario funcional.



### 5. **Manejo de Conexiones Robusto**
   - Try-catch apropiados
   - Cierres de recursos correctos
   - Detección de desconexiones

## Cómo Ejecutar

### Opción 1: Usar los Scripts (Windows)

#### Panel Principal
```batch
run.bat
```
Esto abrirá la ventana principal desde donde puedes:
- Iniciar el Servidor (con GUI)
- Iniciar Clientes (múltiples instancias)

#### Servidor (Consola - Sin GUI)
```batch
runServidor.bat
```
Para pruebas rápidas sin interfaz gráfica

#### Servidor (Con GUI)
```batch
runServidorApp.bat
```
Servidor con interfaz gráfica para monitoreo

#### Cliente
```batch
runCliente.bat
```
Inicia un cliente. Puedes abrir múltiples ventanas.

### Opción 2: Compilar Manualmente

```batch
cd C:\Users\odchalic\Documents\Project\ChatFX
javac -d bin -cp "lib/*" src\main\java\app\*.java src\main\java\cliente\*.java src\main\java\servidor\*.java
```

Luego ejecutar:
```batch
java -cp bin;lib\* app.App
```

## Flujo de Operación

1. **Inicia el Servidor**
   - Haz clic en "Iniciar Servidor" en el panel principal
   - O ejecuta `runServidorApp.bat`

2. **Conecta Clientes**
   - Haz clic en "Iniciar Cliente" (múltiples veces para varios clientes)
   - O ejecuta `runCliente.bat` en diferentes terminales

3. **Ingresa tu Nombre**
   - Cada cliente debe ingresar un nombre único

4. **Chatea**
   - Escribe mensajes en el campo de texto
   - Presiona Enter o haz clic en "Enviar"
   - Todos los clientes connecados reciben el mensaje

5. **Cierra la Sesión**
   - Cierra la ventana para desconectarte

## Características

✅ **Chat en Tiempo Real**
- Sincronización instantánea de mensajes

✅ **Múltiples Clientes**
- Soporta conexiones simultáneas

✅ **Identificación**
- Cada usuario se identifica por nombre

✅ **Interfaz Intuitiva**
- Botones y campos de entrada claros
- Área de chat con historial

✅ **Servidor Robusto**
- Maneja múltiples conexiones
- Sistema de log detallado

✅ **Código de Calidad**
- Bien estructurado
- Fácil de mantener y extender

## Solución de Problemas

### "Puerto ya en uso"
- El puerto 12345 está en uso
- Solución: Cambiar el puerto en las clases (PUERTO = 12345)

### "Conexión rechazada"
- El servidor no está iniciado
- Solución: Inicia el servidor primero

### Problemas con CSS
- El archivo styles.css no se encuentra en resources/
- Solución: Verifica que exista en resources/styles.css

### Errores de compilación
- Verifica que JavaFX esté en la carpeta lib/
- Asegúrate de compilar con -cp "lib/*"

## Arquitectura del Sistema

```
┌─────────────────┐
│   Aplicación    │
│   Principal     │
│   (app.App)     │
└────────┬────────┘
         │
    ┌────┴────┐
    │          │
┌───▼──┐  ┌───▼──┐
│Panel │  │Panel │
│Serv. │  │Cli.  │
└───┬──┘  └───┬──┘
    │         │
┌───▼──┐  ┌───▼───────┐
│Serv. │  │Clienteapp │
│App   │  └───┬───────┘
└───┬──┘      │
    │    ┌────▼──────┐
    │    │  Cliente  │
    │    └────┬──────┘
    │         │
┌───▼─────────▼───┐
│ Puerto 12345    │
│ (Conexión TCP)  │
└─────────────────┘
```

## Datos de Protocolo

**Primer Mensaje**: Nombre del cliente (String)
**Mensajes Posteriores**: Contenido del chat (String)

Ejemplo:
```
Cliente A conecta -> "ChatA"
Cliente B conecta -> "ChatB"
ClienteA envía   -> "Hola a todos!"
```

## Prueba de Funcionalidad

1. Abre Panel Principal (`run.bat`)
2. Haz clic en "Iniciar Servidor"
3. Espera a que diga "Servidor iniciado en puerto 12345"
4. Abre otra terminal y ejecuta `runCliente.bat`
5. Ingresa nombre: "ChatA"
6. Abre otra terminal más y ejecuta `runCliente.bat`
7. Ingresa nombre: "ChatB"
8. En ChatA escribe: "Hola ChatB!"
9. Verás el mensaje en ambas ventanas del cliente y en el servidor

¡Listo! Tu sistema de chat está funcionando correctamente.
