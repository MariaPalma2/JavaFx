package org.palmamaria.gestionrestaurante;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("menu_principal.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 550, 550);
        primaryStage.setTitle("Sistema de Gestión de Pedidos");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}