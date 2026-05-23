# ChatFX - Sistema de Chat en Tiempo Real

Un sistema de chat multi-cliente en Java con JavaFX.

## Características

- Servidor centralizado con interfaz gráfica
- Múltiples clientes conectados simultáneamente
- Mensajería en tiempo real
- Identificación por nombre de usuario
- Interfaz moderna con JavaFX
- Threading seguro y gestión de errores

## Estructura

**/src/main/java/**
- **app/App.java** - Menú principal
- **server/Servidor.java** - Servidor sin GUI
- **server/ServidorGUI.java** - Servidor con interfaz JavaFX
- **client/Cliente.java** - Lógica del cliente
- **client/ClienteGUI.java** - Cliente con interfaz JavaFX

## Requisitos

- Java JDK 11+
- JavaFX SDK

## Compilación y Ejecución

\\\ash
javac -d bin src/main/java/app/*.java src/main/java/server/*.java src/main/java/client/*.java
java --module-path lib --add-modules javafx.controls,javafx.fxml -cp bin app.App
\\\`n
## Uso

1. Ejecuta la app y selecciona Iniciar Servidor
2. En otra instancia, selecciona Iniciar Cliente
3. Ingresa nombre y conéctate
4. ¡Comienza a chatear!

## Puertos

- Servidor escucha en puerto **12345**
