package com.restaurante.app.model;

import java.util.ArrayList;
import java.util.List;

public class Carrito {
    private static Carrito instance;
    private List<Plato> productos;

    private Carrito() {
        productos = new ArrayList<>();
    }

    public static synchronized Carrito getInstance() {
        if (instance == null) {
            instance = new Carrito();
        }
        return instance;
    }

    public void agregarPlato(Plato plato) {
        productos.add(plato);
    }

    public List<Plato> getProductos() {
        return productos;
    }

    public double getTotal() {
        double total = 0;
        for (Plato p : productos) {
            total += p.getPrecio();
        }
        return total;
    }

    public void limpiarCarrito() {
        productos.clear();
    }
}
