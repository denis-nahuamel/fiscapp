package com.fiscapp.fiscapp.Model;

public class TipoIncidencia {
    private String value;
    private String option;

    public TipoIncidencia(String Value, String Option) {
        this.value = Value;
        this.option = Option;

    }
    // Getter Methods
    @Override
    public String toString() {
        return getOption();
    }
    public String getValue() {
        return value;
    }

    public String getOption() {
        return option;
    }

    // Setter Methods

    public void setValue(String value) {
        this.value = value;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
