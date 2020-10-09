package com.fiscapp.fiscapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.fiscapp.fiscapp.Model.Acta;



public class Detalle_Acta extends AppCompatActivity {

    TextView acta_id,acta_preimpreso,
            acta_operador,
            acta_fecha_registro,
            acta_observaciones,
            acta_tipo_servicio,
            acta_estado,
            vehiculo_placa,
            vehiculo_ws,
            conductor_tipo_documento,
            conductor_dni,
            conductor_nombres,
            conductor_apaterno,
            conductor_amaterno,
            conductor_licencia,
            conductor_licencia_clase,
            conductor_licencia_categoria,
            conductor_direccion,
            conductor_ubigeo,
            infraccion_tipo,
            infraccion_via,
            empresa_ruc,
            empresa_rsocial,
            cobrador_dni,
            cobrador_nombres,
            cobrador_apaterno,
            cobrador_amaterno,
            cobrador_direccion,
            cobrador_tipo_documento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle__acta);
        Acta acta = (Acta) getIntent().getSerializableExtra("acta");
        acta_id=(TextView)findViewById(R.id.dtl_acta_id);
        acta_preimpreso =(TextView) findViewById(R.id.dtl_acta_preimpreso);
        acta_operador = (TextView) findViewById(R.id.dtl_acta_operador);
        acta_fecha_registro = (TextView) findViewById(R.id.dtl_fecha_registro);
        acta_observaciones = (TextView) findViewById(R.id.dtl_acta_observaciones);
        acta_tipo_servicio =(TextView) findViewById(R.id.dtl_tipo_acta);
        acta_estado = (TextView) findViewById(R.id.dtl_estado_acta);
        vehiculo_placa = (TextView) findViewById(R.id.dtl_placa);
        vehiculo_ws = (TextView) findViewById(R.id.dtl_ws_vehiculo);
        conductor_tipo_documento =(TextView) findViewById(R.id.dtl_tipo_doc_conductor);
        conductor_dni = (TextView) findViewById(R.id.dtl_dni_conductor);
        conductor_nombres = (TextView) findViewById(R.id.dtl_nombres_conductor);
        conductor_apaterno = (TextView) findViewById(R.id.dtl_conductor_apaterno);
        conductor_amaterno =(TextView) findViewById(R.id.dtl_conductor_amaterno);
        conductor_licencia = (TextView) findViewById(R.id.dtl_licencia);
        conductor_licencia_clase = (TextView) findViewById(R.id.dtl_clase_licencia);
        conductor_licencia_categoria = (TextView) findViewById(R.id.dtl_categoria_licencia);
        conductor_direccion =(TextView) findViewById(R.id.dtl_direccion_conductor);
        conductor_ubigeo = (TextView) findViewById(R.id.dtl_ubigeo_conductor);
        infraccion_tipo = (TextView) findViewById(R.id.dtl_tipo_infraccion);
        infraccion_via = (TextView) findViewById(R.id.dtl_via_infraccion);
        empresa_ruc =(TextView) findViewById(R.id.dtl_ruc_empresa);
        empresa_rsocial = (TextView) findViewById(R.id.dtl_rsocial);
        cobrador_dni = (TextView) findViewById(R.id.dtl_dni_cobrador);
        cobrador_nombres = (TextView) findViewById(R.id.dtl_nombres_cobrador);
        cobrador_apaterno =(TextView) findViewById(R.id.dtl_apaterno_cobrador);
        cobrador_amaterno = (TextView) findViewById(R.id.dtl_amaterno_cobrador);
        cobrador_direccion = (TextView) findViewById(R.id.dtl_direccion_cobrador);
        cobrador_tipo_documento = (TextView) findViewById(R.id.dtl_tipo_doc_cobrador);

        acta_id.setText(acta.getActa_preimpreso());
        infraccion_tipo.setText(acta.getInfraccion_tipo());
        vehiculo_placa.setText(acta.getVehiculo_placa());
        infraccion_via.setText(acta.getInfraccion_tipo());
        acta_preimpreso.setText(acta.getActa_preimpreso());
        acta_operador.setText(acta.getActa_operador());
        acta_fecha_registro.setText(acta.getActa_fecha_registro());
        acta_observaciones.setText(acta.getActa_observaciones());
        acta_tipo_servicio.setText(acta.getActa_tipo_servicio());
        acta_estado.setText(acta.getActa_estado());
        vehiculo_ws.setText(acta.getVehiculo_ws());
        conductor_tipo_documento.setText(acta.getCobrador_tipo_documento());
        conductor_dni.setText(acta.getConductor_dni());
        conductor_nombres.setText(acta.getConductor_nombres());
        conductor_apaterno.setText(acta.getCobrador_apaterno());
        conductor_amaterno.setText(acta.getConductor_amaterno());
        conductor_licencia.setText(acta.getConductor_licencia());
        conductor_licencia_clase.setText(acta.getConductor_licencia_clase());
        conductor_licencia_categoria.setText(acta.getConductor_licencia_categoria());
        conductor_direccion.setText(acta.getConductor_direccion());
        conductor_ubigeo.setText(acta.getConductor_ubigeo());
        empresa_ruc.setText(acta.getEmpresa_ruc());
        empresa_rsocial.setText(acta.getEmpresa_rsocial());
        cobrador_tipo_documento.setText(acta.getCobrador_tipo_documento());
        cobrador_dni.setText(acta.getCobrador_dni());
        cobrador_nombres.setText(acta.getCobrador_nombres());
        cobrador_apaterno.setText(acta.getCobrador_apaterno());
        cobrador_amaterno.setText(acta.getCobrador_amaterno());
        cobrador_direccion.setText(acta.getCobrador_direccion());


    }
}
