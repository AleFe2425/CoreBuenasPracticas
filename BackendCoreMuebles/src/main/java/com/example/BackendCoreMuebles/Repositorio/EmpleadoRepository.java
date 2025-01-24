package com.example.BackendCoreMuebles.Repositorio;

import com.example.BackendCoreMuebles.Modelos.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado,Integer> {
    Empleado findById(int id);

}
