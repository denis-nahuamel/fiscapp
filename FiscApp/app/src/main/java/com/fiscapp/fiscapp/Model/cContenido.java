package com.fiscapp.fiscapp.Model;

public class cContenido {

    private String contenido_id;
    private String contenido_titulo;
    private String contenido_texto;
    private String contenido_adjunto;
    private String contenido_idcategoria;
    private String contenido_categoria;


    public cContenido(String contenido_id, String contenido_titulo, String contenido_texto, String contenido_adjunto, String contenido_idcategoria, String contenido_categoria) {
        this.contenido_id = contenido_id;
        this.contenido_titulo = contenido_titulo;
        this.contenido_texto = contenido_texto;
        this.contenido_adjunto = contenido_adjunto;
        this.contenido_idcategoria = contenido_idcategoria;
        this.contenido_categoria = contenido_categoria;
    }

    public String getContenido_id() {
        return contenido_id;
    }

    public void setContenido_id(String contenido_id) {
        this.contenido_id = contenido_id;
    }

    public String getContenido_titulo() {
        return contenido_titulo;
    }

    public void setContenido_titulo(String contenido_titulo) {
        this.contenido_titulo = contenido_titulo;
    }

    public String getContenido_texto() {
        return contenido_texto;
    }

    public void setContenido_texto(String contenido_texto) {
        this.contenido_texto = contenido_texto;
    }

    public String getContenido_adjunto() {
        return contenido_adjunto;
    }

    public void setContenido_adjunto(String contenido_adjunto) {
        this.contenido_adjunto = contenido_adjunto;
    }

    public String getContenido_idcategoria() {
        return contenido_idcategoria;
    }

    public void setContenido_idcategoria(String contenido_idcategoria) {
        this.contenido_idcategoria = contenido_idcategoria;
    }

    public String getContenido_categoria() {
        return contenido_categoria;
    }

    public void setContenido_categoria(String contenido_categoria) {
        this.contenido_categoria = contenido_categoria;
    }
}
