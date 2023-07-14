package com.firmantour.travelingsolution;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TextView JumlahMobil, JumlahUser, JumlahMobilSewa, PermintaanPesan;
    ImageView imageView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseFirestore firebaseFirestore =FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admindashboard);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));


        imageView = findViewById(R.id.ib_menuDrawer);
        JumlahMobil = findViewById(R.id.tv_jumlahmobil);
        JumlahUser = findViewById(R.id.tv_jumlahuser);
        JumlahMobilSewa = findViewById(R.id.tv_mobildisewa);
        PermintaanPesan = findViewById(R.id.tv_permintaanpesan);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        getJumlahMobil();
        getJumlahUser();
        getJumlahPemesanan();

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.dashboard:
//                startActivity(new Intent(Dashboard.this, AdminRentalMobil.class));
//                finish();
                return true;
            case R.id.rentalmobil:
                startActivity(new Intent(AdminDashboard.this, AdminRentalMobil.class));
                finish();
                return true;
            case R.id.mobildisewa:

                return true;
            case R.id.menunggukonfirmasi:
                startActivity(new Intent(AdminDashboard.this, MenungguKonfirmasi.class));
                finish();
                return true;
            case R.id.dataadmin:
//                startActivity(new Intent(Dashboard.this, DataAdmin.class));
//                finish();
                return true;
            case R.id.datauser:
                startActivity(new Intent(AdminDashboard.this, AdminDataUser.class));
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminDashboard.this);
                alertDialog.setTitle("Keluar");
                alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Logout();
                        startActivity(new Intent(AdminDashboard.this, ActivityLogin.class));
                        finish();
                    }
                });
                alertDialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
                return true;
            default:
                return false;
        }
    }
    private void Logout(){
        LoginSesson.clearData(this);
    }
    private void getJumlahMobil(){
        CollectionReference query = firebaseFirestore.collection("RentalMobil");
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Count fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    JumlahMobil.setText(String.valueOf(snapshot.getCount()));
                } else {
                }
            }
        });
    }
    private void getJumlahUser(){
        CollectionReference query = firebaseFirestore.collection("users");
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Count fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    JumlahUser.setText(String.valueOf(snapshot.getCount()));
                } else {

                }
            }
        });
    }
    private void getJumlahPemesanan(){
        CollectionReference query = firebaseFirestore.collection("PesananBelumSelesai");
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Count fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    PermintaanPesan.setText(String.valueOf(snapshot.getCount()));
                } else {
                }
            }
        });
    }
}