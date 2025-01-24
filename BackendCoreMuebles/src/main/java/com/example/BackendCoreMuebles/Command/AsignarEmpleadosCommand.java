package com.example.BackendCoreMuebles.Command;

import com.example.BackendCoreMuebles.Modelos.Pedido;
import com.example.BackendCoreMuebles.Servicio.AsignacionEmpleadoService;

public class AsignarEmpleadosCommand implements Command {
    private final AsignacionEmpleadoService asignacionEmpleadoService;
    private final Pedido pedido;

    public AsignarEmpleadosCommand(AsignacionEmpleadoService asignacionEmpleadoService, Pedido pedido) {
        this.asignacionEmpleadoService = asignacionEmpleadoService;
        this.pedido = pedido;
    }

    @Override
    public void execute() throws Exception {
        // Llama a la lógica interna del servicio para asignar empleados, no al método principal
        asignacionEmpleadoService.procesarAsignacionEmpleados(pedido);
    }
}
