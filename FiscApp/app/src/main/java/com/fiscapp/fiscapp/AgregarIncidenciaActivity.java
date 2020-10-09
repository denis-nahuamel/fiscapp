package com.fiscapp.fiscapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fiscapp.fiscapp.Model.Incidencia;
import com.fiscapp.fiscapp.Model.TipoIncidencia;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.fiscapp.fiscapp.Constants.URL_GUARDAR_INCIDENCIA;
import static com.fiscapp.fiscapp.Constants.URL_LISTAR_TIPO_INCIDENCIA;


public class AgregarIncidenciaActivity extends AppCompatActivity implements
        View.OnClickListener,AdapterView.OnItemSelectedListener {
    //region============atributos===============
    Button aceptar,cancelar,mapaAct;
    private Spinner spinner;
    TextView tvdescripcion,tvincidencia,tvTitulo;
    Location mCurrentLocation;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    String latitud="null",longitud="null";
    LocationRequest mLocationRequest;
    String fechaActual;
    String value1;
    Context context;
    TipoIncidencia[] tipoIncidencias ;
    ArrayList<String> spinnerArray;
    ArrayList<String> Values;
    private int mUsuarioID;

    ImageView atras;

    String mOperador;

    private final int PETICION_ACTIVITY_SEGUNDA = 1;

    String ROOT;

    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_incidencia);
        spinnerArray=new ArrayList<String>();
        value1="";

        String RootResp = SharedPrefManager.getInstance(this).getDominio();

        try {
            JSONArray jsonArray =  new JSONArray(RootResp);
            JSONObject obj = jsonArray.getJSONObject(0);
            ROOT = obj.getString("dominio") + "?var=";
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListarTipoIncidencia();

        atras = (ImageView) findViewById(R.id.atras);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvdescripcion = (TextView) findViewById(R.id.txtAgregarDescripcion);
        tvTitulo = (TextView) findViewById(R.id.txtTitulo);
        tvincidencia = (TextView) findViewById(R.id.txtTipoIncidencia);
        mapaAct=(Button)findViewById(R.id.btnIrMapa);
        context=this;
        mapaAct.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgregarIncidenciaActivity.this, IncidenciaMapActivity.class);
                startActivityForResult(intent,PETICION_ACTIVITY_SEGUNDA);
                // startActivity(new Intent(getApplicationContext(), IncidenciaMapActivity.class));
            }
        });
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
        Date todayDate = new Date();
        fechaActual = currentDate.format(todayDate);



        //region============Sin uso==================
        if(getIntent().getSerializableExtra("incidencia")!=null)
        {
            Incidencia incidencia= (Incidencia) getIntent().getSerializableExtra("incidencia");
            Log.e("incidenciaa",incidencia.getIncidencia());
            tvdescripcion.setText(incidencia.getDescripcion());
            tvincidencia.setText(incidencia.getIncidencia());
            tvincidencia.setEnabled(false);
            tvdescripcion.setEnabled(false);
        }

        mUsuarioID = SharedPrefManager.getInstance(getApplicationContext()).getUserKeyId();
        mOperador = SharedPrefManager.getInstance(getApplicationContext()).getUserKeyUsuario();
        //endregion==============================================

        aceptar = (Button) findViewById(R.id.btnAgregarIncidencia);
        cancelar = (Button) findViewById(R.id.btnCancelarIncidencia);
        //region===============enviar incidencia====================
        aceptar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                try {


                    String lat=latitud;
                    String lon=longitud;
                    String operador=mOperador;
                    String fecha=fechaActual;
                    String detalle=tvdescripcion.getText().toString();
                    String titulo=tvTitulo.getText().toString();
                    String value=value1;
                    Log.e("saveee",titulo+"+"+value);

                    tvdescripcion = (TextView) findViewById(R.id.txtAgregarDescripcion);
                    tvTitulo = (TextView) findViewById(R.id.txtTitulo);
                    tvincidencia = (TextView) findViewById(R.id.txtTipoIncidencia);

                    if(titulo.length()==0 || detalle.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Complete los campos vacios", Toast.LENGTH_SHORT).show();
                    } else
                    saveIncidencia(titulo,lat,lon,detalle,operador,fecha,value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //endregion
        cancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }


    //region=============spinner===============
    public void addListenerOnSpinnerItemSelection() {
        try{
            spinner = (Spinner) findViewById(R.id.spinner);

            final int listsize = spinnerArray.size() - 1;
            ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(this, R.layout.spinner, spinnerArray);

            spinner.setAdapter(userAdapter);

            spinner.setOnItemSelectedListener(this);
        }
        catch (Exception ex)
        {
            Log.e("error",ex+"");
        }

    }
    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {

        tvincidencia.setText( parent.getItemAtPosition(pos).toString());
        value1=Values.get(pos).toString();
    }
    //endregion
    //region=====================sin uso===================
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    @Override
    public void onClick(View v) {

    }
    //endregion

    boolean guardarIncidenciaTocado = false;


    //region====================guardar incidencia
    public void saveIncidencia(final String titulo, final String latitud, final String longitud, final String descripcion,
                               final String operador,final String fecha,final String value) {

        if(guardarIncidenciaTocado) return;
        guardarIncidenciaTocado = true;
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Guardando incidencia...");
        pd.show();
        pd.setCancelable(false);


        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                ROOT+URL_GUARDAR_INCIDENCIA,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pd.dismiss();

                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(context);
                        }
                        builder.setTitle("Agregar Incidencia")
                                .setMessage("Se guardó la incidencia")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                        guardarIncidenciaTocado = false;
                                        finish();
                                    }
                                })



                                .setIcon(android.R.drawable.checkbox_on_background)
                                .show()
                                .setCancelable(false);
                        try {

                            AlertDialog.Builder builder2 = new AlertDialog.Builder(AgregarIncidenciaActivity.this);

                            builder2.setTitle("Agregar Incidencia")
                                    .setMessage("Se guardó la inciencia.")
                                    .setCancelable(false)
                                    .setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    guardarIncidenciaTocado = false;
                                                    finish();
                                                }
                                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("eror",e+"");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // progressDialog.dismiss();
                        pd.dismiss();
                        guardarIncidenciaTocado = false;
                        Toast.makeText(getApplicationContext(),"Sin conexion a internet",Toast.LENGTH_SHORT).show();
                       /* if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(),"Upps algo anda mal",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(),"AuthFailureError",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getApplicationContext(),"ServerError",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(),"NetworkError",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getApplicationContext(),"ParseError",Toast.LENGTH_SHORT).show();
                        }*/

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("titulo", titulo);
                params.put("latitud",latitud);
                params.put("longitud",longitud);
                params.put("descripcion",descripcion);
                params.put("operador",operador);
                params.put("fecha",fecha);
                params.put("tipo",value);
                params.put("estado","1");

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }
    //endregion
    int request_code = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Comprobar de qué petición se está obteniendo el resultado (requestCode)
        if (requestCode == PETICION_ACTIVITY_SEGUNDA) {
            //Comprobar el tipo de resultado (resultCode)
            if (resultCode == Activity.RESULT_OK) {
                //Operaciones a realizar si se ha pulsado un botón "Aceptar

                latitud=data.getStringExtra(IncidenciaMapActivity.LATITUD_CAPTURADO);
                longitud=data.getStringExtra(IncidenciaMapActivity.TEXTO_CAPTURADO);
            } else {
                //Operaciones a realizar si se ha pulsado un botón "Cancelar" o "Retroceder"
            }
        }
    }
    public void ListarTipoIncidencia() {
/*        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Haciendo Magic...");
        pd.show();*/

        final ArrayList<String> spinnerArray2 = new ArrayList<String>();
        final ArrayList<String> values = new ArrayList<String>();
        //String URL_SAVEINCIDENCIA = Constants.BASE_URL + "?var=listar_tipo_incidencia";
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                ROOT+URL_LISTAR_TIPO_INCIDENCIA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  pd.dismiss();
                        Log.e("primera",response);
                        try {
                            JSONArray arrayInc = new JSONArray(response);
                            final TipoIncidencia[] incidenciasArray = new TipoIncidencia[arrayInc.length()];
                            for(int i=0;i<arrayInc.length();i++){
                                // Get current json object
                                JSONObject incidencia = arrayInc.getJSONObject(i);
                                String value = incidencia.getString("value");
                                String option=incidencia.getString("option");

                                TipoIncidencia act = new TipoIncidencia(value,option);
                                spinnerArray2.add(act.getOption());
                                values.add(act.getValue());
                                //  incidenciasArray[i]=act;

                            }
                            //spinnerArray2.add("Seleccionar Incidencia");
                            Log.e("incidenciaaas",spinnerArray2.size()+"");
                            spinnerArray= spinnerArray2;
                            Values=values;
                            Log.e("inciaaas",spinnerArray.size()+"");

                            addListenerOnSpinnerItemSelection();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("catcha",e+"");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Sin conexion a internet",Toast.LENGTH_SHORT).show();

                    }
                }
        ){

        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }
}



