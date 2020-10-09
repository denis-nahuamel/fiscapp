package com.fiscapp.fiscapp.Model;

import java.io.Serializable;

/**
 * Created by Willy on 12/2/2017.
 */

public class Acta implements Serializable {

    private String acta_id;
    private String acta_preimpreso;
    private String acta_operador;
    private String acta_fecha_registro;
    private String acta_observaciones;
    private String acta_tipo_servicio;
    private String acta_estado;
    private String vehiculo_placa;
    private String vehiculo_ws;
    private String conductor_tipo_documento;

    public String getReglamento() {
        return reglamento;
    }

    public void setReglamento(String reglamento) {
        this.reglamento = reglamento;
    }

    public String getActa_preimpreso_txt() {
        return acta_preimpreso_txt;
    }

    public void setActa_preimpreso_txt(String acta_preimpreso_txt) {
        this.acta_preimpreso_txt = acta_preimpreso_txt;
    }

    private String conductor_dni;
    private String conductor_nombres;
    private String conductor_apaterno;
    private String conductor_amaterno;
    private String conductor_licencia;
    private String conductor_licencia_clase;
    private String conductor_licencia_categoria;
    private String conductor_direccion;
    private String conductor_ubigeo;
    private String infraccion_tipo;
    private String reglamento;
    private String infraccion_via;
    private String empresa_ruc;
    private String empresa_rsocial;
    private String cobrador_dni;
    private String cobrador_nombres;
    private String cobrador_apaterno;
    private String cobrador_amaterno;
    private String cobrador_direccion;
    private String cobrador_tipo_documento;
    private String vehiculo_moto;
    private String empresa_direccion;
    private String vehiculo_tarjeta;
    private String cobrador_edad;
    private String tipo_infraccion_txt;
    private String conductor_tipo_documento_txt;
    private String cobrador_tipo_documento_txt;
    private String conductor_licencia_clase_txt;
    private String conductor_licencia_categoria_txt;
    private String acta_tipo_servicio_txt;
    private String reglamento_txt;
    private String acta_preimpreso_txt;

    public String getReglamento_txt() {
        return reglamento_txt;
    }

    public void setReglamento_txt(String reglamento_txt) {
        this.reglamento_txt = reglamento_txt;
    }

    public String getTipo_infraccion_txt() {
        return tipo_infraccion_txt;
    }

    public void setTipo_infraccion_txt(String tipo_infraccion_txt) {
        this.tipo_infraccion_txt = tipo_infraccion_txt;
    }

    public String getConductor_tipo_documento_txt() {
        return conductor_tipo_documento_txt;
    }

    public void setConductor_tipo_documento_txt(String conductor_tipo_documento_txt) {
        this.conductor_tipo_documento_txt = conductor_tipo_documento_txt;
    }

    public String getCobrador_tipo_documento_txt() {
        return cobrador_tipo_documento_txt;
    }

    public void setCobrador_tipo_documento_txt(String cobrador_tipo_documento_txt) {
        this.cobrador_tipo_documento_txt = cobrador_tipo_documento_txt;
    }

    public String getConductor_licencia_clase_txt() {
        return conductor_licencia_clase_txt;
    }

    public void setConductor_licencia_clase_txt(String conductor_licencia_clase_txt) {
        this.conductor_licencia_clase_txt = conductor_licencia_clase_txt;
    }

    public String getConductor_licencia_categoria_txt() {
        return conductor_licencia_categoria_txt;
    }

    public void setConductor_licencia_categoria_txt(String conductor_licencia_categoria_txt) {
        this.conductor_licencia_categoria_txt = conductor_licencia_categoria_txt;
    }

    public String getActa_tipo_servicio_txt() {
        return acta_tipo_servicio_txt;
    }

    public void setActa_tipo_servicio_txt(String acta_tipo_servicio_txt) {
        this.acta_tipo_servicio_txt = acta_tipo_servicio_txt;
    }

