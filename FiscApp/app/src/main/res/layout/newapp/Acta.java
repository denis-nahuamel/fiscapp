package com.example.willy.newapp;

import java.io.Serializable;

/**
 * Created by Willy on 12/2/2017.
 */

public class Acta implements Serializable {

    private int Id;
    private String conductor;
    private String razon_social;

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    private String placa;

    public Acta(int id, String conductor, String razon_social, String placa) {
        this.Id = id;
        this.conductor = conductor;
        this.razon_social = razon_social;
        this.placa = placa;

    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getConductor() {
        return conductor;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    public String getRazon_social() {
        return razon_social;
    }

    public void setRazon_social(String razon_social) {
        this.razon_social = razon_social;
    }








}
