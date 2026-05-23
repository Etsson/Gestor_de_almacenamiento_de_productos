# 📋 PROTOCOLO DE DATOS Y GENERADOR DE VOUCHERS

## 🔐 Especificación del Protocolo

Se ha establecido un **protocolo claro y seguro** para envío/recepción de datos con:

### 1. **Formato de Trama**
```
<INICIO>|TIPO:operacion|CAMPO1:valor1|CAMPO2:valor2|STATUS:codigo|FIN
```

**Ejemplo de Depósito:**
```
<INICIO>|TIPO:DEPOSITO|USUARIO:juan|CUENTA:12345|MONTO:500.00|ID_TRANS:TRANS_001|STATUS:OK_200|FIN
```

### 2. **Delimitadores**
| Delimitador | Significado |
|---|---|
| `<INICIO>` | Marca el inicio de la trama |
| `\|` | Separa campos |
| `:` | Separa clave del valor |
| `\|FIN` | Marca el final de la trama |

### 3. **Tipos de Operación**
| Tipo | Descripción |
|---|---|
| `MENSAJE` | Mensaje de chat |
| `DEPOSITO` | Depósito bancario |
| `RETIRO` | Retiro bancario |
| `CONSULTA` | Consulta de saldo |
| `CONFIRMACION` | ACK/NACK |
| `VOUCHER` | Generación de voucher |
| `ERROR` | Error del sistema |

### 4. **Códigos de Estado**

#### ✅ Exitosos:
- `OK_200` - Operación exitosa
- `ACK` - Confirmación recibida

#### ❌ Errores:
- `NACK` - Error en transmisión
- `ERR_001` - Usuario no encontrado
- `ERR_002` - Saldo insuficiente
- `ERR_003` - Cuenta inválida
- `ERR_004` - Operación no permitida
- `ERR_005` - Timeout de conexión
- `ERR_006` - Autenticación fallida
- `ERR_007` - Parámetros inválidos

### 5. **Campos Estándar**

| Campo | Tipo | Descripción |
|---|---|---|
| `TIPO` | String | Tipo de operación |
| `STATUS` | String | Código de estado |
| `ID_TRANS` | String | ID único de transacción |
| `USUARIO` | String | Nombre del usuario |
| `CUENTA` | Long | Número de cuenta |
| `MONTO` | Double | Cantidad (formato: 000.00) |
| `SALDO_ANT` | Double | Saldo anterior |
| `SALDO_NUEVO` | Double | Saldo después de operación |
| `FECHA` | String | Fecha (dd/MM/yyyy) |
| `HORA` | String | Hora (HH:mm:ss) |
| `MENSAJE` | String | Contenido del mensaje |
| `TIMESTAMP` | Long | Timestamp de Unix |

---

## 📄 Generador de Vouchers JPEG

Se ha creado un sistema para generar **recibos profesionales** en formato JPEG.

### Características:
✅ **Encabezado azul** con logo
✅ **Secciones claras** y organizadas
✅ **Información de transacción**
✅ **Detalles financieros**
✅ **Fecha y hora**
✅ **Formato profesional**
✅ **Guardado automático**

### Ubicación de Archivos:
```
Windows: C:\Users\{usuario}\Descargas\Vouchers\
Linux:   /home/{usuario}/Descargas/Vouchers/
macOS:   /Users/{usuario}/Downloads/Vouchers/
```

### Nombre de Archivo:
```
VOUCHER_TRANS_00123_1715789234567.jpg
```

---

## 🛠️ Archivos Nuevos

### 1. **protocol/ProtocolConstants.java**
Define todas las constantes del protocolo:
- Delimitadores
- Tipos de operación
- Códigos de estado
- Campos estándar
- Métodos auxiliares

```java
// Uso:
String estado = ProtocolConstants.STATUS_OK_200;
String descripcion = ProtocolConstants.getErrorDescription(estado);
boolean esExito = ProtocolConstants.isSuccess(estado);
```

### 2. **app/VoucherGenerator.java**
Genera vouchers en JPEG:

```java
VoucherGenerator generador = new VoucherGenerator(
    "TRANS_001",           // Número de transacción
    "DEPOSITO",            // Tipo de operación
    "juan",                // Usuario
    "12345",               // Número de cuenta
    500.00,                // Monto
    1000.00,               // Saldo anterior
    1500.00                // Saldo nuevo
);

generador.guardarEnArchivo(new File("voucher.jpg"));
```

---

## 🔘 Botón Generar Voucher

### Ubicación:
Panel de operaciones bancarias en ClienteGUI

### Funcionalidad:
1. Se activa cuando el cliente **está conectado al servidor**
2. Genera un voucher basado en la **última transacción realizada**
3. Guarda automáticamente en carpeta **Descargas/Vouchers/**
4. Muestra la ruta del archivo guardado

### Información del Voucher:
- Número de transacción único
- Tipo de operación
- Usuario autenticado
- Número de cuenta
- Monto operado
- Saldo anterior y nuevo
- Fecha y hora exacta

---

## 📋 Ejemplo de Flujo de Datos

### 1. Cliente envía depósito:
```
Cliente → [<INICIO>|TIPO:DEPOSITO|USUARIO:juan|CUENTA:12345|MONTO:500.00|TIMESTAMP:1715789234567|FIN]
         → Servidor
```

### 2. Servidor procesa y responde:
```
Servidor → [<INICIO>|TIPO:DEPOSITO|ID_TRANS:TRANS_001|STATUS:OK_200|SALDO_ANT:1000.00|SALDO_NUEVO:1500.00|FECHA:13/05/2026|HORA:14:30:45|FIN]
         → Cliente
```

### 3. Cliente genera voucher:
```
Cliente → Crea VoucherGenerator
        → Genera imagen JPEG
        → Guarda en Descargas/Vouchers/VOUCHER_TRANS_001_1715789234567.jpg
        → Muestra confirmación
```

---

## ✅ Checklist de Implementación

- [x] ProtocolConstants.java creado
- [x] VoucherGenerator.java creado
- [x] Botón "GENERAR VOUCHER" agregado
- [x] Método generarYGuardarVoucher() implementado
- [x] Carpeta de destino automática
- [x] Confirmación de guardado
- [x] Integración con transacciones
- [x] Variables de seguimiento de transacciones
- [x] Documentación completa

---

## 🚀 Cómo Compilar y Ejecutar

### Compilar:
```bash
compile.bat
```

### Ejecutar:
```bash
java --module-path lib --add-modules javafx.controls,javafx.fxml -cp bin;lib\* app.App
```

### Usar Voucher:
1. Inicia sesión
2. Conecta al servidor
3. Realiza una transacción (depósito, retiro, etc.)
4. Haz clic en **"GENERAR VOUCHER"**
5. Se guardará automáticamente en **Descargas/Vouchers/**

---

## 📝 Ejemplo de Voucher JPEG

```
┌──────────────────────────────┐
│        VOUCHER               │
└──────────────────────────────┘

INFORMACIÓN DE TRANSACCIÓN
Tipo: Depósito Bancario
Transacción: TRANS_001
Usuario: juan
Cuenta: 12345
────────────────────────────────

DETALLES FINANCIEROS
Monto: $500.00
Saldo Anterior: $1,000.00
Saldo Nuevo: $1,500.00
────────────────────────────────

FECHA Y HORA
Fecha: 13/05/2026
Hora: 14:30:45
────────────────────────────────

Gracias por usar nuestro servicio
Conserve este recibo
```

---

¡Sistema de protocolo y vouchers completamente implementado! 🎉
