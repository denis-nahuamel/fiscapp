package com.fiscapp.fiscapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by JJR2 on 11/6/17.
 */

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "SharedPrefUbusDriver";


    private static final String position = "position";
    private static final String config = "config";
    private static final String dominio = "dominio";


    //region response JSON

    private static final String responseReglamento = "responseReglamento";
    private static final String responseTipoServicio = "responseTipoServicio";
    private static final String responseTipoDocumento = "responseTipoDocumento";
    private static final String responseEmpresaTransportes = "responseEmpresaTransportes";
    private static final String responseClaseLicencia = "responseClaseLicencia";
    private static final String responseInfraccion = "responseInfraccion";
    private static final String responseCatLicencia = "responseCatLicencia";
    private static final String responseListadoIncidencias = "responseListadoIncidencias";
    private static final String responseListadoIncidenciasMap = "responseIncidenciasMap";

    private static final String responseListadoActas = "responseListadoActas";

    //region USER LOGION
    private static final String USER_KEY_ID = "user_id";
    private static final String USER_KEY_USUARIO = "user_usuario";
    private static final String USER_KEY_NOMBRES = "user_nombres";
    private static final String USER_KEY_APELLIDOS = "user_apellidos";
    private static final String USER_KEY_CELULAR = "user_celular";




    //region CONFIGURACION
    private static final String tiempo_anulacion_S = "tiempo_anulacion";
    private static final String tiempo_emergencia_S = "tiempo_emergencia";
    private static final String url_upload_S = "url_upload";
    private static final String pw_ftp_S = "pw_ftp";
    private static final String usuario_ftp_S = "usuario_ftp";
    private static final String numero_emergencia_S = "numero_emergencia";
    private static final String numero_sms_S = "numero_sms";
    private static final String acta_header_S = "acta_header";
    private static final String acta_footer_S = "acta_footer";



    //region Bus
    private static final String B_KEY_ID = "b_id";
    private static final String B_KEY_PLACA = "placa";
    private static final String B_MARCA = "marca";
    private static final String B_TIPO = "tipo";
    private static final String B_CAPACIDAD = "capacidad";
    private static final String B_NUMERO_ASIENTOS = "nasientos";
    private static final String B_KEY_ID_EMPRESA = "idempresa";
    private static final String B_NOMBRE_COMERCIAL = "ncomercial";
    private static final String B_FOTO = "foto";
    //endregion Bus
    //region Driver
    private static final String D_KEY_ID = "d_id";
    private static final String D_KEY_DOC = "doc";
    private static final String D_NOMBRES = "nombres";
    private static final String D_APELLIDOS = "apellidos";
    private static final String D_KEY_USERNAME = "username";
    private static final String D_KEY_EMAIL = "email";
    private static final String D_CELULAR = "celular";
    private static final String D_DIRECCION = "direccion";
    private static final String D_CALIFICACION = "calificacion";
    private static final String D_FOTO = "foto";
    //endregion Driver
    //region Stops
    private static final String S_KEY_ID = "s_id";
    private static final String S_DATA = "data";
    private static final String S_DIRECCION = "direccion";
    private static final String S_RUTA = "ruta";
    private static final String S_CANTIDAD = "cant";
    private static final String S_KEY_STOPS = "stops";
    //endregion Stops
    //region Ruta
    private static final String R_KEY_RUTA = "ruta";
    private static final String R_RUTA_DATA = "rutadata";
    private static final String R_DIRECCION = "direccion";
    private static final String R_ID_EMPRESA = "idempresa";
    private static final String R_SNAP_RUTA = "";

    //region Empresa
    private static final String E_ID = "eid";
    private static final String E_RUC = "ruc";
    private static final String E_RAZON_SOCIAL = "razonsocial";
    private static final String E_N_COMERCIAL = "ncomercial";
    private static final String E_GERENTE = "gerente";
    private static final String E_TELEFONO = "telefono";
    private static final String E_CANT_BUSES = "cantbuses";
    private static final String E_CALIFICACION = "calificacion";
    private static final String E_INICIO_LOCATION = "iniciolocation";
    private static final String E_INICIO = "inicio";
    private static final String E_FINAL_LOCATION = "finallocation";
    private static final String E_FINAL = "final";
    private static final String E_RUTA = "idruta";
    private static final String E_FOTO = "foto";
    //endregion Empresa

    //region Frecuencia
    private static final String F_ID = "fid";
    private static final String F_ID_EMPRESA = "idempresa";
    private static final String F_DIRECCION = "direccion";
    private static final String F_DATA = "fdata";
    //endregion Frecuencia

    //region meFrecuencia
    private static final String MF_ID = "fid";
    private static final String MF_ID_BUS = "idbus";
    private static final String MF_DIRECCION = "direccion";
    private static final String MF_DATA = "fdata";
    //endregion meFrecuencia

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean logoutAll(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    //region Bus
    public boolean busLogin(int id, String placa, String marca, String tipo, String capacidad,
                            String nasientos, String idempresa, String ncomercial, String foto){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(B_KEY_ID, id);
        editor.putString(B_KEY_PLACA, placa);
        editor.putString(B_MARCA, marca);
        editor.putString(B_TIPO, tipo);
        editor.putString(B_CAPACIDAD, capacidad);
        editor.putString(B_NUMERO_ASIENTOS, nasientos);
        editor.putString(B_KEY_ID_EMPRESA, idempresa);
        editor.putString(B_NOMBRE_COMERCIAL, ncomercial);
        editor.putString(B_FOTO, foto);

        editor.apply();

        return true;
    }




    //region Bus
    public boolean usuarioLogin(int id, String usuario, String nombres, String apellidos, String celular){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(USER_KEY_ID, id);
        editor.putString(USER_KEY_USUARIO, usuario);
        editor.putString(USER_KEY_NOMBRES, nombres);
        editor.putString(USER_KEY_APELLIDOS, apellidos);
        editor.putString(USER_KEY_CELULAR, celular);



        editor.apply();

        return true;
    }


    public boolean setresponseReglamento(String c) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(responseReglamento, c);


        editor.apply();
        return true;
    }

    public String getresponseReglamento() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(responseReglamento,null);
    }

    public boolean isresponseReglamentoNull(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(responseReglamento,null) == null;
    }



    public boolean setresponseTipoServicio(String c) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(responseTipoServicio, c);


        editor.apply();
        return true;
    }

    public String getresponseTipoServicio() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(responseTipoServicio,null);
    }

    public boolean isresponseTipoServicioNull(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(responseTipoServicio,null) == null;
    }



    public boolean setresponseTipoDocumento(String c) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(responseTipoDocumento, c);


        editor.apply();
        return true;
    }

    public String getresponseTipoDocumento() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(responseTipoDocumento,null);
    }

    public boolean isresponseTipoDocumentoNull(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(responseTipoDocumento,null) == null;
    }



    public boolean setresponseEmpresaTransportes(String c) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(responseEmpresaTransportes, c);


        editor.apply();
        return true;
    }

    public String getresponseEmpresaTransportes() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(responseEmpresaTransportes,null);
    }

    public boolean isresponseEmpresaTransportesNull(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(responseEmpresaTransportes,null) == null;
    }



    public boolean setresponseClaseLicencia(String c) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(responseClaseLicencia, c);


        editor.apply();
        return true;
    }

    public String getresponseresponseClaseLicencia() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(responseClaseLicencia,null);
    }

    public boolean isresponseClaseLicenciasNull(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(responseClaseLicencia,null) == null;
    }



    public boolean setresponseInfraccion(String id_reglamento, String c) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(responseInfraccion + id_reglamento + "", c);


        editor.apply();
        return true;
    }

    public String getresponseInfraccion(String id_reglamento) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(responseInfraccion + id_reglamento + "",null);
    }

    public boolean isresponseInfraccionNull(String id_reglamento){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(responseInfraccion +  id_reglamento + "",null) == null;
    }



    public boolean setresponseListadoActas (String c) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(responseListadoActas, c);


        editor.apply();
        return true;
    }

    public String getresponseListadoActas() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(responseListadoActas,null);
    }

    public boolean isresponseListadoActasNull(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(responseListadoActas,null) == null;
    }


    public boolean setresponseListadoIncidencias (String c) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(responseListadoIncidencias, c);


        editor.apply();
        return true;
    }

    public String getresponseListadoIncidencias() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(responseListadoIncidencias,null);
    }

    public boolean isresponseListadoIncidenciasNull(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(responseListadoIncidencias,null) == null;
    }


    public boolean setresponseListadoIncidenciasMap(String c) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(responseListadoIncidenciasMap, c);


        editor.apply();
        return true;
    }

    public String getresponseListadoIncidenciasMap() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(responseListadoIncidenciasMap,null);
    }

    public boolean isresponseListadoIncidenciasMapNull(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(responseListadoIncidenciasMap,null) == null;
    }



    public boolean setCurrentPosition(String pos) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(position, pos);


        editor.apply();
        return true;
    }

    public String getCurrentPosition() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(position,null);
    }

    public boolean isCurrentPositionNull(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(position,null) == null;
    }




    public boolean setresponseCatLicencia(String c) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(responseCatLicencia, c);


        editor.apply();
        return true;
    }

    public String getresponseCatLicencia() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(responseCatLicencia,null);
    }

    public boolean isresponseCatLicenciaNull(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(responseCatLicencia,null) == null;
    }




    public boolean setConfig(String c) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

       editor.putString(config, c);


        editor.apply();
        return true;
    }

    public boolean setDominio(String c) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(dominio, c);


        editor.apply();
        return true;
    }

    public boolean setConfiguracion(String tiempo_anulacion, String tiempo_emergencia, String url_upload, String usuario_ftp, String pw_ftp,
                                    String numero_sms, String numero_emergencia, String acta_header, String acta_footer) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(tiempo_anulacion_S, tiempo_anulacion);
        editor.putString(tiempo_emergencia_S, tiempo_emergencia);
        editor.putString(url_upload_S, url_upload);
        editor.putString(usuario_ftp_S, usuario_ftp);
        editor.putString(pw_ftp_S, pw_ftp);
        editor.putString(numero_sms_S, numero_sms);
        editor.putString(numero_emergencia_S, numero_emergencia);
        editor.putString(acta_header_S, acta_header);
        editor.putString(acta_footer_S, acta_footer);




        editor.apply();

        return true;

    }

    public boolean isUsuarioLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_KEY_USUARIO,null) != null;
    }

    public boolean logoutUsuario(){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USER_KEY_ID);
        editor.remove(USER_KEY_USUARIO);
        editor.remove(USER_KEY_NOMBRES);
        editor.remove(USER_KEY_APELLIDOS);
        editor.remove(USER_KEY_CELULAR);


        /*editor.remove(position);
        editor.remove(config);
        editor.remove(responseReglamento);
        editor.remove(responseTipoServicio);
        editor.remove(responseTipoDocumento);
        editor.remove(responseEmpresaTransportes);
        editor.remove(responseClaseLicencia);
        editor.remove(responseInfraccion);
        editor.remove(responseCatLicencia);*/
        editor.remove(responseListadoIncidencias);
        editor.remove(responseListadoIncidenciasMap);
        editor.remove(responseListadoActas);


        editor.apply();
        return true;

    }

    public String getConfig() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(config,null);
    }

    public String getDominio() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(dominio,null);
    }

    public boolean isDominioNull(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(dominio,null) == null;
    }

    public boolean isConfigNull(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(config,null) != null;
    }

    public int getUserKeyId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(USER_KEY_ID,0);
    }


    public String getUserKeyUsuario() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_KEY_USUARIO,null);
    }

    public String getUserNombresUsuario() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_KEY_NOMBRES,null);
    }

    public String getUserApellidosUsuario() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_KEY_APELLIDOS,null);
    }

    public String getUserCelularUsuario() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_KEY_CELULAR,null);
    }

    public boolean isBusLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(B_KEY_PLACA,null) != null;
    }

    public boolean logoutBus(){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(B_KEY_ID);
        editor.remove(B_KEY_PLACA);
        editor.remove(B_MARCA);
        editor.remove(B_TIPO);
        editor.remove(B_CAPACIDAD);
        editor.remove(B_NUMERO_ASIENTOS);
        editor.remove(B_FOTO);
        editor.remove(B_KEY_ID_EMPRESA);
        editor.remove(B_NOMBRE_COMERCIAL);
        editor.apply();
        return true;

    }
    //endregion Bus


    //region Driver
    public boolean driverLogin(int id, String doc, String nombres, String apellidos,
                               String username, String email, String celular,
                               String direccion, String calificacion, String foto){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(D_KEY_ID, id);
        editor.putString(D_KEY_DOC, doc );
        editor.putString(D_NOMBRES, nombres);
        editor.putString(D_APELLIDOS, apellidos);
        editor.putString(D_KEY_USERNAME, username);
        editor.putString(D_KEY_EMAIL, email);
        editor.putString(D_CELULAR, celular);
        editor.putString(D_DIRECCION, direccion);
        editor.putString(D_CALIFICACION, calificacion);
        editor.putString(D_FOTO, foto);

        editor.apply();

        return true;
    }

    public boolean isDriverLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(D_KEY_DOC,null) != null;
    }

    public boolean logoutDriver(){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(D_KEY_ID);
        editor.remove(D_KEY_DOC);
        editor.remove(D_NOMBRES);
        editor.remove(D_APELLIDOS);
        editor.remove(D_KEY_USERNAME);
        editor.remove(D_KEY_EMAIL);
        editor.remove(D_CELULAR);
        editor.remove(D_DIRECCION);
        editor.remove(D_CALIFICACION);
        editor.remove(D_FOTO);
        editor.apply();
        return true;

    }
    //endregion Driver

    //region Stops
    public boolean setStops(int idruta, String ruta, int direccion, String data, int cantidadparaderos){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(S_KEY_ID, idruta);
        editor.putString(S_RUTA, ruta);
        editor.putInt(S_DIRECCION, direccion);
        editor.putString(S_DATA, data);
        editor.putInt(S_CANTIDAD, cantidadparaderos);
        editor.apply();

        return true;
    }

    public boolean setAllStops(String data){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(S_KEY_STOPS, data);
        editor.apply();

        return true;
    }

    public String getAllParaderos() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(S_KEY_STOPS,null);
    }

    public boolean setStopsWithDireccion(String dir, String response){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(dir, response);

        editor.apply();

        return true;
    }

    public boolean isStops(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(S_RUTA,null) != null;
    }
    public boolean clearStops(){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(S_RUTA);
        editor.remove(S_CANTIDAD);
        editor.remove(S_DATA);
        editor.remove(S_DIRECCION);
        editor.remove(S_KEY_ID);
        editor.apply();
        return true;

    }
    //endregion Stops


    //region Ruta
    public boolean setRuta(int ruta, String rutadata, String direccion, String idempresa){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(R_KEY_RUTA, ruta);
        editor.putString(R_RUTA_DATA, rutadata);
        editor.putString(R_DIRECCION, direccion);
        editor.putString(R_ID_EMPRESA, idempresa);
        editor.apply();
        return true;
    }
    //region Ruta
    public boolean setSnapRuta(String snap, String dir){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(dir, snap);
        editor.apply();
        return true;
    }
    public boolean isSnap(String dir){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(dir,null) != null;
    }

    public boolean setSData(String data, String dir) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("dir"+dir, data);
        editor.apply();
        return true;
    }

    public boolean isStops(String dir){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("dir"+dir,null) != null;
    }

    public boolean isRuta(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(R_KEY_RUTA,null) != null;
    }
    public boolean clearRuta(){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(R_KEY_RUTA);
        editor.remove(R_RUTA_DATA);
        editor.remove(R_DIRECCION);
        editor.remove(R_ID_EMPRESA);
        editor.apply();
        return true;

    }
    //endregion Stops

    //region Empresa
    public boolean setEmpresaData(int id, String ruc, String razonsocial, String ncomercial, String gerente,
                                  String telefono, String cantbuses, int calificacion, String iniciolocation,
                                  String ninicio, String finallocation, String nfinal,
                                  String ruta, String foto){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(E_ID, id);
        editor.putString(E_RUC, ruc);
        editor.putString(E_RAZON_SOCIAL, razonsocial);
        editor.putString(E_N_COMERCIAL, ncomercial);
        editor.putString(E_GERENTE, gerente);
        editor.putString(E_TELEFONO, telefono);
        editor.putString(E_CANT_BUSES, cantbuses);
        editor.putInt(E_CALIFICACION, calificacion);
        editor.putString(E_INICIO_LOCATION, iniciolocation);
        editor.putString(E_INICIO, ninicio);
        editor.putString(E_FINAL_LOCATION, finallocation);
        editor.putString(E_FINAL, nfinal);
        editor.putString(E_RUTA, ruta);
        editor.putString(E_FOTO, foto);

        editor.apply();

        return true;
    }

    public boolean clearEmpresaData(){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(E_ID);
        editor.remove(E_RUC);
        editor.remove(E_RAZON_SOCIAL);
        editor.remove(E_N_COMERCIAL);
        editor.remove(E_GERENTE);
        editor.remove(E_TELEFONO);
        editor.remove(E_CANT_BUSES);
        editor.remove(E_CALIFICACION);
        editor.remove(E_INICIO_LOCATION);
        editor.remove(E_INICIO);
        editor.remove(E_FINAL_LOCATION);
        editor.remove(E_FINAL);
        editor.remove(E_RUTA);
        editor.remove(E_FOTO);
        editor.apply();
        return true;

    }
    //region Empresa
    public boolean setFrecuenciaData(int id, String idempresa, String direccion, String data){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(F_ID, id);
        editor.putString(F_ID_EMPRESA, idempresa);
        editor.putString(F_DIRECCION, direccion);
        editor.putString(F_DATA, data);

        editor.apply();

        return true;
    }

    public boolean clearFrecuencia(){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(F_ID);
        editor.remove(F_ID_EMPRESA);
        editor.remove(F_DIRECCION);
        editor.remove(F_DATA);
        editor.apply();
        return true;

    }

    //region mefrecuencia
    public boolean setMeFrecuencia(int id, String idbus, String direccion, String data){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(MF_ID, id);
        editor.putString(MF_ID_BUS, idbus);
        editor.putString(MF_DIRECCION, direccion);
        editor.putString(MF_DATA, data);

        editor.apply();

        return true;
    }

