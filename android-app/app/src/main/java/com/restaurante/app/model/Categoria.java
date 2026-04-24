package com.restaurante.app.model;

import java.io.Serializable;

/**
 * ENTIDAD CATEGORÍA
 * Representa la clasificación de los productos (ej: Bebidas, Postres, Carnes).
 */
public class Categoria implements Serializable {
    private String idCategoria;
    private String nombre;

    public Categoria() {}

    public Categoria(String idCategoria, String nombre) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
    }

    public String getIdCategoria() { return idCategoria; }
    public void setIdCategoria(String idCategoria) { this.idCategoria = idCategoria; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