    public Acta(String acta_id, String acta_preimpreso, String acta_operador, String acta_fecha_registro, String acta_observaciones, String acta_tipo_servicio, String acta_estado, String vehiculo_placa, String vehiculo_ws, String conductor_tipo_documento, String conductor_dni, String conductor_nombres, String conductor_apaterno, String conductor_amaterno, String conductor_licencia, String conductor_licencia_clase, String conductor_licencia_categoria, String conductor_direccion, String conductor_ubigeo, String infraccion_tipo, String reglamento, String infraccion_via, String empresa_ruc, String empresa_rsocial, String cobrador_dni, String cobrador_nombres, String cobrador_apaterno, String cobrador_amaterno, String cobrador_direccion, String cobrador_tipo_documento, String vehiculo_moto, String empresa_direccion, String vehiculo_tarjeta, String cobrador_edad, String tipo_infraccion_txt, String conductor_tipo_documento_txt, String cobrador_tipo_documento_txt, String conductor_licencia_clase_txt, String conductor_licencia_categoria_txt, String acta_tipo_servicio_txt, String reglamento_txt, String acta_preimpreso_txt, int acta_educativa) {
        this.acta_id = acta_id;
        this.acta_preimpreso = acta_preimpreso;
        this.acta_operador = acta_operador;
        this.acta_fecha_registro = acta_fecha_registro;
        this.acta_observaciones = acta_observaciones;
        this.acta_tipo_servicio = acta_tipo_servicio;
        this.acta_estado = acta_estado;
        this.vehiculo_placa = vehiculo_placa;
        this.vehiculo_ws = vehiculo_ws;
        this.conductor_tipo_documento = conductor_tipo_documento;
        this.conductor_dni = conductor_dni;
        this.conductor_nombres = conductor_nombres;
        this.conductor_apaterno = conductor_apaterno;
        this.conductor_amaterno = conductor_amaterno;
        this.conductor_licencia = conductor_licencia;
        this.conductor_licencia_clase = conductor_licencia_clase;
        this.conductor_licencia_categoria = conductor_licencia_categoria;
        this.conductor_direccion = conductor_direccion;
        this.conductor_ubigeo = conductor_ubigeo;
        this.infraccion_tipo = infraccion_tipo;
        this.reglamento = reglamento;
        this.infraccion_via = infraccion_via;
        this.empresa_ruc = empresa_ruc;
        this.empresa_rsocial = empresa_rsocial;
        this.cobrador_dni = cobrador_dni;
        this.cobrador_nombres = cobrador_nombres;
        this.cobrador_apaterno = cobrador_apaterno;
        this.cobrador_amaterno = cobrador_amaterno;
        this.cobrador_direccion = cobrador_direccion;
        this.cobrador_tipo_documento = cobrador_tipo_documento;
        this.vehiculo_moto = vehiculo_moto;
        this.empresa_direccion = empresa_direccion;
        this.vehiculo_tarjeta = vehiculo_tarjeta;
        this.cobrador_edad = cobrador_edad;
        this.tipo_infraccion_txt = tipo_infraccion_txt;
        this.conductor_tipo_documento_txt = conductor_tipo_documento_txt;
        this.cobrador_tipo_documento_txt = cobrador_tipo_documento_txt;
        this.conductor_licencia_clase_txt = conductor_licencia_clase_txt;
        this.conductor_licencia_categoria_txt = conductor_licencia_categoria_txt;
        this.acta_tipo_servicio_txt = acta_tipo_servicio_txt;
        this.reglamento_txt = reglamento_txt;
        this.acta_preimpreso_txt = acta_preimpreso_txt;
        this.acta_educativa = acta_educativa;
    }

