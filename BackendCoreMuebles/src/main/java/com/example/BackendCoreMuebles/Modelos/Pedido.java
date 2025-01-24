package com.example.BackendCoreMuebles.Modelos;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPedido;

    @ManyToOne
    @JoinColumn(name = "idCliente", referencedColumnName = "idCliente")
    private Cliente cliente;

    @Column(name = "Fecha Pedido")
    private Date fechaPedido;

    @Column(name = "Tiempo Estimado")
    private double tiempoTotalEstimado;

    @Column(name = "Estado del Pedido")
    private String estado;

    @Column(name = "Precio Total")
    private double precioTotal;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<DetallePedido> detallesPedido;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<AsignacionEmpleado> asignaciones;

    private boolean tiempoAdicionalAsignado;

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Date getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public double getTiempoTotalEstimado() {
        return tiempoTotalEstimado;
    }

    public void setTiempoTotalEstimado(double tiempoTotalEstimado) {
        this.tiempoTotalEstimado = tiempoTotalEstimado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public List<DetallePedido> getDetallesPedido() {
        return detallesPedido;
    }

    public void setDetallesPedido(List<DetallePedido> detallesPedido) {
        this.detallesPedido = detallesPedido;
    }

    public List<AsignacionEmpleado> getAsignaciones() {
        return asignaciones;
    }

    public void setAsignaciones(List<AsignacionEmpleado> asignaciones) {
        this.asignaciones = asignaciones;
    }

    public boolean isTiempoAdicionalAsignado() {
        return tiempoAdicionalAsignado;
    }

    public void setTiempoAdicionalAsignado(boolean tiempoAdicionalAsignado) {
        this.tiempoAdicionalAsignado = tiempoAdicionalAsignado;
    }
}