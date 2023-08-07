package com.firmantour.travelingsolution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.firmantour.travelingsolution.databinding.ActivityAdminUbahRekeningBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AdminUbahRekening extends AppCompatActivity {

    private ActivityAdminUbahRekeningBinding binding;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DatabaseReference databaseRef;
    private String NomorRekening;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminUbahRekeningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        readData();

        binding.imgKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.etBank.getText().toString().isEmpty() ||
                        binding.etNomorRekening.getText().toString().isEmpty() ||
                        binding.etAN.getText().toString().isEmpty() ||
                        binding.etTeleponRekening.getText().toString().isEmpty()){
                    Toast.makeText(AdminUbahRekening.this, "Data harus lengkap.", Toast.LENGTH_SHORT).show();
                }else {
                    saveData();
                }
            }
        });

    }
    private void readData() {
        Intent intent = getIntent();
        if (intent != null) {
            String namabank = intent.getStringExtra("REK_BANK");
            String nomorrekening = intent.getStringExtra("REK_NO");
            String atasnama = intent.getStringExtra("REK_AN");
            String teleponrekening = intent.getStringExtra("REK_TELP");

            NomorRekening = nomorrekening;
            binding.etBank.setText(namabank);
            binding.etNomorRekening.setText(nomorrekening);
            binding.etAN.setText(atasnama);
            binding.etTeleponRekening.setText(teleponrekening);
        }
    }

    private void simpanDataFirestore() {
        final String namabank = binding.etBank.getText().toString();
        final String nomorrekening = binding.etNomorRekening.getText().toString();
        final String atasnama = binding.etAN.getText().toString();
        final String teleponrekening = binding.etTeleponRekening.getText().toString();

        // Buat referensi ke document yang akan diubah namanya
        DocumentReference adminDocRef = firebaseFirestore.collection("AkunBank").document(nomorrekening);

        // Ubah nama document dengan nama baru yang diinputkan
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("namabank", namabank);
        updateData.put("nomorrekening", nomorrekening);
        updateData.put("atasnama", atasnama);
        updateData.put("teleponrekening", teleponrekening);
        adminDocRef.set(updateData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        } else {
                        }
                    }
                });
    }

    private void hapusDataFireStore(){
        final String nomorrekening = NomorRekening;
        DocumentReference docRef = firebaseFirestore.collection("AkunBank").document(nomorrekening);
        docRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("AdminUbahRekening", "Hapus data firestore berhasil");
                } else {
                    Log.d("AdminUbahRekening", "Hapus data firestore gagal");
                }
            }
        });
    }

    private void saveData(){
        String nomorrekening = NomorRekening;
        firebaseFirestore.collection("AkunBank")
                .whereEqualTo("nomorrekening", nomorrekening)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null && !task.getResult().isEmpty()) {
                                // Data ditemukan di Firestore
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminUbahRekening.this);
                                builder.setTitle("Konfirmasi Perubahan");
                                builder.setMessage("Nama Bank:" +
                                        "\n" + binding.etBank.getText().toString() +
                                        "\n\n" + "Nomor Rekening :" +
                                        "\n" + binding.etNomorRekening.getText().toString() +
                                        "\n\n" + "Atas Nama :" +
                                        "\n" + binding.etAN.getText().toString() +
                                        "\n\n" + "Telepon Rekening :" +
                                        "\n" + binding.etTeleponRekening.getText().toString()
                                );
                                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        hapusDataFireStore();
                                        simpanDataFirestore();
                                        Toast.makeText(AdminUbahRekening.this, "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            } else {
                                // Data tidak ditemukan di Firestore
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminUbahRekening.this);
                                builder.setTitle("Konfirmasi Perubahan");
                                builder.setMessage("Nama Bank:" +
                                        "\n" + binding.etBank.getText().toString() +
                                        "\n\n" + "Nomor Rekening :" +
                                        "\n" + binding.etNomorRekening.getText().toString() +
                                        "\n\n" + "Atas Nama :" +
                                        "\n" + binding.etAN.getText().toString() +
                                        "\n\n" + "Telepon Rekening :" +
                                        "\n" + binding.etTeleponRekening.getText().toString()
                                );
                                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        simpanDataFirestore();
                                        Toast.makeText(AdminUbahRekening.this, "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                        } else {
                            // Terjadi kesalahan saat mengecek data di Firestore
                            Toast.makeText(AdminUbahRekening.this, "Error.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}