    /*public Acta(String acta_id, String acta_preimpreso, String acta_operador, String acta_fecha_registro, String acta_observaciones, String acta_tipo_servicio, String acta_estado, String vehiculo_placa, String vehiculo_ws, String conductor_tipo_documento, String conductor_dni, String conductor_nombres, String conductor_apaterno, String conductor_amaterno, String conductor_licencia, String conductor_licencia_clase, String conductor_licencia_categoria, String conductor_direccion, String conductor_ubigeo, String infraccion_tipo, String infraccion_via, String empresa_ruc, String empresa_rsocial, String cobrador_dni, String cobrador_nombres, String cobrador_apaterno, String cobrador_amaterno, String cobrador_direccion, String cobrador_tipo_documento, String vehiculo_moto, String empresa_direccion, String vehiculo_tarjeta, String cobrador_edad,
                String reglamento_txt, String tipo_infraccion_txt, String conductor_tipo_documento_txt,String cobrador_tipo_documento_txt, String conductor_licencia_clase_txt, String conductor_licencia_categoria_txt, String acta_tipo_servicio_txt) {

        this.reglamento_txt = reglamento_txt;
        this.tipo_infraccion_txt = tipo_infraccion_txt;
        this.conductor_tipo_documento_txt = conductor_tipo_documento_txt;
        this.cobrador_tipo_documento_txt = cobrador_tipo_documento_txt;
        this.conductor_licencia_clase_txt = conductor_tipo_documento_txt;
        this.conductor_licencia_categoria_txt = conductor_tipo_documento_txt;
        this.acta_tipo_servicio_txt = conductor_tipo_documento_txt;



        this.acta_id = acta_id;
        this.acta_preimpreso = acta_preimpreso;
        this.acta_operador = acta_operador;
        this.acta_fecha_registro = acta_fecha_registro;
        this.acta_observaciones = acta_observaciones;
        this.acta_tipo_servicio = acta_tipo_servicio;
        this.acta_estado = acta_estado;
        this.vehiculo_placa = vehiculo_placa;
        this.vehiculo_ws = vehiculo_ws;
        this.conductor_tipo_documento = conductor_tipo_documento;
        this.conductor_dni = conductor_dni;
        this.conductor_nombres = conductor_nombres;
        this.conductor_apaterno = conductor_apaterno;
        this.conductor_amaterno = conductor_amaterno;
        this.conductor_licencia = conductor_licencia;
        this.conductor_licencia_clase = conductor_licencia_clase;
        this.conductor_licencia_categoria = conductor_licencia_categoria;
        this.conductor_direccion = conductor_direccion;
        this.conductor_ubigeo = conductor_ubigeo;
        this.infraccion_tipo = infraccion_tipo;
        this.infraccion_via = infraccion_via;
        this.empresa_ruc = empresa_ruc;
        this.empresa_rsocial = empresa_rsocial;
        this.cobrador_dni = cobrador_dni;
        this.cobrador_nombres = cobrador_nombres;
        this.cobrador_apaterno = cobrador_apaterno;
        this.cobrador_amaterno = cobrador_amaterno;
        this.cobrador_direccion = cobrador_direccion;
        this.cobrador_tipo_documento = cobrador_tipo_documento;
        this.vehiculo_moto = vehiculo_moto;
        this.empresa_direccion = empresa_direccion;
        this.vehiculo_tarjeta = vehiculo_tarjeta;
        this.cobrador_edad = cobrador_edad;

    }*/


