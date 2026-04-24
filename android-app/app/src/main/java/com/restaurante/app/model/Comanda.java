package com.restaurante.app.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ENTIDAD COMANDA
 * Representa un pedido específico enviado por una mesa a la cocina.
 * - Gestiona el "Estado" del ciclo de vida del producto: SOLICITADO -> EN PREPARACIÓN -> LISTO.
 */
@IgnoreExtraProperties
public class Comanda implements Serializable {

    private String idComanda;
    private String idSesion;
    private String idMesa;
    private String fechaHoraEnvio;
    private String estado;
    private List<LineaComanda> lineas;

    public Comanda() {
        this.lineas = new ArrayList<>();
        this.estado = "SOLICITADO"; 
    }


    public void agregarLinea(LineaComanda linea) {
        if (this.lineas == null) {
            this.lineas = new ArrayList<>();
        }
        this.lineas.add(linea);
    }

    @Exclude 
    public boolean isEmpty() {
        return lineas == null || lineas.isEmpty();
    }


    @Exclude 
    public double getTotal() {
        double total = 0;
        if (lineas != null) {
            for (LineaComanda linea : lineas) {
                total += (linea.getPrecioVenta() * linea.getCantidad());
            }
        }
        return total;
    }

    public String getIdComanda() { return idComanda; }
    public void setIdComanda(String idComanda) { this.idComanda = idComanda; }

    public String getIdSesion() { return idSesion; }
    public void setIdSesion(String idSesion) { this.idSesion = idSesion; }

    public String getIdMesa() { return idMesa; }
    public void setIdMesa(String idMesa) { this.idMesa = idMesa; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFechaHoraEnvio() { return fechaHoraEnvio; }
    public void setFechaHoraEnvio(String fechaHoraEnvio) { this.fechaHoraEnvio = fechaHoraEnvio; }

    public List<LineaComanda> getLineas() { return lineas; }
    public void setLineas(List<LineaComanda> lineas) { this.lineas = lineas; }
}
