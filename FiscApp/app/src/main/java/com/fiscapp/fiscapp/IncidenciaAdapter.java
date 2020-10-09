package com.fiscapp.fiscapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fiscapp.fiscapp.Model.Incidencia;

import at.markushi.ui.CircleButton;


import java.util.List;

public class IncidenciaAdapter extends RecyclerView.Adapter<IncidenciaAdapter.ViewHolder> {


    private Context context;
    private List<Incidencia> my_data;

    public IncidenciaAdapter(Context context, List<Incidencia> my_data) {
        this.context = context;
        this.my_data = my_data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_incidencias, parent, false);
        return new ViewHolder(itemView);
    }


    public String getS(String str){
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
        holder.fecha.setText(my_data.get(position).getFecha_incidencia());
        holder.descripcion.setText(my_data.get(position).getIncidencia());
        holder.operador.setText(my_data.get(position).getUsuario_registro());

        holder.tipoIncidencia.setText(my_data.get(position).getTipo_incidencia());

        if(my_data.get(position).getTipo_incidencia().length() > 2) {
            if(my_data.get(position).getTipo_incidencia().equals("EMERGENCIA"))
            {
                holder.tipoIncidencia.setBackgroundResource(R.drawable.borderemergencia);
            }
            else {
                holder.tipoIncidencia.setBackgroundResource(R.drawable.border_normal);
                //holder.tipoIncidencia.setRotation(45);

            }
        }


        //Log.e("vistoo",my_data.get(position).getLeido()+"");
        boolean visto=false;



        holder.circulo.setColor(Color.rgb(191, 190, 187));
        holder.circulo.setImageResource(R.drawable.eye5);
        holder.txtVer.setText("Ver");
        if(my_data.get(position).getLeido().equals("1"))
        {
            // holder.visto.setBackgroundColor(Color.GREEN);
            holder.circulo.setColor(Color.rgb(39, 174, 96));
            holder.txtVer.setText("Visto");
            holder.circulo.setImageResource(R.drawable.ic_action_tick);
            //holder.circulo.setImageIcon();
            // holder.visto.setBackgroundColor();
        }
        visto=true;


        //holder.visto.setChecked(visto);
        // holder.visto.setChecked(my_data.get(position).getLeido()=="1"?true:false);
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView fecha;
        private TextView operador;
        private TextView descripcion;
        private TextView tipoIncidencia;
        private TextView txtVer;

        private CheckBox visto;
        private CircleButton circulo;

        public ViewHolder(final View itemView) {
            super(itemView);


            fecha = (TextView) itemView.findViewById(R.id.txtFecha);
            operador = (TextView) itemView.findViewById(R.id.txtNombre);
            descripcion = (TextView) itemView.findViewById(R.id.txtDescripcion);
            //visto=(CheckBox)itemView.findViewById(R.id.cbVisto);
            circulo=(CircleButton) itemView.findViewById(R.id.cbVisto);
            tipoIncidencia=(TextView) itemView.findViewById(R.id.txtTipoInc);
            txtVer = (TextView) itemView.findViewById(R.id.txtVer);

        }
    }
}



