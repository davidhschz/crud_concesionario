package com.example.concesionario;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class List extends AppCompatActivity {

    ListView vehicleList;
    ArrayList<String> arrayvehiculos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        vehicleList = findViewById(R.id.vehicleList);
        loadVehicles();

    }

    private void loadVehicles() {
        arrayvehiculos = VehicleList();
        ArrayAdapter<String> adapterVehicles = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_expandable_list_item_1, arrayvehiculos);
        vehicleList.setAdapter(adapterVehicles);
    }

    private ArrayList<String> VehicleList() {
        ArrayList<String> data = new ArrayList<String>();
        VehicleDB osqlite = new VehicleDB(this,"DBVEHICULO",null,1);
        SQLiteDatabase db = osqlite.getReadableDatabase();
        String query = "SELECT plateNumber, brand, model, price FROM vehicle";
        Cursor cvehicles = db.rawQuery(query, null);
        if (cvehicles.moveToFirst()){
            do{
                String line = cvehicles.getString(0) + " " + cvehicles.getString(1) + " " + cvehicles.getString(2) + " " + cvehicles.getString(3);
                data.add(line);
            }while (cvehicles.moveToNext());
        }
        db.close();
        return data;
    }
}