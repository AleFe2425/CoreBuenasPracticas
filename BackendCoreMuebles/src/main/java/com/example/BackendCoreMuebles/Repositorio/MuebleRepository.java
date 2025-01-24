package com.example.BackendCoreMuebles.Repositorio;

import com.example.BackendCoreMuebles.Modelos.Mueble;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MuebleRepository extends JpaRepository<Mueble, Integer> {
    Mueble findById(int id);
}
