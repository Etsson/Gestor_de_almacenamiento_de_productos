-- =====================================================
-- SISTEMA DE GESTIÓN DE INVENTARIO Y VENTAS
-- =====================================================
-- Base de datos: MERCADO
-- Motor: SQLite / MySQL / PostgreSQL compatible

-- =====================================================
-- 1. TABLA DE CATEGORÍAS
-- =====================================================
CREATE TABLE IF NOT EXISTS categorias (
    id_categoria INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    estado VARCHAR(20) DEFAULT 'activo',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- 2. TABLA DE USUARIOS
-- =====================================================
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(256) NOT NULL,
    nombre_completo VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    rol VARCHAR(20) DEFAULT 'vendedor',
    estado VARCHAR(20) DEFAULT 'activo',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- 3. TABLA DE PRODUCTOS
-- =====================================================
CREATE TABLE IF NOT EXISTS productos (
    id_producto INTEGER PRIMARY KEY AUTOINCREMENT,
    codigo_producto VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    id_categoria INTEGER NOT NULL,
    precio_costo DECIMAL(10, 2) NOT NULL,
    precio_venta DECIMAL(10, 2) NOT NULL,
    stock_actual INTEGER NOT NULL DEFAULT 0,
    stock_minimo INTEGER DEFAULT 10,
    unidad_medida VARCHAR(20) DEFAULT 'unidad',
    estado VARCHAR(20) DEFAULT 'activo',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria)
);

-- =====================================================
-- 4. TABLA DE COMPRAS
-- =====================================================
CREATE TABLE IF NOT EXISTS compras (
    id_compra INTEGER PRIMARY KEY AUTOINCREMENT,
    numero_compra VARCHAR(50) NOT NULL UNIQUE,
    id_usuario INTEGER NOT NULL,
    fecha_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    proveedor VARCHAR(150),
    total_compra DECIMAL(12, 2) NOT NULL DEFAULT 0,
    observaciones TEXT,
    estado VARCHAR(20) DEFAULT 'completado',
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

-- =====================================================
-- 5. TABLA DE DETALLE DE COMPRAS
-- =====================================================
CREATE TABLE IF NOT EXISTS detalle_compras (
    id_detalle_compra INTEGER PRIMARY KEY AUTOINCREMENT,
    id_compra INTEGER NOT NULL,
    id_producto INTEGER NOT NULL,
    cantidad INTEGER NOT NULL,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(12, 2) NOT NULL,
    FOREIGN KEY (id_compra) REFERENCES compras(id_compra),
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);

-- =====================================================
-- 6. TABLA DE VENTAS
-- =====================================================
CREATE TABLE IF NOT EXISTS ventas (
    id_venta INTEGER PRIMARY KEY AUTOINCREMENT,
    numero_venta VARCHAR(50) NOT NULL UNIQUE,
    id_usuario INTEGER NOT NULL,
    fecha_venta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cliente VARCHAR(150),
    total_venta DECIMAL(12, 2) NOT NULL DEFAULT 0,
    descuento DECIMAL(12, 2) DEFAULT 0,
    observaciones TEXT,
    estado VARCHAR(20) DEFAULT 'completado',
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

-- =====================================================
-- 7. TABLA DE DETALLE DE VENTAS
-- =====================================================
CREATE TABLE IF NOT EXISTS detalle_ventas (
    id_detalle_venta INTEGER PRIMARY KEY AUTOINCREMENT,
    id_venta INTEGER NOT NULL,
    id_producto INTEGER NOT NULL,
    cantidad INTEGER NOT NULL,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(12, 2) NOT NULL,
    FOREIGN KEY (id_venta) REFERENCES ventas(id_venta),
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);

-- =====================================================
-- 8. TABLA DE KARDEX (MOVIMIENTOS DE INVENTARIO)
-- =====================================================
CREATE TABLE IF NOT EXISTS kardex_movimientos (
    id_movimiento INTEGER PRIMARY KEY AUTOINCREMENT,
    id_producto INTEGER NOT NULL,
    tipo_movimiento VARCHAR(20) NOT NULL,
    cantidad INTEGER NOT NULL,
    precio_unitario DECIMAL(10, 2),
    referencia_id INTEGER,
    referencia_tipo VARCHAR(20),
    stock_anterior INTEGER,
    stock_nuevo INTEGER,
    observaciones TEXT,
    fecha_movimiento TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_usuario INTEGER,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

-- =====================================================
-- ÍNDICES PARA OPTIMIZACIÓN
-- =====================================================
CREATE INDEX IF NOT EXISTS idx_productos_categoria ON productos(id_categoria);
CREATE INDEX IF NOT EXISTS idx_productos_codigo ON productos(codigo_producto);
CREATE INDEX IF NOT EXISTS idx_compras_usuario ON compras(id_usuario);
CREATE INDEX IF NOT EXISTS idx_compras_fecha ON compras(fecha_compra);
CREATE INDEX IF NOT EXISTS idx_detalle_compras_compra ON detalle_compras(id_compra);
CREATE INDEX IF NOT EXISTS idx_detalle_compras_producto ON detalle_compras(id_producto);
CREATE INDEX IF NOT EXISTS idx_ventas_usuario ON ventas(id_usuario);
CREATE INDEX IF NOT EXISTS idx_ventas_fecha ON ventas(fecha_venta);
CREATE INDEX IF NOT EXISTS idx_detalle_ventas_venta ON detalle_ventas(id_venta);
CREATE INDEX IF NOT EXISTS idx_detalle_ventas_producto ON detalle_ventas(id_producto);
CREATE INDEX IF NOT EXISTS idx_kardex_producto ON kardex_movimientos(id_producto);
CREATE INDEX IF NOT EXISTS idx_kardex_fecha ON kardex_movimientos(fecha_movimiento);

-- =====================================================
-- DATOS INICIALES DE PRUEBA
-- =====================================================
INSERT OR IGNORE INTO categorias (id_categoria, nombre, descripcion) VALUES
(1, 'Electrónica', 'Productos electrónicos varios'),
(2, 'Ropa', 'Prendas de vestir'),
(3, 'Alimentos', 'Productos de alimentación'),
(4, 'Hogar', 'Artículos para el hogar'),
(5, 'Deportes', 'Equipos y ropa deportiva');

INSERT OR IGNORE INTO usuarios (id_usuario, username, password_hash, nombre_completo, email, rol) VALUES
(1, 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'Administrador', 'admin@mercado.com', 'admin'),
(2, 'vendedor1', 'e5fa44f2b31c1fb553b6021e7aab6b74476544c0dcc2d8ecc0f32d66e56b237e', 'Juan Vendedor', 'juan@mercado.com', 'vendedor'),
(3, 'vendedor2', '5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacb11', 'María Vendedor', 'maria@mercado.com', 'vendedor');
