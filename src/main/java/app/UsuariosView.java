package app;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.List;
import java.util.Map;

public class UsuariosView {
    private InventoryManager inventoryManager;
    private VBox root;

    public UsuariosView(InventoryManager manager) {
        this.inventoryManager = manager;
        crearVista();
    }

    private void crearVista() {
        root = new VBox(10);
        root.setPadding(new Insets(15));

        Label titulo = new Label("Gestión de Usuarios");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        root.getChildren().add(titulo);

        TableView<Map<String, Object>> tabla = new TableView<>();
        tabla.setPrefHeight(400);
        
        TableColumn<Map<String, Object>, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(param -> 
            new javafx.beans.property.SimpleObjectProperty<>((Number) param.getValue().get("id")));

        TableColumn<Map<String, Object>, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(param -> 
            new javafx.beans.property.SimpleStringProperty(param.getValue().get("nombre").toString()));

        TableColumn<Map<String, Object>, String> colRol = new TableColumn<>("Rol");
        colRol.setCellValueFactory(param -> 
            new javafx.beans.property.SimpleStringProperty(param.getValue().get("rol").toString()));

        tabla.getColumns().addAll(colId, colNombre, colRol);
        
        Button btnActualizar = new Button("Actualizar");
        btnActualizar.setStyle("-fx-padding: 8px 20px; -fx-background-color: #f39c12; -fx-text-fill: white;");
        btnActualizar.setOnAction(e -> actualizarTabla(tabla));
        
        root.getChildren().addAll(tabla, btnActualizar);
        actualizarTabla(tabla);
    }

    private void actualizarTabla(TableView<Map<String, Object>> tabla) {
        tabla.getItems().clear();
        List<Map<String, Object>> usuarios = inventoryManager.obtenerUsuarios();
        for (Map<String, Object> usr : usuarios) {
            tabla.getItems().add(usr);
        }
    }

    public VBox getRoot() {
        return root;
    }
}
