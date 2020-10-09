package com.fiscapp.fiscapp.Model;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fiscapp.fiscapp.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PersonaAdapter extends RecyclerView.Adapter<PersonaAdapter.ViewHolder> {

    private Context context;
    private List<cPersona> my_data;

    public PersonaAdapter(Context context, List<cPersona> my_data) {
        this.context = context;
        this.my_data = my_data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_personas, parent, false);
        return new ViewHolder(itemView,my_data);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.dni.setText(my_data.get(position).getPersona_dni());
        holder.nombre.setText(Html.fromHtml(my_data.get(position).getPersona_nombres()));
        holder.tema.setText(my_data.get(position).getPersona_tema());
    }

    @Override
    public int getItemCount() {
        if(my_data != null)
            return my_data.size();
        else
            return 0;
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        List<cPersona> ListaActa;
        CardView cv_acta;
        Dialog myDialog;//---------------------

        Calendar calendar;//------------------------
        SimpleDateFormat simpleDateFormat;//-----------------------
        String fecha;//-------------------------

        private TextView dni, nombre, tema, nombre_modal, dni_modal;
        private ImageView foto_persona;

        public ViewHolder(final View itemView, List<cPersona> datos) {
            super(itemView);
            ListaActa = datos;
            cv_acta = itemView.findViewById(R.id.card_view_personas);


            dni = itemView.findViewById(R.id.pers_dni);
            nombre = itemView.findViewById(R.id.pers_nombre);
            tema = itemView.findViewById(R.id.pers_tema);


            cv_acta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int  position = getAdapterPosition();

                    cPersona persona =  ListaActa.get(position);

                    String nomb = persona.getPersona_nombres();
                    String dni_ = persona.getPersona_dni();
                    String tema = persona.getPersona_tema();
                    String informacion = persona.getPersona_informacion();
                    String obs = persona.getObservaciones();
                    String fecha = persona.getFecha();
                    String vigencia = persona.getVigencia();
                    String foto_persona = persona.getPersona_foto();
                    String tipo = persona.getTipo();



                    modal_(nomb, dni_, tema, informacion, obs, fecha, vigencia, foto_persona, tipo);
                    myDialog.show();
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

            builder.setIcon(R.drawable.ic_error_outline_black_24dp);

            AlertDialog dialog = builder.show();
            TextView messageText = (TextView)dialog.findViewById(android.R.id.message);
            messageText.setGravity(Gravity.FILL);
            dialog.show();
        }

        private int compararFecha(String pFecha){
            try {
                calendar = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                fecha = simpleDateFormat.format(calendar.getTime());



                Date date1 = simpleDateFormat.parse(fecha);
                String A[] = pFecha.split("/");
                pFecha = A[2] + "-"+A[1]+"-"+A[0];
                Date date2 = simpleDateFormat.parse(pFecha);
                Log.e("Fechas",fecha + " - " + pFecha + " || " + date1 + " || " + date2);

                int i = date1.compareTo(date2);
                return i;

            }
            catch (Exception ex){
                return -100;
            }
        }

        private void modal_(String pNombre, String pDni, String pTema, String pInformacion, String pObs, String pFecha, String pVigencia, String pFoto, String pTipo){
            myDialog = new Dialog(context);
            myDialog.setContentView(R.layout.modal);
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.setCancelable(true);
            Button ok = myDialog.findViewById(R.id.ok);
            nombre_modal = myDialog.findViewById(R.id.txt_nombres);
            dni_modal = myDialog.findViewById(R.id.txt_dni);
            foto_persona = myDialog.findViewById(R.id.photo);

            if(pFoto.length()>0) {
                Picasso.with(context)
                        .load(pFoto)
                        .error(R.drawable.unknown)
                        .fit()
                        .centerInside()
                        .into(foto_persona);
            }

            nombre_modal.setText(pNombre);
            dni_modal.setText(pDni);

            int o = compararFecha(pVigencia);

            //Toast.makeText(context, " " + ooo, Toast.LENGTH_SHORT).show();

            String D = "";
            if(!pTema.equals("")) {
                D += "<b>Servicio        : </b>" + pTema + "<br/>";
            }if(!pInformacion.equals("")) {
                D += "<b>Licencia : </b>" + pInformacion + "<br/>";
            }if(!pObs.equals("")) {
                D += "<b>Observación : </b>" + pObs + "<br/>";
            }if(!pFecha.equals("")) {
                D += "<b>Fecha       : </b>" + pFecha + "<br/>";
            }if(!pVigencia.equals("")) {
                if(o>=0)
                    D += "<b>Vigencia     : </b>" + pVigencia + "<br/>";
                else {
                    D += "<b>Vigencia     : </b>" + pVigencia + "<br/>";
                    TextView vigenciaTextView = myDialog.findViewById(R.id.verificacion);
                    RelativeLayout RL = myDialog.findViewById(R.id.ids);
                    vigenciaTextView.setVisibility(View.GONE);
                    RL.setMinimumHeight(20);

                }
            }if(!pTipo.equals("")){
                D += "<b>Tipo : </b>" + pTipo;
            }

            TextView textView = myDialog.findViewById(R.id.txt_informacion);
            textView.setText(Html.fromHtml(D));

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                }
            });
        }

    }
}
