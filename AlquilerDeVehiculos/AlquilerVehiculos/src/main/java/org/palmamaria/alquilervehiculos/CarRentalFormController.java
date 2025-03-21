package org.palmamaria.alquilervehiculos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CarRentalFormController {

    @FXML
    private TextField nombreField, apellidosField, telefonoField, dniField;
    @FXML
    private DatePicker fechaInicioPicker, fechaFinPicker;
    @FXML
    private ComboBox<String> tipoVehiculoComboBox;
    @FXML
    private ChoiceBox<Integer> edadChoice;
    @FXML
    private CheckBox cableCargaCheckBox, cadenasCheckBox, cancelacionCheckBox, seguroCheckBox, sillaBebeCheckBox;

    @FXML
    private void completarReserva() {
        // Mostrar mensaje de confirmación
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reserva Completada");
        alert.setHeaderText(null);
        alert.setContentText("¡Reserva realizada con éxito!");
        alert.showAndWait();
    }

    @FXML
    private void irAlMenuPrincipal() {
        // Cerrar el formulario y volver al menú principal
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }


}

