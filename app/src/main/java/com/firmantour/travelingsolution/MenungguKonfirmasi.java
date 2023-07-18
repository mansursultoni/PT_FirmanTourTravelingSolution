package com.firmantour.travelingsolution;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class MenungguKonfirmasi extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<ModelUser> list = new ArrayList<>();
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    androidx.recyclerview.widget.RecyclerView RecyclerView;
    ImageView imageView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menunggu_konfirmasi);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        imageView = findViewById(R.id.ibMenuDrawer);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationDrawer);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // --- To open Drawer ---
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        RecyclerView = findViewById(R.id.recycler_view);
        RecyclerView.setHasFixedSize(true);
        RecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    private class ProdukHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        TextView nama, nomor, key, namamobil, platnomor;

        public ProdukHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.textViewNama);
            nomor = itemView.findViewById(R.id.textViewNomor);
            key = itemView.findViewById(R.id.textViewKey);
            namamobil = itemView.findViewById(R.id.textVieNamaMobil);
            platnomor = itemView.findViewById(R.id.textViewPlat);
        }
    }
    private void getData(){
        Query query = db.collection("Pemesanan").whereEqualTo("statuspesanan","Belum Selesai");
        FirestoreRecyclerOptions<ModelPesanan> response = new FirestoreRecyclerOptions.Builder<ModelPesanan>()
                .setQuery(query, ModelPesanan.class).build();
        adapter = new FirestoreRecyclerAdapter<ModelPesanan, ProdukHolder>(response) {
            @NonNull
            @Override
            public ProdukHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pesanan, parent, false);
                return new ProdukHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProdukHolder holder, int position, @NonNull final ModelPesanan model) {
                holder.nama.setText(model.getNama());
                holder.nomor.setText(model.getNomortelepon());
                holder.key.setText(model.getKey());
                holder.namamobil.setText(model.getNamamobil());
                holder.platnomor.setText(model.getPlatnomor());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MenungguKonfirmasi.this, AdminDetailPesanan.class);
                        intent.putExtra("key", model.getKey());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                Log.e("Ditemukan Error: ", e.getMessage());
            }
        };
        adapter.notifyDataSetChanged();
        RecyclerView.setAdapter(adapter);
    }
    protected void onStart() {
        super.onStart();
        getData();
//        adapter.startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.dashboard:
                startActivity(new Intent(MenungguKonfirmasi.this, AdminDashboard.class));
                finish();
                return true;
            case R.id.rentalmobil:
                startActivity(new Intent(MenungguKonfirmasi.this, AdminRentalMobil.class));
                finish();
                return true;
            case R.id.paketwisata:
                startActivity(new Intent(MenungguKonfirmasi.this, AdminPaketWisata.class));
                finish();
                return true;
            case R.id.mobildisewa:

                return true;
            case R.id.menunggukonfirmasi:
//                startActivity(new Intent(Dashboard.this, MenungguKonfirmasi.class));
//                finish();
                return true;

            case R.id.datauser:
//                startActivity(new Intent(DataUser.this, DataUser.class));
//                finish();
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MenungguKonfirmasi.this);
                alertDialog.setTitle("Keluar");
                alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
}