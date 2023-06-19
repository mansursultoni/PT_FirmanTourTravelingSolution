package com.firmantour.travelingsolution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataUser extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    AdapterUser adapterUser;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<ModelUser> listUser;
    RecyclerView RView;
    ImageView imageView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_user);

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

        RView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager userLayout = new LinearLayoutManager(this);
        RView.setLayoutManager(userLayout);
        RView.setItemAnimator(new DefaultItemAnimator());

        TampilDataUser();
    }

    private void TampilDataUser() {
        database.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUser = new ArrayList<>();
                for (DataSnapshot item : snapshot.getChildren()){
                    ModelUser user = item.getValue(ModelUser.class);
                    user.setNomor(item.getKey());
                    listUser.add(user);
                }
                adapterUser = new AdapterUser(listUser, DataUser.this);
                RView.setAdapter(adapterUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                startActivity(new Intent(DataUser.this, AdminRentalMobil.class));
                finish();
                return true;
            case R.id.paketwisata:
                startActivity(new Intent(DataUser.this, AdminPaketWisata.class));
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
                startActivity(new Intent(DataUser.this, DataUser.class));
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