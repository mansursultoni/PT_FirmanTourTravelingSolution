package com.firmantour.travelingsolution;

import static com.firmantour.travelingsolution.R.drawable.ic_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdminPaketWisata extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    private Adapter adapterMobil;
    private List<ModelUser> list = new ArrayList<>();
    RecyclerView recyclerView;
    ImageView imageView, btnTambah;
    ImageButton IbMenu;
    DrawerLayout drawerLayout;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_paket_wisata);


        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));


        IbMenu = findViewById(R.id.ibMenuDrawer);
        btnTambah = findViewById(R.id.btn_tambah);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationDrawer);
        recyclerView = findViewById(R.id.recycler_view);


        IbMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // --- To open Drawer ---
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminPaketWisata.this, AdminTambahWisata.class));
            }
        });

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        firebaseFirestore = FirebaseFirestore.getInstance();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapterMobil);

    }
    private void getData() {
        Query query = firebaseFirestore.collection("PaketWisata");
        FirestoreRecyclerOptions<ModelWisata> response = new FirestoreRecyclerOptions.Builder<ModelWisata>()
                .setQuery(query, ModelWisata.class).build();
        adapter = new FirestoreRecyclerAdapter<ModelWisata, ProdukHolder>(response) {
            @NonNull
            @Override
            public ProdukHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wisata, parent, false);
                return new ProdukHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProdukHolder holder, int position, @NonNull final ModelWisata model) {
                if (model.getFoto() != null) {
                    Picasso.get().load(model.getFoto()).fit().into(holder.fotoProduk);
                } else {
                    Picasso.get().load(ic_user).fit().into(holder.fotoProduk);
                }
                holder.namaProduk.setText(model.getNamawisata());
                holder.hargaProduk.setText(model.getHarga());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AdminPaketWisata.this, AdminDetailWisata.class);
                        intent.putExtra("nomor", model.getKodewisata());
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
        recyclerView.setAdapter(adapter);
    }

    private class ProdukHolder extends RecyclerView.ViewHolder {
        ImageView fotoProduk;
        TextView namaProduk, hargaProduk;

        public ProdukHolder(@NonNull View itemView) {
            super(itemView);
            fotoProduk = itemView.findViewById(R.id.imageViewFoto);
            namaProduk = itemView.findViewById(R.id.textViewNama);
            hargaProduk = itemView.findViewById(R.id.textViewHarga);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        getData();
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
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminPaketWisata.this);
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
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.dashboard:
                startActivity(new Intent(AdminPaketWisata.this, AdminDashboard.class));
                finish();
                return true;
            case R.id.rentalmobil:
                startActivity(new Intent(AdminPaketWisata.this, AdminRentalMobil.class));
                finish();
                return true;
            case R.id.paketwisata:
//                startActivity(new Intent(AdminPaketWisata.this, AdminPaketWisata.class));
//                finish();
                return true;
            case R.id.mobildisewa:

                return true;
            case R.id.menunggukonfirmasi:
                startActivity(new Intent(AdminPaketWisata.this, MenungguKonfirmasi.class));
                finish();
                return true;
            case R.id.datauser:
                startActivity(new Intent(AdminPaketWisata.this, AdminDataUser.class));
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminPaketWisata.this);
                alertDialog.setTitle("Keluar");
                alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Logout();
                        startActivity(new Intent(AdminPaketWisata.this, ActivityLogin.class));
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

}