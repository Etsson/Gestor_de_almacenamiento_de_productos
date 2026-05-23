package app;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;
import java.util.Map;

public class CategoriasView {
    private InventoryManager inventoryManager;
    private VBox root;

    public CategoriasView(InventoryManager manager) {
        this.inventoryManager = manager;
        crearVista();
    }

    private void crearVista() {
        root = new VBox(10);
        root.setPadding(new Insets(15));

        Label titulo = new Label("Gestión de Categorías");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        root.getChildren().add(titulo);

        HBox panelEntrada = new HBox(10);
        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre categoría");
        TextArea txtDescripcion = new TextArea();
        txtDescripcion.setPromptText("Descripción");
        txtDescripcion.setPrefHeight(80);

        Button btnAgregar = new Button("Agregar");
        btnAgregar.setOnAction(e -> {
            if (inventoryManager.crearCategoria(txtNombre.getText(), txtDescripcion.getText())) {
                AlertDialog.success("Éxito", "Categoría agregada").show();
                txtNombre.clear();
                txtDescripcion.clear();
            } else {
                AlertDialog.error("Error", "No se pudo agregar la categoría").show();
            }
        });

        panelEntrada.getChildren().addAll(txtNombre, txtDescripcion, btnAgregar);
        root.getChildren().add(panelEntrada);

        TableView<Map<String, Object>> tabla = new TableView<>();
        actualizarTabla(tabla);
        root.getChildren().add(tabla);

        Button btnRegresar = new Button("Regresar");
        btnRegresar.setStyle("-fx-padding: 8px 20px; -fx-background-color: #95a5a6; -fx-text-fill: white;");
        btnRegresar.setOnAction(e -> cerrarVentana());
        root.getChildren().add(btnRegresar);
    }

    private void actualizarTabla(TableView<Map<String, Object>> tabla) {
        tabla.getItems().clear();
        List<Map<String, Object>> categorias = inventoryManager.obtenerCategorias();
        for (Map<String, Object> cat : categorias) {
            tabla.getItems().add(cat);
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
