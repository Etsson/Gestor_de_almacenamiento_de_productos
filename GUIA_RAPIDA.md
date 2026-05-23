# 🚀 Guía Rápida - ChatFX Sistema de Transacciones

## Inicio Rápido en 5 Minutos

### Paso 1: Compilar (Primera vez)
```bash
cd ChatFX
javac -d bin src/main/java/**/*.java
```

### Paso 2: Ejecutar Servidor
```bash
java -cp bin server.ServidorGUI
```
- El servidor se inicia automáticamente
- Esperará conexiones en puerto **12345**

### Paso 3: Ejecutar Cliente(s)
```bash
java -cp bin client.ClienteGUI
```
- Se abre ventana del cliente
- Conecta automáticamente a localhost:12345

---

## 🎮 Usando la Aplicación

### Panel de Conexión (Arriba)
```
┌────────────────────────────────────────────┐
│ Nombre: [Usuario1]  IP: [localhost]        │
│ Puerto: [12345]  [Conectar] [Desconectar] │
└────────────────────────────────────────────┘
```

### Panel de Transacciones (Derecha)
```
┌─ Operaciones Bancarias ───────┐
│                                │
│ Cuenta: [12345]                │
│ Monto:  [500.00]               │
│                                │
│ [DEPÓSITO] [RETIRO]           │
│ [CONSULTA]                     │
│                                │
│ Cuentas Disponibles:           │
│ • Cuenta 12345 - Juan Pérez    │
│ • Cuenta 54321 - María García  │
│ • Cuenta 99999 - Carlos López  │
└────────────────────────────────┘
```

---

## 💰 Ejemplos de Transacciones

### Ejemplo 1: Hacer un Depósito

**Pasos:**
1. Ingresa Cuenta: `12345`
2. Ingresa Monto: `1000`
3. Haz clic en botón **DEPÓSITO**

**Resultado en Servidor:**
```
[TRANSACCION] Usuario: Usuario1 | Tipo: DEPOSITO | 
Estado: OK | Mensaje: Depósito realizado exitosamente | 
Cuenta: 12345
```

**Resultado en Cliente:**
```
[RESPUESTA] ✓ Depósito realizado exitosamente | 
Cuenta: 12345 | Monto Depositado: $1,000.00 | 
Saldo: $6,000.00
```

---

### Ejemplo 2: Consultar Saldo

**Pasos:**
1. Ingresa Cuenta: `12345`
2. (Monto no necesario para consulta)
3. Haz clic en botón **CONSULTA**

**Resultado:**
```
[RESPUESTA] ✓ Consulta realizada exitosamente | 
Cuenta: 12345 | Saldo: $6,000.00
```

---

### Ejemplo 3: Error - Saldo Insuficiente

**Pasos:**
1. Ingresa Cuenta: `54321` (María: $3,500)
2. Ingresa Monto: `5000`
3. Haz clic en botón **RETIRO**

**Resultado:**
```
[RESPUESTA] ✗ Saldo insuficiente [SALDO_INSUFICIENTE]
Disponible: $3,500.00
Solicitado: $5,000.00
```

---

## 🔑 Cuentas de Prueba Rápidas

```
Copiar y pegar en el cliente:

CUENTA 1: 12345   (Juan Pérez)      - $5,000.00
CUENTA 2: 54321   (María García)    - $3,500.00
CUENTA 3: 99999   (Carlos López)    - $10,000.00
```

---

## 📋 Operaciones Disponibles

| Botón | Operación | Parámetros | Ejemplo |
|-------|-----------|-----------|---------|
| **DEPÓSITO** | Agregar dinero | Cuenta, Monto | Depósito de $500 |
| **RETIRO** | Sacar dinero | Cuenta, Monto | Retiro de $200 |
| **CONSULTA** | Ver saldo | Cuenta | Consultar saldo |

---

## 🖥️ Vista del Servidor

El servidor muestra 3 paneles:

### Panel 1: Log de Transacciones
```
[INFO] Servidor escuchando en puerto 12345
[INFO] Sistema de transacciones bancarias activo
[CONEXIÓN] Usuario1 conectado desde 127.0.0.1
[TRANSACCION] Usuario: Usuario1 | Tipo: DEPOSITO | Estado: OK
[DESCONEXIÓN] Usuario1 desconectado
```

### Panel 2: Clientes Conectados
```
Total: 1
└─ Usuario1
```

### Panel 3: Estado de Cuentas
```
Cuenta: 12345
Titular: Juan Pérez
Saldo: $6,000.00

Cuenta: 54321
Titular: María García
Saldo: $3,500.00

Cuenta: 99999
Titular: Carlos López
Saldo: $10,000.00
```

---

## ❌ Solución de Errores Comunes

### Error: "Connection refused"
**Causa:** El servidor no está corriendo  
**Solución:**
```bash
java -cp bin server.ServidorGUI
```

### Error: "Trama mal formada"
**Causa:** Formato incorrecto en protocolo  
**Solución:** Verifica que se use `<INICIO>` y `|FIN`

### Error: "Cuenta no encontrada"
**Causa:** Número de cuenta inválido  
**Solución:** Usa 12345, 54321 o 99999

### Error: "Monto debe ser mayor a 0"
**Causa:** Monto negativo o cero  
**Solución:** Ingresa un monto positivo

---

## 📞 Formato de Tramas (Para Desarrolladores)

### Solicitud de Depósito
```
<INICIO>|TIPO:DEPOSITO|CUENTA:12345|MONTO:500.00|FIN
```

### Respuesta del Servidor
```
<INICIO>|TIPO:RESPUESTA|ESTADO:OK|MENSAJE:Depósito realizado|SALDO:5500.00|FIN
```

Para más detalles, ver [PROTOCOLO.md](PROTOCOLO.md)

---

## 🎯 Casos de Prueba Sugeridos

### Caso 1: Flujo Normal
```
1. Conectar usuario
2. Consultar saldo inicial
3. Hacer depósito
4. Verificar nuevo saldo
5. Desconectar
```

### Caso 2: Validación de Errores
```
1. Intentar depositar -100 → Error
2. Consultar cuenta inexistente → Error
3. Retirar más que el saldo → Error
4. Transferir a cuenta igual → Error
```

### Caso 3: Múltiples Usuarios
```
1. Abrir 2 ventanas de cliente
2. Ambos conectan al servidor
3. Usuario1 transfiere dinero a Usuario2
4. Verificar saldos en ambos lados
```

---

## 💡 Tips Útiles

- **No necesitas ingresar monto para CONSULTA** - Solo la cuenta
- **Los cambios son persistentes durante la sesión** - Pero se reinician al reiniciar servidor
- **Puedes usar múltiples clientes simultáneamente**
- **El servidor muestra todas las operaciones en el log**
- **Prueba con las 3 cuentas diferentes para experimentar**

---

## 🚀 Próximas Mejoras (Fase 3)

- [ ] Base de datos persistente
- [ ] Autenticación con contraseñas
- [ ] Encriptación SSL/TLS
- [ ] Reportes de transacciones
- [ ] Historial de operaciones
- [ ] Límites de transacción
- [ ] Notificaciones en tiempo real

---

**¿Necesitas más ayuda?** Ver [PROTOCOLO.md](PROTOCOLO.md) para documentación completa.

