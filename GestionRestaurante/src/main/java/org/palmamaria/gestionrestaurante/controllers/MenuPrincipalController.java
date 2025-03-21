package org.palmamaria.gestionrestaurante.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;
import java.io.IOException;



public class MenuPrincipalController {

    @FXML
    public Button btnClientes;
    @FXML
    private javafx.scene.control.Button btnGestionProductos;


    @FXML
    private javafx.scene.control.Button btnGestionPedidos;


    @FXML
    private void abrirGestionClientes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/palmamaria/gestionrestaurante/gestionClientes.fxml"));
            Parent root = loader.load();
            Stage ventanaClientes = new Stage();
            ventanaClientes.setTitle("Gestión de Clientes");
            ventanaClientes.setScene(new Scene(root, 1000, 700)); // Tamaño de la ventana
            ventanaClientes.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirGestionProductos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/palmamaria/gestionrestaurante/gestionProductos.fxml"));
            Parent root = loader.load();

            Stage ventanaProductos = new Stage();
            ventanaProductos.setTitle("Gestión de Clientes");
            ventanaProductos.setScene(new Scene(root, 700, 650)); // Tamaño de la ventana
            ventanaProductos.show();

            // Cerrar la ventana actual (opcional)
            Stage actual = (Stage) btnGestionProductos.getScene().getWindow();
            actual.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirGestionPedidos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/palmamaria/gestionrestaurante/gestionPedidos.fxml"));
            Parent root = loader.load();

            Stage ventanaProductos = new Stage();
            ventanaProductos.setTitle("Gestión de Pedidos");
            ventanaProductos.setScene(new Scene(root, 950, 650)); // Tamaño de la ventana
            ventanaProductos.show();

            // Cerrar la ventana actual (opcional)
            Stage actual = (Stage) btnGestionPedidos.getScene().getWindow();
            actual.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
