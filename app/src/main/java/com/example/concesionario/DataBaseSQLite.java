package com.example.concesionario;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseSQLite extends SQLiteOpenHelper {

    String tbluser = "Create Table user (email text primary key, password text,username text, role text)";
    String tblvehicle = "Create Table vehicle (plateNumber text primary key, brand text, model text, price text)";

    public DataBaseSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tbluser);
        db.execSQL(tblvehicle);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE user");
        db.execSQL(tbluser);
        db.execSQL("DROP TABLE vehicle");
        db.execSQL(tblvehicle);
    }
}
