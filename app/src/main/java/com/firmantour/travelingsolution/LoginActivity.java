package com.firmantour.travelingsolution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PostProcessor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText ETNomor, ETPassword;
    TextView Tv1, Tv2, Tv3;
    Button TombolMasuk, TombolDaftar, TombolTambah;
    CheckBox checkBox;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        ETNomor = findViewById(R.id.etNomor);
        ETPassword = findViewById(R.id.etPassword);

        Tv1 = findViewById(R.id.tv1);
        Tv2 = findViewById(R.id.tv2);
        Tv3 = findViewById(R.id.tv3);
        String text2 = "1";
        Tv2.setText(text2);


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

        //Menampilkan total database
        databaseReference.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String data = String.valueOf(snapshot.getChildrenCount());
                    Tv1.setText(data);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Tv1.setText("0");
            }
        });

        TombolTambah = findViewById(R.id.btnTambah);
        TombolTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String txtnama = ETNomor.getText().toString();
//                String txt1 = Tv1.getText().toString();
//                int txt2 = 1;
//                if (!txtnama.isEmpty()){
//                    Tv1.setText(txt1);
//                    int pertama = Integer.parseInt(ETNomor.getText().toString());
//                    int tambah = pertama + txt2;
//                    String text = String.valueOf(tambah);
//                    Tv3.setText(text);
//                    return;
//                }

                String textNomor = ETNomor.getText().toString();
                if (!textNomor.isEmpty()){
                    int text1 = Integer.parseInt(Tv1.getText().toString());
                    int text2 = Integer.parseInt(Tv2.getText().toString());
                    int total = text1 + text2;
                    String tambah = String.valueOf(total);
                    Tv3.setText(tambah);
                }
            }
        });

        TombolDaftar = findViewById(R.id.btnDaftar);
        TombolDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, DaftarActivity.class));
                finish();
            }
        });

        TombolMasuk = findViewById(R.id.btnLogin);
        TombolMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txtnomor = ETNomor.getText().toString();
                String txtpassword = ETPassword.getText().toString();

                if (txtnomor.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Masukkan Nomor Telepon.", Toast.LENGTH_SHORT).show();
                } else if (txtpassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Masukkan Password.", Toast.LENGTH_SHORT).show();
                } else {
                    //langkah pertama cek admin
                    databaseReference.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(txtnomor)) {
                                final String getPassword = snapshot.child(txtnomor).child("password").getValue(String.class);
                                if (getPassword.equals(txtpassword)) {
                                    Toast.makeText(LoginActivity.this, "Login Berhasil.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, Dashboard.class));
                                    finish();
                                   return;
                                }
                                return;
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    //langkah kedua cek user
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