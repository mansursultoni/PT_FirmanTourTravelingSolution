package com.firmantour.travelingsolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
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
    Switch active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nomor = findViewById(R.id.etNomor);
        password = findViewById(R.id.etPassword);
        active = findViewById(R.id.active);
        daftar = findViewById(R.id.btnDaftar);

        masuk = findViewById(R.id.btnLogin);
        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("login").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String input1 = nomor.getText().toString();
                        String input2 = password.getText().toString();

                        if (dataSnapshot.child(input1).exists()) {
                            if (dataSnapshot.child(input1).child("password").getValue(String.class).equals(input2)) {
                                if (active.isChecked()) {
                                    if (dataSnapshot.child(input1).child("as").getValue(String.class).equals("admin")) {
                                        preferences.setDataLogin(LoginActivity.this, true);
                                        preferences.setDataAs(LoginActivity.this, "admin");
                                        startActivity(new Intent(LoginActivity.this, AdminRentalMobil.class));
                                    } else if (dataSnapshot.child(input1).child("as").getValue(String.class).equals("user")) {
                                        preferences.setDataLogin(LoginActivity.this, true);
                                        preferences.setDataAs(LoginActivity.this, "user");
                                        startActivity(new Intent(LoginActivity.this, UserRentalMobil.class));
                                    }
                                } else {
                                    if (dataSnapshot.child(input1).child("as").getValue(String.class).equals("admin")) {
                                        preferences.setDataLogin(LoginActivity.this, false);
                                        startActivity(new Intent(LoginActivity.this, AdminRentalMobil.class));
                                    } else if (dataSnapshot.child(input1).child("as").getValue(String.class).equals("user")) {
                                        preferences.setDataLogin(LoginActivity.this, false);
                                        startActivity(new Intent(LoginActivity.this, UserRentalMobil.class));
                                    }
                                }

                            } else {
                                Toast.makeText(LoginActivity.this, "Kata sandi salah.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Data belum terdaftar.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (preferences.getDataLogin(this)) {
            if (preferences.getDataAs(this).equals("admin")) {
                startActivity(new Intent(LoginActivity.this, AdminRentalMobil.class));
                finish();
            } else if (preferences.getDataAs(this).equals("user")) {
                startActivity(new Intent(LoginActivity.this, UserRentalMobil.class));
                finish();
//                Cara logout kembali ke activity login
//                startActivity(new Intent(---Darisini---.this, LoginActivity.class));
//                preferences.clearData(this);
//                finish();
            }
        }
    }
}