package app;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gestor central para todas las operaciones CRUD del sistema de inventario
 */
public class InventoryManager {
    private Connection connection;

    public InventoryManager(Connection conn) {
        this.connection = conn;
    }

    // ==================== PRODUCTOS ====================
    public boolean crearProducto(String codigo, String nombre, int idCategoria, 
                                 double precioCosto, double precioVenta, int stockInicial, String unidad) {
        String sql = "INSERT INTO productos (codigo_producto, nombre, id_categoria, precio_costo, precio_venta, stock_actual, unidad_medida) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, codigo);
            pstmt.setString(2, nombre);
            pstmt.setInt(3, idCategoria);
            pstmt.setDouble(4, precioCosto);
            pstmt.setDouble(5, precioVenta);
            pstmt.setInt(6, stockInicial);
            pstmt.setString(7, unidad);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("[ERROR] Error creando producto: " + e.getMessage());
            return false;
        }
    }

    public List<Map<String, Object>> obtenerProductos() {
        List<Map<String, Object>> productos = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre as categoria FROM productos p " +
                     "LEFT JOIN categorias c ON p.id_categoria = c.id_categoria " +
                     "WHERE p.estado = 'activo'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> producto = new HashMap<>();
                producto.put("id", rs.getInt("id_producto"));
                producto.put("codigo", rs.getString("codigo_producto"));
                producto.put("nombre", rs.getString("nombre"));
                producto.put("categoria", rs.getString("categoria"));
                producto.put("precioCosto", rs.getDouble("precio_costo"));
                producto.put("precioVenta", rs.getDouble("precio_venta"));
                producto.put("stock", rs.getInt("stock_actual"));
                productos.add(producto);
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error obteniendo productos: " + e.getMessage());
        }
        return productos;
    }

    public boolean actualizarProducto(int idProducto, String codigoProducto, String nombre, int idCategoria, double precioVenta, int stockActual) {
        String sql = "UPDATE productos SET codigo_producto = ?, nombre = ?, id_categoria = ?, precio_venta = ?, stock_actual = ? WHERE id_producto = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, codigoProducto);
            pstmt.setString(2, nombre);
            pstmt.setInt(3, idCategoria);
            pstmt.setDouble(4, precioVenta);
            pstmt.setInt(5, stockActual);
            pstmt.setInt(6, idProducto);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("[ERROR] Error actualizando producto: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarProducto(int idProducto) {
        String sql = "UPDATE productos SET estado = 'inactivo' WHERE id_producto = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idProducto);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("[ERROR] Error eliminando producto: " + e.getMessage());
            return false;
        }
    }

    // ==================== CATEGORÍAS ====================
    public List<Map<String, Object>> obtenerCategorias() {
        List<Map<String, Object>> categorias = new ArrayList<>();
        String sql = "SELECT * FROM categorias WHERE estado = 'activo'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> categoria = new HashMap<>();
                categoria.put("id", rs.getInt("id_categoria"));
                categoria.put("nombre", rs.getString("nombre"));
                categoria.put("descripcion", rs.getString("descripcion"));
                categorias.add(categoria);
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error obteniendo categorías: " + e.getMessage());
        }
        return categorias;
    }

    public boolean crearCategoria(String nombre, String descripcion) {
        String sql = "INSERT INTO categorias (nombre, descripcion) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, descripcion);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("[ERROR] Error creando categoría: " + e.getMessage());
            return false;
        }
    }

    // ==================== COMPRAS ====================
    public int crearCompra(int idUsuario, String proveedor, double totalCompra) {
        String numeroCompra = "CMP-" + System.currentTimeMillis();
        String sql = "INSERT INTO compras (numero_compra, id_usuario, proveedor, total_compra) " +
                     "VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, numeroCompra);
            pstmt.setInt(2, idUsuario);
            pstmt.setString(3, proveedor);
            pstmt.setDouble(4, totalCompra);
            pstmt.executeUpdate();
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;
        } catch (SQLException e) {
            System.err.println("[ERROR] Error creando compra: " + e.getMessage());
            return -1;
        }
    }

    public boolean agregarDetalleCompra(int idCompra, int idProducto, int cantidad, double precioUnitario) {
        if (!existeProducto(idProducto)) {
            System.err.println("[ERROR] Producto no encontrado: " + idProducto);
            return false;
        }

        String sql = "INSERT INTO detalle_compras (id_compra, id_producto, cantidad, precio_unitario, subtotal) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            double subtotal = cantidad * precioUnitario;
            pstmt.setInt(1, idCompra);
            pstmt.setInt(2, idProducto);
            pstmt.setInt(3, cantidad);
            pstmt.setDouble(4, precioUnitario);
            pstmt.setDouble(5, subtotal);
            pstmt.executeUpdate();

            int[] stock = actualizarStock(idProducto, cantidad, "entrada");
            if (stock == null) {
                return false;
            }
            registrarMovimientoKardex(idProducto, "compra", cantidad, precioUnitario, idCompra, "compra", stock[0], stock[1]);
            return true;
        } catch (SQLException e) {
            System.err.println("[ERROR] Error agregando detalle de compra: " + e.getMessage());
            return false;
        }
    }

    // ==================== VENTAS ====================
    public int crearVenta(int idUsuario, String cliente, double totalVenta, double descuento) {
        String numeroVenta = "VTA-" + System.currentTimeMillis();
        String sql = "INSERT INTO ventas (numero_venta, id_usuario, cliente, total_venta, descuento) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, numeroVenta);
            pstmt.setInt(2, idUsuario);
            pstmt.setString(3, cliente);
            pstmt.setDouble(4, totalVenta);
            pstmt.setDouble(5, descuento);
            pstmt.executeUpdate();
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;
        } catch (SQLException e) {
            System.err.println("[ERROR] Error creando venta: " + e.getMessage());
            return -1;
        }
    }

    public boolean agregarDetalleVenta(int idVenta, int idProducto, int cantidad, double precioUnitario) {
        if (!existeProducto(idProducto)) {
            System.err.println("[ERROR] Producto no encontrado: " + idProducto);
            return false;
        }

        String sql = "INSERT INTO detalle_ventas (id_venta, id_producto, cantidad, precio_unitario, subtotal) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            double subtotal = cantidad * precioUnitario;
            pstmt.setInt(1, idVenta);
            pstmt.setInt(2, idProducto);
            pstmt.setInt(3, cantidad);
            pstmt.setDouble(4, precioUnitario);
            pstmt.setDouble(5, subtotal);
            pstmt.executeUpdate();
            
            int[] stock = actualizarStock(idProducto, cantidad, "salida");
            if (stock == null) {
                return false;
            }
            registrarMovimientoKardex(idProducto, "venta", cantidad, precioUnitario, idVenta, "venta", stock[0], stock[1]);
            return true;
        } catch (SQLException e) {
            System.err.println("[ERROR] Error agregando detalle de venta: " + e.getMessage());
            return false;
        }
    }

    // ==================== STOCK Y KARDEX ====================
    private int[] actualizarStock(int idProducto, int cantidad, String tipo) {
        int stockAnterior = obtenerStockActual(idProducto);
        if (stockAnterior < 0) {
            System.err.println("[ERROR] No se puede actualizar stock: producto no existe " + idProducto);
            return null;
        }
        int stockNuevo = tipo.equals("entrada") ? stockAnterior + cantidad : stockAnterior - cantidad;
        String sql = "UPDATE productos SET stock_actual = ? WHERE id_producto = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, stockNuevo);
            pstmt.setInt(2, idProducto);
            int filas = pstmt.executeUpdate();
            if (filas == 0) {
                System.err.println("[ERROR] No se actualizó stock para producto " + idProducto);
                return null;
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error actualizando stock: " + e.getMessage());
            return null;
        }
        return new int[]{stockAnterior, stockNuevo};
    }

    private int obtenerStockActual(int idProducto) {
        String sql = "SELECT stock_actual FROM productos WHERE id_producto = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idProducto);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("stock_actual");
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error obteniendo stock actual: " + e.getMessage());
        }
        return -1;
    }

    private boolean existeProducto(int idProducto) {
        String sql = "SELECT 1 FROM productos WHERE id_producto = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idProducto);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error verificando existencia de producto: " + e.getMessage());
        }
        return false;
    }

    private void registrarMovimientoKardex(int idProducto, String tipoMovimiento, int cantidad, double precioUnitario, int referenciaId, String referenciaTipo, int stockAnterior, int stockNuevo) {
        String sql = "INSERT INTO kardex_movimientos (id_producto, tipo_movimiento, cantidad, precio_unitario, referencia_id, referencia_tipo, stock_anterior, stock_nuevo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idProducto);
            pstmt.setString(2, tipoMovimiento);
            pstmt.setInt(3, cantidad);
            pstmt.setDouble(4, precioUnitario);
            pstmt.setInt(5, referenciaId);
            pstmt.setString(6, referenciaTipo);
            pstmt.setInt(7, stockAnterior);
            pstmt.setInt(8, stockNuevo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[ERROR] Error registrando movimiento kardex: " + e.getMessage());
        }
    }

    public List<Map<String, Object>> obtenerKardex() {
        List<Map<String, Object>> kardex = new ArrayList<>();
        String sql = "SELECT k.*, p.nombre as producto FROM kardex_movimientos k " +
                     "LEFT JOIN productos p ON k.id_producto = p.id_producto " +
                     "ORDER BY k.fecha_movimiento DESC LIMIT 100";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> movimiento = new HashMap<>();
                String producto = rs.getString("producto");
                if (producto == null) {
                    producto = "ID " + rs.getInt("id_producto");
                }
                movimiento.put("producto", producto);
                movimiento.put("tipo", rs.getString("tipo_movimiento"));
                movimiento.put("cantidad", rs.getInt("cantidad"));
                movimiento.put("fecha", rs.getTimestamp("fecha_movimiento"));
                movimiento.put("referencia", rs.getString("referencia_tipo") + " " + rs.getInt("referencia_id"));
                movimiento.put("stockAnterior", rs.getInt("stock_anterior"));
                movimiento.put("stockNuevo", rs.getInt("stock_nuevo"));
                movimiento.put("precioUnitario", rs.getDouble("precio_unitario"));
                kardex.add(movimiento);
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error obteniendo kardex: " + e.getMessage());
        }
        return kardex;
    }

    // ==================== USUARIOS ====================
    public List<Map<String, Object>> obtenerUsuarios() {
        List<Map<String, Object>> usuarios = new ArrayList<>();
        String sql = "SELECT id_usuario, nombre_completo, rol FROM usuarios WHERE estado = 'activo'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> usuario = new HashMap<>();
                usuario.put("id", rs.getInt("id_usuario"));
                usuario.put("nombre", rs.getString("nombre_completo"));
                usuario.put("rol", rs.getString("rol"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Error obteniendo usuarios: " + e.getMessage());
        }
        return usuarios;
    }
}
