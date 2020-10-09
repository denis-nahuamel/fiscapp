package com.fiscapp.fiscapp.Model;



import java.io.Serializable;

public class Incidencia implements Serializable {
    private String latitud;
    private String longitud;
    private String incidencia;
    private String descripcion;
    private String fecha_incidencia;
    private String leido;
    private String notificacion;

    public String getTipo_incidencia() {
        return tipo_incidencia;
    }

    public void setTipo_incidencia(String tipo_incidencia) {
        this.tipo_incidencia = tipo_incidencia;
    }

    private String tipo_incidencia;




    private String usuario_registro;
    private String id;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int status;


    // Getter Methods
    public Incidencia(String Latitud, String Longitud, String Incidencia,
                      String Descripcion, String Fecha_incidencia, String Leido,
                      String Usuario_registro, String Id, String notificacion,int Status, String Tipo_Incidencia) {
        this.latitud = Latitud;
        this.longitud = Longitud;
        this.incidencia = Incidencia;
        this.descripcion = Descripcion;
        this.fecha_incidencia=Fecha_incidencia;
        this.tipo_incidencia=Tipo_Incidencia;
        this.leido=Leido;
        this.usuario_registro=Usuario_registro;
        this.id=Id;
        this.notificacion = notificacion;
        this.status=Status;
    }

    public String getLatitud() {
        return latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public String getIncidencia() {
        return incidencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFecha_incidencia() {
        return fecha_incidencia;
    }

    public String getLeido() {
        return leido;
    }

    public String getUsuario_registro() {
        return usuario_registro;
    }

    // Setter Methods

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public void setIncidencia(String incidencia) {
        this.incidencia = incidencia;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFecha_incidencia(String fecha_incidencia) {
        this.fecha_incidencia = fecha_incidencia;
    }

    public void setLeido(String leido) {
        this.leido = leido;
    }

    public void setUsuario_registro(String usuario_registro) {
        this.usuario_registro = usuario_registro;
    }



    public String getId() {
        return id;
    }

    // Setter Methods

    public void setId(String id) {
        this.id = id;
    }

    public String getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(String notificacion) {
        this.notificacion = notificacion;
    }

}
