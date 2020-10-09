package com.fiscapp.fiscapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fiscapp.fiscapp.Helpers.Connection;
import com.fiscapp.fiscapp.Helpers.CustomListener;
import com.fiscapp.fiscapp.Model.Incidencia;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.*;
import static com.android.volley.Request.Method.*;
import static com.fiscapp.fiscapp.Constants.URL_GUARDAR_LEIDO;
import static com.fiscapp.fiscapp.Constants.URL_LISTAR_INCIDENCIAS;

public class IncidenciasActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    IncidenciaAdapter adapter;
    List<Incidencia> data_list;
    CardView incidencia;
    ImageView img_ida, img_vuelta;
    boolean ejecutar=true;
    ArrayList<Incidencia> incidencias;
    Context context;
    int direccion = 0;
    private int mUsuarioID;
    private String mOperador;

    ImageView atras;

    SwipeRefreshLayout pullToRefresh;

    String GidIncidencia,Goperador,Glatitud,Glongitud,Gtitulo,Gdescripcion;

    String ROOT;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidencias);
        context = this;
        Connection.getInstance(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_incidencias);
        data_list = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
       recyclerView.setLayoutManager(layoutManager);
    //    adapter = new IncidenciaAdapter(context, data_list);
        incidencias=new ArrayList<>();

        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //refreshData(); // your code
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fecha = sdf.format(new Date());
                getIncidencias3(mOperador+"",fecha);
                pullToRefresh.setRefreshing(false);

                //Toast.makeText(getApplicationContext(), "HELLO", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView atras = (ImageView) findViewById(R.id.atras);

        mUsuarioID = SharedPrefManager.getInstance(getApplicationContext()).getUserKeyId();

        /*int states[][] = {{android.R.attr.state_checked}, {}};
        int color_for_state_checked = Color.parseColor("#DA322B");
        int color_for_state_normal = Color.parseColor("#333333");
        int colors[] = {color_for_state_checked, color_for_state_normal};

        CheckBox cbVisto = findViewById(R.id.cbVisto);
        CompoundButtonCompat.setButtonTintList(cbVisto, new ColorStateList(states, colors));*/

        String RootResp = SharedPrefManager.getInstance(this).getDominio();

        try {
            JSONArray jsonArray =  new JSONArray(RootResp);
            JSONObject obj = jsonArray.getJSONObject(0);
            ROOT = obj.getString("dominio") + "?var=";
        } catch (JSONException e) {
            e.printStackTrace();
        }



        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = sdf.format(new Date());

        mOperador = getIntent().getStringExtra("operador");
        //Toast.makeText(getApplicationContext(), mOperador, Toast.LENGTH_SHORT).show();
        getIncidencias3(mOperador+"",fecha);

        runEvery15Sec();
        /*new Thread(new Runnable() {
            public void run() {
                try {
                    while(ejecutar){
                        getIncidencias2("6","2018-10-15");
                        Thread.sleep(30*1000);
                    }

                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();*/

        Log.e("arrayada",data_list.size()+"");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
      
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_incidencias);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AgregarIncidenciaActivity.class));

            }
        });

        //region===================ir a la incidencia=====================
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        final Incidencia e = data_list.get(position);
                       /* Intent paso = new Intent(context, IncidenciaMapActivity.class);
                        paso.putExtra("incidencia", (Serializable) e);
                        setResult(RESULT_OK, paso);
                        finish();*/




                        Glatitud = e.getLatitud();
                        Glongitud = e.getLongitud();

                        Gdescripcion= getS(e.getDescripcion());

                        e.setDescripcion(Gdescripcion);

                        Map<String, Object> jsonParams = new HashMap<>();
                        jsonParams.put("idIncidencia", e.getId());
                        jsonParams.put("operador",mOperador);


                        Connection.getInstance().PostRequestString(ROOT+URL_GUARDAR_LEIDO,
                                jsonParams, new CustomListener<String>()
                                {
                                    @Override
                                    public void getResult(String result)
                                    {
                                        if (!result.isEmpty())
                                        {

                                            Log.e("responsesave",result);
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                            String fecha = sdf.format(new Date());

                                            mOperador = getIntent().getStringExtra("operador");
                                            if(e.getLeido().equals("0")) {
                                                e.setLeido("1");
                                                getIncidencias3(mOperador+"",fecha);
                                            }
                                            //Toast.makeText(getApplicationContext(), mOperador, Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                        if (e.getLongitud().equals("0") && e.getLatitud().equals("0")) {
                            //region=============marcar como leido===================

                            //endregion saveUsuarioIncidecncia(e.getId(),"JPUENTE");

                            Connection.showOKDialog(context,"No se especificó ubicación",
                                    "Titulo: " + e.getIncidencia() + "\n" + "Descripción: " + e.getDescripcion());
                        } else {



                            //region=========preguntar si debe ir al mapa o no
                            Log.e("else",Gdescripcion);
                            AlertDialog.Builder builder;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                            } else {
                                builder = new AlertDialog.Builder(context);
                            }
                            builder.setTitle("Incidencia")
                                    .setMessage("Titulo: " + e.getIncidencia() + "\n" + "Descripción: " + Gdescripcion)
                                    //regresar al listado de incidencias
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    //ir al mapa
                                    .setNegativeButton("Ver en mapa", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // finish();
                                            e.setDescripcion(Gdescripcion);

                                            Intent paso = new Intent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                                            paso.putExtra("incidencia", (Serializable) e);
                                            setResult(RESULT_OK, paso)
                                            ;
                                            //startActivity(paso);
                                            finish();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                            //endregion

                        }



                        // do whatever
                        /*try {


                            final Incidencia e = data_list.get(position);
                            String URL_SAVEINCIDENCIA = Constants.BASE_URL + "?var=recuperar_incidencia&id="+e.getId();
                            Log.e("aca",URL_SAVEINCIDENCIA);
                            StringRequest stringRequest = new StringRequest(
                                    Request.Method.GET,
                                    URL_SAVEINCIDENCIA,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            //  pd.dismiss();
                                            Log.e("primera",response);
                                            try {

                                                JSONArray jsonarray = new JSONArray(response);
                                                JSONObject obj = jsonarray.getJSONObject(0);

                                                Glatitud = obj.getString("latitud");
                                                Glongitud = obj.getString("longitud");

                                                Gdescripcion= String.valueOf(obj.getString("descripcion"));
                                                Log.e("recuperooo",Gdescripcion);
                                                e.setDescripcion(Gdescripcion);
                                                if (e.getLongitud().equals("0") && e.getLatitud().equals("0")) {
                                                    //region=============marcar como leido===================
                                                    Map<String, Object> jsonParams = new HashMap<>();
                                                    jsonParams.put("idIncidencia", e.getId());
                                                    jsonParams.put("operador","JPUENTE");
                                                    Connection.getInstance().PostRequestString("?var=guardar_leido",
                                                            jsonParams, new CustomListener<String>()
                                                            {
                                                                @Override
                                                                public void getResult(String result)
                                                                {
                                                                    if (!result.isEmpty())
                                                                    {

                                                                        Log.e("responsesave",result);
                                                                    }
                                                                }
                                                            });
                                                    //endregion saveUsuarioIncidecncia(e.getId(),"JPUENTE");

                                                    Connection.showOKDialog(context,"No se especificó ubicación",
                                                            "Titulo: " + e.getIncidencia() + "\n" + "Descripción: " + e.getDescripcion());
                                                } else {
                                                    //region=========preguntar si debe ir al mapa o no
                                                    Log.e("else",Gdescripcion);
                                                    AlertDialog.Builder builder;
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                                                    } else {
                                                        builder = new AlertDialog.Builder(context);
                                                    }
                                                    builder.setTitle("Incidencia")
                                                            .setMessage("Titulo: " + e.getIncidencia() + "\n" + "Descripción: " + Gdescripcion)
                                                            //regresar al listado de incidencias
                                                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {

                                                                }
                                                            })
                                                            //ir al mapa
                                                            .setNegativeButton("Ver en mapa", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    // finish();
                                                                    e.setDescripcion(Gdescripcion);
                                                                    Intent paso = new Intent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                                                                    paso.putExtra("incidencia", (Serializable) e);
                                                                    setResult(RESULT_OK, paso)
                                                                    ;
                                                                    //startActivity(paso);
                                                                    finish();
                                                                }
                                                            })
                                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                                            .show();
                                                    //endregion

                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                Log.e("catcha",e+"");
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            // progressDialog.dismiss();
                                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                                Toast.makeText(getApplicationContext(),"Sin conexión a internet.",Toast.LENGTH_SHORT).show();
                                            } else if (error instanceof AuthFailureError) {
                                                Toast.makeText(getApplicationContext(),"AuthFailureError",Toast.LENGTH_SHORT).show();
                                            } else if (error instanceof ServerError) {
                                                Toast.makeText(getApplicationContext(),"Error de servidor.",Toast.LENGTH_SHORT).show();
                                            } else if (error instanceof NetworkError) {
                                                Toast.makeText(getApplicationContext(),"Error de Red.",Toast.LENGTH_SHORT).show();
                                            } else if (error instanceof ParseError) {
                                                Toast.makeText(getApplicationContext(),"Error inesperado.",Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    }
                            ){

                            };

                            //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
                            RequestQueue queue = Volley.newRequestQueue(context);

                            queue.add(stringRequest);
                            Log.e("touch", Gdescripcion);

                        }
                        catch (Exception ex)
                        {

                        }*/

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        /*Incidencia e = data_list.get(position);
                        Intent paso = new Intent(context, IncidenciaMapActivity.class);
                        paso.putExtra("incidencia", (Serializable) e);
                        setResult(RESULT_OK, paso);
                        finish();*/

                        final Incidencia e = data_list.get(position);
                       /* Intent paso = new Intent(context, IncidenciaMapActivity.class);
                        paso.putExtra("incidencia", (Serializable) e);
                        setResult(RESULT_OK, paso);
                        finish();*/




                        Glatitud = e.getLatitud();
                        Glongitud = e.getLongitud();

                        Gdescripcion= getS(e.getDescripcion());

                        e.setDescripcion(Gdescripcion);

                        Map<String, Object> jsonParams = new HashMap<>();
                        jsonParams.put("idIncidencia", e.getId());
                        jsonParams.put("operador",mOperador);


                        Connection.getInstance().PostRequestString(ROOT+URL_GUARDAR_LEIDO,
                                jsonParams, new CustomListener<String>()
                                {
                                    @Override
                                    public void getResult(String result)
                                    {
                                        if (!result.isEmpty())
                                        {

                                            Log.e("responsesave",result);
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                            String fecha = sdf.format(new Date());

                                            mOperador = getIntent().getStringExtra("operador");
                                            if(e.getLeido().equals("0")) {
                                                e.setLeido("1");
                                                getIncidencias3(mOperador+"",fecha);
                                            }
                                            //Toast.makeText(getApplicationContext(), mOperador, Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                        if (e.getLongitud().equals("0") && e.getLatitud().equals("0")) {
                            //region=============marcar como leido===================

                            //endregion saveUsuarioIncidecncia(e.getId(),"JPUENTE");

                            Connection.showOKDialog(context,"No se especificó ubicación",
                                    "Titulo: " + e.getIncidencia() + "\n" + "Descripción: " + e.getDescripcion());
                        } else {



                            //region=========preguntar si debe ir al mapa o no
                            Log.e("else",Gdescripcion);
                            AlertDialog.Builder builder;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                            } else {
                                builder = new AlertDialog.Builder(context);
                            }
                            builder.setTitle("Incidencia")
                                    .setMessage("Titulo: " + e.getIncidencia() + "\n" + "Descripción: " + Gdescripcion)
                                    //regresar al listado de incidencias
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    //ir al mapa
                                    .setNegativeButton("Ver en mapa", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // finish();
                                            e.setDescripcion(Gdescripcion);

                                            Intent paso = new Intent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                                            paso.putExtra("incidencia", (Serializable) e);
                                            setResult(RESULT_OK, paso)
                                            ;
                                            //startActivity(paso);
                                            finish();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                            //endregion

                        }
                    }
                })
        );
        //endregion

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    boolean isRunningEvery15secs = false;

    private void runEvery15Sec() {
        if (!isRunningEvery15secs) {
            (new Thread(new Runnable() {

                @Override
                public void run() {
                    while (!Thread.interrupted())
                        try {
                            Thread.sleep(15*1000);
                            runOnUiThread(new Runnable() // start actions in UI thread
                            {

                                @Override
                                public void run() {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String fecha = sdf.format(new Date());

                                    getIncidencias3(mOperador+"",fecha);
                                }
                            });
                        } catch (InterruptedException e) {
                            // ooops
                        }
                }
            })).start(); // the while thread will start in BG thread
        }

    }

    public void removeItem(int position){
        data_list.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, data_list.size());
    }
    
    public boolean match(String s, String ele) {
        s = s.toLowerCase();
        ele = ele.toLowerCase();
        //"digital".matches(".*ital.*");
        if(s.matches(".*"+ele+".*")) {
            return true;
        }
        return false;
    }

  // public ArrayList<Incidencia> getIncidencias() {
   public void  getIncidencias() {
        RequestQueue queue = Volley.newRequestQueue(this);
        //String h = "http://infomaz.com/?var=listar_incidencias&fecha=2018-10-08&usuario=JPERES";
       String h = "http://www.ubusgo.com/driver/actions/" + "getIncidencias.php";;
        Log.e("aqui pues2","adsfasd");
        incidencias.clear();
       final ArrayList<Incidencia> incidenciasArray = new ArrayList<>();
       final String url = "http://httpbin.org/get?param1=hello";

       RequestQueue requestQueue = Volley.newRequestQueue(context);

       // Initialize a new JsonArrayRequest instance
       JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
               Method.GET,
               h,
               null,
               new Response.Listener<JSONArray>() {
                   @Override
                   public void onResponse(JSONArray response) {
                       try{
                           Log.e("array incis",response.length()+"");
                           // Loop through the array elements
                           for(int i=0;i<response.length();i++){
                               // Get current json object
                               JSONObject student = response.getJSONObject(i);
                              String fecha = "23/12/2018";
                               String descripcion="SOLICITAR AMBULANCIA.";
                               String leido="1";

                               String latitud="-13.5189006";
                               String longitud="-71.9615863";
                            //   Incidencia act = new Incidencia(latitud,longitud,descripcion,descripcion,fecha,leido,"dd");
                            //   incidenciasArray.add(act);


                               Log.e("array incaa",incidenciasArray.size()+"");
                               // Display the formatted json data in text view
                           }
                           Log.e("array inc22a",incidenciasArray.size()+"");
                           incidencias=incidenciasArray;
                          /* recyclerView.setAdapter(adapter);
                           adapter.notifyDataSetChanged();
                           for(int i = 0; i < incidencias.size(); i++) {
                               Incidencia e = incidencias.get(i);
                               data_list.add(e);
                           }*/
                           recyclerView = null;
                           recyclerView = (RecyclerView) findViewById(R.id.recycler_view_incidencias);
                           data_list = new ArrayList<>();
                           layoutManager = new LinearLayoutManager(context);
                           recyclerView.setLayoutManager(layoutManager);

                           adapter = new IncidenciaAdapter(context, data_list);
                           recyclerView.setAdapter(adapter);

                           for(int i = 0; i < incidencias.size(); i++) {
                               Incidencia e = incidencias.get(i);
                               data_list.add(e);
                              /* if(match(e.getNombreComercial(), str)) {
                                   data_list.add(e);
                                   //Toast.makeText(getApplicationContext(),"joven", Toast.LENGTH_SHORT).show();
                               }*/
                           }

                           adapter.notifyDataSetChanged();
                           Log.e("array ina",incidencias.size()+"");
                       }catch (JSONException e){
                           e.printStackTrace();
                       }
                   }
               },
               new Response.ErrorListener(){
                   @Override
                   public void onErrorResponse(VolleyError error){
                       // Do something when error occurred
                       Log.e("error get",error+"");
                   }
               }
       );
       requestQueue.add(jsonArrayRequest);
       Log.e("array incidenciass",incidenciasArray.size()+"");
    }


    public void getIncidencias3(final String usuario, final String fecha) {

        final ArrayList<Incidencia> incidenciasArray = new ArrayList<>();
        final String F_URL_GEINCIDENCIAS = URL_LISTAR_INCIDENCIAS + "&operador=" + usuario;

        Log.d("url", F_URL_GEINCIDENCIAS);
        StringRequest stringRequest = new StringRequest(ROOT+F_URL_GEINCIDENCIAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray jsonArray =  new JSONArray(response);
                            Log.e("array incis",response.length()+"");

                            SharedPrefManager.getInstance(getApplicationContext()).setresponseListadoIncidencias(response);
                            // Loop through the array elements
                            for(int i=0;i<jsonArray.length();i++){
                                // Get current json object
                                JSONObject incidencia = jsonArray.getJSONObject(i);
                                String fecha = incidencia.getString("fecha");
                                String titulo=incidencia.getString("titulo");
                                String leido=incidencia.getString("leido");
                                String id=incidencia.getString("idIncidencia");
                                String ubicacion = "-13.521190,-71.975851";//incidencia.getString("ubicacion");
                                String operador=incidencia.getString("operador");
                                String latitud=  incidencia.getString("latitud");// ubicacionArr[0];//"-13.5158962";
                                String longitud=  incidencia.getString("longitud");//ubicacionArr[1];
                                String notificacion = incidencia.getString("notificado");
                                String descripcion = incidencia.getString("descripcion");

                                String tipoIncidencia=incidencia.getString("tipo_incidencia");

                                Incidencia act = new Incidencia(latitud,longitud,titulo,descripcion,fecha,leido,operador,id, notificacion, 1, tipoIncidencia );
                                incidenciasArray.add(act);
                            }
                            Log.e("array inc22a",incidenciasArray.size()+"");
                            incidencias=incidenciasArray;
                            recyclerView = null;
                            recyclerView = (RecyclerView) findViewById(R.id.recycler_view_incidencias);
                            data_list = new ArrayList<>();
                            layoutManager = new LinearLayoutManager(context);
                            recyclerView.setLayoutManager(layoutManager);

                            adapter = new IncidenciaAdapter(context, data_list);
                            recyclerView.setAdapter(adapter);

                            for(int i = 0; i < incidencias.size(); i++) {
                                Incidencia e = incidencias.get(i);
                                data_list.add(e);
                            }

                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof ServerError) {

                    //Toast.makeText(getApplicationContext(),"Upps algo anda mal",Toast.LENGTH_SHORT).show();

                    if (!SharedPrefManager.getInstance(getApplicationContext()).isresponseListadoIncidenciasNull()) {


                        String response = SharedPrefManager.getInstance(getApplicationContext()).getresponseListadoIncidencias();

                        try {

                            JSONArray jsonArray =  new JSONArray(response);
                            Log.e("array incis",response.length()+"");

                            SharedPrefManager.getInstance(getApplicationContext()).setresponseListadoIncidencias(response);
                            // Loop through the array elements
                            for(int i=0;i<jsonArray.length();i++){
                                // Get current json object
                                JSONObject incidencia = jsonArray.getJSONObject(i);
                                String fecha = incidencia.getString("fecha");
                                String descripcion=incidencia.getString("titulo");
                                String leido=incidencia.getString("leido");
                                String id=incidencia.getString("idIncidencia");
                                String ubicacion = "-13.521190,-71.975851";//incidencia.getString("ubicacion");
                                String operador=incidencia.getString("operador");
                                String latitud=  incidencia.getString("latitud");// ubicacionArr[0];//"-13.5158962";
                                String longitud=  incidencia.getString("longitud");//ubicacionArr[1];
                                String notificacion = incidencia.getString("notificado");

                                String tipoIncidencia=incidencia.getString("tipo_incidencia");

                                Incidencia act = new Incidencia(latitud,longitud,descripcion,descripcion,fecha,leido,operador,id, notificacion, 1, tipoIncidencia );
                                incidenciasArray.add(act);
                            }
                            Log.e("array inc22a",incidenciasArray.size()+"");
                            incidencias=incidenciasArray;
                            recyclerView = null;
                            recyclerView = (RecyclerView) findViewById(R.id.recycler_view_incidencias);
                            data_list = new ArrayList<>();
                            layoutManager = new LinearLayoutManager(context);
                            recyclerView.setLayoutManager(layoutManager);

                            adapter = new IncidenciaAdapter(context, data_list);
                            recyclerView.setAdapter(adapter);

                            for(int i = 0; i < incidencias.size(); i++) {
                                Incidencia e = incidencias.get(i);
                                data_list.add(e);
                            }

                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Sin conexion a internet.",Toast.LENGTH_SHORT).show();
                    }

                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getApplicationContext(),"AuthFailureError",Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(),"NetworkError",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(),"ParseError",Toast.LENGTH_SHORT).show();
                }

            }
        }
        );




        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }

    public String getS(String str){
        return  str;
        /*String out = null;
        try {
            out = new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;*/
    }
}
