package com.example.BackendCoreMuebles.Repositorio;

import com.example.BackendCoreMuebles.Modelos.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente,Integer> {
    Cliente findById(int id);
    Cliente findByEmail(String email);
}
