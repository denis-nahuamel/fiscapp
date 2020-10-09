package com.fiscapp.fiscapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.fiscapp.fiscapp.Model.Placa;



public class PlacaResultados extends AppCompatActivity {

    TextView tvPlaca,tvReplacado,tvAutorizacion,tvEstadoAutorizacion,
    tvFechaDesde,tvFechaHasta,tvFechaInscripcion,tvResolucion,tvNroCertificado,
    tvNroExpediente,tvFechaExpediente,tvPropietario,tvDireccionPropietario,tvNombresConductor,
    tvLicencia,tvEmpresa,tvCarroceria,tvAnioFabricacion,tvClase,tvMarca,
    tvColor,tvNroMotor,tvSerie, lblDireccionPropietario;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placa_resultados);
        context=this;
        lblDireccionPropietario=(TextView)findViewById(R.id.lblDireccionPropietario);
        //region==========instanciar datos===================
        tvPlaca = (TextView) findViewById(R.id.txtPlaca);
        tvReplacado = (TextView) findViewById(R.id.txtReplacado);
        tvAutorizacion = (TextView) findViewById(R.id.txtAutorizacion);
        tvEstadoAutorizacion = (TextView) findViewById(R.id.txtEstadoAutorizacion);
        tvFechaDesde = (TextView) findViewById(R.id.txtFechaDesde);
        tvFechaHasta = (TextView) findViewById(R.id.txtFechaHasta);
        tvFechaInscripcion = (TextView) findViewById(R.id.txtFechaInscripcion);
        tvResolucion = (TextView) findViewById(R.id.txtResolucion);
        tvNroCertificado = (TextView) findViewById(R.id.txtNroCertificado);
        tvNroExpediente = (TextView) findViewById(R.id.txtNroExpediente);
        tvFechaExpediente = (TextView) findViewById(R.id.txtFechaExpediente);
        tvPropietario = (TextView) findViewById(R.id.txtPropietario);
        tvDireccionPropietario = (TextView) findViewById(R.id.txtDireccionPropietario);
        tvNombresConductor = (TextView) findViewById(R.id.txtNombreConductor);
        tvLicencia = (TextView) findViewById(R.id.txtLicencia);
        tvEmpresa = (TextView) findViewById(R.id.txtEmpresa);
        tvCarroceria = (TextView) findViewById(R.id.txtCarroceria);
        tvAnioFabricacion = (TextView) findViewById(R.id.txtAñoFabricacion);
        tvClase = (TextView) findViewById(R.id.txtClase);
        tvMarca = (TextView) findViewById(R.id.txtMarcaVehiculo);
        tvColor = (TextView) findViewById(R.id.txtColor);
        tvNroMotor = (TextView) findViewById(R.id.txtNroMotor);
        tvSerie = (TextView) findViewById(R.id.txtSerie);
        //endregion
       //===========asignar datos del vehiculo==================
        Placa vehiculo= (Placa) getIntent().getSerializableExtra("placa");
        tvPlaca.setText(vehiculo.getPlaca());
        tvReplacado.setText(vehiculo.getReplacado());
        tvAutorizacion.setText(vehiculo.getAutorizacion());
        tvEstadoAutorizacion.setText(vehiculo.getEstadoAutorizacion());
        tvFechaDesde.setText(vehiculo.getFechaDesde());
        tvFechaHasta.setText( vehiculo.getFechaHasta());
        tvFechaInscripcion.setText(vehiculo.getFechaInscripcion());
        tvResolucion.setText(vehiculo.getResolucion());
        tvNroCertificado.setText( vehiculo.getNumeroCertificado());
        tvNroExpediente.setText( vehiculo.getNumeroExpediente());
        tvFechaExpediente.setText( vehiculo.getFechaExpediente());
        tvPropietario.setText( vehiculo.getPropietario());
        tvDireccionPropietario.setText( vehiculo.getDireccionPropietario());
        tvNombresConductor.setText(vehiculo.getNombresConductor());
        tvLicencia.setText(vehiculo.getLicencia());
        tvEmpresa.setText(vehiculo.getEmpresa());
        tvCarroceria.setText(vehiculo.getCarroceria());
        tvAnioFabricacion.setText( vehiculo.getAnioFabricacion());
        tvClase.setText( vehiculo.getClase());
        tvMarca.setText( vehiculo.getMarcaVehiculo());
        tvColor.setText(vehiculo.getColor());
        tvNroMotor.setText( vehiculo.getNro_motor());
        tvSerie.setText( vehiculo.getSerie());

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPlaca);
        toolbar.setTitle("Resultados");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                startActivity(new Intent(getApplicationContext(), VerificarPlacaActivity.class));

            }
        });*/
    }

}
