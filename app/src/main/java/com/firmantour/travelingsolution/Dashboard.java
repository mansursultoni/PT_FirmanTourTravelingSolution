package com.firmantour.travelingsolution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TextView JumlahMobil, JumlahUser;
    ImageView imageView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));


        imageView = findViewById(R.id.imageButton);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // --- To open Drawer ---
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        JumlahMobil = findViewById(R.id.txt_jumlahMobil);
        JumlahUser = findViewById(R.id.txt_jumlahUser);

        //Menampilkan total user
        databaseReference.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String data = String.valueOf(snapshot.getChildrenCount());
                    JumlahUser.setText(data);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                JumlahUser.setText("0");
            }
        });
        //Menampilkan total mobil
        databaseReference.child("JumlahMobil").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String data = String.valueOf(snapshot.getChildrenCount());
                    JumlahMobil.setText(data);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                JumlahMobil.setText("0");
            }
        });





    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.dashboard:
//                startActivity(new Intent(Dashboard.this, AdminRentalMobil.class));
//                finish();
                return true;
            case R.id.rentalmobil:
                startActivity(new Intent(Dashboard.this, AdminRentalMobil.class));
                finish();
                return true;
            case R.id.paketwisata:
                startActivity(new Intent(Dashboard.this, AdminPaketWisata.class));
                finish();
                return true;
            case R.id.menunggukonfirmasi:
//                startActivity(new Intent(Dashboard.this, MenungguKonfirmasi.class));
//                finish();
                return true;
            case R.id.dataadmin:
//                startActivity(new Intent(Dashboard.this, DataAdmin.class));
//                finish();
                return true;
            case R.id.datauser:
                startActivity(new Intent(Dashboard.this, DataUser.class));
                finish();
                return true;
            case R.id.setting:
//                startActivity(new Intent(Dashboard.this, PengaturanAdmin.class));
//                finish();
                return true;
            case R.id.laporan:
//                startActivity(new Intent(Dashboard.this, Laporan.class));
//                finish();
                return true;
            case R.id.logout:

                return true;
            default:
                return false;
        }
    }

}