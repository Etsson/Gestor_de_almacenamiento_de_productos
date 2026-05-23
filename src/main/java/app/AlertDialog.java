package app;

import javafx.scene.control.Alert;

public class AlertDialog {
    private Alert alert;

    public AlertDialog(String titulo, String mensaje) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
    }

    public AlertDialog(String titulo, String mensaje, Alert.AlertType tipo) {
        alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
    }

    public void show() {
        alert.showAndWait();
    }

    public static AlertDialog error(String titulo, String mensaje) {
        return new AlertDialog(titulo, mensaje, Alert.AlertType.ERROR);
    }

    public static AlertDialog success(String titulo, String mensaje) {
        return new AlertDialog(titulo, mensaje, Alert.AlertType.INFORMATION);
    }

    public static AlertDialog warning(String titulo, String mensaje) {
        return new AlertDialog(titulo, mensaje, Alert.AlertType.WARNING);
    }
}
