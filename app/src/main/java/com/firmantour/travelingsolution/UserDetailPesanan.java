package com.firmantour.travelingsolution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.firmantour.travelingsolution.databinding.ActivityUserDetailPesananBinding;
import com.firmantour.travelingsolution.databinding.ActivityUserdetailmobilBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class UserDetailPesanan extends AppCompatActivity {

    private ActivityUserDetailPesananBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference docRef;
    private String fotoUrl, produkId, Status;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDetailPesananBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));


        firebaseFirestore   = FirebaseFirestore.getInstance();

        ambilDataIntent();
        readData();

        binding.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserDetailPesanan.this);
                alertDialog.setTitle("Batalkan Pesanan");
                alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        batalPesanan();
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
        });
    }
    private void batalPesanan(){
        docRef = firebaseFirestore.collection("Pemesanan").document(produkId);
        docRef.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Document deleted successfully
                            // Do something if needed
                        } else {
                            // An error occurred
                            // Handle the error
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the error
                    }
                });

    }
    private void ambilDataIntent(){
        Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        String status = intent.getStringExtra("status");
        binding.tvId.setText(key);
        produkId = key;
        Status = status;
    }
    private void readData() {
        if (Status.equals("Belum Selesai")){
            String key = binding.tvId.getText().toString();
            String status = Status;
            firebaseFirestore.collection("Pemesanan").whereEqualTo("key",key).whereEqualTo("statuspesanan",status)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    binding.tvNama.setText(document.getString("nama"));
                                    binding.tvTelepon.setText(document.getString("nomortelepon"));
                                    binding.tvAlamat.setText(document.getString("alamat"));
                                    binding.tvPlatNomor.setText(document.getString("platnomor"));
                                    binding.tvNamaMerk.setText(document.getString("namamerk"));
                                    binding.tvNamaMobil.setText(document.getString("namamobil"));
                                    binding.tvWarna.setText(document.getString("warna"));
                                    binding.tvKursi.setText(document.getString("jumlahkursi"));
                                    binding.tvTanggalSewa.setText(document.getString("tanggalsewa"));
                                    binding.tvTanggalKembali.setText(document.getString("tanggalkembali"));
                                    binding.tvTotalHarga.setText(document.getString("harga"));
                                    binding.tvWaktu.setText(document.getString("waktu"));
                                    fotoUrl = document.getString("foto");
                                    if (fotoUrl != "") {
                                        Picasso.get().load(fotoUrl).fit().into(binding.ivBuktipembayaran);
                                    } else {
                                        Picasso.get().load(R.drawable.ic_image).fit().into(binding.ivBuktipembayaran);
                                    }


                                }
                            } else {
                                Toast.makeText(UserDetailPesanan.this, "Gagal Mengambil Document", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
        } else if (Status.equals("Dibatalkan")){
            binding.btBatal.setVisibility(View.INVISIBLE);
            binding.btBatal.setEnabled(false);
            binding.textCatatan.setVisibility(View.VISIBLE);
            String key = binding.tvId.getText().toString();
            String status = Status;
            firebaseFirestore.collection("Pemesanan").whereEqualTo("key",key).whereEqualTo("statuspesanan",status)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    binding.tvNama.setText(document.getString("nama"));
                                    binding.tvTelepon.setText(document.getString("nomortelepon"));
                                    binding.tvAlamat.setText(document.getString("alamat"));
                                    binding.tvPlatNomor.setText(document.getString("platnomor"));
                                    binding.tvNamaMerk.setText(document.getString("namamerk"));
                                    binding.tvNamaMobil.setText(document.getString("namamobil"));
                                    binding.tvWarna.setText(document.getString("warna"));
                                    binding.tvKursi.setText(document.getString("jumlahkursi"));
                                    binding.tvTanggalSewa.setText(document.getString("tanggalsewa"));
                                    binding.tvTanggalKembali.setText(document.getString("tanggalkembali"));
                                    binding.tvTotalHarga.setText(document.getString("harga"));
                                    binding.tvWaktu.setText(document.getString("waktu"));
                                    binding.etCatatan.setText(document.getString("catatan"));
                                    fotoUrl = document.getString("foto");
                                    if (fotoUrl != "") {
                                        Picasso.get().load(fotoUrl).fit().into(binding.ivBuktipembayaran);
                                    } else {
                                        Picasso.get().load(R.drawable.ic_image).fit().into(binding.ivBuktipembayaran);
                                    }


                                }
                            } else {
                                Toast.makeText(UserDetailPesanan.this, "Gagal Mengambil Document", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
        }
        else {
            binding.btBatal.setVisibility(View.INVISIBLE);
            binding.btBatal.setEnabled(false);
            String key = binding.tvId.getText().toString();
            String status = Status;
            firebaseFirestore.collection("Pemesanan").whereEqualTo("key",key).whereEqualTo("statuspesanan",status)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    binding.tvNama.setText(document.getString("nama"));
                                    binding.tvTelepon.setText(document.getString("nomortelepon"));
                                    binding.tvAlamat.setText(document.getString("alamat"));
                                    binding.tvPlatNomor.setText(document.getString("platnomor"));
                                    binding.tvNamaMerk.setText(document.getString("namamerk"));
                                    binding.tvNamaMobil.setText(document.getString("namamobil"));
                                    binding.tvWarna.setText(document.getString("warna"));
                                    binding.tvKursi.setText(document.getString("jumlahkursi"));
                                    binding.tvTanggalSewa.setText(document.getString("tanggalsewa"));
                                    binding.tvTanggalKembali.setText(document.getString("tanggalkembali"));
                                    binding.tvTotalHarga.setText(document.getString("harga"));
                                    binding.tvWaktu.setText(document.getString("waktu"));
                                    fotoUrl = document.getString("foto");
                                    if (fotoUrl != "") {
                                        Picasso.get().load(fotoUrl).fit().into(binding.ivBuktipembayaran);
                                    } else {
                                        Picasso.get().load(R.drawable.ic_image).fit().into(binding.ivBuktipembayaran);
                                    }


                                }
                            } else {
                                Toast.makeText(UserDetailPesanan.this, "Gagal Mengambil Document", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });

        }

    }
}