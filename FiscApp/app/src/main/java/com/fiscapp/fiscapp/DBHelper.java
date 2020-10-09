package com.fiscapp.fiscapp;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


//Esto es una clase Aparte
public class DBHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_PATH = "";

    private static String DB_NAME = "mydb.db";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DBHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
        DB_PATH= myContext.getDatabasePath(DB_NAME).toString();
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getWritableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }

        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
        //  this.getReadableDatabase();

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH ;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH ;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH ;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }
    public void openDataBasetoWrite() throws SQLException{

        //Open the database
        String myPath = DB_PATH ;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE t_acta " +
                "(acta_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "acta_preimpreso TEXT, " +
                "acta_operador TEXT, " +
                "acta_fecha_registro TEXT, " +
                "acta_observaciones TEXT, " +
                "tipo_servicio TEXT, " +
                "acta_estado TEXT, " +
                "vehiculo_placa TEXT, " +
                "vehiculo_ws TEXT, " +
                "conductor_documento TEXT, " +
                "conductor_dni TEXT, " +
                "conductor_nombres TEXT, " +
                "conductor_apaterno TEXT, " +
                "conductor_amaterno TEXT, " +
                "licencia TEXT, " +
                "conductor_licencia_clase TEXT, " +
                "conductor_licencia_categoria TEXT, " +
                "conductor_direccion TEXT, " +
                "conductor_ubigeo TEXT, " +
                "infraccion_tipo TEXT, " +
                "infraccion_via TEXT, " +
                "empresa_ruc TEXT, " +
                "empresa_rsocial TEXT, " +
                "cobrador_dni TEXT, " +
                "cobrador_nombres TEXT, " +
                "cobrador_apaterno TEXT, " +
                "cobrador_amaterno TEXT, " +
                "cobrador_direccion TEXT, " +
                "cobrador_documento TEXT, " +
                "vehiculo_moto TEXT, " +
                "empresa_direccion TEXT, " +
                "vehiculo_tarjeta TEXT, " +
                "cobrador_edad TEXT, " +
                "reglamento_txt TEXT, " +
                "infraccion_tipo_txt TEXT, " +
                "acta_educativa INTEGER, " +
                "tipo_servicio_txt TEXT, " +
                "codruta_transporte TEXT ) ";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
};

