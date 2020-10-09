package  com.fiscapp.fiscapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import com.fiscapp.fiscapp.Helpers.Connection;
import com.fiscapp.fiscapp.Model.Incidencia;
import com.fiscapp.fiscapp.Model.Placa;
import com.fiscapp.fiscapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VerificarPlacaActivity extends AppCompatActivity implements View.OnClickListener {
    Button enviarWeb,enviarSMS,cancelar;
    EditText placa;
    Context context;
    LinearLayout noInternet;
    Placa Placa;
    String ROOT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_placa);

        String RootResp = SharedPrefManager.getInstance(this).getDominio();

        try {
            JSONArray jsonArray =  new JSONArray(RootResp);
            JSONObject obj = jsonArray.getJSONObject(0);
            ROOT = obj.getString("dominio") + "?var=";
        } catch (JSONException e) {
            e.printStackTrace();
        }

        context=this;
        Placa=new Placa();
        /*noInternet=(LinearLayout)findViewById(R.id.noInternet);
        if (isConnected(context)) {
            noInternet.setVisibility(LinearLayout.GONE);
        } else {
            noInternet.setVisibility(LinearLayout.VISIBLE);

        }*/

        Button atras = (Button) findViewById(R.id.atras);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        placa=(EditText) findViewById(R.id.editPlaca);
        enviarWeb = (Button) findViewById(R.id.btnWeb);
        enviarSMS = (Button) findViewById(R.id.btnSMS);
        cancelar = (Button) findViewById(R.id.btnCancela);
        enviarWeb.setOnClickListener(this);
        cancelar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        enviarSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!placa.getText().toString().isEmpty()){
                    String valor = placa.getText().toString();


                    String config = SharedPrefManager.getInstance(getApplicationContext()).getConfig();


                    String tiempo_anulacion = "";
                    String tiempo_emergencia = "";
                    String url_upload = "";
                    String usuario_ftp = "";
                    String pw_ftp = "";
                    String numero_sms = "";
                    String numero_emergencia = "";
                    String acta_header = "";
                    String acta_footer = "";
                    String educativa_footer = "";

                    ArrayList<String> lista = new ArrayList<>();

                    try {
                        JSONArray jsonArray = new JSONArray(config);



                        JSONObject obj = jsonArray.getJSONObject(0);
                        numero_sms = obj.getString("numero_sms");


                        sendSMS(numero_sms,"La placa a consultar es "+valor);

                        Toast.makeText(getApplicationContext(), "Mensaje enviado a " + numero_sms, Toast.LENGTH_SHORT).show();



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }else{
                    Connection.showOKDialog(context,"Mensaje","Debe poner una placa válida.");
                }

            }
        });
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarValidar);
        toolbar.setTitle("Verificar Placa");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                startActivity(new Intent(getApplicationContext(), MapActivity.class));
            }
        });*/
    }
    public void sendSMS(String phoneNo, String msg) {

        String config = SharedPrefManager.getInstance(getApplicationContext()).getConfig();


        String tiempo_anulacion = "";
        String tiempo_emergencia = "";
        String url_upload = "";
        String usuario_ftp = "";
        String pw_ftp = "";
        String numero_sms = "";
        String numero_emergencia = "";



        try {
            JSONArray jsonArray = new JSONArray(config);



            JSONObject obj = jsonArray.getJSONObject(0);
            tiempo_anulacion = obj.getString("tiempo_anulacion");
            tiempo_emergencia = obj.getString("tiempo_emergencia");
            url_upload = obj.getString("url_upload");
            usuario_ftp = obj.getString("usuario_ftp");
            pw_ftp = obj.getString("pw_ftp");
            numero_sms = obj.getString("numero_sms");
            numero_emergencia = obj.getString("numero_emergencia");

            //Toast.makeText(getApplicationContext(), tiempo_anulacion, Toast.LENGTH_SHORT).show();



        } catch (JSONException e) {
            e.printStackTrace();
        }

        phoneNo = numero_sms;

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Mensaje Enviado",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
    //region====================Consultar Via Weeb=====
    @Override
    public void onClick(View v) {
        if(!placa.getText().toString().isEmpty()){
            String valor = placa.getText().toString();
            recuperarDatosPlaca(valor);

        }else{
            Connection.showOKDialog(context,"Mensaje","Debe poner una placa válida.");
        }
    }
    //endregion

    private static ConnectivityManager manager;

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            Network[] activeNetworks = cm.getAllNetworks();
            for (Network n : activeNetworks) {
                NetworkInfo nInfo = cm.getNetworkInfo(n);
                if (nInfo.isConnected())
                    return true;
            }

        } else {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo anInfo : info)
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }

        return false;

    }
    public  void recuperarDatosPlaca(final String placa) {
/*        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Haciendo Magic...");
        pd.show();*/
        final ArrayList<Incidencia> incidenciasArray = new ArrayList<>();

        String URL_SAVEINCIDENCIA = ROOT+"recuperar_vehiculo_autorizado&placa="+placa;
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
                            // if(obj.getString("placa"))
                            Placa placa= new Placa();
                            placa.setPlaca(obj.getString("placa"));
                            placa.setReplacado(obj.getString("replacado"));
                            placa.setAutorizacion(obj.getString("autorizacion"));
                            placa.setEstadoAutorizacion(obj.getString("estadoAutorizacion"));
                            placa.setFechaDesde(obj.getString("fechaDesde"));
                            placa.setFechaHasta( obj.getString("fechaHasta"));
                            placa.setFechaInscripcion(obj.getString("fechaInscripcion"));
                            placa.setResolucion(obj.getString("resolucion"));
                            placa.setNumeroCertificado(obj.getString("numeroCertificado"));
                            placa.setNumeroExpediente(obj.getString("numeroExpediente"));
                            placa.setFechaExpediente(obj.getString("fechaExpediente"));
                            placa.setPropietario(obj.getString("propietario"));
                            placa.setDireccionPropietario( obj.getString("direccionPropietario"));
                            placa.setNombresConductor(obj.getString("nombresConductor"));
                            placa.setLicencia(obj.getString("licencia"));
                            placa.setEmpresa(obj.getString("empresa"));
                            placa.setCarroceria(obj.getString("carroceria"));
                            placa.setAnioFabricacion( obj.getString("anioFabricacion"));
                            placa.setClase( obj.getString("clase"));
                            placa.setMarcaVehiculo( obj.getString("marcaVehiculo"));
                            placa.setColor(obj.getString("color"));
                            placa.setNro_motor( obj.getString("nro_motor"));
                            placa.setSerie( obj.getString("serie"));

                            Placa=placa;
                            Intent intent = new Intent(getApplicationContext(), PlacaResultados.class);
                            intent.putExtra("placa",(Serializable) Placa);

                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("priaaamera",e+"");
                            Connection.showOKDialog(context,"Mensaje","LA PLACA DE VEHICULO NO SE ENCUENTRA REGISTRADA EN LA BASE DE DATOS DE VEHICULOS AUTORIZADOS");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // progressDialog.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(),"Upps algo anda mal",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(),"AuthFailureError",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getApplicationContext(),"ServerError",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(),"NetworkError",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getApplicationContext(),"ParseError",Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        ){

        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }
}
