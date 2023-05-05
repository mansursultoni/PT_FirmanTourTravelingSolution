package com.firmantour.travelingsolution;

import static com.firmantour.travelingsolution.R.drawable.ic_user;
import static com.firmantour.travelingsolution.R.drawable.rounded_background_white;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class AdminPaketWisata extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_paket_wisata);



        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));


        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        firebaseFirestore = FirebaseFirestore.getInstance();

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        getData();




        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminPaketWisata.this, TambahPaketWisata.class));
            }
        });

    }

    private void getData() {
        Query query = firebaseFirestore.collection("PaketWisata");
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
                progressBar.setVisibility(View.GONE);
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
                        Intent intent = new Intent(AdminPaketWisata.this, AdminDetailPaketWisata.class);
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
            fotoProduk  = itemView.findViewById(R.id.imageViewFoto);
            namaProduk  = itemView.findViewById(R.id.textViewNama);
            hargaProduk = itemView.findViewById(R.id.textViewHarga);
            statusProduk= itemView.findViewById(R.id.textStatus);



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

    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_item);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                startActivity(new Intent(AdminPaketWisata.this, AdminRentalMobil.class));
                finish();
                return true;
            case R.id.item2:
                closeOptionsMenu();
                return true;
            default:
                return false;
        }
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
}