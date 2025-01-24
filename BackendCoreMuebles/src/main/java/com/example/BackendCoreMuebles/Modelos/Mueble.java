package com.example.BackendCoreMuebles.Modelos;

import jakarta.persistence.*;

@Entity
public class Mueble {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMueble;

    @Column(name = "Mueble")
    private String nombreMueble;

    @Column(name = "Tiempo de Produccion")
    private double tiempoBaseProduccion;

    @Column(name = "Precio Mueble")
    private double precioMueble;

    public int getIdMueble() {
        return idMueble;
    }

    public void setIdMueble(int idMueble) {
        this.idMueble = idMueble;
    }

    public String getNombreMueble() {
        return nombreMueble;
    }

    public void setNombreMueble(String nombreMueble) {
        this.nombreMueble = nombreMueble;
    }

    public double getTiempoBaseProduccion() {
        return tiempoBaseProduccion;
    }

    public void setTiempoBaseProduccion(double tiempoBaseProduccion) {
        this.tiempoBaseProduccion = tiempoBaseProduccion;
    }

    public double getPrecioMueble() {
        return precioMueble;
    }

    public void setPrecioMueble(double precioMueble) {
        this.precioMueble = precioMueble;
    }
}
