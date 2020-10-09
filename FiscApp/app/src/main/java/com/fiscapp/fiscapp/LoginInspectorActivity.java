package com.fiscapp.fiscapp;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.net.ConnectivityManagerCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;



/**
 * Created by Willy on 10/4/2018.
 */

public class LoginInspectorActivity extends Activity implements View.OnClickListener {

    Button btnLogin;
    //CircularProgressButton btn;
    EditText txdUser, txdPass ;

    // permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    private static final long INTERVAL = 1000 * 5;
    private long FASTEST_INTERVAL = 1000; /* 1 sec */

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        verify();
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        buttonEffect(btnLogin);
        txdUser = (EditText) findViewById(R.id.txdUser);
        txdPass = (EditText) findViewById(R.id.txdPass);
        progressDialog = new ProgressDialog(this);
        requestPermissions();
    }


    private void requestPermissions() {
        Log.w("tag", "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.SEND_SMS};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }


        Toast.makeText(getApplicationContext(), "Camera not granted 2", Toast.LENGTH_SHORT).show();
        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };
    }

    /*public void openSweetAlert(String msj) {
        SweetAlertDialog sweet = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        sweet
                .setTitleText("(GPS) " + msj + " Guardado")
                .setContentText("Gracias por contribuir.")
                .setConfirmText("OK")
                .show();

        sweet.setCanceledOnTouchOutside(true);

        //Button btn = (Button) sweet.findViewById(R.id.confirm_button);
       // btn.setBackgroundColor(ContextCompat.getColor(MapsActivity.this,R.color.colorPrimary));

    }*/



    public void sendNotification1() {
        int NOTIFICATION_ID = 1;
        String ns = Context.NOTIFICATION_SERVICE;

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Get the bitmap to show in notification bar
        Bitmap bitmap_image = BitmapFactory.decodeResource(getResources(), R.drawable.mdlogo);

        Bitmap big_bitmap_image = BitmapFactory.decodeResource(getResources(), R.drawable.mdlogo);


        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle()
                //.bigPicture(big_bitmap_image)
                .setSummaryText("Se ha registrado una incidencia por Av El Sol.");

        NotificationCompat.Builder nb = new NotificationCompat.Builder(this);

        nb.setContentTitle("Emergencia")
            .setContentText("Notification Content")
                .setSmallIcon(R.drawable.icon_notification)
                .setLargeIcon(bitmap_image)
                .setTicker("Notification ticker!")
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

    public void sendNotification2() {
        Intent intent = new Intent(this, LoginInspectorActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(this);

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.icon_notification)
                .setTicker("Hearty365")
                .setContentTitle("Default notification")
                .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("Info");


        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
    }

    public void sendNotification3() {
        Intent notificationIntent = new Intent(this,
                LoginInspectorActivity.class);



        notificationIntent.putExtra("clicked", "Notification Clicked");
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP); // To open only one activity

        Bitmap bitmap_image = BitmapFactory.decodeResource(getResources(), R.drawable.mdlogo);

        // Invoking the default notification service

        NotificationManager mNotificationManager;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this);
        Uri uri = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setContentTitle("Reminder");
        mBuilder.setContentText("Atencion, Emergencia!!.");
        mBuilder.setTicker("New Reminder Alert!");
        mBuilder.setSmallIcon(R.drawable.icon_notification);
        mBuilder.setSound(uri);
        mBuilder.setAutoCancel(true);
        mBuilder.setLargeIcon(bitmap_image);

        // Add Big View Specific Configuration
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        String[] events = new String[2];

        events[0] = new String("Se ha registrado una incidencia");
        events[1] = new String("Tener cuidado y avisar a los demas.");



        // Sets a title for the Inbox style big view
        inboxStyle.setBigContentTitle("Atencion, Emergencia!!.");

        // Moves events into the big view
        for (int i = 0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }

        mBuilder.setStyle(inboxStyle);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this,
                LoginInspectorActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder
                .create(this);
        stackBuilder.addParentStack(LoginInspectorActivity.class);


        // Adds the Intent that starts the Activity to the top of the stack


        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder
                .getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);


        // notificationID allows you to update the notification later  on.


        mNotificationManager.notify(999, mBuilder.build());
    }

    @Override
    public void onClick(View view) {
        if(view == btnLogin) {
            String usuario = txdUser.getText().toString();
            String password = txdPass.getText().toString();
            usuarioLogin(usuario, password);
        }
    }




    public void sendNotification4() {
        String title, message, img_urlSmall, img_urlBig;
        title = "Titulo";
        message = "Mensaje";
        img_urlBig = "BigImage";
        img_urlSmall = "Small Image";

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Toast.makeText(getApplicationContext(), img_url, Toast.LENGTH_SHORT).show();
        Bitmap bitmap = getBitmapfromUrl(img_urlBig);
        Bitmap bitmap1 = getBitmapfromUrl(img_urlSmall);

        Intent likeIntent = new Intent(this,LoginInspectorActivity.class);
        //likeIntent.putExtra(NOTIFICATION_ID_EXTRA,0);
        //likeIntent.putExtra(IMAGE_URL_EXTRA,remoteMessage.getData().get("image-url"));
        PendingIntent likePendingIntent = PendingIntent.getService(this,0,likeIntent,PendingIntent.FLAG_ONE_SHOT);


        final NotificationCompat.Builder not = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_power_button)
                .setLargeIcon(bitmap1)  //set it in the notification
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .setSummaryText("Summary Message")
                        .bigPicture(bitmap))
                .setStyle(new  NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri)
                .addAction(R.drawable.icon_setting,
                        "Me gusta",likePendingIntent)
                .addAction(R.drawable.icon_share,
                        "Compartir",likePendingIntent)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, not.build());
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void getIncidencias() {
        String h = "http://infomaz.com/?var=listar_incidencias&fecha=2018-10-08&usuario=JPERES";
        RequestQueue queue = Volley.newRequestQueue(this);
        Toast.makeText(getApplicationContext(),"OLI", Toast.LENGTH_SHORT).show();
        //final ArrayList<Incidencia> incidencias = new ArrayList<>();
        final StringRequest stringRequest = new StringRequest(h,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            /*Log.e("aqui pues",response);
                            JSONArray arr = new JSONArray(response);
                            String fechaIncidencia, descripcion, placa;
                            String TODO = "";
                            Toast.makeText(getApplicationContext(),"JAJA", Toast.LENGTH_SHORT).show();*/
                            /*for(int i = 0; i < arr.length(); i++){
                                JSONObject currentObj = arr.getJSONObject(i);
                                fechaIncidencia = currentObj.getString("fecha_incidencia");
                                descripcion = currentObj.getString("descripcion");
                                String ruta = currentObj.getString("incidencia");

                            }*/


                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"HAHAH", Toast.LENGTH_SHORT).show();
                            Log.e("error1",response);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),"XDXDXD", Toast.LENGTH_SHORT).show();
                Log.e("error2","adsfafsadfads");
            }
        });
        // return incidencias;
        queue.add(stringRequest);
    }

    private void Listar_empresas_transportes(){
        RequestQueue queue = Volley.newRequestQueue(this);

        // new JsonTask_GetPathFromGoogle().execute("https://roads.googleapis.com/v1/snapToRoads?path="+ruta+"&interpolate=true&key="+key);
        //String urlPath = "https://roads.googleapis.com/v1/snapToRoads?path="+ruta+"&interpolate=true&key="+key;
        String h = "http://infomaz.com/?var=listar_incidencias&fecha=2018-10-08&usuario=JPERES";
        StringRequest stringRequest = new StringRequest(h,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray arr = new JSONArray(response);
                            JSONObject currentObj = arr.getJSONObject(0);
                            String incidencia = currentObj.getString("incidencia");
                            String TODO = "";
                            Toast.makeText(LoginInspectorActivity.this, incidencia, Toast.LENGTH_SHORT).show();
                            /*for(int i = 0; i < arr.length(); i++){
                                JSONObject currentObj = arr.getJSONObject(i);

                                String id = currentObj.getString("Empresa_Id");
                                String razon_social = currentObj.getString("Empresa_Rsocial");
                                String ruta = currentObj.getString("Empresa_Ruta");

                                TODO += id + "\n" + razon_social + "\n" + ruta +"\n________\n";
                            }*/


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(stringRequest);
    }

    public static void buttonEffect(View button){
        button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0a0aa1d, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                v.getBackground().clearColorFilter();
                                v.invalidate();
                            }
                        }, 50L);
                        break;
                    }
                }
                return false;
            }
        });
    }

    BroadcastReceiver connectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equals(ConnectivityManager
                    .CONNECTIVITY_ACTION)) {

                boolean isConnected = ConnectivityManagerCompat.getNetworkInfoFromBroadcast
                        ((ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE),
                                intent).isConnected();

                if (isConnected) {
                    onConnectionEstablished();
                } else {
                    onConnectionAbsent();
                }
            }
        }
    };

    public void onConnectionEstablished() {
        // do something
        Toast.makeText(this, "Connection established", Toast.LENGTH_SHORT).show();
    }

    public void onConnectionAbsent() {
        // do something
        Toast.makeText(this, "Connection Absent", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(connectionReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    private void verify(){
        if (SharedPrefManager.getInstance(this).isUsuarioLoggedIn()){
            startActivity(new Intent(getApplicationContext(), MapActivity.class));
        }
    }

    private void usuarioLogin(final String usuario, final String password) {


        progressDialog.setMessage("Haciendo Magia...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_USUARIOLOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")){

                                JSONObject userObj = obj.getJSONObject("datalogin");

                                int userID = userObj.getInt("id");
                                String usuario = userObj.getString("usuario");
                                String password = userObj.getString("password");
                                String login = userObj.getString("login");
                                String uid = userObj.getString("uid");
                                String device = userObj.getString("device");
                                String nombres = userObj.getString("nombres");
                                String apellidos = userObj.getString("apellidos");
                                String celular = userObj.getString("celular");




                                Toast.makeText(getApplicationContext(),"Bienvenido " + nombres ,Toast.LENGTH_SHORT).show();

                                SharedPrefManager.getInstance(getApplicationContext()).usuarioLogin(
                                        userID,
                                        usuario,
                                        nombres,
                                        apellidos,
                                        celular
                                );

                                startActivity(new Intent(getApplicationContext(), MapActivity.class));

                            } else {
                                if (obj.getString("message").toString().length() > 20){
                                    Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(),"Usted ya esta usando ubus en otro dispositivo: " ,Toast.LENGTH_SHORT).show();
                                }
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
                String android_id = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                String namedivice = Build.MANUFACTURER + " - " + Build.MODEL;
                params.put("usuario",usuario);
                params.put("password",password);
                params.put("uid",android_id);
                params.put("device",namedivice);
                return params;
            }
        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }


    public void saveIncidencia(final String id_usuario, final String ubicacion, final String detalle) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Haciendo Magic...");
        pd.show();

        String URL_SAVEINCIDENCIA = "http://www.ubusgo.com/driver/actions/" + "saveIncidencia.php";
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
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();


                            } else {
                                String msg = obj.getString("message");
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

                params.put("id_usuario", id_usuario);
                params.put("ubicacion",ubicacion);
                params.put("detalle",detalle);

                return params;
            }
        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }




}
