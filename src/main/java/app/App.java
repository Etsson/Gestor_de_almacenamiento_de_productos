package app;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Inicializar base de datos
        DatabaseInit.inicializarBD();
        
        // Mostrar menú principal
        MenuPrincipal menu = new MenuPrincipal();
        try {
            menu.start(primaryStage);
        } catch (Exception e) {
            System.err.println("[ERROR] Error iniciando menú: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Cerrar BD al salir
        primaryStage.setOnCloseRequest(e -> {
            DatabaseInit.cerrar();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
