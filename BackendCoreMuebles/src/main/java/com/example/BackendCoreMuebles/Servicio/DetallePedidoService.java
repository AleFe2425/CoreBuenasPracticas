package com.example.BackendCoreMuebles.Servicio;

import com.example.BackendCoreMuebles.Modelos.DetallePedido;
import com.example.BackendCoreMuebles.Modelos.Pedido;
import com.example.BackendCoreMuebles.Repositorio.DetallePedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetallePedidoService {
    @Autowired
    private final DetallePedidoRepository detallePedidoRepository;

    public DetallePedidoService(DetallePedidoRepository detallePedidoRepository) {
        this.detallePedidoRepository = detallePedidoRepository;
    }

    //Metodo para Obtener todos los detalles de pedido
    public List<DetallePedido> getAllDetalles() {
        return detallePedidoRepository.findAll();
    }

    //Metodo para Obtener detalles por ID de pedido
    public List<DetallePedido> findDetallesByPedidoId(int pedidoId) throws Exception {
        Pedido pedido = detallePedidoRepository.findById(pedidoId).getPedido();

        List<DetallePedido> detalles = detallePedidoRepository.findByPedido(pedido);
        if (detalles.isEmpty()) {
            throw new Exception("No se encontraron detalles para el pedido con ID: " + pedidoId);
        }
        return detalles;
    }

    //Metodo para Crear o actualizar detalles de un pedido
    public List<DetallePedido> saveDetalles(List<DetallePedido> detalles) {
        return detallePedidoRepository.saveAll(detalles);
    }

    //Metodo para Eliminar un detalle por ID
    public boolean deleteDetalle(int idDetalle) throws Exception {
        if (detallePedidoRepository.existsById(idDetalle)) {
            detallePedidoRepository.deleteById(idDetalle);
            return true;
        } else {
            throw new Exception("El detalle de pedido con ID: " + idDetalle + " no existe");
        }
    }
}