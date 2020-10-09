package com.fiscapp.fiscapp.Model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.fiscapp.fiscapp.DBHelper;
import com.fiscapp.fiscapp.R;
import com.fiscapp.fiscapp.SharedPrefManager;
import com.fiscapp.fiscapp.UtilityPhoto;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import hardware.print.printer;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.fiscapp.fiscapp.Constants.URL_GUARDAR_ACTA;
import static com.fiscapp.fiscapp.Constants.URL_LISTAR_DOCUMENTO;
import static com.fiscapp.fiscapp.Constants.URL_LISTAR_E_TRANSP;
import static com.fiscapp.fiscapp.Constants.URL_LISTAR_LICENCIA_CATEGORIA;
import static com.fiscapp.fiscapp.Constants.URL_LISTAR_LICENCIA_CLASE;
import static com.fiscapp.fiscapp.Constants.URL_LISTAR_REGLAMENTO;
import static com.fiscapp.fiscapp.Constants.URL_LISTAR_TIPOSERVICIO;


public class Actas_formulario extends AppCompatActivity implements
        View.OnClickListener,AdapterView.OnItemSelectedListener{

    TextView reglamento,infraccion_tipo, tvclaseLicencia, tvCatLicencia, tvImgClose, tvMensajeInfraccion;
    TextView tvclaseLicenciaSugerida, tvCatLicenciaSugerida ;

    Dialog myDialog, myDialogImg, myDialogPreImp;

    String tipo_impresion = "online";

    Dialog myDialogInfraccionesPas;

    String sunarp_vehiculo_ws="#";

    DBHelper db ;

    ImageView imgAdjuntada;

    ScrollView myScrollView;

    RecyclerView recyclerViewInf;
    RecyclerView.LayoutManager layoutManagerInf;
    InfraccionesAdapter adapterInf;
    List<Infracciones> infraccionesArray;

    TextView tvPreImpClose;
    EditText etPreImpreso;
    Button boPreImpAceptar;



    EditText acta_preimpreso,
            acta_operador,
            acta_fecha_registro,
            acta_observaciones,
            acta_tipo_servicio,
            acta_estado,
            vehiculo_placa,
            vehiculo_ws,
            conductor_tipo_documento,
            conductor_dni,
            conductor_nombres,
            conductor_apaterno,
            conductor_amaterno,
            conductor_licencia,
            conductor_licencia_clase,
            conductor_licencia_categoria,
            conductor_direccion,
            conductor_ubigeo,
            conductor_tarjeta,
            cobrador_edad,
            infraccion_via,
            empresa_ruc,
            empresa_rsocial,
            empresa_direccion,
            cobrador_dni,
            cobrador_nombres,
            cobrador_apaterno,
            cobrador_amaterno,
            cobrador_direccion,
            cobrador_tipo_documento;


    String responseReglamento, responseTipoServicio, responseTipoDocumento, responseEmpresaTransportes, responseClaseLicencia;

    ArrayList <String> spinner_Array_reglamento;

    Acta actaParaImprimir;


    //UNA SOLA COSA QUE DEMUESTRE Q JESUS TRAGEDIO LA LEY

    String reglamento_ws="",infraccion_ws="",tipo_acta_servicio_ws = "",tipo_documento_ws="",tipo_cobrador_doc_ws="",
            clase_licencia_ws="",categoria_licencia_ws="",razon_social_empresa_ws="";

    String imp_codRutaEmpresa = "", imp_empresa_rsocial = "";

    Spinner sreglamento, sinfraccion_tipo;
    Button guardar,validar_infraccion,completar_datos_conductor,completar_datos_cobrador;
    ImageButton  spinner_reglamento,spinner_tipoinfraccion,spinner_rsocial,spinner_tipo_doc_conductor,spinner_tipo_doc_cobrador,
            spinner_clase_licencia,spinner_categoria_licencia,spinner_tipo_acta_servicio;
    CheckBox moto, chbeducativa;
    String Permitir_Acta_gen="1";
    Acta ActaId;
    String currentDate;
    private ProgressDialog progressDialog;

    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> items_infraccion = new ArrayList<>();
    ArrayList<String> items_acta_tipo_servicio = new ArrayList<>();
    ArrayList<String> items_tipo_doc = new ArrayList<>();
    ArrayList<String> items_clase_licencia = new ArrayList<>();
    ArrayList<String> items_categoria_licencia = new ArrayList<>();
    ArrayList<String> items_razon_social_empresa = new ArrayList<>();

    SpinnerDialog spinnerDialog;


    Button imprimir, ok, boVerImagen, boadjuntar;
    TextView tvClose;

    String file_path = "", file_name = "";


    TextView tvDatosCobrador, tvTipoDocCobrador, tvDoctoCobrador, tvNombresCobrador, tvApCobrador, tvAmCobrador,
            tvDireccionConductor, tvEdadCobrador;

    LinearLayout lacbtipodocumento, laDocCobrador;


    public void showSpinner() {
        //spinnerDialog = new SpinnerDialog(Actas_formulario.this, items, "Elija una opcion : Reglamento");

        spinnerDialog=new SpinnerDialog(Actas_formulario.this,items,"Elija una opcion : Reglamento",R.style.DialogAnimations_SmileWindow);// With 	Animation

        //spinnerDialog=new SpinnerDialog(Actas_formulario.this,items,"Select or Search City","Close Button Text");
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                //Toast.makeText(getApplicationContext(), "Selected: " + i, Toast.LENGTH_SHORT).show();
                reglamento.setText(s);
                reglamento_ws= reglaIds.get(i);
            }
        });
        spinnerDialog.showSpinerDialog();
    }

    public void showSpinnerInfraccion() {
        spinnerDialog = new SpinnerDialog(Actas_formulario.this, items_infraccion, "Elija una opcion",R.style.DialogAnimations_SmileWindow);
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                //Toast.makeText(getApplicationContext(), "Selected: " + i, Toast.LENGTH_SHORT).show();
                infraccion_tipo.setText(s);
                infraccion_ws= InfraccionIds.get(i);

            }
        });
        spinnerDialog.showSpinerDialog();
    }

    public void showSpinnerTipoActaServicio() {
        spinnerDialog = new SpinnerDialog(Actas_formulario.this, items_acta_tipo_servicio, "Elija una opcion : T. Acta de Servicio",R.style.DialogAnimations_SmileWindow);
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                //Toast.makeText(getApplicationContext(), "Selected: " + i, Toast.LENGTH_SHORT).show();
                acta_tipo_servicio.setText(s);
                tipo_acta_servicio_ws= TipoActaSerIds.get(i);

                cobrador_tipo_documento.setText("");
                cobrador_dni.setText("");

                cobrador_nombres.setText("");
                cobrador_apaterno.setText("");
                cobrador_amaterno.setText("");
                cobrador_direccion.setText("");
                cobrador_edad.setText("");
                tipo_cobrador_doc_ws = "";

                checkTipoServicio(s);

            }
        });
        spinnerDialog.showSpinerDialog();
    }

    public void showSpinnerTipoDocumento() {
        spinnerDialog = new SpinnerDialog(Actas_formulario.this, items_tipo_doc, "Elija una opcion : Tipo Doc",R.style.DialogAnimations_SmileWindow);
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                //Toast.makeText(getApplicationContext(), "Selected: " + i, Toast.LENGTH_SHORT).show();
                conductor_tipo_documento.setText(s);
                tipo_documento_ws= TipoDocId.get(i);

            }
        });
        spinnerDialog.showSpinerDialog();
    }

    public void showSpinnerTipoDocumentoCobrador() {
        spinnerDialog = new SpinnerDialog(Actas_formulario.this, items_tipo_doc, "Elija una opcion : Tipo Doc",R.style.DialogAnimations_SmileWindow);
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                //Toast.makeText(getApplicationContext(), "Selected: " + i, Toast.LENGTH_SHORT).show();
                cobrador_tipo_documento.setText(s);
                tipo_cobrador_doc_ws= TipoDocId.get(i);

            }
        });
        spinnerDialog.showSpinerDialog();
    }

    public void showClaseLicencia() {
        spinnerDialog = new SpinnerDialog(Actas_formulario.this, items_clase_licencia, "Elija una opcion : Clase Licencia",R.style.DialogAnimations_SmileWindow);
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                //Toast.makeText(getApplicationContext(), "Selected: " + i, Toast.LENGTH_SHORT).show();
                conductor_licencia_clase.setText(s);
                clase_licencia_ws= ClaseLicIds.get(i);

            }
        });
        spinnerDialog.showSpinerDialog();
    }

    public void showCategoriaLicencia() {
        spinnerDialog = new SpinnerDialog(Actas_formulario.this, items_categoria_licencia, "Elija una opcion : Cat Licencia",R.style.DialogAnimations_SmileWindow);
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                //Toast.makeText(getApplicationContext(), "Selected: " + i, Toast.LENGTH_SHORT).show();
                conductor_licencia_categoria.setText(s);
                categoria_licencia_ws= CategoriaLicIds.get(i);

            }
        });
        spinnerDialog.showSpinerDialog();
    }
    public void showRazonSocialEmpresa() {
        spinnerDialog = new SpinnerDialog(Actas_formulario.this, items_razon_social_empresa,
                "Elija una opcion : R.S Empresa",R.style.DialogAnimations_SmileWindow);
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                //Toast.makeText(getApplicationContext(), "Selected: " + i, Toast.LENGTH_SHORT).show();
                empresa_rsocial.setText(s);
                razon_social_empresa_ws= Empresa_Id.get(i);
                imp_codRutaEmpresa = Empresa_Ruta.get(i);
                imp_empresa_rsocial = Empresa_Rsocial.get(i);
                String ruc = Empresa_Ruc.get(i);
                String domicilio = Empresa_Domicilio.get(i);

                empresa_direccion.setText(domicilio);
                empresa_ruc.setText(ruc);
                //Toast.makeText(getApplicationContext(),ruc + "-" + domicilio + "-"+razon_social_empresa_ws + " - " + imp_codRutaEmpresa + " - " +  imp_empresa_rsocial, Toast.LENGTH_SHORT).show();


            }
        });
        spinnerDialog.showSpinerDialog();
    }


    private TextWatcher getTextWatcher(final EditText editText) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // do what you want with your EditText
                //editText.setText("blabla");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s=editable.toString();
                if(!s.equals(s.toUpperCase()))
                {
                    s=s.toUpperCase();
                    editText.setText(s);
                }
                //Toast.makeText(getApplicationContext(), "haha", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void checkTipoServicio(String tipo_servicio) {
        if(tipo_servicio.equals("Transporte Urbano e Interurbano")) {

            //Toast.makeText(getApplicationContext(), "SI ES", Toast.LENGTH_SHORT).show();

            tvDatosCobrador.setVisibility(View.VISIBLE);
            tvTipoDocCobrador.setVisibility(View.VISIBLE);
            tvDoctoCobrador.setVisibility(View.VISIBLE);

            lacbtipodocumento.setVisibility(View.VISIBLE);
            laDocCobrador.setVisibility(View.VISIBLE);



            tvNombresCobrador.setVisibility(View.VISIBLE);
            tvApCobrador.setVisibility(View.VISIBLE);
            tvAmCobrador.setVisibility(View.VISIBLE);
            tvDireccionConductor.setVisibility(View.VISIBLE);
            tvEdadCobrador.setVisibility(View.VISIBLE);

            cobrador_nombres.setVisibility(View.VISIBLE);
            cobrador_apaterno.setVisibility(View.VISIBLE);
            cobrador_amaterno.setVisibility(View.VISIBLE);
            cobrador_direccion.setVisibility(View.VISIBLE);
            cobrador_edad.setVisibility(View.VISIBLE);








        } else {
            //Toast.makeText(getApplicationContext(), "NO ES", Toast.LENGTH_SHORT).show();
            tvDatosCobrador.setVisibility(View.GONE);
            tvTipoDocCobrador.setVisibility(View.GONE);
            tvDoctoCobrador.setVisibility(View.GONE);
            lacbtipodocumento.setVisibility(View.GONE);
            laDocCobrador.setVisibility(View.GONE);

            tvNombresCobrador.setVisibility(View.GONE);
            tvApCobrador.setVisibility(View.GONE);
            tvAmCobrador.setVisibility(View.GONE);
            tvDireccionConductor.setVisibility(View.GONE);
            tvEdadCobrador.setVisibility(View.GONE);

            cobrador_nombres.setVisibility(View.GONE);
            cobrador_apaterno.setVisibility(View.GONE);
            cobrador_amaterno.setVisibility(View.GONE);
            cobrador_direccion.setVisibility(View.GONE);
            cobrador_edad.setVisibility(View.GONE);
        }
    }


    ArrayList<String> arrStr = new ArrayList<>();
    String imgNameHeader = "header.png";
    String imgNameFooter = "footer.png";
    String imgNameEducativa = "educativa_footer.png";

    Bitmap bmHeader, bmFooter, bmEducativa;

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

    String ROOT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actas_formulario);


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






        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        chbeducativa = (CheckBox) findViewById(R.id.chbeducativa);

        mUsuarioID = SharedPrefManager.getInstance(getApplicationContext()).getUserKeyId() + "";
        mOperador = SharedPrefManager.getInstance(getApplicationContext()).getUserKeyUsuario();

        completar_datos_conductor = (Button) findViewById(R.id.boCompletarConductor_cDNI);
        completar_datos_cobrador = (Button) findViewById(R.id.boCompletarCobrador_cDNI);
        spinner_reglamento = (ImageButton) findViewById(R.id.boReglamento);
        spinner_tipoinfraccion = (ImageButton) findViewById(R.id.boInfraccion_tipo);
        spinner_rsocial = (ImageButton) findViewById(R.id.boRazonSocial);
        spinner_clase_licencia = (ImageButton) findViewById(R.id.boClase_licencia);
        spinner_categoria_licencia = (ImageButton) findViewById(R.id.boCategoria_licencia);
        spinner_tipo_doc_conductor = (ImageButton) findViewById(R.id.boTipo_doc_conductor);
        spinner_tipo_doc_cobrador = (ImageButton) findViewById(R.id.boTipo_doc_cobrador);
        spinner_tipo_acta_servicio = (ImageButton) findViewById(R.id.boActa_tipo_servicio);

        tvDatosCobrador = (TextView) findViewById(R.id.tvDatosCobrador);
        tvTipoDocCobrador = (TextView) findViewById(R.id.tvTipoDocCobrador);
        tvDoctoCobrador = (TextView) findViewById(R.id.tvDoctoCobrador);
        tvNombresCobrador = (TextView) findViewById(R.id.tvNombresCobrador);
        tvApCobrador = (TextView) findViewById(R.id.tvApCobrador);
        tvAmCobrador = (TextView) findViewById(R.id.tvAmCobrador);
        tvDireccionConductor = (TextView) findViewById(R.id.tvDireccionConductor);
        tvEdadCobrador = (TextView) findViewById(R.id.tvEdadCobrador);



        lacbtipodocumento = (LinearLayout) findViewById(R.id.lacbtipodocumento);
        laDocCobrador = (LinearLayout) findViewById(R.id.laDocCobrador);




        db = new DBHelper(this);

        //acta_preimpreso = (EditText) findViewById(R.id.facta_impreso);
        //acta_operador = (EditText) findViewById(R.id.facta_operador);
        //acta_fecha_registro = (EditText) findViewById(R.id.facta_fecha_registro);
        acta_observaciones = (EditText) findViewById(R.id.facta_observaciones);
        acta_tipo_servicio = (EditText) findViewById(R.id.facta_tipo_servicio);
        acta_tipo_servicio.setEnabled(false);
        //acta_estado = (EditText) findViewById(R.id.facta_estado);
        vehiculo_placa = (EditText) findViewById(R.id.fvehiculo_placa);
        vehiculo_placa.requestFocus();
        //vehiculo_ws = (EditText) findViewById(R.id.fvehiculo_ws);
        conductor_tipo_documento = (EditText) findViewById(R.id.fconductor_tipo_documento);
        conductor_tipo_documento.setEnabled(false);
        conductor_dni = (EditText) findViewById(R.id.fconductor_dni);
        conductor_nombres = (EditText) findViewById(R.id.fconductor_nombres);
        conductor_apaterno = (EditText) findViewById(R.id.fconductor_apaterno);
        conductor_amaterno = (EditText) findViewById(R.id.fconductor_amaterno);
        conductor_licencia = (EditText) findViewById(R.id.fconductor_licencia);
        conductor_licencia_clase = (EditText) findViewById(R.id.fconductor_licencia_clase);
        conductor_licencia_clase.setEnabled(false);
        conductor_licencia_categoria = (EditText) findViewById(R.id.fconductor_licencia_categoria);
        conductor_licencia_categoria.setEnabled(false);
        conductor_direccion = (EditText) findViewById(R.id.fconductor_direccion);
        conductor_ubigeo = (EditText) findViewById(R.id.fconductor_ubigeo);
        cobrador_edad = (EditText) findViewById(R.id.fcobrador_edad);
        conductor_tarjeta = (EditText) findViewById(R.id.ftarjeta_vehiculo);
        reglamento = (TextView) findViewById(R.id.freglamento);
        reglamento.setEnabled(false);
        infraccion_tipo = (TextView) findViewById(R.id.finfraccion_tipo);
        infraccion_tipo.setEnabled(false);
        infraccion_via = (EditText) findViewById(R.id.finfraccion_via);
        empresa_ruc = (EditText) findViewById(R.id.fempresa_ruc);
        empresa_rsocial = (EditText) findViewById(R.id.fempresa_rsocial);
        empresa_direccion=(EditText) findViewById(R.id.fempresa_direccion);
        cobrador_dni = (EditText) findViewById(R.id.fcobrador_dni);
        cobrador_nombres = (EditText) findViewById(R.id.fcobrador_nombres);
        cobrador_apaterno = (EditText) findViewById(R.id.fcobrador_apaterno);
        cobrador_amaterno = (EditText) findViewById(R.id.fcobrador_amaterno);
        cobrador_direccion = (EditText) findViewById(R.id.fcobrador_direccion);
        cobrador_tipo_documento = (EditText) findViewById(R.id.fcobrador_tipo_documento);
        cobrador_tipo_documento.setEnabled(false);

    //empresa_direccion empresa_ruc
        empresa_direccion.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        vehiculo_placa.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        conductor_nombres.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        conductor_apaterno.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        conductor_amaterno.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        conductor_licencia.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        conductor_licencia_clase.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        conductor_licencia_categoria.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        conductor_direccion.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        conductor_ubigeo.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        conductor_tarjeta.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        infraccion_via.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        empresa_ruc.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        empresa_rsocial.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        cobrador_nombres.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        cobrador_apaterno.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        cobrador_amaterno.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        cobrador_direccion.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        cobrador_amaterno.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        acta_observaciones.setFilters(new InputFilter[] {new InputFilter.AllCaps()});



        //empresa_direccion.addTextChangedListener(getTextWatcher(empresa_direccion));

        moto =(CheckBox) findViewById(R.id.moto);

        tvclaseLicencia = (TextView) findViewById(R.id.tvclaseLicencia);
        tvCatLicencia = (TextView) findViewById(R.id.tvCatLicencia);

        tvclaseLicenciaSugerida = (TextView) findViewById(R.id.tvclaseLicenciaSugerida);
        tvCatLicenciaSugerida = (TextView) findViewById(R.id.tvCatLicenciaSugerida);

        myScrollView = (ScrollView) findViewById(R.id.myScrollView);

        //incializar array de spinner
        //probar spinner
       /* spinner_Array_reglamento = new ArrayList<>();
        for(int i=0; i<50;i++){
            spinner_Array_reglamento.add("hola");
        }
*/
        //addListenerOnSpinnerItemSelection();
        //lennado sniper reglamento




        //progreess dialog

        progressDialog = new ProgressDialog(this);

        guardar = (Button) findViewById(R.id.boGuardarActa);
        validar_infraccion =(Button) findViewById(R.id.validar_infraccion_pasada);
        Acta acta = (Acta) getIntent().getSerializableExtra("acta");
        if(acta != null)
        {
            // Log.i("acta desde act for",acta_id);
            infraccion_tipo.setText(acta.getTipo_infraccion_txt());
            infraccion_ws = acta.getInfraccion_tipo();
            vehiculo_placa.setText(acta.getVehiculo_placa());
            infraccion_via.setText(acta.getInfraccion_tipo());

            sunarp_vehiculo_ws = acta.getVehiculo_ws();

            //ACTA PREIMPRESO
            //acta_preimpreso.setText(acta.getActa_preimpreso());
            //acta_operador.setText(acta.getActa_operador());
            //acta_fecha_registro.setText(acta.getActa_fecha_registro());
            acta_observaciones.setText(acta.getActa_observaciones());
            acta_tipo_servicio.setText(acta.getActa_tipo_servicio_txt());

            checkTipoServicio(acta_tipo_servicio.getText().toString());

            tipo_acta_servicio_ws = acta.getActa_tipo_servicio();
            reglamento.setText(acta.getReglamento_txt());
            reglamento_ws = acta.getReglamento();
            //acta_estado.setText(acta.getActa_estado());
            //vehiculo_ws.setText(acta.getVehiculo_ws());
            conductor_tipo_documento.setText(acta.getConductor_tipo_documento_txt());
            tipo_documento_ws = acta.getConductor_tipo_documento();
            conductor_dni.setText(acta.getConductor_dni());
            conductor_nombres.setText(acta.getConductor_nombres());
            conductor_apaterno.setText(acta.getCobrador_apaterno());
            conductor_amaterno.setText(acta.getConductor_amaterno());
            conductor_licencia.setText(acta.getConductor_licencia());
            conductor_licencia_clase.setText(acta.getConductor_licencia_clase_txt());
            clase_licencia_ws = acta.getConductor_licencia_clase();
            conductor_licencia_categoria.setText(acta.getConductor_licencia_categoria_txt());
            categoria_licencia_ws = acta.getConductor_licencia_categoria();
            conductor_direccion.setText(acta.getConductor_direccion());
            conductor_ubigeo.setText(acta.getConductor_ubigeo());
            conductor_tarjeta.setText(acta.getVehiculo_tarjeta());
            empresa_ruc.setText(acta.getEmpresa_ruc());
            empresa_rsocial.setText(acta.getEmpresa_rsocial());
            empresa_direccion.setText(acta.getEmpresa_direccion());
            cobrador_tipo_documento.setText(acta.getCobrador_tipo_documento_txt());
            tipo_cobrador_doc_ws = acta.getCobrador_tipo_documento();
            cobrador_dni.setText(acta.getCobrador_dni());
            cobrador_nombres.setText(acta.getCobrador_nombres());
            cobrador_apaterno.setText(acta.getCobrador_apaterno());
            cobrador_amaterno.setText(acta.getCobrador_amaterno());
            cobrador_direccion.setText(acta.getCobrador_direccion());
            cobrador_edad.setText(acta.getCobrador_edad());
            String motos = acta.getVehiculo_moto();

            int educativa = acta.getActa_educativa();
            Log.e("moto",motos);
            //Toast.makeText(getApplicationContext(), motos, Toast.LENGTH_SHORT).show();
            if(motos.equals("1")){
                moto.setChecked(true);
            }
            else {
                moto.setChecked(false);
            }

            if(educativa == 1){
                chbeducativa.setChecked(true);
            }
            else {
                chbeducativa.setChecked(false);
            }
        }

        /*saveActa(2, "INFF4", "2018-10-20");
        getActas();*/

        getReglamento();
        getTipoActaServicio();
        getTipoDocumento();
        getEmpresaTransportes();
        getClaseLicencia();



        myDialogPreImp = new Dialog(Actas_formulario.this);
        myDialogPreImp.setContentView(R.layout.modal_preimpreso);
        myDialogPreImp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tvPreImpClose = (TextView) myDialogPreImp.findViewById(R.id.tvPreImpClose);
        etPreImpreso = (EditText) myDialogPreImp.findViewById(R.id.etPreImpreso);
        boPreImpAceptar = (Button) myDialogPreImp.findViewById(R.id.boPreImpAceptar);

        tvPreImpClose.setOnClickListener(this);
        boPreImpAceptar.setOnClickListener(this);

        myDialog = new Dialog(Actas_formulario.this);
        myDialog.setContentView(R.layout.modal_acta);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        boadjuntar = (Button) findViewById(R.id.boadjuntar);


        imprimir = (Button) myDialog.findViewById(R.id.boImprimir);
        ok = (Button) myDialog.findViewById(R.id.boOk);
        tvClose = (TextView) myDialog.findViewById(R.id.tvClose);

        imprimir.setOnClickListener(this);
        ok.setOnClickListener(this);
        tvClose.setOnClickListener(this);
        boadjuntar.setOnClickListener(this);





        myDialogImg = new Dialog(Actas_formulario.this);
        myDialogImg.setContentView(R.layout.modal_imagen);
        myDialogImg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        imgAdjuntada = (ImageView) myDialogImg.findViewById(R.id.imgAdjuntada);
        tvImgClose = (TextView) myDialogImg.findViewById(R.id.tvImgClose);

        boVerImagen = (Button) findViewById(R.id.boVerImagen);

        tvImgClose.setOnClickListener(this);
        boVerImagen.setOnClickListener(this);






        //acta_operador.setText("hola");
        spinner_reglamento.setOnClickListener(this);
        spinner_tipoinfraccion.setOnClickListener(this);
        validar_infraccion.setOnClickListener(this);
        spinner_tipo_acta_servicio.setOnClickListener(this);
        spinner_tipo_doc_cobrador.setOnClickListener(this);
        spinner_tipo_doc_conductor.setOnClickListener(this);
        spinner_clase_licencia.setOnClickListener(this);
        spinner_categoria_licencia.setOnClickListener(this);
        spinner_rsocial.setOnClickListener(this);
        guardar.setOnClickListener(this);
        completar_datos_conductor.setOnClickListener(this);
        completar_datos_cobrador.setOnClickListener(this);


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //EditText searchTo = (EditText)findViewById(R.id.medittext);
        vehiculo_placa.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Permitir_Acta_gen ="1";
                Log.i("acta cambiada placa",Permitir_Acta_gen);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //doSomething();
                // Log.i("textchangeg","caracter:"+s+" start"+start+" before"+before+" count"+count);
            }
        });

        myDialogInfraccionesPas = new Dialog(Actas_formulario.this);
        //View indicar_motivo = ActasActivity.this.getLayoutInflater().inflate(R.layout.motivo_acta, null);
        myDialogInfraccionesPas.setContentView(R.layout.modal_infracciones_pasadas);

        myDialogInfraccionesPas.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //  mBottomSheetDialog.show();

        recyclerViewInf = (RecyclerView) myDialogInfraccionesPas.findViewById(R.id.rvInfracciones);
        tvMensajeInfraccion = (TextView) myDialogInfraccionesPas.findViewById(R.id.tvMensaje_infraccion);
        tvMensajeInfraccion.setVisibility(View.GONE);
        Button cerrar = (Button) myDialogInfraccionesPas.findViewById(R.id.cerrar_infraccion);


        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit code here;
                //Toast.makeText(getApplicationContext(), "cancelo", Toast.LENGTH_SHORT).show();
                myDialogInfraccionesPas.dismiss();
            }
        });


        /*infraccion_tipo.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Permitir_Acta_gen ="1";
                Log.i("Acta id_infraccion",Permitir_Acta_gen);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //doSomething();
                Log.i("textchangeg","caracter:"+s+" start"+start+" before"+before+" count"+count);
            }
        });






*/
    }

    boolean infraccPasClicked;

    public  void getListaInfracciones(final String placa,final String infraccion_id) {
        if(infraccPasClicked) {
            return;
        }
        infraccPasClicked=true;
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Cargando infracciones pasadas");
        pd.show();

        //progressDialog.setMessage("Haciendo Magia...");
        //progressDialog.show();

        infraccionesArray = new ArrayList<>();
        String URL_GETACTAS = ROOT + "validar_infraccion_pasada&placa="+placa+"&infraccion_id="+infraccion_id;
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_GETACTAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        infraccPasClicked = false;
                        try {
                            JSONArray jsnArr = new JSONArray(response);

                            //SharedPrefManager.getInstance(getApplicationContext()).setresponseListadoActas(response);
                            if(jsnArr.length() == 0){
                                recyclerViewInf.setVisibility(View.GONE);
                                tvMensajeInfraccion.setVisibility(View.VISIBLE);
                                myDialogInfraccionesPas.show();
                            }
                            else{
                                for(int i=0;i<jsnArr.length();i++){

                                    // Get current json object
                                    JSONObject infraccion_pasada = jsnArr.getJSONObject(i);
                                    String Vehiculo_Placa = infraccion_pasada.getString("Vehiculo_Placa");
                                    String Infracciones_Fecha_Infraccion=infraccion_pasada.getString("Infracciones_Fecha_Infraccion");
                                    String Infracciones_Preimpreso=infraccion_pasada.getString("Infracciones_Preimpreso");
                                    String Infracciones_Situacion = infraccion_pasada.getString("Infracciones_Situacion");
                                    String Estado_Descripcion=infraccion_pasada.getString("Estado_Descripcion");
                                    String Infracciones_obs=infraccion_pasada.getString("Infracciones_obs");
                                    String Infraccion_Numero = infraccion_pasada.getString("Infraccion_Numero");
                                    String Infraccion_Descripcion=infraccion_pasada.getString("Infraccion_Descripcion");
                                    String Permitir_Acta=infraccion_pasada.getString("Permitir_Acta");

                                    Infracciones infra_pas = new Infracciones(Vehiculo_Placa,Infracciones_Fecha_Infraccion,Infracciones_Preimpreso,Infracciones_Situacion,Estado_Descripcion,Infracciones_obs,Infraccion_Numero,Infraccion_Descripcion,Permitir_Acta);
                                    infraccionesArray.add(infra_pas);
                                    Log.i("infracciones","gil");
                                }
                                Log.e("array infracciones",infraccionesArray.size()+"");
                                //recyclerViewInf = null;
                                recyclerViewInf = (RecyclerView) myDialogInfraccionesPas.findViewById(R.id.rvInfracciones);
                                layoutManagerInf = new LinearLayoutManager(myDialogInfraccionesPas.getContext());
                                recyclerViewInf.setLayoutManager(layoutManagerInf);
                                adapterInf = new InfraccionesAdapter(myDialogInfraccionesPas.getContext(), infraccionesArray);
                                recyclerViewInf.setAdapter(adapterInf);
                                adapterInf.notifyDataSetChanged();
                                //isOnResume = true;
                                recyclerViewInf.setVisibility(View.VISIBLE);
                                tvMensajeInfraccion.setVisibility(View.GONE);
                                myDialogInfraccionesPas.show();}

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        recyclerViewInf.setVisibility(View.GONE);
                        tvMensajeInfraccion.setText("Sin acceso a internet");
                        tvMensajeInfraccion.setVisibility(View.VISIBLE);
                        infraccPasClicked = false;
                        myDialogInfraccionesPas.show();



                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
        //   return ActaId;
    }

    public void saveActa(int id, String infraccion, String fecha)
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


            sd.execSQL("INSERT INTO TActa VALUES(" +  id +  ",'" + infraccion + "','" + fecha + "')");
            db.close(); // Closing database connection



        } catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("yiyi", e.getMessage());
            e.printStackTrace();
        }
    }

    private void getActas(){
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
            Cursor cursor = sd.rawQuery("Select * From TActa", null);
//            cursor.moveToFirst();
            while (cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String infraccion = cursor.getString(cursor.getColumnIndex("infraccion"));
                String fecha = cursor.getString(cursor.getColumnIndex("fecha"));
                String msg = id + " " + infraccion + " " + fecha;
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                //String[] temp_location = cursor.getString(cursor.getColumnIndex("LatLng")).split(",");
                /*latlngParaderos.add(new LatLng(Double.parseDouble(temp_location[0]),Double.parseDouble(temp_location[1])));
                slugParaderos.add(cursor.getString(cursor.getColumnIndex("Slug")));
                nombreParaderos.add(cursor.getString(cursor.getColumnIndex("Nombre")));*/
            }

            cursor.close();


        } catch (Exception e){
            e.printStackTrace();
        }

    }


    private final void focusOnView(){
        myScrollView.post(new Runnable() {
            @Override
            public void run() {
                myScrollView.scrollTo(0, 320*8);
            }
        });
    }



    public  void recuperarEmpresa(final String id) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Cargando ws vehiculo...");
        pd.show();


        ///*final ArrayList<Acta>*/ actasArray = new ArrayList<>();
        String URL_GETACTAS = ROOT+"recuperar_empresa&id="+id;
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_GETACTAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {

                            JSONArray jsnArr = new JSONArray(response);


                            JSONObject DatosLicencia_sunarp = jsnArr.getJSONObject(0);
                            sunarp_vehiculo_ws = DatosLicencia_sunarp.toString();
                            Log.e("sunarp_vehiculo",sunarp_vehiculo_ws);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
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
        ){

        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }


    public  void RecuperarWsVehiculo(final String placa) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Cargando ws vehiculo...");
        pd.show();


        ///*final ArrayList<Acta>*/ actasArray = new ArrayList<>();
        String URL_RECUPERAR_WSVEHICULO = ROOT+"ws_sunarp&placa="+placa;;
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_RECUPERAR_WSVEHICULO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {

                            JSONArray jsnArr = new JSONArray(response);


                            JSONObject DatosLicencia_sunarp = jsnArr.getJSONObject(0);
                            sunarp_vehiculo_ws = DatosLicencia_sunarp.toString();
                            Log.e("sunarp_vehiculo",sunarp_vehiculo_ws);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
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
        ){

        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }

    public  void RecuperarDatosCobrador_cDNI(final String DNI) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Cargando datos...");
        pd.show();


        ///*final ArrayList<Acta>*/ actasArray = new ArrayList<>();
        String URL_GETACTAS = ROOT+"ws_dni&dni="+DNI;
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_GETACTAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {

                            JSONArray jsnArr = new JSONArray(response);
                            Log.e("long reglamento",jsnArr.length()+"");
                            // Get current json object
                            JSONObject Datos = jsnArr.getJSONObject(0);
                            if (!Datos.getBoolean("error")){

                                String dni = Datos.getString("dni");
                                String nombres=Datos.getString("nombres");
                                String apaterno=Datos.getString("apaterno");
                                String aMaterno = Datos.getString("amaterno");
                                String Direccion = Datos.getString("direccion");
                                String Ubigeo = Datos.getString("ubigeo");


                                cobrador_nombres.setText(nombres);
                                cobrador_apaterno.setText(apaterno);
                                cobrador_amaterno.setText(aMaterno);
                                cobrador_direccion.setText(Direccion);}
                            else{
                                String msg = Datos.getString("message");

                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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
        ){

        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }

    public  void RecuperarDatosConductor_cDNI(final String DNI) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Cargando datos...");
        pd.show();


        ///*final ArrayList<Acta>*/ actasArray = new ArrayList<>();
        String URL_GETACTAS = ROOT+"ws_dni&dni="+DNI;
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_GETACTAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {

                            JSONArray jsnArr = new JSONArray(response);
                            Log.e("long reglamento",jsnArr.length()+"");
                            // Get current json object
                            JSONObject Datos = jsnArr.getJSONObject(0);

                            if (!Datos.getBoolean("error")){
                                String dni = Datos.getString("dni");
                                String nombres=Datos.getString("nombres");
                                String apaterno=Datos.getString("apaterno");
                                String aMaterno = Datos.getString("amaterno");
                                String Direccion = Datos.getString("direccion");
                                String Ubigeo = Datos.getString("ubigeo");


                                conductor_nombres.setText(nombres);
                                conductor_apaterno.setText(apaterno);
                                conductor_amaterno.setText(aMaterno);
                                conductor_direccion.setText(Direccion);
                                conductor_ubigeo.setText(Ubigeo);
                            }
                            else{
                                String msg = Datos.getString("message");

                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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
        ){

        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }

    public  void RecuperarLicencia_cDNI(final String DNI) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Cargando datos licencia...");
        pd.show();


        ///*final ArrayList<Acta>*/ actasArray = new ArrayList<>();
        String URL_GETACTAS = ROOT+"ws_licencia&dni="+DNI;
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_GETACTAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {

                            JSONArray jsnArr = new JSONArray(response);




                            // Get current json object
                            JSONObject DatosLicencia = jsnArr.getJSONObject(0);
                            if(!DatosLicencia.getBoolean("error")) {
                                String licencia = DatosLicencia.getString("licencia");
                                String licenciaClase = DatosLicencia.getString("clase");
                                String licenciaCategoria = DatosLicencia.getString("categoria");

                                conductor_licencia.setText(licencia);
                                /*conductor_licencia_clase.setText(licenciaClase);
                                conductor_licencia_categoria.setText(licenciaCategoria);*/

                                tvclaseLicenciaSugerida.setText(licenciaClase);
                                tvCatLicenciaSugerida.setText(licenciaCategoria);


                                //Log.i("holaa","gil2");


                                //showCategoriaLicencia();
                                //             Log.e("array inc22a",actasArray.size()+"");
                            }
                            else
                            {
                                String msg = DatosLicencia.getString("message");

                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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
        ){

        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }

    public void addListenerOnSpinnerItemSelection() {
        try {
            //spinner = (Spinner) findViewById(R.id.spinner);

            final int listsize = spinner_Array_reglamento.size() - 1;
            ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, spinner_Array_reglamento);
            //  ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(this, R.layout.spinner, spinnerArray);

            userAdapter.setDropDownViewResource(R.layout.spinner_item);

            /*{
                @Override
                public int getCount() {
                    return(listsize); // Truncate the list
                }
            };*/
            sreglamento.setAdapter(userAdapter);
            // spinner.setSelection(userAdapter.getCount());
            sreglamento.setOnItemSelectedListener(this);
        } catch (Exception ex) {
            Log.e("error", ex + "");
        }
    }
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
      /*  Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_SHORT).show();*/
        //  tvincidencia.setText( parent.getItemAtPosition(pos).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private String GIdActa;


    public void imprimirActaOffline() {
        //  Log.i("acta recuperada_id",actaobj.getActa_id()+"");

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Imprimiendo acta " + imp_pacta_preimpreso);
        pd.show();
        pd.setCancelable(false);

        wait1sec(15*1000, pd);





        //myDialog.dismiss();
        String tipo_acta = "ACTA DE CONTROL";
        if(imp_vehiculo_moto.equals("1")) {
            imp_vehiculo_moto = "( MOTO )";
        } else {
            imp_vehiculo_moto = "";
        }

        if(chbeducativa.isChecked()) {
            tipo_acta = "ACTA EDUCATIVA";
        }

        imp_ptipo_infraccion_txt = imp_preglamento_txt + " : " + imp_ptipo_infraccion_txt;

        //Toast.makeText(getApplicationContext(), imp_pacta_preimpreso, Toast.LENGTH_SHORT).show();

        String arr[] = imp_pacta_fecha_registro.split(" ");
        String otro[] = arr[0].split("-");
        String year = otro[0];
        String month = otro[1];
        String day = otro[2];

        String otro2[] = arr[1].split(":");
        String horas = otro2[0];
        String minutos = otro2[1];

        String fecha = day + "/" + month + "/" +  year;
        String hora = horas + ":" + minutos;

        imp_pacta_fecha_registro = fecha + " " + hora;

        if(imp_pacta_preimpreso==null)imp_pacta_preimpreso="";
        if(imp_empresa_rsocial==null)imp_empresa_rsocial="";
        if(imp_codRutaEmpresa==null)imp_codRutaEmpresa="";
        if(imp_empresa_direccionp==null)imp_empresa_direccionp="";
        if(imp_pempresa_ruc==null)imp_pempresa_ruc="";
        if(imp_pacta_fecha_registro==null)imp_pacta_fecha_registro="";
        if(imp_pinfraccion_via==null)imp_pinfraccion_via="";
        if(imp_pconductor_nombres==null)imp_pconductor_nombres="";
        if(imp_pconductor_apaterno==null)imp_pconductor_apaterno="";
        if(imp_pconductor_amaterno==null)imp_pconductor_amaterno="";
        if(imp_pconductor_direccion==null)imp_pconductor_direccion="";
        if(imp_pconductor_dni==null)imp_pconductor_dni="";
        if(imp_pvehiculo_placa==null)imp_pvehiculo_placa="";
        if(imp_pcobrador_nombres==null)imp_pcobrador_nombres="";
        if(imp_pcobrador_apaterno==null)imp_pcobrador_apaterno="";
        if(imp_pcobrador_amaterno==null)imp_pcobrador_amaterno="";
        if(imp_pcobrador_direccion==null)imp_pcobrador_direccion="";
        if(imp_pcobrador_dni==null)imp_pcobrador_dni="";
        if(imp_pcobrador_edad==null)imp_pcobrador_edad="";
        if(imp_pacta_tipo_servicio==null)imp_pacta_tipo_servicio="";
        if(imp_pempresa_rsocial==null)imp_pempresa_rsocial="";
        if(imp_ptipo_infraccion_txt==null)imp_ptipo_infraccion_txt="";
        if(imp_vehiculo_moto==null)imp_vehiculo_moto="";
        if(imp_vehiculo_tarjeta==null)imp_vehiculo_tarjeta="";
        if(imp_pacta_observaciones==null)imp_pacta_observaciones="";
        if(imp_licensia_completa==null)imp_licensia_completa="";

        //Toast.makeText(getApplicationContext(), imp_codRutaEmpresa, Toast.LENGTH_SHORT).show();


        testPrint2(tipo_acta, imp_pacta_preimpreso, "20177217043", imp_empresa_rsocial, imp_codRutaEmpresa, imp_empresa_direccionp, imp_pempresa_ruc, imp_pacta_fecha_registro,
                imp_pinfraccion_via, imp_pconductor_nombres, imp_pconductor_apaterno + " " + imp_pconductor_amaterno, imp_pconductor_direccion,
                imp_pconductor_dni, imp_licensia_completa, imp_pvehiculo_placa, imp_pcobrador_nombres, imp_pcobrador_apaterno + " " + imp_pcobrador_amaterno,
                imp_pcobrador_direccion, imp_pcobrador_dni, imp_pcobrador_edad,imp_pacta_tipo_servicio, imp_pempresa_rsocial,   imp_ptipo_infraccion_txt, imp_vehiculo_moto, imp_vehiculo_tarjeta,
                imp_pacta_observaciones
        );



    }

    public void imprimirActa(String id_acta) {




        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Cargando la impresion...");
        pd.show();

            //progressDialog.setMessage("Haciendo Magia...");
            //progressDialog.show();


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
                                String conductor_licencia_completa = getS(acta.getString("licencia_completa"));//para imprimir
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

                                String reglamento_txt =  acta.getString("reglamento");
                                String tipo_infraccion_txt = acta.getString("infraccion_tipo");
                                String conductor_tipo_documento_txt = acta.getString("conductor_documento");
                                String cobrador_tipo_documento_txt = acta.getString("cobrador_documento");
                                String conductor_licencia_clase_txt = acta.getString("clase");
                                String conductor_licencia_categoria_txt = acta.getString("categoria");
                                String acta_tipo_servicio_txt = acta.getString("tipo_servicio");

                                ActaId= new Acta(acta_id,acta_preimpreso,acta_operador,acta_fecha_registro,acta_observaciones,acta_tipo_servicio,acta_estado,
                                        vehiculo_placa,vehiculo_ws,conductor_tipo_documento,conductor_dni,conductor_nombres,conductor_apaterno,conductor_amaterno,
                                        conductor_licencia,conductor_licencia_clase,conductor_licencia_categoria,conductor_direccion,conductor_ubigeo,
                                        infraccion_tipo,infraccion_via,empresa_ruc,empresa_rsocial,cobrador_dni,cobrador_nombres,cobrador_apaterno,cobrador_amaterno,
                                        cobrador_direccion,cobrador_tipo_documento,vehiculo_moto,empresa_direccion,vehiculo_tarjeta,cobrador_edad);

                                Log.i("acta final",ActaId+" "+ActaId.getActa_operador()+"ño");
                                //  Log.i("acta recuperada_id",actaobj.getActa_id()+"");

                                Intent paso = new Intent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                paso.putExtra("incidencia", "servidor" );
                                setResult(RESULT_OK, paso);


                                finish();
                                myDialog.dismiss();

                                if(vehiculo_moto.equals("1")) {
                                    vehiculo_moto = "( MOTO )";
                                } else {
                                    vehiculo_moto = "";
                                }

                                String tipo_acta = "ACTA DE CONTROL";
                                if(chbeducativa.isChecked()) {
                                    tipo_acta = "ACTA EDUCATIVA";
                                }

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


                                testPrint2(tipo_acta,acta_preimpreso, "20177217043", empresa_rsocial, imp_codRutaEmpresa, empresa_direccion, empresa_ruc, acta_fecha_registro,
                                        infraccion_via, conductor_nombres, conductor_apaterno + " " + conductor_amaterno, conductor_direccion,
                                        conductor_dni, conductor_licencia_completa, vehiculo_placa, cobrador_nombres, cobrador_apaterno + " " + cobrador_amaterno,
                                        cobrador_direccion, cobrador_dni, cobrador_edad,acta_tipo_servicio, empresa_rsocial, reglamento_txt + " : " + infraccion_tipo, vehiculo_moto, vehiculo_tarjeta,
                                        acta_observaciones
                                        );

                                pd.dismiss();
                                //wait1sec(10*1000,pd);


                            } catch (JSONException e) {
                                Log.e("error reimp",e.getMessage());
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


    boolean guardarActaTocado;

    public void saveActas(String acta_preimpreso, String acta_operador, String acta_fecha_registro, String acta_observaciones, String acta_tipo_servicio,
                          String acta_estado, String vehiculo_placa, String vehiculo_ws, String conductor_tipo_documento,
                          String conductor_dni, String conductor_nombres, String conductor_apaterno, String conductor_amaterno,
                          String conductor_licencia, String conductor_licencia_clase, String conductor_licencia_categoria,
                          String conductor_direccion, String conductor_ubigeo, String infraccion_tipo, String infraccion_via,
                          String empresa_ruc, final String empresa_rsocial, String cobrador_dni, String cobrador_nombres, String cobrador_apaterno,
                          String cobrador_amaterno, String cobrador_direccion, String cobrador_tipo_documento,
                          final String vehiculo_moto, final String empresa_direccion, final String vehiculo_tarjeta, final String cobrador_edad, final String preglamento_txt,
                          final String ptipo_infraccion_txt) {

        tipo_impresion = "online";
        final String pacta_preimpreso= "#";
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

        imp_pacta_preimpreso = "#";

        final String conductor_licencia_completa = pconductor_licencia + " " + pconductor_licencia_clase + " " + pconductor_licencia_categoria;

        if(guardarActaTocado) return;
        guardarActaTocado = true;

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Creando Acta...    ");
        pd.show();
        pd.setCancelable(false);










        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                ROOT+URL_GUARDAR_ACTA,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("gabi","primer");
                        Log.e("msje","mensaje2");


                        wait1sec(1000,pd);

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
                                String preimpreso = obj.getString("preimpreso");


                                imp_pacta_preimpreso = preimpreso;


                                String msg = obj.getString("messsage");
                                Log.e("responsee",msg);
                                //Log.e("gabi",msg);

                                Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();

                                //finish();

                                GIdActa = id_acta;

                                if(!file_name.equals("")) {
                                    new  UploadFile().execute(file_path, file_name);
                                } else {
                                    myDialog.show();
                                    myDialog.setCancelable(false);
                                }

                                guardarActaTocado = false;









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
                        Log.e("msje","mensaje3");
                        guardarActaTocado = false;

                        Toast.makeText(getApplicationContext(),"Acta guardado localmente",Toast.LENGTH_SHORT).show();
                        /*saveActa(pacta_preimpreso, pacta_operador, pacta_fecha_registro, pacta_observaciones, tipo_acta_servicio_ws, pacta_estado,
                                pvehiculo_placa, "", tipo_documento_ws, pconductor_dni, pconductor_nombres, pconductor_apaterno, pconductor_amaterno,
                                pconductor_licencia, clase_licencia_ws, categoria_licencia_ws, pconductor_direccion, pconductor_ubigeo, infraccion_ws
                                , pinfraccion_via, pempresa_ruc, empresa_rsocial, pcobrador_dni, pcobrador_nombres, pcobrador_apaterno, pcobrador_amaterno, pcobrador_direccion,
                                tipo_cobrador_doc_ws, vehiculo_moto,empresa_direccion , vehiculo_tarjeta, cobrador_edad,preglamento_txt,ptipo_infraccion_txt);*/

                        myDialogPreImp.show();
                        myDialogPreImp.setCancelable(false);
                        //ESTE ES
                        /*saveActa(imp_pacta_preimpreso, imp_pacta_operador, imp_pacta_fecha_registro, imp_pacta_observaciones, tipo_acta_servicio_ws, imp_pacta_estado,
                                imp_pvehiculo_placa, imp_pvehiculo_ws, tipo_documento_ws, imp_pconductor_dni, imp_pconductor_nombres, imp_pconductor_apaterno, imp_pconductor_amaterno,
                                imp_pconductor_licencia, clase_licencia_ws, categoria_licencia_ws, imp_pconductor_direccion, imp_pconductor_ubigeo, infraccion_ws
                                , imp_pinfraccion_via, imp_pempresa_ruc, imp_pempresa_rsocial, imp_pcobrador_dni, imp_pcobrador_nombres, imp_pcobrador_apaterno, imp_pcobrador_amaterno, imp_pcobrador_direccion,
                                tipo_cobrador_doc_ws, imp_vehiculo_moto,imp_empresa_direccionp , imp_vehiculo_tarjeta, imp_pcobrador_edad,imp_preglamento_txt,imp_ptipo_infraccion_txt);*/

                        /*if (error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof ServerError) {
                            Toast.makeText(getApplicationContext(),"Acta guardado localmente",Toast.LENGTH_SHORT).show();
                            Log.e("msje","mensaje3");
                            saveActa(pacta_preimpreso, pacta_operador, pacta_fecha_registro, pacta_observaciones, tipo_acta_servicio_ws, pacta_estado,
                                    pvehiculo_placa, "", tipo_documento_ws, pconductor_dni, pconductor_nombres, pconductor_apaterno, pconductor_amaterno,
                                    pconductor_licencia, clase_licencia_ws, categoria_licencia_ws, pconductor_direccion, pconductor_ubigeo, infraccion_ws
                                    , pinfraccion_via, pempresa_ruc, empresa_rsocial, pcobrador_dni, pcobrador_nombres, pcobrador_apaterno, pcobrador_amaterno, pcobrador_direccion,
                                    tipo_cobrador_doc_ws, vehiculo_moto,empresa_direccion , vehiculo_tarjeta, cobrador_edad,preglamento_txt,ptipo_infraccion_txt);

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

                acta_educativa = 0;
                if(chbeducativa.isChecked()) {
                    acta_educativa = 1;
                }

                Log.d("acta_educativa", "xd " + acta_educativa);
                params.put("educativa",acta_educativa+"");


                return params;
            }
        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }

    ArrayList<String> reglaIds, reglaText;



    public  void getReglamento() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Cargando Reglamentos...");
        pd.show();

        reglaIds = new ArrayList<>();
        reglaText = new ArrayList<>();
        // para llamar al ahcer click en el edit view
        items = new ArrayList<>();

        ///*final ArrayList<Acta>*/ actasArray = new ArrayList<>();
        String URL_GETACTAS = "http://infomaz.com/?var=listar_reglamento";
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                ROOT+URL_LISTAR_REGLAMENTO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {




                            JSONArray jsnArr = new JSONArray(response);
                            Log.e("long reglamento",jsnArr.length()+"");

                            for(int i=0;i<jsnArr.length();i++){

                                // Get current json object
                                JSONObject reglamento = jsnArr.getJSONObject(i);
                                String value = reglamento.getString("value");
                                //Log.i("holaa","gil1");
                                String option=reglamento.getString("option");
                                //Log.i("holaa","gil2");

                                reglaIds.add(value);
                                reglaText.add(option);


                                items.add(option);

                            }

                            SharedPrefManager.getInstance(getApplicationContext()).setresponseReglamento(response);
                            //             Log.e("array inc22a",actasArray.size()+"");

                            // showSpinner();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof ServerError) {



                            if (!SharedPrefManager.getInstance(getApplicationContext()).isresponseReglamentoNull()) {


                                String response = SharedPrefManager.getInstance(getApplicationContext()).getresponseReglamento();

                                Log.d("res", SharedPrefManager.getInstance(getApplicationContext()).isresponseReglamentoNull()+"");
                                Log.d("res", response);
                                try {


                                    JSONArray jsnArr = new JSONArray(response);
                                    Log.e("long reglamento", jsnArr.length() + "");

                                    for (int i = 0; i < jsnArr.length(); i++) {

                                        // Get current json object
                                        JSONObject reglamento = jsnArr.getJSONObject(i);
                                        String value = reglamento.getString("value");
                                        //Log.i("holaa","gil1");
                                        String option = reglamento.getString("option");
                                        //Log.i("holaa","gil2");

                                        reglaIds.add(value);
                                        reglaText.add(option);


                                        items.add(option);

                                    }
                                    //             Log.e("array inc22a",actasArray.size()+"");

                                    // showSpinner();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Upps algo anda mal",Toast.LENGTH_SHORT).show();
                            }


                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(),"AuthFailureError",Toast.LENGTH_SHORT).show();
                        }  else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(),"NetworkError",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getApplicationContext(),"ParseError",Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        ){


        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }

    ArrayList<String> InfraccionIds, Infraccion_Deno,Infraccion_Regla;

    public  void getInfraccion(final String id_reglamento) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Cargando infracciones...");
        pd.show();

        InfraccionIds = new ArrayList<>();
        Infraccion_Deno = new ArrayList<>();
        Infraccion_Regla = new ArrayList<>();

        //Toast.makeText(getApplicationContext(), "HELLO HERE "+ id_reglamento, Toast.LENGTH_SHORT).show();

        ///*final ArrayList<Acta>*/ actasArray = new ArrayList<>();
        String URL_GETACTAS = ROOT+"listar_tipo_infraccion&id="+id_reglamento;
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_GETACTAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {

                            JSONArray jsnArr = new JSONArray(response);
                            Log.e("long reglamento",jsnArr.length()+"");

                            SharedPrefManager.getInstance(getApplicationContext()).setresponseInfraccion(id_reglamento,response);

                            for(int i=0;i<jsnArr.length();i++){

                                // Get current json object
                                JSONObject reglamento = jsnArr.getJSONObject(i);
                                String infraccion_id = reglamento.getString("Infraccion_Id");
                                //Log.i("holaa","gil1");
                                String infraccion_denominacion=reglamento.getString("Infraccion_Denominacion");
                                String infraccion_reglamento=reglamento.getString("Infraccion_Reglamento");
                                //Log.i("holaa","gil2");

                                InfraccionIds.add(infraccion_id);
                                Infraccion_Deno.add(infraccion_denominacion);
                                Infraccion_Regla.add(infraccion_reglamento);



                                items_infraccion.add(infraccion_denominacion);


                            }

                            //Toast.makeText(getApplicationContext(), "HELLO HERE", Toast.LENGTH_SHORT).show();
                            showSpinnerInfraccion();
                            //             Log.e("array inc22a",actasArray.size()+"");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof ServerError) {


                            if (!SharedPrefManager.getInstance(getApplicationContext()).isresponseInfraccionNull(id_reglamento)) {


                                String response = SharedPrefManager.getInstance(getApplicationContext()).getresponseInfraccion(id_reglamento);


                                try {


                                    JSONArray jsnArr = new JSONArray(response);
                                    Log.e("long reglamento",jsnArr.length()+"");



                                    for(int i=0;i<jsnArr.length();i++){

                                        // Get current json object
                                        JSONObject reglamento = jsnArr.getJSONObject(i);
                                        String infraccion_id = reglamento.getString("Infraccion_Id");
                                        //Log.i("holaa","gil1");
                                        String infraccion_denominacion=reglamento.getString("Infraccion_Denominacion");
                                        String infraccion_reglamento=reglamento.getString("Infraccion_Reglamento");
                                        //Log.i("holaa","gil2");

                                        InfraccionIds.add(infraccion_id);
                                        Infraccion_Deno.add(infraccion_denominacion);
                                        Infraccion_Regla.add(infraccion_reglamento);



                                        items_infraccion.add(infraccion_denominacion);


                                    }

                                    //Toast.makeText(getApplicationContext(), "HELLO HERE", Toast.LENGTH_SHORT).show();
                                    showSpinnerInfraccion();


                                    //             Log.e("array inc22a",actasArray.size()+"");

                                    // showSpinner();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Upps algo anda mal",Toast.LENGTH_SHORT).show();
                            }

                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(),"AuthFailureError",Toast.LENGTH_SHORT).show();
                        }  else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(),"NetworkError",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getApplicationContext(),"ParseError",Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        ){

        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }


    ArrayList<String> TipoActaSerIds, TipoActaSerTExt;

    public  void getTipoActaServicio() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Cargando ActaServicio...");
        pd.show();

        TipoActaSerIds = new ArrayList<>();
        TipoActaSerTExt = new ArrayList<>();
        // para llamar al ahcer click en el edit view
        items_acta_tipo_servicio = new ArrayList<>();
        String URL_GETACTAS = "http://infomaz.com/?var=listar_tipo_servicio";
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                ROOT+URL_LISTAR_TIPOSERVICIO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {


                            SharedPrefManager.getInstance(getApplicationContext()).setresponseTipoServicio(response);

                            JSONArray jsnArr = new JSONArray(response);
                            Log.e("long reglamento",jsnArr.length()+"");

                            for(int i=0;i<jsnArr.length();i++){

                                // Get current json object
                                JSONObject reglamento = jsnArr.getJSONObject(i);
                                String value = reglamento.getString("value");
                                //Log.i("holaa","gil1");
                                String option=reglamento.getString("option");
                                //Log.i("holaa","gil2");

                                TipoActaSerIds.add(value);
                                TipoActaSerTExt.add(option);



                                items_acta_tipo_servicio.add(option);

                            }
                            //             Log.e("array inc22a",actasArray.size()+"");

                            // showSpinner();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof ServerError) {



                            if(!SharedPrefManager.getInstance(getApplicationContext()).isresponseTipoServicioNull()) {


                                String response = SharedPrefManager.getInstance(getApplicationContext()).getresponseTipoServicio();

                                try {

                                    JSONArray jsnArr = new JSONArray(response);
                                    Log.e("long reglamento", jsnArr.length() + "");

                                    for (int i = 0; i < jsnArr.length(); i++) {

                                        // Get current json object
                                        JSONObject reglamento = jsnArr.getJSONObject(i);
                                        String value = reglamento.getString("value");
                                        //Log.i("holaa","gil1");
                                        String option = reglamento.getString("option");
                                        //Log.i("holaa","gil2");

                                        TipoActaSerIds.add(value);
                                        TipoActaSerTExt.add(option);


                                        items_acta_tipo_servicio.add(option);

                                    }
                                    //             Log.e("array inc22a",actasArray.size()+"");

                                    // showSpinner();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(),"Upps algo anda mal",Toast.LENGTH_SHORT).show();
                            }


                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(),"AuthFailureError",Toast.LENGTH_SHORT).show();
                        }  else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(),"NetworkError",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getApplicationContext(),"ParseError",Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        ){


        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }

    ArrayList<String> TipoDocId, TipoDocText;
    public  void getTipoDocumento() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Cargando tipo Docs..");
        pd.show();

        TipoDocId = new ArrayList<>();
        TipoDocText = new ArrayList<>();
        // para llamar al ahcer click en el edit view
        items_tipo_doc = new ArrayList<>();

        ///*final ArrayList<Acta>*/ actasArray = new ArrayList<>();
        String URL_GETACTAS = "http://infomaz.com/?var=listar_documento";
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                ROOT+URL_LISTAR_DOCUMENTO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {

                            SharedPrefManager.getInstance(getApplicationContext()).setresponseTipoDocumento(response);
                            JSONArray jsnArr = new JSONArray(response);
                            Log.e("long reglamento",jsnArr.length()+"");

                            for(int i=0;i<jsnArr.length();i++){

                                // Get current json object
                                JSONObject tipoDoc = jsnArr.getJSONObject(i);
                                String value = tipoDoc.getString("value");
                                //Log.i("holaa","gil1");
                                String option=tipoDoc.getString("option");
                                //Log.i("holaa","gil2");

                                TipoDocId.add(value);
                                TipoDocText.add(option);



                                items_tipo_doc.add(option);

                            }
                            //             Log.e("array inc22a",actasArray.size()+"");

                            // showSpinner();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof ServerError) {


                            if(!SharedPrefManager.getInstance(getApplicationContext()).isresponseTipoDocumentoNull()) {


                                String response = SharedPrefManager.getInstance(getApplicationContext()).getresponseTipoDocumento();

                                try {

                                    JSONArray jsnArr = new JSONArray(response);
                                    Log.e("long reglamento", jsnArr.length() + "");

                                    for (int i = 0; i < jsnArr.length(); i++) {

                                        // Get current json object
                                        JSONObject tipoDoc = jsnArr.getJSONObject(i);
                                        String value = tipoDoc.getString("value");
                                        //Log.i("holaa","gil1");
                                        String option = tipoDoc.getString("option");
                                        //Log.i("holaa","gil2");

                                        TipoDocId.add(value);
                                        TipoDocText.add(option);


                                        items_tipo_doc.add(option);

                                    }
                                    //             Log.e("array inc22a",actasArray.size()+"");

                                    // showSpinner();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(),"Upps algo anda mal",Toast.LENGTH_SHORT).show();
                            }


                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(),"AuthFailureError",Toast.LENGTH_SHORT).show();
                        }  else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(),"NetworkError",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getApplicationContext(),"ParseError",Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        ){


        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }

    ArrayList<String> Empresa_Id, Empresa_Rsocial,Empresa_Ruta,Empresa_Ruc,Empresa_Domicilio;
    public  void getEmpresaTransportes() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Cargando Empresas de transportes..");
        pd.show();

        Empresa_Id = new ArrayList<>();
        Empresa_Rsocial = new ArrayList<>();
        Empresa_Ruta = new ArrayList<>();
        Empresa_Ruc = new ArrayList<>();
        Empresa_Domicilio = new ArrayList<>();
        // para llamar al ahcer click en el edit view
        items_razon_social_empresa = new ArrayList<>();

        ///k*final ArrayList<Acta>*/ actasArray = new ArrayList<>();
        String URL_GETACTAS = "http://infomaz.com/?var=listar_empresas_transportes";
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,

                URL_GETACTAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {

                            JSONArray jsnArr = new JSONArray(response);
                            // Log.e("long reglamento",jsnArr.length()+"");


                                SharedPrefManager.getInstance(getApplicationContext()).setresponseEmpresaTransportes(response);

                                for (int i = 0; i < jsnArr.length(); i++) {

                                    // Get current json object
                                    JSONObject EmpresasTrans = jsnArr.getJSONObject(i);
                                    String empresa_id = EmpresasTrans.getString("Empresa_Id");
                                    //Log.i("holaa","gil1");
                                    String empresa_rsocial = EmpresasTrans.getString("Empresa_Rsocial");
                                    String empresa_ruta = EmpresasTrans.getString("Empresa_Ruta");
                                    String empresa_ruc = EmpresasTrans.getString("Empresa_Ruc");
                                    String empresa_domicilio = EmpresasTrans.getString("Empresa_Direccion");
                                    //Log.i("holaa","gil2");

                                    Empresa_Id.add(empresa_id);
                                    Empresa_Rsocial.add(empresa_rsocial);
                                    Empresa_Ruta.add(empresa_ruta);
                                    Empresa_Ruc.add(empresa_ruc);
                                    Empresa_Domicilio.add(empresa_domicilio);



                                    items_razon_social_empresa.add(empresa_rsocial + "  Ruta:" + empresa_ruta);

                                }

                            //             Log.e("array inc22a",actasArray.size()+"");

                            // showSpinner();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof ServerError) {


                            if(!SharedPrefManager.getInstance(getApplicationContext()).isresponseEmpresaTransportesNull()) {


                                String response = SharedPrefManager.getInstance(getApplicationContext()).getresponseEmpresaTransportes();

                                try {

                                    JSONArray jsnArr = new JSONArray(response);
                                    Log.e("long reglamento", jsnArr.length() + "");

                                    for (int i = 0; i < jsnArr.length(); i++) {

                                        // Get current json object
                                        JSONObject EmpresasTrans = jsnArr.getJSONObject(i);
                                        String empresa_id = EmpresasTrans.getString("Empresa_Id");
                                        //Log.i("holaa","gil1");
                                        String empresa_rsocial = EmpresasTrans.getString("Empresa_Rsocial");
                                        String empresa_ruta = EmpresasTrans.getString("Empresa_Ruta");
                                        String empresa_ruc = EmpresasTrans.getString("Empresa_Ruc");
                                        String empresa_domicilio = EmpresasTrans.getString("Empresa_Direccion");
                                        //Log.i("holaa","gil2");

                                        Empresa_Id.add(empresa_id);
                                        Empresa_Rsocial.add(empresa_rsocial);
                                        Empresa_Ruta.add(empresa_ruta);
                                        Empresa_Ruc.add(empresa_ruc);
                                        Empresa_Domicilio.add(empresa_domicilio);


                                        items_razon_social_empresa.add(empresa_rsocial + "  Ruta:" + empresa_ruta);

                                    }
                                    //             Log.e("array inc22a",actasArray.size()+"");

                                    // showSpinner();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(),"Upps algo anda mal",Toast.LENGTH_SHORT).show();
                            }


                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(),"AuthFailureError",Toast.LENGTH_SHORT).show();
                        }else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(),"NetworkError",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getApplicationContext(),"ParseError",Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        ){


        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }

    ArrayList<String> ClaseLicIds, ClaseLicenciaText;
    public  void getClaseLicencia() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Cargando clase lic..");
        pd.show();

        ClaseLicIds = new ArrayList<>();
        ClaseLicenciaText = new ArrayList<>();
        // para llamar al ahcer click en el edit view
        items_clase_licencia = new ArrayList<>();

        ///k*final ArrayList<Acta>*/ actasArray = new ArrayList<>();
        String URL_GETACTAS = "http://infomaz.com/?var=listar_licencia_clase";
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                ROOT+URL_LISTAR_LICENCIA_CLASE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {

                            SharedPrefManager.getInstance(getApplicationContext()).setresponseClaseLicencia(response);

                            JSONArray jsnArr = new JSONArray(response);
                            // Log.e("long reglamento",jsnArr.length()+"");

                            for(int i=0;i<jsnArr.length();i++){

                                // Get current json object
                                JSONObject EmpresasTrans = jsnArr.getJSONObject(i);
                                String value = EmpresasTrans.getString("value");
                                //Log.i("holaa","gil1");
                                String option=EmpresasTrans.getString("option");

                                //Log.i("holaa","gil2");

                                ClaseLicIds.add(value);
                                ClaseLicenciaText.add(option);

                                items_clase_licencia.add(option);

                            }
                            //             Log.e("array inc22a",actasArray.size()+"");

                            // showSpinner();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof ServerError) {



                            if(!SharedPrefManager.getInstance(getApplicationContext()).isresponseClaseLicenciasNull()) {


                                String response = SharedPrefManager.getInstance(getApplicationContext()).getresponseresponseClaseLicencia();

                                try {

                                    SharedPrefManager.getInstance(getApplicationContext()).setresponseClaseLicencia(response);

                                    JSONArray jsnArr = new JSONArray(response);
                                    // Log.e("long reglamento",jsnArr.length()+"");

                                    for (int i = 0; i < jsnArr.length(); i++) {

                                        // Get current json object
                                        JSONObject EmpresasTrans = jsnArr.getJSONObject(i);
                                        String value = EmpresasTrans.getString("value");
                                        //Log.i("holaa","gil1");
                                        String option = EmpresasTrans.getString("option");

                                        //Log.i("holaa","gil2");

                                        ClaseLicIds.add(value);
                                        ClaseLicenciaText.add(option);

                                        items_clase_licencia.add(option);

                                    }
                                    //             Log.e("array inc22a",actasArray.size()+"");

                                    // showSpinner();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(),"Upps algo anda mal",Toast.LENGTH_SHORT).show();
                            }



                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(),"AuthFailureError",Toast.LENGTH_SHORT).show();
                        }  else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(),"NetworkError",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getApplicationContext(),"ParseError",Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        ){


        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }

    ArrayList<String> CategoriaLicIds, CategoriaLicOption,CategoriaLicClase;
    public  void getCategoriaLicencia(final String clase_lic) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Cargando categorias...");
        pd.show();

        CategoriaLicIds = new ArrayList<>();
        CategoriaLicOption = new ArrayList<>();
        CategoriaLicClase = new ArrayList<>();

        ///*final ArrayList<Acta>*/ actasArray = new ArrayList<>();
        String URL_GETACTAS = "http://infomaz.com/?var=listar_licencia_categoria";
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                ROOT+URL_LISTAR_LICENCIA_CATEGORIA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {

                            JSONArray jsnArr = new JSONArray(response);
                            Log.e("long reglamento",jsnArr.length()+"");

                            SharedPrefManager.getInstance(getApplicationContext()).setresponseCatLicencia(response);
                            for(int i=0;i<jsnArr.length();i++){


                                // Get current json object
                                JSONObject categoria = jsnArr.getJSONObject(i);

                                String value = categoria.getString("value");
                                //Log.i("holaa","gil1");
                                String option=categoria.getString("option");
                                String clase=categoria.getString("clase");

                                if(clase.equals(clase_lic)){
                                    CategoriaLicIds.add(value);
                                    CategoriaLicOption.add(option);

                                    items_categoria_licencia.add(option);
                                }
                                //Log.i("holaa","gil2");




                            }
                            showCategoriaLicencia();
                            //             Log.e("array inc22a",actasArray.size()+"");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof ServerError) {

                            if (!SharedPrefManager.getInstance(getApplicationContext()).isresponseCatLicenciaNull()) {


                                String response = SharedPrefManager.getInstance(getApplicationContext()).getresponseCatLicencia();


                                Log.d("res", response);
                                try {


                                    JSONArray jsnArr = new JSONArray(response);
                                    Log.e("long reglamento",jsnArr.length()+"");

                                    SharedPrefManager.getInstance(getApplicationContext()).setresponseCatLicencia(response);
                                    for(int i=0;i<jsnArr.length();i++){


                                        // Get current json object
                                        JSONObject categoria = jsnArr.getJSONObject(i);

                                        String value = categoria.getString("value");
                                        //Log.i("holaa","gil1");
                                        String option=categoria.getString("option");
                                        String clase=categoria.getString("clase");

                                        if(clase.equals(clase_lic)){
                                            CategoriaLicIds.add(value);
                                            CategoriaLicOption.add(option);

                                            items_categoria_licencia.add(option);
                                        }
                                        //Log.i("holaa","gil2");




                                    }
                                    showCategoriaLicencia();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Upps algo anda mal",Toast.LENGTH_SHORT).show();
                            }
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(),"AuthFailureError",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(),"NetworkError",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getApplicationContext(),"ParseError",Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        ){

        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);
    }


    public  void ValidarInformacionPasada(final String placa, final String infraccion_id) {
        final String pPlaca = placa;
        final String pInfraccion_id= infraccion_id;
        // /*final ArrayList<Acta>*/ actasArray = new ArrayList<>();

        final ProgressDialog pd = new ProgressDialog(this);

        pd.setMessage("Validando Placa...");
        pd.show();


        String URL_GETACTAS = "http://infomaz.com/?var=validar_infraccion_pasada&placa="+pPlaca+"&infraccion_id="+pInfraccion_id+"";
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_GETACTAS,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("hola",pPlaca+""+pInfraccion_id+"");
                        //wait1sec(1000,pd);
                        pd.dismiss();
                        //  pd.dismiss();
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

                            // Get current json object
                            JSONObject acta = jsnArr.getJSONObject(0);
                            String vehiculo_placa = acta.getString("Vehiculo_Placa");
                            String Infracciones_Fecha_Infraccion=acta.getString("Infracciones_Fecha_Infraccion");
                            String Infracciones_Preimpreso=acta.getString("Infracciones_Preimpreso");
                            String Infracciones_Situacion=acta.getString("Infracciones_Situacion");
                            String Estado_Descripcion = acta.getString("Estado_Descripcion");
                            String Infracciones_obs = acta.getString("Infracciones_obs");
                            String Infraccion_Numero=acta.getString("Infraccion_Numero");
                            String Infraccion_Descripcion=acta.getString("Infraccion_Descripcion");
                            String Permitir_Acta=acta.getString("Permitir_Acta");
                            if(Permitir_Acta.equals("1"))
                            {

                                Toast.makeText(getApplicationContext(),"No se peude guardar esta acta",Toast.LENGTH_SHORT ).show();
                            }
                            else{

                                Toast.makeText(getApplicationContext(),"Acta admitida",Toast.LENGTH_SHORT).show();

                            }





                            Permitir_Acta_gen = Permitir_Acta;
                            Log.i("validar acta",Permitir_Acta_gen);

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
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("id_usuario", placa);
                params.put("fecha",infraccion_id+"");
                //params.put("detalle",detalle);

                return params;
            }
        };

        //RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }

    String mOperador, mUsuarioID;

    String imp_pacta_preimpreso,imp_pacta_operador,imp_pempresa_rsocial, imp_empresa_direccionp, imp_pempresa_ruc, imp_pacta_fecha_registro,
    imp_pinfraccion_via, imp_pconductor_nombres, imp_pconductor_apaterno,imp_pconductor_amaterno, imp_pconductor_direccion,
    imp_pconductor_dni, imp_licensia_completa, imp_pvehiculo_placa, imp_pcobrador_nombres, imp_pcobrador_apaterno, imp_pcobrador_amaterno,
    imp_pcobrador_direccion, imp_pcobrador_dni, imp_pcobrador_edad,imp_pacta_tipo_servicio,   imp_ptipo_infraccion_txt, imp_vehiculo_moto, imp_vehiculo_tarjeta,
    imp_pacta_observaciones, imp_pacta_estado, imp_pconductor_tipo_documento, imp_pconductor_licencia_clase, imp_pconductor_licencia_categoria;

    String imp_pvehiculo_ws, imp_pconductor_licencia, imp_pconductor_ubigeo, imp_pinfraccion_tipo, imp_pcobrador_tipo_documento,
    imp_fvehiculo_moto, imp_tipo_infraccion_txt, imp_preglamento_txt ;

    @Override
    public void onClick(View v) {


        SimpleDateFormat feca = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        currentDate = feca.format(new Date());

        //Toast.makeText(getApplicationContext(), currentDate, Toast.LENGTH_SHORT).show();


        imp_pacta_operador = mOperador;
        imp_pacta_fecha_registro=currentDate;
        imp_pacta_observaciones= acta_observaciones.getText().toString();
        imp_pacta_tipo_servicio=acta_tipo_servicio.getText().toString();
        imp_pacta_estado="1";
        imp_pvehiculo_placa = vehiculo_placa.getText().toString();
        imp_pvehiculo_ws=sunarp_vehiculo_ws;
        imp_pconductor_tipo_documento=conductor_tipo_documento.getText().toString();
        imp_pconductor_dni=conductor_dni.getText().toString();
        imp_pconductor_nombres=conductor_nombres.getText().toString();
        imp_pconductor_apaterno=conductor_apaterno.getText().toString();
        imp_pconductor_amaterno=conductor_amaterno.getText().toString();
        imp_pconductor_licencia=conductor_licencia.getText().toString();
        imp_pconductor_licencia_clase=conductor_licencia_clase.getText().toString();
        imp_pconductor_licencia_categoria=conductor_licencia_categoria.getText().toString();
        imp_pconductor_direccion=conductor_direccion.getText().toString();
        imp_pconductor_ubigeo=conductor_ubigeo.getText().toString();
        imp_pinfraccion_tipo=infraccion_tipo.getText().toString();
        imp_pinfraccion_via=infraccion_via.getText().toString();
        imp_pempresa_ruc=empresa_ruc.getText().toString();
        imp_pempresa_rsocial=empresa_rsocial.getText().toString();
        imp_pcobrador_dni=cobrador_dni.getText().toString();
        imp_pcobrador_nombres=cobrador_nombres.getText().toString();
        imp_pcobrador_apaterno=cobrador_apaterno.getText().toString();
        imp_pcobrador_amaterno=cobrador_amaterno.getText().toString();
        imp_pcobrador_direccion=cobrador_direccion.getText().toString();
        imp_pcobrador_tipo_documento=cobrador_tipo_documento.getText().toString();

        imp_pcobrador_edad = cobrador_edad.getText().toString();
        imp_vehiculo_moto = moto.isChecked() ? "1" : "0";
        imp_empresa_direccionp = empresa_direccion.getText().toString();
        imp_vehiculo_tarjeta = conductor_tarjeta.getText().toString();

        imp_tipo_infraccion_txt = infraccion_tipo.getText().toString();
        imp_preglamento_txt = reglamento.getText().toString();

        imp_ptipo_infraccion_txt =  imp_tipo_infraccion_txt;

        imp_licensia_completa =  imp_pconductor_licencia + " " + imp_pconductor_licencia_clase  + " " + imp_pconductor_licencia_categoria;

        //Toast.makeText(getApplicationContext(), imp_preglamento_txt, Toast.LENGTH_SHORT).show();







        if(v == boadjuntar) {
            selectImage();
        }

        if(v == tvImgClose) {
            myDialogImg.dismiss();
        }

        if(v == boVerImagen) {
            myDialogImg.show();
            //setImage("");
        }

        if(v == tvClose) {
            myDialog.dismiss();

            Intent paso = new Intent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            if(tipo_impresion.equals("online")) {
                paso.putExtra("incidencia", "servidor" );
            } else {
                paso.putExtra("incidencia", "local" );
            }
            setResult(RESULT_OK, paso);


            finish();
        }

        if(v == ok) {
            int result = mPrinter.Open();

            if(result == 0)
                mPrinter.Step((byte)0x5f);
        }

        if(v == imprimir) {
            //Toast.makeText(getApplicationContext(), "IMPRIMIR " + GIdActa, Toast.LENGTH_SHORT).show();
            imprimirActaOffline();
            //imprimirActa(GIdActa);
        }

        if(v==spinner_reglamento){
            //items = new ArrayList<>();
            //getReglamento();
            showSpinner();


        }

        if(v == boPreImpAceptar) {

            String pre_impreso_txt = etPreImpreso.getText().toString();
            if(pre_impreso_txt.length() > 0) {
                //ESTE ES
                myDialogPreImp.dismiss();
                imp_pacta_preimpreso = pre_impreso_txt;
                saveActa(imp_pacta_preimpreso, imp_pacta_operador, imp_pacta_fecha_registro, imp_pacta_observaciones, tipo_acta_servicio_ws, imp_pacta_estado,
                        imp_pvehiculo_placa, imp_pvehiculo_ws, tipo_documento_ws, imp_pconductor_dni, imp_pconductor_nombres, imp_pconductor_apaterno, imp_pconductor_amaterno,
                        imp_pconductor_licencia, clase_licencia_ws, categoria_licencia_ws, imp_pconductor_direccion, imp_pconductor_ubigeo, infraccion_ws
                        , imp_pinfraccion_via, imp_pempresa_ruc, imp_pempresa_rsocial, imp_pcobrador_dni, imp_pcobrador_nombres, imp_pcobrador_apaterno, imp_pcobrador_amaterno, imp_pcobrador_direccion,
                        tipo_cobrador_doc_ws, imp_vehiculo_moto,imp_empresa_direccionp , imp_vehiculo_tarjeta, imp_pcobrador_edad,imp_preglamento_txt,imp_ptipo_infraccion_txt);

            }

        }

        if(v == tvPreImpClose) {

        }

        if(v==spinner_tipoinfraccion)
        {
            items_infraccion = new ArrayList<>();
            getInfraccion(reglamento_ws);



        }

        if(v==validar_infraccion){

            // final String pvehiculo_placa = vehiculo_placa.getText().toString();
            // final int pinfraccion_tipo=Integer.parseInt(infraccion_tipo.getText().toString());
            //  startActivity(new Intent(getApplicationContext(), Detalle_Acta.class));

            /*String position = SharedPrefManager.getInstance(getApplicationContext()).getCurrentPosition();
            Toast.makeText(getApplicationContext(), position, Toast.LENGTH_SHORT).show();*/

            // Check if no view has focus:
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            //ValidarInformacionPasada(pvehiculo_placa,infraccion_ws);
//            Log.i("boton validar fin",Permitir_Acta_gen);


            RecuperarWsVehiculo(imp_pvehiculo_placa);


            getListaInfracciones(imp_pvehiculo_placa,infraccion_ws);
            // ValidarInformacionPasada(pvehiculo_placa,infraccion_ws);
//            Log.i("boton validar fin",Permitir_Acta_gen);

        }

        if(v==completar_datos_conductor){
            // Check if no view has focus:
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            String DNI_conductor = conductor_dni.getText().toString();
            RecuperarDatosConductor_cDNI(DNI_conductor);
            RecuperarLicencia_cDNI(DNI_conductor);



        }

        if(v==completar_datos_cobrador){
            // Check if no view has focus:
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            String DNI_cobrador = cobrador_dni.getText().toString();
            RecuperarDatosCobrador_cDNI(DNI_cobrador);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        }


        if(v==spinner_tipo_acta_servicio){
            showSpinnerTipoActaServicio();
        }
        if(v==spinner_tipo_doc_conductor){
            showSpinnerTipoDocumento();

        }
        if(v==spinner_tipo_doc_cobrador){
            showSpinnerTipoDocumentoCobrador();
        }
        if(v==spinner_clase_licencia){
            //Toast.makeText(getApplicationContext(),"clase",Toast.LENGTH_SHORT).show();
            showClaseLicencia();
        }
        if(v==spinner_categoria_licencia)
        {
            items_categoria_licencia = new ArrayList<>();
            getCategoriaLicencia(clase_licencia_ws);
        }
        if(v==spinner_rsocial){
            showRazonSocialEmpresa();

        }
        /*if(v==guardar) {


            if ((pacta_preimpreso.length() == 0) && (pacta_operador.length() == 0) && (pacta_observaciones.length() == 0)) {
                Toast.makeText(this, "llene campos obligatorios", Toast.LENGTH_SHORT).show();

            }
            else{
                if (Permitir_Acta_gen.equals("1")) {
                    Toast.makeText(this, "No puede guardar el acta, vuelva a llenar los campos", Toast.LENGTH_SHORT).show();
                    Log.i("acta final", Permitir_Acta_gen);
                } else {

                    Log.d("ws_prueba1", reglamento_ws + " ");
                    Log.d("ws_prueba1", infraccion_ws + " ");
                    Log.d("ws_prueba1", tipo_acta_servicio_ws + " ");
                    Log.d("ws_prueba1", tipo_documento_ws + " ");
                    Log.d("ws_prueba1", clase_licencia_ws + " ");
                    Log.d("ws_prueba1", categoria_licencia_ws + " ");
                    Log.d("ws_prueba1", razon_social_empresa_ws + " ");
                    Log.d("ws_prueba1", tipo_cobrador_doc_ws + " ");


                    Toast.makeText(getApplicationContext(), pcobrador_edad + " " + vehiculo_moto + " " + vehiculo_tarjeta, Toast.LENGTH_SHORT).show();



                    saveActas(pacta_preimpreso, pacta_operador, pacta_fecha_registro, pacta_observaciones, tipo_acta_servicio_ws, pacta_estado,
                            pvehiculo_placa, pvehiculo_ws, tipo_documento_ws, pconductor_dni, pconductor_nombres, pconductor_apaterno, pconductor_amaterno,
                            pconductor_licencia, clase_licencia_ws, categoria_licencia_ws, pconductor_direccion, pconductor_ubigeo, infraccion_ws
                            , pinfraccion_via, pempresa_ruc, pempresa_rsocial, pcobrador_dni, pcobrador_nombres, pcobrador_apaterno, pcobrador_amaterno, pcobrador_direccion,
                            tipo_cobrador_doc_ws, vehiculo_moto,empresa_direccionp , vehiculo_tarjeta, pcobrador_edad,preglamento_txt,ptipo_infraccion_txt);



                    //Toast.makeText(this, "SE GUARDO ELA ACTA CON EXITO", Toast.LENGTH_SHORT).show();
                }
            }
        }*/

        if(v==guardar) {

           //myDialog.show();
           /* getImage();
           if(true)
               return;*/
            if (  (reglamento.length() == 0) || (imp_pconductor_nombres.length() == 0) || (tipo_acta_servicio_ws.length() == 0)   || (infraccion_tipo.length() == 0)) {
                Toast.makeText(this, "Llene los campos obligatorios", Toast.LENGTH_SHORT).show();

            }
            else{
                Log.e("msje","mensaje1");
              /* if (Permitir_Acta_gen.equals("1")) {
                   Toast.makeText(this, "No puede guardar el acta, vuelva a llenar los campos", Toast.LENGTH_SHORT).show();
                   Log.i("acta final", Permitir_Acta_gen);
               } else {

                   Log.d("ws_prueba1", reglamento_ws + " ");
                   Log.d("ws_prueba1", infraccion_ws + " ");
                   Log.d("ws_prueba1", tipo_acta_servicio_ws + " ");
                   Log.d("ws_prueba1", tipo_documento_ws + " ");
                   Log.d("ws_prueba1", clase_licencia_ws + " ");
                   Log.d("ws_prueba1", categoria_licencia_ws + " ");
                   Log.d("ws_prueba1", razon_social_empresa_ws + " ");
                   Log.d("ws_prueba1", tipo_cobrador_doc_ws + " ");
                   Toast.makeText(getApplicationContext(), pcobrador_edad + " " + vehiculo_moto + " " + vehiculo_tarjeta, Toast.LENGTH_SHORT).show();
               */

                //guardarActaTocado = false;


                saveActas(imp_pacta_preimpreso, imp_pacta_operador, imp_pacta_fecha_registro, imp_pacta_observaciones, tipo_acta_servicio_ws, imp_pacta_estado,
                        imp_pvehiculo_placa, imp_pvehiculo_ws, tipo_documento_ws, imp_pconductor_dni, imp_pconductor_nombres, imp_pconductor_apaterno, imp_pconductor_amaterno,
                        imp_pconductor_licencia, clase_licencia_ws, categoria_licencia_ws, imp_pconductor_direccion, imp_pconductor_ubigeo, infraccion_ws
                        , imp_pinfraccion_via, imp_pempresa_ruc, imp_pempresa_rsocial, imp_pcobrador_dni, imp_pcobrador_nombres, imp_pcobrador_apaterno, imp_pcobrador_amaterno, imp_pcobrador_direccion,
                        tipo_cobrador_doc_ws, imp_vehiculo_moto, imp_empresa_direccionp, imp_vehiculo_tarjeta, imp_pcobrador_edad, imp_preglamento_txt, imp_ptipo_infraccion_txt);




                //Toast.makeText(this, "SE GUARDO ELA ACTA CON EXITO", Toast.LENGTH_SHORT).show();
                //}
            }
        }
    }


    /*IMPRESION DATA*/

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

    Bitmap mybm;



    public void getImage() {

        //https://i.imgur.com/BZ4Sl2L.png
        Picasso.with(getApplicationContext()).load("https://i.imgur.com/Vg9j6w1.png").into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // loaded bitmap is here (bitmap)


                int result = mPrinter.Open();
                if(result == 0) {

                    mPrinter.PrintBitmap(bitmap);
                    mPrinter.PrintStringEx("GERENCIA DE TRANSITO VIALIDAD Y TRANSPORTE", mTitleTextSize-20, false, false, mTitleType);

                    mPrinter.printBlankLine(20);

                    mPrinter.Close();

                }
                Toast.makeText(getApplicationContext(), "PICASSO", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) { }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        });
    }


    private void testPrint2(  String tipo_acta, String pre_impreso,String ruc, String nombre_razon_social, String cod_ruta_empresa, String domicilio, String dni_ruc,String fecha, String lugar_intervencion,
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
            if(nombre_razon_social.length()==0) nombre_razon_social=texto_blanco;
            mPrinter.PrintStringEx(nombre_razon_social, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);

            mPrinter.PrintStringEx("Domicilio", mTitleTextSize-18, false, true, mTitleType);
            if(domicilio.length()==0) domicilio=texto_blanco;
            mPrinter.PrintStringEx(domicilio, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);

            String ss = "DNI:";
            if(dni_ruc.length() > 8) ss = "RUC:";


            mPrinter.PrintLineInit(mKeyTextSize);
            mPrinter.PrintLineString(ss, mKeyTextSize, mOffsetX-95, true);
            if(dni_ruc.length()==0) dni_ruc=texto_blanco;
            mPrinter.PrintLineString(dni_ruc, mValue2TextSize, mOffsetX-40, false);//160
            mPrinter.PrintLineEnd();


            mPrinter.printBlankLine(4);

            mPrinter.PrintLineInit(mKeyTextSize-2);
            mPrinter.PrintLineString("Fecha y Hora:", mKeyTextSize-2, mOffsetX-181, true);
            if(fecha.length()==0) fecha=texto_blanco;
            mPrinter.PrintLineString(fecha, mValue2TextSize-2, mOffsetX-42, false);//160
            mPrinter.PrintLineEnd();


            mPrinter.printBlankLine(4);

            mPrinter.PrintStringEx("Lugar de Intervención", mTitleTextSize-18, false, true, mTitleType);
            if(lugar_intervencion.length()==0) lugar_intervencion=texto_blanco;
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
            if(cdomicilio.length()==0) cdomicilio=texto_blanco;
            mPrinter.PrintStringEx(cdomicilio, mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);


            mPrinter.PrintLineInit(mKeyTextSize);
            mPrinter.PrintLineString("DNI:", mKeyTextSize, mOffsetX-90, true);
            if(cdni.length()==0) cdni=texto_blanco;
            mPrinter.PrintLineString(cdni, mValue2TextSize, mOffsetX-40, false);//160
            mPrinter.PrintLineEnd();
            mPrinter.printBlankLine(4);



            mPrinter.PrintStringEx("Licencia, clase y categoría", mTitleTextSize-18, false, true, mTitleType);
            if(clicencia_clase_cat.length()==0) clicencia_clase_cat=texto_blanco;
            mPrinter.PrintStringEx(clicencia_clase_cat , mTitleTextSize-20, false, false, mTitleType);
            mPrinter.printBlankLine(4);


            mPrinter.PrintLineInit(mKeyTextSize-2);
            mPrinter.PrintLineString("Placa de rodaje:", mKeyTextSize-2, mOffsetX-142, true);
            if(cplaca_rodaje.length()==0) cplaca_rodaje=texto_blanco;
            mPrinter.PrintLineString(cplaca_rodaje, mValue2TextSize, mOffsetX+20, false);//160
            mPrinter.PrintLineEnd();
            mPrinter.printBlankLine(4);


            mPrinter.PrintLineInit(mKeyTextSize-2);
            mPrinter.PrintLineString("N° Tarjeta:", mKeyTextSize-2, mOffsetX-125, true);
            if(vehiculo_tarjeta.length()==0) vehiculo_tarjeta=texto_blanco;
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
            if(cobnombres.length()==0) cobnombres=texto_blanco;
            if(cobapellidos.length()==0) cobapellidos=texto_blanco;
            mPrinter.PrintStringEx(cobnombres + " " + cobapellidos, mTitleTextSize - 20, false, false, mTitleType);
            mPrinter.printBlankLine(4);

            mPrinter.PrintStringEx("Domicilio", mTitleTextSize - 18, false, true, mTitleType);
            if(cobdomicilio.length()==0) cobdomicilio=texto_blanco;
            mPrinter.PrintStringEx(cobdomicilio, mTitleTextSize - 20, false, false, mTitleType);
            mPrinter.printBlankLine(4);


            mPrinter.PrintLineInit(mKeyTextSize);
            mPrinter.PrintLineString("DNI:", mKeyTextSize, mOffsetX-90, true);
            if(cobdni.length()==0) cobdni=texto_blanco;
            mPrinter.PrintLineString(cobdni, mValue2TextSize, mOffsetX-40, false);//160
            mPrinter.PrintLineEnd();
            mPrinter.printBlankLine(4);


            mPrinter.PrintLineInit(mKeyTextSize);
            mPrinter.PrintLineString("Edad:", mKeyTextSize, mOffsetX-105+40, true);
            if(cobedad.length()==0) cobedad=texto_blanco;
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

            if(acta_obs.length()==0) acta_obs=texto_blanco;

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

            if(modalidad.length()==0) modalidad=texto_blanco;
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

            if(cod_ruta_empresa.length()==0) cod_ruta_empresa=texto_blanco;
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

            if(codinfraccion.length()==0) codinfraccion=texto_blanco;

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

            if(chbeducativa.isChecked())
                mPrinter.PrintBitmap(bmEducativa);
            else
                mPrinter.PrintBitmap(bmFooter);


            mPrinter.PrintLineInit(40);
            mPrinter.PrintLineStringByType("", mKeyTextSize, printer.PrintType.Right, true);//160
            mPrinter.PrintLineEnd();

            mPrinter.printBlankLine(40);

            mPrinter.Close();
        }

        //03.aabaa.15
    }

    private void selectImage() {
        final CharSequence[] items = { "Toma una Foto", "Elije una imagen",
                "Cancelar" };
        AlertDialog.Builder builder = new AlertDialog.Builder(Actas_formulario.this);
        builder.setTitle("Adjunta una imagen");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=UtilityPhoto.checkPermission(Actas_formulario.this);
                if (items[item].equals("Toma una Foto")) {
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Elije una imagen")) {
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private final static int REQUEST_CAMERA = 10, SELECT_FILE = 11;

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void onCaptureImageResult(Intent data) {
        //Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        /*ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        //thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, out);*/

        //Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));


        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, out);
        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));





        imgAdjuntada.setAdjustViewBounds(true);

        String time = System.currentTimeMillis() + "";

        file_path = time + ".jpg";
        file_name = time + ".jpg";

        Log.d("mypath", Environment.getExternalStorageDirectory().toString());
        File destination = new File(Environment.getExternalStorageDirectory(),
                file_path);

        file_path = Environment.getExternalStorageDirectory() + "/" + time + ".jpg";

        Log.d("mypath", file_path);

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(out.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        imgAdjuntada.setImageBitmap(decoded);
        imgAdjuntada.setBackgroundColor(Color.parseColor("#00000000"));
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        //Log.d("mypath",data.getData());
        //Log.d("mypath", getPathFromURI(data.getData()));
        file_path = getPathFromURI(data.getData());
        Log.d("mypath",  file_path);
        //file_path = file_path.substring(19, file_path.length());
        //Log.d("mypath",  file_path);
        file_name = System.currentTimeMillis() + ".jpg";
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imgAdjuntada.setImageBitmap(bm);
        imgAdjuntada.setBackgroundColor(Color.parseColor("#00000000"));
    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {


            case SELECT_FILE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        onSelectFromGalleryResult(data);
                        break;
                }
                break;

            case REQUEST_CAMERA:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        onCaptureImageResult(data);
                        break;
                }

                break;


        }
    }

    private class UploadFile extends AsyncTask<String, Integer, Boolean> {
        //private SweetAlertDialog pDialog;
        //private ProgressDialog mProgressDialog = new ProgressDialog(getApplicationContext());
        @Override
        protected Boolean doInBackground(String... params) {

            String f_path = params[0];
            String f_name = params[1];
            f_name = mOperador.toLowerCase() + "-" + f_name;
            FTPClient con = null;

            try
            {
                con = new FTPClient();
                /*con.connect(InetAddress.getByName("ftp.smarterasp.net"));

                if (con.login("facturamas", "facturamas*1"))
                {*/



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

                con.connect(InetAddress.getByName(url_upload));


                String username = usuario_ftp + "@" + url_upload;
                String password = pw_ftp;

                //if (con.login("admin@infomaz.com", "admin$$$"))
                if (con.login(username, password))
                {
                    con.changeWorkingDirectory("/uploads");
                    con.setFileType(FTP.BINARY_FILE_TYPE);

                    //File file = new File(Environment.getExternalStorageDirectory(), f_path);

                    File file = new File(f_path);

                    BufferedInputStream buffIn = null;
                    buffIn = new BufferedInputStream(new FileInputStream(file));



                    con.enterLocalPassiveMode();
                    boolean result = con.storeFile(f_name, buffIn);
                    if (result) Log.v("whatis", "succeeded");
                    buffIn.close();
                    con.logout();
                    con.disconnect();

                    return result;

                } else {
                    Log.d("whatis", "MAL LOGEO");
                    //Toast.makeText(getApplicationContext(), "Error logueo", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            catch (Exception e)
            {
                Log.d("whatis", "wtf");
                //Toast.makeText(getApplicationContext(), "Exception correctamente", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean sucess) {
            progressDialog.dismiss();
            myDialog.show();
            myDialog.setCancelable(false);
            if (sucess) {
                Toast.makeText(getApplicationContext(), "Imagen enviada", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Error al enviar imagen", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPreExecute() {

            progressDialog.setMessage("Subiendo Imagen...");
            progressDialog.show();

            /*pDialog = new SweetAlertDialog(Actas_formulario.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Subiendo Imagen...");
            pDialog.setCancelable(false);
            pDialog.show();*/

            //Toast.makeText(getApplicationContext(), "ONPREEXCEUTE", Toast.LENGTH_SHORT).show();
            //super.onPreExecute();
            //btnDownload.setText("Descargando...");

            /*mProgressDialog = new ProgressDialog(getApplicationContext());
            mProgressDialog.setMessage("Descargando audios..");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();*/

            /*pDialog = new SweetAlertDialog(MapTest.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Descargando audios..");
            pDialog.setCancelable(false);
            pDialog.show();*/


//            buttonText.setEnabled(false);
//            btnDownload.setText("Donload Started...");//Set Button Text when download started
        }



    }



    int acta_educativa = 0;

    public void saveActa(String acta_preimpreso, String acta_operador, String acta_fecha_registro, String acta_observaciones, String acta_tipo_servicio,
                         String acta_estado, String vehiculo_placa, String vehiculo_ws, String conductor_tipo_documento,
                         String conductor_dni, String conductor_nombres, String conductor_apaterno, String conductor_amaterno,
                         String conductor_licencia, String conductor_licencia_clase, String conductor_licencia_categoria,
                         String conductor_direccion, String conductor_ubigeo, String infraccion_tipo, String infraccion_via,
                         String empresa_ruc, String empresa_rsocial, String cobrador_dni, String cobrador_nombres, String cobrador_apaterno,
                         String cobrador_amaterno, String cobrador_direccion, String cobrador_tipo_documento,
                         String vehiculo_moto,  String empresa_direccion,  String vehiculo_tarjeta,  String cobrador_edad,String reglamento_txt,String infraccion_tipo_txt)
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


            acta_educativa = 0;
            if(chbeducativa.isChecked()) {
                acta_educativa = 1;
            }


            //Toast.makeText(getApplicationContext(),"JAJAJA", Toast.LENGTH_SHORT).show();


            sd.execSQL("INSERT INTO t_acta VALUES(null,'" + acta_preimpreso + "','" + acta_operador + "','" + acta_fecha_registro + "','" + acta_observaciones + "','" + acta_tipo_servicio + "','" + acta_estado + "','" + vehiculo_placa + "','" + vehiculo_ws + "','" + conductor_tipo_documento + "','" + conductor_dni + "','" + conductor_nombres + "','" + conductor_apaterno + "','" + conductor_amaterno + "','" + conductor_licencia + "','" + conductor_licencia_clase + "','" + conductor_licencia_categoria + "','" + conductor_direccion + "','" + conductor_ubigeo + "','" + infraccion_tipo + "','" + infraccion_via + "','" + empresa_ruc + "','" + empresa_rsocial + "','" + cobrador_dni + "','" + cobrador_nombres + "','" + cobrador_apaterno + "','" + cobrador_amaterno + "','" + cobrador_direccion + "','" + cobrador_tipo_documento + "','" + vehiculo_moto + "','" + empresa_direccion + "','" + vehiculo_tarjeta + "','" +cobrador_edad + "','" +reglamento_txt +  "','" +infraccion_tipo_txt + "','" +acta_educativa + "','" + imp_pacta_tipo_servicio + "','" + imp_codRutaEmpresa  + "')");
            Log.e("base sqlite",acta_fecha_registro + "','" + acta_observaciones + "','" + acta_tipo_servicio + "','" + acta_estado + "','" + vehiculo_placa + "','" + vehiculo_ws + "','" + conductor_tipo_documento + "','" + conductor_dni + "','" + acta_educativa);
            db.close(); // Closing database connection

            /*Intent paso = new Intent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            paso.putExtra("incidencia", "local" );
            setResult(RESULT_OK, paso);

            finish();*/

            tipo_impresion = "offline";
            myDialog.show();
            myDialog.setCancelable(false);




        } catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("yiyi", e.getMessage());
            e.printStackTrace();
        }
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






}
