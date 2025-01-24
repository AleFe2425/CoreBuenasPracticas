package com.example.BackendCoreMuebles.Repositorio;

import com.example.BackendCoreMuebles.Modelos.Cliente;
import com.example.BackendCoreMuebles.Modelos.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    Pedido findById(int id);
    List<Pedido> findByCliente(Cliente Cliente);
    List<Pedido> findByEstado(String estado);
}
