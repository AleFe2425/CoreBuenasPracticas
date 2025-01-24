package com.example.BackendCoreMuebles.Repositorio;

import com.example.BackendCoreMuebles.Modelos.AsignacionEmpleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsignacionEmpleadoRepository extends JpaRepository<AsignacionEmpleado, Integer> {
    AsignacionEmpleado findById(int id);
}
