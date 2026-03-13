package com.restaurante.app.model;

import java.io.Serializable;

public class Plato implements Serializable {
    private String nombre;
    private String descripcion;
    private double precio;
    private String categoria; // "Entrante", "Principal", "Postre", "Bebida"

    public Plato(String nombre, String descripcion, double precio, String categoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
    public String getCategoria() { return categoria; }
    
    public String getPrecioFormateado() { 
        return String.format("%.2f€", precio); 
    }
}
