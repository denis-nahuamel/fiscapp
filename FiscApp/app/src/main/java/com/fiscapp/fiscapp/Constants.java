package com.fiscapp.fiscapp;

/**
 * Created by JJR2 on 4/3/17.
 */

public class Constants {

    private static final String ROOT_URL = "http://www.ubusgo.com/driver/actions/";

    /*INICIO DE SESION & SALIR DE SESION*/
    public static final String URL_USUARIOLOGIN = ROOT_URL + "UsuarioLogin.php";
    public static final String URL_USUARIOLOGOUT = ROOT_URL + "UsuarioLogout.php";
    /*ACTUALIZAR LA UBICACION DEL USUARIO*/
    public static final String URL_UPDATEUSERLOCATION = ROOT_URL + "updateUserLocation.php";
    /*OBTENER LA POSICION DE TODOS LOS USUARIOS EXCEPTO LA MIA*/
    public static final String URL_GETONLINEUSERS = ROOT_URL + "getOnlineUsers.php";

    /*INCIDENCIAS*/
    //OBTENER TODAS LAS INCIDENCIAS REGISTRADAS EN UNA FECHA
    public static final String URL_GETINCIDENCIAS= ROOT_URL + "getIncidencias.php";
    //REGISTRAR UNA INCIDENCIA
    public static final String URL_SAVEINCIDENCIA = ROOT_URL + "saveIncidencia.php";
    //REGISTRAR SI UN USUARIO AH LEIDO UNA INCIDENCIA
    public static final String URL_SAVEUSUARIOINCIDENCIA = ROOT_URL + "saveUsuarioIncidencia.php";

    public static final String URL_UPDATEESTADOINCIDENCIA = ROOT_URL + "updateEstadoIncidencia.php";

    public static final String ROOT_URL_JAIRO = "http://infomaz.com/?var=dominio";

    private static final String ROOT_BASE_URL = "http://infomaz.com/?var=";
    public static final String URL_ACCEDER = "acceder";
    public static final String URL_CERRAR_SESION =  "cerrar_sesion";
    public static final String URL_CONFIGURACION = "listar_configuracion";
    public static final String URL_LISTAR_INCIDENCIAS =  "listar_incidencias";
    public static final String URL_GUARDAR_LEIDO = "guardar_leido";
    public static final String URL_GUARDAR_NOTIFICADO =  "guardar_notificado";
    public static final String URL_RECUPERAR_INCIDENCIA = "recuperar_incidencia";
    public static final String URL_GUARDAR_INCIDENCIA =  "guardar_incidencia";
    public static final String URL_LISTAR_OPERADORES =  "listar_coordenadas";
    public static final String URL_ACTUALIZAR_UBICACION =  "editar_coordenadas";
    public static final String URL_LISTAR_CONFIGURACION = "listar_configuracion";
    public static final String URL_LISTAR_ACTAS= "listar_actas_dia";
    public static final String URL_GUARDAR_ACTA= "guardar_acta";
    public static final String URL_LISTAR_REGLAMENTO= "listar_reglamento";
    public static final String URL_LISTAR_TIPOSERVICIO= "listar_tipo_servicio";
    public static final String URL_LISTAR_DOCUMENTO= "listar_documento";
    public static final String URL_LISTAR_E_TRANSP= "listar_empresas_transportes";
    public static final String URL_LISTAR_LICENCIA_CLASE= "listar_licencia_clase";
    public static final String URL_LISTAR_LICENCIA_CATEGORIA= "listar_licencia_categoria";
    public static final String URL_ANULAR_ACTA= "anular_acta";
    //DENNIS
    public static final String URL_LISTAR_TIPO_INCIDENCIA =  "listar_tipo_incidencia";


    public static final String BASE_URL = "http://infomaz.com/";

    /*ACTAS*/
    public static final String URL_RECUPERAR_ACTA = ROOT_URL_JAIRO + "recuperar_acta";



}