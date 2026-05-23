package app;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;
import java.util.Map;

public class ProductosView {
    private InventoryManager inventoryManager;
    private VBox root;
    private TableView<Map<String, Object>> tablaProductos;
    private TextField txtCodigo;
    private TextField txtNombre;
    private ComboBox<String> cbCategoria;
    private TextField txtPrecio;
    private TextField txtStock;
    private Button btnAgregar;
    private Button btnGuardar;
    private boolean enEdicion = false;
    private Map<String, Object> productoSeleccionado;

    public ProductosView(InventoryManager manager) {
        this.inventoryManager = manager;
        crearVista();
    }

    private void crearVista() {
        root = new VBox(10);
        root.setPadding(new Insets(15));

        // Título
        Label titulo = new Label("Gestión de Productos");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        root.getChildren().add(titulo);

        // Panel de entrada
        HBox panelEntrada = crearPanelEntrada();
        root.getChildren().add(panelEntrada);

        // Tabla
        tablaProductos = new TableView<>();
        tablaProductos.setPrefHeight(400);
        crearColumnasTabla();
        tablaProductos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                productoSeleccionado = newSelection;
                cargarProductoSeleccionado();
            }
        });
        root.getChildren().add(tablaProductos);

        // Botones de acción
        HBox botonesAccion = new HBox(10);
        botonesAccion.setPadding(new Insets(10));

        btnAgregar = new Button("✓ Agregar");
        btnAgregar.setStyle("-fx-padding: 8px 20px; -fx-background-color: #27ae60; -fx-text-fill: white;");
        btnAgregar.setOnAction(e -> agregarProducto());

        Button btnEditar = new Button("✎ Editar");
        btnEditar.setStyle("-fx-padding: 8px 20px; -fx-background-color: #3498db; -fx-text-fill: white;");
        btnEditar.setOnAction(e -> iniciarEdicionProducto());

        btnGuardar = new Button("💾 Guardar");
        btnGuardar.setStyle("-fx-padding: 8px 20px; -fx-background-color: #2980b9; -fx-text-fill: white;");
        btnGuardar.setDisable(true);
        btnGuardar.setOnAction(e -> guardarProducto());

        Button btnEliminar = new Button("✗ Eliminar");
        btnEliminar.setStyle("-fx-padding: 8px 20px; -fx-background-color: #e74c3c; -fx-text-fill: white;");
        btnEliminar.setOnAction(e -> eliminarProducto());

        Button btnActualizar = new Button("↻ Actualizar");
        btnActualizar.setStyle("-fx-padding: 8px 20px; -fx-background-color: #f39c12; -fx-text-fill: white;");
        btnActualizar.setOnAction(e -> actualizarTabla());

        Button btnRegresar = new Button("◀ Regresar");
        btnRegresar.setStyle("-fx-padding: 8px 20px; -fx-background-color: #95a5a6; -fx-text-fill: white;");
        btnRegresar.setOnAction(e -> cerrarVentana());

        botonesAccion.getChildren().addAll(btnAgregar, btnEditar, btnGuardar, btnEliminar, btnActualizar, btnRegresar);
        root.getChildren().add(botonesAccion);

        actualizarTabla();
    }

    private HBox crearPanelEntrada() {
        HBox panel = new HBox(10);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-border-color: #ecf0f1; -fx-border-width: 1; -fx-border-radius: 5;");

        txtCodigo = new TextField();
        txtCodigo.setPromptText("Código producto");
        txtCodigo.setPrefWidth(120);

        txtNombre = new TextField();
        txtNombre.setPromptText("Nombre producto");
        txtNombre.setPrefWidth(200);

        cbCategoria = new ComboBox<>();
        cbCategoria.setPromptText("Categoría");
        cbCategoria.setPrefWidth(150);
        cargarCategorias(cbCategoria);

        txtPrecio = new TextField();
        txtPrecio.setPromptText("Precio venta");
        txtPrecio.setPrefWidth(100);

        txtStock = new TextField();
        txtStock.setPromptText("Stock");
        txtStock.setPrefWidth(80);

        panel.getChildren().addAll(
            new Label("Código:"), txtCodigo,
            new Label("Nombre:"), txtNombre,
            new Label("Categoría:"), cbCategoria,
            new Label("Precio:"), txtPrecio,
            new Label("Stock:"), txtStock
        );

        return panel;
    }

    private void crearColumnasTabla() {
        TableColumn<Map<String, Object>, String> colCodigo = new TableColumn<>("Código");
        colCodigo.setCellValueFactory(param -> 
            new javafx.beans.property.SimpleStringProperty(param.getValue().get("codigo").toString()));

        TableColumn<Map<String, Object>, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(param -> 
            new javafx.beans.property.SimpleStringProperty(param.getValue().get("nombre").toString()));

        TableColumn<Map<String, Object>, String> colCategoria = new TableColumn<>("Categoría");
        colCategoria.setCellValueFactory(param -> 
            new javafx.beans.property.SimpleStringProperty(param.getValue().get("categoria").toString()));

        TableColumn<Map<String, Object>, Number> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(param -> 
            new javafx.beans.property.SimpleObjectProperty<>((Number) param.getValue().get("stock")));

        TableColumn<Map<String, Object>, Number> colPrecio = new TableColumn<>("Precio");
        colPrecio.setCellValueFactory(param -> 
            new javafx.beans.property.SimpleObjectProperty<>((Number) param.getValue().get("precioVenta")));

        tablaProductos.getColumns().addAll(colCodigo, colNombre, colCategoria, colStock, colPrecio);
    }

    private void cargarCategorias(ComboBox<String> combo) {
        List<Map<String, Object>> categorias = inventoryManager.obtenerCategorias();
        for (Map<String, Object> cat : categorias) {
            combo.getItems().add(cat.get("nombre").toString());
        }
    }

    private void agregarProducto() {
        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String categoriaNombre = cbCategoria.getValue();
        String precioTexto = txtPrecio.getText().trim();
        String stockTexto = txtStock.getText().trim();

        if (codigo.isEmpty() || nombre.isEmpty() || categoriaNombre == null || precioTexto.isEmpty() || stockTexto.isEmpty()) {
            AlertDialog.error("Agregar Producto", "Complete todos los campos antes de agregar el producto.");
            return;
        }

        double precioVenta;
        int stockInicial;
        try {
            precioVenta = Double.parseDouble(precioTexto);
            stockInicial = Integer.parseInt(stockTexto);
        } catch (NumberFormatException e) {
            AlertDialog.error("Agregar Producto", "Ingrese un precio y stock válidos.");
            return;
        }

        int categoriaId = obtenerCategoriaId(categoriaNombre);
        if (categoriaId < 0) {
            AlertDialog.error("Agregar Producto", "La categoría seleccionada no es válida.");
            return;
        }

        boolean agregado = inventoryManager.crearProducto(codigo, nombre, categoriaId, precioVenta, precioVenta, stockInicial, "unidad");
        if (agregado) {
            AlertDialog.success("Agregar Producto", "Producto agregado correctamente.");
            limpiarCampos();
            actualizarTabla();
        } else {
            AlertDialog.error("Agregar Producto", "No se pudo agregar el producto. Revise el registro de errores.");
        }
    }

    private int obtenerCategoriaId(String nombreCategoria) {
        List<Map<String, Object>> categorias = inventoryManager.obtenerCategorias();
        for (Map<String, Object> categoria : categorias) {
            if (nombreCategoria.equals(categoria.get("nombre"))) {
                return (int) categoria.get("id");
            }
        }
        return -1;
    }

    private void limpiarCampos() {
        txtCodigo.clear();
        txtNombre.clear();
        cbCategoria.getSelectionModel().clearSelection();
        txtPrecio.clear();
        txtStock.clear();
    }

    private void iniciarEdicionProducto() {
        if (productoSeleccionado == null) {
            AlertDialog.error("Editar Producto", "Seleccione un producto antes de editar.");
            return;
        }

        enEdicion = true;
        btnGuardar.setDisable(false);
        btnAgregar.setDisable(true);
        AlertDialog.success("Modo edición", "Modifique los campos y presione Guardar.");
    }

    private void guardarProducto() {
        if (productoSeleccionado == null) {
            AlertDialog.error("Guardar Producto", "No hay producto seleccionado para guardar.");
            return;
        }

        String codigo = txtCodigo.getText().trim();
        String nombre = txtNombre.getText().trim();
        String categoriaNombre = cbCategoria.getValue();
        String precioTexto = txtPrecio.getText().trim();
        String stockTexto = txtStock.getText().trim();

        if (codigo.isEmpty() || nombre.isEmpty() || categoriaNombre == null || precioTexto.isEmpty() || stockTexto.isEmpty()) {
            AlertDialog.error("Guardar Producto", "Complete todos los campos antes de guardar.");
            return;
        }

        double precioVenta;
        int stockActual;
        try {
            precioVenta = Double.parseDouble(precioTexto);
            stockActual = Integer.parseInt(stockTexto);
        } catch (NumberFormatException e) {
            AlertDialog.error("Guardar Producto", "Ingrese un precio y stock válidos.");
            return;
        }

        int categoriaId = obtenerCategoriaId(categoriaNombre);
        if (categoriaId < 0) {
            AlertDialog.error("Guardar Producto", "La categoría seleccionada no es válida.");
            return;
        }

        int idProducto = (int) productoSeleccionado.get("id");
        boolean actualizado = inventoryManager.actualizarProducto(idProducto, codigo, nombre, categoriaId, precioVenta, stockActual);
        if (actualizado) {
            AlertDialog.success("Guardar Producto", "Producto guardado correctamente.");
            limpiarCampos();
            productoSeleccionado = null;
            enEdicion = false;
            btnGuardar.setDisable(true);
            btnAgregar.setDisable(false);
            actualizarTabla();
        } else {
            AlertDialog.error("Guardar Producto", "No se pudo guardar el producto.");
        }
    }

    private void eliminarProducto() {
        if (productoSeleccionado == null) {
            AlertDialog.error("Eliminar Producto", "Seleccione un producto antes de eliminar.");
            return;
        }

        int idProducto = (int) productoSeleccionado.get("id");
        boolean eliminado = inventoryManager.eliminarProducto(idProducto);
        if (eliminado) {
            AlertDialog.success("Eliminar Producto", "Producto eliminado correctamente.");
            limpiarCampos();
            productoSeleccionado = null;
            actualizarTabla();
        } else {
            AlertDialog.error("Eliminar Producto", "No se pudo eliminar el producto.");
        }
    }

    private void cargarProductoSeleccionado() {
        if (productoSeleccionado == null) {
            return;
        }
        txtCodigo.setText(productoSeleccionado.get("codigo").toString());
        txtNombre.setText(productoSeleccionado.get("nombre").toString());
        txtPrecio.setText(productoSeleccionado.get("precioVenta").toString());
        txtStock.setText(productoSeleccionado.get("stock").toString());
        String categoria = productoSeleccionado.get("categoria") != null ? productoSeleccionado.get("categoria").toString() : null;
        if (categoria != null) {
            cbCategoria.getSelectionModel().select(categoria);
        }
    }

    private void actualizarTabla() {
        tablaProductos.getItems().clear();
        List<Map<String, Object>> productos = inventoryManager.obtenerProductos();
        for (Map<String, Object> prod : productos) {
            tablaProductos.getItems().add(prod);
        }
    }

    private void cerrarVentana() {
        Stage stage = (Stage) root.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    public VBox getRoot() {
        return root;
    }
}
