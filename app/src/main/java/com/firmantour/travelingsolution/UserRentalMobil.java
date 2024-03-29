package com.firmantour.travelingsolution;

import static com.firmantour.travelingsolution.R.drawable.ic_user;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firmantour.travelingsolution.model.ModelMobil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class UserRentalMobil extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    RecyclerView recyclerView;
    ProgressBar progressBar;
    BottomNavigationView navigationView;
    TextView TvNomorTelpon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userentalmobil);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));


        TvNomorTelpon = findViewById(R.id.tv_nomorTelpon);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        firebaseFirestore = FirebaseFirestore.getInstance();

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        ambilDataIntent();
        //getUser();
        getData();


        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String telepon = TvNomorTelpon.getText().toString();
                switch (item.getItemId()) {
                        case R.id.rentalMobil:
                            /*Intent intent = new Intent(UserRentalMobil.this, UserRentalMobil.class);
                            intent.putExtra("nomortelepon", telepon);
                            startActivity(intent);*/
                            return true;
                        case R.id.pemesanan:
                            Intent intent3 = new Intent(UserRentalMobil.this, UserPesananBelumSelesai.class);
                            intent3.putExtra("nomortelepon", telepon);
                            startActivity(intent3);
                            finish();
                            return true;
                        case R.id.pengaturan:
                            Intent intent4 = new Intent(UserRentalMobil.this, UserPengaturan.class);
                            intent4.putExtra("nomortelepon", telepon);
                            startActivity(intent4);
                            return true;
                        default:
                            return false;
                    }
            }
        });
    }

    private void ambilDataIntent(){
        Intent intent = getIntent();
        String nomorTelpon = intent.getStringExtra("nomortelepon");
        TvNomorTelpon.setText(nomorTelpon);
    }
    private void Logout(){
        LoginSesson.clearData(this);
    }
    private void getData() {
        String input1 = TvNomorTelpon.getText().toString();
        Query query = firebaseFirestore.collection("RentalMobil");
        FirestoreRecyclerOptions<ModelMobil> response = new FirestoreRecyclerOptions.Builder<ModelMobil>()
                .setQuery(query, ModelMobil.class).build();
        adapter = new FirestoreRecyclerAdapter<ModelMobil, ProdukHolder>(response) {
            @NonNull
            @Override
            public ProdukHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mobil, parent, false);
                return new ProdukHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProdukHolder holder, int position, @NonNull final ModelMobil model) {
                progressBar.setVisibility(View.GONE);
                if (model.getFoto() != null) {
                    Picasso.get().load(model.getFoto()).fit().into(holder.fotoProduk);
                } else {
                    Picasso.get().load(ic_user).fit().into(holder.fotoProduk);
                }
                holder.namaProduk.setText(model.getNamamobil());
                holder.hargaProduk.setText(model.getHarga());
                holder.statusProduk.setText(model.getStatus());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserRentalMobil.this, UserDetaillMobil.class);
                        intent.putExtra("nomor", model.getPlatnomor());
                        intent.putExtra("nomortelepon", input1);
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
            namaProduk = itemView.findViewById(R.id.tvNama);
            hargaProduk = itemView.findViewById(R.id.textViewHarga);
            statusProduk = itemView.findViewById(R.id.textStatus);
//            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserRentalMobil.this);
        alertDialog.setTitle("Keluar");
        alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Logout();
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