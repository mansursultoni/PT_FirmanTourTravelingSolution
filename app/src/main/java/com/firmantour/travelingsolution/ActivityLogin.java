package com.firmantour.travelingsolution;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityLogin extends AppCompatActivity {

    EditText EtNomor, EtPassword;
    Button TombolMasuk, TombolDaftar;
    CheckBox checkBox;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        //--Inisialisasi--
        EtNomor = findViewById(R.id.etNomorRekening);
        EtPassword = findViewById(R.id.etAN);
        checkBox = findViewById(R.id.ingatsaya);

        setCheckBox();

        //--TombolDaftar--
        TombolDaftar = findViewById(R.id.btnDaftar);
        TombolDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityLogin.this, UserDaftar.class));
            }
        });


        //--TombolMasuk--
        TombolMasuk = findViewById(R.id.btnLogin);
        TombolMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtnomor = EtNomor.getText().toString();
                String txtpassword = EtPassword.getText().toString();
                if (txtnomor.isEmpty()) {
                    Toast.makeText(ActivityLogin.this, "Masukkan Nomor Telepon.", Toast.LENGTH_SHORT).show();
                } else if (txtpassword.isEmpty()) {
                    Toast.makeText(ActivityLogin.this, "Masukkan Password.", Toast.LENGTH_SHORT).show();
                } else {
                    setLogin();
                }

            }
        });

    }
    private void setCheckBox(){
        String nomor = EtNomor.getText().toString();
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox = preferences.getString("ingat","");
        if (checkbox.equals("true")){
            Intent intent = new Intent(ActivityLogin.this, UserRentalMobil.class);
            intent.putExtra("nomortelpon", nomor);
            startActivity(intent);
        }else if (checkbox.equals("false")){

        }
    }
    private void setLogin(){
        String nomor = EtNomor.getText().toString();
        databaseReference.child("Login").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String input1 = EtNomor.getText().toString();
                String input2 = EtPassword.getText().toString();

                if (dataSnapshot.child(input1).exists()) {
                    if (dataSnapshot.child(input1).child("password").getValue(String.class).equals(input2)) {
                        if (checkBox.isChecked()) {
                            if (dataSnapshot.child(input1).child("sebagai").getValue(String.class).equals("admin")) {
                                LoginSesson.setDataLogin(ActivityLogin.this, true);
                                LoginSesson.setDataAs(ActivityLogin.this, "admin");
                                Toast.makeText(ActivityLogin.this, "Login Berhasil.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ActivityLogin.this, AdminDashboard2.class);
                                intent.putExtra("nomortelepon", nomor);
                                startActivity(intent);
                                finish();
                            } else if (dataSnapshot.child(input1).child("sebagai").getValue(String.class).equals("user")){
                                LoginSesson.setDataLogin(ActivityLogin.this, true);
                                LoginSesson.setDataAs(ActivityLogin.this, "user");
                                Toast.makeText(ActivityLogin.this, "Login Berhasil.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ActivityLogin.this, UserRentalMobil.class);
                                intent.putExtra("nomortelepon", nomor);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            if (dataSnapshot.child(input1).child("sebagai").getValue(String.class).equals("admin")) {
                                LoginSesson.setDataLogin(ActivityLogin.this, false);
                                Toast.makeText(ActivityLogin.this, "Login Berhasil.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ActivityLogin.this, AdminDashboard2.class);
                                intent.putExtra("nomortelepon", nomor);
                                startActivity(intent);
                                finish();

                            } else if (dataSnapshot.child(input1).child("sebagai").getValue(String.class).equals("user")){
                                LoginSesson.setDataLogin(ActivityLogin.this, false);
                                Toast.makeText(ActivityLogin.this, "Login Berhasil.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ActivityLogin.this, UserRentalMobil.class);
                                intent.putExtra("nomortelepon", nomor);
                                startActivity(intent);
                                finish();
                            }
                        }

                    } else {
                        Toast.makeText(ActivityLogin.this, "Kata sandi salah", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ActivityLogin.this, "Data belum terdaftar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (LoginSesson.getDataLogin(this)) {
            if (LoginSesson.getDataAs(this).equals("admin")) {
                startActivity(new Intent(this, AdminDashboard.class));
                finish();
            } else {
                startActivity(new Intent(this, UserRentalMobil.class));
                finish();
            }
        }
    }
}