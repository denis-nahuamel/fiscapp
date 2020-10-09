package com.fiscapp.fiscapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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
import com.fiscapp.fiscapp.Model.cController;
import com.fiscapp.fiscapp.Model.cPersona;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ConfiguracionActivity extends Activity implements View.OnClickListener {

    ArrayList<String> arrStr = new ArrayList<>();
    String imgNameHeader = "header.png";
    String imgNameFooter = "footer.png";
    String imgNameEducativa = "educativa_footer.png";
    ImageView atras;
    Button btnDescargarImg, btnImportarDatos;

    final Context context = this;
    cController c = new cController(context);
    String ROOT = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        String RootResp = SharedPrefManager.getInstance(this).getDominio();

        try {
            JSONArray jsonArray =  new JSONArray(RootResp);
            JSONObject obj = jsonArray.getJSONObject(0);
            ROOT = obj.getString("dominio") + "?var=";
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnDescargarImg = (Button) findViewById(R.id.btnDescargarImg);
        btnImportarDatos = (Button) findViewById(R.id.btnImportarDatos);
        atras = (ImageView) findViewById(R.id.atras);

        arrStr.add(imgNameHeader);
        arrStr.add(imgNameFooter);
        arrStr.add(imgNameEducativa);

        btnDescargarImg.setOnClickListener(this);
        btnImportarDatos.setOnClickListener(this);
        atras.setOnClickListener(this);

    }


    public  void Load() {
        final RequestQueue queue = Volley.newRequestQueue(this);
        final ProgressDialog progressDialog = new ProgressDialog(this);
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

                                boolean createSuccessful = new cController(getApplicationContext()).create(persona);
                                if(createSuccessful) cnt++;

                            }

                            Toast.makeText(getApplicationContext(), "Total datos importados : " + cnt, Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();

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
                }*/
            }
        });

        queue.add(stringRequest);

    }

    public void openAlertDescargarAudios() {
        final SweetAlertDialog sweet = new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        sweet
                .setTitleText("Descarga de imagenes")
                .setContentText("Vamos a descargar imagenes para la impresion.")
                .setCancelText("NO")
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

                new ConfiguracionActivity.DownloadingParaderos().execute("");
            }
        });



        sweet.setCanceledOnTouchOutside(true);

        Button btnConf = (Button) sweet.findViewById(R.id.confirm_button);
        btnConf.setBackgroundColor(ContextCompat.getColor(ConfiguracionActivity.this,R.color.success_stroke_color));

        Button btnCanc = (Button) sweet.findViewById(R.id.cancel_button);
        btnCanc.setBackgroundColor(ContextCompat.getColor(ConfiguracionActivity.this,R.color.red_btn_bg_pressed_color));
    }

    @Override
    public void onClick(View view) {
        if(view == btnDescargarImg) {
            openAlertDescargarAudios();

        } else if(view == atras) {
            finish();
        } else if(view == btnImportarDatos) {
            ConnectivityManager conn = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = conn.getActiveNetworkInfo();

            if(networkInfo != null && networkInfo.isConnected()){

                Load();
            }
            else{

                Toast.makeText(getApplicationContext(), "Sin conexion a internet.", Toast.LENGTH_SHORT).show();
            }

        }
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

            pDialog = new SweetAlertDialog(ConfiguracionActivity.this, SweetAlertDialog.PROGRESS_TYPE);
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

}
