# ChatFX - Sistema de Transacciones Bancarias en Tiempo Real

## 📱 Descripción General

**ChatFX** es una aplicación Java con arquitectura cliente-servidor que simula un sistema de chat integrado con operaciones bancarias. El sistema utiliza un protocolo de tramas estructurado para comunicación confiable entre clientes y servidor.

## ✨ Características Principales

### Fase 1: Chat Básico
- ✅ Comunicación en tiempo real entre múltiples clientes
- ✅ Servidor centralizado que gestiona conexiones
- ✅ Interfaz gráfica JavaFX para cliente y servidor

### Fase 2: Sistema de Protocolo y Transacciones Bancarias (NUEVO)
- ✅ **Protocolo de Tramas Estructurado**: Formato fijo para mensajes con validación
- ✅ **Depósitos**: Deposita dinero en cuentas
- ✅ **Retiros**: Retira dinero con validación de saldo
- ✅ **Consultas**: Consulta el saldo disponible
- ✅ **Transferencias**: Transfiere dinero entre cuentas
- ✅ **Control de Errores**: Manejo completo de errores y excepciones
- ✅ **Cuentas Predefinidas**: 3 cuentas de prueba con datos reales
- ✅ **Auditoría de Transacciones**: Registro de todas las operaciones

## 🏗️ Estructura del Protocolo

Todas las tramas siguen este formato:

```
<INICIO>|TIPO:operacion|CAMPO1:valor1|CAMPO2:valor2|FIN
```

### Ejemplo de Depósito:
```
<INICIO>|TIPO:DEPOSITO|CUENTA:12345|MONTO:500.00|FIN
```

### Ejemplo de Respuesta:
```
<INICIO>|TIPO:RESPUESTA|ESTADO:OK|MENSAJE:Depósito realizado exitosamente|SALDO:5500.00|FIN
```

## 🔄 Operaciones Soportadas

| Operación | Descripción | Código |
|-----------|-------------|--------|
| **DEPOSITO** | Depositar dinero en una cuenta | `TIPO:DEPOSITO` |
| **RETIRO** | Retirar dinero de una cuenta | `TIPO:RETIRO` |
| **CONSULTA** | Consultar saldo de una cuenta | `TIPO:CONSULTA` |
| **TRANSFERENCIA** | Transferir dinero entre cuentas | `TIPO:TRANSFERENCIA` |
| **REGISTRO** | Registrar un usuario | `TIPO:REGISTRO` |
| **RESPUESTA** | Respuesta del servidor a operaciones | `TIPO:RESPUESTA` |

## 💾 Cuentas de Prueba Predefinidas

```
┌─────────────┬──────────────┬────────────────┐
│ Número      │ Titular      │ Saldo Inicial  │
├─────────────┼──────────────┼────────────────┤
│ 12345       │ Juan Pérez   │ $5,000.00      │
│ 54321       │ María García │ $3,500.00      │
│ 99999       │ Carlos López │ $10,000.00     │
└─────────────┴──────────────┴────────────────┘
```

## 📁 Estructura del Proyecto

```
ChatFX/
├── src/
│   └── main/
│       └── java/
│           ├── app/
│           │   └── App.java                    # Aplicación principal
│           ├── client/
│           │   ├── Cliente.java                # Lógica de cliente (ACTUALIZADO)
│           │   └── ClienteGUI.java             # Interfaz gráfica cliente (ACTUALIZADO)
│           ├── server/
│           │   ├── Servidor.java               # Lógica de servidor (ACTUALIZADO)
│           │   └── ServidorGUI.java            # Interfaz gráfica servidor (ACTUALIZADO)
│           └── protocol/                       # NUEVO PAQUETE
│               ├── Frame.java                  # Representación de tramas
│               ├── FrameParser.java            # Parser de tramas
│               └── TransactionProcessor.java   # Procesador de transacciones
├── lib/
│   └── javafx.properties
├── resources/
│   └── styles.css
├── PROTOCOLO.md                                # NUEVO - Documentación del protocolo
├── README.md                                   # Este archivo
└── MANIFEST.MF
```

