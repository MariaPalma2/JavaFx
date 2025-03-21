package org.palmamaria.hotelreservas;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class DatosController {


    @FXML
    private ComboBox<String> tipoHabitacionComboBox;

    @FXML
    private ChoiceBox<Integer> habitacionesChoiseBox;

    @FXML
    private TextField dniTextField;

    @FXML
    private TextField nombreTextField;

    @FXML
    private TextField direccionTextField;
    @FXML
    private TextField localidadTextField;
    @FXML
    private TextField provinciaTextField;

    @FXML
    private DatePicker dateLlegada;
    @FXML
    private DatePicker dateSalida;

    @FXML
    private CheckBox checkBoxFumador;

    @FXML
    private CheckBox checkBox1;
    @FXML
    private CheckBox checkBox2;
    @FXML
    private CheckBox checkBox3;

    @FXML
    private Button botonAceptar;



    @FXML
    public void initialize() {

            // Agregar las opciones al ComboBox
            tipoHabitacionComboBox.getItems().addAll(
                    "Doble de uso individual",
                    "Doble",
                    "Junior Suite",
                    "Suite"
            );
            //Elegir un número de habitaciones
            for (int i = 1; i <= 10; i++) {
                habitacionesChoiseBox.getItems().add(i);
            }

        // Listener para el CheckBox "Fumador"
        checkBoxFumador.setOnAction(event -> {
            if (checkBoxFumador.isSelected()) {
                // Mostrar mensaje si se selecciona el CheckBox
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Aviso importante");
                alerta.setHeaderText("Ley de Sanidad");
                alerta.setContentText("En virtud de la Ley de Sanidad se informa a los clientes de que sólo podrán fumar en las habitaciones reservadas para tal fin.");
                alerta.showAndWait();
            }
        });


            }
    @FXML
    private void limpiarDatos() {
        //Limpiar habitaciones
        tipoHabitacionComboBox.setValue(null);  // Quita la selección
        habitacionesChoiseBox.setValue(null);  // Quita la selección
        //Limpiar campos de texto
        dniTextField.clear();
        nombreTextField.clear();
        direccionTextField.clear();
        localidadTextField.clear();
        provinciaTextField.clear();
        //Limpiar fechas
        dateLlegada.setValue(null);
        dateSalida.setValue(null);
        //Limpiar checkBox
        checkBoxFumador.setSelected(false);
        checkBox1.setSelected(false);
        checkBox2.setSelected(false);
        checkBox3.setSelected(false);
     }

    @FXML
    private void completarReserva() {
        // Mostrar mensaje de confirmación
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reserva Completada");
        alert.setHeaderText(null);
        alert.setContentText("¡Reserva realizada con éxito!");
        alert.showAndWait();
    }
    }


