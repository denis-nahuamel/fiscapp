package com.fiscapp.fiscapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
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
import com.fiscapp.fiscapp.Model.Acta;
import com.fiscapp.fiscapp.Model.ActaAdapter;
import com.fiscapp.fiscapp.Model.Actas_formulario;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import hardware.print.printer;

import static com.fiscapp.fiscapp.Constants.URL_ANULAR_ACTA;
import static com.fiscapp.fiscapp.Constants.URL_GUARDAR_ACTA;
import static com.fiscapp.fiscapp.Constants.URL_LISTAR_ACTAS;

/**
 * Created by Willy on 12/4/2017.
 */

public class ActasActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ActaAdapter adapter;
    List<Acta> data_list;
    List<Acta> actasArray,ActasArrayLocales,actaArrayCompletalocal;
    Acta ActaId;
    EditText etBusqueda;
    ArrayList<Acta> actas;
    Context context;

    DBHelper db ;

    boolean isOnResume  = false;

    Button imprimir, ok;

    int acta_educativa = 0;

    String ROOT;



    Acta actaParaImprimir;

    int posicion_general;

    TextView infracciondenominacion, txtclose;

    //ventana emergente
    BottomSheetDialog mBottomSheetDialog, mBottomSheetDialogLocal;
    Dialog myDialog, myDialogImp;

    String mOperador, mUsuarioID;

    SwipeRefreshLayout pullToRefresh;
    SimpleDateFormat feca;
    String currentDate;

    TextView obtener_actas_locales,obtener_actas_servidor, obtener_actas_educativas, tvClose, tvActa;
    int actas_local_tocado;

    private String GIdActa;

    public static final int REQUEST_CODE = 2;

    CustomEditText etbusqueda;

    int atras_toco = 0;
    ImageView atras;


    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "xDD", Toast.LENGTH_LONG).show();
        /*if(atras_toco == 1) {
            atras_toco = 0;
        } else {
            super.onBackPressed();
        }*/
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }


    ArrayList<String> arrStr = new ArrayList<>();
    String imgNameHeader = "header.png";
    String imgNameFooter = "footer.png";
    String imgNameEducativa = "educativa_footer.png";

    Bitmap bmHeader, bmFooter, bmEducativa;

    TextView tv_preimpreso1,tv_preimpreso2,tv_preimpreso3,tv_preimpreso4,tv_preimpreso5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_actas);

        arrStr.add(imgNameHeader);
        arrStr.add(imgNameFooter);
        arrStr.add(imgNameEducativa);

        String RootResp = SharedPrefManager.getInstance(this).getDominio();

        try {
            JSONArray jsonArray =  new JSONArray(RootResp);
            JSONObject obj = jsonArray.getJSONObject(0);
            ROOT = obj.getString("dominio") + "?var=";
        } catch (JSONException e) {
            e.printStackTrace();
        }


        bmHeader = setBitMapOfImage(arrStr.get(0));
        bmFooter = setBitMapOfImage(arrStr.get(1));
        bmEducativa = setBitMapOfImage(arrStr.get(2));

        /*pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getListaActas(mOperador,currentDate);
                pullToRefresh.setRefreshing(false);

                //Toast.makeText(getApplicationContext(), "HELLO", Toast.LENGTH_SHORT).show();
            }
        });*/

        db = new DBHelper(this);

        /*CustomEditText CEditText = (CustomEditText) findViewById(R.id.myid);


        CEditText.setKeyImeChangeListener(new CustomEditText.KeyImeChange() {

            @Override
            public void onKeyIme(int keyCode, KeyEvent event) {
                // All keypresses with the keyboard open will come through here!
                // You could also bubble up the true/false if you wanted
                // to disable propagation.

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Toast.makeText(getApplicationContext(), "JAJJA", Toast.LENGTH_SHORT).show();
                }

            }
        });

        */




        myDialogImp = new Dialog(ActasActivity.this);
        myDialogImp.setContentView(R.layout.modal_acta);
        myDialogImp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        imprimir = (Button) myDialogImp.findViewById(R.id.boImprimir);
        ok = (Button) myDialogImp.findViewById(R.id.boOk);
        tvClose = (TextView) myDialogImp.findViewById(R.id.tvClose);

        imprimir.setOnClickListener(this);
        ok.setOnClickListener(this);
        tvClose.setOnClickListener(this);

        etbusqueda = (CustomEditText) findViewById(R.id.etbusqueda);
        tvActa = (TextView) findViewById(R.id.tvActa);

        etbusqueda.setOnClickListener(this);
        tvActa.setOnClickListener(this);



        etbusqueda.setKeyImeChangeListener(new CustomEditText.KeyImeChange() {

            @Override
            public void onKeyIme(int keyCode, KeyEvent event) {
                // All keypresses with the keyboard open will come through here!
                // You could also bubble up the true/false if you wanted
                // to disable propagation.

                if (keyCode == KeyEvent.KEYCODE_BACK ) {
                    //etbusqueda.setText("");
                    etbusqueda.setVisibility(View.GONE);
                    //etbusqueda.clearFocus();
                    tvActa.setVisibility(View.VISIBLE);
                }

            }
        });




        etbusqueda.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event){
                if(keyCode == event.KEYCODE_ENTER){
                    //do what you want

                    View view = getCurrentFocus();
                    if (view != null) {

                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                        etbusqueda.setText("");
                        etbusqueda.setVisibility(View.GONE);
                        etbusqueda.clearFocus();
                        tvActa.setVisibility(View.VISIBLE);
                    }
                }
                return false;
            }


        });






        /*WindowManager.LayoutParams lp = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);

        etbusqueda.setLayoutParams(lp);*/

        atras = (ImageView) findViewById(R.id.atras);

        atras.setOnClickListener(this);

        mBottomSheetDialog = new BottomSheetDialog(ActasActivity.this);
        View sheetView = ActasActivity.this.getLayoutInflater().inflate(R.layout.ventana_emergente, null);
        mBottomSheetDialog.setContentView(sheetView);
        //  mBottomSheetDialog.show();

        /*NUEVO DE GABI BEGIN*/

        mBottomSheetDialogLocal = new BottomSheetDialog(ActasActivity.this);
        View sheetView2 = ActasActivity.this.getLayoutInflater().inflate(R.layout.ventana_emergente_local, null);
        mBottomSheetDialogLocal.setContentView(sheetView2);
        //  mBottomSheetDialog.show();

        LinearLayout subir = (LinearLayout) sheetView2.findViewById(R.id.fragment_history_bottom_sheet_sincronizar);
        LinearLayout linlaimprimir = (LinearLayout) sheetView2.findViewById(R.id.linlaimprimir);

        linlaimprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Edit code here;

                //Toast.makeText(getApplicationContext(), "ñoño "+posicion_general, Toast.LENGTH_SHORT).show();
                mBottomSheetDialogLocal.dismiss();

                Acta actaobj= actaArrayCompletalocal.get(posicion_general);
                String acta_preimpreso = actaobj.getActa_preimpreso();
                String acta_operador = actaobj.getActa_operador();
                String acta_fecha_registro = actaobj.getActa_fecha_registro();
                String acta_observaciones = actaobj.getActa_observaciones();
                String tipo_servicio = actaobj.getActa_tipo_servicio();
                String acta_estado = actaobj.getActa_estado();
                String vehiculo_placa = actaobj.getVehiculo_placa();
                String vehiculo_ws = actaobj.getVehiculo_ws();
                String conductor_documento = actaobj.getConductor_tipo_documento();
                String conductor_dni =actaobj.getConductor_dni();
                String conductor_nombres = actaobj.getConductor_nombres();
                String conductor_apaterno = actaobj.getConductor_apaterno();
                String conductor_amaterno =actaobj.getConductor_amaterno();
                String licencia = actaobj.getConductor_licencia();
                String conductor_licencia_clase = actaobj.getConductor_licencia_clase();
                String conductor_licencia_categoria = actaobj.getConductor_licencia_categoria();
                String conductor_direccion = actaobj.getConductor_direccion();
                String conductor_ubigeo =actaobj.getConductor_ubigeo();
                String infraccion_tipo = actaobj.getInfraccion_tipo();
                String infraccion_via = actaobj.getInfraccion_via();
                String empresa_ruc = actaobj.getEmpresa_ruc();

                String empresa_rsocial = actaobj.getEmpresa_rsocial();
                String cobrador_dni = actaobj.getCobrador_dni();
                String cobrador_nombres = actaobj.getCobrador_nombres();
                String cobrador_apaterno = actaobj.getCobrador_apaterno();
                String cobrador_amaterno = actaobj.getCobrador_amaterno();
                String cobrador_direccion =  actaobj.getCobrador_direccion();
                String cobrador_documento = actaobj.getCobrador_tipo_documento();
                String vehiculo_moto = actaobj.getVehiculo_moto();
                String empresa_direccion = actaobj.getEmpresa_direccion();
                String vehiculo_tarjeta =actaobj.getVehiculo_tarjeta();
                String cobrador_edad = actaobj.getCobrador_edad();



                if(empresa_ruc == null) empresa_ruc="";
                if(empresa_rsocial == null) empresa_rsocial="";
                if(cobrador_dni == null) cobrador_dni="";
                if(cobrador_nombres == null) cobrador_nombres="";
                if(cobrador_apaterno == null) cobrador_apaterno="";
                if(cobrador_amaterno == null) cobrador_amaterno="";
                if(cobrador_direccion == null) cobrador_direccion="";
                if(cobrador_documento == null) cobrador_documento="";
                if(empresa_rsocial == null) empresa_rsocial="";
                if(empresa_ruc == null) empresa_ruc="";
                if(licencia==null) licencia="";
                if(conductor_licencia_clase==null)conductor_licencia_clase="";
                if(conductor_licencia_categoria==null)conductor_licencia_categoria="";



                String conductor_licencia_completa = licencia + " - " + conductor_licencia_clase + " - " + conductor_licencia_categoria;

                infraccion_tipo = actaobj.getReglamento_txt() + " : " + actaobj.getTipo_infraccion_txt();

                tipo_servicio = actaobj.getTipo_servicio_txt();
                String codruta_transporte = actaobj.getCodruta_transporte();


                //Toast.makeText(getApplicationContext(), conductor_licencia_completa + " " + tipo_servicio + " " + codruta_transporte, Toast.LENGTH_SHORT).show();
                int peducativa = actaobj.getActa_educativa();


                String tipo_acta = "ACTA DE CONTROL";
                if(peducativa==1) {
                    tipo_acta = "ACTA EDUCATIVA";
                }




                //Toast.makeText(getApplicationContext(), imp_pacta_preimpreso, Toast.LENGTH_SHORT).show();

                String arr[] = acta_fecha_registro.split(" ");
                String otro[] = arr[0].split("-");
                String year = otro[0];
                String month = otro[1];
                String day = otro[2];

                String otro2[] = arr[1].split(":");
                String horas = otro2[0];
                String minutos = otro2[1];

                String fecha = day + "/" + month + "/" +  year;
                String hora = horas + ":" + minutos;

                acta_fecha_registro = fecha + " " + hora;


                testPrint4(tipo_acta, acta_preimpreso, "20177217043", empresa_rsocial, codruta_transporte, empresa_direccion, empresa_ruc, acta_fecha_registro,
                        infraccion_via, conductor_nombres, conductor_apaterno + " " + conductor_amaterno, conductor_direccion,
                        conductor_dni, conductor_licencia_completa, vehiculo_placa, cobrador_nombres, cobrador_apaterno + " " + cobrador_amaterno,
                        cobrador_direccion, cobrador_dni, cobrador_edad,tipo_servicio, empresa_rsocial,   infraccion_tipo, vehiculo_moto, vehiculo_tarjeta,
                        acta_observaciones
                );


                //Toast.makeText(getApplicationContext(), peducativa+"", Toast.LENGTH_SHORT).show();

                Log.d("msje acta",acta_preimpreso+" "+acta_operador+" "+acta_fecha_registro+" "+acta_observaciones+" "+tipo_servicio+" "+
                        acta_estado+" "+vehiculo_placa+" "+vehiculo_ws+" "+conductor_documento+" "+conductor_dni+" "+conductor_nombres+" "+conductor_apaterno+" "+conductor_amaterno
                        +" "+licencia+" "+conductor_licencia_clase+" "+conductor_licencia_categoria+" "+conductor_direccion+" "+conductor_ubigeo+" "+
                        infraccion_tipo+" "+infraccion_via+" "+empresa_ruc+" "+empresa_rsocial+" "+cobrador_dni+" "+cobrador_nombres+" "+cobrador_amaterno+" "+cobrador_apaterno
                        +" "+cobrador_direccion+" "+cobrador_documento+" "+vehiculo_moto+" "+empresa_direccion+" "+vehiculo_tarjeta+" "+cobrador_edad);

            }
        });

        subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit code here;

                //Toast.makeText(getApplicationContext(), "ñoño "+posicion_general, Toast.LENGTH_SHORT).show();
                mBottomSheetDialogLocal.dismiss();

                Acta actaobj= actaArrayCompletalocal.get(posicion_general);
                String acta_preimpreso = actaobj.getActa_preimpreso();
                String acta_operador = actaobj.getActa_operador();
                String acta_fecha_registro = actaobj.getActa_fecha_registro();
                String acta_observaciones = actaobj.getActa_observaciones();
                String tipo_servicio = actaobj.getActa_tipo_servicio();
                String acta_estado = actaobj.getActa_estado();
                String vehiculo_placa = actaobj.getVehiculo_placa();
                String vehiculo_ws = actaobj.getVehiculo_ws();
                String conductor_documento = actaobj.getConductor_tipo_documento();
                String conductor_dni =actaobj.getConductor_dni();
                String conductor_nombres = actaobj.getConductor_nombres();
                String conductor_apaterno = actaobj.getConductor_apaterno();
                String conductor_amaterno =actaobj.getConductor_amaterno();
                String licencia = actaobj.getConductor_licencia();
                String conductor_licencia_clase = actaobj.getConductor_licencia_clase();
                String conductor_licencia_categoria = actaobj.getConductor_licencia_categoria();
                String conductor_direccion = actaobj.getConductor_direccion();
                String conductor_ubigeo =actaobj.getConductor_ubigeo();
                String infraccion_tipo = actaobj.getInfraccion_tipo();
                String infraccion_via = actaobj.getInfraccion_via();
                String empresa_ruc = actaobj.getEmpresa_ruc();
                String empresa_rsocial = actaobj.getEmpresa_rsocial();
                String cobrador_dni = actaobj.getCobrador_dni();
                String cobrador_nombres = actaobj.getCobrador_nombres();
                String cobrador_apaterno = actaobj.getCobrador_apaterno();
                String cobrador_amaterno = actaobj.getCobrador_amaterno();
                String cobrador_direccion =  actaobj.getCobrador_direccion();
                String cobrador_documento = actaobj.getCobrador_tipo_documento();
                String vehiculo_moto = actaobj.getVehiculo_moto();
                String empresa_direccion = actaobj.getEmpresa_direccion();
                String vehiculo_tarjeta =actaobj.getVehiculo_tarjeta();
                String cobrador_edad = actaobj.getCobrador_edad();
                String pre_impreso = actaobj.getActa_preimpreso();

                String conductor_licencia_completa = licencia + " " + conductor_licencia_clase + " " + conductor_licencia_categoria;

                //Toast.makeText(getApplicationContext(), pre_impreso + " " + actaobj.getActa_tipo_servicio_txt(), Toast.LENGTH_SHORT).show();
                int peducativa = actaobj.getActa_educativa();


                String tipo_acta = "ACTA DE CONTROL";
                if(peducativa==1) {
                    tipo_acta = "ACTA EDUCATIVA";
                }





                //Toast.makeText(getApplicationContext(), peducativa+"", Toast.LENGTH_SHORT).show();

                Log.e("msje acta",acta_preimpreso+" "+acta_operador+" "+acta_fecha_registro+" "+acta_observaciones+" "+tipo_servicio+" "+
                        acta_estado+" "+vehiculo_placa+" "+vehiculo_ws+" "+conductor_documento+" "+conductor_dni+" "+conductor_nombres+" "+conductor_apaterno+" "+conductor_amaterno
                        +" "+licencia+" "+conductor_licencia_clase+" "+conductor_licencia_categoria+" "+conductor_direccion+" "+conductor_ubigeo+" "+
                        infraccion_tipo+" "+infraccion_via+" "+empresa_ruc+" "+empresa_rsocial+" "+cobrador_dni+" "+cobrador_nombres+" "+cobrador_amaterno+" "+cobrador_apaterno
                        +" "+cobrador_direccion+" "+cobrador_documento+" "+vehiculo_moto+" "+empresa_direccion+" "+vehiculo_tarjeta+" "+cobrador_edad);

                //SimpleDateFormat feca = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                //currentDate = feca.format(new Date());

                saveActas(pre_impreso,actaobj.getActa_operador(),actaobj.getActa_fecha_registro(),actaobj.getActa_observaciones(),
                        actaobj.getActa_tipo_servicio(),actaobj.getActa_estado(),actaobj.getVehiculo_placa(),actaobj.getVehiculo_ws(),
                        actaobj.getConductor_tipo_documento(),actaobj.getConductor_dni(),actaobj.getConductor_nombres(),actaobj.getConductor_apaterno(),
                        actaobj.getConductor_amaterno(),actaobj.getConductor_licencia(),actaobj.getConductor_licencia_clase(),
                        actaobj.getConductor_licencia_categoria(),actaobj.getConductor_direccion(),actaobj.getConductor_ubigeo(),
                        actaobj.getInfraccion_tipo(),actaobj.getInfraccion_via(),actaobj.getEmpresa_ruc(),actaobj.getEmpresa_rsocial(),
                        actaobj.getCobrador_dni(),actaobj.getCobrador_nombres(),actaobj.getCobrador_apaterno(),actaobj.getCobrador_amaterno(),
                        actaobj.getCobrador_direccion(),actaobj.getCobrador_tipo_documento(),actaobj.getVehiculo_moto(),
                        actaobj.getEmpresa_direccion(),actaobj.getVehiculo_tarjeta(),actaobj.getCobrador_edad(), peducativa);






            }
        });


        obtener_actas_locales = (TextView) findViewById(R.id.tab_actas_locales);
        obtener_actas_servidor = (TextView) findViewById(R.id.tab_actas_servidor);
        //obtener_actas_educativas = (TextView) findViewById(R.id.tab_actas_educativas);


        obtener_actas_servidor.setTextColor(Color.parseColor("#000000"));
        obtener_actas_servidor.setBackgroundResource(R.drawable.border_outline_activado);
        obtener_actas_locales.setBackgroundResource(R.drawable.border_outline_desactivado);


        obtener_actas_servidor.setOnClickListener(this);
        obtener_actas_locales.setOnClickListener(this);
        //obtener_actas_educativas.setOnClickListener(this);

        /*NUEVO GABI END*/


        //LinearLayout edit = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_edit);
        LinearLayout delete_modificar = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_delete);
        final LinearLayout ver_detalle = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_detalle);
        final LinearLayout ver_imprimir = (LinearLayout) sheetView.findViewById(R.id.fragment_history_bottom_sheet_imprimir);
        final TextView tv_detalle = (TextView) sheetView.findViewById(R.id.tv_ver_detalle);
        final ImageView imv_boorar_modificar = (ImageView) sheetView.findViewById(R.id.imv_anular_modificar);

        tv_preimpreso1 = (TextView) sheetView.findViewById(R.id.tv_preimpreso1);
        tv_preimpreso2 = (TextView) sheetView.findViewById(R.id.tv_preimpreso2);
        tv_preimpreso3 = (TextView) sheetView.findViewById(R.id.tv_preimpreso3);
        tv_preimpreso4 = (TextView) sheetView2.findViewById(R.id.tv_preimpreso4);
        tv_preimpreso5 = (TextView) sheetView2.findViewById(R.id.tv_preimpreso5);



        /*edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Edit code here;

                Toast.makeText(getApplicationContext(), "Volver a generar", Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
                Acta actaobj= actasArray.get(posicion_general);

                //getActaId(actaobj.getActa_id());

            }
        });*/

        ver_imprimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mBottomSheetDialog.dismiss();
                Acta actaobj= actasArray.get(posicion_general);
                String estado_de_acta = actaobj.getActa_estado();
                String id = actaobj.getActa_id();
                GIdActa = id;
                myDialogImp.show();
                //Toast.makeText(getApplicationContext(), id + " " + posicion_general, Toast.LENGTH_SHORT).show();
            }
        });

        delete_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Anular", Toast.LENGTH_SHORT).show();
                //myDialog.show();

                mBottomSheetDialog.dismiss();
                Acta actaobj= actasArray.get(posicion_general);
                String estado_de_acta = actaobj.getActa_estado();

                if(estado_de_acta.equals("0")){
                    //tv_detalle.setText("volver a generar nuevo");
                    //Toast.makeText(getApplicationContext(), actaobj.getActa_educativa()+"", Toast.LENGTH_SHORT).show();
                    getActaId(actaobj.getActa_id());
                }
                else
                {


                    String config = SharedPrefManager.getInstance(getApplicationContext()).getConfig();


                    String tiempo_anulacion = "";
                    String tiempo_emergencia = "";
                    String url_upload = "";
                    String usuario_ftp = "";
                    String pw_ftp = "";
                    String numero_sms = "";
                    String numero_emergencia = "";

                    try {
                        JSONArray jsonArray = new JSONArray(config);



                        JSONObject obj = jsonArray.getJSONObject(0);
                        tiempo_anulacion = obj.getString("tiempo_anulacion");
                        tiempo_emergencia = obj.getString("tiempo_emergencia");
                        url_upload = obj.getString("url_upload");
                        usuario_ftp = obj.getString("usuario_ftp");
                        pw_ftp = obj.getString("pw_ftp");
                        numero_sms = obj.getString("numero_sms");
                        numero_emergencia = obj.getString("numero_emergencia");

                        //Toast.makeText(getApplicationContext(), tiempo_anulacion, Toast.LENGTH_SHORT).show();



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String fecha_registro = actaobj.getActa_fecha_registro();

                    String Arr[] = fecha_registro.split(" ");
                    String Hora = Arr[1];

                    Date d2 = new Date();

                    int dif = calcDiff(Hora, d2)/60;


                    if(dif <= Integer.parseInt(tiempo_anulacion)) {
                        myDialog.show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Se ha excedido el tiempo de anulacion", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        ver_detalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                Acta actaobj= actasArray.get(posicion_general);
                    //abrir ventana de detalle
                getActaId_detalle(actaobj.getActa_id());

            }
        });

        myDialog = new Dialog(ActasActivity.this);
        //View indicar_motivo = ActasActivity.this.getLayoutInflater().inflate(R.layout.motivo_acta, null);
        myDialog.setContentView(R.layout.motivo_acta);

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //  mBottomSheetDialog.show();

        Button cancelar = (Button) myDialog.findViewById(R.id.boCancelar_motivo);
        Button enviar = (Button) myDialog.findViewById(R.id.boEnviar_Motivo);
        final EditText motivo = (EditText) myDialog.findViewById(R.id.etMotivo_anulacion);

        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);

        txtclose.setOnClickListener(this);


        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit code here;
                //Toast.makeText(getApplicationContext(), "cancelo", Toast.LENGTH_SHORT).show();
                myDialog.dismiss();
            }
        });

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "enviar", Toast.LENGTH_SHORT).show();
                Acta objActa =  actasArray.get(posicion_general);
                final String Motivo_eliminacion = motivo.getText().toString();

                EliminarIngreso(objActa.getActa_id(),objActa.getActa_operador(),Motivo_eliminacion);



                Log.i("posicion cv",posicion_general+"");
                myDialog.dismiss();
            }
        });



        //Toast.makeText(this, "WAT", Toast.LENGTH_LONG).show();
        infracciondenominacion = (TextView) findViewById(R.id.cvInfraccion_denominacion);
        actasArray= new ArrayList<>();
        context = this;

        mUsuarioID = SharedPrefManager.getInstance(getApplicationContext()).getUserKeyId() + "";
        mOperador = SharedPrefManager.getInstance(getApplicationContext()).getUserKeyUsuario();

        //Toast.makeText(getApplicationContext(), mUsuarioID + " " + mOperador, Toast.LENGTH_SHORT).show();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_actas);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, recyclerView, new RecyclerItemClickListener.OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {

                /*posicion_general=position;
                Toast.makeText(getApplicationContext(), "ñoño "+posicion_general, Toast.LENGTH_SHORT).show();
                mBottomSheetDialogLocal.show();*/

                /*testPrint2( String pre_impreso,String ruc, String nombre_razon_social, String domicilio, String dni_ruc,String fecha, String lugar_intervencion,
                        String cnombres, String capellidos,String cdomicilio,String cdni,String clicencia_clase_cat, String cplaca_rodaje,
                        String cobnombres,String cobapellidos, String cobdomicilio, String cobdni, String cobedad,String  modalidad, String codruta,String codinfraccion);*/
            }
           //holder.infraccion_denominacion.setBackgroundResource(my_data.get(position).getActa_estado().equals("0")?R.drawable.layout_border_anular:R.drawable.layout_border);
            @Override
            public void onLongItemClick(View view, int position) {



                if(actas_local_tocado==1)
                {
                    posicion_general=position;
                    Acta actaobj= actaArrayCompletalocal.get(posicion_general);
                    //Toast.makeText(getApplicationContext(), "ñoño "+posicion_general, Toast.LENGTH_SHORT).show();
                    mBottomSheetDialogLocal.show();

                    tv_preimpreso4.setText("Acta " + actaobj.getActa_preimpreso());
                    tv_preimpreso5.setText("");
                }
                else
                {



                    posicion_general=position;
                    //posicion_general=position;
                    // mBottomSheetDialog.show();
                    Acta actaobj= actasArray.get(posicion_general);


                    tv_preimpreso1.setText("Acta " + actaobj.getActa_preimpreso());
                    tv_preimpreso2.setText("");
                    tv_preimpreso3.setText("");

                    String estado_de_acta = actaobj.getActa_estado();

                    if(estado_de_acta.equals("0")){
                        tv_detalle.setText("Generar nuevamente");
                        imv_boorar_modificar.setImageResource(R.drawable.edit);
                        //getActaId(actaobj.getActa_id());
                    }
                    else
                    {


                        tv_detalle.setText("Anular");
                        imv_boorar_modificar.setImageResource(R.drawable.trash);
                        //myDialog.show();
                    }

                    mBottomSheetDialog.show();

                }

               /*Acta objActa =  actasArray.get(position);


                   EliminarIngreso(objActa.getActa_id(),objActa.getActa_operador());


                   Log.i("posicion cv",position+"");
                  // infracciondenominacion.setBackgroundResource(R.drawable.layout_border_anular);*/


            }
        }));

       /* data_list = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ActaAdapter(context, data_list);
        recyclerView.setAdapter(adapter);*/
       /* ArrayList<Acta> actasArray = new ArrayList<>();
        Acta act = new Acta("1","1","1","1","1","1","1","1","1");
        actasArray.add(act);
       // actas=actasArray;
        recyclerView = null;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_actas);
        data_list = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);


        adapter = new ActaAdapter(context, actasArray);
        recyclerView.setAdapter(adapter);*/


         feca = new SimpleDateFormat("yyyy-MM-dd");
         currentDate = feca.format(new Date());

         //getActasLocales();

        getListaActas(mOperador,currentDate);

       /* for(int i = 0; i < actas.size(); i++) {
            Acta e = actas.get(i);
            data_list.add(e);
        }*/


        etBusqueda = (EditText) findViewById(R.id.etBusqueda);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //startActivity(new Intent(getApplicationContext(), Actas_formulario.class));

                Intent paso = new Intent(ActasActivity.this, Actas_formulario.class);

                //paso.putExtra("operador", mOperador);

                startActivityForResult(paso , REQUEST_CODE);

            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                    {

                        String s= data.getStringExtra("incidencia");

                        if(s.equals("local")) {
                            recyclerView = null;
                            layoutManager=null;
                            actas_local_tocado=1;

                            obtener_actas_servidor.setBackgroundResource(R.drawable.border_outline_desactivado);
                            obtener_actas_locales.setBackgroundResource(R.drawable.border_outline_activado);
                            obtener_actas_locales.setTextColor(Color.parseColor("#000000"));
                            obtener_actas_servidor.setTextColor(Color.parseColor("#bdbdbd"));

                            getActasLocales();
                        } else {
                            actas_local_tocado=0;
                            recyclerView = null;
                            layoutManager=null;
                            obtener_actas_servidor.setBackgroundResource(R.drawable.border_outline_activado);
                            obtener_actas_locales.setBackgroundResource(R.drawable.border_outline_desactivado);
                            obtener_actas_locales.setTextColor(Color.parseColor("#bdbdbd"));
                            obtener_actas_servidor.setTextColor(Color.parseColor("#000000"));

                            feca = new SimpleDateFormat("yyyy-MM-dd");
                            currentDate = feca.format(new Date());

                            getListaActas(mOperador,currentDate);
                        }
                        //Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
                        break;
                    }
                    case Activity.RESULT_CANCELED:
                    {
                        // The user was asked to change settings, but chose not to
                       // Toast.makeText(getApplicationContext(), "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();
                        //activarGPS();
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                break;
        }
    }

    public int calcDiff(String Hora, Date date2) {

        String Arr[] = Hora.split(":");
        int horaPast = Integer.parseInt(Arr[0]);
        int minutosPast = Integer.parseInt(Arr[1]);
        int segundosPast = Integer.parseInt(Arr[2]);

        //Toast.makeText(getApplicationContext(), horaPast + " " + minutosPast + " " + segundosPast, Toast.LENGTH_SHORT).show();

        int seg_tot_past = horaPast*3600 + minutosPast*60 + segundosPast;

        int horaActual = date2.getHours();
        int minutosActual = date2.getMinutes();
        int segundosActual = date2.getSeconds();

        int seg_tot_actual = horaActual*3600 + minutosActual*60 + segundosActual;

        int diff = Math.abs(seg_tot_actual - seg_tot_past);

        return diff;
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if(isOnResume) {
            getListaActas(mOperador, currentDate);
        }*/
    }

    public void removeItem(int position){
        data_list.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, data_list.size());
    }

    /*public void addItem(int position, Empresa emp){

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

    public ArrayList<Acta> getActas() {

        ArrayList<Acta> actas = new ArrayList<>();


        String conductor, razon_social, placa;

        for(int i = 0; i < 24; i++){
            conductor = "Gabriela Lizeth Rivera Alagon";
            razon_social = "VENTA DE PRODUCTOS AL POR MAYOR";
            placa = "ABC-123";
            // Acta act = new Acta(0, conductor, razon_social, placa);
            //  actas.add(act);

        }

        return actas;
    }

    public  void getListaActas(final String operador, final String fecha) {
/*        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Haciendo Magic...");
        pd.show();*/

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Cargando Actas...");
        pd.show();
        pd.setCancelable(false);

        /*final ArrayList<Acta>*/
        actasArray = new ArrayList<>();

        recyclerView = null;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_actas);
        //data_list = new ArrayList<>();
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);


        adapter = new ActaAdapter(context, actasArray);
        recyclerView.setAdapter(adapter);

        String URL_GETACTAS = ROOT + URL_LISTAR_ACTAS  + "&fecha=" + fecha + "&operador=" + operador;

        Log.d("urlcito", fecha + " " + operador);
        Log.d("urlcito", URL_GETACTAS);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_GETACTAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  pd.dismiss();
                        try {



                            pd.dismiss();
                            //wait1sec(500, pd);
                            //Log.e("entrando","2");
                            // JSONObject obj = new JSONObject(response);
                            //  if (!obj.getBoolean("error")){
                            JSONArray jsnArr = new JSONArray(response);

                            //  String msg = obj.getString("message");
                            // Log.e("responsee",msg);
                            // JSONArray arrayInc= (JSONArray)obj.get("data");

                            //Log.i("holaaa",response.length()+"");
                            // Loop through the array elements

                            SharedPrefManager.getInstance(getApplicationContext()).setresponseListadoActas(response);

                            //Toast.makeText(getApplicationContext(), jsnArr.length() + " recupere", Toast.LENGTH_SHORT).show();

                            for(int i=0;i<jsnArr.length();i++){

                                // Get current json object
                                JSONObject acta = jsnArr.getJSONObject(i);
                                String acta_id = acta.getString("acta_id");
                                //Log.i("holaa","gil1");
                                String acta_preimpreso=acta.getString("acta_preimpreso");
                                //Log.i("holaa","gil2");
                                String acta_operador=acta.getString("acta_operador");
                                //Log.i("holaa","gil3");
                                String acta_fecha_registro=acta.getString("acta_fecha_registro");
                                Log.i("holaa","gil4");
                                String vehiculo_placa = acta.getString("vehiculo_placa");
                                Log.i("holaa","gil5");
                                String acta_estado = acta.getString("acta_estado");
                                Log.i("holaa","gil6");
                                String infraccion_tipo=acta.getString("infraccion_tipo");
                                Log.i("holaa","gil7");
                                String conductor_nombre=acta.getString("conductor_nombre_completo");
                                Log.i("holaa","gil8");
                                String infraccion_denominacion=acta.getString("Infraccion_Denominacion");
                                String tipo_acta = acta.getString("tipo_acta");
                                Log.i("holaa","gil9");

                                String fecha_label = acta.getString("fecha_label");

                                //acta_fecha_registro = fecha_label;

                                int acta_edu = 1;
                                if(tipo_acta.equals("ACTA DE CONTROL")) {
                                    acta_edu = 0;
                                }


                                Acta act = new Acta(acta_id,acta_preimpreso,acta_operador,acta_fecha_registro,vehiculo_placa,acta_estado,infraccion_tipo,conductor_nombre,infraccion_denominacion, acta_edu, fecha_label);
                                actasArray.add(act);
                                Log.i("holaa","gil");
                            }
                            //Acta act = new Acta("1","1","1","1","1","1","1","1","1");
                            //actasArray.add(act);
                            Log.e("array inc22a",actasArray.size()+"");
                            //actas=actasArray;
                            recyclerView = null;
                            recyclerView = (RecyclerView) findViewById(R.id.recycler_view_actas);
                            //data_list = new ArrayList<>();
                            layoutManager = new LinearLayoutManager(context);
                            recyclerView.setLayoutManager(layoutManager);


                            adapter = new ActaAdapter(context, actasArray);
                            recyclerView.setAdapter(adapter);









                            // RecyclerView contenedor = (RecyclerView) findViewById(R.id.contenedor);
                            // contenedor.setHasFixedSize(true);
                            //LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
                            //layout.setOrientation(LinearLayoutManager.VERTICAL);
                            ////contenedor.setAdapter(new Adaptador(Lista, rootView));
                            //contenedor.setLayoutManager(layout);


                            adapter.notifyDataSetChanged();

                            isOnResume = true;


                            //   Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            //  finish();

                          /*  } else {
                                String msg = obj.getString("message");

                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                //finish();
                            }*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // progressDialog.dismiss();



                        pd.dismiss();




                        if (!SharedPrefManager.getInstance(getApplicationContext()).isresponseListadoActasNull()) {


                            String response = SharedPrefManager.getInstance(getApplicationContext()).getresponseListadoActas();


                            try {

                                //Log.e("entrando","2");
                                // JSONObject obj = new JSONObject(response);
                                //  if (!obj.getBoolean("error")){
                                JSONArray jsnArr = new JSONArray(response);

                                //  String msg = obj.getString("message");
                                // Log.e("responsee",msg);
                                // JSONArray arrayInc= (JSONArray)obj.get("data");

                                //Log.i("holaaa",response.length()+"");
                                // Loop through the array elements

                                //Toast.makeText(getApplicationContext(), jsnArr.length() + " recupere cache", Toast.LENGTH_SHORT).show();

                                SharedPrefManager.getInstance(getApplicationContext()).setresponseListadoActas(response);

                                for(int i=0;i<jsnArr.length();i++){

                                    // Get current json object
                                    JSONObject acta = jsnArr.getJSONObject(i);
                                    String acta_id = acta.getString("acta_id");
                                    //Log.i("holaa","gil1");
                                    String acta_preimpreso=acta.getString("acta_preimpreso");
                                    //Log.i("holaa","gil2");
                                    String acta_operador=acta.getString("acta_operador");
                                    //Log.i("holaa","gil3");
                                    String acta_fecha_registro=acta.getString("acta_fecha_registro");
                                    Log.i("holaa","gil4");
                                    String vehiculo_placa = acta.getString("vehiculo_placa");
                                    Log.i("holaa","gil5");
                                    String acta_estado = acta.getString("acta_estado");
                                    Log.i("holaa","gil6");
                                    String infraccion_tipo=acta.getString("infraccion_tipo");
                                    Log.i("holaa","gil7");
                                    String conductor_nombre=acta.getString("conductor_nombre_completo");
                                    Log.i("holaa","gil8");
                                    String infraccion_denominacion=acta.getString("Infraccion_Denominacion");
                                    Log.i("holaa","gil9");

                                    String tipo_acta = acta.getString("tipo_acta");
                                    Log.i("holaa","gil9");

                                    String fecha_label = acta.getString("fecha_label");

                                    if(tipo_acta.equals("ACTA DE CONTROL")) {
                                        acta_educativa = 0;
                                    } else {
                                        acta_educativa = 1;
                                    }


                                    Acta act = new Acta(acta_id,acta_preimpreso,acta_operador,acta_fecha_registro,vehiculo_placa,acta_estado,infraccion_tipo,conductor_nombre,infraccion_denominacion, acta_educativa, fecha_label);
                                    actasArray.add(act);
                                    Log.i("holaa","gil");
                                }
                                //Acta act = new Acta("1","1","1","1","1","1","1","1","1");
                                //actasArray.add(act);
                                Log.e("array inc22a",actasArray.size()+"");
                                //actas=actasArray;
                                recyclerView = null;
                                recyclerView = (RecyclerView) findViewById(R.id.recycler_view_actas);
                                //data_list = new ArrayList<>();
                                layoutManager = new LinearLayoutManager(context);
                                recyclerView.setLayoutManager(layoutManager);


                                adapter = new ActaAdapter(context, actasArray);
                                recyclerView.setAdapter(adapter);









                                // RecyclerView contenedor = (RecyclerView) findViewById(R.id.contenedor);
                                // contenedor.setHasFixedSize(true);
                                //LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
                                //layout.setOrientation(LinearLayoutManager.VERTICAL);
                                ////contenedor.setAdapter(new Adaptador(Lista, rootView));
                                //contenedor.setLayoutManager(layout);


                                adapter.notifyDataSetChanged();

                                isOnResume = true;


                                //   Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                //  finish();

                          /*  } else {
                                String msg = obj.getString("message");

                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                //finish();
                            }*/
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Sin conexion a internet",Toast.LENGTH_SHORT).show();
                        }

                        /*if (error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof ServerError) {




                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(),"AuthFailureError",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(),"NetworkError",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getApplicationContext(),"ParseError",Toast.LENGTH_SHORT).show();
                        }*/

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("id_usuario", operador);
                params.put("fecha",fecha);
                //params.put("detalle",detalle);

                return params;
            }
        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }


    private void EliminarIngreso(String id_acta, String Operador, String Motivo){
        final String pid_acta = id_acta;
        final String pOperador = Operador;
        final String pMotivo = Motivo;
        //String URL_DELETE_ACTAS = "http://infomaz.com/?var=anular_acta";
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                ROOT+URL_ANULAR_ACTA,
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


                            Toast.makeText(getApplicationContext(), "new mensaje"+mensaje, Toast.LENGTH_SHORT).show();
                            getListaActas(mOperador,currentDate);
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
                Toast.makeText(getApplicationContext(),error.getMessage()+"error",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Variables que necesita el PHP, deben ser iguales al PHP
                params.put("id",pid_acta+"");
                params.put("operador",pOperador+"");
                params.put("motivo",pMotivo);

                return params;
            }
        };

        //RequestHandler.getInstance(cv_acta.getContext()).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        queue.add(stringRequest);
    }

    public  void getActaId(final String id_acta) {
/*        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Haciendo Magic...");
        pd.show();*/

        //progressDialog.setMessage("Haciendo Magia...");
        //progressDialog.show();


        String URL_GETACTAS = ROOT + "recuperar_acta&id="+id_acta;
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_GETACTAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  progressDialog.dismiss();
                        //  pd.dismiss();
                        try {

                            JSONArray jsnArr = new JSONArray(response);
                            Log.i("resspuesta",jsnArr+"");
                            JSONObject acta = jsnArr.getJSONObject(0);
                            String acta_id = acta.getString("acta_id");
                            String acta_preimpreso_text= acta.getString("acta_preimpreso");
                            String acta_preimpreso_id= acta.getString("acta_preimpreso_int");
                            String acta_operador = acta.getString("acta_operador");
                            String acta_fecha_registro=acta.getString("acta_fecha_registro");
                            String acta_observaciones= acta.getString("acta_observaciones");
                            String acta_tipo_servicio_text=acta.getString("tipo_servicio");
                            String acta_tipo_servicio_id=acta.getString("id_tipo_servicio");
                            String acta_estado=acta.getString("acta_estado");
                            String vehiculo_placa = acta.getString("vehiculo_placa");
                            String vehiculo_ws=acta.getString("vehiculo_ws");
                            String conductor_tipo_documento_text=acta.getString("conductor_documento");
                            String conductor_tipo_documento_id=acta.getString("id_conductor_documento");
                            String conductor_dni=acta.getString("conductor_dni");
                            String conductor_nombres=acta.getString("conductor_nombres");
                            String conductor_apaterno=acta.getString("conductor_apaterno");
                            String conductor_amaterno=acta.getString("conductor_amaterno");
                            String conductor_licencia=acta.getString("licencia");
                            String conductor_licencia_clase_txt=acta.getString("clase");
                            String conductor_licencia_clase_id=acta.getString("conductor_licencia_clase");
                            String conductor_licencia_categoria_txt=acta.getString("categoria");
                            String conductor_licencia_categoria_id=acta.getString("conductor_licencia_categoria");
                            String conductor_direccion=acta.getString("conductor_direccion");
                            String conductor_ubigeo=acta.getString("conductor_ubigeo");
                            String infraccion_tipo_txt=acta.getString("infraccion_tipo");
                            String infraccion_tipo_id=acta.getString("id_infraccion_tipo");
                            String reglamento_txt=acta.getString("reglamento");
                            String reglamento_id=acta.getString("id_reglamento");
                            String infraccion_via=acta.getString("infraccion_via");
                            String empresa_ruc=acta.getString("empresa_ruc");
                            String empresa_rsocial=acta.getString("empresa_rsocial");
                            String cobrador_dni=acta.getString("cobrador_dni");;
                            String cobrador_nombres=acta.getString("cobrador_nombres");
                            String cobrador_apaterno=acta.getString("cobrador_apaterno");
                            String cobrador_amaterno=acta.getString("cobrador_amaterno");
                            String cobrador_direccion=acta.getString("cobrador_direccion");
                            String cobrador_tipo_documento_txt=acta.getString("cobrador_documento");
                            String cobrador_tipo_documento_id=acta.getString("id_cobrador_documento");
                            String cobrador_edad= acta.getString("cobrador_edad");
                            String empresa_direccion = acta.getString("empresa_direccion");
                            String vehiculo_tarjeta = acta.getString("vehiculo_tarjeta");
                            String vehiculo_moto = acta.getString("vehiculo_moto");
                            String educativa = acta.getString("educativa");
                            int pacta_educativa = 0;

                            if(educativa.equals("1")) pacta_educativa = 1;

                            ActaId= new Acta(acta_id,acta_preimpreso_id,acta_operador,acta_fecha_registro,acta_observaciones,acta_tipo_servicio_id,
                                    acta_estado,vehiculo_placa,vehiculo_ws,conductor_tipo_documento_id,conductor_dni,conductor_nombres,
                                    conductor_apaterno,conductor_amaterno,conductor_licencia,conductor_licencia_clase_id,conductor_licencia_categoria_id,
                                    conductor_direccion,conductor_ubigeo,infraccion_tipo_id,reglamento_id,infraccion_via,empresa_ruc,empresa_rsocial,
                                    cobrador_dni,cobrador_nombres,cobrador_apaterno,cobrador_amaterno,cobrador_direccion,cobrador_tipo_documento_id,
                                    vehiculo_moto,empresa_direccion,vehiculo_tarjeta,cobrador_edad,infraccion_tipo_txt,conductor_tipo_documento_text,
                                    cobrador_tipo_documento_txt,conductor_licencia_clase_txt,conductor_licencia_categoria_txt,acta_tipo_servicio_text,
                                    reglamento_txt, acta_preimpreso_text, pacta_educativa);

                            Log.i("acta final",ActaId+" "+ActaId.getActa_operador()+"ño");
                            //  Log.i("acta recuperada_id",actaobj.getActa_id()+"");
                            Intent paso = new Intent(getApplicationContext(), Actas_formulario.class);
                            paso.putExtra("acta",(Serializable) ActaId);
                            context.startActivity(paso);
                            // Log.i("acta recuperada",ActaId.getActa_operador());
                            /* context.startActivity(paso);*/


                        /*Intent paso = getIntent();
                        paso.putExtra("key", "watafuck");*/
                            //setResult(RESULT_OK, paso);
                            // finish();

                            //adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
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
                }
        );


        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
        //   return ActaId;
    }

    public  void getActaId_detalle(final String id_acta) {
/*        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Haciendo Magic...");
        pd.show();*/

        //progressDialog.setMessage("Haciendo Magia...");
        //progressDialog.show();


        String URL_GETACTAS = ROOT + "recuperar_acta&id="+id_acta;
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_GETACTAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  progressDialog.dismiss();
                        //  pd.dismiss();
                        try {

                            JSONArray jsnArr = new JSONArray(response);
                            Log.i("resspuesta",jsnArr+"");
                            JSONObject acta = jsnArr.getJSONObject(0);
                            String acta_id = acta.getString("acta_id");
                            String acta_preimpreso= acta.getString("acta_preimpreso");
                            String acta_operador = acta.getString("acta_operador");
                            String acta_fecha_registro=acta.getString("acta_fecha_registro");
                            String acta_observaciones= acta.getString("acta_observaciones");
                            String acta_tipo_servicio=acta.getString("tipo_servicio");
                            String acta_estado=acta.getString("acta_estado");
                            String vehiculo_placa = acta.getString("vehiculo_placa");
                            String vehiculo_ws=acta.getString("vehiculo_ws");
                            String conductor_tipo_documento=acta.getString("conductor_documento");
                            String conductor_dni=acta.getString("conductor_dni");
                            String conductor_nombres=acta.getString("conductor_nombres");
                            String conductor_apaterno=acta.getString("conductor_apaterno");
                            String conductor_amaterno=acta.getString("conductor_amaterno");
                            String conductor_licencia=acta.getString("licencia");
                            String conductor_licencia_clase=acta.getString("conductor_licencia_clase");
                            String conductor_licencia_categoria=acta.getString("conductor_licencia_categoria");
                            String conductor_direccion=acta.getString("conductor_direccion");
                            String conductor_ubigeo=acta.getString("conductor_ubigeo");
                            String infraccion_tipo=acta.getString("infraccion_tipo");
                            String infraccion_via=acta.getString("infraccion_via");
                            String empresa_ruc=acta.getString("empresa_ruc");
                            String empresa_rsocial=acta.getString("empresa_rsocial");
                            String cobrador_dni=acta.getString("cobrador_dni");;
                            String cobrador_nombres=acta.getString("cobrador_nombres");
                            String cobrador_apaterno=acta.getString("cobrador_apaterno");
                            String cobrador_amaterno=acta.getString("cobrador_amaterno");
                            String cobrador_direccion=acta.getString("cobrador_direccion");
                            String cobrador_tipo_documento=acta.getString("cobrador_documento");
                            ActaId= new Acta(acta_id,acta_preimpreso,acta_operador,acta_fecha_registro,acta_observaciones,acta_tipo_servicio,acta_estado,
                                    vehiculo_placa,vehiculo_ws,conductor_tipo_documento,conductor_dni,conductor_nombres,conductor_apaterno,conductor_amaterno,
                                    conductor_licencia,conductor_licencia_clase,conductor_licencia_categoria,conductor_direccion,conductor_ubigeo,
                                    infraccion_tipo,infraccion_via,empresa_ruc,empresa_rsocial,cobrador_dni,cobrador_nombres,cobrador_apaterno,cobrador_amaterno,
                                    cobrador_direccion,cobrador_tipo_documento);

                            Log.i("acta final",ActaId+" "+ActaId.getActa_operador()+"ño");
                            //  Log.i("acta recuperada_id",actaobj.getActa_id()+"");
                            Intent paso = new Intent(getApplicationContext(), Detalle_Acta.class);
                            paso.putExtra("acta",(Serializable) ActaId);
                            context.startActivity(paso);
                            // Log.i("acta recuperada",ActaId.getActa_operador());
                            /* context.startActivity(paso);*/


                        /*Intent paso = getIntent();
                        paso.putExtra("key", "watafuck");*/
                            //setResult(RESULT_OK, paso);
                            // finish();

                            //adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Sin conexion a internet.",Toast.LENGTH_SHORT).show();
                        // progressDialog.dismiss();
                        /*if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(),"Upps algo anda mal",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(),"AuthFailureError",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getApplicationContext(),"ServerError",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(),"NetworkError",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getApplicationContext(),"ParseError",Toast.LENGTH_SHORT).show();
                        }*/

                    }
                }
        );


        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
        //   return ActaId;
    }


    private int mKeyTextSize = 24;
    private int mValueTextSize = 20;
    private int mValue2TextSize = 22;
    private int mLineTextSize = 18;
    private int mTitleTextSize = 40;
    /* BarCode Image size  */
    private int mBarcodeSize = 80;
    private int mOffsetX = 210;
    private int mStepDis = 1;
    private boolean bold = true;
    private float mCardWidth = 0;




    private Switch mToggleButton;
    private Button mStepButton;
    private Button mPrintButton;
    private Spinner mSpinner;
    private TextView mStatusTextView;

    private String mTitleStr = "Printer test ";
    private printer.PrintType mTitleType = printer.PrintType.Centering;
    private CheckBox mBoldCheckbox;
    private Spinner mAlignSpinner;
    private EditText mEditText;

    private boolean titleBold = true;

    printer mPrinter = new printer();

    public void printLine(int tam) {
        String str = "";
        for(int i = 0; i < tam; i++) str += " ";
        mPrinter.PrintStringEx(str, mTitleTextSize-20, true, false, mTitleType);
    }








    private void getActasLocales(){

        ActasArrayLocales = new ArrayList<>();
        actaArrayCompletalocal = new ArrayList<>();

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Cargando Actas...");
        pd.show();


        try {
            db =new DBHelper(this);
            try {

                db.createDataBase();
                db.openDataBase();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


            SQLiteDatabase sd = db.getReadableDatabase();
            //Cursor cursor = sd.rawQuery("Select * From t_acta ORDER BY acta_id DESC", null);
            //            cursor.moveToFirst();
            Cursor cursor = sd.rawQuery("Select * From t_acta  WHERE acta_operador = '" + mOperador + "' ORDER BY acta_id DESC", null);
            //            cursor.moveToFirst();



            while (cursor.moveToNext()){
                String acta_id = cursor.getString(cursor.getColumnIndex("acta_id"));
                String acta_preimpreso = cursor.getString(cursor.getColumnIndex("acta_preimpreso"));
                String acta_operador = cursor.getString(cursor.getColumnIndex("acta_operador"));
                String acta_fecha_registro = cursor.getString(cursor.getColumnIndex("acta_fecha_registro"));
                String acta_observaciones = cursor.getString(cursor.getColumnIndex("acta_observaciones"));
                String tipo_servicio = cursor.getString(cursor.getColumnIndex("tipo_servicio"));
                String acta_estado = cursor.getString(cursor.getColumnIndex("acta_estado"));
                String vehiculo_placa = cursor.getString(cursor.getColumnIndex("vehiculo_placa"));
                String vehiculo_ws = cursor.getString(cursor.getColumnIndex("vehiculo_ws"));
                String conductor_documento = cursor.getString(cursor.getColumnIndex("conductor_documento"));
                String conductor_dni = cursor.getString(cursor.getColumnIndex("conductor_dni"));
                String conductor_nombres = cursor.getString(cursor.getColumnIndex("conductor_nombres"));
                String conductor_apaterno = cursor.getString(cursor.getColumnIndex("conductor_apaterno"));
                String conductor_amaterno = cursor.getString(cursor.getColumnIndex("conductor_amaterno"));
                String licencia = cursor.getString(cursor.getColumnIndex("licencia"));
                String conductor_licencia_clase = cursor.getString(cursor.getColumnIndex("conductor_licencia_clase"));
                String conductor_licencia_categoria = cursor.getString(cursor.getColumnIndex("conductor_licencia_categoria"));
                String conductor_direccion = cursor.getString(cursor.getColumnIndex("conductor_direccion"));
                String conductor_ubigeo = cursor.getString(cursor.getColumnIndex("conductor_ubigeo"));
                String infraccion_tipo = cursor.getString(cursor.getColumnIndex("infraccion_tipo"));
                String infraccion_via = cursor.getString(cursor.getColumnIndex("infraccion_via"));
                String empresa_ruc = cursor.getString(cursor.getColumnIndex("empresa_ruc"));
                String empresa_rsocial = cursor.getString(cursor.getColumnIndex("empresa_rsocial"));
                String cobrador_dni = cursor.getString(cursor.getColumnIndex("cobrador_dni"));
                String cobrador_nombres = cursor.getString(cursor.getColumnIndex("cobrador_nombres"));
                String cobrador_apaterno = cursor.getString(cursor.getColumnIndex("cobrador_apaterno"));
                String cobrador_amaterno = cursor.getString(cursor.getColumnIndex("cobrador_amaterno"));
                String cobrador_direccion = cursor.getString(cursor.getColumnIndex("cobrador_direccion"));
                String cobrador_documento = cursor.getString(cursor.getColumnIndex("cobrador_documento"));
                String vehiculo_moto = cursor.getString(cursor.getColumnIndex("vehiculo_moto"));
                String empresa_direccion = cursor.getString(cursor.getColumnIndex("empresa_direccion"));
                String vehiculo_tarjeta = cursor.getString(cursor.getColumnIndex("vehiculo_tarjeta"));
                String cobrador_edad = cursor.getString(cursor.getColumnIndex("cobrador_edad"));
                int educativa = cursor.getInt(cursor.getColumnIndex("acta_educativa"));



                String msg = acta_id + " " + infraccion_tipo + " " + acta_fecha_registro+" "+cobrador_edad;
                // Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                //String[] temp_location = cursor.getString(cursor.getColumnIndex("LatLng")).split(",");
                //latlngParaderos.add(new LatLng(Double.parseDouble(temp_location[0]),Double.parseDouble(temp_location[1])));
                //slugParaderos.add(cursor.getString(cursor.getColumnIndex("Slug")));


                String reglamento_txt =  cursor.getString(cursor.getColumnIndex("reglamento_txt"));
                String tipo_infraccion_txt = cursor.getString(cursor.getColumnIndex("infraccion_tipo_txt"));

                String tipo_servicio_txt = cursor.getString(cursor.getColumnIndex("tipo_servicio_txt"));
                String codruta_transporte = cursor.getString(cursor.getColumnIndex("codruta_transporte"));
                /*String conductor_tipo_documento_txt = cursor.getString(cursor.getColumnIndex("conductor_documento_txt"));
                String cobrador_tipo_documento_txt = cursor.getString(cursor.getColumnIndex("cobrador_documento_txt"));
                String conductor_licencia_clase_txt = cursor.getString(cursor.getColumnIndex("clase_txt"));
                String conductor_licencia_categoria_txt = cursor.getString(cursor.getColumnIndex("categoria_txt"));
                String acta_tipo_servicio_txt = cursor.getString(cursor.getColumnIndex("tipo_servicio_txt"));*/

                Acta actcompleta = new Acta(acta_id,acta_preimpreso, acta_operador, acta_fecha_registro, acta_observaciones, tipo_servicio, acta_estado,
                        vehiculo_placa, vehiculo_ws, conductor_documento,conductor_dni,conductor_nombres,conductor_apaterno,conductor_amaterno,licencia,conductor_licencia_clase,conductor_licencia_categoria,conductor_direccion,conductor_ubigeo,infraccion_tipo,infraccion_via,empresa_ruc,empresa_rsocial,cobrador_dni,cobrador_nombres,cobrador_apaterno,cobrador_amaterno,cobrador_direccion,cobrador_documento,vehiculo_moto,empresa_direccion,vehiculo_tarjeta,cobrador_edad,
                        reglamento_txt, tipo_infraccion_txt, educativa,tipo_servicio_txt,codruta_transporte);



                Acta act = new Acta(acta_id,acta_preimpreso,acta_operador,acta_fecha_registro,vehiculo_placa,acta_estado,infraccion_tipo,conductor_nombres+" "+conductor_apaterno+" "+conductor_amaterno,infraccion_tipo, educativa, acta_fecha_registro);

                actaArrayCompletalocal.add(actcompleta);
                ActasArrayLocales.add(act);
                Log.i("holaa","gil");
            }
            //Acta act = new Acta("1","1","1","1","1","1","1","1","1");
            //actasArray.add(act);
            // Log.e("array inc22a",actasArray.size()+"");
            //actas=actasArray;
            recyclerView = null;
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view_actas);
            //data_list = new ArrayList<>();
            layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);


            adapter = new ActaAdapter(context, actaArrayCompletalocal);
            recyclerView.setAdapter(adapter);

            pd.dismiss();

            //wait1sec(500, pd);


            cursor.close();


        } catch (Exception e){
            e.printStackTrace();
        }

    }


    public void saveActas(String acta_preimpreso, String acta_operador, String acta_fecha_registro, String acta_observaciones, String acta_tipo_servicio,
                          String acta_estado, String vehiculo_placa, String vehiculo_ws, String conductor_tipo_documento,
                          String conductor_dni, String conductor_nombres, String conductor_apaterno, String conductor_amaterno,
                          String conductor_licencia, String conductor_licencia_clase, String conductor_licencia_categoria,
                          String conductor_direccion, String conductor_ubigeo, String infraccion_tipo, String infraccion_via,
                          String empresa_ruc, String empresa_rsocial, String cobrador_dni, String cobrador_nombres, String cobrador_apaterno,
                          String cobrador_amaterno, String cobrador_direccion, String cobrador_tipo_documento,
                          final String vehiculo_moto, final String empresa_direccion, final String vehiculo_tarjeta, final String cobrador_edad, final int peducativa) {

        final String pacta_preimpreso= acta_preimpreso;
        final String pacta_operador = acta_operador;
        final String pacta_fecha_registro=acta_fecha_registro;
        final String pacta_observaciones= acta_observaciones;
        final String pacta_tipo_servicio=acta_tipo_servicio;
        final String pacta_estado=acta_estado;
        final String pvehiculo_placa = vehiculo_placa;
        final String pvehiculo_ws=vehiculo_ws;
        final String pconductor_tipo_documento=conductor_tipo_documento;
        final String pconductor_dni=conductor_dni;
        final String pconductor_nombres=conductor_nombres;
        final String pconductor_apaterno=conductor_apaterno;
        final String pconductor_amaterno=conductor_amaterno;
        final String pconductor_licencia=conductor_licencia;
        final String pconductor_licencia_clase=conductor_licencia_clase;
        final String pconductor_licencia_categoria=conductor_licencia_categoria;
        final String pconductor_direccion=conductor_direccion;
        final String pconductor_ubigeo=conductor_ubigeo;
        final String pinfraccion_tipo=infraccion_tipo;
        final String pinfraccion_via=infraccion_via;
        final String pempresa_ruc=empresa_ruc;
        final String pempresa_rsocial=empresa_rsocial;
        final String pcobrador_dni=cobrador_dni;
        final String pcobrador_nombres=cobrador_nombres;
        final String pcobrador_apaterno=cobrador_apaterno;
        final String pcobrador_amaterno=cobrador_amaterno;
        final String pcobrador_direccion=cobrador_direccion;
        final String pcobrador_tipo_documento=cobrador_tipo_documento;

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Guardando...");
        pd.show();









        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                ROOT+URL_GUARDAR_ACTA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("gabi","primer");
                        pd.dismiss();
                        Log.d("response", response);
                        try {
                            //Toast.makeText(getApplicationContext(), "entrando", Toast.LENGTH_SHORT).show();
                            JSONArray jsnArr = new JSONArray(response);
                            JSONObject obj = jsnArr.getJSONObject(0);

                            //JSONObject obj = new JSONObject(response);


                            //Log.d("response", response);

                            if (!obj.getBoolean("error")){

                                //Log.d("")

                                String id_acta = obj.getString("id");

                                //actaParaImprimir.setActa_id(id_acta);



                                //Log.e("gabi",msg);

                                //Toast.makeText(getApplicationContext(), msg + " " + id_acta, Toast.LENGTH_SHORT).show();
                                //finish();

                                GIdActa = id_acta;

                                String msg = obj.getString("messsage");
                                Log.e("responsee",msg);
                                //Log.e("gabi",msg);

                                Toast.makeText(getApplicationContext(), msg + " " + id_acta, Toast.LENGTH_SHORT).show();
                                //finish();


                                //GIdActa = id_acta;

                                //myDialog.show();

                                Acta actaobj= actaArrayCompletalocal.get(posicion_general);
                                String acta_id = actaobj.getActa_id();






                                eliminarActa(Integer.parseInt(acta_id));

                                myDialogImp.show();
                                myDialogImp.setCancelable(false);


                            } else {
                                String msg = obj.getString("messsage");

                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        if (error instanceof TimeoutError /*|| error instanceof NoConnectionError*/) {
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

                params.put("acta_preimpreso",pacta_preimpreso );
                params.put("acta_operador",pacta_operador);
                params.put("acta_fecha_registro",pacta_fecha_registro);
                params.put("acta_observaciones",pacta_observaciones);
                params.put("acta_tipo_servicio",pacta_tipo_servicio+"");
                params.put("acta_estado",pacta_estado+"");
                params.put("vehiculo_placa",pvehiculo_placa);
                params.put("vehiculo_ws",pvehiculo_ws);
                params.put("conductor_tipo_documento", pconductor_tipo_documento+"");
                params.put("conductor_dni",pconductor_dni);
                params.put("conductor_nombres",pconductor_nombres);
                params.put("conductor_apaterno",pconductor_apaterno);
                params.put("conductor_amaterno", pconductor_amaterno);
                params.put("conductor_licencia",pconductor_licencia);
                params.put("conductor_licencia_clase",pconductor_licencia_clase+"");
                params.put("conductor_licencia_categoria",pconductor_licencia_categoria+"");
                params.put("conductor_direccion",pconductor_direccion );
                params.put("conductor_ubigeo",pconductor_ubigeo);
                params.put("infraccion_tipo",pinfraccion_tipo+"");
                params.put("infraccion_via",pinfraccion_via);
                params.put("empresa_ruc",pempresa_ruc );
                params.put("empresa_rsocial",pempresa_rsocial);
                params.put("cobrador_dni",pcobrador_dni);
                params.put("cobrador_nombres",pcobrador_nombres);
                params.put("cobrador_apaterno",pcobrador_apaterno );
                params.put("cobrador_amaterno",pcobrador_amaterno);
                params.put("cobrador_direccion",pcobrador_direccion);
                params.put("cobrador_tipo_documento",pcobrador_tipo_documento+"");

                params.put("vehiculo_moto",vehiculo_moto);
                params.put("empresa_direccion",empresa_direccion);
                params.put("vehiculo_tarjeta", vehiculo_tarjeta);
                params.put("cobrador_edad",cobrador_edad);




                params.put("educativa",peducativa+"");

                return params;
            }
        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }

    boolean tiene_focus = false;
    @Override
    public void onClick(View v) {


        //actas_local_tocado = 1 local
        //actas_local_tocado = 0 servidor
        //actas_local_tocado = 2 educativas
        if(v == tvActa) {
            /*tvActa.setVisibility(View.GONE);
            etbusqueda.setVisibility(View.VISIBLE);
            etbusqueda.requestFocus();

            InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            tiene_focus = true;*/
        }
        if(v == etbusqueda) {

        }
        if(v==atras) {
            finish();
        }
        if(v==obtener_actas_locales){
            acta_educativa = 0;
            recyclerView = null;
            layoutManager=null;
            actas_local_tocado=1;

            obtener_actas_servidor.setBackgroundResource(R.drawable.border_outline_desactivado);
            //obtener_actas_educativas.setBackgroundResource(R.drawable.border_outline_desactivado);
            obtener_actas_locales.setBackgroundResource(R.drawable.border_outline_activado);

            obtener_actas_servidor.setTextColor(Color.parseColor("#bdbdbd"));
            //obtener_actas_educativas.setTextColor(Color.parseColor("#bdbdbd"));
            obtener_actas_locales.setTextColor(Color.parseColor("#000000"));


            getActasLocales();
        }
        if(v==obtener_actas_servidor){
            actas_local_tocado=0;
            recyclerView = null;
            layoutManager=null;
            obtener_actas_servidor.setBackgroundResource(R.drawable.border_outline_activado);
            obtener_actas_locales.setBackgroundResource(R.drawable.border_outline_desactivado);
            //obtener_actas_educativas.setBackgroundResource(R.drawable.border_outline_desactivado);

            obtener_actas_locales.setTextColor(Color.parseColor("#bdbdbd"));
            //obtener_actas_educativas.setTextColor(Color.parseColor("#bdbdbd"));
            obtener_actas_servidor.setTextColor(Color.parseColor("#000000"));

            feca = new SimpleDateFormat("yyyy-MM-dd");
            currentDate = feca.format(new Date());

            getListaActas(mOperador,currentDate);
        }
        if(v==obtener_actas_educativas){
            acta_educativa = 1;
            actas_local_tocado=1;
            recyclerView = null;
            layoutManager=null;
            obtener_actas_educativas.setBackgroundResource(R.drawable.border_outline_activado);
            obtener_actas_locales.setBackgroundResource(R.drawable.border_outline_desactivado);
            obtener_actas_servidor.setBackgroundResource(R.drawable.border_outline_desactivado);

            obtener_actas_locales.setTextColor(Color.parseColor("#bdbdbd"));
            obtener_actas_servidor.setTextColor(Color.parseColor("#bdbdbd"));
            obtener_actas_educativas.setTextColor(Color.parseColor("#000000"));

            feca = new SimpleDateFormat("yyyy-MM-dd");
            currentDate = feca.format(new Date());

            getActasLocales();
        }
        if(v == tvClose) {
            myDialogImp.dismiss();
            //finish();
        }
        if(v == txtclose) {
            myDialog.dismiss();
        }

        if(v == ok) {
            int result = mPrinter.Open();

            if(result == 0)
                mPrinter.Step((byte)0x5f);
        }

        if(v == imprimir) {
            //Toast.makeText(getApplicationContext(), "IMPRIMIR " + GIdActa, Toast.LENGTH_SHORT).show();

            imprimirActa(GIdActa);
        }
    }


    public void imprimirActa(String id_acta) {




        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Cargando la impresion...");
        pd.show();




        String URL_GETACTAS = ROOT+"recuperar_acta&id="+id_acta;

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_GETACTAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  progressDialog.dismiss();
                        //  pd.dismiss();
                        try {

                            JSONArray jsnArr = new JSONArray(response);
                            Log.i("resspuesta",jsnArr+"");
                            JSONObject acta = jsnArr.getJSONObject(0);
                            String acta_id = getS(acta.getString("acta_id"));
                            String acta_preimpreso= getS(acta.getString("acta_preimpreso"));
                            String acta_operador = getS(acta.getString("acta_operador"));
                            String acta_fecha_registro = getS(acta.getString("acta_fecha_registro"));
                            String acta_observaciones= getS(acta.getString("acta_observaciones"));
                            String acta_tipo_servicio= getS(acta.getString("tipo_servicio"));
                            String acta_estado= getS(acta.getString("acta_estado"));
                            String vehiculo_placa = getS(acta.getString("vehiculo_placa"));
                            String vehiculo_ws= getS(acta.getString("vehiculo_ws"));
                            String conductor_tipo_documento= getS(acta.getString("conductor_documento"));
                            String conductor_dni= getS(acta.getString("conductor_dni"));
                            String conductor_nombres= getS(acta.getString("conductor_nombres"));
                            String conductor_apaterno= getS(acta.getString("conductor_apaterno"));
                            String conductor_amaterno= getS(acta.getString("conductor_amaterno"));
                            String conductor_licencia= getS(acta.getString("licencia"));
                            String conductor_licencia_clase=getS(acta.getString("conductor_licencia_clase"));
                            String conductor_licencia_categoria=getS(acta.getString("conductor_licencia_categoria"));
                            String conductor_direccion=getS(acta.getString("conductor_direccion"));
                            String conductor_ubigeo=getS(acta.getString("conductor_ubigeo"));
                            String infraccion_tipo=getS(acta.getString("infraccion_tipo"));
                            String infraccion_via=getS(acta.getString("infraccion_via"));
                            String empresa_ruc=getS(acta.getString("empresa_ruc"));
                            String empresa_rsocial=getS(acta.getString("empresa_rsocial"));
                            String cobrador_dni=getS(acta.getString("cobrador_dni"));
                            String cobrador_nombres=getS(acta.getString("cobrador_nombres"));
                            String cobrador_apaterno=getS(acta.getString("cobrador_apaterno"));
                            String cobrador_amaterno=getS(acta.getString("cobrador_amaterno"));
                            String cobrador_direccion=getS(acta.getString("cobrador_direccion"));
                            String cobrador_tipo_documento=getS(acta.getString("cobrador_documento"));
                            String cobrador_edad= getS(acta.getString("cobrador_edad"));
                            String empresa_direccion = acta.getString("empresa_direccion");// getS(acta.getString("empresa_direccion"));
                            String vehiculo_tarjeta = getS(acta.getString("vehiculo_tarjeta"));
                            String vehiculo_moto = getS(acta.getString("vehiculo_moto"));

                            String conductor_licencia_completa = getS(acta.getString("licencia_completa"));//para imprimir

                            String reglamento_txt =  acta.getString("reglamento");
                            String tipo_infraccion_txt = acta.getString("infraccion_tipo");
                            String conductor_tipo_documento_txt = acta.getString("conductor_documento");
                            String cobrador_tipo_documento_txt = acta.getString("cobrador_documento");
                            String conductor_licencia_clase_txt = acta.getString("clase");
                            String conductor_licencia_categoria_txt = acta.getString("categoria");
                            String acta_tipo_servicio_txt = acta.getString("tipo_servicio");

                            String ruta = acta.getString("ruta");
                            //String ruta = "ruta";


                            String educativa = acta.getString("educativa");
                            int peducativa = Integer.parseInt(educativa);

                            ActaId= new Acta(acta_id,acta_preimpreso,acta_operador,acta_fecha_registro,acta_observaciones,acta_tipo_servicio,acta_estado,
                                    vehiculo_placa,vehiculo_ws,conductor_tipo_documento,conductor_dni,conductor_nombres,conductor_apaterno,conductor_amaterno,
                                    conductor_licencia,conductor_licencia_clase,conductor_licencia_categoria,conductor_direccion,conductor_ubigeo,
                                    infraccion_tipo,infraccion_via,empresa_ruc,empresa_rsocial,cobrador_dni,cobrador_nombres,cobrador_apaterno,cobrador_amaterno,
                                    cobrador_direccion,cobrador_tipo_documento,vehiculo_moto,empresa_direccion,vehiculo_tarjeta,cobrador_edad);

                            Log.i("acta final",ActaId+" "+ActaId.getActa_operador()+"ño");
                            //  Log.i("acta recuperada_id",actaobj.getActa_id()+"");
                            Intent paso = new Intent(getApplicationContext(), Actas_formulario.class);
                            paso.putExtra("acta",(Serializable) ActaId);
                            pd.dismiss();
                            //wait1sec(10*1000, pd);
                            //finish();
                            myDialogImp.dismiss();

                            if(vehiculo_moto.equals("1")) {
                                vehiculo_moto = "( MOTO )";
                            } else {
                                vehiculo_moto = "";
                            }

                            String tipo_acta = "ACTA DE CONTROL";

                            if(educativa.equals("1")) {
                                tipo_acta = "ACTA EDUCATIVA";
                            }

                            /*testPrint2(acta_preimpreso, "20177217043", empresa_rsocial, empresa_direccion, empresa_ruc, acta_fecha_registro,
                                    infraccion_via, conductor_nombres, conductor_apaterno + " " + conductor_amaterno, conductor_direccion,
                                    conductor_dni, conductor_licencia_completa, vehiculo_placa, cobrador_nombres, cobrador_apaterno + " " + cobrador_amaterno,
                                    cobrador_direccion, cobrador_dni, cobrador_edad,acta_tipo_servicio, empresa_rsocial, reglamento_txt + " : " + infraccion_tipo, vehiculo_moto, vehiculo_tarjeta,
                                    acta_observaciones
                            );*/


                            infraccion_tipo = reglamento_txt + " : " + infraccion_tipo;

                            //Toast.makeText(getApplicationContext(), imp_pacta_preimpreso, Toast.LENGTH_SHORT).show();

                            String arr[] = acta_fecha_registro.split(" ");
                            String otro[] = arr[0].split("-");
                            String year = otro[0];
                            String month = otro[1];
                            String day = otro[2];

                            String otro2[] = arr[1].split(":");
                            String horas = otro2[0];
                            String minutos = otro2[1];

                            String fecha = day + "/" + month + "/" +  year;
                            String hora = horas + ":" + minutos;

                            acta_fecha_registro = fecha + " " + hora;




                            testPrint4(tipo_acta, acta_preimpreso, "20177217043", empresa_rsocial, ruta, empresa_direccion, empresa_ruc, acta_fecha_registro,
                                    infraccion_via, conductor_nombres, conductor_apaterno + " " + conductor_amaterno, conductor_direccion,
                                    conductor_dni, conductor_licencia_completa, vehiculo_placa, cobrador_nombres, cobrador_apaterno + " " + cobrador_amaterno,
                                    cobrador_direccion, cobrador_dni, cobrador_edad,acta_tipo_servicio, empresa_rsocial,   infraccion_tipo, vehiculo_moto, vehiculo_tarjeta,
                                    acta_observaciones
                            );

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
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
                }
        );


        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
        //   return ActaId;



    }


    public void eliminarActa(int id)
    {
        try {
            db = new DBHelper(this);
            try {

                db.createDataBase();
                db.openDataBase();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            SQLiteDatabase sd = db.getWritableDatabase();




            //Toast.makeText(getApplicationContext(),"JAJAJA", Toast.LENGTH_SHORT).show();


            sd.execSQL("DELETE FROM t_acta WHERE acta_id ='" + id + "'");
            db.close(); // Closing database connection
            getActasLocales();




        } catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("yiyi", e.getMessage());
            e.printStackTrace();
        }
    }







    private void testPrint3(  String pre_impreso,String ruc, String nombre_razon_social, String domicilio, String dni_ruc,String fecha, String lugar_intervencion,
                              String cnombres, String capellidos,String cdomicilio,String cdni,String clicencia_clase_cat, String cplaca_rodaje,
                              String cobnombres,String cobapellidos, String cobdomicilio, String cobdni, String cobedad,String  modalidad, String codruta,String codinfraccion) {

        mTitleStr =  "77023812";
        String mRazonSocial = "Willy Rosa";
        String mDireccion = "Manahua 2E";
        String mDpto = "Cusco";
        String mProvincia = "Cusco";
        String mDistrito = "Santiago";

        int c = 18;
        int s = 5;
        String str = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";
        int sz = str.length();
        //str = "";
        String str2 = "";
        //for(int i = 0; i < sz; i++) { str += '-';  str2 += '.'; }


        int result = mPrinter.Open();
        if(result == 0) {

            /*Bitmap bm=BitmapFactory.decodeResource(getResources(), R.drawable.escudo9);
            mPrinter.PrintBitmap(bm);*/

            mPrinter.PrintStringEx("GERENCIA DE TRANSITO VIALIDAD Y TRANSPORTE", mTitleTextSize-20, false, false, mTitleType);

            mPrinter.printBlankLine(20);


            mPrinter.PrintStringEx("ACTA DE CONTROL", mTitleTextSize-10, false, true, mTitleType);
            mPrinter.PrintStringEx(pre_impreso, mTitleTextSize-15, false, true, mTitleType);
            mPrinter.PrintStringEx("RUC : " + ruc, mTitleTextSize-15, false, false, mTitleType);

            mPrinter.printBlankLine(9);






            mPrinter.printBlankLine(9);

            /*COBRADOR SECTION BEGIN*/

            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();

            mPrinter.PrintStringEx("DATOS DEL COBRADOR", mTitleTextSize-19, false, true, mTitleType);

            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();

            mPrinter.printBlankLine(6);
            mPrinter.PrintStringEx("Nombres y Apellidos", mTitleTextSize-18, false, true, mTitleType);
            mPrinter.PrintStringEx(cobnombres + " " + cobapellidos, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(6);




            mPrinter.PrintLineInit(40);
            mPrinter.PrintLineStringByType("", mKeyTextSize, printer.PrintType.Right, true);//160
            mPrinter.PrintLineEnd();

            mPrinter.printBlankLine(40);

            mPrinter.Close();
        }


    }

    public void wait1sec(final int tiempo, final ProgressDialog pd) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                    }
                }, tiempo);

            }
        });

    }


    private void testPrint2(  String pre_impreso,String ruc, String nombre_razon_social, String domicilio, String dni_ruc,String fecha, String lugar_intervencion,
                              String cnombres, String capellidos,String cdomicilio,String cdni,String clicencia_clase_cat, String cplaca_rodaje,
                              String cobnombres,String cobapellidos, String cobdomicilio, String cobdni, String cobedad,String  modalidad, String codruta,String codinfraccion,
                              String vehiculo_moto, String vehiculo_tarjeta, String acta_obs ) {

        mTitleStr =  "77023812";
        String mRazonSocial = "Willy Rosa";
        String mDireccion = "Manahua 2E";
        String mDpto = "Cusco";
        String mProvincia = "Cusco";
        String mDistrito = "Santiago";

        int c = 18;
        int s = 5;
        String str = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";
        int sz = str.length();
        //str = "";
        String str2 = "";
        //for(int i = 0; i < sz; i++) { str += '-';  str2 += '.'; }


        int result = mPrinter.Open();
        if(result == 0) {

            Bitmap bm=BitmapFactory.decodeResource(getResources(), R.drawable.escudo9);
            mPrinter.PrintBitmap(bm);

            mPrinter.PrintStringEx("GERENCIA DE TRANSITO VIALIDAD Y TRANSPORTE", mTitleTextSize-20, false, false, mTitleType);

            mPrinter.printBlankLine(20);


            mPrinter.PrintStringEx("ACTA DE CONTROL", mTitleTextSize-10, false, true, mTitleType);
            mPrinter.PrintStringEx(pre_impreso, mTitleTextSize-15, false, true, mTitleType);
            mPrinter.PrintStringEx("RUC : " + ruc, mTitleTextSize-15, false, false, mTitleType);

            mPrinter.printBlankLine(9);


            /*TRANSPORTISTA SECTION BEGIN*/

            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();

            mPrinter.PrintStringEx("DATOS DEL TRANSPORTISTA", mTitleTextSize-19, false, true, mTitleType);

            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();


            mPrinter.printBlankLine(4);




            mPrinter.PrintStringEx("Nombre o Razon Social", mTitleTextSize-18, false, true, mTitleType);
            mPrinter.PrintStringEx(nombre_razon_social, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);

            mPrinter.PrintStringEx("Domicilio", mTitleTextSize-18, false, true, mTitleType);
            mPrinter.PrintStringEx(domicilio, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);

            String ss = "DNI";
            if(dni_ruc.length() > 8) ss = "RUC";
            mPrinter.PrintStringEx(ss, mTitleTextSize-18, false, true, mTitleType);
            mPrinter.PrintStringEx(dni_ruc, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);

            mPrinter.PrintStringEx("Fecha y Hora", mTitleTextSize-18, false, true, mTitleType);
            mPrinter.PrintStringEx(fecha, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);

            mPrinter.PrintStringEx("Lugar de Intervencion", mTitleTextSize-18, false, true, mTitleType);
            mPrinter.PrintStringEx(lugar_intervencion, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);

            mPrinter.printBlankLine(5);

            /*CONDUCTOR SECTION FIN*/





            /*CONDUCTOR SECTION BEGIN*/

            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();

            mPrinter.PrintStringEx("DATOS DEL CONDUCTOR AL VOLANTE", mTitleTextSize-19, false, true, mTitleType);

            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();

            mPrinter.printBlankLine(4);


            mPrinter.PrintStringEx("Nombres y Apellidos", mTitleTextSize-18, false, true, mTitleType);
            mPrinter.PrintStringEx(cnombres + " " + capellidos, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);

            /*mPrinter.PrintStringEx("Apellidos", mTitleTextSize-18, false, true, mTitleType);
            mPrinter.PrintStringEx(capellidos, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(6);*/

            mPrinter.PrintStringEx("Domicilio", mTitleTextSize-18, false, true, mTitleType);
            mPrinter.PrintStringEx(cdomicilio, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);

            mPrinter.PrintStringEx("DNI", mTitleTextSize-18, false, true, mTitleType);
            mPrinter.PrintStringEx(cdni, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);

            mPrinter.PrintStringEx("Licencia, clase y categoria", mTitleTextSize-18, false, true, mTitleType);
            mPrinter.PrintStringEx(clicencia_clase_cat , mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);

            mPrinter.PrintStringEx("Placa de rodaje", mTitleTextSize-18, false, true, mTitleType);
            mPrinter.PrintStringEx(cplaca_rodaje + " " + vehiculo_moto, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.PrintStringEx( vehiculo_tarjeta, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);

            mPrinter.printBlankLine(5);

            /*CONDUCTOR SECTION end*/



            if(!cobnombres.equals("")&&!cobapellidos.equals("")&&!cobdomicilio.equals("")&&!cobdni.equals("")&&!cobedad.equals("")) {


                /*COBRADOR SECTION BEGIN*/

                mPrinter.PrintLineInit(mLineTextSize);
                mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
                mPrinter.PrintLineEnd();

                mPrinter.PrintStringEx("DATOS DEL COBRADOR", mTitleTextSize - 19, false, true, mTitleType);

                mPrinter.PrintLineInit(mLineTextSize);
                mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
                mPrinter.PrintLineEnd();

                mPrinter.printBlankLine(4);
                mPrinter.PrintStringEx("Nombres Y Apellidos", mTitleTextSize - 18, false, true, mTitleType);
                mPrinter.PrintStringEx(cobnombres + " " + cobapellidos, mTitleTextSize - 20, false, false, mTitleType);
                mPrinter.printBlankLine(4);

            /*mPrinter.PrintStringEx("Apellidos", mTitleTextSize-18, false, true, mTitleType);
            mPrinter.PrintStringEx(cobapellidos, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(6);*/

                mPrinter.PrintStringEx("Domicilio", mTitleTextSize - 18, false, true, mTitleType);
                mPrinter.PrintStringEx(cobdomicilio, mTitleTextSize - 20, false, false, mTitleType);
                mPrinter.printBlankLine(4);

                mPrinter.PrintStringEx("DNI", mTitleTextSize - 18, false, true, mTitleType);
                mPrinter.PrintStringEx(cobdni, mTitleTextSize - 20, false, false, mTitleType);
                mPrinter.printBlankLine(4);

                mPrinter.PrintStringEx("Edad", mTitleTextSize - 18, false, true, mTitleType);
                mPrinter.PrintStringEx(cobedad, mTitleTextSize - 20, false, false, mTitleType);
                mPrinter.printBlankLine(4);

                /*COBRADOR SECTION FIN*/

                mPrinter.printBlankLine(5);
            }



            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();

            mPrinter.PrintStringEx("DE LA ACCION DE CONTROL SE VERIFICO LO SIGUIENTE", mTitleTextSize-19, false, true, mTitleType);

            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();

            mPrinter.PrintStringEx(acta_obs, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);



            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();




            mPrinter.PrintStringEx("MODALIDAD DE SERVICIO DE TRANSPORTE", mTitleTextSize-19, false, true, mTitleType);

            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();


            mPrinter.PrintStringEx(modalidad, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);


            mPrinter.printBlankLine(5);


            /*mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();

            mPrinter.PrintStringEx("CODIGO DE RUTA DE TRANSPORTE", mTitleTextSize-19, false, true, mTitleType);

            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();

            mPrinter.printBlankLine(6);


            mPrinter.printBlankLine(9);*/

            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();

            mPrinter.PrintStringEx("CODIGO DE INFRACCION", mTitleTextSize-19, false, true, mTitleType);

            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();


            mPrinter.PrintStringEx(codinfraccion, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);




            mPrinter.printBlankLine(5);

            mPrinter.PrintStringEx("OBSERVACIONES DEL INTERVENIDO", mTitleTextSize-19, false, true, mTitleType);

            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();


            mPrinter.printBlankLine(10);

            mPrinter.printBlankLine(40);


            Bitmap bme=BitmapFactory.decodeResource(getResources(), R.drawable.footer4);
            mPrinter.PrintBitmap(bme);


            mPrinter.PrintLineInit(40);
            mPrinter.PrintLineStringByType("", mKeyTextSize, printer.PrintType.Right, true);//160
            mPrinter.PrintLineEnd();

            mPrinter.printBlankLine(40);

            mPrinter.Close();
        }


    }



    private void testPrint4(  String tipo_acta, String pre_impreso,String ruc, String nombre_razon_social, String cod_ruta_empresa, String domicilio, String dni_ruc,String fecha, String lugar_intervencion,
                              String cnombres, String capellidos,String cdomicilio,String cdni,String clicencia_clase_cat, String cplaca_rodaje,
                              String cobnombres,String cobapellidos, String cobdomicilio, String cobdni, String cobedad,String  modalidad, String codruta,String codinfraccion,
                              String vehiculo_moto, String vehiculo_tarjeta, String acta_obs ) {

        mTitleStr =  "77023812";



        String str = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";
        String ttt = "---------------------------------------------";


        String config = SharedPrefManager.getInstance(getApplicationContext()).getConfig();

        String tiempo_anulacion = "";
        String tiempo_emergencia = "";
        String url_upload = "";
        String usuario_ftp = "";
        String pw_ftp = "";
        String numero_sms = "";
        String numero_emergencia = "";
        String texto_blanco = "";

        try {
            JSONArray jsonArray = new JSONArray(config);



            JSONObject obj = jsonArray.getJSONObject(0);
            tiempo_anulacion = obj.getString("tiempo_anulacion");
            tiempo_emergencia = obj.getString("tiempo_emergencia");
            url_upload = obj.getString("url_upload");
            usuario_ftp = obj.getString("usuario_ftp");
            pw_ftp = obj.getString("pw_ftp");
            numero_sms = obj.getString("numero_sms");
            numero_emergencia = obj.getString("numero_emergencia");
            texto_blanco = obj.getString("texto_blanco");

            //Toast.makeText(getApplicationContext(), tiempo_anulacion, Toast.LENGTH_SHORT).show();



        } catch (JSONException e) {
            e.printStackTrace();
        }


        int result = mPrinter.Open();
        if(result == 0) {

            Bitmap bm=BitmapFactory.decodeResource(getResources(), R.drawable.logoheader);
            mPrinter.PrintBitmap(bmHeader);

            //mPrinter.PrintStringEx("GERENCIA DE TRANSITO VIALIDAD Y TRANSPORTE", mTitleTextSize-20, false, false, mTitleType);

            mPrinter.printBlankLine(20);


            mPrinter.PrintStringEx(tipo_acta, mTitleTextSize-10, false, true, mTitleType);
            mPrinter.PrintStringEx(pre_impreso, mTitleTextSize-15, false, true, mTitleType);
            mPrinter.PrintStringEx("RUC : " + ruc, mTitleTextSize-15, false, false, mTitleType);

            mPrinter.printBlankLine(9);



            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();

            mPrinter.PrintStringEx("DATOS DEL TRANSPORTISTA", mTitleTextSize-19, false, true, mTitleType);

            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();


            mPrinter.printBlankLine(4);




            mPrinter.PrintStringEx("Nombre o Razón Social", mTitleTextSize-18, false, true, mTitleType);
            if(nombre_razon_social.length()==0||nombre_razon_social==null) nombre_razon_social=texto_blanco;
            mPrinter.PrintStringEx(nombre_razon_social, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);

            mPrinter.PrintStringEx("Domicilio", mTitleTextSize-18, false, true, mTitleType);
            if(domicilio.length()==0||domicilio==null) domicilio=texto_blanco;
            mPrinter.PrintStringEx(domicilio, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);

            String ss = "DNI:";
            if(dni_ruc.length() > 8) ss = "RUC:";


            mPrinter.PrintLineInit(mKeyTextSize);
            mPrinter.PrintLineString(ss, mKeyTextSize, mOffsetX-95, true);
            if(dni_ruc.length()==0||dni_ruc==null) dni_ruc=texto_blanco;
            mPrinter.PrintLineString(dni_ruc, mValue2TextSize, mOffsetX-40, false);//160
            mPrinter.PrintLineEnd();


            mPrinter.printBlankLine(4);

            mPrinter.PrintLineInit(mKeyTextSize-2);
            mPrinter.PrintLineString("Fecha y Hora:", mKeyTextSize-2, mOffsetX-181, true);
            if(fecha.length()==0||fecha==null) fecha=texto_blanco;
            mPrinter.PrintLineString(fecha, mValue2TextSize-2, mOffsetX-42, false);//160
            mPrinter.PrintLineEnd();


            mPrinter.printBlankLine(4);

            mPrinter.PrintStringEx("Lugar de Intervención", mTitleTextSize-18, false, true, mTitleType);
            if(lugar_intervencion.length()==0||lugar_intervencion==null) lugar_intervencion=texto_blanco;
            mPrinter.PrintStringEx(lugar_intervencion, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);

            mPrinter.printBlankLine(5);


            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();

            mPrinter.PrintStringEx("DATOS DEL CONDUCTOR AL VOLANTE", mTitleTextSize-19, false, true, mTitleType);

            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();

            mPrinter.printBlankLine(4);


            mPrinter.PrintStringEx("Nombres y Apellidos", mTitleTextSize-18, false, true, mTitleType);
            mPrinter.PrintStringEx(cnombres + " " + capellidos, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);

            mPrinter.PrintStringEx("Domicilio", mTitleTextSize-18, false, true, mTitleType);
            if(cdomicilio.length()==0||cdomicilio==null) cdomicilio=texto_blanco;
            mPrinter.PrintStringEx(cdomicilio, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);


            mPrinter.PrintLineInit(mKeyTextSize);
            mPrinter.PrintLineString("DNI:", mKeyTextSize, mOffsetX-90, true);
            if(cdni.length()==0||cdni==null) cdni=texto_blanco;
            mPrinter.PrintLineString(cdni, mValue2TextSize, mOffsetX-40, false);//160
            mPrinter.PrintLineEnd();
            mPrinter.printBlankLine(4);



            mPrinter.PrintStringEx("Licencia, clase y categoría", mTitleTextSize-18, false, true, mTitleType);
            if(clicencia_clase_cat.length()==0||clicencia_clase_cat==null) clicencia_clase_cat=texto_blanco;
            mPrinter.PrintStringEx(clicencia_clase_cat , mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);


            mPrinter.PrintLineInit(mKeyTextSize-2);
            mPrinter.PrintLineString("Placa de rodaje:", mKeyTextSize-2, mOffsetX-142, true);
            if(cplaca_rodaje.length()==0||cplaca_rodaje==null) cplaca_rodaje=texto_blanco;
            mPrinter.PrintLineString(cplaca_rodaje, mValue2TextSize, mOffsetX+20, false);//160
            mPrinter.PrintLineEnd();
            mPrinter.printBlankLine(4);


            mPrinter.PrintLineInit(mKeyTextSize-2);
            mPrinter.PrintLineString("N° Tarjeta:", mKeyTextSize-2, mOffsetX-125, true);
            if(vehiculo_tarjeta.length()==0||vehiculo_tarjeta==null) vehiculo_tarjeta=texto_blanco;
            mPrinter.PrintLineString(vehiculo_tarjeta, mValue2TextSize, mOffsetX-15, false);//160
            mPrinter.PrintLineEnd();
            mPrinter.printBlankLine(4);



            mPrinter.printBlankLine(5);


            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();

            mPrinter.PrintStringEx("DATOS DEL COBRADOR", mTitleTextSize - 19, false, true, mTitleType);

            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();

            mPrinter.printBlankLine(4);
            mPrinter.PrintStringEx("Nombres y Apellidos", mTitleTextSize - 18, false, true, mTitleType);
            if(cobnombres.length()==0||cobnombres==null) cobnombres=texto_blanco;
            if(cobapellidos.length()==0||cobapellidos==null) cobapellidos=texto_blanco;
            mPrinter.PrintStringEx(cobnombres + " " + cobapellidos, mTitleTextSize - 20, false, false, mTitleType);
            mPrinter.printBlankLine(4);

            mPrinter.PrintStringEx("Domicilio", mTitleTextSize - 18, false, true, mTitleType);
            if(cobdomicilio.length()==0||cobdomicilio==null) cobdomicilio=texto_blanco;
            mPrinter.PrintStringEx(cobdomicilio, mTitleTextSize - 20, false, false, mTitleType);
            mPrinter.printBlankLine(4);


            mPrinter.PrintLineInit(mKeyTextSize);
            mPrinter.PrintLineString("DNI:", mKeyTextSize, mOffsetX-90, true);
            if(cobdni.length()==0||cobdni==null) cobdni=texto_blanco;
            mPrinter.PrintLineString(cobdni, mValue2TextSize, mOffsetX-40, false);//160
            mPrinter.PrintLineEnd();
            mPrinter.printBlankLine(4);


            mPrinter.PrintLineInit(mKeyTextSize);
            mPrinter.PrintLineString("Edad:", mKeyTextSize, mOffsetX-105+40, true);
            if(cobedad.length()==0||cobedad==null) cobedad=texto_blanco;
            mPrinter.PrintLineString(cobedad, mValue2TextSize, mOffsetX-40+40, false);//160
            mPrinter.PrintLineEnd();
            mPrinter.printBlankLine(4);


            mPrinter.printBlankLine(5);




            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();

            //mPrinter.PrintStringEx("DE LA ACCION DE CONTROL SE VERIFICO LO SIGUIENTE", mTitleTextSize-19, false, true, mTitleType);
            mPrinter.PrintStringEx("DE LA ACCION DE CONTROL SE", mTitleTextSize-19, false, true, mTitleType);
            mPrinter.PrintStringEx("VERIFICO LO SIGUIENTE", mTitleTextSize-19, false, true, mTitleType);

            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();

            if(acta_obs.length()==0||acta_obs==null) acta_obs=texto_blanco;

            mPrinter.PrintStringEx(acta_obs, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);





            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();



            mPrinter.PrintStringEx("MODALIDAD DE SERVICIO DE", mTitleTextSize-19, false, true, mTitleType);
            mPrinter.PrintStringEx("TRANSPORTE", mTitleTextSize-19, false, true, mTitleType);

            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();

            if(modalidad.length()==0||modalidad==null) modalidad=texto_blanco;
            mPrinter.PrintStringEx(modalidad, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);


            mPrinter.printBlankLine(5);




            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();

            mPrinter.PrintStringEx("CODIGO DE RUTA DE TRANSPORTE", mTitleTextSize-19, false, true, mTitleType);

            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();

            if(cod_ruta_empresa.length()==0||cod_ruta_empresa==null) cod_ruta_empresa=texto_blanco;
            mPrinter.PrintStringEx(cod_ruta_empresa, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);


            mPrinter.printBlankLine(5);




            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();

            mPrinter.PrintStringEx("CODIGO DE INFRACCION", mTitleTextSize-19, false, true, mTitleType);

            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();

            if(codinfraccion.length()==0||codinfraccion==null) codinfraccion=texto_blanco;

            mPrinter.PrintStringEx(codinfraccion, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);

            mPrinter.printBlankLine(5);





            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();

            mPrinter.PrintStringEx("OBSERVACIONES DEL INTERVENIDO", mTitleTextSize-19, false, true, mTitleType);

            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();


            mPrinter.printBlankLine(10);

            for(int i = 0; i < 10; i++) {
                mPrinter.PrintStringEx("                                                                        ", mTitleTextSize-19, true, true, mTitleType);
                mPrinter.printBlankLine(13);
            }


            Bitmap bme=BitmapFactory.decodeResource(getResources(), R.drawable.footer7);

            /*if(chbeducativa.isChecked())
                mPrinter.PrintBitmap(bmEducativa);
            else
                mPrinter.PrintBitmap(bmFooter);*/

            if(tipo_acta.equals("ACTA DE CONTROL")) {
                mPrinter.PrintBitmap(bmFooter);
            } else {
                mPrinter.PrintBitmap(bmEducativa);
            }


            mPrinter.PrintLineInit(40);
            mPrinter.PrintLineStringByType("", mKeyTextSize, printer.PrintType.Right, true);//160
            mPrinter.PrintLineEnd();

            mPrinter.printBlankLine(40);

            mPrinter.Close();
        }

        //03.aabaa.15
    }



    public String getS(String str){

        return str;
        /*String out = null;
        try {
            out = new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;*/
    }

    private Bitmap setBitMapOfImage(String imgname)
    {
        Bitmap b = null;


        try {

            File f = new File(Environment.getExternalStorageDirectory() + "/Fiscapp/" + imgname);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            Log.d("gg", e.getMessage());
            e.printStackTrace();
        }

        return b;

    }
}
