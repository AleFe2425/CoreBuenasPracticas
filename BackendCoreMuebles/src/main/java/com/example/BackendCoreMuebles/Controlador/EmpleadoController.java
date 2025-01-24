package com.example.BackendCoreMuebles.Controlador;

import com.example.BackendCoreMuebles.Modelos.Empleado;
import com.example.BackendCoreMuebles.Servicio.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empleado")
public class EmpleadoController {
    @Autowired
    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @GetMapping("/info")
    public  String index(){
        return "Conectado a la tabla Empleado";
    }

    @GetMapping()
    public List<Empleado> getAllEmpleados(){
        return empleadoService.getAllEmpleados();
    }

    @GetMapping("/{idEmpleado}")
    public ResponseEntity<Empleado> getEmpleadoById(@PathVariable int idEmpleado) throws Exception {
        Empleado empleado = empleadoService.findEmpladoById(idEmpleado);
        return empleado != null ? ResponseEntity.ok(empleado) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Empleado> createEmpleado(@RequestBody Empleado empleado) throws Exception {
        return new ResponseEntity<>(empleadoService.saveEmpleado(empleado), HttpStatus.CREATED);
    }

    @PutMapping("/{idEmpleado}")
    public ResponseEntity<Empleado> updateEmpleado(@PathVariable int idEmpleado, @RequestBody Empleado empleado) throws Exception{
        Empleado actualizar = empleadoService.updateEmpleado(idEmpleado, empleado);
        return actualizar != null ? ResponseEntity.ok(actualizar) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{idEmpleado}")
    public ResponseEntity<Void> deleteEmpleado(@PathVariable int idEmpleado) throws Exception{
        return empleadoService.deleteEmpleado(idEmpleado) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/{idEmpleado}/notificacion")
    public ResponseEntity<String> getNotificacion(@PathVariable int idEmpleado) throws Exception {
        Empleado empleado = empleadoService.findEmpladoById(idEmpleado);
        if (empleado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(empleado.getNotificaciones());
    }
}
