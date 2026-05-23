package app;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.List;
import java.util.Map;

public class KardexView {
    private InventoryManager inventoryManager;
    private VBox root;

    public KardexView(InventoryManager manager) {
        this.inventoryManager = manager;
        crearVista();
    }

    private void crearVista() {
        root = new VBox(10);
        root.setPadding(new Insets(15));

        Label titulo = new Label("Kardex - Movimientos de Inventario");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        root.getChildren().add(titulo);

        TableView<Map<String, Object>> tabla = new TableView<>();
        tabla.setPrefHeight(500);
        
        TableColumn<Map<String, Object>, String> colProducto = new TableColumn<>("Producto");
        colProducto.setCellValueFactory(param -> {
            Object producto = param.getValue().get("producto");
            return new javafx.beans.property.SimpleStringProperty(producto != null ? producto.toString() : "(sin producto)");
        });

        TableColumn<Map<String, Object>, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(param -> 
            new javafx.beans.property.SimpleStringProperty(param.getValue().get("tipo").toString()));

        TableColumn<Map<String, Object>, Number> colCantidad = new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(param -> 
            new javafx.beans.property.SimpleObjectProperty<>((Number) param.getValue().get("cantidad")));

        TableColumn<Map<String, Object>, String> colReferencia = new TableColumn<>("Referencia");
        colReferencia.setCellValueFactory(param -> {
            Object ref = param.getValue().get("referencia");
            return new javafx.beans.property.SimpleStringProperty(ref != null ? ref.toString() : "");
        });

        TableColumn<Map<String, Object>, Number> colStockAnterior = new TableColumn<>("Stock Ant.");
        colStockAnterior.setCellValueFactory(param -> 
            new javafx.beans.property.SimpleObjectProperty<>((Number) param.getValue().get("stockAnterior")));

        TableColumn<Map<String, Object>, Number> colStockNuevo = new TableColumn<>("Stock Nuevo");
        colStockNuevo.setCellValueFactory(param -> 
            new javafx.beans.property.SimpleObjectProperty<>((Number) param.getValue().get("stockNuevo")));

        TableColumn<Map<String, Object>, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(param -> {
            Object fecha = param.getValue().get("fecha");
            return new javafx.beans.property.SimpleStringProperty(fecha != null ? fecha.toString() : "");
        });

        tabla.getColumns().addAll(colProducto, colTipo, colCantidad, colReferencia, colStockAnterior, colStockNuevo, colFecha);
        
        Button btnActualizar = new Button("Actualizar");
        btnActualizar.setStyle("-fx-padding: 8px 20px; -fx-background-color: #f39c12; -fx-text-fill: white;");
        btnActualizar.setOnAction(e -> actualizarTabla(tabla));
        
        root.getChildren().addAll(tabla, btnActualizar);
        actualizarTabla(tabla);
    }

    private void actualizarTabla(TableView<Map<String, Object>> tabla) {
        tabla.getItems().clear();
        List<Map<String, Object>> kardex = inventoryManager.obtenerKardex();
        for (Map<String, Object> mov : kardex) {
            tabla.getItems().add(mov);
        }
    }

    public VBox getRoot() {
        return root;
    }
}
