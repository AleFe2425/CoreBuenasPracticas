package com.example.BackendCoreMuebles.Command;

import com.example.BackendCoreMuebles.Servicio.AsignacionEmpleadoService;

public class ReasignarEmpleadosCommand implements Command {
    private final AsignacionEmpleadoService asignacionEmpleadoService;

    public ReasignarEmpleadosCommand(AsignacionEmpleadoService asignacionEmpleadoService) {
        this.asignacionEmpleadoService = asignacionEmpleadoService;
    }

    @Override
    public void execute() throws Exception {
        // Llama al método interno del servicio que contiene la lógica principal
        asignacionEmpleadoService.procesarReasignacionDemorados();
    }
}
