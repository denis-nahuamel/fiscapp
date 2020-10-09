package com.fiscapp.fiscapp;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

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
import com.google.android.gms.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.fiscapp.fiscapp.Constants.URL_GETINCIDENCIAS;
import static com.fiscapp.fiscapp.Constants.URL_SAVEINCIDENCIA;
import static com.fiscapp.fiscapp.Constants.URL_SAVEUSUARIOINCIDENCIA;
import static com.fiscapp.fiscapp.Constants.URL_UPDATEESTADOINCIDENCIA;
import static com.fiscapp.fiscapp.MainActivity.REQUEST_CHECK_SETTINGS;

public class MapActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener,
        LocationListener, OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

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


    private static final long INTERVAL = 1000 * 5;
    private long FASTEST_INTERVAL = 1000 * 1; /* 1 sec */
    PendingResult<LocationSettingsResult> result;
    final static int REQUEST_LOCATION = 199;

    private ImageButton btnVerFiscas;
    private ProgressDialog progressDialog;

    private ImageButton imgbtnMenu, imgbtnEmergencia, btnVerIncidentes;

    private ImageView position;

    private String  mUsuario;

    private int mUsuarioID;

    private  Handler mHandler;
    private Runnable mAnimation;

    Date d1, d2;

    TextView counter, etLugar, tvInciNovistas;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnEmergencia = (TextView) findViewById(R.id.btnEmergencia);
        addClickEffect(btnEmergencia);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        rg = (Button) findViewById(R.id.registrarBus);

        rg.setOnClickListener(this);

        sl = (SlidingUpPanelLayout) findViewById(R.id.sliding);

        imgbtnEmergencia = (ImageButton) findViewById(R.id.imgbtnEmergencia);

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
                        c = 0;
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
                        if(c >= 3) {
                            String ubicacion = mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude();
                            saveIncidencia(mUsuarioID+"", ubicacion, "EMERGENCIA", "1");
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

        sl.setDragView(btnEmergencia);
        btnEmergencia.setOnClickListener(this);


        createLocationRequest();
        activarGPS();

        mUsuarioID = SharedPrefManager.getInstance(getApplicationContext()).getUserKeyId();
        mUsuario = SharedPrefManager.getInstance(getApplicationContext()).getUserKeyUsuario();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);


        //count();

    }

    boolean canceled = false;

    int c = 0;
    public void count() {

        if(canceled) return;


        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
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
                }, 1000);

            }
        });

    }

    public void updateEstadoIncidencia(final String id_usuario, final String id_incidencia, final String notificacion) {
        final ArrayList<Incidencia> incidenciasArray = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_UPDATEESTADOINCIDENCIA,
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
                params.put("id_incidencia",id_incidencia);
                params.put("notificacion",notificacion);
                return params;
            }
        };

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
                                            sendNotification1(nombres, apellidos, celular);
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

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    protected void createLocationRequest() {
        //Toast.makeText(this, "CREATE LOCATION REQUEST", Toast.LENGTH_SHORT).show();
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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

    public void activarGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);


        progressDialog = new ProgressDialog(this);


        btnEmergencia = (TextView) findViewById(R.id.btnEmergencia);
        btnEmergencia.setOnClickListener(this);

      //  btnVerFiscas = (ImageButton) findViewById(R.id.btnVerFiscas);

        btnVerFiscas.setOnClickListener(this);

        imgbtnMenu = (ImageButton) findViewById(R.id.imgbtnMenu);
        position = (ImageView) findViewById(R.id.position);

        position.setOnClickListener(this);
        imgbtnMenu.setOnClickListener(this);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                //Toast.makeText(getApplicationContext(), "NOSE1", Toast.LENGTH_SHORT).show();
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        //Toast.makeText(getApplicationContext(), "NOSE2", Toast.LENGTH_SHORT).show();
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MapActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                        //Toast.makeText(getApplicationContext(), "NOSE3", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }





    private void usuarioLogout(final String usuario) {


        progressDialog.setMessage("Haciendo Magia...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_USUARIOLOGOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")){
                                SharedPrefManager.getInstance(getApplicationContext()).logoutUsuario();
                                finish();
                                startActivity(new Intent(getApplicationContext(), LoginInspectorActivity.class));
                                Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();

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
                params.put("usuario",usuario);
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
        else if(id == R.id.nav_logout) {
            usuarioLogout(mUsuario);
        } else if(id == R.id.nav_incidencias) {
            Intent intent = new Intent(this,
                    IncidenciasActivity.class);
            startActivityForResult(intent , REQUEST_CODE);

            //startActivity(new Intent(this, IncidenciasActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.act_main);
        drawer.closeDrawer(GravityCompat.START);

        return true;
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

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(this);

        runEvery15Sec();
        buildGoogleApiClient();

        mMap.setPadding(0,520,0,0);

        //mMap.setMyLocationEnabled(true);
        //mMap.setOnCameraIdleListener(this);
       // mMap.setOnCameraMoveStartedListener(this);
        //mMap.setOnCameraMoveListener(this);
        //mMap.setOnCameraMoveCanceledListener(this);

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

                                    getIncidencias2(mUsuarioID+"",fecha);
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
        circleOptions.radius(45);

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



    public void bouncerMarker(Marker marker) {
        // This causes the marker at Perth to bounce into position when it is clicked.
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500L;

        // Cancels the previous animation
        mHandler.removeCallbacks(mAnimation);

        // Starts the bounce animation
        mAnimation = new BounceAnimation(start, duration, marker, mHandler);
        mHandler.post(mAnimation);
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
    }



    @Override
    public boolean onMarkerClick(Marker marker) {
        // This causes the marker at Perth to bounce into position when it is clicked.
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500L;

        // Cancels the previous animation
        mHandler.removeCallbacks(mAnimation);

        // Starts the bounce animation
        mAnimation = new BounceAnimation(start, duration, marker, mHandler);
        mHandler.post(mAnimation);
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public void onClick(View view) {
        if(view == rg) {
            //clicked = true;
            sl.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else if(view == btnEmergencia) {
            sl.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            clicked = true;

            String add = getAddress(this, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            Toast.makeText(getApplicationContext(), add, Toast.LENGTH_SHORT).show();
        } else if(view == btnVerFiscas) {
            getOnlineUsers(mUsuario);
        } else if(view == imgbtnMenu) {

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.act_main);
            drawer.openDrawer(Gravity.LEFT);

        } else if(view == position) {
            if(markerd!=null) {
                markerd.showInfoWindow();
                LatLng ll = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 18);
                mMap.animateCamera(update);
            }
        } else if(view == btnVerIncidentes) {
            Intent intent = new Intent(this,
                    IncidenciasActivity.class);
            startActivityForResult(intent , REQUEST_CODE);

        }
    }


    public static final int REQUEST_CODE = 2;
    Marker markerIncidencia;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d("onActivityResult()", Integer.toString(resultCode));

        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode)
        {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                    {
                        // All required changes were successfully made
                        Toast.makeText(MapActivity.this, "El GPS esta activado", Toast.LENGTH_LONG).show();
                        break;
                    }
                    case Activity.RESULT_CANCELED:
                    {
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(MapActivity.this, "El GPS no esta activado", Toast.LENGTH_LONG).show();
                        //activarGPS();
                        break;
                    }
                    default:
                    {
                        break;
                    }
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
                        String id=incidencia.getId();
                        String leido=incidencia.getLeido();
                        String notificacion =  incidencia.getNotificacion();

                        markerd.showInfoWindow();
                        LatLng ll = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));
                        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 18);
                        mMap.animateCamera(update);

                        if(markerIncidencia != null) {
                            markerIncidencia.remove();
                            markerIncidencia = null;
                        }
                         markerIncidencia = mMap.addMarker(new MarkerOptions()
                                .position(ll).title(descripcion)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.warning16))
                                .flat(true).anchor(.5f, 0.5f));


                        markerIncidencia.showInfoWindow();




                        if(leido.equals("false")) { //no esta leido
                            saveUsuarioIncidecncia(mUsuarioID+"",id,"1", notificacion);
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


    public  void saveUsuarioIncidecncia(final String id_usuario, final String id_incidencia, final String leido, final String notificacion) {
/*        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Haciendo Magic...");
        pd.show();*/
        final ArrayList<Incidencia> incidenciasArray = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_SAVEUSUARIOINCIDENCIA,
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


    public  void saveUsuarioIncidecncia2(final String id_usuario, final String id_incidencia, final String leido, final String notificacion) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_SAVEUSUARIOINCIDENCIA,
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

    Marker markerd;
    boolean first = true;
    boolean firstShowInfo  = true;
    @Override
    public void onLocationChanged(Location location) {

        //Location myCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        mCurrentLocation = location;
        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 18);
        if(first)
        mMap.animateCamera(update);
        first = false;
        //mMap.addMarker(new MarkerOptions().title("xd").position(ll));

        etLugar.setText(getAddress(this, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));

        if(markerd == null) {
            markerd = mMap.addMarker(new MarkerOptions()
                    .position(ll).
                    title(mUsuario)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.manmarker))
                    .flat(true).anchor(.5f, 0.5f));
            if(firstShowInfo)
                markerd.showInfoWindow();
            firstShowInfo = false;

        } else {
            markerd.setPosition(ll);
            Toast.makeText(getApplicationContext(), mUsuarioID+"", Toast.LENGTH_SHORT).show();
            saveUserLocation(mUsuarioID+"", location.getLatitude() + "," + location.getLongitude());
        }



        LatLng correctLocation = null;

    }



    private void saveUserLocation(final String usuario, final String location){
        //Toast.makeText(getApplicationContext(), "ACG", Toast.LENGTH_SHORT).show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_UPDATEUSERLOCATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getApplicationContext(),"tmr",Toast.LENGTH_LONG).show();
                        try {
                            JSONObject obj = new JSONObject(response);
                            //Toast.makeText(getApplicationContext(),"SE GUARDO" ,Toast.LENGTH_LONG).show();
                            //Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_LONG).show();
                            if (!obj.getBoolean("error")){

                            } else {
                                //Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
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
                params.put("usuario", usuario);
//                params.put("location", mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude());
                params.put("location", location);

                return params;
            }
        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }

    public void sendNotification1(String nombres, String apellidos, String celular) {
        int NOTIFICATION_ID = 1;
        String ns = Context.NOTIFICATION_SERVICE;

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Get the bitmap to show in notification bar
        Bitmap bitmap_image = BitmapFactory.decodeResource(getResources(), R.drawable.mdlogo);

        Bitmap big_bitmap_image = BitmapFactory.decodeResource(getResources(), R.drawable.mdlogo);


        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle()
                .bigPicture(big_bitmap_image)
                .setSummaryText("Se ha registrado una incidencia por Av El Sol.");

        NotificationCompat.Builder nb = new NotificationCompat.Builder(this);

        nb.setContentTitle("Emergencia - " + nombres)
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


    @Override
    public void onConnected(Bundle bundle) {
        //Toast.makeText(this, "CONNECTED", Toast.LENGTH_SHORT).show();
        startLocationUpdates();
        //runEvery5Sec();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mGoogleApiClient != null)
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient != null &&mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
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

        progressDialog.setMessage("Haciendo Magia...");
        progressDialog.show();

        listaUsuarios = new ArrayList<Marker>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_GETONLINEUSERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getApplicationContext(),"tmr",Toast.LENGTH_LONG).show();
                        try {
                            JSONObject obj = new JSONObject(response);

                            progressDialog.dismiss();
                            //Toast.makeText(getApplicationContext(),"SE GUARDO" ,Toast.LENGTH_LONG).show();
                            //Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_LONG).show();
                            if (!obj.getBoolean("error")){
                                JSONArray arr = obj.getJSONArray("data");

                                for(int i = 0; i < arr.length(); i++) {
                                    JSONObject curr = arr.getJSONObject(i);
                                    String id_usuario = curr.getString("id_usuario");
                                    String posicion = curr.getString("posicion");
                                    String posArr[] = posicion.split(",");
                                    LatLng ll = new LatLng(Double.parseDouble(posArr[0]), Double.parseDouble(posArr[1]));
                                    Marker m = mMap.addMarker(new MarkerOptions()
                                            .position(ll)
                                            .title(id_usuario)
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.manmarker))
                                            .flat(true).anchor(.5f, 0.5f));

                                    //m.showInfoWindow();
                                    listaUsuarios.add(m);

                                }

                                /*LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                for (Marker marker : listaUsuarios) {
                                    builder.include(marker.getPosition());
                                }
                                LatLngBounds bounds = builder.build();
                                int padding = 0; // offset from edges of the map in pixels
                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                                mMap.animateCamera(cu);*/


                                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                                //the include method will calculate the min and max bound.
                                /*builder.include(marker1.getPosition());
                                builder.include(marker2.getPosition());
                                builder.include(marker3.getPosition());
                                builder.include(marker4.getPosition());*/

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

                            } else {
                                //Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Variables que necesita el PHP, deben ser iguales al PHP
                params.put("usuario", usuario);

                return params;
            }
        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
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

}