    public Acta(String acta_id, String acta_preimpreso, String acta_operador, String acta_fecha_registro, String acta_observaciones, String acta_tipo_servicio, String acta_estado, String vehiculo_placa, String vehiculo_ws, String conductor_tipo_documento, String conductor_dni, String conductor_nombres, String conductor_apaterno, String conductor_amaterno, String conductor_licencia, String conductor_licencia_clase, String conductor_licencia_categoria, String conductor_direccion, String conductor_ubigeo, String infraccion_tipo, String infraccion_via, String empresa_ruc, String empresa_rsocial, String cobrador_dni, String cobrador_nombres, String cobrador_apaterno, String cobrador_amaterno, String cobrador_direccion, String cobrador_tipo_documento, String vehiculo_moto, String empresa_direccion, String vehiculo_tarjeta, String cobrador_edad) {

        this.reglamento_txt = reglamento_txt;
        this.tipo_infraccion_txt = tipo_infraccion_txt;
        this.conductor_tipo_documento_txt = conductor_tipo_documento_txt;
        this.cobrador_tipo_documento_txt = cobrador_tipo_documento_txt;
        this.conductor_licencia_clase_txt = conductor_tipo_documento_txt;
        this.conductor_licencia_categoria_txt = conductor_tipo_documento_txt;
        this.acta_tipo_servicio_txt = conductor_tipo_documento_txt;



        this.acta_id = acta_id;
        this.acta_preimpreso = acta_preimpreso;
        this.acta_operador = acta_operador;
        this.acta_fecha_registro = acta_fecha_registro;
        this.acta_observaciones = acta_observaciones;
        this.acta_tipo_servicio = acta_tipo_servicio;
        this.acta_estado = acta_estado;
        this.vehiculo_placa = vehiculo_placa;
        this.vehiculo_ws = vehiculo_ws;
        this.conductor_tipo_documento = conductor_tipo_documento;
        this.conductor_dni = conductor_dni;
        this.conductor_nombres = conductor_nombres;
        this.conductor_apaterno = conductor_apaterno;
        this.conductor_amaterno = conductor_amaterno;
        this.conductor_licencia = conductor_licencia;
        this.conductor_licencia_clase = conductor_licencia_clase;
        this.conductor_licencia_categoria = conductor_licencia_categoria;
        this.conductor_direccion = conductor_direccion;
        this.conductor_ubigeo = conductor_ubigeo;
        this.infraccion_tipo = infraccion_tipo;
        this.infraccion_via = infraccion_via;
        this.empresa_ruc = empresa_ruc;
        this.empresa_rsocial = empresa_rsocial;
        this.cobrador_dni = cobrador_dni;
        this.cobrador_nombres = cobrador_nombres;
        this.cobrador_apaterno = cobrador_apaterno;
        this.cobrador_amaterno = cobrador_amaterno;
        this.cobrador_direccion = cobrador_direccion;
        this.cobrador_tipo_documento = cobrador_tipo_documento;
        this.vehiculo_moto = vehiculo_moto;
        this.empresa_direccion = empresa_direccion;
        this.vehiculo_tarjeta = vehiculo_tarjeta;
        this.cobrador_edad = cobrador_edad;

    }

    public int getActa_educativa() {
        return acta_educativa;
    }

    public void setActa_educativa(int acta_educativa) {
        this.acta_educativa = acta_educativa;
    }

    private int acta_educativa = 0;
    private String tipo_servicio_txt, codruta_transporte;

    public String getCodruta_transporte() {
        return codruta_transporte;
    }

    public void setCodruta_transporte(String codruta_transporte) {
        this.codruta_transporte = codruta_transporte;
    }

    public String getTipo_servicio_txt() {
        return tipo_servicio_txt;
    }

    public void setTipo_servicio_txt(String tipo_servicio_txt) {
        this.tipo_servicio_txt = tipo_servicio_txt;
    }

