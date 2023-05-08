package com.firmantour.travelingsolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.google.android.material.textfield.TextInputEditText;

public class DaftarActivity extends AppCompatActivity {

    TextInputEditText nama, nomor, tanggalLahir, jenisKelamin, alamat, password;
    ImageButton kembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        kembali = findViewById(R.id.imgKembali);
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DaftarActivity.this,LoginActivity.class));
                finish();
            }
        });

    }
}