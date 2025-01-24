package com.example.BackendCoreMuebles.Servicio;

import com.example.BackendCoreMuebles.Modelos.Empleado;
import com.example.BackendCoreMuebles.Repositorio.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoService {
    @Autowired
    private final EmpleadoRepository empleadoRepository;

    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    //Metodo para Obtener todos los empleados
    public List<Empleado> getAllEmpleados() {
        return empleadoRepository.findAll();
    }

    //Metodo para Obtener Empleado por ID
    public Empleado findEmpladoById(int id) throws Exception {
        Empleado empleado = empleadoRepository.findById(id);
        if (empleado == null) {
            throw new Exception("No se encontró empleado con el ID: " + id);
        }
        return empleado;
    }

    //Metodo para Crear Empleado
    public Empleado saveEmpleado(Empleado empleado) throws Exception{
        if(!empleado.getNombre().matches("^[a-zA-Z\\s]*$")){
            throw new Exception("El nombre debe contar solo con letras y comenzar con una mayúscula");
        }

        if (!empleado.isAsistencia() && empleado.getPedidosAsignados() > 0) {
            throw new Exception("No se pueden asignar pedidos a un empleado que no está presente");
        }

        empleado.setAsistencia(false);
        empleado.setPedidosAsignados(0);
        empleado.setHorasTrabajadas(0.0);
        return empleadoRepository.save(empleado);
    }

    //Metodo para Actualizar Empleado
    public Empleado updateEmpleado(int idEmpleado, Empleado empleadoActualizado) throws Exception {
        Empleado empleadoEncontrado = empleadoRepository.findById(idEmpleado);

        if (empleadoEncontrado != null) {

            if (!empleadoActualizado.getNombre().equals(empleadoEncontrado.getNombre())){
                if(!empleadoActualizado.getNombre().matches("^[a-zA-Z\\s]*$")){
                    throw new Exception("El nombre debe contar solo con letras y comenzar con una mayúscula");
                }
                empleadoEncontrado.setNombre(empleadoActualizado.getNombre());
            }

            if (!empleadoActualizado.isAsistencia()){
                if (empleadoActualizado.getPedidosAsignados() != 0){
                    throw new Exception("No se le pueden asignar pedidos a un empleado que no esta presente");
                }
            }

            if (empleadoActualizado.getPedidosAsignados() < 0 || empleadoActualizado.getPedidosAsignados() > 2){
                throw new Exception("No se le pueden asignar pedidos negativos o más de 2 pedidos al empleado");
            }

            empleadoEncontrado.setAsistencia(empleadoActualizado.isAsistencia());
            empleadoEncontrado.setPedidosAsignados(empleadoActualizado.getPedidosAsignados());
            return empleadoRepository.save(empleadoEncontrado);
        } else {
            throw new Exception("El empleado con ID: " + idEmpleado + " no existe");
        }
    }

    //Metodo para Eliminar Empleado
    public boolean deleteEmpleado(int idEmpleado) throws Exception {
        Empleado empleadoEncontrado = empleadoRepository.findById(idEmpleado);
        if (empleadoEncontrado != null) {
            if (empleadoEncontrado.getPedidosAsignados() > 0) {
                throw new Exception("No se puede eliminar un empleado con asignaciones activas");
            }
            empleadoRepository.deleteById(idEmpleado);
            return true;
        } else {
            throw new Exception("El empleado con ID: " + idEmpleado + " no existe");
        }
    }
}
