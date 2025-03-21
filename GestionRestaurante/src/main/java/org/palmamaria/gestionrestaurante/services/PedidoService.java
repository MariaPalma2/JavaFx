package org.palmamaria.gestionrestaurante.services;

import org.palmamaria.gestionrestaurante.modelos.Pedido;
import java.util.List;

public class PedidoService {

    // Simulación de la base de datos de pedidos
    private List<Pedido> pedidos;

    public PedidoService() {
        // Inicializar pedidos
    }

    public void crearPedido(Pedido pedido) {
        // Lógica para guardar el pedido en la base de datos
    }

    public void eliminarPedido(Pedido pedido) {
        // Lógica para eliminar el pedido de la base de datos
    }

    public List<Pedido> obtenerPedidos() {
        // Retornar lista de pedidos
        return pedidos;
    }
}
