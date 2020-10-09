package com.fiscapp.fiscapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.fiscapp.fiscapp.Model.Incidencia;
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

import static com.fiscapp.fiscapp.Constants.ROOT_URL_JAIRO;
import static com.fiscapp.fiscapp.Constants.URL_LISTAR_CONFIGURACION;


public class SplashActivity extends Activity {

    ImageView imglogocusco, imgIcono;
    LottieAnimationView loading2;

    ArrayList<String> arrStr = new ArrayList<>();
    String imgNameHeader = "header.png";
    String imgNameFooter = "footer.png";
    String imgNameEducativa = "educativa_footer.png";
    DBHelper db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        arrStr.add(imgNameHeader);
        arrStr.add(imgNameFooter);
        arrStr.add(imgNameEducativa);

        db =new DBHelper(this);
        try {

            db.createDataBase();
            db.openDataBase();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        loading2 = (LottieAnimationView) findViewById(R.id.loading2);
        File newDir = new File(Environment.getExternalStorageDirectory() + "/Fiscapp");
        if(!newDir.exists()) newDir.mkdirs();
        new DownloadingParaderos().execute("");

        getDominio();







        /*imglogocusco = (ImageView) findViewById(R.id.imglogocusco);
        imgIcono = (ImageView) findViewById(R.id.imgIcono);*/

        /*Thread myThread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    sleep(4900);
                    verify();


                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();*/




        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //imglogocusco.setVisibility(View.VISIBLE);
                        //imgIcono.setVisibility(View.VISIBLE);
                        //runLogo();
                        //finish();
                        verify();

                    }
                }, 7000);

            }
        });

    }

    public void runLogo() {
        loading2.playAnimation();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //imglogocusco.setVisibility(View.VISIBLE);
                        //imgIcono.setVisibility(View.VISIBLE);

                        verify();

                    }
                }, 1800);

            }
        });
    }

    private void verify(){
        //imglogocusco.setVisibility(View.VISIBLE);
        //getDominio();
        if (SharedPrefManager.getInstance(this).isUsuarioLoggedIn()){
            startActivity(new Intent(SplashActivity.this, MapTest.class));
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivityJ.class));
        }
    }

    public  void Load() {
        final RequestQueue queue = Volley.newRequestQueue(this);



        String h = "http://infomaz.com/?var=recuperar_persona_capacitada";
        StringRequest stringRequest = new StringRequest(h,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            JSONArray arr = new JSONArray(response);
                            JSONObject obj = arr.getJSONObject(0);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
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
        });

        queue.add(stringRequest);

    }

    public void getConfiguracion() {

        final ArrayList<Incidencia> incidenciasArray = new ArrayList<>();
        String ROOT = "";

        String RootResp = SharedPrefManager.getInstance(this).getDominio();

        try {
            JSONArray jsonArray =  new JSONArray(RootResp);
            JSONObject obj = jsonArray.getJSONObject(0);
            ROOT = obj.getString("dominio") + "?var=";
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

                            getConfiguracion();


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

            /*pDialog = new SweetAlertDialog(SplashActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Descargando audios..");
            pDialog.setCancelable(false);
            pDialog.show();*/


//            buttonText.setEnabled(false);
//            btnDownload.setText("Donload Started...");//Set Button Text when download started
        }



        @Override
        protected void onPostExecute(String result) {
            try {
                if (outputFile != null) {
//                    buttonText.setEnabled(true);
                    //btnDownload.setText("Descarga Completa");
                   // pDialog.dismiss();


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

                    //pDialog.dismissWithAnimation();
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
