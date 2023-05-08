package com.firmantour.travelingsolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btadmin,btuser, btdaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btadmin = findViewById(R.id.btnAdmin);
        btadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AdminRentalMobil.class));
                finish();
            }
        });
        btuser  = findViewById(R.id.btnUser);
        btuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UserRentalMobil.class));
                finish();
            }
        });

        btdaftar = findViewById(R.id.btnDaftar);
        btdaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DaftarActivity.class));
                finish();
            }
        });

    }
}