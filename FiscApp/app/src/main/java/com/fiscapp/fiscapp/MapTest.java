package com.fiscapp.fiscapp;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.maps.android.ui.IconGenerator;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.jibble.simpleftp.SimpleFTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.fiscapp.fiscapp.Constants.ROOT_URL_JAIRO;
import static com.fiscapp.fiscapp.Constants.URL_GETINCIDENCIAS;
import static com.fiscapp.fiscapp.Constants.URL_GUARDAR_INCIDENCIA;
import static com.fiscapp.fiscapp.Constants.URL_GUARDAR_LEIDO;
import static com.fiscapp.fiscapp.Constants.URL_GUARDAR_NOTIFICADO;
import static com.fiscapp.fiscapp.Constants.URL_LISTAR_CONFIGURACION;
import static com.fiscapp.fiscapp.Constants.URL_LISTAR_INCIDENCIAS;
import static com.fiscapp.fiscapp.Constants.URL_SAVEINCIDENCIA;
import static com.fiscapp.fiscapp.Constants.URL_SAVEUSUARIOINCIDENCIA;
import static com.fiscapp.fiscapp.Constants.URL_UPDATEESTADOINCIDENCIA;
import static com.fiscapp.fiscapp.MainActivity.REQUEST_CHECK_SETTINGS;

public class MapTest extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,View.OnClickListener,
        GoogleMap.OnMarkerClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private TextView latitudeTextView,longitudeTextView;
    private Location mylocation;
    private GoogleApiClient googleApiClient;

    private final static int REQUEST_CHECK_SETTINGS_GPS=0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;


    private static final long INTERVAL = 1000 * 5;
    private long FASTEST_INTERVAL = 1000 * 1; /* 1 sec */


    private GoogleMap mMap;
    private LocationManager locationManager;


    Location mCurrentLocation;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    private TextView btnEmergencia;
    private Button rg;
    SlidingUpPanelLayout sl;

    boolean clicked = false;

    Dialog dialogSMS;


    PendingResult<LocationSettingsResult> result;
    final static int REQUEST_LOCATION = 199;

    private ImageButton btnVerFiscas;
    private ProgressDialog progressDialog;

    private ImageButton imgbtnMenu, imgbtnEmergencia, btnVerIncidentes;

    private ImageView position;

    private String  mUsuario, mOperador;

    private int mUsuarioID;

    private Handler mHandler;
    private Runnable mAnimation;

    Date d1, d2;

    TextView counter, etLugar, tvInciNovistas;

    ImageView imageView;

    String file_path = "", file_name;

    private ProgressDialog GpdDialog;

    private TextView btnClose;
    private Button btnCancelar, btnEnviarSMS, btnAbrirModal, btnInvisible;

    private EditText etSMS;

    private ImageView direccion;

    TextView textView2, textView8, textView6;

    ArrayList<String> arrStr = new ArrayList<>();
    String imgNameHeader = "header.png";
    String imgNameFooter = "footer.png";
    String imgNameEducativa = "educativa_footer.png";

    String ROOT;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        arrStr.add(imgNameHeader);
        arrStr.add(imgNameFooter);
        arrStr.add(imgNameEducativa);


        btnEmergencia = (TextView) findViewById(R.id.btnEmergencia);
        addClickEffect(btnEmergencia);

        //imageView = (CircleImageView)findViewById(R.id.circleImageView2);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        rg = (Button) findViewById(R.id.registrarBus);

        rg.setOnClickListener(this);

        sl = (SlidingUpPanelLayout) findViewById(R.id.sliding);

        imgbtnEmergencia = (ImageButton) findViewById(R.id.imgbtnEmergencia);

        direccion = (ImageView) findViewById(R.id.direccion);

        GpdDialog = new ProgressDialog(this);

        GpdDialog.setMessage("Cargando Mapa...");
        GpdDialog.show();

        btnInvisible = (Button) findViewById(R.id.btnInvisible);

        btnAbrirModal = (Button) findViewById(R.id.btnAbrirModal);
        dialogSMS = new Dialog(MapTest.this);
        dialogSMS.setContentView(R.layout.modal_enviar_sms);
        dialogSMS.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        etSMS = (EditText) dialogSMS.findViewById(R.id.etSMS);
        btnClose = (TextView) dialogSMS.findViewById(R.id.btnClose);
        btnCancelar = (Button) dialogSMS.findViewById(R.id.btnCancelar);
        btnEnviarSMS = (Button) dialogSMS.findViewById(R.id.btnEnviarSMS);

        btnClose.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
        btnEnviarSMS.setOnClickListener(this);
        btnAbrirModal.setOnClickListener(this);





        addClickEffect(imgbtnEmergencia);

        counter = (TextView) findViewById(R.id.counter);

        etLugar = (TextView) findViewById(R.id.etLugar);

        tvInciNovistas = (TextView) findViewById(R.id.tvInciNovistas);

        btnVerIncidentes = (ImageButton) findViewById(R.id.btnVerIncidentes);

        btnVerIncidentes.setOnClickListener(this);

        mHandler = new Handler();

        imgbtnEmergencia.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        canceled = false;
                        counter.setVisibility(View.VISIBLE);

                        if(mhandler != null) mhandler.removeCallbacks(r);
                        c = 0;
                        tiempo = 1000;


                        count();
                        d1 = new Date();
                        Log.i("TAG", "touched down");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i("TAG", "moving: (" + x + ", " + y + ")");
                        break;
                    case MotionEvent.ACTION_UP:
                        d2 =  new Date();
                        canceled = true;
                        int dif = calcDiff(d1, d2);
                        mhandler.removeCallbacks(r);
                        tiempo = 0;

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

                        if(c >= Integer.parseInt(tiempo_emergencia)) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String fecha = sdf.format(new Date());
                            String ubicacion = mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude();
                            guardarIncidencia("EMERGENCIA", ubicacion, "Ocurrio una emergencia",
                                                mOperador, fecha, "2", "");

                            Intent intent = new Intent(Intent.ACTION_CALL);

                            intent.setData(Uri.parse("tel:" + numero_emergencia));
                            startActivity(intent);
                            //saveIncidencia(mUsuarioID+"", ubicacion, "EMERGENCIA", "1");
                        } else {
                            if(c >= 1) {
                                Toast.makeText(getApplicationContext(), "Presione " + tiempo_emergencia + "s para enviar una Emergencia", Toast.LENGTH_SHORT).show();
                            }
                        }
                        counter.setText("00");
                        counter.setVisibility(View.GONE);


                        //Toast.makeText(getApplicationContext(), dif +"", Toast.LENGTH_LONG).show();
                        Log.i("TAG", "touched up");
                        break;
                }

                return true;
            }
        });



        btnEmergencia.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        canceled = false;
                        counter.setVisibility(View.VISIBLE);
                        if(mhandler != null) mhandler.removeCallbacks(r);
                        c = 0;
                        tiempo = 1000;
                        count();
                        d1 = new Date();
                        Log.i("TAG", "touched down");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i("TAG", "moving: (" + x + ", " + y + ")");
                        break;
                    case MotionEvent.ACTION_UP:
                        d2 =  new Date();
                        canceled = true;
                        int dif = calcDiff(d1, d2);
                        mhandler.removeCallbacks(r);

                        String config = SharedPrefManager.getInstance(getApplicationContext()).getConfig();

                        tiempo = 0;
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

                        if(c >= Integer.parseInt(tiempo_emergencia)) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String fecha = sdf.format(new Date());
                            String ubicacion = mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude();
                            guardarIncidencia("EMERGENCIA", ubicacion, "Ocurrio una emergencia",
                                    mOperador, fecha, "2", "");


                            Intent intent = new Intent(Intent.ACTION_CALL);

                            intent.setData(Uri.parse("tel:" + numero_emergencia));
                            startActivity(intent);
                            //saveIncidencia(mUsuarioID+"", ubicacion, "EMERGENCIA", "1");
                        } else {
                            if(c >= 1) {
                                Toast.makeText(getApplicationContext(), "Presione " + tiempo_emergencia + "s para enviar una Emergencia", Toast.LENGTH_SHORT).show();
                            }

                        }
                        counter.setText("00");
                        counter.setVisibility(View.GONE);


                        //Toast.makeText(getApplicationContext(), dif +"", Toast.LENGTH_LONG).show();
                        Log.i("TAG", "touched up");
                        break;
                }

                return true;
            }
        });

        //sl.setDragView(btnInvisible);



        sl.setTouchEnabled(false);

        direccion.setOnClickListener(this);
        //btnEmergencia.setOnClickListener(this);
        /*latitudeTextView=(TextView)findViewById(R.id.latitudeTextView);
        longitudeTextView=(TextView)findViewById(R.id.longitudeTextView);*/
        setUpGClient();


        progressDialog = new ProgressDialog(this);


        btnEmergencia = (TextView) findViewById(R.id.btnEmergencia);
        btnEmergencia.setOnClickListener(this);

        /*PARA LOS NROS DE EMERGENCIA*/
        textView2 = (TextView)findViewById(R.id.textView2);
        textView8 = (TextView)findViewById(R.id.textView8);
        /*TEXTO*/
        textView6 = (TextView)findViewById(R.id.textView6);


