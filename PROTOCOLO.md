# Protocolo de Transacciones Bancarias - ChatFX

## 📋 Introducción

Este documento describe el protocolo de comunicación utilizado en el sistema **ChatFX** para simular transacciones bancarias entre clientes y servidor. El protocolo define un formato de tramas (frames) fijo que permite estructurar los mensajes y garantizar la validación de datos.

---

## 🏗️ Estructura de una Trama

Todas las tramas siguen el siguiente formato:

```
<INICIO>|TIPO:tipo|CAMPO1:valor1|CAMPO2:valor2|...|FIN
```

### Componentes:

- **`<INICIO>`**: Marcador de inicio de trama (obligatorio)
- **`TIPO`**: Identifica el tipo de operación o respuesta
- **`CAMPO:valor`**: Pares clave-valor con datos de la operación
- **`FIN`**: Marcador de fin de trama (obligatorio)
- **`|`**: Separador entre componentes

### Ejemplos:

#### Depósito:
```
<INICIO>|TIPO:DEPOSITO|CUENTA:12345|MONTO:500.00|FIN
```

#### Retiro:
```
<INICIO>|TIPO:RETIRO|CUENTA:12345|MONTO:250.50|FIN
```

#### Consulta:
```
<INICIO>|TIPO:CONSULTA|CUENTA:12345|FIN
```

---

## 🔄 Operaciones Soportadas

### 1. DEPÓSITO
**Descripción**: Deposita dinero en una cuenta bancaria.

**Trama de Solicitud**:
```
<INICIO>|TIPO:DEPOSITO|CUENTA:numero_cuenta|MONTO:cantidad|FIN
```

**Parámetros**:
- `CUENTA` (long): Número de cuenta destino
- `MONTO` (double): Cantidad a depositar (debe ser > 0)

**Trama de Respuesta (Exitosa)**:
```
<INICIO>|TIPO:RESPUESTA|ESTADO:OK|MENSAJE:Depósito realizado exitosamente|CUENTA:12345|MONTO_DEPOSITADO:500.00|SALDO_ANTERIOR:5000.00|SALDO_NUEVO:5500.00|CODIGO:OPERACION_EXITOSA|FIN
```

**Trama de Respuesta (Error)**:
```
<INICIO>|TIPO:RESPUESTA|ESTADO:ERROR|MENSAJE:Cuenta no encontrada|CODIGO:CUENTA_NO_EXISTE|FIN
```

---

### 2. RETIRO
**Descripción**: Retira dinero de una cuenta bancaria.

**Trama de Solicitud**:
```
<INICIO>|TIPO:RETIRO|CUENTA:numero_cuenta|MONTO:cantidad|FIN
```

**Parámetros**:
- `CUENTA` (long): Número de cuenta origen
- `MONTO` (double): Cantidad a retirar (debe ser > 0)

**Trama de Respuesta (Exitosa)**:
```
<INICIO>|TIPO:RESPUESTA|ESTADO:OK|MENSAJE:Retiro realizado exitosamente|CUENTA:12345|MONTO_RETIRADO:250.00|SALDO_ANTERIOR:5000.00|SALDO_NUEVO:4750.00|CODIGO:OPERACION_EXITOSA|FIN
```

**Posibles Errores**:
- `CUENTA_NO_EXISTE`: La cuenta no existe
- `SALDO_INSUFICIENTE`: No hay suficiente saldo
- `MONTO_INVALIDO`: Monto debe ser mayor a 0

---

### 3. CONSULTA
**Descripción**: Consulta el saldo de una cuenta.

**Trama de Solicitud**:
```
<INICIO>|TIPO:CONSULTA|CUENTA:numero_cuenta|FIN
```

**Parámetros**:
- `CUENTA` (long): Número de cuenta

**Trama de Respuesta (Exitosa)**:
```
<INICIO>|TIPO:RESPUESTA|ESTADO:OK|MENSAJE:Consulta realizada exitosamente|CUENTA:12345|TITULAR:Juan Pérez|SALDO:5500.00|CODIGO:OPERACION_EXITOSA|FIN
```

---

### 4. TRANSFERENCIA
**Descripción**: Transfiere dinero de una cuenta a otra.

**Trama de Solicitud**:
```
<INICIO>|TIPO:TRANSFERENCIA|CUENTA_ORIGEN:12345|CUENTA_DESTINO:54321|MONTO:1000.00|FIN
```

**Parámetros**:
- `CUENTA_ORIGEN` (long): Cuenta desde donde se transfiere
- `CUENTA_DESTINO` (long): Cuenta hacia donde se transfiere
- `MONTO` (double): Cantidad a transferir (debe ser > 0)

