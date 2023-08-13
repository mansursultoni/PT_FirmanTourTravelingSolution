package com.firmantour.travelingsolution;

import static com.firmantour.travelingsolution.R.drawable.gradient_blue;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firmantour.travelingsolution.adminfragment.ADashboard;
import com.firmantour.travelingsolution.databinding.ActivityUserPesananBelumSelesaiBinding;
import com.firmantour.travelingsolution.fragmentpesanan.PesananDibatalkan;
import com.firmantour.travelingsolution.fragmentpesanan.PesananDikonfirmasi;
import com.firmantour.travelingsolution.fragmentpesanan.PesananMenunggu;
import com.firmantour.travelingsolution.fragmentpesanan.PesananSelesai;
import com.firmantour.travelingsolution.model.ModelMobil;
import com.firmantour.travelingsolution.model.ModelPesanan;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class UserPesananBelumSelesai extends AppCompatActivity {

    private ActivityUserPesananBelumSelesaiBinding binding;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter adapter;
    TextView nomortelepon;
    String notlep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserPesananBelumSelesaiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        Fragment menunggu = new PesananMenunggu();
        replaceFragment(menunggu);

        nomortelepon = findViewById(R.id.tvNomorTelepon);


        //binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        ambilDataIntent();
        //getData();


        binding.btMenunggu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment menunggu = new PesananMenunggu();
                replaceFragment(menunggu);
            }
        });
        binding.btDikonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment dikonfirmasi = new PesananDikonfirmasi();
                replaceFragment(dikonfirmasi);
            }
        });
        binding.btSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment selesai = new PesananSelesai();
                replaceFragment(selesai);
            }
        });
        binding.btDibatalkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment dibatalkan = new PesananDibatalkan();
                replaceFragment(dibatalkan);
            }
        });

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String telepon = binding.tvNomorTelepon.getText().toString();
                switch (item.getItemId()) {
                    case R.id.rentalMobil:
                        Intent intent = new Intent(UserPesananBelumSelesai.this, UserRentalMobil.class);
                        intent.putExtra("nomortelepon", telepon);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.pemesanan:
                        /*Intent intent3 = new Intent(UserPesananBelumSelesai.this, UserPesananBelumSelesai.class);
                        intent3.putExtra("nomortelepon", telepon);
                        startActivity(intent3);*/
                        return true;
                    case R.id.pengaturan:
                        Intent intent4 = new Intent(UserPesananBelumSelesai.this, UserPengaturan.class);
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
        binding.tvNomorTelepon.setText(nomorTelpon);
        notlep = nomorTelpon;
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.recyclerFrame, fragment);
        fragmentTransaction.commit();
    }

    /*private void getData() {
        Query query = firebaseFirestore.collection("Pemesanan").whereEqualTo("nomortelepon", notlep)
                .whereEqualTo("statuspesanan","Belum Selesai");
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
                holder.nomortelepon.setText(model.getNomortelepon());
                holder.namamobil.setText(model.getNamamobil());
                holder.platnomor.setText(model.getPlatnomor());
                holder.tanggalsewa.setText(model.getTanggalsewa());
                holder.tanggalkembali.setText(model.getTanggalkembali());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserPesananBelumSelesai.this, UserDetailPesanan.class);
                        intent.putExtra("key", model.getKey());
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
        binding.recyclerView.setAdapter(adapter);
    }*/

    /*public class ProdukHolder extends RecyclerView.ViewHolder {
        TextView nama, nomortelepon, namamobil, platnomor, tanggalsewa, tanggalkembali;
        ConstraintLayout constraintLayout;

        public ProdukHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.tvNama);
            nomortelepon = itemView.findViewById(R.id.tvNomorTelepon);
            namamobil = itemView.findViewById(R.id.tvNamaMobil);
            platnomor = itemView.findViewById(R.id.tvPlatNomor);
            tanggalsewa = itemView.findViewById(R.id.tvTanggalSewa);
            tanggalkembali = itemView.findViewById(R.id.tvTanggalKembali);
        }

    }*/
    /*@Override
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
    }*/
    private void Logout(){
        LoginSesson.clearData(this);
    }
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserPesananBelumSelesai.this);
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

}