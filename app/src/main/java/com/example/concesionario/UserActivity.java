package com.example.concesionario;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
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
    String previousPN;
    boolean busqueda = false;
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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!plateNumber.getText().toString().isEmpty()){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserActivity.this);
                    alertDialogBuilder.setMessage("Está seguro de eliminar el vehículo con placa: " + plateNumber.getText().toString() + "?");
                    alertDialogBuilder.setPositiveButton("Si",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SQLiteDatabase db = osqlite.getWritableDatabase();
                                    db.execSQL("DELETE FROM vehicle WHERE plateNumber = '" + plateNumber.getText().toString() +"'");
                                    db.close();
                                    Toast.makeText(UserActivity.this, "Vehículo eliminado correctamente", Toast.LENGTH_SHORT).show();
                                }
                            });
                    alertDialogBuilder.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else{
                    Toast.makeText(UserActivity.this, "Ingrese la placa del vehículo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (busqueda) {
                    updateVehicle(plateNumber.getText().toString().trim(), brand.getText().toString().trim(), price.getText().toString().trim(), model.getText().toString().trim());    
                }
                else {
                    Toast.makeText(UserActivity.this, "Para actualizar primero debe buscar el vehículo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!plateNumber.getText().toString().isEmpty()){
                    busqueda = true;
                    searchPlateNumber();
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

    private void updateVehicle(String plateNumber, String brand, String price, String model) {
        if (!plateNumber.isEmpty() && !brand.isEmpty() && !price.isEmpty() && !model.isEmpty()){
            SQLiteDatabase db = osqlite.getWritableDatabase();
            SQLiteDatabase db1 = osqlite.getReadableDatabase();
            if (plateNumber.equals(previousPN)){
                db.execSQL("UPDATE vehicle SET brand = '" + brand + "', price = '" + price + "', model = '" + model + "' WHERE plateNumber = '" + plateNumber + "'");
                db.close();
                Toast.makeText(this, "Datos de vehículo actualizados correctamente", Toast.LENGTH_SHORT).show();
            }
            else{
                String query = "Select plateNumber From vehicle Where plateNumber = '" + plateNumber + "'";
                Cursor cdataVehicle = db1.rawQuery(query, null);
                if (cdataVehicle.moveToFirst()){
                    Toast.makeText(this, "Ya existe un registro con esta placa", Toast.LENGTH_SHORT).show();
                }
                else {
                    db.execSQL("UPDATE vehicle SET plateNumber = '" + plateNumber + "', brand = '" + brand + "', price = '" + price + "', model = '" + model + "' WHERE plateNumber = '" + previousPN + "'");
                    db.close();
                    Toast.makeText(this, "Datos de vehículo actualizados correctamente", Toast.LENGTH_SHORT).show();
                    busqueda = false;
                }
            }
        }
        else {
            Toast.makeText(UserActivity.this, "Ingrese todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private String searchPlateNumber() {
        if (!plateNumber.getText().toString().isEmpty()) {
            SQLiteDatabase db = osqlite.getWritableDatabase();
            String query = "SELECT brand, model, price FROM vehicle WHERE plateNumber = '" + plateNumber.getText().toString() + "'";
            Cursor cvehicledata = db.rawQuery(query, null);
            if (cvehicledata.moveToFirst()) {
                previousPN = plateNumber.getText().toString().trim();
                brand.setText(cvehicledata.getString(0));
                price.setText(cvehicledata.getString(2));
                model.setText(cvehicledata.getString(1));
            } else {
                Toast.makeText(this, "El vehículo con placa " + plateNumber.getText().toString().trim() + " No está registrado", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(UserActivity.this, "Ingrese todos los campos", Toast.LENGTH_SHORT).show();
        }
        return previousPN;
    }

    private void seleccionarVistaUsuario(boolean admin) {
        if (admin){
            add.setEnabled(true);
            search.setEnabled(true);
            update.setEnabled(true);
            delete.setEnabled(true);
            list.setEnabled(true);
        }
        else{
            add.setEnabled(false);
            search.setEnabled(true);
            update.setEnabled(false);
            delete.setEnabled(false);
            list.setEnabled(false);
        }
    }
}