package com.fiscapp.fiscapp.Model;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fiscapp.fiscapp.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfraccionesAdapter extends RecyclerView.Adapter<InfraccionesAdapter.ViewHolder> {
    private Context context;
    private List<Infracciones> my_data;
    //List<Acta> ListaActa;

    public InfraccionesAdapter(Context context, List<Infracciones> my_data) {
        this.context = context;
        this.my_data = my_data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_infracciones_pasadas, parent, false);
        return new ViewHolder(itemView,my_data);
    }

    public String getString(String str){
        String out = null;
        try {
            out = new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.fecha.setText(my_data.get(position).getInfraccion_fecha());
        holder.situacion.setText(my_data.get(position).getInfracciones_situacion());
        holder.observacion.setText(my_data.get(position).getInfracciones_obs());
        holder.descripcion.setText(my_data.get(position).getInfraccion_descripcion());
        holder.numero.setText(my_data.get(position).getInfraccion_numero());
        holder.preimpreso.setText(my_data.get(position).getInfracciones_preimpreso());

        holder.preimpreso.setTextColor(Color.parseColor("#bdbdbd"));
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }


    //view holder class

    public class ViewHolder extends RecyclerView.ViewHolder{
        List<Infracciones> ListaActa;
        CardView cv_acta;
        private TextView fecha,situacion,observacion,descripcion,numero,preimpreso;

        public ViewHolder(final View itemView, List<Infracciones> datos) {
            super(itemView);
            ListaActa = datos;
            cv_acta = (CardView) itemView.findViewById(R.id.card_view_infracciones);
            //cv.setOnClickListener(this);
            //  cv_acta.setOnLongClickListener(this);

            //acta_id = (TextView) itemView.findViewById(R.id.cvActa_id);
            fecha = (TextView) itemView.findViewById(R.id.cvInfracciones_fecha);
            situacion = (TextView) itemView.findViewById(R.id.cvInfraccion_situacion);
            observacion = (TextView) itemView.findViewById(R.id.cvInfraccion_observacion);
            descripcion = (TextView) itemView.findViewById(R.id.cvInfracion_descripcion);
            numero = (TextView) itemView.findViewById(R.id.cvNro_infraccion);
            //infraccion_tipo = (TextView) itemView.findViewById(R.id.cvInfraccion_tipo);
            preimpreso = (TextView) itemView.findViewById(R.id.cvInfraccion_preimpreso);



        }

     /*   public boolean onLongClick(View view) {
            int  position = getAdapterPosition();

            Acta objActa =  ListaActa.get(position);
            if(view.getId()==cv_acta.getId())
            EliminarIngreso(objActa.getActa_id(),objActa.getActa_operador());
            return true;

        }*/

        private void EliminarIngreso(String id_acta, String Operador){
            final String pid_acta = id_acta;
            final String pOperador = Operador;
            String URL_DELETE_ACTAS = "http://infomaz.com/?var=anular_acta";
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    URL_DELETE_ACTAS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // progressDialog.dismiss();
                            //Recoger Variables JSon del PHP
                            try {
                                JSONArray jsnArr = new JSONArray(response);
                                JSONObject jsonObject = jsnArr.getJSONObject(0);
                                String mensaje = jsonObject.getString("messsage");
                                //  String msg = obj.getString("message");
                                // Log.e("responsee",msg);
                                // JSONArray arrayInc= (JSONArray)obj.get("data");

                                //Log.i("holaaa",response.length()+"");
                                // Loop through the array elements


                                Toast.makeText(cv_acta.getContext(), "hola"+mensaje, Toast.LENGTH_SHORT).show();
                                //currentDate = feca.format(new Date());
                                //getIngresos(currentDate);
                                //--!android:textColor="@android:drawable/screen_background_dark_transparent"


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                progressDialog.hide();
                    Toast.makeText(cv_acta.getContext(),error.getMessage()+"error",Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    //Variables que necesita el PHP, deben ser iguales al PHP
                    params.put("id",pid_acta+"");
                    params.put("operador",pOperador+"");

                    return params;
                }
            };

            //RequestHandler.getInstance(cv_acta.getContext()).addToRequestQueue(stringRequest);
            RequestQueue queue = Volley.newRequestQueue(cv_acta.getContext());

            queue.add(stringRequest);
        }


    }
}

