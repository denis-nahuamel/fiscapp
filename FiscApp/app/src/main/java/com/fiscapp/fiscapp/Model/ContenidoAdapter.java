package com.fiscapp.fiscapp.Model;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fiscapp.fiscapp.R;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ContenidoAdapter extends RecyclerView.Adapter<ContenidoAdapter.ViewHolder>{
    private Context context;
    private List<cContenido> my_data;

    //Button btn_text;

    public ContenidoAdapter(Context context, List<cContenido> my_data) {
        this.context = context;
        this.my_data = my_data;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.cont_titulo.setText(my_data.get(position).getContenido_titulo());
        holder.cont_texto.setText(Html.fromHtml(my_data.get(position).getContenido_texto()));
        holder.cont_categoria.setText(my_data.get(position).getContenido_categoria());
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_contenidos, parent, false);
        return new ViewHolder(itemView,my_data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        List<cContenido> ListaActa;
        CardView cv_acta;
        private TextView cont_titulo, cont_texto, cont_categoria;
        Button btn_pdf, btn_ver;
        Dialog myDialog;

        public ViewHolder(final View itemView, List<cContenido> datos) {
            super(itemView);
            ListaActa = datos;
            cv_acta = itemView.findViewById(R.id.card_view_actas);
            cv_acta.setClickable(false);

            btn_ver = itemView.findViewById(R.id.btn_ver);
            btn_pdf = itemView.findViewById(R.id.btn_ver_pdf);

            cont_titulo = itemView.findViewById(R.id.title_content);
            cont_texto = itemView.findViewById(R.id.text_content);
            cont_categoria = itemView.findViewById(R.id.category);

            //btn_text = itemView.findViewById(R.id.btn_ver);
            btn_ver.setOnClickListener(this);

            btn_pdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int  position = getAdapterPosition();

                    cContenido objActa =  ListaActa.get(position);
                    String cont_adj=objActa.getContenido_adjunto();
                    if(!cont_adj.equals("")) {
                        download(objActa.getContenido_adjunto());
                    }
                    else
                        Toast.makeText(context, "Sin Contenido", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void Alert(String dato){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("DATOS");
            builder.setMessage(Html.fromHtml(dato));
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.setIcon(R.drawable.reading);

            AlertDialog dialog = builder.show();

            TextView messageText = dialog.findViewById(android.R.id.message);
            messageText.setGravity(Gravity.FILL);
            dialog.show();
        }



        private void ver_texto(final String idcontenido){

            final ProgressDialog progressDialog = new ProgressDialog(context);

            progressDialog.setMessage("Cargando..");
            progressDialog.setCancelable(false);
            progressDialog.show();

            final RequestQueue queue = Volley.newRequestQueue(context);

            String h = "http://infomaz.com/?var=listar_contenido_interes&export=true";
            StringRequest stringRequest = new StringRequest(h,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                String contenido_text = "";

                                JSONArray arr = new JSONArray(response);

                                for(int i = 0; i < arr.length(); i++) {

                                    JSONObject currentObj = arr.getJSONObject(i);
                                    String contenido_id = currentObj.getString("Contenido_Id");

                                    if(idcontenido.equals(contenido_id)) {
                                        contenido_text = currentObj.getString("Contenido_Texto");
                                        break;
                                    }
                                }

                                progressDialog.dismiss();

                                if (contenido_text.length() != 0) {
                                    //modal(contenido_text);
                                    //myDialog.show();
                                    Alert(contenido_text);
                                }
                                else
                                    Toast.makeText(context, "Sin contenido", Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Sin conexion a internet.", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
            });
            queue.add(stringRequest);
        }

        public void download(String dir_url)
        {
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("Cargando contenido...");
            new DownloadFile(progressDialog).execute(dir_url, "maven.pdf");
        }

        @Override
        public void onClick(View v) {
            int  position = getAdapterPosition();

            cContenido objActa =  ListaActa.get(position);
            ver_texto(objActa.getContenido_id());
            //Toast.makeText(context, "clicked " + objActa.getContenido_id(), Toast.LENGTH_SHORT).show();
            /*if(v.getId()==cv_acta.getId()){
                ver_texto(objActa.getContenido_id());
                Toast.makeText(context, objActa.getContenido_id() + "", Toast.LENGTH_SHORT).show();
            }*/
        }
    }

    private class DownloadFile extends AsyncTask<String, Integer, String> {

        private static final int  MEGABYTE = 1024 * 1024;

        ProgressDialog progressDialog;

        DownloadFile(ProgressDialog progressDialog){
            this.progressDialog = progressDialog;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String fileUrl = strings[0];
            String fileName = strings[1];
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "contents");

            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }


            try {

                URL url = new URL(fileUrl);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);
                int totalSize = urlConnection.getContentLength();

                int total = 0;

                byte[] buffer = new byte[MEGABYTE];
                int bufferLength = 0;
                while((bufferLength = inputStream.read(buffer))>0 ){
                    fileOutputStream.write(buffer, 0, bufferLength);

                    total += bufferLength;

                    publishProgress((int) (total*100/totalSize));

                }
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "Consulta realizada";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setMax(100);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String msj) {
            super.onPostExecute(msj);

            progressDialog.dismiss();
            mostrarPDF(context);
        }

        public void mostrarPDF(Context context) {

            String dir = Environment.getExternalStorageDirectory()+ "/contents/" + "maven.pdf";
            File arch = new File(dir);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(arch), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "No existe una aplicación para abrir el PDF", Toast.LENGTH_SHORT).show();
            }
        }
    }
}