    public Acta(String acta_id, String acta_preimpreso, String acta_operador, String acta_fecha_registro, String acta_observaciones, String acta_tipo_servicio, String acta_estado, String vehiculo_placa, String vehiculo_ws, String conductor_tipo_documento, String conductor_dni, String conductor_nombres, String conductor_apaterno, String conductor_amaterno, String conductor_licencia, String conductor_licencia_clase, String conductor_licencia_categoria, String conductor_direccion, String conductor_ubigeo, String infraccion_tipo, String infraccion_via, String empresa_ruc, String empresa_rsocial, String cobrador_dni, String cobrador_nombres, String cobrador_apaterno, String cobrador_amaterno, String cobrador_direccion, String cobrador_tipo_documento, String vehiculo_moto, String empresa_direccion, String vehiculo_tarjeta, String cobrador_edad,
                String reglamento_txt, String tipo_infraccion_txt, int acta_educativa, String tipo_servicio_txt, String codruta_transporte) {

        this.reglamento_txt = reglamento_txt;
        this.tipo_infraccion_txt = tipo_infraccion_txt;

        this.acta_educativa = acta_educativa;



        this.acta_id = acta_id;
        this.acta_preimpreso = acta_preimpreso;
        this.acta_operador = acta_operador;
        this.acta_fecha_registro = acta_fecha_registro;
        this.acta_observaciones = acta_observaciones;
        this.acta_tipo_servicio = acta_tipo_servicio;
        this.acta_estado = acta_estado;
        this.vehiculo_placa = vehiculo_placa;
        this.vehiculo_ws = vehiculo_ws;
        this.conductor_tipo_documento = conductor_tipo_documento;
        this.conductor_dni = conductor_dni;
        this.conductor_nombres = conductor_nombres;
        this.conductor_apaterno = conductor_apaterno;
        this.conductor_amaterno = conductor_amaterno;
        this.conductor_licencia = conductor_licencia;
        this.conductor_licencia_clase = conductor_licencia_clase;
        this.conductor_licencia_categoria = conductor_licencia_categoria;
        this.conductor_direccion = conductor_direccion;
        this.conductor_ubigeo = conductor_ubigeo;
        this.infraccion_tipo = infraccion_tipo;
        this.infraccion_via = infraccion_via;
        this.empresa_ruc = empresa_ruc;
        this.empresa_rsocial = empresa_rsocial;
        this.cobrador_dni = cobrador_dni;
        this.cobrador_nombres = cobrador_nombres;
        this.cobrador_apaterno = cobrador_apaterno;
        this.cobrador_amaterno = cobrador_amaterno;
        this.cobrador_direccion = cobrador_direccion;
        this.cobrador_tipo_documento = cobrador_tipo_documento;
        this.vehiculo_moto = vehiculo_moto;
        this.empresa_direccion = empresa_direccion;
        this.vehiculo_tarjeta = vehiculo_tarjeta;
        this.cobrador_edad = cobrador_edad;
        this.tipo_servicio_txt = tipo_servicio_txt;
        this.codruta_transporte = codruta_transporte;

    }

    public String getVehiculo_moto() {
        return vehiculo_moto;
    }

    public void setVehiculo_moto(String vehiculo_moto) {
        this.vehiculo_moto = vehiculo_moto;
    }

    public String getEmpresa_direccion() {
        return empresa_direccion;
    }

    public void setEmpresa_direccion(String empresa_direccion) {
        this.empresa_direccion = empresa_direccion;
    }

    public String getVehiculo_tarjeta() {
        return vehiculo_tarjeta;
    }

    public void setVehiculo_tarjeta(String vehiculo_tarjeta) {
        this.vehiculo_tarjeta = vehiculo_tarjeta;
    }

    public String getCobrador_edad() {
        return cobrador_edad;
    }

    public void setCobrador_edad(String cobrador_edad) {
        this.cobrador_edad = cobrador_edad;
    }




    public Acta() {
    }

    public Acta(String acta_id, String acta_preimpreso, String acta_operador, String acta_fecha_registro, String acta_observaciones, String acta_tipo_servicio, String acta_estado, String vehiculo_placa, String vehiculo_ws, String conductor_tipo_documento, String conductor_dni, String conductor_nombres, String conductor_apaterno, String conductor_amaterno, String conductor_licencia, String conductor_licencia_clase, String conductor_licencia_categoria, String conductor_direccion, String conductor_ubigeo, String infraccion_tipo, String infraccion_via, String empresa_ruc, String empresa_rsocial, String cobrador_dni, String cobrador_nombres, String cobrador_apaterno, String cobrador_amaterno, String cobrador_direccion, String cobrador_tipo_documento) {
        this.acta_id = acta_id;
        this.acta_preimpreso = acta_preimpreso;
        this.acta_operador = acta_operador;
        this.acta_fecha_registro = acta_fecha_registro;
        this.acta_observaciones = acta_observaciones;
        this.acta_tipo_servicio = acta_tipo_servicio;
        this.acta_estado = acta_estado;
        this.vehiculo_placa = vehiculo_placa;
        this.vehiculo_ws = vehiculo_ws;
        this.conductor_tipo_documento = conductor_tipo_documento;
        this.conductor_dni = conductor_dni;
        this.conductor_nombres = conductor_nombres;
        this.conductor_apaterno = conductor_apaterno;
        this.conductor_amaterno = conductor_amaterno;
        this.conductor_licencia = conductor_licencia;
        this.conductor_licencia_clase = conductor_licencia_clase;
        this.conductor_licencia_categoria = conductor_licencia_categoria;
        this.conductor_direccion = conductor_direccion;
        this.conductor_ubigeo = conductor_ubigeo;
        this.infraccion_tipo = infraccion_tipo;
        this.infraccion_via = infraccion_via;
        this.empresa_ruc = empresa_ruc;
        this.empresa_rsocial = empresa_rsocial;
        this.cobrador_dni = cobrador_dni;
        this.cobrador_nombres = cobrador_nombres;
        this.cobrador_apaterno = cobrador_apaterno;
        this.cobrador_amaterno = cobrador_amaterno;
        this.cobrador_direccion = cobrador_direccion;
        this.cobrador_tipo_documento = cobrador_tipo_documento;
    }

