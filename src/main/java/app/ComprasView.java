package app;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class ComprasView {
    private InventoryManager inventoryManager;
    private VBox root;

    public ComprasView(InventoryManager manager) {
        this.inventoryManager = manager;
        crearVista();
    }

    private void crearVista() {
        root = new VBox(10);
        root.setPadding(new Insets(15));

        Label titulo = new Label("Registrar Compras");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        root.getChildren().add(titulo);

        TextField txtProveedor = new TextField();
        txtProveedor.setPromptText("Nombre del proveedor");
        
        TextField txtProducto = new TextField();
        txtProducto.setPromptText("ID Producto");
        
        Spinner<Integer> spinCantidad = new Spinner<>(1, 1000, 1);
        spinCantidad.setPrefWidth(80);
        
        TextField txtPrecio = new TextField();
        txtPrecio.setPromptText("Precio unitario");

        Button btnRegistrar = new Button("Registrar Compra");
        btnRegistrar.setStyle("-fx-padding: 10px 20px; -fx-background-color: #e74c3c; -fx-text-fill: white;");
        btnRegistrar.setOnAction(e -> {
            try {
                int idProducto = Integer.parseInt(txtProducto.getText());
                int cantidad = spinCantidad.getValue();
                double precio = Double.parseDouble(txtPrecio.getText());

                int idCompra = inventoryManager.crearCompra(1, txtProveedor.getText(), cantidad * precio);
                if (idCompra > 0) {
                    if (inventoryManager.agregarDetalleCompra(idCompra, idProducto, cantidad, precio)) {
                        AlertDialog.success("Éxito", "Compra registrada: ID " + idCompra).show();
                        limpiar(txtProveedor, txtProducto, txtPrecio);
                    } else {
                        AlertDialog.error("Error", "No se pudo registrar el detalle de compra. Verifica el ID de producto.").show();
                    }
                } else {
                    AlertDialog.error("Error", "No se pudo registrar la compra").show();
                }
            } catch (NumberFormatException ex) {
                AlertDialog.error("Error", "ID de producto o precio inválido").show();
            }
        });

        root.getChildren().addAll(
            new Label("Proveedor:"), txtProveedor,
            new Label("Producto ID:"), txtProducto,
            new Label("Cantidad:"), spinCantidad,
            new Label("Precio unitario:"), txtPrecio,
            btnRegistrar
        );
    }

    private void limpiar(TextField... fields) {
        for (TextField field : fields) field.clear();
    }

    public VBox getRoot() {
        return root;
    }
}
