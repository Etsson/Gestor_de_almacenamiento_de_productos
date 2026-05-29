# Gestor de Almacenamiento de Productos

Sistema de gestión de inventario de escritorio desarrollado en Java con JavaFX y SQLite.

## Características

- Gestión de productos, categorías y usuarios
- Registro de ventas y compras con detalle por ítem
- Control de stock automático por movimientos
- Kardex de movimientos de inventario
- Interfaz gráfica moderna con JavaFX
- Base de datos local con SQLite

## Estructura

```
src/
├── main/java/app/
│   ├── App.java              - Punto de entrada
│   ├── MenuPrincipal.java    - Menú principal
│   ├── InventoryManager.java - Lógica CRUD central
│   ├── DatabaseInit.java     - Inicialización de BD
│   ├── ProductosView.java    - Vista de productos
│   ├── VentasView.java       - Vista de ventas
│   ├── ComprasView.java      - Vista de compras
│   ├── KardexView.java       - Vista del kardex
│   ├── CategoriasView.java   - Vista de categorías
│   └── UsuariosView.java     - Vista de usuarios
└── test/java/app/
    └── InventoryManagerTest.java - Pruebas unitarias
```

## Requisitos

- Java JDK 11+
- JavaFX SDK

## Compilación y Ejecución

```bat
compilar_pruebas.bat   REM Compila el proyecto principal y las pruebas
run.bat                REM Ejecuta la aplicación
```

---

## Pruebas Unitarias

Las pruebas unitarias cubren la clase `InventoryManager`, que contiene toda la lógica de negocio del sistema. Se utiliza una base de datos **SQLite en memoria** para cada prueba, garantizando aislamiento total sin afectar la base de datos real (`MERCADO.db`).

### Dependencias de testing (`lib/test/`)

| JAR | Versión | Descripción |
|-----|---------|-------------|
| `junit-4.13.2.jar` | 4.13.2 | Framework de pruebas JUnit 4 |
| `hamcrest-core-1.3.jar` | 1.3 | Librería de assertions |
| `sqlite-jdbc-3.44.0.0.jar` | 3.44.0.0 | Driver SQLite para BD en memoria |

### Pruebas implementadas

| # | Nombre | Qué verifica |
|---|--------|--------------|
| 1 | `crearProducto_debeRetornarVerdadero` | `crearProducto()` retorna `true` al insertar un producto válido |
| 2 | `listarProductos_debeRetornarListaNoVacia` | `obtenerProductos()` devuelve la lista de productos activos |
| 3 | `crearVenta_debeRetornarIdValido` | `crearVenta()` retorna un ID positivo al registrar una venta |
| 4 | `agregarDetalleVenta_debeRegistrarMovimientoEnKardex` | Al vender, el kardex registra un movimiento de tipo `"venta"` |
| 5 | `eliminarProducto_debeMarcarsComoInactivo` | Soft-delete: el producto deja de aparecer en `obtenerProductos()` |
| 6 | `crearCategoria_debeRetornarVerdadero` | `crearCategoria()` inserta correctamente una categoría nueva |
| 7 | `agregarCompra_debeRegistrarMovimientoDeEntradaEnKardex` | Al comprar, el kardex registra un movimiento de tipo `"compra"` |

### Cómo ejecutar las pruebas

```bat
REM Paso 1: Compilar el proyecto y las pruebas
compilar_pruebas.bat

REM Paso 2: Ejecutar las pruebas
ejecutar_pruebas.bat
```

> **Nota:** Cada prueba crea su propio esquema limpio en memoria (`@Before`) y lo descarta al finalizar (`@After`). La base de datos `MERCADO.db` nunca es modificada durante las pruebas.