//    public ArrayList<String> getDataBus(){
//        ArrayList<String> busData = new ArrayList<String>();
//        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        busData.add(0,sharedPreferences.getString(B_KEY_ID,null));
//        busData.add(1,sharedPreferences.getString(B_KEY_PLACA,null));
//        busData.add(2,sharedPreferences.getString(B_MARCA,null));
//        busData.add(3,sharedPreferences.getString(B_TIPO,null));
//        busData.add(4,sharedPreferences.getString(B_CAPACIDAD,null));
//        busData.add(5,sharedPreferences.getString(B_NUMERO_ASIENTOS,null));
//        busData.add(6,sharedPreferences.getString(B_FOTO,null));
//        busData.add(7,sharedPreferences.getString(B_KEY_ID_EMPRESA,null));
//        busData.add(8,sharedPreferences.getString(B_NOMBRE_COMERCIAL,null));
//        busData.add(9,sharedPreferences.getString(B_KEY_ID_CONDUCTOR,null));
//        return busData;
//    }

    public int getbKeyId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(B_KEY_ID,0);
    }

    public String getbKeyPlaca() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(B_KEY_PLACA,null);
    }

    public String getbMarca() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(B_MARCA,null);
    }

    public String getbTipo() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(B_TIPO,null);
    }

    public String getbCapacidad() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(B_CAPACIDAD,null);
    }

    public String getbNumeroAsientos() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(B_NUMERO_ASIENTOS,null);
    }

    public String getbFoto() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(B_FOTO,null);
    }

    public String getbKeyIdEmpresa() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(B_KEY_ID_EMPRESA,null);
    }

    public String getbNombreComercial() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(B_NOMBRE_COMERCIAL,null);
    }

    public int getdKeyId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(D_KEY_ID,0);
    }

    public String getdKeyDoc() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(D_KEY_DOC,null);
    }

    public String getdNombres() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(D_NOMBRES,null);
    }

    public String getdApellidos() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(D_APELLIDOS,null);
    }

    public String getdKeyUsername() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(D_KEY_USERNAME,null);
    }

    public String getdKeyEmail() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(D_KEY_EMAIL,null);
    }

    public String getdCelular() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(D_CELULAR,null);
    }

    public String getdDireccion() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(D_DIRECCION,null);
    }

    public String getdCalificacion() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(D_CALIFICACION,null);
    }

    public String getdFoto() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(D_FOTO,null);
    }

    public String getsKeyId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(S_KEY_ID,null);
    }

    public String getsData(String dir) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("dir"+dir,null);
    }

    public String getParaderosData(String dir) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(dir,null);
    }

    public String getsDireccion() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(S_DIRECCION,null);
    }

    public String getsRuta() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(S_RUTA,null);
    }

    public String getsCantidad() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(S_CANTIDAD,null);
    }

    public String getrKeyRuta() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(R_KEY_RUTA,null);
    }

    public String getrRutaData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(R_RUTA_DATA,null);
    }

    public String getrDireccion() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(R_DIRECCION,null);
    }

    public String getrIdEmpresa() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(R_ID_EMPRESA,null);
    }

    public String getrSnapRuta(String dir) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(dir,null);
    }

    ///Empresa
    public String geteId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(E_ID,null);
    }

    public String geteRuc() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(E_RUC,null);
    }

    public String geteRazonSocial() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(E_RAZON_SOCIAL,null);
    }

    public String geteNComercial() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(E_N_COMERCIAL,null);
    }

    public String geteGerente() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(E_GERENTE,null);
    }

    public String geteTelefono() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(E_TELEFONO,null);
    }

    public String geteCantBuses() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(E_CANT_BUSES,null);
    }

    public String geteCalificacion() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(E_CALIFICACION,null);
    }

    public String geteInicioLocation() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(E_INICIO_LOCATION,null);
    }

    public String geteInicio() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(E_INICIO,null);
    }

    public String geteFinalLocation() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(E_FINAL_LOCATION,null);
    }

    public String geteFinal() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(E_FINAL,null);
    }

    public String geteRuta() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(E_RUTA,null);
    }

    public String geteFoto() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(E_FOTO,null);
    }


    public String getfId() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(F_ID,null);
    }

    public String getfIdEmpresa() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(F_ID_EMPRESA,null);
    }

    public String geteDireccion() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(F_DIRECCION,null);
    }

    public String getfData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(F_DATA,null);
    }
}
