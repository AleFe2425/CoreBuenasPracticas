package com.example.BackendCoreMuebles.Controlador;

import com.example.BackendCoreMuebles.Modelos.Cliente;
import com.example.BackendCoreMuebles.Modelos.Pedido;
import com.example.BackendCoreMuebles.Servicio.AsignacionEmpleadoService;
import com.example.BackendCoreMuebles.Servicio.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedido")
public class PedidoController {
    @Autowired
    private final PedidoService pedidoService;
    @Autowired
    private final AsignacionEmpleadoService asignacionEmpleadoService;

    public PedidoController(PedidoService pedidoService, AsignacionEmpleadoService asignacionEmpleadoService) {
        this.pedidoService = pedidoService;
        this.asignacionEmpleadoService = asignacionEmpleadoService;
    }

    @GetMapping("/info")
    public String index(){
        return "Conectado a la tabla Pedido";
    }

    @GetMapping()
    public List<Pedido> getAllPedidos() {
        return pedidoService.getAllPedidos();
    }

    @GetMapping("/{idPedido}")
    public ResponseEntity<Pedido> getPedidoById(@PathVariable int idPedido) {
        try {
            return ResponseEntity.ok(pedidoService.findPedidoById(idPedido));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Pedido> createPedido(@RequestBody Pedido pedido) {
        try {
            Pedido nuevoPedido = pedidoService.savePedido(pedido);

            // Intentar asignar empleados al pedido automáticamente
            asignacionEmpleadoService.asignarEmpleadosAPedido(nuevoPedido);

            // Si el pedido queda demorado, intentamos reasignar empleados
            if ("Demorado".equals(nuevoPedido.getEstado())) {
                asignacionEmpleadoService.reasignacionPedidosDemorados();
            }

            return ResponseEntity.ok(nuevoPedido);
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el stack trace para más detalles
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{idPedido}")
    public ResponseEntity<Pedido> updatePedido(@PathVariable int idPedido, @RequestBody Pedido pedidoActualizado) {
        try {
            Pedido pedido = pedidoService.updatePedido(idPedido, pedidoActualizado);

            // Liberar empleados automáticamente si el pedido se completa
            if (pedidoActualizado.getEstado().equals("Completado")) {
                asignacionEmpleadoService.liberarEmpleadosDePedido(pedidoActualizado);

                // Reasignar empleados automáticamente a pedidos demorados
                asignacionEmpleadoService.reasignacionPedidosDemorados();
            }

            return ResponseEntity.ok(pedido);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{idPedido}")
    public ResponseEntity<Boolean> deletePedido(@PathVariable int idPedido) {
        try {
            return ResponseEntity.ok(pedidoService.deletePedido(idPedido));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Pedido>> getPedidosByClienteId(@PathVariable int idCliente) {
        try {
            List<Pedido> pedidosCliente = pedidoService.getPedidosByClienteId(idCliente);
            return ResponseEntity.ok(pedidosCliente);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
