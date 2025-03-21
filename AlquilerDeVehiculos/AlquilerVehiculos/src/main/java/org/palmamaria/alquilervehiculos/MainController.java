package org.palmamaria.alquilervehiculos;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private void abrirFormulario() {
        try {
            // Cargar el archivo FXML del formulario
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CarRentalForm.fxml"));
            Scene scene = new Scene(loader.load());

            // Crear una nueva ventana para el formulario
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Formulario de Alquiler");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void mostrarMensajeError() {
        // Mostrar mensaje de error para bicicletas
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("NO DISPONEMOS DE BICICLETAS EN ESTE MOMENTO");
        alert.showAndWait();
    }

    @FXML
    private void salirAplicacion() {
        // Salir de la aplicación
        System.exit(0);
    }
}