## 🚀 Cómo Usar

### 1. Compilar el Proyecto

```bash
cd ChatFX
javac -d bin src/main/java/**/*.java
```

### 2. Ejecutar el Servidor

```bash
java -cp bin server.ServidorGUI
```

El servidor se iniciará automáticamente y escuchará en puerto **12345**.

**Pantalla del Servidor mostrará:**
- 📊 Log de transacciones en tiempo real
- 👥 Lista de clientes conectados
- 💰 Estado actual de todas las cuentas bancarias

### 3. Ejecutar Clientes

```bash
java -cp bin client.ClienteGUI
```

**Características del Cliente:**
- 🔐 Campo para nombre de usuario
- 🌐 Campos para IP y puerto del servidor
- 💬 Área de chat para mensajes de texto
- 🏦 Panel de operaciones bancarias
- 🔢 Botones para Depósito, Retiro y Consulta
- 📱 Visualización de cuentas disponibles

## 📝 Ejemplos de Uso

### Ejemplo 1: Realizar un Depósito

```
1. Cliente ingresa nombre: "Usuario1"
2. Cliente conecta al servidor
3. Cliente ingresa cuenta: 12345
4. Cliente ingresa monto: 500.00
5. Cliente hace clic en botón "DEPÓSITO"

Servidor procesa:
- Valida que cuenta 12345 existe
- Valida que monto es > 0
- Suma 500.00 al saldo
- Responde al cliente con éxito

Cliente ve:
✓ Depósito realizado exitosamente | Saldo: $5,500.00
```

### Ejemplo 2: Retiro con Saldo Insuficiente

```
1. Cliente ingresa cuenta: 12345
2. Cliente ingresa monto: 10000.00
3. Cliente hace clic en botón "RETIRO"

Servidor procesa:
- Valida que cuenta existe
- Verifica saldo (disponible: 5500.00)
- Rechaza porque saldo < monto solicitado

Cliente ve:
✗ Saldo insuficiente [SALDO_INSUFICIENTE]
  Disponible: $5,500.00
  Solicitado: $10,000.00
```

### Ejemplo 3: Transferencia Entre Cuentas

```
1. Cliente ingresa cuenta origen: 12345
2. Cliente ingresa cuenta destino: 54321
3. Cliente ingresa monto: 1000.00
4. Cliente hace clic en "TRANSFERENCIA"

Servidor procesa:
- Valida ambas cuentas existen
- Valida saldo en origen
- Resta 1000 de 12345
- Suma 1000 a 54321

Cliente ve:
✓ Transferencia realizada exitosamente
  Origen: $4,500.00
  Destino: $4,500.00
```

## 🛡️ Validaciones y Control de Errores

### Validaciones en Cliente
- ✅ Campos no vacíos
- ✅ Números válidos
- ✅ Montos positivos
- ✅ Cuentas diferentes en transferencias

### Validaciones en Servidor
- ✅ Formato de trama correcto
- ✅ Campos requeridos presentes
- ✅ Cuenta existe en el sistema
- ✅ Saldo suficiente
- ✅ Montos válidos

### Códigos de Error
| Código | Significado |
|--------|-------------|
| `OPERACION_EXITOSA` | Operación completada |
| `CUENTA_NO_EXISTE` | Cuenta no encontrada |
| `SALDO_INSUFICIENTE` | Fondos insuficientes |
| `MONTO_INVALIDO` | Monto debe ser > 0 |
| `TRAMA_INVALIDA` | Formato de trama incorrecto |

## 🔌 Arquitectura de Comunicación

