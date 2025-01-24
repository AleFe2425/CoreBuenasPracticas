package com.example.BackendCoreMuebles.Repositorio;

import com.example.BackendCoreMuebles.Modelos.DetallePedido;
import com.example.BackendCoreMuebles.Modelos.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido,Integer> {
    List<DetallePedido> findByPedido(Pedido pedido);
    DetallePedido findById(int idDetallePedido);
}
