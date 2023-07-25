package com.firmantour.travelingsolution;

import static com.firmantour.travelingsolution.R.drawable.ic_user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firmantour.travelingsolution.databinding.ActivityUserPaketWisataBinding;
import com.firmantour.travelingsolution.model.ModelWisata;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class UserPaketWisata extends AppCompatActivity {

    private ActivityUserPaketWisataBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserPaketWisataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        firebaseFirestore = FirebaseFirestore.getInstance();

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        ambilDataIntent();
        getData();

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String telepon = binding.tvNomorTelpon.getText().toString();
                switch (item.getItemId()) {
                    case R.id.rentalMobil:
                            Intent intent = new Intent(UserPaketWisata.this, UserRentalMobil.class);
                            intent.putExtra("nomortelepon", telepon);
                            startActivity(intent);
                        return true;
                    case R.id.paketWisata:
                        /*Intent intent2 = new Intent(UserPaketWisata.this, UserPaketWisata.class);
                        intent2.putExtra("nomortelepon", telepon);
                        startActivity(intent2)*/;
                        return true;
                    case R.id.pemesanan:
                        Intent intent3 = new Intent(UserPaketWisata.this, UserPesananBelumSelesai.class);
                        intent3.putExtra("nomortelepon", telepon);
                        startActivity(intent3);
                        return true;
                    case R.id.pengaturan:
                        Intent intent4 = new Intent(UserPaketWisata.this, UserPengaturan.class);
                        intent4.putExtra("nomortelepon", telepon);
                        startActivity(intent4);
                        return true;
                    default:
                        return false;
                }
            }
        });
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
                        Intent intent = new Intent(UserPaketWisata.this, AdminDetailWisata.class);
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
        binding.recyclerView.setAdapter(adapter);
    }
    private class ProdukHolder extends RecyclerView.ViewHolder {
        ImageView fotoProduk;
        TextView namaProduk, hargaProduk;

        public ProdukHolder(@NonNull View itemView) {
            super(itemView);
            fotoProduk = itemView.findViewById(R.id.imageViewFoto);
            namaProduk = itemView.findViewById(R.id.tvNama);
            hargaProduk = itemView.findViewById(R.id.textViewHarga);
        }
    }
    private void ambilDataIntent(){
        Intent intent = getIntent();
        String nomorTelpon = intent.getStringExtra("nomortelepon");
        binding.tvNomorTelpon.setText(nomorTelpon);
    }
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserPaketWisata.this);
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
}