package com.firmantour.travelingsolution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import static com.firmantour.travelingsolution.R.drawable.ic_user;

public class AdminRentalMobil extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    RecyclerView recyclerView;
    ImageView imageView, btnTambah;
    DrawerLayout drawerLayout;
    NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_rental_mobil);


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

        btnTambah = findViewById(R.id.btn_tambah);
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminRentalMobil.this, TambahRentalMobil.class));
            }
        });

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = findViewById(R.id.recyclerView);
        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        getData();
    }

    private void getData() {
        Query query = firebaseFirestore.collection("RentalMobil");
        FirestoreRecyclerOptions<ClassProduk> response = new FirestoreRecyclerOptions.Builder<ClassProduk>()
                .setQuery(query, ClassProduk.class).build();
        adapter = new FirestoreRecyclerAdapter<ClassProduk, ProdukHolder>(response) {
            @NonNull
            @Override
            public ProdukHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produk, parent, false);
                return new ProdukHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProdukHolder holder, int position, @NonNull final ClassProduk model) {
                if (model.getFoto() != null) {
                    Picasso.get().load(model.getFoto()).fit().into(holder.fotoProduk);
                } else {
                    Picasso.get().load(ic_user).fit().into(holder.fotoProduk);
                }
                holder.namaProduk.setText(model.getNama());
                holder.hargaProduk.setText(model.getHarga());
                holder.statusProduk.setText(model.getStatus());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AdminRentalMobil.this, AdminDetailRentalMobil.class);
                        intent.putExtra("nomor", model.getNomor());
                        startActivity(intent);
//Snackbar.make(recyclerView, model.getNama()+", " +model.getTelepon(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
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

    public class ProdukHolder extends RecyclerView.ViewHolder {
        ImageView fotoProduk;
        TextView namaProduk, hargaProduk, statusProduk;
        ConstraintLayout constraintLayout;

        public ProdukHolder(@NonNull View itemView) {
            super(itemView);
            fotoProduk = itemView.findViewById(R.id.imageViewFoto);
            namaProduk = itemView.findViewById(R.id.textViewNama);
            hargaProduk = itemView.findViewById(R.id.textViewHarga);
            statusProduk = itemView.findViewById(R.id.textStatus);
//            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminRentalMobil.this);
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
            case R.id.item1:
                startActivity(new Intent(AdminRentalMobil.this, Dashboard.class));
                finish();
                return true;
            case R.id.item2:

                return true;
            default:
                return false;
        }
    }
}