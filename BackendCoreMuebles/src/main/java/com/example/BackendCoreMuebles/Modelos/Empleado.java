package com.example.BackendCoreMuebles.Modelos;

import jakarta.persistence.*;

@Entity
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEmpleado;

    @Column(name = "Nombre")
    private String nombre;

    @Column(name = "Pedidos Asignados")
    private int pedidosAsignados;

    @Column(name = "Asistencia Laboral")
    private boolean asistencia;

    @Column(name = "Horas Trabajadas")
    private double horasTrabajadas = 0.0
            ;

    @Column(name = "Notificaciones de Horas Trabajadas")
    private String notificaciones;

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPedidosAsignados() {
        return pedidosAsignados;
    }

    public void setPedidosAsignados(int pedidosAsignados) {
        this.pedidosAsignados = pedidosAsignados;
    }

    public boolean isAsistencia() {
        return asistencia;
    }

    public void setAsistencia(boolean asistencia) {
        this.asistencia = asistencia;
    }

    public double getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setHorasTrabajadas(double horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }

    public String getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(String notificaciones) {
        this.notificaciones = notificaciones;
    }
}
