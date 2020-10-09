package com.fiscapp.fiscapp.Model;

import android.content.Context;
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

public class ActaAdapter extends RecyclerView.Adapter<ActaAdapter.ViewHolder> {
    private Context context;
    private List<Acta> my_data;
    //List<Acta> ListaActa;

    public ActaAdapter(Context context, List<Acta> my_data) {
        this.context = context;
        this.my_data = my_data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_acta, parent, false);
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
        holder.acta_preimpreso.setText(my_data.get(position).getActa_preimpreso());
        holder.acta_operador.setText(my_data.get(position).getActa_operador());
        String fecha_label = my_data.get(position).getFecha_label();
        //holder.acta_fecha_registro.setText(my_data.get(position).getActa_fecha_registro());
        if(fecha_label==null) {
            fecha_label = my_data.get(position).getActa_fecha_registro();
        }
        holder.acta_fecha_registro.setText(fecha_label);
        holder.vehiculo_placa.setText(my_data.get(position).getVehiculo_placa());
        String estado= my_data.get(position).getActa_estado();
        if(estado.equals("0")){estado="ANULADO";}else{estado="GUARDADO";}
        holder.acta_estado.setText(estado);
        //holder.infraccion_tipo.setText(my_data.get(position).getInfraccion_tipo());
        String nombre_c_completo =  my_data.get(position).getConductor_nombre_completo();
        String nombre_c = my_data.get(position).getConductor_nombres();
        String apaterno_c = my_data.get(position).getConductor_apaterno();
        String amaterno_c = my_data.get(position).getConductor_amaterno();
        if(nombre_c_completo==null){nombre_c_completo="";}
        if(nombre_c==null){nombre_c="";}
        if(apaterno_c==null){apaterno_c="";}
        if(amaterno_c==null){amaterno_c="";}
        Log.e("nombres",my_data.get(position).getConductor_nombre_completo()+" "+nombre_c+" "+apaterno_c+" "+amaterno_c);
        holder.conductor_nombre_completo.setText(nombre_c_completo+" "+nombre_c+" "+apaterno_c+" "+amaterno_c);
        //holder.conductor_nombre_completo.setText(my_data.get(position).getConductor_nombre_completo());
        String infraccion_denominacion =  my_data.get(position).getInfraccion_denominacion();
        String reglamento = my_data.get(position).getReglamento_txt();
        String infraccion_tipo = my_data.get(position).getTipo_infraccion_txt();
        if(infraccion_denominacion==null){infraccion_denominacion="";}
        if(reglamento==null){reglamento="";}
        if(infraccion_tipo==null){infraccion_tipo="";}

        if(infraccion_tipo.equals(""))
            holder.infraccion_denominacion.setText(infraccion_denominacion+""+reglamento);
        else
            holder.infraccion_denominacion.setText(infraccion_denominacion+""+reglamento+":"+infraccion_tipo);

        if(my_data.get(position).getActa_educativa() == 0) {
            holder.tipoacta.setText("Acta de control");
        } else {
            holder.tipoacta.setText("Acta educativa");
        }

        holder.infraccion_denominacion.setBackgroundResource(my_data.get(position).getActa_estado().equals("0")?R.drawable.layout_border_anular:R.drawable.layout_border);
        holder.acta_educativa.setBackgroundResource(my_data.get(position).getActa_educativa()==0?R.drawable.circle_control:R.drawable.circle_educativo);
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }


    //view holder class

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        List<Acta> ListaActa;
        CardView cv_acta;
        private TextView acta_id,acta_preimpreso,acta_operador,acta_fecha_registro,vehiculo_placa,acta_estado,
                infraccion_tipo,conductor_nombre_completo,infraccion_denominacion, acta_educativa, tipoacta;

        public ViewHolder(final View itemView, List<Acta> datos) {
            super(itemView);
            ListaActa = datos;
            cv_acta = (CardView) itemView.findViewById(R.id.card_view_actas);
            //cv.setOnClickListener(this);
            //  cv_acta.setOnLongClickListener(this);

            //acta_id = (TextView) itemView.findViewById(R.id.cvActa_id);
            acta_preimpreso = (TextView) itemView.findViewById(R.id.cvActa_preimpreso);
            acta_operador = (TextView) itemView.findViewById(R.id.cvActa_operador);
            acta_fecha_registro = (TextView) itemView.findViewById(R.id.cvActa_fecha_registro);
            vehiculo_placa = (TextView) itemView.findViewById(R.id.cvPlaca);
            acta_estado = (TextView) itemView.findViewById(R.id.cvActa_estado);
            acta_educativa = (TextView) itemView.findViewById(R.id.acta_educativa);
            tipoacta = (TextView) itemView.findViewById(R.id.tipoacta);
            //infraccion_tipo = (TextView) itemView.findViewById(R.id.cvInfraccion_tipo);
            conductor_nombre_completo = (TextView) itemView.findViewById(R.id.cvConductor_nombre_completo);
            infraccion_denominacion = (TextView) itemView.findViewById(R.id.cvInfraccion_denominacion);


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

        @Override
        public boolean onLongClick(View v) {
            int  position = getAdapterPosition();

            Acta objActa =  ListaActa.get(position);
            if(v.getId()==cv_acta.getId())
                EliminarIngreso(objActa.getActa_id(),objActa.getActa_operador());

            return true;
        }
    }
}

