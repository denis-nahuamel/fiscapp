package com.example.willy.newapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Willy on 12/4/2017.
 */

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.ViewHolder> {



    private Context context;
    private List<com.example.willy.newapp.Acta> my_data;

    public BusAdapter(Context context, List<com.example.willy.newapp.Acta> my_data) {
        this.context = context;
        this.my_data = my_data;
    }

    @Override
    public com.example.willy.newapp.BusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout2,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.conductor.setText(my_data.get(position).getConductor());
        holder.razon_social.setText(my_data.get(position).getRazon_social());
        holder.placa.setText(my_data.get(position).getPlaca());
        //holder.busImage.setImageResource(my_data.get(position).getBusImage());
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        /*public TextView nombre;
        public TextView ruta;
        public ImageView busImage;*/

        private TextView conductor;
        private TextView razon_social;
        private TextView placa;

        public ViewHolder(final View itemView) {
            super(itemView);


            conductor = (TextView) itemView.findViewById(R.id.conductor);
            razon_social = (TextView) itemView.findViewById(R.id.razon_social);
            placa = (TextView) itemView.findViewById(R.id.placa);



            //busImage = (ImageView) itemView.findViewById(R.id.bus_image);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Log.d("data", my_data.get(position).getNombre());
                    Empresa e = my_data.get(position);
                    Intent paso = new Intent(context, MapsActivity.class);
                    paso.putExtra("data",(Serializable) e);
                    context.startActivity(paso);

                }
            });*/
        }
    }
}