```
┌─────────────────┐                     ┌──────────────────┐
│                 │                     │                  │
│  CLIENTE 1      │  ──────────────►   │                  │
│  (ChatFX)       │                     │   SERVIDOR       │
│                 │  ◄──────────────    │   (Socket        │
└─────────────────┘                     │    12345)        │
                                        │                  │
┌─────────────────┐                     │  TransactionProc │
│                 │  ──────────────►    │                  │
│  CLIENTE 2      │  ──────────────►    │  - Depósitos     │
│  (ChatFX)       │  ◄──────────────    │  - Retiros       │
│                 │                     │  - Consultas     │
└─────────────────┘                     │  - Transferencias│
                                        │                  │
┌─────────────────┐                     │  Base de Datos:  │
│                 │  ──────────────►    │  - 12345: $5K    │
│  CLIENTE N      │  ◄──────────────    │  - 54321: $3.5K  │
│  (ChatFX)       │                     │  - 99999: $10K   │
│                 │                     │                  │
└─────────────────┘                     └──────────────────┘
```

## 📊 Flujo de Transacción

```
CLIENTE                          SERVIDOR
  │                               │
  │─────── TRAMA SOLICITUD ─────►│
  │        (DEPOSITO)            │
  │                              ├─ Validar trama
  │                              ├─ Verificar cuenta
  │                              ├─ Procesar operación
  │                              ├─ Actualizar saldo
  │◄───── TRAMA RESPUESTA ─────│
  │         (RESPUESTA:OK)        │
  │                              ├─ Log de auditoría
  │                              └─ Notificar otros
```

## 🎯 Cambios Realizados en Fase 2

### Nuevas Clases
- `protocol/Frame.java` - Objeto que representa una trama
- `protocol/FrameParser.java` - Parsea y valida tramas
- `protocol/TransactionProcessor.java` - Procesa transacciones bancarias

### Clases Modificadas
- `client/Cliente.java` - Ahora envía/recibe tramas
- `client/ClienteGUI.java` - Interfaz con botones para operaciones
- `server/Servidor.java` - Procesa tramas con TransactionProcessor
- `server/ServidorGUI.java` - Muestra estado de cuentas y transacciones

### Nuevos Archivos
- `PROTOCOLO.md` - Documentación completa del protocolo

## 💡 Cómo Extender el Sistema

### Agregar una Nueva Operación

1. **Crear Frame en Cliente**:
```java
Frame trama = new Frame("MI_OPERACION")
    .campo("PARAMETRO1", valor1)
    .campo("PARAMETRO2", valor2);
cliente.enviarTrama(trama);
```

2. **Actualizar Parser**:
```java
case "MI_OPERACION":
    return tieneParametros && tieneParametro1;
```

3. **Implementar en Processor**:
```java
case "MI_OPERACION":
    return procesarMiOperacion(trama);
```

## 🔒 Notas de Seguridad

Este es un **sistema de simulación educativo**. En producción, implementar:
- 🔐 Autenticación y contraseñas
- 🔒 Encriptación SSL/TLS
- 📋 Logs de auditoría detallados
- ⏱️ Rate limiting
- ✅ Checksums y firmas digitales

## 🐛 Resolución de Problemas

### "Connection refused" 
- Asegúrate que el servidor está corriendo en el puerto 12345

### "Trama mal formada"
- Verifica que la trama tenga `<INICIO>` y `|FIN`

### "Cuenta no encontrada"
- Usa una de las cuentas predefinidas (12345, 54321, 99999)

### "Saldo insuficiente"
- Verifica el saldo disponible con CONSULTA primero

## 📚 Documentación Adicional

Ver [PROTOCOLO.md](PROTOCOLO.md) para:
- Formato detallado de tramas
- Especificación de cada operación
- Ejemplos de comunicación
- Códigos de error completos
- Guía de implementación

## 👥 Autores

Desarrollado como proyecto educativo de sistemas bancarios distribuidos.

## 📄 Licencia

Proyecto de código abierto para propósitos educativos.

---

**Versión**: 2.0 (Fase 2)  
**Último actualizado**: Enero 2025  
**Estado**: Producción ✓

