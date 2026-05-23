package app;

import java.sql.*;
import java.nio.file.Paths;

public class DatabaseInit {
    private static final String DB_URL = "jdbc:sqlite:" + Paths.get(System.getProperty("user.dir"), "MERCADO.db").toString();
    private static Connection conexion;

    public static void inicializarBD() {
        try {
            Class.forName("org.sqlite.JDBC");
            conexion = DriverManager.getConnection(DB_URL);
            crearTablas();
            inicializarDatos();
            System.out.println("[DB] Base de datos inicializada correctamente");
        } catch (ClassNotFoundException e) {
            System.err.println("[ERROR] Driver SQLite no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("[ERROR] No se pudo conectar a la BD: " + e.getMessage());
        }
    }

    private static void crearTablas() throws SQLException {
        try (Statement stmt = conexion.createStatement()) {
            // Si existe un esquema antiguo incompatible, eliminarlo para recrear correctamente
            if (isIncompatibleUsuarioTable()) {
                stmt.execute("DROP TABLE IF EXISTS usuarios");
                System.out.println("[DB] Tabla 'usuarios' antigua eliminada para recreación");
            }

            // Tabla de Categorías
            stmt.execute("CREATE TABLE IF NOT EXISTS categorias (" +
                    "id_categoria INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre VARCHAR(100) NOT NULL UNIQUE," +
                    "descripcion TEXT," +
                    "estado VARCHAR(20) DEFAULT 'activo'," +
                    "fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");
            System.out.println("[DB] Tabla 'categorias' creada/verificada");

            // Tabla de Usuarios
            stmt.execute("CREATE TABLE IF NOT EXISTS usuarios (" +
                    "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username VARCHAR(50) NOT NULL UNIQUE," +
                    "password_hash VARCHAR(256) NOT NULL," +
                    "nombre_completo VARCHAR(100) NOT NULL," +
                    "email VARCHAR(100)," +
                    "rol VARCHAR(20) DEFAULT 'vendedor'," +
                    "estado VARCHAR(20) DEFAULT 'activo'," +
                    "fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");
            System.out.println("[DB] Tabla 'usuarios' creada/verificada");

            // Tabla de Productos
            stmt.execute("CREATE TABLE IF NOT EXISTS productos (" +
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
            System.out.println("[DB] Tabla 'productos' creada/verificada");

            // Tabla de Compras
            stmt.execute("CREATE TABLE IF NOT EXISTS compras (" +
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
            System.out.println("[DB] Tabla 'compras' creada/verificada");

            // Tabla de Detalle de Compras
            stmt.execute("CREATE TABLE IF NOT EXISTS detalle_compras (" +
                    "id_detalle_compra INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "id_compra INTEGER NOT NULL," +
                    "id_producto INTEGER NOT NULL," +
                    "cantidad INTEGER NOT NULL," +
                    "precio_unitario DECIMAL(10,2) NOT NULL," +
                    "subtotal DECIMAL(12,2) NOT NULL," +
                    "FOREIGN KEY(id_compra) REFERENCES compras(id_compra)," +
                    "FOREIGN KEY(id_producto) REFERENCES productos(id_producto)" +
                    ")");
            System.out.println("[DB] Tabla 'detalle_compras' creada/verificada");

            // Tabla de Ventas
            stmt.execute("CREATE TABLE IF NOT EXISTS ventas (" +
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
            System.out.println("[DB] Tabla 'ventas' creada/verificada");

            // Tabla de Detalle de Ventas
            stmt.execute("CREATE TABLE IF NOT EXISTS detalle_ventas (" +
                    "id_detalle_venta INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "id_venta INTEGER NOT NULL," +
                    "id_producto INTEGER NOT NULL," +
                    "cantidad INTEGER NOT NULL," +
                    "precio_unitario DECIMAL(10,2) NOT NULL," +
                    "subtotal DECIMAL(12,2) NOT NULL," +
                    "FOREIGN KEY(id_venta) REFERENCES ventas(id_venta)," +
                    "FOREIGN KEY(id_producto) REFERENCES productos(id_producto)" +
                    ")");
            System.out.println("[DB] Tabla 'detalle_ventas' creada/verificada");

            // Tabla de Kardex
            stmt.execute("CREATE TABLE IF NOT EXISTS kardex_movimientos (" +
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
                    "FOREIGN KEY(id_producto) REFERENCES productos(id_producto)," +
                    "FOREIGN KEY(id_usuario) REFERENCES usuarios(id_usuario)" +
                    ")");
            System.out.println("[DB] Tabla 'kardex_movimientos' creada/verificada");
            garantizarColumna("kardex_movimientos", "referencia_id", "INTEGER");
            garantizarColumna("kardex_movimientos", "referencia_tipo", "VARCHAR(20)");
            garantizarColumna("kardex_movimientos", "stock_anterior", "INTEGER");
            garantizarColumna("kardex_movimientos", "stock_nuevo", "INTEGER");
            garantizarColumna("kardex_movimientos", "observaciones", "TEXT");
        }
    }

    private static void garantizarColumna(String tabla, String columna, String definicion) throws SQLException {
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery("PRAGMA table_info('" + tabla + "')")) {
            boolean existe = false;
            while (rs.next()) {
                if (columna.equalsIgnoreCase(rs.getString("name"))) {
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                stmt.execute("ALTER TABLE " + tabla + " ADD COLUMN " + columna + " " + definicion);
                System.out.println("[DB] Columna '" + columna + "' agregada a la tabla '" + tabla + "'");
            }
        }
    }

    private static void inicializarDatos() throws SQLException {
        try (Statement stmt = conexion.createStatement()) {
            stmt.execute("INSERT OR IGNORE INTO categorias VALUES " +
                    "(1, 'Electrónica', 'Productos electrónicos varios', 'activo', CURRENT_TIMESTAMP)," +
                    "(2, 'Ropa', 'Prendas de vestir', 'activo', CURRENT_TIMESTAMP)," +
                    "(3, 'Alimentos', 'Productos de alimentación', 'activo', CURRENT_TIMESTAMP)," +
                    "(4, 'Hogar', 'Artículos para el hogar', 'activo', CURRENT_TIMESTAMP)," +
                    "(5, 'Deportes', 'Equipos y ropa deportiva', 'activo', CURRENT_TIMESTAMP)");

            stmt.execute("INSERT OR IGNORE INTO usuarios VALUES " +
                    "(1, 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'Administrador', 'admin@mercado.com', 'admin', 'activo', CURRENT_TIMESTAMP)," +
                    "(2, 'vendedor1', 'e5fa44f2b31c1fb553b6021e7aab6b74476544c0dcc2d8ecc0f32d66e56b237e', 'Juan Vendedor', 'juan@mercado.com', 'vendedor', 'activo', CURRENT_TIMESTAMP)," +
                    "(3, 'vendedor2', '5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacb11', 'María Vendedor', 'maria@mercado.com', 'vendedor', 'activo', CURRENT_TIMESTAMP)");

            System.out.println("[DB] Datos iniciales cargados");
        }
    }

    private static boolean isIncompatibleUsuarioTable() throws SQLException {
        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery("PRAGMA table_info('usuarios')")) {
            int count = 0;
            while (rs.next()) {
                count++;
            }
            return count > 0 && count != 8;
        }
    }

    public static Connection getConexion() {
        if (conexion == null) {
            inicializarBD();
        }
        return conexion;
    }

    public static void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("[DB] Conexión cerrada");
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] No se pudo cerrar la BD: " + e.getMessage());
        }
    }
}
