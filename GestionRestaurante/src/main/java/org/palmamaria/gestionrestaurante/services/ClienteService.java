package org.palmamaria.gestionrestaurante.services;

import org.palmamaria.gestionrestaurante.modelos.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClienteService {

    private List<Cliente> clientes;
    private int nextId;

    public ClienteService() {
        clientes = new ArrayList<>();
        nextId = 1;  // Empezamos con el ID 1
    }

    // Obtener todos los clientes
    public List<Cliente> obtenerClientes() {
        return clientes;  // Devolver la lista de clientes
    }

    // Obtener el siguiente ID
    public int getNextId() {
        return nextId++;
    }

    // Agregar un nuevo cliente
    public void agregarCliente(Cliente cliente) {
        clientes.add(cliente);
    }
}
