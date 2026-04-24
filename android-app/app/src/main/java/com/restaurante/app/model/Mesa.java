package com.restaurante.app.model;

import java.io.Serializable;

/**
 * ENTIDAD MESA
 * - Almacena las credenciales (usuario/contraseña) que la Tablet usa para autenticarse en el sistema.
 * - Se vincula con la SesionMesa para rastrear qué clientes están usando qué mesa física.
 */
public class Mesa implements Serializable {
    private String idMesa;
    private int numero;
    private String usuarioTablet;
    private String contrasenaTablet;

    public Mesa() {}

    public Mesa(String idMesa, int numero, String usuarioTablet, String contrasenaTablet) {
        this.idMesa = idMesa;
        this.numero = numero;
        this.usuarioTablet = usuarioTablet;
        this.contrasenaTablet = contrasenaTablet;
    }

    public String getIdMesa() { return idMesa; }
    public void setIdMesa(String idMesa) { this.idMesa = idMesa; }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public String getUsuarioTablet() { return usuarioTablet; }
    public void setUsuarioTablet(String usuarioTablet) { this.usuarioTablet = usuarioTablet; }

    public String getContrasenaTablet() { return contrasenaTablet; }
    public void setContrasenaTablet(String contrasenaTablet) { this.contrasenaTablet = contrasenaTablet; }
}