**Trama de Respuesta (Exitosa)**:
```
<INICIO>|TIPO:RESPUESTA|ESTADO:OK|MENSAJE:Transferencia realizada exitosamente|CUENTA_ORIGEN:12345|CUENTA_DESTINO:54321|MONTO_TRANSFERIDO:1000.00|SALDO_ORIGEN:4500.00|SALDO_DESTINO:4500.00|CODIGO:OPERACION_EXITOSA|FIN
```

---

### 5. REGISTRO
**Descripción**: Registra un usuario en el sistema.

**Trama de Solicitud**:
```
<INICIO>|TIPO:REGISTRO|USUARIO:nombre_usuario|FIN
```

**Parámetros**:
- `USUARIO` (string): Nombre del usuario

---

### 6. DESCONEXION
**Descripción**: Notifica al servidor que el cliente se desconecta.

**Trama de Solicitud**:
```
<INICIO>|TIPO:DESCONEXION|FIN
```

---

### 7. NOTIFICACION
**Descripción**: Notificación del servidor a los clientes (no solicitada).

**Trama**:
```
<INICIO>|TIPO:NOTIFICACION|TIPO:CONEXION|USUARIO:nombre|MENSAJE:mensaje|FIN
<INICIO>|TIPO:NOTIFICACION|TIPO:DESCONEXION|USUARIO:nombre|FIN
```

---

## ✅ Códigos de Respuesta

| Código | Significado |
|--------|-------------|
| `OPERACION_EXITOSA` | Operación completada exitosamente |
| `TRAMA_INVALIDA` | Formato de trama incorrecto |
| `TRAMA_NULA` | Se recibió una trama nula |
| `OPERACION_DESCONOCIDA` | Tipo de operación no reconocido |
| `CUENTA_NO_EXISTE` | La cuenta especificada no existe |
| `SALDO_INSUFICIENTE` | Saldo insuficiente para la operación |
| `MONTO_INVALIDO` | Monto debe ser mayor a 0 |
| `CUENTA_ORIGEN_IGUAL_DESTINO` | No se puede transferir a la misma cuenta |
| `ERROR_INTERNO` | Error interno del servidor |

---

## 🚨 Manejo de Errores

### Validación en Cliente

El cliente debe validar:
1. Que los campos requeridos no estén vacíos
2. Que los números sean válidos (cuenta debe ser número largo, monto debe ser número decimal positivo)
3. Que las cuentas sean diferentes en caso de transferencia

### Validación en Servidor

El servidor valida:
1. Que la trama esté bien formada (tiene `<INICIO>` y `|FIN`)
2. Que tenga tipo de operación
3. Que tenga todos los campos requeridos
4. Que los valores sean válidos según el tipo de operación
5. Que la cuenta exista
6. Que haya saldo suficiente (para retiros y transferencias)

### Respuestas de Error

Cuando hay un error, el servidor siempre responde con:
```
<INICIO>|TIPO:RESPUESTA|ESTADO:ERROR|MENSAJE:descripción_del_error|CODIGO:codigo_error|FIN
```

---

## 📊 Cuentas de Prueba

El sistema viene preconfigurado con las siguientes cuentas:

| Número | Titular | Saldo Inicial |
|--------|---------|---------------|
| 12345 | Juan Pérez | $5,000.00 |
| 54321 | María García | $3,500.00 |
| 99999 | Carlos López | $10,000.00 |

Estas cuentas están predefinidas en la clase `TransactionProcessor`.

---

## 🔌 Flujo de Comunicación

### Conexión y Registro

```
1. Cliente → Servidor: <INICIO>|TIPO:REGISTRO|USUARIO:usuario1|FIN
2. Servidor → Otros Clientes: <INICIO>|TIPO:NOTIFICACION|TIPO:CONEXION|USUARIO:usuario1|FIN
```

### Transacción

```
1. Cliente → Servidor: <INICIO>|TIPO:DEPOSITO|CUENTA:12345|MONTO:500|FIN
2. Servidor → Cliente: <INICIO>|TIPO:RESPUESTA|ESTADO:OK|MENSAJE:...|FIN
```

### Desconexión

```
1. Cliente → Servidor: <INICIO>|TIPO:DESCONEXION|FIN
2. Servidor → Otros Clientes: <INICIO>|TIPO:NOTIFICACION|TIPO:DESCONEXION|USUARIO:usuario1|FIN
```

---

## 🛠️ Implementación

### Clases Principales

#### `Frame.java`
Representa una trama individual. Permite construir tramas con:
- Constructor: `new Frame("DEPOSITO")`
- Método encadenado: `.campo("CUENTA", 12345).campo("MONTO", 500.0)`
- Conversión a string: `.toString()` retorna la trama formateada

#### `FrameParser.java`
Parsea strings en objetos Frame:
- `Frame parsear(String)`: Convierte un string en Frame
- `boolean esValida(Frame)`: Valida una trama según su tipo
- `String obtenerErrorValidacion(String)`: Proporciona descripción del error