    private String infraccion_denominacion;
    private String conductor_nombre_completo;

    private String fecha_label;

    public String getFecha_label() {
        return fecha_label;
    }

    public void setFecha_label(String fecha_label) {
        this.fecha_label = fecha_label;
    }
// Getter Methods


    public Acta(String acta_id, String acta_preimpreso, String acta_operador, String acta_fecha_registro, String vehiculo_placa, String acta_estado, String infraccion_tipo, String conductor_nombre_completo, String infraccion_denominacion, int acta_educativa, String fecha_label) {
        this.acta_id = acta_id;
        this.acta_preimpreso = acta_preimpreso;
        this.acta_operador = acta_operador;
        this.acta_fecha_registro = acta_fecha_registro;
        this.vehiculo_placa = vehiculo_placa;
        this.acta_estado = acta_estado;
        this.infraccion_tipo = infraccion_tipo;
        this.conductor_nombre_completo = conductor_nombre_completo;
        this.infraccion_denominacion = infraccion_denominacion;
        this.acta_educativa = acta_educativa;
        this.fecha_label = fecha_label;
    }

    public String getActa_observaciones() {
        return acta_observaciones;
    }

    public void setActa_observaciones(String acta_observaciones) {
        this.acta_observaciones = acta_observaciones;
    }

    public String getActa_tipo_servicio() {
        return acta_tipo_servicio;
    }

    public void setActa_tipo_servicio(String acta_tipo_servicio) {
        this.acta_tipo_servicio = acta_tipo_servicio;
    }

    public String getVehiculo_ws() {
        return vehiculo_ws;
    }

    public void setVehiculo_ws(String vehiculo_ws) {
        this.vehiculo_ws = vehiculo_ws;
    }

    public String getConductor_tipo_documento() {
        return conductor_tipo_documento;
    }

    public void setConductor_tipo_documento(String conductor_tipo_documento) {
        this.conductor_tipo_documento = conductor_tipo_documento;
    }

    public String getConductor_dni() {
        return conductor_dni;
    }

    public void setConductor_dni(String conductor_dni) {
        this.conductor_dni = conductor_dni;
    }

    public String getConductor_nombres() {
        return conductor_nombres;
    }

    public void setConductor_nombres(String conductor_nombres) {
        this.conductor_nombres = conductor_nombres;
    }

    public String getConductor_apaterno() {
        return conductor_apaterno;
    }

    public void setConductor_apaterno(String conductor_apaterno) {
        this.conductor_apaterno = conductor_apaterno;
    }

    public String getConductor_amaterno() {
        return conductor_amaterno;
    }

    public void setConductor_amaterno(String conductor_amaterno) {
        this.conductor_amaterno = conductor_amaterno;
    }

    public String getConductor_licencia() {
        return conductor_licencia;
    }

    public void setConductor_licencia(String conductor_licencia) {
        this.conductor_licencia = conductor_licencia;
    }

    public String getConductor_licencia_clase() {
        return conductor_licencia_clase;
    }

    public void setConductor_licencia_clase(String conductor_licencia_clase) {
        this.conductor_licencia_clase = conductor_licencia_clase;
    }

    public String getConductor_licencia_categoria() {
        return conductor_licencia_categoria;
    }