//        btnVerFiscas = (ImageButton) findViewById(R.id.btnVerFiscas);

//        btnVerFiscas.setOnClickListener(this);

        imgbtnMenu = (ImageButton) findViewById(R.id.imgbtnMenu);
        position = (ImageView) findViewById(R.id.position);

        position.setOnClickListener(this);
        imgbtnMenu.setOnClickListener(this);


        mUsuarioID = SharedPrefManager.getInstance(getApplicationContext()).getUserKeyId();
        mUsuario = SharedPrefManager.getInstance(getApplicationContext()).getUserKeyUsuario();
        mOperador = mUsuario;

        TextView tvOperador;




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.act_main);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

        NavigationView navigationView2 = findViewById(R.id.nav_view);

        View header = navigationView2.getHeaderView(0);
        tvOperador = header.findViewById(R.id.tvNombresInspec);

        tvOperador.setText(mOperador);



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);

        String RootResp = SharedPrefManager.getInstance(this).getDominio();

        try {
            JSONArray jsonArray =  new JSONArray(RootResp);
            JSONObject obj = jsonArray.getJSONObject(0);
            ROOT = obj.getString("dominio") + "?var=";
        } catch (JSONException e) {
            e.printStackTrace();
        }



        getConfiguracion();





    }

    public void guardarIncidencia(final String titulo, final String ubicacion, final String descripcion, final String operador,final String fecha,final String estado,final String tipo) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Guardando Incidencia...");
        pd.show();

        //Toast.makeText(getApplicationContext(), "QUE PASO", Toast.LENGTH_SHORT).show();

        Log.d("urll", URL_GUARDAR_INCIDENCIA);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                ROOT+URL_GUARDAR_INCIDENCIA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {
                            JSONArray jsonArray =  new JSONArray(response);
                            JSONObject obj = jsonArray.getJSONObject(0);
                            if (!obj.getBoolean("error")){
                                String msg = obj.getString("messsage");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                            } else {
                                String msg = obj.getString("messsage");
                                Toast.makeText(getApplicationContext(), msg + "wtf", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();

                        Toast.makeText(getApplicationContext(), "Sin conexion a internet.", Toast.LENGTH_SHORT).show();
                        /*if (error instanceof TimeoutError || error instanceof NoConnectionError) {
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
                String ll[] = ubicacion.split(",");
                String latitud = ll[0];
                String longitud = ll[1];

                params.put("titulo", titulo);
                params.put("latitud",latitud);
                params.put("longitud",longitud);
                params.put("descripcion",descripcion);
                params.put("operador",operador);
                params.put("fecha",fecha);
                params.put("estado",estado);
                params.put("tipo",tipo);
                String res = titulo + " " + latitud + " " + longitud + " " + descripcion + " " + operador + " " + fecha + " " + estado + " " + tipo;
                Log.d("urll", res);


                return params;
            }
        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }


    public void go(){
        FTPClient con = null;

        try
        {
            con = new FTPClient();
            con.connect("192.168.2.57");

            if (con.login("Administrator", "KUjWbk"))
            {
                con.enterLocalPassiveMode(); // important!
                con.setFileType(FTP.BINARY_FILE_TYPE);
                String data = "/sdcard/vivekm4a.m4a";

                OutputStream out = new FileOutputStream(new File(data));
                boolean result = con.retrieveFile("vivekm4a.m4a", out);
                out.close();

                if (result) Log.v("download result", "succeeded");
                con.logout();
                con.disconnect();
            }
        }
        catch (Exception e)
        {
            Log.v("download result","failed");
            e.printStackTrace();
        }

    }

    private class UploadFile extends AsyncTask<String, Integer, Boolean> {
        private SweetAlertDialog pDialog;
        //private ProgressDialog mProgressDialog;
        @Override
        protected Boolean doInBackground(String... params) {

            String f_path = params[0];
            String f_name = params[1];
            f_name = mUsuario.toLowerCase() + f_name;
            FTPClient con = null;

            try
            {
                con = new FTPClient();
                /*con.connect(InetAddress.getByName("ftp.smarterasp.net"));

                if (con.login("facturamas", "facturamas*1"))
                {*/

                con.connect(InetAddress.getByName("infomaz.com"));

                if (con.login("admin@infomaz.com", "admin$$$"))
                {
                        con.changeWorkingDirectory("/uploads");
                        con.setFileType(FTP.BINARY_FILE_TYPE);

                        File file = new File(Environment.getExternalStorageDirectory(), f_path);
                        BufferedInputStream buffIn = null;
                        buffIn = new BufferedInputStream(new FileInputStream(file));



                        con.enterLocalPassiveMode();
                        boolean result = con.storeFile(f_name, buffIn);
                        if (result) Log.v("whatis", "succeeded");
                        buffIn.close();
                        con.logout();
                        con.disconnect();

                        return result;

                } else {
                    Log.d("whatis", "MAL LOGEO");
                    //Toast.makeText(getApplicationContext(), "Error logueo", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            catch (Exception e)
            {
                Log.d("whatis", "wtf");
                //Toast.makeText(getApplicationContext(), "Exception correctamente", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean sucess) {
            pDialog.dismiss();
            if (sucess)
                Toast.makeText(getApplicationContext(), "File Sent", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute() {

            /*progressDialog.setMessage("Haciendo Magia...");
            progressDialog.show();*/

            pDialog = new SweetAlertDialog(MapTest.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Subiendo Imagen...");
            pDialog.setCancelable(false);
            pDialog.show();

            //Toast.makeText(getApplicationContext(), "ONPREEXCEUTE", Toast.LENGTH_SHORT).show();
            //super.onPreExecute();
            //btnDownload.setText("Descargando...");

            /*mProgressDialog = new ProgressDialog(getApplicationContext());
            mProgressDialog.setMessage("Descargando audios..");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();*/

            /*pDialog = new SweetAlertDialog(MapTest.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Descargando audios..");
            pDialog.setCancelable(false);
            pDialog.show();*/


//            buttonText.setEnabled(false);
//            btnDownload.setText("Donload Started...");//Set Button Text when download started
        }



    }

    private class DownloadFile extends AsyncTask<String, Integer, Boolean> {
        private SweetAlertDialog pDialog;
        //private ProgressDialog mProgressDialog;
        @Override
        protected Boolean doInBackground(String... params) {

            String f_path = params[0];
            String f_name = params[1];
            f_name = mUsuario.toLowerCase() + f_name;
            FTPClient con = null;

            try
            {
                con = new FTPClient();
                con.connect(InetAddress.getByName("infomaz.com"));

                if (con.login("admin@infomaz.com", "admin$$$"))
                {

                    String saveDir = Environment.getExternalStorageDirectory() + "";

                    downloadDirectory(con, "", "uploads", saveDir + "/Ubus");

                    /*con.changeWorkingDirectory("/uploads");
                    con.setFileType(FTP.BINARY_FILE_TYPE);

                    Log.d("whatis", "ENTREEEE");
                    File file = new File(Environment.getExternalStorageDirectory(), "Ubus/kid.mp4");
                    con.enterLocalPassiveMode();
                    OutputStream outputStream = null;
                    boolean success = false;
                    try {
                        outputStream = new BufferedOutputStream(new FileOutputStream(
                                file));
                        success = con.retrieveFile("tudumi.mp4", outputStream);
                    } finally {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    }

                    if (success) Log.v("whatis", "succeeded");*/
                    //out.close();
                    con.logout();
                    con.disconnect();

                    return true;

                } else {
                    Log.d("whatis", "MAL LOGEO");
                    //Toast.makeText(getApplicationContext(), "Error logueo", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            catch (Exception e)
            {
                Log.d("whatis", "wtf " + e.getMessage());
                //Toast.makeText(getApplicationContext(), "Exception correctamente", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean sucess) {
            pDialog.dismiss();
            if (sucess)
                Toast.makeText(getApplicationContext(), "File Sent", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute() {

            /*progressDialog.setMessage("Haciendo Magia...");
            progressDialog.show();*/

            pDialog = new SweetAlertDialog(MapTest.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Descargando Imagen...");
            pDialog.setCancelable(false);
            pDialog.show();

            //Toast.makeText(getApplicationContext(), "ONPREEXCEUTE", Toast.LENGTH_SHORT).show();
            //super.onPreExecute();
            //btnDownload.setText("Descargando...");

            /*mProgressDialog = new ProgressDialog(getApplicationContext());
            mProgressDialog.setMessage("Descargando audios..");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();*/

            /*pDialog = new SweetAlertDialog(MapTest.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Descargando audios..");
            pDialog.setCancelable(false);
            pDialog.show();*/


//            buttonText.setEnabled(false);
//            btnDownload.setText("Donload Started...");//Set Button Text when download started
        }



    }


    public class MyTask extends  AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onProgressUpdate(Integer... values) {

        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected String doInBackground(String... params) {
            String myS =  params[0];
            int i = 0;
            publishProgress(i);
            return "some string";
        }

    }

    public void goforIt(){


        FTPClient con = null;

        try
        {
            con = new FTPClient();
            con.connect("ubusgo.com");

            if (con.login("ftp_chasty@ubusgo.com", "|3fE5h8c.s^i-"))
            {
                Toast.makeText(this, "Logueado correctamente", Toast.LENGTH_SHORT).show();
               /* con.enterLocalPassiveMode(); // important!
                con.setFileType(FTP.BINARY_FILE_TYPE);
                String data = "/sdcard/vivekm4a.m4a";

                FileInputStream in = new FileInputStream(new File(data));
                boolean result = con.storeFile("/vivekm4a.m4a", in);
                in.close();
                if (result) Log.v("upload result", "succeeded");
                con.logout();
                con.disconnect();*/
            } else {
                Toast.makeText(this, "Error logueo", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Exception correctamente", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }






    }

    public void upload() throws IOException {

        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(InetAddress.getByName("ubusgo.com"));
        ftpClient.login("ftp_chasty@ubusgo.com", "\"|3fE5h8c.s^i-");
        ftpClient.changeWorkingDirectory("public_html/users");
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);


        File file = new File(Environment.getExternalStorageDirectory(), "Ubus/picture.jpg");

        BufferedInputStream buffIn = null;
        buffIn = new BufferedInputStream(new FileInputStream(file));
        ftpClient.enterLocalPassiveMode();
        ftpClient.storeFile("test.jpg", buffIn);
        buffIn.close();
        ftpClient.logout();
        ftpClient.disconnect();

        /*try {
            SimpleFTP ftp = new SimpleFTP();

            // Connect to an FTP server on port 21.
            ftp.connect("ubusgo.com", 21, "ftp_chasty@ubusgo.com", "\"|3fE5h8c.s^i-");

            // Set binary mode.
            ftp.bin();

            // Change to a new working directory on the FTP server.
            ftp.cwd("public_html/users");

            // Upload some files.
            /*ftp.stor(new File("webcam.jpg"));
            ftp.stor(new File("comicbot-latest.png"));*/

            // Upload some files.
        /*    File file = new File(Environment.getExternalStorageDirectory(), "Ubus/picture.jpg");
            ftp.stor(file);
            // You can also upload from an InputStream, e.g.
            //ftp.stor(new FileInputStream(new File("test.png")), "test.png");
            //ftp.stor(someSocket.getInputStream(), "blah.dat");

            // Quit from the FTP server.
            ftp.disconnect();



        }
        catch (IOException e) {
            // Jibble.
            Log.d("error", e.getMessage());
        }*/
    }

    public void getDominio() {

        final ArrayList<Incidencia> incidenciasArray = new ArrayList<>();


        StringRequest stringRequest = new StringRequest(ROOT_URL_JAIRO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray jsonArray =  new JSONArray(response);
                            JSONObject obj = jsonArray.getJSONObject(0);
                            String dominio = obj.getString("dominio");

                            //Toast.makeText(getApplicationContext(), dominio, Toast.LENGTH_SHORT).show();

                            SharedPrefManager.getInstance(getApplicationContext()).setDominio(response);




                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });




        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }

    public void getConfiguracion() {

        final ArrayList<Incidencia> incidenciasArray = new ArrayList<>();


        StringRequest stringRequest = new StringRequest(ROOT+URL_LISTAR_CONFIGURACION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray jsonArray =  new JSONArray(response);
                            JSONObject obj = jsonArray.getJSONObject(0);
                            String tiempo_anulacion = obj.getString("tiempo_anulacion");
                            String tiempo_emergencia = obj.getString("tiempo_emergencia");
                            String url_upload = obj.getString("url_upload");
                            String usuario_ftp = obj.getString("usuario_ftp");
                            String pw_ftp = obj.getString("pw_ftp");
                            String numero_sms = obj.getString("numero_sms");
                            String numero_emergencia = obj.getString("numero_emergencia");


                            SharedPrefManager.getInstance(getApplicationContext()).setConfig(response);




                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });




        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }


    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    Marker markerd;
    boolean first = true;
    boolean firstShowInfo  = true;


    public int checkImagesContent() {
        int numberOfFiles = 0;
        File root = Environment.getExternalStorageDirectory();
        File dir = new File (root.getAbsolutePath() + "/Fiscapp");
        if(!dir.exists()) {
            return numberOfFiles;
        } else {
            File[] files = dir.listFiles();
            numberOfFiles = files.length;
            return numberOfFiles;
        }
        //File dir = new File(Environment.getExternalStorageDirectory() + "/UbusDriver/voices/paraderos");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onLocationChanged(Location location) {
        mylocation = location;
        //Toast.makeText(getApplicationContext(), "WTF", Toast.LENGTH_SHORT).show();
        if (mylocation != null) {

            //Location myCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            mCurrentLocation = location;
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());

            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 16.5f);
            if(first)
                mMap.animateCamera(update);
            first = false;
            //mMap.addMarker(new MarkerOptions().title("xd").position(ll));

            //etLugar.setText(getAddress(this, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));

            if(markerd == null) {
                markerd = mMap.addMarker(new MarkerOptions()
                        .position(ll)
                        .title(mUsuario)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.manmarker))
                        .flat(true).anchor(.5f, 0.5f));
                if(firstShowInfo)
                    markerd.showInfoWindow();
                firstShowInfo = false;

                GpdDialog.dismiss();



                if (isNetworkAvailable() && checkImagesContent() < 3){ // only if u have internet and u didnt have the audio
                    openAlertDescargarAudios();
                }



            } else {
                markerd.setPosition(ll);
                String curPos = location.getLatitude() + "," + location.getLongitude();
                SharedPrefManager.getInstance(getApplicationContext()).setCurrentPosition(curPos);
                //Toast.makeText(getApplicationContext(), "map " + curPos, Toast.LENGTH_SHORT).show();
                saveUserLocation(mUsuarioID+"", curPos);


            }



            LatLng correctLocation = null;
        }
    }


    public void openAlertDescargarAudios() {
        final SweetAlertDialog sweet = new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        sweet
                .setTitleText("Descarga de imagenes")
                .setContentText("Vamos a descargar imagenes para la impresion.")
                .setConfirmText("SI")
                //.setCustomImage(R.drawable.busstop)
                .show();

        sweet.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                //crearRuta();
                //onRutaCreada = true;
                sweet.dismissWithAnimation();

                //Toast.makeText(getApplicationContext(), "ENTRE a", Toast.LENGTH_SHORT).show();

                new MapTest.DownloadingParaderos().execute("");
            }
        });



        sweet.setCanceledOnTouchOutside(false);

        Button btnConf = (Button) sweet.findViewById(R.id.confirm_button);
        btnConf.setBackgroundColor(ContextCompat.getColor(MapTest.this,R.color.success_stroke_color));

        Button btnCanc = (Button) sweet.findViewById(R.id.cancel_button);
        btnCanc.setBackgroundColor(ContextCompat.getColor(MapTest.this,R.color.red_btn_bg_pressed_color));
    }



    private void saveUserLocation(final String usuario, final String location){
        //Toast.makeText(getApplicationContext(), "ACG", Toast.LENGTH_SHORT).show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ROOT+Constants.URL_ACTUALIZAR_UBICACION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getApplicationContext(),"tmr",Toast.LENGTH_LONG).show();
                        try {


                            JSONArray jsonArray = new JSONArray(response);
                            /*JSONObject obj = jsonArray.getJSONObject(0);
                            String msg = obj.getString("message");
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();*/

                        } catch (JSONException e) {
                            //Toast.makeText(getApplicationContext(), "Acggg", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Variables que necesita el PHP, deben ser iguales al PHP

//                params.put("location", mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude());
                //params.put("location", location);

                String arr[] = location.split(",");
                params.put("id", usuario);
                params.put("latitud", arr[0]);
                params.put("longitud", arr[1]);

                return params;
            }
        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }


    @Override
    public void onConnected(Bundle bundle) {
        checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {
        //Do whatever you need
        //You can display a message here
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //You can display a message here
    }

    private void getMyLocation(){
        Log.e("location","duera");
        if(googleApiClient!=null) {
          //  if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(MapTest.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                 if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                Log.e("location","location");
                mylocation =  LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

                LocationRequest locationRequest = new LocationRequest();
                locationRequest.setInterval(INTERVAL);
                locationRequest.setFastestInterval(FASTEST_INTERVAL);
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest);
                //builder.setAlwaysShow(false);
                LocationServices.FusedLocationApi
                        .requestLocationUpdates(googleApiClient, locationRequest, this);

                PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                        .checkLocationSettings(googleApiClient, builder.build());
                result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                    @Override
                    public void onResult(LocationSettingsResult result) {
                        final Status status = result.getStatus();
                        switch (status.getStatusCode()) {
                            case LocationSettingsStatusCodes.SUCCESS:
                                // All location settings are satisfied.
                                // You can initialize location requests here.
                                int permissionLocation = ContextCompat
                                        .checkSelfPermission(MapTest.this,
                                                Manifest.permission.ACCESS_FINE_LOCATION);
                                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                    mylocation = LocationServices.FusedLocationApi
                                            .getLastLocation(googleApiClient);
                                }
                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied.
                                // But could be fixed by showing the user a dialog.
                                try {
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    // Ask to turn on GPS automatically
                                    status.startResolutionForResult(MapTest.this,
                                            REQUEST_CHECK_SETTINGS_GPS);
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied.
                                // However, we have no way
                                // to fix the
                                // settings so we won't show the dialog.
                                // finish();
                                break;
                        }
                    }
                });
           }
            //}
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

                case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                       // getMyLocation();
                        break;
                }
                break;

                case SELECT_FILE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        onSelectFromGalleryResult(data);
                        break;
                }
                break;

                case REQUEST_CAMERA:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        onCaptureImageResult(data);
                        break;
                }
                break;

                case REQUEST_CODE:
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                    {
                        // All required changes were successfully made
                        Incidencia incidencia= (Incidencia) data.getSerializableExtra("incidencia");
                        String latitud=incidencia.getLatitud();
                        String longitud=incidencia.getLongitud();
                        String descripcion=incidencia.getDescripcion();
                        String titulo = incidencia.getIncidencia();
                        String id=incidencia.getId();
                        String leido=incidencia.getLeido();
                        String notificacion =  incidencia.getNotificacion();

                        markerd.showInfoWindow();
                        LatLng ll = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 17f);
                        mMap.animateCamera(update);

                        if(markerIncidencia != null) {
                            markerIncidencia.remove();
                            markerIncidencia = null;
                        }
                        markerIncidencia = mMap.addMarker(new MarkerOptions()
                                .position(ll).title(getS(titulo))
                                .snippet(descripcion)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.warning16))
                                .flat(true).anchor(.5f, 0.5f));


                        markerIncidencia.showInfoWindow();



                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String fecha = sdf.format(new Date());

                        getIncidencias3(mOperador+"",fecha);

                        if(leido.equals("0")) { //no esta leido
                            guardarLeido(mOperador, id);

                        }





                        drawCircle(ll);

                        //Toast.makeText(MapActivity.this, latitud + ", " + longitud, Toast.LENGTH_LONG).show();
                        break;
                    }
                    case Activity.RESULT_CANCELED:
                    {
                        // The user was asked to change settings, but chose not to
                        //Toast.makeText(MapActivity.this, "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();
                        //activarGPS();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String fecha = sdf.format(new Date());

                        getIncidencias3(mOperador+"",fecha);
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
        break;
        }
    }

    private void checkPermissions(){
        int permissionLocation = ContextCompat.checkSelfPermission(MapTest.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        }else{
            getMyLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(MapTest.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
    }



    boolean canceled = false;

    Handler mhandler;

    Runnable r;

    int c = 0;
    int tiempo = 1000;
    public void count() {

        if(canceled) return;


        runOnUiThread(new Runnable() {


            @Override
            public void run() {
                mhandler = new Handler();
                if(canceled) return;
                mhandler.postDelayed(r = new Runnable() {
                    @Override
                    public void run() {
                        //add your code here
                        c++;
                        if(!canceled) {
                            String countStr = "" + c;
                            if (c < 10) countStr = "0" + c;
                            counter.setText(countStr);
                        }
                        if(!canceled) count();

                    }
                }, tiempo);

            }
        });

    }

    public void updateEstadoIncidencia(final String id_usuario, final String id_incidencia, final String notificacion) {
        final ArrayList<Incidencia> incidenciasArray = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                ROOT+URL_UPDATEESTADOINCIDENCIA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  pd.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")){

                            } else {
                                String msg = obj.getString("message");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Sin conexion a internet.",Toast.LENGTH_SHORT).show();
                        /*if (error instanceof TimeoutError || error instanceof NoConnectionError) {
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

                params.put("id_usuario", id_usuario);
                params.put("id_incidencia",id_incidencia);
                params.put("notificacion",notificacion);
                return params;
            }
        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
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
                            SharedPrefManager.getInstance(getApplicationContext()).setresponseListadoIncidenciasMap(response);
                            boolean found_notificacion = false;
                            int count_novistos = 0;
                            for(int i=0;i<jsonArray.length();i++){
                                // Get current json object
                                JSONObject incidencia = jsonArray.getJSONObject(i);
                                String fecha = incidencia.getString("fecha");
                                String descripcion=incidencia.getString("titulo");
                                String leido=incidencia.getString("leido");
                                String id=incidencia.getString("idIncidencia");
                                String operador = incidencia.getString("operador");
                                String notificado = incidencia.getString("notificado");
                                String tipo_incidencia = incidencia.getString("tipo_incidencia");

                                /*String ubicacion = incidencia.getString("ubicacion");
                                String ubicacionArr[] = ubicacion.split(",");
                                String latitud= ubicacionArr[0];//"-13.5158962";
                                String longitud= ubicacionArr[1];
                                String nombres = incidencia.getString("nombres");
                                String apellidos = incidencia.getString("apellidos");
                                String celular = incidencia.getString("celular");
                                int estado = incidencia.getInt("estado");
                                int notificacion = incidencia.getInt("notificacion");*/

                                if(leido.equals("0")) {
                                    count_novistos++;
                                }

                                //Aqui encontramos a alguien q ha pulsado el boton de emergencia -> (estado = 1)
                                //y que todavia no recibio la notificacion -> (notificacion = 0)
                                //ademas que no haya mostrado(encontrado) la notificacion local
                                /*if(tipo_incidencia.equals("EMERGENCIA") && notificado.equals("0") && !found_notificacion) { //que se haya registrado una notificacion y que yo no lo haya abierto la notificacion
                                    sendNotification1(operador, "", "937208282");
                                    //si es asi, procedemos a
                                    //saveUsuarioIncidecncia2(mUsuarioID+"",id,"0", "1");

                                    guardarNotificado(mUsuario, id);
                                    found_notificacion = true;
                                }*/
                                if(notificado.equals("0") && !found_notificacion) { //que se haya registrado una notificacion y que yo no lo haya abierto la notificacion
                                    sendNotification1(operador, "", "937208282", tipo_incidencia);
                                    //si es asi, procedemos a
                                    //saveUsuarioIncidecncia2(mUsuarioID+"",id,"0", "1");

                                    guardarNotificado(mUsuario, id);
                                    found_notificacion = true;
                                }
                                //Incidencia act = new Incidencia(latitud,longitud,descripcion,descripcion,fecha,leido,"dd",id);
                                //incidenciasArray.add(act);
                            }

                            tvInciNovistas.setText(count_novistos+"");

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof ServerError) {



                            if (!SharedPrefManager.getInstance(getApplicationContext()).isresponseListadoIncidenciasMapNull()) {


                                String response = SharedPrefManager.getInstance(getApplicationContext()).getresponseListadoIncidenciasMap();


                                try {

                                    JSONArray jsonArray =  new JSONArray(response);
                                    boolean found_notificacion = false;
                                    int count_novistos = 0;
                                    for(int i=0;i<jsonArray.length();i++){
                                        // Get current json object
                                        JSONObject incidencia = jsonArray.getJSONObject(i);
                                        String fecha = incidencia.getString("fecha");
                                        String descripcion=incidencia.getString("titulo");
                                        String leido=incidencia.getString("leido");
                                        String id=incidencia.getString("idIncidencia");
                                        String operador = incidencia.getString("operador");
                                        String notificado = incidencia.getString("notificado");
                                        String tipo_incidencia = incidencia.getString("tipo_incidencia");


                                        if(leido.equals("0")) {
                                            count_novistos++;
                                        }

                                    }

                                    tvInciNovistas.setText(count_novistos+"");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Upps algo anda mal",Toast.LENGTH_SHORT).show();
                            }


                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(),"AuthFailureError",Toast.LENGTH_SHORT).show();
                        }  else if (error instanceof NetworkError) {
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

    public  void getIncidencias2(final String id_usuario, final String fecha) {
/*        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Haciendo Magic...");
        pd.show();*/
        final ArrayList<Incidencia> incidenciasArray = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_GETINCIDENCIAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  pd.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")){

                                String msg = obj.getString("message");
                                Log.e("responsee",msg);
                                JSONArray arrayInc= (JSONArray)obj.get("data");
                                try{
                                    Log.e("array incis",response.length()+"");
                                    // Loop through the array elements

                                    boolean found_notificacion = false;
                                    int count_novistos = 0;
                                    for(int i=0;i<arrayInc.length();i++){
                                        // Get current json object
                                        JSONObject incidencia = arrayInc.getJSONObject(i);
                                        String fecha = incidencia.getString("fecha");
                                        String descripcion=incidencia.getString("detalle");
                                        String leido=incidencia.getString("leido");
                                        String id=incidencia.getString("id");
                                        String ubicacion = incidencia.getString("ubicacion");
                                        String ubicacionArr[] = ubicacion.split(",");
                                        String latitud= ubicacionArr[0];//"-13.5158962";
                                        String longitud= ubicacionArr[1];
                                        String nombres = incidencia.getString("nombres");
                                        String apellidos = incidencia.getString("apellidos");
                                        String celular = incidencia.getString("celular");
                                        int estado = incidencia.getInt("estado");
                                        int notificacion = incidencia.getInt("notificacion");

                                        if(leido.equals("false")) {
                                            count_novistos++;
                                        }

                                        //Aqui encontramos a alguien q ha pulsado el boton de emergencia -> (estado = 1)
                                        //y que todavia no recibio la notificacion -> (notificacion = 0)
                                        //ademas que no haya mostrado(encontrado) la notificacion local
                                        if(estado == 1 && notificacion == 0 && !found_notificacion) { //que se haya registrado una notificacion y que yo no lo haya abierto la notificacion
                                            //sendNotification1(nombres, apellidos, celular);
                                            //si es asi, procedemos a
                                            saveUsuarioIncidecncia2(mUsuarioID+"",id,"0", "1");
                                            found_notificacion = true;
                                        }
                                        //Incidencia act = new Incidencia(latitud,longitud,descripcion,descripcion,fecha,leido,"dd",id);
                                        //incidenciasArray.add(act);
                                    }

                                    tvInciNovistas.setText(count_novistos+"");





                                } catch (JSONException e){
                                    e.printStackTrace();
                                }
                                //   Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                //  finish();

                            } else {
                                String msg = obj.getString("message");

                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                //finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("id_usuario", id_usuario);
                params.put("fecha",fecha);
                //params.put("detalle",detalle);

                return params;
            }
        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }


    public  void guardarNotificado(final String id_usuario, final String id_incidencia) {
        //Toast.makeText(getApplicationContext(), "HELLO", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                ROOT+URL_GUARDAR_NOTIFICADO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  pd.dismiss();

                        try {
                            JSONArray jsonArray =  new JSONArray(response);
                            JSONObject obj = jsonArray.getJSONObject(0);


                            if (!obj.getBoolean("error")){
                                String msg = obj.getString("messsage");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            } else {
                                String msg = obj.getString("messsage");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Sin conexion a internet.", Toast.LENGTH_SHORT).show();
                        // progressDialog.dismiss();
                        /*if (error instanceof TimeoutError || error instanceof NoConnectionError) {
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

                params.put("operador", id_usuario);
                params.put("idIncidencia",id_incidencia);


                return params;
            }
        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }

    public  void guardarLeido(final String id_usuario, final String id_incidencia) {
        //Toast.makeText(getApplicationContext(), "HELLO", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                ROOT+URL_GUARDAR_LEIDO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  pd.dismiss();

                        try {
                            JSONArray jsonArray =  new JSONArray(response);
                            JSONObject obj = jsonArray.getJSONObject(0);


                            if (!obj.getBoolean("error")){
                                String msg = obj.getString("messsage");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            } else {
                                String msg = obj.getString("messsage");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), "Sin conexion a internet.", Toast.LENGTH_SHORT).show();
                        // progressDialog.dismiss();
                        /*if (error instanceof TimeoutError || error instanceof NoConnectionError) {
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

                params.put("operador", id_usuario);
                params.put("idIncidencia",id_incidencia);


                return params;
            }
        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }

    public  void saveUsuarioIncidecncia2(final String id_usuario, final String id_incidencia, final String leido, final String notificacion) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                ROOT+URL_SAVEUSUARIOINCIDENCIA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  pd.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("error")){

                                String msg = obj.getString("message");
                                Log.e("responsesave",msg);
                                updateEstadoIncidencia(mUsuarioID+"", id_incidencia, "1");


                            } else {
                                String msg = obj.getString("message");
                                Log.e("responsesave2",msg);
                                //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                //finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Sin conexion a internet.", Toast.LENGTH_SHORT).show();
                        /*if (error instanceof TimeoutError || error instanceof NoConnectionError) {
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

                params.put("id_usuario", id_usuario);
                params.put("id_incidencia",id_incidencia);
                params.put("leido",leido);
                params.put("notificacion",notificacion);
                //params.put("detalle",detalle);

                return params;
            }
        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }


    public void saveIncidencia(final String id_usuario, final String ubicacion, final String detalle, final String estado) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Haciendo Magic...");
        pd.show();


        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_SAVEINCIDENCIA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")){

                                String msg = obj.getString("message");
                                Log.e("responsee",msg);

                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                //finish();

                            } else {
                                String msg = obj.getString("message");

                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("id_usuario", id_usuario);
                params.put("ubicacion",ubicacion);
                params.put("detalle",detalle);
                params.put("estado",estado);

                return params;
            }
        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }

    public int calcDiff(Date date1, Date date2) {

        int horaPast = date1.getHours();
        int minutosPast = date1.getMinutes();
        int segundosPast = date1.getSeconds();

        int seg_tot_past = horaPast*3600 + minutosPast*60 + segundosPast;

        int horaActual = date2.getHours();
        int minutosActual = date2.getMinutes();
        int segundosActual = date2.getSeconds();

        int seg_tot_actual = horaActual*3600 + minutosActual*60 + segundosActual;

        int diff = Math.abs(seg_tot_actual - seg_tot_past);

        return diff;
    }

    private int count = 0;
    @Override
    public void onBackPressed() {
        //Toast.makeText(getApplicationContext(), "xDD", Toast.LENGTH_LONG).show();
        if(clicked) {
            sl.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            clicked = false;
        } else {
            //super.onBackPressed();
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public void loadImage() {
        String url = "http://infomaz.com/acta_control/header.png";
        Bitmap bm = getBitmapFromURL(url);
        Log.d("myurl", bm.toString());
    }



    private void usuarioLogout(final String id_usuario) {


        progressDialog.setMessage("Cerrando sesion...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                ROOT+Constants.URL_CERRAR_SESION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject obj = jsonArray.getJSONObject(0);


                            if (!obj.getBoolean("error")){
                                SharedPrefManager.getInstance(getApplicationContext()).logoutUsuario();
                                finish();


                                startActivity(new Intent(MapTest.this, LoginActivityJ.class));
                                Toast.makeText(getApplicationContext(),obj.getString("msg"),Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(getApplicationContext(),obj.getString("msg"),Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Sin conexion a internet.", Toast.LENGTH_SHORT).show();
                        /*if (error instanceof TimeoutError || error instanceof NoConnectionError) {
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
                params.put("id",id_usuario);
                return params;
            }
        };


        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_incomes) {
            startActivity(new Intent(this, ActasActivity.class));
        }
        else if(id == R.id.nav_creditos) {
            startActivity(new Intent(this, CreditosActivity.class));
        }
        else if(id == R.id.nav_settings) {
            startActivity(new Intent(this, ConfiguracionActivity.class));
        }
        else if(id == R.id.nav_logout) {
            //Toast.makeText(getApplicationContext(), mUsuarioID + " " + mUsuario + " " + mOperador, Toast.LENGTH_SHORT).show();
            usuarioLogout(mUsuarioID+"");
        } else if(id == R.id.nav_incidencias) {
            /*Intent intent = new Intent(this,
                    IncidenciasActivity.class);
            startActivityForResult(intent , REQUEST_CODE);*/

            Intent paso = new Intent(MapTest.this, IncidenciasActivity.class);

            paso.putExtra("operador", mOperador);

            startActivityForResult(paso , REQUEST_CODE);

            //startActivity(new Intent(this, IncidenciasActivity.class));
        } else if(id == R.id.nav_contenidos) {
            startActivity(new Intent(this, contenido.class));
        }else if(id == R.id.nav_verificar) {
            startActivity(new Intent(this, verificacion_ayudante.class));
        }
        else if(id == R.id.nav_verificar_placa) {
            startActivity(new Intent(this, VerificarPlacaActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.act_main);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(this);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = sdf.format(new Date());
        getIncidencias3(mOperador+"",fecha);




        runEvery15Sec();
        //buildGoogleApiClient();

        mMap.setPadding(0,520,0,0);

        //mMap.setMyLocationEnabled(true);
        //mMap.setOnCameraIdleListener(this);
        // mMap.setOnCameraMoveStartedListener(this);
        //mMap.setOnCameraMoveListener(this);
        //mMap.setOnCameraMoveCanceledListener(this);

    }

    public void sendNotification1(String nombres, String apellidos, String celular, String tipo_incidencia) {
        int NOTIFICATION_ID = 1;
        String ns = Context.NOTIFICATION_SERVICE;

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Get the bitmap to show in notification bar
        Bitmap bitmap_image = BitmapFactory.decodeResource(getResources(), R.drawable.mdlogo);

        Bitmap big_bitmap_image = BitmapFactory.decodeResource(getResources(), R.drawable.mdlogo);


        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle()
                .bigPicture(big_bitmap_image)
                .setSummaryText("Se ha registrado una incidencia.");

        NotificationCompat.Builder nb = new NotificationCompat.Builder(this);

        nb.setContentTitle(tipo_incidencia + " - " + nombres)
                .setContentText("Notification Content")
                .setSmallIcon(R.drawable.icon_notification)
                .setLargeIcon(bitmap_image)
                .setTicker("Registrado por " + nombres + " " + apellidos)
                .setSound(soundUri)
                //API Level min 16 is required
                .setStyle(style)
                .build();


        Intent notificationIntent = new Intent(this, LoginInspectorActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        TaskStackBuilder TSB = TaskStackBuilder.create(this);
        TSB.addParentStack(MainActivity.class);

        TSB.addNextIntent(notificationIntent);
        PendingIntent resultPendingIntent = TSB.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        nb.setContentIntent(resultPendingIntent);
        nb.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(NOTIFICATION_ID, nb.build());
    }

    ArrayList<Marker> listaUsuarios;

    public void removeMarkers() {
        for(int i = 0; i < listaUsuarios.size(); i++) {
            Marker m =  listaUsuarios.get(i);
            m.remove();
            m = null;
        }
    }

    public void getOnlineUsers(final String usuario) {
        //Toast.makeText(getApplicationContext(), "ACG", Toast.LENGTH_SHORT).show();

        if(listaUsuarios != null)
            removeMarkers();

        progressDialog.setMessage("Cargando Inspectores...");
        progressDialog.show();

        listaUsuarios = new ArrayList<Marker>();

        //Toast.makeText(getApplicationContext(), "WTF", Toast.LENGTH_SHORT).show();


        StringRequest stringRequest = new StringRequest(ROOT+Constants.URL_LISTAR_OPERADORES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {


                            progressDialog.dismiss();
                            JSONArray arr = new JSONArray(response);
                            for(int i = 0; i < arr.length(); i++) {
                                JSONObject curr = arr.getJSONObject(i);
                                String id_usuario = curr.getString("codigo");
                                String id_persona = curr.getString("idPersona");
                                String latitud = curr.getString("latitud");
                                String longitud = curr.getString("longitud");

                                if(!latitud.equals("") && !longitud.equals("") && !id_persona.equals(mUsuarioID+"")) {
                                    LatLng ll = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));
                                    Marker m = mMap.addMarker(new MarkerOptions()
                                            .position(ll)
                                            .title(id_usuario)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.manmarker))
                                            .flat(true).anchor(.5f, 0.5f));
                                    listaUsuarios.add(m);
                                }

                            }

                            LatLngBounds.Builder builder = new LatLngBounds.Builder();

                            for (Marker marker : listaUsuarios) {
                                builder.include(marker.getPosition());
                            }

                            builder.include(markerd.getPosition());

                            LatLngBounds bounds = builder.build();

                            int width = getResources().getDisplayMetrics().widthPixels;
                            int height = getResources().getDisplayMetrics().heightPixels;
                            int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

                            mMap.animateCamera(cu);
                            markerd.showInfoWindow();


                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Sin conexion a internet.", Toast.LENGTH_SHORT).show();
            }
        });


        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        // This causes the marker at Perth to bounce into position when it is clicked.



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LatLng coordinate = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 16.5f));
            }
        }, 350);

        final long start = SystemClock.uptimeMillis();
        final long duration = 1500L;

        // Cancels the previous animation
        mHandler.removeCallbacks(mAnimation);

        // Starts the bounce animation
        mAnimation = new MapTest.BounceAnimation(start, duration, marker, mHandler);
        mHandler.post(mAnimation);






        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    private static class BounceAnimation implements Runnable {

        private final long mStart, mDuration;
        private final Interpolator mInterpolator;
        private final Marker mMarker;
        private final Handler mHandler;

        private BounceAnimation(long start, long duration, Marker marker, Handler handler) {
            mStart = start;
            mDuration = duration;
            mMarker = marker;
            mHandler = handler;
            mInterpolator = new BounceInterpolator();
        }

        @Override
        public void run() {
            long elapsed = SystemClock.uptimeMillis() - mStart;
            float t = Math.max(1 - mInterpolator.getInterpolation((float) elapsed / mDuration), 0f);
            mMarker.setAnchor(0.5f, 1.0f + 1.2f * t);

            if (t > 0.0) {
                // Post again 16ms later.
                mHandler.postDelayed(this, 16L);
            }
        }
    }
    void addClickEffect(View view)
    {
        Drawable drawableNormal = view.getBackground();

        Drawable drawablePressed = view.getBackground().getConstantState().newDrawable();
        drawablePressed.mutate();
        drawablePressed.setColorFilter(Color.argb(50, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);

        StateListDrawable listDrawable = new StateListDrawable();
        listDrawable.addState(new int[] {android.R.attr.state_pressed}, drawablePressed);
        listDrawable.addState(new int[] {}, drawableNormal);
        view.setBackground(listDrawable);
    }

    boolean isRunningEvery15secs = false;
    private void runEvery15Sec() {
        if (!isRunningEvery15secs) {
            (new Thread(new Runnable() {

                @Override
                public void run() {
                    while (!Thread.interrupted())
                        try {
                            Thread.sleep(30*1000);
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


    Circle mapCircle;
    private void drawCircle(LatLng point){

        if(mapCircle!=null){
            mapCircle.remove();
        }

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(62);

        int color = Color.parseColor("#DA322B");

        // Border color of the circle
        circleOptions.strokeColor(color);

        // Fill color of the circle
        circleOptions.fillColor(0x220000FF);



        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        mapCircle = mMap.addCircle(circleOptions);

    }



    public String getAddress(Context context, double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);

            String add = obj.getAddressLine(0);
            /*add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();*/

            return add;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public void openSweetAlert(String msj) {
        SweetAlertDialog sweet = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        sweet
                .setTitleText("(GPS) " + msj + " Guardado")
                .setContentText("Gracias por contribuir.")
                .setConfirmText("OK")
                .show();


        sweet.setCanceledOnTouchOutside(true);

        Button btn = (Button) sweet.findViewById(R.id.confirm_button);
        //btn.setBackgroundColor(ContextCompat.getColor(MapsActivity.this,R.color.colorPrimary));

    }


    public void sendSMS(String phoneNo, String msg) {
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

    @Override
    public void onClick(View view) {

        if(view == btnCancelar || view == btnClose) {
            dialogSMS.dismiss();
        }
        else if(view == btnAbrirModal) {
            dialogSMS.show();
        }
        else if(view == btnEnviarSMS) {

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


            String valor = etSMS.getText().toString();
            sendSMS(numero_sms,valor);
            dialogSMS.dismiss();

        }
        else if(view == rg) {
            //clicked = true;
            //sl.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

            //new UploadFile().execute(file_path, file_name);

            //new DownloadFile().execute(file_path, file_name);

            //startActivity(new Intent(MapTest.this, MainGalleryActivity.class));


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



            Intent intent = new Intent(Intent.ACTION_CALL);

            intent.setData(Uri.parse("tel:" + numero_emergencia));
            startActivity(intent);

        } else if(view == direccion) {

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

            textView2.setText(numero_emergencia);
            textView8.setText(numero_sms);

            textView6.setText("Al pulsar el boton de emergencia durante al menos " + tiempo_emergencia + " segundos, ud reportara una emergencia en su posicion actual. se recomienda apretar el boton en algunos casos, como accidentes automovolisticos, robo, asalto o algun incidente de caracter de emergencia");


            if(clicked) {
                clicked = false;
                sl.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                direccion.setImageResource(R.drawable.arriba);
            }
            else {
                clicked = true;
                sl.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                direccion.setImageResource(R.drawable.abajo);
            }


            //String add = getAddress(this, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            //Toast.makeText(getApplicationContext(), add, Toast.LENGTH_SHORT).show();


            //goforIt();

            //new MyTask().execute("Hello World");

            /*SweetAlertDialog pDialog = new SweetAlertDialog(MapTest.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Descargando audios..");
            pDialog.setCancelable(false);
            pDialog.show();*/

            /*ProgressDialog mProgressDialog = new ProgressDialog(getApplicationContext());
            mProgressDialog.setMessage("Descargando audios..");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();*/

            /*progressDialog.setMessage("Haciendo Magia...");
            progressDialog.show();*/

            /*ProgressDialog pg = new ProgressDialog(this);
            pg.setMessage("Haciendo Magia...");
            pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pg.show();*/


            //openSweetAlert("asdasd");

            //new UploadFile().execute();


            //selectImage();


        } else if(view == btnVerFiscas) {

            //loadImage();
            getOnlineUsers(mUsuario);

        } else if(view == imgbtnMenu) {

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.act_main);
            drawer.openDrawer(Gravity.LEFT);

        } else if(view == position) {
            if(markerd!=null) {
                markerd.showInfoWindow();
                LatLng ll = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 16.5f);
                mMap.animateCamera(update);
            }
        } else if(view == btnVerIncidentes) {
            /*Intent intent = new Intent(this,
                    IncidenciasActivity.class);
            startActivityForResult(intent , REQUEST_CODE);*/

            Intent paso = new Intent(MapTest.this, IncidenciasActivity.class);

            paso.putExtra("operador", mOperador);

            startActivityForResult(paso , REQUEST_CODE);

        }
    }


    private void selectImage() {
        final CharSequence[] items = { "Toma una Foto", "Elije una imagen",
                "Cancelar" };
        AlertDialog.Builder builder = new AlertDialog.Builder(MapTest.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=UtilityPhoto.checkPermission(MapTest.this);
                if (items[item].equals("Toma una Foto")) {
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Elije una imagen")) {
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private final static int REQUEST_CAMERA = 10, SELECT_FILE = 11;

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    private void onCaptureImageResult(Intent data) {
        //Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        /*ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, out);*/

        //Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));


        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, out);
        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));



        file_path = System.currentTimeMillis() + ".jpg";
        file_name = System.currentTimeMillis() + ".jpg";

        Log.d("mypath", Environment.getExternalStorageDirectory().toString());
        File destination = new File(Environment.getExternalStorageDirectory(),
                file_path);
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(out.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView.setImageBitmap(decoded);
        imageView.setBackgroundColor(Color.parseColor("#00000000"));
    }


    /**
     * Download a single file from the FTP server
     * @param ftpClient an instance of org.apache.commons.net.ftp.FTPClient class.
     * @param remoteFilePath path of the file on the server
     * @param savePath path of directory where the file will be stored
     * @return true if the file was downloaded successfully, false otherwise
     * @throws IOException if any network or IO error occurred.
     */
    public static boolean downloadSingleFile(FTPClient ftpClient,
                                             String remoteFilePath, String savePath) throws IOException {

        File downloadFile = new File(savePath);



        //File file = new File(Environment.getExternalStorageDirectory(), "Ubus/kid.mp4");



        File parentDir = downloadFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdir();
        }

        OutputStream outputStream = new BufferedOutputStream(
                new FileOutputStream(downloadFile));
        try {
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            return ftpClient.retrieveFile(remoteFilePath, outputStream);
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }



        //File file = new File(Environment.getExternalStorageDirectory(), "Ubus/kid.mp4");
        //con.enterLocalPassiveMode();


        /*con.changeWorkingDirectory("/uploads");
        con.setFileType(FTP.BINARY_FILE_TYPE);

        Log.d("whatis", "ENTREEEE");
        File file = new File(Environment.getExternalStorageDirectory(), "Ubus/kid.mp4");
        con.enterLocalPassiveMode();
        OutputStream outputStream = null;
        boolean success = false;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(
                    file));
            success = con.retrieveFile("tudumi.mp4", outputStream);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }

        if (success) Log.v("whatis", "succeeded");
        //out.close();
        con.logout();
        con.disconnect();

        return true;*/

    }




    /**
     * Download a whole directory from a FTP server.
     * @param ftpClient an instance of org.apache.commons.net.ftp.FTPClient class.
     * @param parentDir Path of the parent directory of the current directory being
     * downloaded.
     * @param currentDir Path of the current directory being downloaded.
     * @param saveDir path of directory where the whole remote directory will be
     * downloaded and saved.
     * @throws IOException if any network or IO error occurred.
     */


    public static void downloadDirectory(FTPClient ftpClient, String parentDir,
                                             String currentDir, String saveDir) throws IOException {
        String dirToList = parentDir;
        if (!currentDir.equals("")) {
            dirToList += "/" + currentDir;
        }

        FTPFile[] subFiles = ftpClient.listFiles(dirToList);

        if (subFiles != null && subFiles.length > 0) {
            for (FTPFile aFile : subFiles) {
                String currentFileName = aFile.getName();

                if (currentFileName.equals(".") || currentFileName.equals("..")) {
                    // skip parent directory and the directory itself
                    continue;
                }

                String filePath = parentDir + "/" + currentDir + "/"
                        + currentFileName;
                if (currentDir.equals("")) {
                    filePath = parentDir + "/" + currentFileName;
                }



                String newDirPath = saveDir + parentDir + File.separator
                        + currentDir + File.separator + currentFileName;
                if (currentDir.equals("")) {
                    newDirPath = saveDir + parentDir + File.separator
                            + currentFileName;
                }

                Log.d("whatis", filePath + " ** " + newDirPath);



                if (aFile.isDirectory()) {
                    // create the directory in saveDir
                    Log.d("whatis", "ESTE ES UN DIRECTORIO");
                    File newDir = new File(newDirPath);
                    boolean created = newDir.mkdirs();
                    if (created) {
                        System.out.println("CREATED the directory: " + newDirPath);
                    } else {
                        System.out.println("COULD NOT create the directory: " + newDirPath);
                    }

                    // download the sub directory
                    downloadDirectory(ftpClient, dirToList, currentFileName,
                            saveDir);
                } else {
                    //Log.d("whatis", "ESTE ES UN ARCHIVO");
                    // download the file
                    boolean success = downloadSingleFile(ftpClient, filePath,
                            newDirPath);
                    if (success) {
                        System.out.println("DOWNLOADED the file: " + filePath);
                    } else {
                        System.out.println("COULD NOT download the file: "
                                + filePath);
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        //Log.d("mypath",data.getData());
        //Log.d("mypath", getPathFromURI(data.getData()));
        file_path = getPathFromURI(data.getData());
        file_path = file_path.substring(19, file_path.length());
        Log.d("mypath",  file_path);
        file_name = System.currentTimeMillis() + ".jpg";
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imageView.setImageBitmap(bm);
        imageView.setBackgroundColor(Color.parseColor("#00000000"));
    }
    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public static final int REQUEST_CODE = 2;
    Marker markerIncidencia;



    public  void saveUsuarioIncidecncia3(final String id_usuario, final String id_incidencia, final String leido, final String notificacion) {
/*        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Haciendo Magic...");
        pd.show();*/
        final ArrayList<Incidencia> incidenciasArray = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_GUARDAR_LEIDO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  pd.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("error")){

                                String msg = obj.getString("message");
                                Log.e("responsesave",msg);




                            } else {
                                String msg = obj.getString("message");
                                Log.e("responsesave2",msg);
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                //finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("operador", id_usuario);
                params.put("idIncidencia",id_incidencia);
                /*params.put("leido",leido);
                params.put("notificacion",notificacion);*/
                //params.put("detalle",detalle);

                return params;
            }
        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }


    public class DownloadingParaderos extends AsyncTask<String, Integer, String> {
        private ProgressDialog mProgressDialog;
        private SweetAlertDialog pDialog;
        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //btnDownload.setText("Descargando...");

            /*mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setMessage("Descargando audios..");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();*/

            pDialog = new SweetAlertDialog(MapTest.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Descargando Imagenes..");
            pDialog.setCancelable(false);
            pDialog.show();


//            buttonText.setEnabled(false);
//            btnDownload.setText("Donload Started...");//Set Button Text when download started
        }



        @Override
        protected void onPostExecute(String result) {
            try {
                if (outputFile != null) {
//                    buttonText.setEnabled(true);
                    //btnDownload.setText("Descarga Completa");
                    pDialog.dismissWithAnimation();

                    Toast.makeText(getApplicationContext(), "Descarga Correcta", Toast.LENGTH_SHORT).show();


                    //btnDownload.setText("Completed");//If Download completed then change button text
                } else {
                    //btnDownload.setText("Failed to Download");//If download failed change button text
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            buttonText.setEnabled(true);
                            //      btnDownload.setText("Download Again");//Change button text again after 3sec
                        }
                    }, 3000);

                    Log.e("downloadtask", "Download Failed");

                }
            } catch (Exception e) {
                e.printStackTrace();

                //Change button text if exception occurs
                //btnDownload.setText("Download Failed");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        buttonText.setEnabled(true);
                        //      btnDownload.setText("Download Again");
                    }
                }, 3000);
                Log.e("downloadtask", "Download Failed with Exception - " + e.getLocalizedMessage());

            }




            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // mProgressDialog.setProgress(values[0]);
            //mProgressDialog.incrementProgressBy(1);
            //super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                //Toast.makeText(getApplicationContext(), "ENTRE", Toast.LENGTH_SHORT).show();
                JSONObject obj = null;
                String audioPath = params[0];//"https://www.ubusgo.com/driver/voices/paraderos/";
                try {


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



                        obj = jsonArray.getJSONObject(0);
                        tiempo_anulacion = obj.getString("tiempo_anulacion");
                        tiempo_emergencia = obj.getString("tiempo_emergencia");
                        url_upload = obj.getString("url_upload");
                        usuario_ftp = obj.getString("usuario_ftp");
                        pw_ftp = obj.getString("pw_ftp");
                        numero_sms = obj.getString("numero_sms");
                        numero_emergencia = obj.getString("numero_emergencia");
                        numero_emergencia = obj.getString("numero_emergencia");
                        acta_header = obj.getString("acta_header");
                        acta_footer = obj.getString("acta_footer");
                        educativa_footer = obj.getString("educativa_footer");

                        lista.add(acta_header);
                        lista.add(acta_footer);
                        lista.add(educativa_footer);

                        //Toast.makeText(getApplicationContext(), tiempo_anulacion, Toast.LENGTH_SHORT).show();



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // obj = new JSONObject(SharedPrefManager.getInstance(getApplicationContext()).getAllParaderos());
                    //Log.d(TAG, "DIRECCION " + obj.get("direccion"));










                    //mProgressDialog.setMax(arrayParaderos.length());

                    //Toast.makeText(getApplicationContext(), arrayParaderos.length() + " tam ", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < lista.size(); i++) {
                        // obj = arrayParaderos.getJSONObject(i);

                        String slug = "2";//obj.getString("Slug");
                        String aurl = audioPath+slug+".mp3";
                        String fname = arrStr.get(i);//slug+".mp3";


                        aurl = lista.get(i);
                        Log.d("aurl", aurl);

                        //mProgressDialog.setMessage("Downloading file.. " + fname);

                        URL url = new URL(aurl);//Create Download URl
                        HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                        c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                        c.connect();//connect the URL Connection

                        int lenghtOfFile = c.getContentLength();

                        //If Connection response is not OK then show Logs
                        if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                            Log.e("aurl", "Server returned HTTP " + c.getResponseCode()
                                    + " " + c.getResponseMessage());

                        } else {


                            File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Fiscapp/");
                            outputFile = new File(mediaStorageDir, fname);//Create Output file in Main File

                            //Create New File if not present
                            if (!outputFile.exists()) {
                                outputFile.createNewFile();
                                Log.e("aurl", "File Created");
                            }

                            FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                            InputStream is = c.getInputStream();//Get InputStream for connection

                            byte[] buffer = new byte[1024];//Set buffer type
                            int len1 = 0;//init length


                            long total = 0;

                            while ((len1 = is.read(buffer)) != -1) {
                                fos.write(buffer, 0, len1);//Write new file
                                total += len1;
                                int progress_temp = (int) total * 100 / lenghtOfFile;
                                //publishProgress(progress_temp);


                                // publishProgress(String.valueOf(total /1000));
                                // publishProgress((int) ((total * 100)/lenghtOfFile));
                            }


                            //Close all connection after doing task
                            fos.close();
                            is.close();
                        }

                    }
//
                } catch (Exception e){

                    pDialog.dismissWithAnimation();
                    e.printStackTrace();
                }

            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
                Log.e("aurl", "Download Error Exception " + e.getMessage());
            }

            return null;
        }
    }

    public String getS(String str){
        return str;
        /*String out = null;
        try {
            out = new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;*/
    }
    //new MainActivity.DownloadingParaderos().execute("https://www.ubusgo.com/driver/voices/paraderos/");


}