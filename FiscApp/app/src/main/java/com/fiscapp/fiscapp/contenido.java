package com.fiscapp.fiscapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.fiscapp.fiscapp.Model.ContenidoAdapter;
import com.fiscapp.fiscapp.Model.cContenido;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class contenido extends AppCompatActivity {

    final Context context = this;

    private ProgressDialog progressDialog;


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<cContenido> contenidoArray;

    ContenidoAdapter adapter;

    EditText etBusqueda;
    Button btn_ver, back_contenido;

    String ROOT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contenido);

        //getSupportActionBar().hide();

        String RootResp = SharedPrefManager.getInstance(this).getDominio();

        try {
            JSONArray jsonArray =  new JSONArray(RootResp);
            JSONObject obj = jsonArray.getJSONObject(0);
            ROOT = obj.getString("dominio") + "?var=";
        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialog = new ProgressDialog(this);
        recyclerView = findViewById(R.id.recycler_view_contents);
        btn_ver = findViewById(R.id.btn_ver);
        back_contenido = findViewById(R.id.back_cont);
        back_contenido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recuperar_contenido_categoria();

        Spinner spinner = findViewById(R.id.spnn2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                //Toast.makeText(context, ">" + selectedItemText, Toast.LENGTH_SHORT).show();
                getListaContenidos(selectedItemText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void recuperar_contenido_categoria(){
        final RequestQueue queue = Volley.newRequestQueue(this);

        progressDialog.setMessage("Cargando..");
        progressDialog.show();

        String h = ROOT + "listar_contenido_interes&export=true";
        StringRequest stringRequest = new StringRequest(h,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            ArrayList<String> SPINNERLIST2 = new ArrayList();

                            JSONArray arr = new JSONArray(response);

                            for(int i = 0; i < arr.length(); i++) {

                                JSONObject currentObj = arr.getJSONObject(i);
                                String contenido_categoria = currentObj.getString("Contenido_Categoria");

                                if(!SPINNERLIST2.contains(contenido_categoria))
                                    SPINNERLIST2.add(contenido_categoria);

                            }

                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, SPINNERLIST2);
                            Spinner spinner =  findViewById(R.id.spnn2);
                            spinner.setAdapter(arrayAdapter);

                            progressDialog.dismiss();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Sin conexion a internet.",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        queue.add(stringRequest);
    }

    public  void getListaContenidos(final String pcontenido_categoria) {
        final RequestQueue queue = Volley.newRequestQueue(this);

        contenidoArray = new ArrayList<>();
        String h = ROOT + "listar_contenido_interes&export=true";
        StringRequest stringRequest = new StringRequest(h,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray arr = new JSONArray(response);

                            for(int i = 0; i < arr.length(); i++) {

                                JSONObject currentObj = arr.getJSONObject(i);
                                String contenido_id = currentObj.getString("Contenido_Id");
                                String contenido_titulo = currentObj.getString("Contenido_Titulo");
                                String contenido_texto = currentObj.getString("Contenido_Texto");
                                String contenido_adjunto = currentObj.getString("Contenido_Adjunto");
                                String contenido_idCategoria = currentObj.getString("Contenido_idCategoria");
                                String contenido_categoria = currentObj.getString("Contenido_Categoria");

                                if (contenido_categoria.equals(pcontenido_categoria)){
                                    cContenido act = new cContenido(contenido_id,contenido_titulo,contenido_texto,contenido_adjunto,contenido_idCategoria,contenido_categoria);
                                    contenidoArray.add(act);
                                }
                            }

                            recyclerView = null;
                            recyclerView = findViewById(R.id.recycler_view_contents);
                            layoutManager = new LinearLayoutManager(context);
                            recyclerView.setLayoutManager(layoutManager);
                            adapter = new ContenidoAdapter(context, contenidoArray);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Sin conexion a internet.",Toast.LENGTH_SHORT).show();
                /*if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Sin conexion a internet.",Toast.LENGTH_SHORT).show();
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
                }*/

            }
        });

        queue.add(stringRequest);
    }
}
