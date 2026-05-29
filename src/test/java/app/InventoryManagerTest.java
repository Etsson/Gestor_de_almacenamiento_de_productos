package app;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Pruebas unitarias para InventoryManager.
 * Se usa una base de datos SQLite en memoria para aislar cada prueba
 * sin afectar la base de datos real del proyecto (MERCADO.db).
 */
public class InventoryManagerTest {

    private Connection conexion;
    private InventoryManager manager;

    // ──────────────────────────────────────────────────────────────
    // Configuración antes y después de cada prueba
    // ──────────────────────────────────────────────────────────────

    @Before
    public void configurar() throws Exception {
        // Base de datos completamente en memoria, descartada al cerrar la conexión
        conexion = DriverManager.getConnection("jdbc:sqlite::memory:");
        crearEsquema(conexion);
        insertarDatosBase(conexion);
        manager = new InventoryManager(conexion);
    }

    @After
    public void limpiar() throws Exception {
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
        }
    }

    // ──────────────────────────────────────────────────────────────
    // PRUEBA 1: Crear un producto nuevo
    // ──────────────────────────────────────────────────────────────

    @Test
    public void crearProducto_debeRetornarVerdadero() {
        boolean resultado = manager.crearProducto(
                "TEST-001", "Producto de Prueba", 1,
                50.00, 80.00, 100, "unidad"
        );
        assertTrue("crearProducto() debe retornar true al insertar un producto válido", resultado);
    }

    // ──────────────────────────────────────────────────────────────
    // PRUEBA 2: Listar productos activos
    // ──────────────────────────────────────────────────────────────

    @Test
    public void listarProductos_debeRetornarListaNoVacia() {
        // Insertamos un producto para asegurar que hay datos
        manager.crearProducto("LISTA-001", "Arroz Premium", 1, 10.00, 15.00, 50, "kg");

        List<Map<String, Object>> productos = manager.obtenerProductos();

        assertNotNull("La lista de productos no debe ser null", productos);
        assertFalse("La lista de productos no debe estar vacía", productos.isEmpty());
    }

    // ──────────────────────────────────────────────────────────────
    // PRUEBA 3: Crear una venta y verificar que retorna un ID válido
    // ──────────────────────────────────────────────────────────────

    @Test
    public void crearVenta_debeRetornarIdValido() {
        int idVenta = manager.crearVenta(1, "Cliente Prueba", 200.00, 0.00);

        assertTrue("crearVenta() debe retornar un ID positivo", idVenta > 0);
    }

    // ──────────────────────────────────────────────────────────────
    // PRUEBA 4: Agregar detalle de venta debe registrar movimiento en kardex
    // ──────────────────────────────────────────────────────────────

    @Test
    public void agregarDetalleVenta_debeRegistrarMovimientoEnKardex() {
        // Crear producto con stock suficiente
        manager.crearProducto("VENTA-001", "Televisor 40\"", 1, 300.00, 450.00, 20, "unidad");
        List<Map<String, Object>> productos = manager.obtenerProductos();
        int idProducto = (int) productos.get(0).get("id");

        // Crear venta
        int idVenta = manager.crearVenta(1, "Cliente TV", 450.00, 0.00);

        // Agregar detalle de venta
        boolean detalleAgregado = manager.agregarDetalleVenta(idVenta, idProducto, 1, 450.00);
        assertTrue("agregarDetalleVenta() debe retornar true", detalleAgregado);

        // Verificar que el kardex tiene al menos un movimiento
        List<Map<String, Object>> kardex = manager.obtenerKardex();
        assertFalse("El kardex no debe estar vacío después de una venta", kardex.isEmpty());

        // Verificar que el tipo del último movimiento es "venta"
        String tipoMovimiento = (String) kardex.get(0).get("tipo");
        assertEquals("El tipo de movimiento debe ser 'venta'", "venta", tipoMovimiento);
    }

    // ──────────────────────────────────────────────────────────────
    // PRUEBA 5: Eliminar producto debe marcarlo como inactivo (soft delete)
    // ──────────────────────────────────────────────────────────────

    @Test
    public void eliminarProducto_debeMarcarsComoInactivo() {
        // Crear y luego eliminar un producto
        manager.crearProducto("DEL-001", "Producto Temporal", 1, 5.00, 10.00, 5, "unidad");
        List<Map<String, Object>> antesDeEliminar = manager.obtenerProductos();
        assertFalse("Debe haber al menos un producto antes de eliminar", antesDeEliminar.isEmpty());

        int idProducto = (int) antesDeEliminar.get(0).get("id");
        boolean resultado = manager.eliminarProducto(idProducto);
        assertTrue("eliminarProducto() debe retornar true", resultado);

        // obtenerProductos() solo retorna activos; la lista debe estar vacía ahora
        List<Map<String, Object>> despuesDeEliminar = manager.obtenerProductos();
        assertTrue("La lista de productos activos debe estar vacía después de eliminar el único producto", despuesDeEliminar.isEmpty());
    }

    // ──────────────────────────────────────────────────────────────
    // PRUEBA 6: Crear una categoría nueva
    // ──────────────────────────────────────────────────────────────

    @Test
    public void crearCategoria_debeRetornarVerdadero() {
        boolean resultado = manager.crearCategoria("Tecnología", "Gadgets y accesorios tecnológicos");
        assertTrue("crearCategoria() debe retornar true al insertar una categoría válida", resultado);

        List<Map<String, Object>> categorias = manager.obtenerCategorias();
        // La categoría base ya tiene 1; ahora debe haber 2
        assertEquals("Debe haber exactamente 2 categorías (la base + la nueva)", 2, categorias.size());
    }

    // ──────────────────────────────────────────────────────────────
    // PRUEBA 7: Agregar compra debe registrar movimiento de entrada en kardex
    // ──────────────────────────────────────────────────────────────

    @Test
    public void agregarCompra_debeRegistrarMovimientoDeEntradaEnKardex() {
        // Crear producto con stock inicial 0
        manager.crearProducto("COMP-001", "Silla Ergonómica", 1, 80.00, 120.00, 0, "unidad");
        List<Map<String, Object>> productos = manager.obtenerProductos();
        int idProducto = (int) productos.get(0).get("id");

        // Crear compra y agregar detalle
        int idCompra = manager.crearCompra(1, "Proveedor ABC", 800.00);
        assertTrue("crearCompra() debe retornar un ID positivo", idCompra > 0);

        boolean detalleAgregado = manager.agregarDetalleCompra(idCompra, idProducto, 10, 80.00);
        assertTrue("agregarDetalleCompra() debe retornar true", detalleAgregado);

        // Verificar kardex: debe existir un movimiento de tipo "compra"
        List<Map<String, Object>> kardex = manager.obtenerKardex();
        assertFalse("El kardex no debe estar vacío después de una compra", kardex.isEmpty());

        String tipoMovimiento = (String) kardex.get(0).get("tipo");
        assertEquals("El tipo de movimiento debe ser 'compra'", "compra", tipoMovimiento);
    }

    // ──────────────────────────────────────────────────────────────
    // Helpers: Esquema y datos base en memoria
    // ──────────────────────────────────────────────────────────────

    /**
     * Crea todas las tablas necesarias en la BD en memoria,
     * replicando el esquema real del sistema.
     */
    private void crearEsquema(Connection conn) throws Exception {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE categorias (" +
                    "id_categoria INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre VARCHAR(100) NOT NULL UNIQUE," +
                    "descripcion TEXT," +
                    "estado VARCHAR(20) DEFAULT 'activo'," +
                    "fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");

            stmt.execute("CREATE TABLE usuarios (" +
                    "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username VARCHAR(50) NOT NULL UNIQUE," +
                    "password_hash VARCHAR(256) NOT NULL," +
                    "nombre_completo VARCHAR(100) NOT NULL," +
                    "email VARCHAR(100)," +
                    "rol VARCHAR(20) DEFAULT 'vendedor'," +
                    "estado VARCHAR(20) DEFAULT 'activo'," +
                    "fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");

            stmt.execute("CREATE TABLE productos (" +
                    "id_producto INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "codigo_producto VARCHAR(50) NOT NULL UNIQUE," +
                    "nombre VARCHAR(150) NOT NULL," +
                    "descripcion TEXT," +
                    "id_categoria INTEGER NOT NULL," +
                    "precio_costo DECIMAL(10,2) NOT NULL," +
                    "precio_venta DECIMAL(10,2) NOT NULL," +
                    "stock_actual INTEGER NOT NULL DEFAULT 0," +
                    "stock_minimo INTEGER DEFAULT 10," +
                    "unidad_medida VARCHAR(20) DEFAULT 'unidad'," +
                    "estado VARCHAR(20) DEFAULT 'activo'," +
                    "fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY(id_categoria) REFERENCES categorias(id_categoria)" +
                    ")");

            stmt.execute("CREATE TABLE ventas (" +
                    "id_venta INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "numero_venta VARCHAR(50) NOT NULL UNIQUE," +
                    "id_usuario INTEGER NOT NULL," +
                    "fecha_venta TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "cliente VARCHAR(150)," +
                    "total_venta DECIMAL(12,2) NOT NULL DEFAULT 0," +
                    "descuento DECIMAL(12,2) DEFAULT 0," +
                    "observaciones TEXT," +
                    "estado VARCHAR(20) DEFAULT 'completado'," +
                    "FOREIGN KEY(id_usuario) REFERENCES usuarios(id_usuario)" +
                    ")");

            stmt.execute("CREATE TABLE detalle_ventas (" +
                    "id_detalle_venta INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "id_venta INTEGER NOT NULL," +
                    "id_producto INTEGER NOT NULL," +
                    "cantidad INTEGER NOT NULL," +
                    "precio_unitario DECIMAL(10,2) NOT NULL," +
                    "subtotal DECIMAL(12,2) NOT NULL," +
                    "FOREIGN KEY(id_venta) REFERENCES ventas(id_venta)," +
                    "FOREIGN KEY(id_producto) REFERENCES productos(id_producto)" +
                    ")");

            stmt.execute("CREATE TABLE compras (" +
                    "id_compra INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "numero_compra VARCHAR(50) NOT NULL UNIQUE," +
                    "id_usuario INTEGER NOT NULL," +
                    "fecha_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "proveedor VARCHAR(150)," +
                    "total_compra DECIMAL(12,2) NOT NULL DEFAULT 0," +
                    "observaciones TEXT," +
                    "estado VARCHAR(20) DEFAULT 'completado'," +
                    "FOREIGN KEY(id_usuario) REFERENCES usuarios(id_usuario)" +
                    ")");

            stmt.execute("CREATE TABLE detalle_compras (" +
                    "id_detalle_compra INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "id_compra INTEGER NOT NULL," +
                    "id_producto INTEGER NOT NULL," +
                    "cantidad INTEGER NOT NULL," +
                    "precio_unitario DECIMAL(10,2) NOT NULL," +
                    "subtotal DECIMAL(12,2) NOT NULL," +
                    "FOREIGN KEY(id_compra) REFERENCES compras(id_compra)," +
                    "FOREIGN KEY(id_producto) REFERENCES productos(id_producto)" +
                    ")");

            stmt.execute("CREATE TABLE kardex_movimientos (" +
                    "id_movimiento INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "id_producto INTEGER NOT NULL," +
                    "tipo_movimiento VARCHAR(20) NOT NULL," +
                    "cantidad INTEGER NOT NULL," +
                    "precio_unitario DECIMAL(10,2)," +
                    "referencia_id INTEGER," +
                    "referencia_tipo VARCHAR(20)," +
                    "stock_anterior INTEGER," +
                    "stock_nuevo INTEGER," +
                    "observaciones TEXT," +
                    "fecha_movimiento TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "id_usuario INTEGER," +
                    "FOREIGN KEY(id_producto) REFERENCES productos(id_producto)" +
                    ")");
        }
    }

    /**
     * Inserta una categoría y un usuario mínimos para que
     * las pruebas puedan referenciarlos por FK.
     */
    private void insertarDatosBase(Connection conn) throws Exception {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("INSERT INTO categorias (id_categoria, nombre, descripcion) " +
                    "VALUES (1, 'General', 'Categoría general de prueba')");

            stmt.execute("INSERT INTO usuarios " +
                    "(id_usuario, username, password_hash, nombre_completo, rol) " +
                    "VALUES (1, 'admin_test', 'hash_prueba', 'Admin Prueba', 'admin')");
        }
    }
}
