package com.example.BackendCoreMuebles.Servicio;

import com.example.BackendCoreMuebles.Command.AsignarEmpleadosCommand;
import com.example.BackendCoreMuebles.Command.Command;
import com.example.BackendCoreMuebles.Command.LiberarEmpleadosCommand;
import com.example.BackendCoreMuebles.Command.ReasignarEmpleadosCommand;
import com.example.BackendCoreMuebles.Modelos.AsignacionEmpleado;
import com.example.BackendCoreMuebles.Modelos.DetallePedido;
import com.example.BackendCoreMuebles.Modelos.Empleado;
import com.example.BackendCoreMuebles.Modelos.Pedido;
import com.example.BackendCoreMuebles.Repositorio.AsignacionEmpleadoRepository;
import com.example.BackendCoreMuebles.Repositorio.EmpleadoRepository;
import com.example.BackendCoreMuebles.Repositorio.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsignacionEmpleadoService {
    @Autowired
    private final AsignacionEmpleadoRepository asignacionEmpleadoRepository;
    @Autowired
    private final EmpleadoRepository empleadoRepository;
    @Autowired
    private final PedidoRepository pedidoRepository;
    @Autowired
    private final EmpleadoService empleadoService;

    public AsignacionEmpleadoService(AsignacionEmpleadoRepository asignacionEmpleadoRepository, EmpleadoRepository empleadoRepository, PedidoRepository pedidoRepository, EmpleadoService empleadoService) {
        this.asignacionEmpleadoRepository = asignacionEmpleadoRepository;
        this.empleadoRepository = empleadoRepository;
        this.pedidoRepository = pedidoRepository;
        this.empleadoService = empleadoService;
    }

    public List<AsignacionEmpleado> getAll() {
        return asignacionEmpleadoRepository.findAll();
    }


    /**
     * Método principal para asignar empleados a un pedido (usa Command).
     */
    public void asignarEmpleadosAPedido(Pedido pedido) throws Exception {
        Command command = new AsignarEmpleadosCommand(this, pedido);
        ejecutarComando(command);
    }

    /**
     * Método principal para liberar empleados de un pedido (usa Command).
     */
    public void liberarEmpleadosDePedido(Pedido pedido) throws Exception {
        Command command = new LiberarEmpleadosCommand(this, pedido);
        ejecutarComando(command);
    }

    /**
     * Método principal para reasignar empleados a pedidos demorados (usa Command).
     */
    public void reasignacionPedidosDemorados() throws Exception {
        Command command = new ReasignarEmpleadosCommand(this);
        ejecutarComando(command);
    }

    /**
     * Método genérico para ejecutar cualquier comando.
     */
    public void ejecutarComando(Command command) throws Exception {
        command.execute();
    }



    /**
     * Asigna empleados disponibles a un pedido. Si no hay suficientes empleados,
     * el pedido se coloca como "Demorado" y se ajusta su tiempo estimado.
     */
    public void procesarAsignacionEmpleados(Pedido pedido) throws Exception {
        // Verificar si el pedido ya tiene asignaciones activas
        List<AsignacionEmpleado> asignacionesExistentes = asignacionEmpleadoRepository.findAll().stream()
                .filter(asignacion -> asignacion.getPedido().getIdPedido() == pedido.getIdPedido() && asignacion.getFechaFin() == null)
                .collect(Collectors.toList());

        if (!asignacionesExistentes.isEmpty()) {
            System.out.println("El pedido #" + pedido.getIdPedido() + " ya tiene empleados asignados. No se reasignarán.");
            return; // Salimos sin hacer nada
        }

        List<Empleado> empleadosDisponibles = obtenerEmpleadosDisponibles(pedido);

        if (empleadosDisponibles.isEmpty()) {
            System.out.println("No hay empleados disponibles para el pedido #"+pedido.getIdPedido());

            if (!pedido.isTiempoAdicionalAsignado()) {
                double tiempoAdicional = calcularTiempoAdicionalParaPedidoDemorado();
                pedido.setTiempoTotalEstimado(pedido.getTiempoTotalEstimado() + tiempoAdicional);
                pedido.setTiempoAdicionalAsignado(true); // Marcar como procesado
            }

            pedido.setEstado("Demorado");
            pedidoRepository.save(pedido);
            return;
        }

        int empleadosNecesarios = calcularEmpleadosPorPedido(pedido);
        System.out.println("Empleados necesarios para el pedido #"+pedido.getIdPedido()+":"+empleadosNecesarios);
        System.out.println("Empleados disponibles: "+empleadosDisponibles.size());

        // Asignar empleados de manera equitativa
        for (Empleado empleado : empleadosDisponibles) {
            if (empleadosNecesarios == 0) break;

            AsignacionEmpleado asignacion = new AsignacionEmpleado();
            asignacion.setEmpleado(empleado);
            asignacion.setPedido(pedido);
            asignacion.setFechaAsignacion(new Date());

            asignacionEmpleadoRepository.save(asignacion);

            empleado.setPedidosAsignados(empleado.getPedidosAsignados() + 1);
            empleadoRepository.save(empleado);

            empleadosNecesarios--;
        }

        // Ajustar el estado del pedido
        if (empleadosNecesarios > 0) {
            System.out.println("No se pudieron asignar suficientes empleados al pedido #" + pedido.getIdPedido());
            pedido.setEstado("Demorado");
        } else {
            System.out.println("Pedido #" + pedido.getIdPedido() + " está en proceso.");
            pedido.setEstado("Proceso");
        }
        pedidoRepository.save(pedido);
    }

    /**
     * Libera empleados asignados a un pedido cuando este se completa.
     */
    public void procesarLiberacionEmpleados(Pedido pedido) throws Exception {
        List<AsignacionEmpleado> asignaciones = asignacionEmpleadoRepository.findAll().stream()
                .filter(asignacion -> asignacion.getPedido().getIdPedido() == pedido.getIdPedido() && asignacion.getFechaFin() == null)
                .collect(Collectors.toList());

        for (AsignacionEmpleado asignacion : asignaciones) {
            Empleado empleado = asignacion.getEmpleado();

            // Sumar el tiempo estimado del pedido a las horas trabajadas del empleado
            empleado.setHorasTrabajadas(empleado.getHorasTrabajadas() + pedido.getTiempoTotalEstimado());

            // Verificar las horas trabajadas y generar notificaciones según el umbral
            if (empleado.getHorasTrabajadas() >= 160) {
                String mensaje = "El empleado " + empleado.getNombre() + " ha trabajado más de 160 horas. Se debe contratar un empleado fijo.";
                System.out.println(mensaje);
                empleado.setNotificaciones(mensaje); // Agregar notificación
            } else if (empleado.getHorasTrabajadas() >= 80) {
                String mensaje = "El empleado " + empleado.getNombre() + " ha trabajado más de 80 horas. Se recomienda contratar un empleado temporal.";
                System.out.println(mensaje);
                empleado.setNotificaciones(mensaje); // Agregar notificación
            }

            asignacion.setFechaFin(new Date());
            asignacionEmpleadoRepository.save(asignacion);

            empleado.setPedidosAsignados(empleado.getPedidosAsignados() - 1);
            empleadoRepository.save(empleado);
        }

        reasignacionPedidosDemorados();
    }

    /**
     * Reasigna empleados disponibles a pedidos demorados en orden de llegada.
     */
    public void procesarReasignacionDemorados() {
        List<Pedido> pedidosDemorados = pedidoRepository.findByEstado("Demorado")
                .stream()
                .sorted((p1, p2) -> p1.getFechaPedido().compareTo(p2.getFechaPedido())) //Orden por fecha de llegada
                .collect(Collectors.toList());

        List<Pedido> pedidosEnProceso = pedidoRepository.findByEstado("Proceso");


        for (Pedido pedido : pedidosDemorados) {
            List<Empleado> empleadosDisponibles = obtenerEmpleadosDisponibles(pedido);
            try {
                // Verificar si el pedido ya tiene empleados asignados
                List<AsignacionEmpleado> asignacionesExistentes = asignacionEmpleadoRepository.findAll().stream()
                        .filter(asignacion -> asignacion.getPedido().getIdPedido() == pedido.getIdPedido() && asignacion.getFechaFin() == null)
                        .collect(Collectors.toList());

                //Obtiene empleados ya asignados al pedido
                List<Empleado> empleadosYaAsignados = asignacionesExistentes.stream()
                        .map(AsignacionEmpleado::getEmpleado)
                        .collect(Collectors.toList());

                int empleadosAsignados = asignacionesExistentes.size();
                int empleadosNecesarios = calcularEmpleadosPorPedido(pedido) -empleadosAsignados;

                if (empleadosNecesarios > 0 && !empleadosDisponibles.isEmpty()){
                    // Asignar empleados faltantes que no estén ya asignados al pedido
                    for (Empleado empleado : empleadosDisponibles){
                        if (empleadosNecesarios == 0) break;
                        //Verifica si el empleado no fue ya asignado a es pedido
                        if (!empleadosYaAsignados.contains(empleado)){
                            AsignacionEmpleado nuevaAsignacion = new AsignacionEmpleado();
                            nuevaAsignacion.setEmpleado(empleado);
                            nuevaAsignacion.setPedido(pedido);
                            nuevaAsignacion.setFechaAsignacion(new Date());

                            asignacionEmpleadoRepository.save(nuevaAsignacion);

                            empleado.setPedidosAsignados(empleado.getPedidosAsignados()+1);
                            empleadoRepository.save(empleado);

                            empleadosAsignados++;
                            empleadosNecesarios--;
                        }
                    }
                }

                //Si el pedido ya tienen los empleados necesarios, cambia al estado "Proceso"
                if (empleadosAsignados >= calcularEmpleadosPorPedido(pedido)) {
                    pedido.setEstado("Proceso");

                    double tiempoTotal = 0;
                    for (DetallePedido detalle : pedido.getDetallesPedido()) {
                        tiempoTotal += detalle.getTiempoEstimado();
                    }
                    pedido.setTiempoTotalEstimado(tiempoTotal);
                    pedidoRepository.save(pedido);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ajustarTiempoAdicionalParaPedidoDemorado();
    }

    /**
     * Método auxiliar para calcular el tiempo adicional necesario para un pedido demorado.
     */
    private double calcularTiempoAdicionalParaPedidoDemorado() {
        // Obtener pedidos en proceso y demorados
        List<Pedido> pedidosActivos = pedidoRepository.findByEstado("Proceso");
        List<Pedido> pedidosDemorados = pedidoRepository.findByEstado("Demorado");

        // Ordenar los tiempos estimados de menor a mayor
        List<Double> tiemposEstimados = pedidosActivos.stream()
                .map(Pedido::getTiempoTotalEstimado)
                .sorted()
                .collect(Collectors.toList());

        int index = pedidosDemorados.size();
        if (index < tiemposEstimados.size()) {
            return tiemposEstimados.get(index);
        }

        return 10; //Retornamos 10 si ya no hay pedidosActivos disponibles para asignar el tiempo de espera.
    }

    /**
     * Método auxiliar para ajustar el tiempo adicional necesario para un pedido demorado.
     */
    private void ajustarTiempoAdicionalParaPedidoDemorado() {
        // Obtener pedidos en proceso y demorados
        List<Pedido> pedidosActivos = pedidoRepository.findByEstado("Proceso");

        List<Pedido> pedidosDemorados = pedidoRepository.findByEstado("Demorado")
                .stream()
                .sorted((p1, p2) -> p1.getFechaPedido().compareTo(p2.getFechaPedido())) //Orden por fecha de llegada
                .collect(Collectors.toList());

        // Ordenar los tiempos estimados de menor a mayor
        List<Double> tiemposEstimados = pedidosActivos.stream()
                .map(Pedido::getTiempoTotalEstimado)
                .sorted()
                .collect(Collectors.toList());

        int index = 0;

        for (Pedido pedido : pedidosDemorados){
            double tiempoTotal = 0;
            for (DetallePedido detalle : pedido.getDetallesPedido()) {
                tiempoTotal += detalle.getTiempoEstimado();
            }

            if (index < tiemposEstimados.size()) {
                pedido.setTiempoTotalEstimado(tiempoTotal+tiemposEstimados.get(index));
                pedidoRepository.save(pedido);
            } else {
                pedido.setTiempoTotalEstimado(tiempoTotal+10);
                pedidoRepository.save(pedido);
            }
            index++;
        }
    }


    /**
     * Método auxiliar para obtener empleados disponibles.
     */
    private List<Empleado> obtenerEmpleadosDisponibles(Pedido pedido) {
        //Obtener empleados ya asignados al pedido
        List<Empleado> empleadosYaAsignados = asignacionEmpleadoRepository.findAll().stream()
                .filter(asignacionEmpleado -> asignacionEmpleado.getPedido().getIdPedido() == pedido.getIdPedido() && asignacionEmpleado.getFechaFin() == null)
                .map(AsignacionEmpleado::getEmpleado)
                .collect(Collectors.toList());

        //Filtra empleados displonibles que no estén asignados al pedido
        return empleadoService.getAllEmpleados().stream()
                .filter(empleado -> empleado.isAsistencia() && empleado.getPedidosAsignados() < 2 && !empleadosYaAsignados.contains(empleado))
                .sorted((e1, e2) -> {
                    //Calcular el tiempo restante del pedido activo para cada empleado
                    double tiempoRestanteE1 = calcularTiempoRestanteEmpleado(e1);
                    double tiempoRestanteE2 = calcularTiempoRestanteEmpleado(e2);

                    //Priorizar primero por número de pedidos asignados, luego por el menor tiempo restante
                    int comparacionPedidos = Integer.compare(e1.getPedidosAsignados(), e2.getPedidosAsignados());
                    if (comparacionPedidos == 0){
                        return Double.compare(tiempoRestanteE1,tiempoRestanteE2); //Priorizar menor tiempo restante
                    }
                    return comparacionPedidos;
                }) // Priorizar empleados con menos pedidos y con menos tiempo de espera
                .collect(Collectors.toList());
    }

    /**
     * Método auxiliar para calcular cuántos empleados son necesarios para un pedido.
     */
    private int calcularEmpleadosPorPedido(Pedido pedido) {
        List<Empleado> empleadosAsistentes = empleadoService.getAllEmpleados().stream()
                .filter(empleado -> empleado.isAsistencia()).collect(Collectors.toList());

        int totalEmpleadosAsistentesHoy = empleadosAsistentes.size();

        int totalElementos = pedido.getDetallesPedido().stream()
                .mapToInt(detalle -> detalle.getCantidad())
                .sum();

        if (totalEmpleadosAsistentesHoy == 1){
            return 1;
        } else if (totalEmpleadosAsistentesHoy == 2){
            if (totalElementos <= 6) return 1;
            return 2;
        }

        if (totalElementos <= 4) return 1;
        if (totalElementos <= 9) return 2;
        return 3;
    }

    /**
     * Calcula el tiempo restante del pedido activo más próximo de un empleado.
     */
    private double calcularTiempoRestanteEmpleado(Empleado empleado) {
        return asignacionEmpleadoRepository.findAll().stream()
                .filter(asignacion -> asignacion.getEmpleado().getIdEmpleado() == empleado.getIdEmpleado() && asignacion.getFechaFin() == null)
                .mapToDouble(asignacion -> asignacion.getPedido().getTiempoTotalEstimado())
                .min()
                .orElse(0); // Si no tiene pedidos, retorna 0
    }
}
