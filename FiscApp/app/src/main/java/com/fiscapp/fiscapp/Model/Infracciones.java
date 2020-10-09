package com.fiscapp.fiscapp.Model;


public class Infracciones {
    private String infracion_vehiculo_placa;
    private String infraccion_fecha;
    private String infracciones_preimpreso;
    private String infracciones_situacion;
    private String estado_descripcion;
    private String infracciones_obs;
    private String infraccion_numero;
    private String infraccion_descripcion;
    private String permitir_Acta;

    public Infracciones(String infracion_vehiculo_placa, String infraccion_fecha, String infracciones_preimpreso, String infracciones_situacion, String estado_descripcion, String infracciones_obs, String infraccion_numero, String infraccion_descripcion, String permitir_Acta) {
        this.infracion_vehiculo_placa = infracion_vehiculo_placa;
        this.infraccion_fecha = infraccion_fecha;
        this.infracciones_preimpreso = infracciones_preimpreso;
        this.infracciones_situacion = infracciones_situacion;
        this.estado_descripcion = estado_descripcion;
        this.infracciones_obs = infracciones_obs;
        this.infraccion_numero = infraccion_numero;
        this.infraccion_descripcion = infraccion_descripcion;
        this.permitir_Acta = permitir_Acta;
    }

    public String getInfracion_vehiculo_placa() {
        return infracion_vehiculo_placa;
    }

    public void setInfracion_vehiculo_placa(String infracion_vehiculo_placa) {
        this.infracion_vehiculo_placa = infracion_vehiculo_placa;
    }

    public String getInfraccion_fecha() {
        return infraccion_fecha;
    }

    public void setInfraccion_fecha(String infracciones_fecha_infraccion) {
        this.infraccion_fecha = infracciones_fecha_infraccion;
    }

    public String getInfracciones_preimpreso() {
        return infracciones_preimpreso;
    }

    public void setInfracciones_preimpreso(String infracciones_preimpreso) {
        this.infracciones_preimpreso = infracciones_preimpreso;
    }

    public String getInfracciones_situacion() {
        return infracciones_situacion;
    }

    public void setInfracciones_situacion(String infracciones_situacion) {
        this.infracciones_situacion = infracciones_situacion;
    }

    public String getEstado_descripcion() {
        return estado_descripcion;
    }

    public void setEstado_descripcion(String estado_descripcion) {
        this.estado_descripcion = estado_descripcion;
    }

    public String getInfracciones_obs() {
        return infracciones_obs;
    }

    public void setInfracciones_obs(String infracciones_obs) {
        this.infracciones_obs = infracciones_obs;
    }

    public String getInfraccion_numero() {
        return infraccion_numero;
    }

    public void setInfraccion_numero(String infraccion_numero) {
        this.infraccion_numero = infraccion_numero;
    }

    public String getInfraccion_descripcion() {
        return infraccion_descripcion;
    }

    public void setInfraccion_descripcion(String infraccion_descripcion) {
        this.infraccion_descripcion = infraccion_descripcion;
    }

    public String getPermitir_Acta() {
        return permitir_Acta;
    }

    public void setPermitir_Acta(String permitir_Acta) {
        this.permitir_Acta = permitir_Acta;
    }
}