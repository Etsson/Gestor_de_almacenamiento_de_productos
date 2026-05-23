# Ejemplos de Código - Sistema ChatFX

## Para Desarrolladores: Cómo Usar las Clases

### 1. Trabajar con Tramas (Frame.java)

#### Crear una trama simple

```java
// Crear una trama de depósito
Frame deposito = new Frame("DEPOSITO")
    .campo("CUENTA", 12345)
    .campo("MONTO", 500.0);

System.out.println(deposito.toString());
// Output: <INICIO>|TIPO:DEPOSITO|CUENTA:12345|MONTO:500.00|FIN
```

#### Crear una trama de respuesta

```java
// Respuesta exitosa
Frame respuesta = new Frame("RESPUESTA")
    .campo("ESTADO", "OK")
    .campo("MENSAJE", "Operación exitosa")
    .campo("SALDO", 5500.0)
    .campo("CODIGO", "OPERACION_EXITOSA");

System.out.println(respuesta.toString());
// Output: <INICIO>|TIPO:RESPUESTA|ESTADO:OK|MENSAJE:Operación exitosa|SALDO:5500.00|CODIGO:OPERACION_EXITOSA|FIN
```

#### Extraer datos de una trama

```java
Frame frame = new Frame("CONSULTA")
    .campo("CUENTA", 12345);

// Obtener valores
long cuenta = frame.obtenerLong("CUENTA");        // Returns 12345
String tipo = frame.getTipo();                     // Returns "CONSULTA"
Map<String, String> campos = frame.getCampos();   // Returns all fields
boolean existe = frame.existe("CUENTA");          // Returns true
```

---

### 2. Parsear Tramas (FrameParser.java)

#### Parsear una trama recibida

```java
String tramaBruta = "<INICIO>|TIPO:DEPOSITO|CUENTA:12345|MONTO:500.00|FIN";

// Parsear
Frame frame = FrameParser.parsear(tramaBruta);

if (frame != null) {
    System.out.println("Tipo: " + frame.getTipo());      // DEPOSITO
    System.out.println("Cuenta: " + frame.obtener("CUENTA"));  // 12345
    System.out.println("Monto: " + frame.obtenerDouble("MONTO"));  // 500.0
} else {
    System.out.println("Trama mal formada");
}
```

#### Validar una trama

```java
Frame frame = FrameParser.parsear(tramaBruta);

if (FrameParser.esValida(frame)) {
    System.out.println("Trama válida");
    // Procesar trama
} else {
    System.out.println("Trama inválida");
    String error = FrameParser.obtenerErrorValidacion(tramaBruta);
    System.out.println("Error: " + error);
}
```

---

### 3. Procesar Transacciones (TransactionProcessor.java)

#### Procesar una trama cualquiera

```java
TransactionProcessor processor = new TransactionProcessor();

// Crear solicitud
Frame solicitud = new Frame("DEPOSITO")
    .campo("CUENTA", 12345)
    .campo("MONTO", 500.0);

// Procesar
Frame respuesta = processor.procesarTrama(solicitud);

System.out.println("Estado: " + respuesta.obtener("ESTADO"));      // OK
System.out.println("Mensaje: " + respuesta.obtener("MENSAJE"));    // Depósito realizado...
System.out.println("Saldo: " + respuesta.obtenerDouble("SALDO")); // 5500.0
```

#### Operaciones específicas

```java
TransactionProcessor processor = new TransactionProcessor();

// 1. Depósito
Frame deposito = new Frame("DEPOSITO")
    .campo("CUENTA", 12345)
    .campo("MONTO", 1000.0);
Frame respDeposito = processor.procesarDeposito(deposito);

// 2. Retiro
Frame retiro = new Frame("RETIRO")
    .campo("CUENTA", 12345)
    .campo("MONTO", 500.0);
Frame respRetiro = processor.procesarRetiro(retiro);

// 3. Consulta
Frame consulta = new Frame("CONSULTA")
    .campo("CUENTA", 12345);
Frame respConsulta = processor.procesarConsulta(consulta);

// 4. Transferencia
Frame transfer = new Frame("TRANSFERENCIA")
    .campo("CUENTA_ORIGEN", 12345)
    .campo("CUENTA_DESTINO", 54321)
    .campo("MONTO", 500.0);
Frame respTransfer = processor.procesarTransferencia(transfer);
```

#### Acceder a cuentas

```java
TransactionProcessor processor = new TransactionProcessor();

// Obtener una cuenta específica
TransactionProcessor.BankAccount cuenta = processor.obtenerCuenta(12345);
if (cuenta != null) {
    System.out.println("Titular: " + cuenta.getTitular());
    System.out.println("Saldo: " + cuenta.getSaldo());
    System.out.println("Número: " + cuenta.getNumero());
}

// Obtener todas las cuentas
for (TransactionProcessor.BankAccount c : processor.obtenerTodasLasCuentas()) {
    System.out.println(c.getNumero() + " - " + c.getTitular() + ": $" + c.getSaldo());
}
```

#### Obtener historial de transacciones

