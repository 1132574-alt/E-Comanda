package com.restaurante.app.model;

import java.io.Serializable;

/**
 * ENTIDAD PRODUCTO
 * - Incluye el campo 'disponible' para la gestión de stock en tiempo real.
 */
public class Producto implements Serializable {

    private String idProducto;
    private String nombre;
    private String descripcion;
    private double precio;
    private String categoria; 
    private boolean disponible; 
    private String precioFormateado;

    public Producto() {
    }

    public Producto(String idProducto, String nombre, String descripcion, double precio, String categoria, boolean disponible) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.disponible = disponible;
    }

    public String getIdProducto() { return idProducto; }
    public void setIdProducto(String idProducto) { this.idProducto = idProducto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public String getPrecioFormateado() {
        if (precioFormateado != null) return precioFormateado;
        return String.format("%.2f€", precio);
    }
    
    public void setPrecioFormateado(String precioFormateado) {
        this.precioFormateado = precioFormateado;
    }
}
