package org.palmamaria.hotelreservas;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ReservaController {
    @FXML
    private void reservar() {
        try {
            // Cargar el archivo FXML del formulario
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Datos.fxml"));
            Scene scene = new Scene(loader.load());

            // Crear una nueva ventana para el formulario
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Formulario de reserva");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void salir() {
        // Salir de la aplicación
        System.exit(0);
    }
}