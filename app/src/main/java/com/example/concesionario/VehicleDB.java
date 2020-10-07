package com.example.concesionario;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

public class VehicleDB extends SQLiteOpenHelper {

    String tblvehicle = "Create Table vehicle (plateNumber text primary key, brand text, model text, price text)";

    public VehicleDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tblvehicle);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE vehicle");
        db.execSQL(tblvehicle);
    }
}
