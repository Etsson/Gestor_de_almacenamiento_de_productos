package app;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.List;
import java.util.Map;

public class VentasView {
    private InventoryManager inventoryManager;
    private VBox root;

    public VentasView(InventoryManager manager) {
        this.inventoryManager = manager;
        crearVista();
    }

    private void crearVista() {
        root = new VBox(10);
        root.setPadding(new Insets(15));

        Label titulo = new Label("Registrar Ventas");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        root.getChildren().add(titulo);

        TextField txtCliente = new TextField();
        txtCliente.setPromptText("Nombre cliente");
        
        ComboBox<String> comboProducto = new ComboBox<>();
        comboProducto.setPromptText("Seleccione un producto");
        comboProducto.setPrefWidth(240);
        List<Map<String, Object>> productos = inventoryManager.obtenerProductos();
        for (Map<String, Object> producto : productos) {
            comboProducto.getItems().add(producto.get("id") + " - " + producto.get("nombre"));
        }
        
        Spinner<Integer> spinCantidad = new Spinner<>(1, 1000, 1);
        spinCantidad.setPrefWidth(80);
        
        TextField txtPrecio = new TextField();
        txtPrecio.setPromptText("Precio unitario");
        
        TextField txtDescuento = new TextField();
        txtDescuento.setPromptText("Descuento (opcional)");
        txtDescuento.setText("0");

        Button btnRegistrar = new Button("Registrar Venta");
        btnRegistrar.setStyle("-fx-padding: 10px 20px; -fx-background-color: #27ae60; -fx-text-fill: white;");
        btnRegistrar.setOnAction(e -> {
            try {
                String selected = comboProducto.getValue();
                if (selected == null || selected.isBlank()) {
                    AlertDialog.error("Error", "Seleccione un producto válido").show();
                    return;
                }
                int idProducto = Integer.parseInt(selected.split(" - ")[0]);
                int cantidad = spinCantidad.getValue();
                double precio = Double.parseDouble(txtPrecio.getText());
                double descuento = Double.parseDouble(txtDescuento.getText());
                double total = (cantidad * precio) - descuento;
                
                int idVenta = inventoryManager.crearVenta(1, txtCliente.getText(), total, descuento);
                if (idVenta > 0) {
                    if (inventoryManager.agregarDetalleVenta(idVenta, idProducto, cantidad, precio)) {
                        AlertDialog.success("Éxito", "Venta registrada: ID " + idVenta).show();
                        limpiar(txtCliente, txtPrecio, txtDescuento);
                        comboProducto.getSelectionModel().clearSelection();
                    } else {
                        AlertDialog.error("Error", "No se pudo registrar el detalle de venta. Verifica el producto seleccionado.").show();
                    }
                } else {
                    AlertDialog.error("Error", "No se pudo registrar la venta").show();
                }
            } catch (NumberFormatException ex) {
                AlertDialog.error("Error", "Precio o descuento inválido").show();
            }
        });

        root.getChildren().addAll(
            new Label("Cliente:"), txtCliente,
            new Label("Producto:"), comboProducto,
            new Label("Cantidad:"), spinCantidad,
            new Label("Precio unitario:"), txtPrecio,
            new Label("Descuento:"), txtDescuento,
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
