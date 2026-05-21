package com.restaurante.app.model;

import java.io.Serializable;

/**
 * ENTIDAD LINEA COMANDA
 * Representa el detalle individual de un producto dentro de un pedido.
 * - Incluye un campo 'comentario' para personalizaciones del cliente (ej: "sin cebolla").
 */
public class LineaComanda implements Serializable {

    private int idLinea;
    private int cantidad;
    private double precioVenta;
    private String comentario;
    private Producto producto;


    public LineaComanda() {
    }

    public LineaComanda(Producto producto, int cantidad, String comentario) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.comentario = comentario;
        // Se congela el precio para el histórico de ventas
        this.precioVenta = producto.getPrecio();
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(int idLinea) {
        this.idLinea = idLinea;
    }
}
