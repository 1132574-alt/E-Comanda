package com.restaurante.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * CLASE CARRITO
 * Esta clase actúa como un contenedor temporal de datos durante la ejecución de la app del cliente.
 */
public class Carrito implements Serializable {
    private static Carrito instance;
    private Comanda comandaActual;
    private String idSesionActual;
    private String idMesa;
    private int numeroMesa;
    
    // Historial de comandas enviadas en esta sesión para calcular la cuenta total
    private List<Comanda> historialComandas;

    private Carrito() {
        comandaActual = new Comanda();
        historialComandas = new ArrayList<>();
    }


    public static synchronized Carrito getInstance() {
        if (instance == null) {
            instance = new Carrito();
        }
        return instance;
    }

    public String getIdSesionActual() { return idSesionActual; }
    public void setIdSesionActual(String idSesionActual) { this.idSesionActual = idSesionActual; }

    public String getIdMesa() { return idMesa; }
    public void setIdMesa(String idMesa) { this.idMesa = idMesa; }

    public int getNumeroMesa() { return numeroMesa; }
    public void setNumeroMesa(int numeroMesa) { this.numeroMesa = numeroMesa; }

    public void agregarProducto(Producto producto, int cantidad, String comentario) {
        LineaComanda linea = new LineaComanda(producto, cantidad, comentario);
        comandaActual.agregarLinea(linea);
    }

    public Comanda getComandaActual() { return comandaActual; }

    /**
     * Calcula el total de lo que hay actualmente en el carrito (productos aún no enviados a cocina).
     */
    public double getTotalCarrito() {
        return comandaActual.getTotal();
    }

    /**
     * Calcula la cuenta acumulada de toda la estancia del cliente.
     */
    public double getCuentaTotalSesion() {
        double total = getTotalCarrito();
        for (Comanda c : historialComandas) {
            total += c.getTotal();
        }
        return total;
    }

    public void añadirComandaAlHistorial(Comanda comanda) {
        historialComandas.add(comanda);
    }

    public List<Comanda> getHistorialComandas() {
        return historialComandas;
    }

    public boolean isEmpty() { return comandaActual.isEmpty(); }

    /**
     * Reinicia el carrito tras enviar un pedido a cocina.
     */
    public void limpiarCarrito() {
        comandaActual = new Comanda();
    }
    
    /**
     * Limpia todos los datos al finalizar la estancia (pago de cuenta).
     */
    public void resetearSesion() {
        idSesionActual = null;
        historialComandas.clear();
        limpiarCarrito();
    }
}
