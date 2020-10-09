package com.fiscapp.fiscapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.fiscapp.fiscapp.Model.PersonaAdapter;
import com.fiscapp.fiscapp.Model.cController;
import com.fiscapp.fiscapp.Model.cPersona;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class verificacion_ayudante extends AppCompatActivity {

    private ProgressDialog progressDialog;

    final Context context = this;

    List<cPersona> list_personas;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    PersonaAdapter adapter;
    EditText eBuscar;
    Button btnSearch, back_per_cap;
    cController c = new cController(context);
    Dialog myDialog;
    Dialog myDialog_ERROR;

    TextView datos;

    String ROOT = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificacion_ayudante);
        //getSupportActionBar().hide();

        String RootResp = SharedPrefManager.getInstance(this).getDominio();

        try {
            JSONArray jsonArray =  new JSONArray(RootResp);
            JSONObject obj = jsonArray.getJSONObject(0);
            ROOT = obj.getString("dominio") + "?var=";
        } catch (JSONException e) {
            e.printStackTrace();
        }

        eBuscar = findViewById(R.id.etBusqueda);
        btnSearch = findViewById(R.id.btn_search);
        btnSearch.setGravity(R.id.center);
        progressDialog = new ProgressDialog(this);

        back_per_cap = findViewById(R.id.back_pc);
        back_per_cap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager conn = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = conn.getActiveNetworkInfo();

                if(networkInfo != null && networkInfo.isConnected()){

                    modal_(R.style.modalslide);
                    myDialog.show();
                }
                else{

                    modal_error_internet(R.style.modalslide);
                    myDialog_ERROR.show();
                }

            }
        });
        eBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                getListarPersonas(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void mostrarCapa(){


    }

    public void ocultarCapa(){

    }

    private void modal_(int type){
        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.modal2);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCancelable(true);
        Button ok = myDialog.findViewById(R.id.si_);
        Button okno = myDialog.findViewById(R.id.no_);
        datos = myDialog.findViewById(R.id.txt_import);
        datos.setText("\nImportar datos?\n");
        datos.setTextSize(16);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                Load();
            }
        });

        okno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        //myDialog.getWindow().getAttributes().windowAnimations = type;

    }

    private void modal_error_internet(int type){
        Display display = getWindowManager().getDefaultDisplay();
        int ancho = display.getWidth();
        int  alto= display.getHeight();

        myDialog_ERROR = new Dialog(context);
        myDialog_ERROR.setContentView(R.layout.modal2);
        myDialog_ERROR.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog_ERROR.setCancelable(true);

        Button ok = myDialog_ERROR.findViewById(R.id.si_);
        Button okno = myDialog_ERROR.findViewById(R.id.no_);
        TextView titulo = myDialog_ERROR.findViewById(R.id.titulo_modal);
        TextView datos1 = myDialog_ERROR.findViewById(R.id.txt_import);
        datos1.setText("\nNo hay conexión a internet!\n");
        datos1.setGravity(Gravity.CENTER);
        datos1.setTextSize(16);
        //datos1.setMaxHeight(alto-50);
        //datos1.setWidth(ancho-150);
        ok.setText("OK");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog_ERROR.dismiss();
            }
        });

        okno.setVisibility(View.GONE);
        titulo.setText("  SIN CONEXION");
        titulo.setGravity(Gravity.CENTER);
        titulo.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(myDialog_ERROR.getContext(), R.drawable.sin_internet), null, null, null);
        myDialog_ERROR.getWindow().getAttributes().windowAnimations = type;

    }



    public  void Load() {
        final RequestQueue queue = Volley.newRequestQueue(this);

        progressDialog.setMessage("Cargando..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String h = ROOT+"recuperar_persona_capacitada";
        StringRequest stringRequest = new StringRequest(h,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            c.delete();
                            JSONArray arr = new JSONArray(response);
                            int cnt  =0;
                            for(int i = 0; i < arr.length(); i++) {

                                JSONObject currentObj = arr.getJSONObject(i);
                                String persona_nombres = currentObj.getString("persona_nombres");
                                String persona_dni = currentObj.getString("persona_dni");
                                String persona_tema = currentObj.getString("persona_tema");
                                String persona_informacion = currentObj.getString("persona_informacion");
                                String persona_foto = currentObj.getString("persona_foto");
                                String observaciones = currentObj.getString("observaciones");
                                String fecha = currentObj.getString("fecha");
                                String vigencia = currentObj.getString("vigencia");
                                String tipo = currentObj.getString("tipo");

                                cPersona persona = new cPersona(persona_nombres,persona_dni,persona_tema,persona_informacion,persona_foto,observaciones, fecha, vigencia, tipo);

                                boolean createSuccessful = new cController(context).create(persona);
                                if(createSuccessful) cnt++;

                            }

                            Toast.makeText(context, "Total datos importados : " + cnt, Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progressDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Upps algo anda mal",Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"AuthFailureError",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"ServerError",Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"NetworkError",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"ParseError",Toast.LENGTH_SHORT).show();
                }
            }
        });

        queue.add(stringRequest);

    }

    public  void getListarPersonas(String data) {

        list_personas = new cController(context).read(data);

        recyclerView = null;
        recyclerView = findViewById(R.id.recycler_view_contents);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PersonaAdapter(context, list_personas);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}
