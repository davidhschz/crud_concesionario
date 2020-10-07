package com.example.concesionario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends SignUp {

    EditText emaillog, passwordlog;
    TextView signup;
    RadioButton admin, user;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emaillog = findViewById(R.id.etemail);
        passwordlog = findViewById(R.id.etpassword);
        signup = findViewById(R.id.tvsignup);
        admin = findViewById(R.id.rbtnadmin);
        user = findViewById(R.id.rbtnuser);
        login = findViewById(R.id.btnlogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!emaillog.getText().toString().isEmpty() && !passwordlog.getText().toString().isEmpty() && (admin.isChecked() || user.isChecked())){
                    SQLiteDatabase db = osqlite.getReadableDatabase();
                    String query = "SELECT password FROM user WHERE email = '" + emaillog.getText().toString()+"'";
                    Cursor cuserinfo = db.rawQuery(query, null);
                    if (cuserinfo.moveToFirst()){
                        if (passwordlog.getText().toString().trim().equals(cuserinfo.getString(0))){
                            startActivity(new Intent(getApplicationContext(), UserActivity.class));
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Contraseña inválida", Toast.LENGTH_SHORT).show();
                            passwordlog.setText("");
                        }
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Usuario no existe", Toast.LENGTH_SHORT).show();
                    }

                    /*if (username.getText().toString().equals("admin") && password.getText().toString().equals("1234")){
                        startActivity(new Intent(getApplicationContext(), UserActivity.class));
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "No coinciden", Toast.LENGTH_SHORT).show();
                    }*/
                }
                else{
                    Toast.makeText(getApplicationContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUp.class));
            }
        });

    }
}