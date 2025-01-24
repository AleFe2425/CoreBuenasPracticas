package com.example.BackendCoreMuebles.Controlador;

import com.example.BackendCoreMuebles.Modelos.AsignacionEmpleado;
import com.example.BackendCoreMuebles.Modelos.Pedido;
import com.example.BackendCoreMuebles.Servicio.AsignacionEmpleadoService;
import com.example.BackendCoreMuebles.Servicio.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/asignacionEmpleado")
public class AsignacionEmpleadoController {
    @Autowired
    private final AsignacionEmpleadoService asignacionEmpleadoService;
    @Autowired
    private final PedidoService pedidoService;

    public AsignacionEmpleadoController(AsignacionEmpleadoService asignacionEmpleadoService, PedidoService pedidoService) {
        this.asignacionEmpleadoService = asignacionEmpleadoService;
        this.pedidoService = pedidoService;
    }

    @GetMapping("/info")
    public String index() {
        return "Conectado a la tabla Asignación Empleado";
    }

    // Obtener todas las asignaciones
    @GetMapping
    public List<AsignacionEmpleado> getAllAsignaciones() {
        return asignacionEmpleadoService.getAll();
    }

    // Asignar empleados a un pedido
    @PostMapping("/asignar/{idPedido}")
    public ResponseEntity<String> asignarEmpleados(@PathVariable int idPedido) {
        try {
            Pedido pedido = pedidoService.findPedidoById(idPedido);
            asignacionEmpleadoService.asignarEmpleadosAPedido(pedido);
            return ResponseEntity.ok("Empleados asignados correctamente al pedido.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al asignar empleados: " + e.getMessage());
        }
    }

    // Liberar empleados de un pedido
    @PutMapping("/liberar/{idPedido}")
    public ResponseEntity<String> liberarEmpleados(@PathVariable int idPedido) {
        try {
            Pedido pedido = pedidoService.findPedidoById(idPedido);
            asignacionEmpleadoService.liberarEmpleadosDePedido(pedido);
            return ResponseEntity.ok("Empleados liberados del pedido.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al liberar empleados: " + e.getMessage());
        }
    }

    // Reasignar empleados a pedidos demorados
    @PutMapping("/reasignar")
    public ResponseEntity<String> reasignarPedidosDemorados() {
        try {
            asignacionEmpleadoService.reasignacionPedidosDemorados();
            return ResponseEntity.ok("Reasignación de empleados a pedidos demorados completada.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al reasignar pedidos demorados.");
        }
    }
}
