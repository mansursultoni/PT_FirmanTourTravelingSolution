package com.firmantour.travelingsolution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText nomor, password;
    Button masuk, daftar;
    CheckBox checkBox;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().
            getReferenceFromUrl("https://pt-firman-tour-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        nomor = findViewById(R.id.etNomor);
        password = findViewById(R.id.etPassword);


        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox = preferences.getString("ingat","");
        if (checkbox.equals("true")){
            startActivity(new Intent(LoginActivity.this, UserRentalMobil.class));
            finish();
        }else if (checkbox.equals("false")){

        }

        checkBox = findViewById(R.id.ingatsaya);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("ingat","true");
                    editor.apply();
                    Toast.makeText(LoginActivity.this, "Check", Toast.LENGTH_SHORT).show();
                }else {
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("ingat","false");
                    editor.apply();
                    Toast.makeText(LoginActivity.this, "Uncheck", Toast.LENGTH_SHORT).show();
                }
            }
        });

        daftar = findViewById(R.id.btnDaftar);
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, DaftarActivity.class));
                finish();
            }
        });

        masuk = findViewById(R.id.btnLogin);
        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txtnomor = nomor.getText().toString();
                String txtpassword = password.getText().toString();

                if (txtnomor.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Masukkan Nomor Telepon.", Toast.LENGTH_SHORT).show();
                } else if (txtpassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Masukkan Password.", Toast.LENGTH_SHORT).show();
                } else {
                    //langkah pertama cek user
                    databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(txtnomor)) {
                                final String getPassword = snapshot.child(txtnomor).child("password").getValue(String.class);
                                if (getPassword.equals(txtpassword)) {
                                    Toast.makeText(LoginActivity.this, "Login Berhasil.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, UserRentalMobil.class));
                                    finish();
                                }  else {
                                    //no action
                                }
                            } else {
                                //no action
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    //langkah kedua cek admin
                    databaseReference.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(txtnomor)) {
                                final String getPassword = snapshot.child(txtnomor).child("password").getValue(String.class);
                                if (getPassword.equals(txtpassword)) {
                                    Toast.makeText(LoginActivity.this, "Login Berhasil.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, AdminRentalMobil.class));
                                    finish();
                                }  else {
                                    Toast.makeText(LoginActivity.this, "Password Salah.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Nomor Telpon Salah.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}