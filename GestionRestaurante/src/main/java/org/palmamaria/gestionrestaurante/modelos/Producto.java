package org.palmamaria.gestionrestaurante.modelos;

import javafx.beans.property.*;

public class Producto {
    private final IntegerProperty id;
    private final StringProperty nombre;
    private final StringProperty descripcion;
    private final DoubleProperty precio;
    private final BooleanProperty disponibilidad;

    // 🔹 **Constructor**
    public Producto(int id, String nombre, String descripcion, double precio, boolean disponibilidad) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.precio = new SimpleDoubleProperty(precio);
        this.disponibilidad = new SimpleBooleanProperty(disponibilidad);
    }

    // 🔹 **Getters y Setters con JavaFX Properties**
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public StringProperty descripcionProperty() {
        return descripcion;
    }

    public double getPrecio() {
        return precio.get();
    }

    public void setPrecio(double precio) {
        this.precio.set(precio);
    }

    public DoubleProperty precioProperty() {
        return precio;
    }

    public boolean getDisponibilidad() {
        return disponibilidad.get();
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad.set(disponibilidad);
    }

    public BooleanProperty disponibilidadProperty() {
        return disponibilidad;
    }

    // 🔹 **Método toString() para mostrar el nombre en listas y tablas**
    @Override
    public String toString() {
        return nombre.get();
    }
}
