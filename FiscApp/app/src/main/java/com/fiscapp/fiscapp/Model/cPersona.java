package com.fiscapp.fiscapp.Model;

public class cPersona {

     private String persona_nombres;
     private String persona_dni;
     private String persona_tema;
     private String persona_informacion;
     private String persona_foto;
     private String observaciones;
     private String fecha;
     private String vigencia;
     private String tipo;

    public cPersona() {
        this.persona_nombres = "";
        this.persona_dni = "";
        this.persona_tema = "";
        this.persona_informacion = "";
        this.persona_foto = "";
        this.observaciones = "";
        this.fecha = "";
        this.vigencia = "";
        this.tipo = "";
    }

    public cPersona(String persona_nombres, String persona_dni, String persona_tema, String persona_informacion, String persona_foto, String observaciones, String fecha, String vigencia, String tipo) {
        this.persona_nombres = persona_nombres;
        this.persona_dni = persona_dni;
        this.persona_tema = persona_tema;
        this.persona_informacion = persona_informacion;
        this.persona_foto = persona_foto;
        this.observaciones = observaciones;
        this.fecha = fecha;
        this.vigencia = vigencia;
        this.tipo = tipo;
    }

    public String getPersona_nombres() {
        return persona_nombres;
    }

    public void setPersona_nombres(String persona_nombres) {
        this.persona_nombres = persona_nombres;
    }

    public String getPersona_dni() {
        return persona_dni;
    }

    public void setPersona_dni(String persona_dni) {
        this.persona_dni = persona_dni;
    }

    public String getPersona_tema() {
        return persona_tema;
    }

    public void setPersona_tema(String persona_tema) {
        this.persona_tema = persona_tema;
    }

    public String getPersona_informacion() {
        return persona_informacion;
    }

    public void setPersona_informacion(String persona_informacion) {
        this.persona_informacion = persona_informacion;
    }

    public String getPersona_foto() {
        return persona_foto;
    }

    public void setPersona_foto(String persona_foto) {
        this.persona_foto = persona_foto;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getVigencia() {
        return vigencia;
    }

    public void setVigencia(String vigencia) {
        this.vigencia = vigencia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