```java
TransactionProcessor processor = new TransactionProcessor();

// ... realizar algunas transacciones ...

List<TransactionProcessor.Transaction> historial = processor.obtenerHistorial();

for (TransactionProcessor.Transaction t : historial) {
    System.out.println(t.getFecha() + " - " + t.getTipo() + 
                      " - Cuenta: " + t.getCuenta() + 
                      " - Monto: $" + t.getMonto() + 
                      " - " + t.getEstado());
}
```

---

### 4. Usar Cliente (Cliente.java)

#### Conectar al servidor

```java
Cliente cliente = new Cliente("Usuario1", new Cliente.ClienteListener() {
    @Override
    public void onMensajeRecibido(String mensaje) {
        System.out.println("Chat: " + mensaje);
    }

    @Override
    public void onTramaRecibida(Frame frame) {
        System.out.println("Trama recibida: " + frame.getTipo());
    }

    @Override
    public void onConexionCambiada(boolean conectado) {
        System.out.println("Conectado: " + conectado);
    }

    @Override
    public void onError(String error) {
        System.err.println("Error: " + error);
    }
});

// Conectar a servidor
if (cliente.conectar()) {
    System.out.println("Conectado al servidor");
} else {
    System.out.println("No se pudo conectar");
}
```

#### Enviar operaciones bancarias

```java
// Desde dentro de ClienteListener

// Enviar depósito
cliente.enviarDeposito(12345, 500.0);

// Enviar retiro
cliente.enviarRetiro(12345, 250.0);

// Enviar consulta
cliente.enviarConsulta(12345);

// Enviar transferencia
cliente.enviarTransferencia(12345, 54321, 1000.0);
```

#### Enviar trama personalizada

```java
Frame miTrama = new Frame("MI_OPERACION")
    .campo("PARAMETRO1", "valor1")
    .campo("PARAMETRO2", "valor2");

cliente.enviarTrama(miTrama);
```

#### Desconectar

```java
cliente.desconectar();
```

---

### 5. Patrones de Uso Completo

#### Patrón 1: Chat Simple con Respuesta

```java
public class ClienteChatSimple {
    public static void main(String[] args) {
        Cliente cliente = new Cliente("Usuario1", new Cliente.ClienteListener() {
            @Override
            public void onMensajeRecibido(String mensaje) {
                System.out.println(">> " + mensaje);
            }

            @Override
            public void onTramaRecibida(Frame frame) {
                // Ignorar para este ejemplo
            }

            @Override
            public void onConexionCambiada(boolean conectado) {
                System.out.println("Estado: " + (conectado ? "Conectado" : "Desconectado"));
            }

            @Override
            public void onError(String error) {
                System.err.println("[ERROR] " + error);
            }
        });

        // Conectar
        cliente.conectar();

        // Enviar mensaje
        cliente.enviarMensaje("Hola a todos!");

        // Mantener abierto
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Desconectar
        cliente.desconectar();
    }
}
```

#### Patrón 2: Transacción Bancaria Completa

```java
public class OperacionBancaria {
    public static void main(String[] args) {
        TransactionProcessor processor = new TransactionProcessor();

        // Consultar saldo inicial
        Frame consulta1 = new Frame("CONSULTA").campo("CUENTA", 12345);
        Frame respConsulta1 = processor.procesarTrama(consulta1);
        double saldoInicial = respConsulta1.obtenerDouble("SALDO");
        System.out.println("Saldo inicial: $" + saldoInicial);

        // Hacer depósito
        Frame deposito = new Frame("DEPOSITO")
            .campo("CUENTA", 12345)
            .campo("MONTO", 1000.0);
        Frame respDeposito = processor.procesarTrama(deposito);
        if ("OK".equals(respDeposito.obtener("ESTADO"))) {
            System.out.println("✓ " + respDeposito.obtener("MENSAJE"));
            System.out.println("  Nuevo saldo: $" + respDeposito.obtener("SALDO"));
        }

        // Retirar
        Frame retiro = new Frame("RETIRO")
            .campo("CUENTA", 12345)
            .campo("MONTO", 500.0);
        Frame respRetiro = processor.procesarTrama(retiro);
        if ("OK".equals(respRetiro.obtener("ESTADO"))) {
            System.out.println("✓ " + respRetiro.obtener("MENSAJE"));
            System.out.println("  Nuevo saldo: $" + respRetiro.obtener("SALDO"));
        }

        // Consultar saldo final
        Frame consulta2 = new Frame("CONSULTA").campo("CUENTA", 12345);
        Frame respConsulta2 = processor.procesarTrama(consulta2);
        double saldoFinal = respConsulta2.obtenerDouble("SALDO");
        System.out.println("\nSaldo inicial: $" + saldoInicial);
        System.out.println("Saldo final: $" + saldoFinal);
        System.out.println("Cambio neto: $" + (saldoFinal - saldoInicial));
    }
}
```

#### Patrón 3: Validación de Trama

