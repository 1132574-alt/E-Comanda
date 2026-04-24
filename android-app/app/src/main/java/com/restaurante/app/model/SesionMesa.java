package com.restaurante.app.model;

import java.io.Serializable;

/**
 * ENTIDAD SESIÓN MESA
 * Representa la estancia de un cliente o grupo de clientes en una mesa física desde que se sientan hasta que pagan.
 * - Es la entidad que permite cumplir el requisito de trazabilidad de los pedidos (vincula mesa y tiempo).
 * - Incluye atributos para marcar hitos temporales (fechaInicio, fechaFin).
 */
public class SesionMesa implements Serializable {
    private String idSesion;
    private String idMesa;
    private long fechaInicio;
    private long fechaFin;
    private String estadoSesion; // "ACTIVA", "FINALIZADA"
    

    private boolean pedidoCamarero;
    private boolean pedidoCuenta;

    /**
     * Constructor vacío requerido por Firebase.
     */
    public SesionMesa() {}

    /**
     * Constructor para iniciar una nueva sesión de cliente.
     */
    public SesionMesa(String idSesion, String idMesa) {
        this.idSesion = idSesion;
        this.idMesa = idMesa;
        this.fechaInicio = System.currentTimeMillis();
        this.estadoSesion = "ACTIVA";
        this.pedidoCamarero = false;
        this.pedidoCuenta = false;
    }

    public String getIdSesion() { return idSesion; }
    public void setIdSesion(String idSesion) { this.idSesion = idSesion; }

    public String getIdMesa() { return idMesa; }
    public void setIdMesa(String idMesa) { this.idMesa = idMesa; }

    public long getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(long fechaInicio) { this.fechaInicio = fechaInicio; }

    public long getFechaFin() { return fechaFin; }
    public void setFechaFin(long fechaFin) { this.fechaFin = fechaFin; }

    public String getEstadoSesion() { return estadoSesion; }
    public void setEstadoSesion(String estadoSesion) { this.estadoSesion = estadoSesion; }

    public boolean isPedidoCamarero() { return pedidoCamarero; }
    public void setPedidoCamarero(boolean pedidoCamarero) { this.pedidoCamarero = pedidoCamarero; }

    public boolean isPedidoCuenta() { return pedidoCuenta; }
    public void setPedidoCuenta(boolean pedidoCuenta) { this.pedidoCuenta = pedidoCuenta; }
}
