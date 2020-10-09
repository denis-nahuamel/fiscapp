package com.fiscapp.fiscapp.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class data_base extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    protected static final String DATABASE_NAME = "data";

    public data_base(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE t_person " +
                "( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "pers_nombres TEXT, " +
                "pers_dni TEXT, " +
                "pers_tema TEXT, " +
                "pers_informacion TEXT, " +
                "pers_foto TEXT, " +
                "obs TEXT, " +
                "fecha TEXT, " +
                "vigencia TEXT, " +
                "tipo TEXT ) ";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS t_person";
        db.execSQL(sql);

        onCreate(db);
    }

    public int count() {

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * FROM t_person";
        int recordCount = db.rawQuery(sql, null).getCount();
        db.close();

        return recordCount;

    }

    public void delete() {
        boolean deleteSuccessful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        deleteSuccessful = db.delete("t_person", null, null) > 0;
        db.close();

        //return deleteSuccessful;

    }
}