#### `TransactionProcessor.java`
Procesa transacciones bancarias:
- `Frame procesarTrama(Frame)`: Procesa cualquier trama
- `Frame procesarDeposito(Frame)`: Maneja depósitos
- `Frame procesarRetiro(Frame)`: Maneja retiros
- `Frame procesarConsulta(Frame)`: Consulta saldos
- `Frame procesarTransferencia(Frame)`: Realiza transferencias

#### `Cliente.java`
Cliente que envía tramas:
- `void enviarTrama(Frame)`: Envía trama al servidor
- `void enviarDeposito(long, double)`: Helper para depósito
- `void enviarRetiro(long, double)`: Helper para retiro
- `void enviarConsulta(long)`: Helper para consulta
- `void enviarTransferencia(long, long, double)`: Helper para transferencia

#### `Servidor.java` / `ServidorGUI.java`
Servidor que procesa tramas y genera respuestas.

---

## 📝 Ejemplo de Sesión Completa

```
# Usuario se conecta
Usuario Envía:
<INICIO>|TIPO:REGISTRO|USUARIO:Juan|FIN

Servidor Responde:
[Sistema recibe y registra al usuario]

# Usuario hace un depósito
Usuario Envía:
<INICIO>|TIPO:DEPOSITO|CUENTA:12345|MONTO:500.00|FIN

Servidor Responde:
<INICIO>|TIPO:RESPUESTA|ESTADO:OK|MENSAJE:Depósito realizado exitosamente|CUENTA:12345|MONTO_DEPOSITADO:500.00|SALDO_ANTERIOR:5000.00|SALDO_NUEVO:5500.00|CODIGO:OPERACION_EXITOSA|FIN

# Usuario consulta saldo
Usuario Envía:
<INICIO>|TIPO:CONSULTA|CUENTA:12345|FIN

Servidor Responde:
<INICIO>|TIPO:RESPUESTA|ESTADO:OK|MENSAJE:Consulta realizada exitosamente|CUENTA:12345|TITULAR:Juan Pérez|SALDO:5500.00|CODIGO:OPERACION_EXITOSA|FIN

# Usuario intenta retirar más de lo disponible
Usuario Envía:
<INICIO>|TIPO:RETIRO|CUENTA:12345|MONTO:10000.00|FIN

Servidor Responde:
<INICIO>|TIPO:RESPUESTA|ESTADO:ERROR|MENSAJE:Saldo insuficiente|CODIGO:SALDO_INSUFICIENTE|SALDO_DISPONIBLE:5500.00|MONTO_SOLICITADO:10000.00|FIN

# Usuario se desconecta
Usuario Envía:
<INICIO>|TIPO:DESCONEXION|FIN

Servidor Notifica a Otros:
<INICIO>|TIPO:NOTIFICACION|TIPO:DESCONEXION|USUARIO:Juan|FIN
```

---

## 🔒 Consideraciones de Seguridad

Aunque este es un sistema de simulación educativo, en una implementación real se debería:

1. **Autenticación**: Implementar contraseñas y tokens
2. **Encriptación**: Usar SSL/TLS para cifrar la comunicación
3. **Validación**: Validar rangos de dinero y límites de transacción
4. **Auditoría**: Mantener logs detallados de todas las operaciones
5. **Rate Limiting**: Limitar número de transacciones por usuario/minuto
6. **Checksums**: Agregar checksums o firmas digitales a las tramas

---

## 📚 Extensibilidad

El protocolo puede extenderse fácilmente:

### Agregar una nueva operación:

1. Crear una clase `Frame` nueva:
   ```java
   Frame trama = new Frame("NUEVA_OPERACION")
       .campo("PARAMETRO1", valor1)
       .campo("PARAMETRO2", valor2);
   ```

2. Actualizar `FrameParser.esValida()` para validar los campos requeridos

3. Agregar procesamiento en `TransactionProcessor`:
   ```java
   case "NUEVA_OPERACION":
       return procesarNuevaOperacion(trama);
   ```

4. Implementar el método de procesamiento en el servidor

---

## 🚀 Cómo Usar

### Servidor

```bash
java -cp bin server.ServidorGUI
```

El servidor inicia automáticamente en puerto `12345` y acepta conexiones.

### Cliente

```bash
java -cp bin client.ClienteGUI
```

1. Ingresa un nombre de usuario
2. Conecta al servidor (localhost:12345 por defecto)
3. Usa los botones para ejecutar operaciones
4. Las respuestas aparecen en el área de chat

---

## 📖 Versión del Protocolo

- **Versión**: 1.0
- **Fecha**: Enero 2025
- **Estado**: Producción

---

## 📞 Soporte

Para reportar errores o sugerir mejoras, contacte al equipo de desarrollo.

