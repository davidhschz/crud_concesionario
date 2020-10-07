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
import android.widget.RadioButton;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    EditText username, password, email;
    RadioButton admin, user;
    Button signupbtn;
    DataBaseSQLite osqlite = new DataBaseSQLite(this,"DBCONCESIONARIO",null,1);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        username = findViewById(R.id.signupusername);
        password = findViewById(R.id.signuppassword);
        email = findViewById(R.id.signupemail);
        admin = findViewById(R.id.signupadmin);
        user = findViewById(R.id.signupuser);
        signupbtn = findViewById(R.id.signupbtn);

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && (admin.isChecked() || user.isChecked())){
                    SQLiteDatabase db = osqlite.getReadableDatabase();
                    String query = "SELECT email FROM user WHERE email = '" + email.getText().toString()+"'";
                    Cursor cuser = db.rawQuery(query,null);
                    if (cuser.moveToFirst()){
                        Toast.makeText(getApplicationContext(), "El Email ya está en uso", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        SQLiteDatabase db1 = osqlite.getWritableDatabase();
                        try{
                            ContentValues cvuser = new ContentValues();
                            cvuser.put("email", email.getText().toString().trim());
                            cvuser.put("password", password.getText().toString().trim());
                            cvuser.put("username", username.getText().toString().trim());
                            if (admin.isChecked()){
                                cvuser.put("role", "1");
                            }
                            else{
                                cvuser.put("role", "2");
                            }
                            db1.insert("user",null,cvuser);
                            db1.close();
                        }
                        catch (Exception e){
                            Toast.makeText(getApplicationContext(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(getApplicationContext(), "Usuario Creado Satisfactoriamente", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), UserActivity.class));
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}