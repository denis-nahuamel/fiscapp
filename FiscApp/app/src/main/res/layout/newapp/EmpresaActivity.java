package com.example.willy.newapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Willy on 12/4/2017.
 */

public class EmpresaActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    com.example.willy.newapp.BusAdapter adapter;
    List<com.example.willy.newapp.Acta> data_list;

    ImageView img_ida, img_vuelta;
    EditText etBusqueda;
    ArrayList<com.example.willy.newapp.Empresa> empresas;
    ArrayList<com.example.willy.newapp.Acta> actas;
    Context context;
    int direccion = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_empresas);
        Toast.makeText(this, "WAT", Toast.LENGTH_LONG).show();
        context = this;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_empresa);

        data_list = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new com.example.willy.newapp.BusAdapter(context, data_list);
        recyclerView.setAdapter(adapter);



        /*empresas = getEmpresas();

        for(int i = 0; i < empresas.size(); i++) {
            Empresa e = empresas.get(i);
            data_list.add(e);
        }*/

        actas = getActas();

        for(int i = 0; i < actas.size(); i++) {
            com.example.willy.newapp.Acta e = actas.get(i);
            data_list.add(e);
        }






        //adapter.notifyItemRangeChanged(position, list.size());

        adapter.notifyDataSetChanged();

        etBusqueda = (EditText) findViewById(R.id.etBusqueda);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

       /* etBusqueda.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {



                String str = etBusqueda.getText().toString();

                recyclerView = null;
                recyclerView = (RecyclerView) findViewById(R.id.recycler_view_empresa);
                data_list = new ArrayList<>();
                layoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(layoutManager);

                adapter = new ActaAdapterW(context, data_list);
                recyclerView.setAdapter(adapter);

                for(int i = 0; i < empresas.size(); i++) {
                    Empresa e = empresas.get(i);
                    if(match(e.getNombreComercial(), str)) {
                        data_list.add(e);
                        //Toast.makeText(getApplicationContext(),"joven", Toast.LENGTH_SHORT).show();
                    }
                }

                adapter.notifyDataSetChanged();

            }
        });*/

        /*recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        Empresa e = data_list.get(position);
                        Intent paso = new Intent(context, MapActivity.class);
                        paso.putExtra("data",(Serializable) e);
                        paso.putExtra("direccion", direccion + "");
                        context.startActivity(paso);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                        Empresa e = data_list.get(position);
                        Intent paso = new Intent(context, MapActivity.class);
                        paso.putExtra("data",(Serializable) e);
                        paso.putExtra("direccion", direccion + "");
                        context.startActivity(paso);
                    }
                })
        );*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), com.example.willy.newapp.ActaActivity.class));
               /* final EditText taskEditText = new EditText(ActasActivity.this);
                AlertDialog dialog = new AlertDialog.Builder(ActasActivity.this)
                        .setTitle("Add Task")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();*/
            }
        });

    }

    public void removeItem(int position){
        data_list.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, data_list.size());
    }

    /*public void addItem(int position, Empresa emp){
        emp.direccion = direccion;
        data_list.add(position, emp);
        adapter.notifyItemInserted(position);
        adapter.notifyItemRangeChanged(position, data_list.size());
    }*/

    public boolean match(String s, String ele) {
        s = s.toLowerCase();
        ele = ele.toLowerCase();
        //"digital".matches(".*ital.*");
        if(s.matches(".*"+ele+".*")) {
            return true;
        }
        return false;
    }




    public ArrayList<com.example.willy.newapp.Empresa> getEmpresas() {
        BufferedReader reader = null;
        ArrayList<com.example.willy.newapp.Empresa> empresas = new ArrayList<>();

        ArrayList<com.example.willy.newapp.Acta> actas = new ArrayList<>();
        /*try {
            reader = new BufferedReader(new InputStreamReader(this.getAssets().open("data.txt")));
            // do reading, usually loop until end of file reading
            StringBuilder sb = new StringBuilder();
            String mLine = reader.readLine();
            /*Variables de una empresa*/
            /*String tipo_ruta, ruta, nombre, nombreComercial;
            while (mLine != null) {
                sb.append(mLine); // process line
                String line[] = mLine.split(";");
                //Toast.makeText(this, line[0] + " " + line[1] + " " + line[2] + " " + line[3], Toast.LENGTH_SHORT).show();
                tipo_ruta = "RTU 01";//line[0];
                ruta = "05";//line[1];
                nombre = "SAYLLA HUASAO";//line[2];
                nombreComercial = "EMPRESA DE TRANPOSRTES";//line[3];
                Empresa emp = new Empresa(0, tipo_ruta, ruta, nombre, nombreComercial, R.drawable.busbig);
                empresas.add(emp);
                mLine = reader.readLine();
                //Log.d("line",mLine);
            }
            reader.close();
            Log.d("acg", sb.toString());

        } catch (IOException e) {
            //Log.d("error", e + "");
            e.printStackTrace();
        }*/

        String tipo_ruta, ruta, nombre, nombreComercial;

        String conductor, razon_social, placa;

        for(int i = 0; i < 24; i++){

            tipo_ruta = "RTU 01";//line[0];
            ruta = "05";//line[1];
            nombre = "SAYLLA HUASAO";//line[2];
            nombreComercial = "EMPRESA DE TRANPOSRTES";//line[3];
            if(i %2 == 0) {
                tipo_ruta = "RTU 01";//line[0];
                ruta = "10";//line[1];
                nombre = "Juan Carlos Tito Apumayta";//line[2];
                nombreComercial = "EMPRESA DE TRANPOSRTES";//line[3];
            }
            com.example.willy.newapp.Empresa emp = new com.example.willy.newapp.Empresa(0, tipo_ruta, ruta, nombre, nombreComercial, R.drawable.busbig);
            empresas.add(emp);
            //Log.d("line",mLine);

            conductor = "Willy Rosa";
            razon_social = "CUSCO EMPRENDE";
            placa = "ABC-123";

            com.example.willy.newapp.Acta act = new com.example.willy.newapp.Acta(0, conductor, razon_social, placa);
            actas.add(act);

        }





        //Log.d("gg", empresas.size() + "");
        return empresas;
    }

    public ArrayList<com.example.willy.newapp.Acta> getActas() {

        ArrayList<com.example.willy.newapp.Acta> actas = new ArrayList<>();


        String conductor, razon_social, placa;

        for(int i = 0; i < 24; i++){
            conductor = "Gabriela Lizeth Rivera Alagon";
            razon_social = "VENTA DE PRODUCTOS AL POR MAYOR";
            placa = "ABC-123";
            com.example.willy.newapp.Acta act = new com.example.willy.newapp.Acta(0, conductor, razon_social, placa);
            actas.add(act);

        }

        return actas;
    }

}