```java
public class ValidarTrama {
    public static void validarYProcesar(String tramaBruta, TransactionProcessor processor) {
        System.out.println("Trama recibida: " + tramaBruta);

        // Parsear
        Frame frame = FrameParser.parsear(tramaBruta);
        
        if (frame == null) {
            System.out.println("❌ Trama mal formada");
            String error = FrameParser.obtenerErrorValidacion(tramaBruta);
            System.out.println("   Error: " + error);
            return;
        }

        // Validar
        if (!FrameParser.esValida(frame)) {
            System.out.println("❌ Trama inválida para el tipo: " + frame.getTipo());
            System.out.println("   Campos presentes: " + frame.getCampos().keySet());
            return;
        }

        // Procesar
        System.out.println("✓ Trama válida, procesando...");
        Frame respuesta = processor.procesarTrama(frame);
        System.out.println("  Resultado: " + respuesta.obtener("MENSAJE"));
    }

    public static void main(String[] args) {
        TransactionProcessor processor = new TransactionProcessor();

        // Trama válida
        validarYProcesar(
            "<INICIO>|TIPO:DEPOSITO|CUENTA:12345|MONTO:500.00|FIN",
            processor
        );

        // Trama mal formada (falta FIN)
        validarYProcesar(
            "<INICIO>|TIPO:DEPOSITO|CUENTA:12345|MONTO:500.00",
            processor
        );

        // Trama con campos faltantes
        validarYProcesar(
            "<INICIO>|TIPO:DEPOSITO|CUENTA:12345|FIN",
            processor
        );
    }
}
```

---

### 6. Extender el Sistema

#### Agregar una Nueva Operación

**Paso 1: Actualizar FrameParser.esValida()**

```java
// En FrameParser.java, método esValida()
case "MI_OPERACION":
    return frame.existe("CAMPO1") && frame.existe("CAMPO2");
```

**Paso 2: Agregar método en TransactionProcessor**

```java
// En TransactionProcessor.java
public Frame procesarMiOperacion(Frame trama) {
    Frame respuesta = new Frame("RESPUESTA");
    respuesta.campo("ID_OPERACION", trama.getId());

    try {
        // Tu lógica aquí
        String campo1 = trama.obtener("CAMPO1");
        String campo2 = trama.obtener("CAMPO2");

        // Procesar...
        
        respuesta.campo("ESTADO", "OK");
        respuesta.campo("MENSAJE", "Operación completada");
        respuesta.campo("CODIGO", "OPERACION_EXITOSA");

    } catch (Exception e) {
        respuesta.campo("ESTADO", "ERROR");
        respuesta.campo("MENSAJE", "Error: " + e.getMessage());
        respuesta.campo("CODIGO", "ERROR_INTERNO");
    }

    return respuesta;
}
```

**Paso 3: Agregar caso en procesarTrama()**

```java
// En TransactionProcessor.java, método procesarTrama()
case "MI_OPERACION":
    return procesarMiOperacion(trama);
```

---

## 🔍 Debugging Tips

### Mostrar detalles de una trama

```java
Frame frame = new Frame("DEPOSITO")
    .campo("CUENTA", 12345)
    .campo("MONTO", 500.0);

System.out.println("=== DETALLES DE LA TRAMA ===");
System.out.println("Tipo: " + frame.getTipo());
System.out.println("ID: " + frame.getId());
System.out.println("Timestamp: " + frame.getTimestamp());
System.out.println("Campos:");
for (var entry : frame.getCampos().entrySet()) {
    System.out.println("  - " + entry.getKey() + " = " + entry.getValue());
}
System.out.println("String: " + frame.toString());
```

### Simular servidor sin GUI

```java
public class ServidorSimulado {
    public static void main(String[] args) {
        TransactionProcessor processor = new TransactionProcessor();

        // Simular recepción de trama
        String tramaBruta = "<INICIO>|TIPO:DEPOSITO|CUENTA:12345|MONTO:500.00|FIN";
        
        Frame solicitud = FrameParser.parsear(tramaBruta);
        if (solicitud != null && FrameParser.esValida(solicitud)) {
            Frame respuesta = processor.procesarTrama(solicitud);
            
            System.out.println("Solicitud: " + tramaBruta);
            System.out.println("Respuesta: " + respuesta.toString());
        }
    }
}
```

---

## 📚 Referencias Rápidas

| Clase | Método | Uso |
|-------|--------|-----|
| `Frame` | `campo(String, Object)` | Agregar campo |
| `Frame` | `obtener(String)` | Obtener valor string |
| `Frame` | `obtenerLong(String)` | Obtener valor long |
| `Frame` | `obtenerDouble(String)` | Obtener valor double |
| `Frame` | `existe(String)` | Verificar si existe campo |
| `Frame` | `toString()` | Convertir a string |
| `FrameParser` | `parsear(String)` | Parsear string a Frame |
| `FrameParser` | `esValida(Frame)` | Validar Frame |
| `TransactionProcessor` | `procesarTrama(Frame)` | Procesar cualquier trama |
| `Cliente` | `enviarTrama(Frame)` | Enviar trama |
| `Cliente` | `enviarDeposito(...)` | Enviar depósito |
| `Cliente` | `enviarRetiro(...)` | Enviar retiro |

---

¡Felicitaciones! Ahora tienes ejemplos prácticos para trabajar con el sistema ChatFX.

