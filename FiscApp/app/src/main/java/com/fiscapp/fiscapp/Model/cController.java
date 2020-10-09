package com.fiscapp.fiscapp.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class cController extends data_base {
    public cController(Context context) {
        super(context);
    }

    public boolean create(cPersona objectPerson) {

        ContentValues values = new ContentValues();

        values.put("pers_nombres", objectPerson.getPersona_nombres());
        values.put("pers_dni", objectPerson.getPersona_dni());
        values.put("pers_tema", objectPerson.getPersona_tema());
        values.put("pers_informacion", objectPerson.getPersona_informacion());
        values.put("pers_foto", objectPerson.getPersona_foto());
        values.put("obs", objectPerson.getObservaciones());
        values.put("fecha", objectPerson.getFecha());
        values.put("vigencia", objectPerson.getVigencia());
        values.put("tipo", objectPerson.getTipo());


        SQLiteDatabase db = this.getWritableDatabase();

        boolean createSuccessful = db.insert("t_person", null, values) > 0;
        db.close();

        return createSuccessful;
    }

    public List<cPersona> read(String param) {
	List<cPersona> recordsList = new ArrayList<>();

        if(param.length() > 0) {
            

            String sql = "SELECT * FROM t_person WHERE pers_nombres LIKE '%" + param + "%' OR pers_dni LIKE '%" + param + "%'";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(sql, null);

            if (cursor.moveToFirst()) {
                do {

                    int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                    String nombre = cursor.getString(cursor.getColumnIndex("pers_nombres"));
                    String dni = cursor.getString(cursor.getColumnIndex("pers_dni"));
                    String tema = cursor.getString(cursor.getColumnIndex("pers_tema"));
                    String informacion = cursor.getString(cursor.getColumnIndex("pers_informacion"));
                    String foto = cursor.getString(cursor.getColumnIndex("pers_foto"));
                    String observaciones = cursor.getString(cursor.getColumnIndex("obs"));
                    String fecha = cursor.getString(cursor.getColumnIndex("fecha"));
                    String vigencia = cursor.getString(cursor.getColumnIndex("vigencia"));
                    String tipo = cursor.getString(cursor.getColumnIndex("tipo"));

                    cPersona objectPerson = new cPersona();

                    objectPerson.setPersona_nombres(nombre);
                    objectPerson.setPersona_dni(dni);
                    objectPerson.setPersona_tema(tema);
                    objectPerson.setPersona_informacion(informacion);
                    objectPerson.setPersona_foto(foto);
                    objectPerson.setObservaciones(observaciones);
                    objectPerson.setFecha(fecha);
                    objectPerson.setVigencia(vigencia);
                    objectPerson.setTipo(tipo);

                    recordsList.add(objectPerson);

                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

        }
        return recordsList;
    }
}
