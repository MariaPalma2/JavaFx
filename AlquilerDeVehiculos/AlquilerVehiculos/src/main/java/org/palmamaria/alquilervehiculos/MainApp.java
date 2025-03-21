package org.palmamaria.alquilervehiculos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargar el archivo FXML del menú principal
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        Scene scene = new Scene(loader.load());

        // Configurar el escenario principal
        primaryStage.setScene(scene);
        primaryStage.setTitle("Menú Principal");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
