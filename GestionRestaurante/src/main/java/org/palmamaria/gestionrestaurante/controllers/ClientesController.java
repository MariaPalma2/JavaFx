package org.palmamaria.gestionrestaurante.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.palmamaria.gestionrestaurante.ConexionDB;
import org.palmamaria.gestionrestaurante.modelos.Cliente;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ClientesController {
    @FXML
    private TextField txtNombre, txtTelefono, txtDireccion, txtBuscarId;
    @FXML
    private Button btnAgregar, btnModificar, btnBuscar, btnEliminar;
    @FXML
    private TableView<Cliente> tablaClientes;
    @FXML
    private TableColumn<Cliente, Integer> colId;
    @FXML
    private TableColumn<Cliente, String> colNombre, colTelefono, colDireccion;

    private ObservableList<Cliente> listaClientes;
    private int clienteSeleccionadoId = -1;  // ID del cliente seleccionado para modificar/eliminar


    public void initialize() {
        listaClientes = FXCollections.observableArrayList();
        tablaClientes.setItems(listaClientes);

        // Configurar las columnas de la tabla
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        colTelefono.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTelefono()));
        colDireccion.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDireccion()));

        cargarClientes();

        btnAgregar.setOnAction(event -> agregarCliente());
        btnModificar.setOnAction(event -> modificarCliente());
        btnBuscar.setOnAction(event -> buscarClientePorId());
        btnEliminar.setOnAction(event -> eliminarCliente());

        // Seleccionar cliente en la tabla
        tablaClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                clienteSeleccionadoId = newSelection.getId();
                txtNombre.setText(newSelection.getNombre());
                txtTelefono.setText(newSelection.getTelefono());
                txtDireccion.setText(newSelection.getDireccion());
            }
        });
    }

    private void cargarClientes() {
        listaClientes.clear();
        try (Connection con = ConexionDB.conectar();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Clientes")) {

            while (rs.next()) {
                listaClientes.add(new Cliente(
                        rs.getInt("idCliente"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("direccion")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void agregarCliente() {
        String nombre = txtNombre.getText();
        String telefono = txtTelefono.getText();
        String direccion = txtDireccion.getText();

        if (nombre.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        String sql = "INSERT INTO Clientes (nombre, telefono, direccion) VALUES (?, ?, ?)";
        try (Connection con = ConexionDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, telefono);
            stmt.setString(3, direccion);
            stmt.executeUpdate();

            mostrarAlerta("Éxito", "Cliente agregado correctamente.");
            cargarClientes();
            limpiarCampos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modificarCliente() {
        if (clienteSeleccionadoId == -1) {
            mostrarAlerta("Error", "Selecciona un cliente para modificar.");
            return;
        }

        String nombre = txtNombre.getText();
        String telefono = txtTelefono.getText();
        String direccion = txtDireccion.getText();

        if (nombre.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        String sql = "UPDATE Clientes SET nombre = ?, telefono = ?, direccion = ? WHERE idCliente = ?";
        try (Connection con = ConexionDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, telefono);
            stmt.setString(3, direccion);
            stmt.setInt(4, clienteSeleccionadoId);
            stmt.executeUpdate();

            mostrarAlerta("Éxito", "Cliente modificado correctamente.");
            cargarClientes();
            limpiarCampos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void buscarClientePorId() {
        String idBusqueda = txtBuscarId.getText().trim();

        if (idBusqueda.isEmpty()) {
            mostrarAlerta("Error", "Ingrese un ID para buscar.");
            return;
        }

        listaClientes.clear();
        String sql = "SELECT * FROM Clientes WHERE idCliente = ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(idBusqueda));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                listaClientes.add(new Cliente(
                        rs.getInt("idCliente"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("direccion")
                ));
            }

            if (listaClientes.isEmpty()) {
                mostrarAlerta("Información", "No se encontraron clientes con ese ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "Hubo un problema al buscar el cliente.");
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID debe ser un número válido.");
        }
    }


    private void eliminarCliente() {
        if (clienteSeleccionadoId == -1) {
            mostrarAlerta("Error", "Selecciona un cliente para eliminar.");
            return;
        }

        String sql = "DELETE FROM Clientes WHERE idCliente = ?";
        try (Connection con = ConexionDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, clienteSeleccionadoId);
            stmt.executeUpdate();

            mostrarAlerta("Éxito", "Cliente eliminado correctamente.");
            cargarClientes();
            limpiarCampos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Nuevo método para generar informe de clientes
    @FXML
    private void generarInformeClientes() {
        try {
            // Conexión con la base de datos
            Connection conexion = ConexionDB.conectar();

            // Ruta del archivo Jasper compilado
            String rutaReporte = "C:\\Users\\Maria\\JaspersoftWorkspace\\Restaurante\\plantillaClientes.jasper";

            // Parámetros (vacío en este caso)
            Map<String, Object> parametros = new HashMap<>();

            // Llenar el informe con datos
            JasperPrint jasperPrint = JasperFillManager.fillReport(rutaReporte, parametros, conexion);

            // Mostrar el informe
            JasperViewer.viewReport(jasperPrint, false);

            // Cerrar conexión
            conexion.close();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo generar el informe.");
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        clienteSeleccionadoId = -1;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
