package com.example.BackendCoreMuebles.Modelos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDetalle;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "idPedido", referencedColumnName = "idPedido")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "idMueble", referencedColumnName = "idMueble")
    private Mueble mueble;

    @ManyToOne
    @JoinColumn(name = "idMaterial", referencedColumnName = "idMaterial")
    private Material material;

    @Column(name = "Cantidad")
    private int cantidad;

    @Column(name = "Tiempo Estimado")
    private double tiempoEstimado;

    @Column(name = "Precio Unitario")
    private double precioUnitario;

    @Column(name = "Precio Subtotal")
    private double precioSubtotal;

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Mueble getMueble() {
        return mueble;
    }

    public void setMueble(Mueble mueble) {
        this.mueble = mueble;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(double tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getPrecioSubtotal() {
        return precioSubtotal;
    }

    public void setPrecioSubtotal(double precioSubtotal) {
        this.precioSubtotal = precioSubtotal;
    }


    // Constructor privado para usar con el Builder
    private DetallePedido() {}

    // Builder interno para DetallePedido
    public static class Builder {
        private Mueble mueble;
        private Material material;
        private int cantidad;
        private Pedido pedido;

        public Builder setMueble(Mueble mueble) {
            this.mueble = mueble;
            return this;
        }

        public Builder setMaterial(Material material) {
            this.material = material;
            return this;
        }

        public Builder setCantidad(int cantidad) {
            this.cantidad = cantidad;
            return this;
        }

        public Builder setPedido(Pedido pedido) {
            this.pedido = pedido;
            return this;
        }

        public DetallePedido build() {
            if (mueble == null || material == null || cantidad <= 0) {
                throw new IllegalArgumentException("Mueble, material o cantidad no son válidos");
            }

            DetallePedido detalle = new DetallePedido();
            detalle.mueble = this.mueble;
            detalle.material = this.material;
            detalle.cantidad = this.cantidad;
            detalle.pedido = this.pedido;

            detalle.tiempoEstimado = cantidad * (mueble.getTiempoBaseProduccion() * material.getFactorTiempo());
            detalle.precioUnitario = mueble.getPrecioMueble() * material.getPrecioMaterial();
            detalle.precioSubtotal = detalle.precioUnitario * cantidad;

            if (detalle.tiempoEstimado <= 0 || detalle.precioUnitario <= 0 || detalle.precioSubtotal <= 0) {
                throw new IllegalArgumentException("Los cálculos de tiempo o precio son incorrectos");
            }

            return detalle;
        }
    }
}


