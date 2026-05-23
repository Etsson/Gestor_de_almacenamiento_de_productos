package app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuPrincipal extends Application {
    private InventoryManager inventoryManager;

    public MenuPrincipal() {
        this.inventoryManager = new InventoryManager(DatabaseInit.getConexion());
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mercado - Sistema de Gestión de Inventario");
        primaryStage.setWidth(600);
        primaryStage.setHeight(700);

        VBox root = crearMenuPrincipal();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox crearMenuPrincipal() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(30));
        container.setStyle("-fx-background-color: #f5f5f5;");
        container.setAlignment(Pos.TOP_CENTER);

        // Título
        Label titulo = new Label("SISTEMA DE GESTIÓN DE INVENTARIO");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        container.getChildren().add(titulo);

        // Subtítulo
        Label subtitulo = new Label("Bienvenido al Mercado");
        subtitulo.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d;");
        container.getChildren().add(subtitulo);

        // Grid de botones
        GridPane gridBotones = new GridPane();
        gridBotones.setHgap(15);
        gridBotones.setVgap(15);
        gridBotones.setPadding(new Insets(20));
        gridBotones.setAlignment(Pos.CENTER);

        // Fila 1: Productos y Categorías
        Button btnProductos = crearBoton("📦 Productos", 180, 80, "#3498db");
        btnProductos.setOnAction(e -> abrirProductos());
        gridBotones.add(btnProductos, 0, 0);

        Button btnCategorias = crearBoton("🏷️ Categorías", 180, 80, "#9b59b6");
        btnCategorias.setOnAction(e -> abrirCategorias());
        gridBotones.add(btnCategorias, 1, 0);

        // Fila 2: Compras y Ventas
        Button btnCompras = crearBoton("📥 Compras", 180, 80, "#e74c3c");
        btnCompras.setOnAction(e -> abrirCompras());
        gridBotones.add(btnCompras, 0, 1);

        Button btnVentas = crearBoton("📤 Ventas", 180, 80, "#27ae60");
        btnVentas.setOnAction(e -> abrirVentas());
        gridBotones.add(btnVentas, 1, 1);

        // Fila 3: Kardex y Usuarios
        Button btnKardex = crearBoton("📊 Kardex", 180, 80, "#f39c12");
        btnKardex.setOnAction(e -> abrirKardex());
        gridBotones.add(btnKardex, 0, 2);

        Button btnUsuarios = crearBoton("👥 Usuarios", 180, 80, "#1abc9c");
        btnUsuarios.setOnAction(e -> abrirUsuarios());
        gridBotones.add(btnUsuarios, 1, 2);

        container.getChildren().add(gridBotones);

        // Botón de salida
        Button btnSalir = new Button("Salir");
        btnSalir.setStyle("-fx-font-size: 14px; -fx-padding: 10px 30px; -fx-background-color: #34495e; -fx-text-fill: white;");
        btnSalir.setOnAction(e -> System.exit(0));
        container.getChildren().add(btnSalir);

        return container;
    }

    private Button crearBoton(String texto, double ancho, double alto, String color) {
        Button btn = new Button(texto);
        btn.setStyle(
            "-fx-font-size: 16px; " +
            "-fx-padding: 15px; " +
            "-fx-background-color: " + color + "; " +
            "-fx-text-fill: white; " +
            "-fx-border-radius: 5; " +
            "-fx-cursor: hand;"
        );
        btn.setPrefWidth(ancho);
        btn.setPrefHeight(alto);
        return btn;
    }

    private void abrirProductos() {
        Stage stage = new Stage();
        stage.setTitle("Gestionar Productos");
        stage.setScene(new Scene(new ProductosView(inventoryManager).getRoot(), 800, 600));
        stage.show();
    }

    private void abrirCategorias() {
        Stage stage = new Stage();
        stage.setTitle("Gestionar Categorías");
        stage.setScene(new Scene(new CategoriasView(inventoryManager).getRoot(), 600, 500));
        stage.show();
    }

    private void abrirCompras() {
        Stage stage = new Stage();
        stage.setTitle("Registrar Compras");
        stage.setScene(new Scene(new ComprasView(inventoryManager).getRoot(), 900, 700));
        stage.show();
    }

    private void abrirVentas() {
        Stage stage = new Stage();
        stage.setTitle("Registrar Ventas");
        stage.setScene(new Scene(new VentasView(inventoryManager).getRoot(), 900, 700));
        stage.show();
    }

    private void abrirKardex() {
        Stage stage = new Stage();
        stage.setTitle("Movimientos de Inventario (Kardex)");
        stage.setScene(new Scene(new KardexView(inventoryManager).getRoot(), 1000, 600));
        stage.show();
    }

    private void abrirUsuarios() {
        Stage stage = new Stage();
        stage.setTitle("Gestionar Usuarios");
        stage.setScene(new Scene(new UsuariosView(inventoryManager).getRoot(), 700, 500));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
