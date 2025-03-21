package org.palmamaria.gestionrestaurante.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.palmamaria.gestionrestaurante.ConexionDB;
import org.palmamaria.gestionrestaurante.modelos.Producto;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ProductosController {
    @FXML
    private TextField txtNombre, txtPrecio, txtBuscarId;
    @FXML
    private
    TextArea txtDescripcion;
    @FXML
    private CheckBox cbDisponibilidad;
    @FXML
    private Button btnAgregar, btnModificar, btnBuscar, btnEliminar;
    @FXML
    private TableView<Producto> tablaProductos;
    @FXML
    private TableColumn<Producto, Integer> colId;
    @FXML
    private TableColumn<Producto, String> colNombre, colDescripcion;
    @FXML
    private TableColumn<Producto, Double> colPrecio;
    @FXML
    private TableColumn<Producto, Boolean> colDisponibilidad;


    private ObservableList<Producto> listaProductos;
    private int productoSeleccionadoId = -1;

    public void initialize() {
        listaProductos = FXCollections.observableArrayList();
        tablaProductos.setItems(listaProductos);

        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        colDescripcion.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescripcion()));
        colPrecio.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getPrecio()).asObject());
        colDisponibilidad.setCellValueFactory(cellData -> new javafx.beans.property.SimpleBooleanProperty(cellData.getValue().getDisponibilidad()));

        cargarProductos();

        btnAgregar.setOnAction(event -> agregarProducto());
        btnModificar.setOnAction(event -> modificarProducto());
        btnBuscar.setOnAction(event -> buscarProducto());
        btnEliminar.setOnAction(event -> eliminarProducto());

        tablaProductos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                productoSeleccionadoId = newSelection.getId();
                txtNombre.setText(newSelection.getNombre());
                txtDescripcion.setText(newSelection.getDescripcion());
                txtPrecio.setText(String.valueOf(newSelection.getPrecio()));
                cbDisponibilidad.setSelected(newSelection.getDisponibilidad());
            }
        });
    }

    private void cargarProductos() {
        listaProductos.clear();
        try (Connection con = ConexionDB.conectar();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM productos")) {

            while (rs.next()) {
                listaProductos.add(new Producto(
                        rs.getInt("idProducto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getBoolean("disponibilidad")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void agregarProducto() {
        String nombre = txtNombre.getText();
        String descripcion = txtDescripcion.getText();
        String precioStr = txtPrecio.getText();
        boolean disponibilidad = cbDisponibilidad.isSelected();

        if (nombre.isEmpty() || descripcion.isEmpty() || precioStr.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        try {
            double precio = Double.parseDouble(precioStr);

            String sql = "INSERT INTO productos (nombre, descripcion, precio, disponibilidad) VALUES (?, ?, ?, ?)";
            try (Connection con = ConexionDB.conectar();
                 PreparedStatement stmt = con.prepareStatement(sql)) {

                stmt.setString(1, nombre);
                stmt.setString(2, descripcion);
                stmt.setDouble(3, precio);
                stmt.setBoolean(4, disponibilidad);
                stmt.executeUpdate();

                mostrarAlerta("Éxito", "Producto agregado correctamente.");
                cargarProductos();
                limpiarCampos();
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El precio y el stock deben ser números válidos.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modificarProducto() {
        if (productoSeleccionadoId == -1) {
            mostrarAlerta("Error", "Selecciona un producto para modificar.");
            return;
        }

        String nombre = txtNombre.getText();
        String descripcion = txtDescripcion.getText();
        String precioStr = txtPrecio.getText();
        boolean disponibilidad = cbDisponibilidad.isSelected();

        if (nombre.isEmpty() || descripcion.isEmpty() || precioStr.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        try {
            double precio = Double.parseDouble(precioStr);

            String sql = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, disponibilidad = ? WHERE idProducto = ?";
            try (Connection con = ConexionDB.conectar();
                 PreparedStatement stmt = con.prepareStatement(sql)) {

                stmt.setString(1, nombre);
                stmt.setString(2, descripcion);
                stmt.setDouble(3, precio);
                stmt.setBoolean(4, disponibilidad);
                stmt.setInt(5, productoSeleccionadoId);
                stmt.executeUpdate();

                mostrarAlerta("Éxito", "Producto modificado correctamente.");
                cargarProductos();
                limpiarCampos();
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El precio y el stock deben ser números válidos.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void buscarProducto() {
        String idBusquedaStr = txtBuscarId.getText();

        if (idBusquedaStr.isEmpty()) {
            mostrarAlerta("Error", "Ingrese un ID para buscar.");
            return;
        }

        try {
            int idBusqueda = Integer.parseInt(idBusquedaStr);
            listaProductos.clear();

            String sql = "SELECT * FROM productos WHERE idProducto = ?";
            try (Connection con = ConexionDB.conectar();
                 PreparedStatement stmt = con.prepareStatement(sql)) {

                stmt.setInt(1, idBusqueda);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    listaProductos.add(new Producto(
                            rs.getInt("idCliente"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio"),
                            rs.getBoolean("disponibilidad")
                    ));
                } else {
                    mostrarAlerta("Información", "No se encontró el producto con ese ID.");
                }
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID debe ser un número válido.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void eliminarProducto() {
        if (productoSeleccionadoId == -1) {
            mostrarAlerta("Error", "Selecciona un producto para eliminar.");
            return;
        }

        String sql = "DELETE FROM productos WHERE idProducto = ?";
        try (Connection con = ConexionDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, productoSeleccionadoId);
            stmt.executeUpdate();

            mostrarAlerta("Éxito", "Producto eliminado correctamente.");
            cargarProductos();
            limpiarCampos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Nuevo método para generar informe de productos con disponibilidad por debajo de 5
    @FXML
    private void generarInformeProductos() {
        try {
            // Conexión con la base de datos
            Connection conexion = ConexionDB.conectar();

            // Ruta del archivo Jasper compilado
            String rutaReporte = "C:\\Users\\Maria\\JaspersoftWorkspace\\Restaurante\\Leaf_Green.jasper";

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
        txtDescripcion.clear();
        txtPrecio.clear();
        cbDisponibilidad.setSelected(false);
        txtBuscarId.clear();
        productoSeleccionadoId = -1;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    @FXML
    private void abrirMenuPrincipal(){}
}
