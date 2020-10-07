package com.example.concesionario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserActivity extends AppCompatActivity {

    EditText plateNumber, brand, price, model;
    Button add, search, update, delete, list;
    //DataBaseSQLite osqlite = new DataBaseSQLite(this,"DBCONCESIONARIO",null,1);
    VehicleDB osqlite = new VehicleDB(this,"DBVEHICULO",null,1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        plateNumber = findViewById(R.id.actplatenumber);
        brand = findViewById(R.id.actbrand);
        price = findViewById(R.id.actprice);
        model = findViewById(R.id.actmodel);
        add = findViewById(R.id.actbtnadd);
        search = findViewById(R.id.actbtnsearch);
        update = findViewById(R.id.actbtnupdate);
        delete = findViewById(R.id.actbtndelete);
        list = findViewById(R.id.actbtnlist);
        
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!plateNumber.getText().toString().isEmpty()){
                    searchPlateNumber(plateNumber.getText().toString().trim());
                }
                else{
                    Toast.makeText(UserActivity.this, "Ingrese el número de placa", Toast.LENGTH_SHORT).show();
                }
            }
        });

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), List.class));
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!plateNumber.getText().toString().isEmpty() && !brand.getText().toString().isEmpty() && !price.getText().toString().isEmpty() && !model.getText().toString().isEmpty()){
                    SQLiteDatabase db = osqlite.getReadableDatabase();
                    String query = "SELECT plateNumber FROM vehicle WHERE plateNumber = '" + plateNumber.getText().toString()+"'";
                    Cursor cplateNumber = db.rawQuery(query,null);
                    if (cplateNumber.moveToFirst()){
                        Toast.makeText(UserActivity.this, "La placa ya está registrada", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        SQLiteDatabase db1 = osqlite.getWritableDatabase();
                        try{
                            ContentValues cvVehicle = new ContentValues();
                            cvVehicle.put("plateNumber", plateNumber.getText().toString().trim());
                            cvVehicle.put("brand", brand.getText().toString().trim());
                            cvVehicle.put("model", plateNumber.getText().toString().trim());
                            cvVehicle.put("price", price.getText().toString().trim());
                            db1.insert("vehicle", null, cvVehicle);
                            db1.close();
                            Toast.makeText(UserActivity.this, "Vehículo ingresado correctamente", Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e){
                            Toast.makeText(UserActivity.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } 
                }
                else{
                    Toast.makeText(UserActivity.this, "Ingrese todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void searchPlateNumber(String idVehicle) {
        SQLiteDatabase db = osqlite.getWritableDatabase();
        String query = "SELECT brand, model, price FROM vehicle WHERE plateNumber = '" + plateNumber.getText().toString()+"'";
        Cursor cvehicledata = db.rawQuery(query, null);
        if (cvehicledata.moveToFirst()){
            brand.setText(cvehicledata.getString(0));
            price.setText(cvehicledata.getString(1));
            model.setText(cvehicledata.getString(2));
        }
        else{
            Toast.makeText(this, "El vehículo con placa " + idVehicle + " No está registrado", Toast.LENGTH_SHORT).show();
        }
    }
}