package com.example.BackendCoreMuebles.Controlador;

import com.example.BackendCoreMuebles.Modelos.DetallePedido;
import com.example.BackendCoreMuebles.Servicio.DetallePedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detallePedido")
public class DetallePedidoController {
    @Autowired
    private final DetallePedidoService detallePedidoService;

    public DetallePedidoController(DetallePedidoService detallePedidoService) {
        this.detallePedidoService = detallePedidoService;
    }

    @GetMapping("/info")
    public String index(){
        return "Conectado a la tabla Detalle Pedido";
    }

    @GetMapping
    public List<DetallePedido> getAllDetalles() {
        return detallePedidoService.getAllDetalles();
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<DetallePedido>> getDetallesByPedidoId(@PathVariable int pedidoId) {
        try {
            return ResponseEntity.ok(detallePedidoService.findDetallesByPedidoId(pedidoId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping
    public ResponseEntity<List<DetallePedido>> createDetalles(@RequestBody List<DetallePedido> detalles) {
        return ResponseEntity.ok(detallePedidoService.saveDetalles(detalles));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteDetalle(@PathVariable int id) {
        try {
            return ResponseEntity.ok(detallePedidoService.deleteDetalle(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(false);
        }
    }
}