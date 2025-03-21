package org.palmamaria.gestionrestaurante.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.palmamaria.gestionrestaurante.ConexionDB;
import org.palmamaria.gestionrestaurante.modelos.Cliente;
import org.palmamaria.gestionrestaurante.modelos.DetallePedido;
import org.palmamaria.gestionrestaurante.modelos.Pedido;
import org.palmamaria.gestionrestaurante.modelos.Producto;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class PedidoController {

    @FXML
    private TableView<Pedido> tablaPedidos;

    @FXML
    private TableView<DetallePedido> tablaDetallePedido;

    @FXML
    private TableColumn<Pedido, Integer> colId;

    @FXML
    private TableColumn<Pedido, Cliente> colCliente;

    @FXML
    private TableColumn<Pedido, String> colFecha;

    @FXML
    private TableColumn<Pedido, String> colHora;

    @FXML
    private TableColumn<Pedido, Double> colTotal;

    @FXML
    private TableColumn<Pedido, String> colEstado;

    @FXML
    private TableColumn<Pedido, Integer> colDetalleId;

    @FXML
    private TableColumn<Pedido, Integer> colPedido;

    @FXML
    private TableColumn<Pedido, Integer> colProducto;

    @FXML
    private TableColumn<Pedido, Integer> colCantidad;

    @FXML
    private TableColumn<Pedido, Double> colPrecio;

    @FXML
    private TableColumn<Pedido, Double> colSubtotal;

    private ObservableList<Pedido> pedidos;

    private ObservableList<DetallePedido> detallePedidos;

    @FXML
    private ChoiceBox<String> selectEstado;

    @FXML
    private ChoiceBox<Cliente> selectCliente;

    @FXML
    private ChoiceBox<Producto> selectProducto;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private TextField txtHora;

    @FXML
    private TextField txtDetallePedido;

    @FXML
    private TextField txtCantidad;

    @FXML
    private Label labelTotal;

    @FXML
    private Label labelSubtotal;

    private int idPedido = -1;
    private int idDetallePedido = -1;

    // Método de inicialización
    @FXML
    public void initialize() {
        // Inicializamos la lista de pedidos
        pedidos = FXCollections.observableArrayList();
        detallePedidos = FXCollections.observableArrayList();

        selectEstado.getItems().addAll("En preparación", "Listo para llevar", "Entregado");

        cargarClientes();
        cargarProductos();

        // Opcional: seleccionar un estado por defecto
        selectEstado.setValue("En preparación");

        // Establecemos las propiedades de las columnas
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        colDetalleId.setCellValueFactory(new PropertyValueFactory<>("idDetallePedido"));
        colPedido.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
        colProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        tablaPedidos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                idPedido = newSelection.getId();
                selectCliente.getSelectionModel().select(
                        selectCliente.getItems().stream()
                                .filter(cliente -> cliente.getNombre().equals(newSelection.getCliente()))
                                .findFirst()
                                .get()
                );
                dpFecha.setValue(LocalDate.parse(newSelection.getFecha()));
                txtHora.setText(newSelection.getHora());
                selectEstado.getSelectionModel().select(newSelection.getEstado());
                labelTotal.setText(newSelection.getTotal() + " €");
            }
        });

        tablaDetallePedido.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                idDetallePedido = newSelection.getIdDetallePedido();
                selectProducto.getSelectionModel().select(
                        selectProducto.getItems().stream()
                                .filter(producto -> producto.getNombre().equals(newSelection.getNombreProducto()))
                                .findFirst()
                                .get()
                );
                txtDetallePedido.setText(String.valueOf(idDetallePedido));
                txtCantidad.setText(String.valueOf(newSelection.getCantidad()));
            }
        });

        // Llamamos al método para cargar los pedidos desde la base de datos
        cargarPedidos();
        cargarDetallesPedidos();

        txtCantidad.setOnKeyReleased(_ -> {
            if (txtCantidad.getText().isEmpty() || selectProducto.getValue() == null) {
                return;
            }
            labelSubtotal.setText(selectProducto.getValue().getPrecio() * Integer.parseInt(txtCantidad.getText()) + " €");
        });
    }

    private void cargarClientes() {
        String sql = "SELECT idCliente, nombre FROM clientes";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("idCliente");
                String nombre = rs.getString("nombre");
                selectCliente.getItems().add(new Cliente(id, nombre, null, null));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarProductos() {
        String sql = "SELECT idProducto, nombre, precio FROM productos";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("idProducto");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                selectProducto.getItems().add(new Producto(id, nombre, null, precio, false));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        selectProducto.setOnAction(_ -> {
            if (txtCantidad.getText().isEmpty()) {
                return;
            }

            labelSubtotal.setText(selectProducto.getValue().getPrecio() * Integer.parseInt(txtCantidad.getText()) + " €");
        });
    }


    // Método para cargar los pedidos desde la base de datos
    private void cargarPedidos() {
        // Aquí se conecta a la base de datos
        String url = "jdbc:mysql://localhost:3306/restaurantedb";
        String usuario = "root";
        String contrasena = "";

        String query = "SELECT p.idPedido AS id, c.nombre AS cliente, p.fechaPedido, p.horaPedido, p.total, p.estadoPedido " +
                "FROM pedidos p JOIN clientes c ON p.idCliente = c.idCliente";



        try (Connection connection = DriverManager.getConnection(url, usuario, contrasena);
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            // Limpiamos la lista antes de agregar los nuevos pedidos
            pedidos.clear();

            while (rs.next()) {
                // Creamos el objeto Pedido y lo agregamos a la lista
                Pedido pedido = new Pedido(
                        rs.getInt("id"),
                        rs.getString("cliente"),
                        rs.getString("fechaPedido"),
                        rs.getString("horaPedido"),
                        rs.getDouble("total"),
                        rs.getString("estadoPedido")
                );
                pedidos.add(pedido);
            }

            // Actualizamos la TableView con los nuevos datos
            tablaPedidos.getItems().setAll(pedidos);

        } catch (SQLException e) {
            e.printStackTrace();
            // Maneja los errores de la base de datos
        }
    }

    private void cargarDetallesPedidos() {
        // Aquí se conecta a la base de datos
        String url = "jdbc:mysql://localhost:3306/restaurantedb";
        String usuario = "root";
        String contrasena = "";

        String query = "SELECT d.idDetallePedido, d.idPedido, p.nombre AS nombreProducto, d.cantidad, d.precio, d.subtotal " +
                       "FROM detallepedidos d JOIN productos p ON d.idProducto = p.idProducto";

        try (Connection connection = DriverManager.getConnection(url, usuario, contrasena);
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            detallePedidos.clear();

            while (rs.next()) {
                DetallePedido detallePedido = new DetallePedido(
                        rs.getInt("idDetallePedido"),
                        rs.getInt("idPedido"),
                        rs.getString("nombreProducto"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio"),
                        rs.getInt("cantidad") * rs.getDouble("precio")
                );
                detallePedidos.add(detallePedido);
            }

            // Actualizamos la TableView con los nuevos datos
            tablaDetallePedido.getItems().setAll(detallePedidos);

        } catch (SQLException e) {
            e.printStackTrace();
            // Maneja los errores de la base de datos
        }
    }

    public void anadirPedido() {
        int idCliente = selectCliente.getValue().getId();
        String fecha = dpFecha.getValue().toString();
        String hora = txtHora.getText();
        String estado = selectEstado.getValue();

        try {
            String sql = "INSERT INTO pedidos (idCliente, fechaPedido, horaPedido, total, estadoPedido) VALUES (?, ?, ?, ?, ?)";
            try (Connection con = ConexionDB.conectar();
                 PreparedStatement stmt = con.prepareStatement(sql)) {

                stmt.setInt(1, idCliente);
                stmt.setString(2, fecha);
                stmt.setString(3, hora);
                stmt.setDouble(4, 0);
                stmt.setString(5, estado);
                stmt.executeUpdate();

                mostrarAlerta("Éxito", "Producto agregado correctamente.");
                cargarPedidos();
                limpiarCamposPedido();
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El precio y el stock deben ser números válidos.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modificarPedido() {
        int idCliente = selectCliente.getValue().getId();
        String fecha = dpFecha.getValue().toString();
        String hora = txtHora.getText();
        String estado = selectEstado.getValue();

        String sql = "UPDATE pedidos SET idCliente = ?, fechaPedido = ?, horaPedido = ?, estadoPedido = ? WHERE idPedido = ?";
        try (Connection con = ConexionDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idCliente);
            stmt.setString(2, fecha);
            stmt.setString(3, hora);
            stmt.setString(4, estado);
            stmt.setInt(5, idPedido);
            stmt.executeUpdate();

            mostrarAlerta("Éxito", "Pedido modificado correctamente.");
            cargarPedidos();
            limpiarCamposPedido();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarPedido() {
        String sql = "DELETE FROM pedidos WHERE idPedido = ?";
        try (Connection con = ConexionDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idPedido);
            stmt.executeUpdate();

            mostrarAlerta("Éxito", "Pedido eliminado correctamente.");
            cargarPedidos();
            limpiarCamposPedido();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cargarDetallesPedidos();
    }

    public void eliminarDetallePedido() {
        String sql = "DELETE FROM detallepedidos WHERE idDetallePedido = ?";
        try (Connection con = ConexionDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, idDetallePedido);
            stmt.executeUpdate();

            mostrarAlerta("Éxito", "Pedido eliminado correctamente.");
            cargarPedidos();
            limpiarCamposPedido();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cargarDetallesPedidos();
    }
    public void volverAlMenuPrincipal() {}

    public void anadirDetallePedido() {
        double precio = selectProducto.getValue().getPrecio();
        int idPedido = Integer.parseInt(txtDetallePedido.getText());
        int idProducto = selectProducto.getValue().getId();
        int cantidad = Integer.parseInt(txtCantidad.getText());

        try {
            String sql = "INSERT INTO detallepedidos (idPedido, idProducto, cantidad, precio, subtotal) VALUES (?, ?, ?, ?, ?)";
            Connection con = ConexionDB.conectar();
            try (PreparedStatement stmt = con.prepareStatement(sql)) {

                stmt.setInt(1, idPedido);
                stmt.setInt(2, idProducto);
                stmt.setDouble(3, cantidad);
                stmt.setDouble(4, precio);
                stmt.setDouble(5, precio * cantidad);
                stmt.executeUpdate();

                mostrarAlerta("Éxito", "Detalle pedido agregado correctamente.");
                cargarPedidos();
                limpiarCamposPedido();
            }

            sql = "SELECT SUM(subtotal) AS total FROM detallepedidos WHERE idPedido = ?";
            double total = 0;
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setDouble(1, idPedido);

                ResultSet rs = stmt.executeQuery();
                rs.next();
                total = rs.getDouble("total");
            } catch (Exception e) {
                e.printStackTrace();
            }

            sql = "UPDATE pedidos SET total = ? WHERE idPedido = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setDouble(1, total);
                stmt.setInt(2, idPedido);
                stmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cargarPedidos();
        cargarDetallesPedidos();
    }

    // ✅ Nuevo método para generar informe de Pedidos en preparación
    @FXML
    private void generarInformePedidos() {
        try {
            // Conexión con la base de datos
            Connection conexion = ConexionDB.conectar();

            // Ruta del archivo Jasper compilado
            String rutaReporte = "C:\\Users\\Maria\\JaspersoftWorkspace\\Restaurante\\Leaf_Green_1.jasper";

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

    // ✅ Nuevo método para generar informe de Pedidos en preparación
    @FXML
    private void generarTicket() {
        try {
            // Conexión con la base de datos
            Connection conexion = ConexionDB.conectar();

            // Ruta del archivo Jasper compilado
            String rutaReporte = "C:\\Users\\Maria\\JaspersoftWorkspace\\Restaurante\\Leaf_Green_1.jasper";

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




    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void limpiarCamposPedido() {
        selectCliente.getSelectionModel().select(null);
        dpFecha.setValue(null);
        txtHora.setText(null);
        selectEstado.getSelectionModel().select(0);
    }
}
