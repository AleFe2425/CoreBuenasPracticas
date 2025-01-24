package com.example.BackendCoreMuebles.Servicio;

import com.example.BackendCoreMuebles.Modelos.Cliente;
import com.example.BackendCoreMuebles.Modelos.Pedido;
import com.example.BackendCoreMuebles.Modelos.DetallePedido;
import com.example.BackendCoreMuebles.Repositorio.ClienteRepository;
import com.example.BackendCoreMuebles.Repositorio.PedidoRepository;
import com.example.BackendCoreMuebles.Repositorio.DetallePedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class PedidoService {
    @Autowired
    private final PedidoRepository pedidoRepository;
    @Autowired
    private final ClienteRepository clienteRepository;
    @Autowired
    private final DetallePedidoService detallePedidoService;
    @Autowired
    private final DetallePedidoRepository detallePedidoRepository;
    @Autowired
    private final AsignacionEmpleadoService asignacionEmpleadoService;

    public PedidoService(PedidoRepository pedidoRepository, DetallePedidoRepository detallePedidoRepository, ClienteRepository clienteRepository, DetallePedidoService detallePedidoService, DetallePedidoRepository detallePedidoRepository1, AsignacionEmpleadoService asignacionEmpleadoService) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.detallePedidoService = detallePedidoService;
        this.detallePedidoRepository = detallePedidoRepository1;
        this.asignacionEmpleadoService = asignacionEmpleadoService;
    }

    //Metodo para Obtener todos los pedidos
    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    //Metodo para Obtener pedido por ID, incluyendo los detalles
    public Pedido findPedidoById(int id) throws Exception {
        Pedido pedido = pedidoRepository.findById(id);
        if (pedido == null) {
            throw new Exception("No se encontró pedido con el ID: " + id);
        }
        pedido.setDetallesPedido(detallePedidoRepository.findByPedido(pedido)); // Asignar detalles al pedido
        return pedido;
    }

    // Metodo para Obtener los pedidos de un Cliente por su ID
    public List<Pedido> getPedidosByClienteId(int idCliente) throws Exception {
        Cliente cliente = clienteRepository.findById(idCliente);

        List<Pedido> pedidosCliente = pedidoRepository.findByCliente(cliente);

        // Cargar los detalles de cada pedido
        for (Pedido pedido : pedidosCliente) {
            pedido.setDetallesPedido(detallePedidoRepository.findByPedido(pedido));
        }

        return pedidosCliente;
    }


    //Metodo para Crear Pedido
    /*public Pedido savePedido(Pedido pedido) throws Exception {
        if (pedido.getCliente() == null || clienteRepository.findById(pedido.getCliente().getIdCliente()) == null) {
            throw new Exception("El cliente asociado al pedido no existe");
        }
        if (pedido.getDetallesPedido() == null || pedido.getDetallesPedido().isEmpty()) {
            throw new Exception("El pedido debe contener al menos un detalle");
        }

        double precioTotal = 0;
        double tiempoTotal = 0;

        for (DetallePedido detalle : pedido.getDetallesPedido()) {
            detalle.setTiempoEstimado(detalle.getCantidad()*(detalle.getMueble().getTiempoBaseProduccion() * detalle.getMaterial().getFactorTiempo()));
            detalle.setPrecioUnitario(detalle.getMueble().getPrecioMueble() * detalle.getMaterial().getPrecioMaterial());
            detalle.setPrecioSubtotal(detalle.getPrecioUnitario()*detalle.getCantidad());

            if (detalle.getCantidad()<=0){
                throw new Exception("La cantidad del mueble es incorrecta");
            }
            if (detalle.getTiempoEstimado()<=0){
                throw new Exception("El tiempo estimado no puede ser menor o igual a 0");
            }
            if (detalle.getPrecioUnitario()<=0){
                throw new Exception("El precio del producto no puede ser menor o igual a 0");
            }

            precioTotal += detalle.getPrecioSubtotal();
            tiempoTotal += detalle.getTiempoEstimado();
        }

        pedido.setPrecioTotal(precioTotal);
        pedido.setTiempoTotalEstimado(tiempoTotal);
        pedido.setEstado("Pendiente");
        Pedido savedPedido = pedidoRepository.save(pedido);

        List<DetallePedido> pedidos = new ArrayList<>();
        for (DetallePedido detalles: pedido.getDetallesPedido()){
            detalles.setPedido(savedPedido);
            pedidos.add(detalles);
        }

        detallePedidoService.saveDetalles(pedidos);
        asignacionEmpleadoService.asignarEmpleadosAPedido(savedPedido);
        return savedPedido;
    }*/

    public Pedido savePedido(Pedido pedido) throws Exception {
        if (pedido.getCliente() == null || clienteRepository.findById(pedido.getCliente().getIdCliente()) == null) {
            throw new Exception("El cliente asociado al pedido no existe");
        }
        if (pedido.getDetallesPedido() == null || pedido.getDetallesPedido().isEmpty()) {
            throw new Exception("El pedido debe contener al menos un detalle");
        }

        double precioTotal = 0;
        double tiempoTotal = 0;

        List<DetallePedido> detalles = new ArrayList<>();

        for (DetallePedido detalle : pedido.getDetallesPedido()) {
            DetallePedido nuevoDetalle = new DetallePedido.Builder()
                    .setMueble(detalle.getMueble())
                    .setMaterial(detalle.getMaterial())
                    .setCantidad(detalle.getCantidad())
                    .setPedido(pedido) // Opcional, dependiendo de la relación
                    .build();

            detalles.add(nuevoDetalle);
            precioTotal += nuevoDetalle.getPrecioSubtotal();
            tiempoTotal += nuevoDetalle.getTiempoEstimado();
        }

        pedido.setDetallesPedido(detalles);
        pedido.setPrecioTotal(precioTotal);
        pedido.setTiempoTotalEstimado(tiempoTotal);
        pedido.setEstado("Pendiente");

        Pedido savedPedido = pedidoRepository.save(pedido);

        detallePedidoService.saveDetalles(detalles);
        asignacionEmpleadoService.asignarEmpleadosAPedido(savedPedido);

        return savedPedido;
    }



    //Metodo para Actualizar Pedido
    public Pedido updatePedido(int idPedido, Pedido pedidoActualizado) throws Exception{
        Pedido pedidoEncontrado = pedidoRepository.findById(idPedido);
        if (pedidoEncontrado != null){
            pedidoEncontrado.setTiempoTotalEstimado(pedidoActualizado.getTiempoTotalEstimado());
            pedidoEncontrado.setEstado(pedidoActualizado.getEstado());

            List<String> allowedEstado = List.of("Pendiente","Proceso", "Demorado", "Completado");
            if (!allowedEstado.contains(pedidoEncontrado.getEstado())){
                throw new Exception("El estado del pedido "+pedidoActualizado.getEstado()+" no esta permitido");
            }
            if ("Completado".equals(pedidoActualizado.getEstado()) && !"Completado".equals(pedidoEncontrado.getEstado())) {
                // Liberar empleados si el pedido se completa
                asignacionEmpleadoService.liberarEmpleadosDePedido(pedidoEncontrado);
            }
            if ("Completado".equals(pedidoEncontrado.getEstado()) && !pedidoActualizado.getEstado().equals("Completado")) {
                throw new Exception("No se puede cambiar el estado de un pedido que ya está completado");
            }
             return pedidoRepository.save(pedidoEncontrado);
        }else {
            throw new Exception("El pedido con el ID:"+idPedido+" no existe.");
        }
    }

    //Metodo para Eliminar pedido y sus detalles
    public boolean deletePedido(int idPedido) throws Exception {
        Pedido pedido = findPedidoById(idPedido);
        if (pedido != null) {
            List<DetallePedido> detalles = detallePedidoRepository.findByPedido(pedido);
            detallePedidoRepository.deleteAll(detalles);
            pedidoRepository.deleteById(idPedido);
            return true;
        } else {
            throw new Exception("El pedido con ID: " + idPedido + " no existe");
        }
    }
}