    public void setConductor_licencia_categoria(String conductor_licencia_categoria) {
        this.conductor_licencia_categoria = conductor_licencia_categoria;
    }

    public String getConductor_direccion() {
        return conductor_direccion;
    }

    public void setConductor_direccion(String conductor_direccion) {
        this.conductor_direccion = conductor_direccion;
    }

    public String getConductor_ubigeo() {
        return conductor_ubigeo;
    }

    public void setConductor_ubigeo(String conductor_ubigeo) {
        this.conductor_ubigeo = conductor_ubigeo;
    }

    public String getInfraccion_via() {
        return infraccion_via;
    }

    public void setInfraccion_via(String infraccion_via) {
        this.infraccion_via = infraccion_via;
    }

    public String getEmpresa_ruc() {
        return empresa_ruc;
    }

    public void setEmpresa_ruc(String empresa_ruc) {
        this.empresa_ruc = empresa_ruc;
    }

    public String getEmpresa_rsocial() {
        return empresa_rsocial;
    }

    public void setEmpresa_rsocial(String empresa_rsocial) {
        this.empresa_rsocial = empresa_rsocial;
    }

    public String getCobrador_dni() {
        return cobrador_dni;
    }

    public void setCobrador_dni(String cobrador_dni) {
        this.cobrador_dni = cobrador_dni;
    }

    public String getCobrador_nombres() {
        return cobrador_nombres;
    }

    public void setCobrador_nombres(String cobrador_nombres) {
        this.cobrador_nombres = cobrador_nombres;
    }

    public String getCobrador_apaterno() {
        return cobrador_apaterno;
    }

    public void setCobrador_apaterno(String cobrador_apaterno) {
        this.cobrador_apaterno = cobrador_apaterno;
    }

    public String getCobrador_amaterno() {
        return cobrador_amaterno;
    }

    public void setCobrador_amaterno(String cobrador_amaterno) {
        this.cobrador_amaterno = cobrador_amaterno;
    }

    public String getCobrador_direccion() {
        return cobrador_direccion;
    }

    public void setCobrador_direccion(String cobrador_direccion) {
        this.cobrador_direccion = cobrador_direccion;
    }

    public String getCobrador_tipo_documento() {
        return cobrador_tipo_documento;
    }

    public void setCobrador_tipo_documento(String cobrador_tipo_documento) {
        this.cobrador_tipo_documento = cobrador_tipo_documento;
    }

    public String getActa_id() {
        return acta_id;
    }

    public void setActa_id(String acta_id) {
        this.acta_id = acta_id;
    }

    public String getActa_preimpreso() {
        return acta_preimpreso;
    }

    public void setActa_preimpreso(String acta_preimpreso) {
        this.acta_preimpreso = acta_preimpreso;
    }

    public String getActa_operador() {
        return acta_operador;
    }

    public void setActa_operador(String acta_operador) {
        this.acta_operador = acta_operador;
    }

    public String getActa_fecha_registro() {
        return acta_fecha_registro;
    }

    public void setActa_fecha_registro(String acta_fecha_registro) {
        this.acta_fecha_registro = acta_fecha_registro;
    }

    public String getVehiculo_placa() {
        return vehiculo_placa;
    }

    public void setVehiculo_placa(String vehiculo_placa) {
        this.vehiculo_placa = vehiculo_placa;
    }

    public String getActa_estado() {
        return acta_estado;
    }

    public void setActa_estado(String acta_estado) {
        this.acta_estado = acta_estado;
    }

    public String getInfraccion_tipo() {
        return infraccion_tipo;
    }

    public void setInfraccion_tipo(String infraccion_tipo) {
        this.infraccion_tipo = infraccion_tipo;
    }

    public String getConductor_nombre_completo() {
        return conductor_nombre_completo;
    }

    public void setConductor_nombre_completo(String conductor_nombre_completo) {
        this.conductor_nombre_completo = conductor_nombre_completo;
    }

    public String getInfraccion_denominacion() {
        return infraccion_denominacion;
    }

    public void setInfraccion_denominacion(String infraccion_denominacion) {
        this.infraccion_denominacion = infraccion_denominacion;
    }
}