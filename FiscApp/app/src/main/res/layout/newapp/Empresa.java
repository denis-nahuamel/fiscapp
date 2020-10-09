package com.example.willy.newapp;

import java.io.Serializable;

/**
 * Created by Willy on 12/2/2017.
 */

public class Empresa implements Serializable {
    private int Id;
    private String tipoRuta;
    private String ruta;
    private String nombre;
    private String nombreComercial;
    private int busImage;
    public int direccion;

    public int getBusImage() {
        return busImage;
    }

    public void setBusImage(int busImage) {
        this.busImage = busImage;
    }

    public Empresa(int id, String tipoRuta, String ruta, String nombre, String nombreComercial, int busImage) {
        Id = id;
        this.nombreComercial = nombreComercial;
        this.nombre = nombre;
        this.tipoRuta = tipoRuta;
        this.ruta = ruta;
        this.busImage = busImage;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoRuta() {
        return tipoRuta;
    }

    public void setTipoRuta(String tipoRuta) {
        this.tipoRuta = tipoRuta;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}
