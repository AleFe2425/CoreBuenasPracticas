package com.example.BackendCoreMuebles.Modelos;

import jakarta.persistence.*;

@Entity
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMaterial;

    @Column(name = "Nombre")
    private String nombreMaterial;

    @Column(name = "Factor de tiempo")
    private double factorTiempo;

    @Column(name = "Precio Material")
    private double precioMaterial;

    public int getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
    }

    public String getNombreMaterial() {
        return nombreMaterial;
    }

    public void setNombreMaterial(String nombreMaterial) {
        this.nombreMaterial = nombreMaterial;
    }

    public double getFactorTiempo() {
        return factorTiempo;
    }

    public void setFactorTiempo(double factorTiempo) {
        this.factorTiempo = factorTiempo;
    }

    public double getPrecioMaterial() {
        return precioMaterial;
    }

    public void setPrecioMaterial(double precioMaterial) {
        this.precioMaterial = precioMaterial;
    }
}
