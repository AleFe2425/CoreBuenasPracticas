package com.example.BackendCoreMuebles.Modelos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class AsignacionEmpleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAsignacion;

    @ManyToOne
    @JoinColumn(name = "idEmpleado", referencedColumnName = "idEmpleado")
    private Empleado empleado;

    @ManyToOne
    @JoinColumn(name = "idPedido", referencedColumnName = "idPedido")
    @JsonBackReference
    private Pedido pedido;

    @Column(name = "Fecha Asignacion")
    private Date fechaAsignacion;

    @Column(name = "Fecha Finalizacion")
    private Date fechaFin;

    public int getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(int idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
}
