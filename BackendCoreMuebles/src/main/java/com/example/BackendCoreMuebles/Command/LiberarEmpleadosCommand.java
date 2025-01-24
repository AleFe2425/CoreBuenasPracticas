package com.example.BackendCoreMuebles.Command;

import com.example.BackendCoreMuebles.Modelos.Pedido;
import com.example.BackendCoreMuebles.Servicio.AsignacionEmpleadoService;

public class LiberarEmpleadosCommand implements Command {
    private final AsignacionEmpleadoService asignacionEmpleadoService;
    private final Pedido pedido;

    public LiberarEmpleadosCommand(AsignacionEmpleadoService asignacionEmpleadoService, Pedido pedido) {
        this.asignacionEmpleadoService = asignacionEmpleadoService;
        this.pedido = pedido;
    }

    @Override
    public void execute() throws Exception {
        // Llama al método interno del servicio que contiene la lógica principal
        asignacionEmpleadoService.procesarLiberacionEmpleados(pedido);
    }
}
