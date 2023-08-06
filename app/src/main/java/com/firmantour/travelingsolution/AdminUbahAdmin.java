package com.firmantour.travelingsolution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firmantour.travelingsolution.databinding.ActivityAdminUbahAdminBinding;
import com.firmantour.travelingsolution.model.Admin;
import com.firmantour.travelingsolution.model.ModelAdmin;
import com.firmantour.travelingsolution.model.ModelUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;

import java.util.HashMap;
import java.util.Map;

public class AdminUbahAdmin extends AppCompatActivity {

    private ActivityAdminUbahAdminBinding binding;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DatabaseReference databaseRef;
    private String Nomor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminUbahAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminUbahAdmin.this);
                builder.setTitle("Konfirmasi Perubahan");
                builder.setMessage("Nama :" +
                        "\n" + binding.etNama.getText().toString() +
                        "\n\n" + "Nomor Telepon :" +
                        "\n" + binding.etNomor.getText().toString() +
                        "\n\n" + "Password :" +
                        "\n" + binding.etPassword.getText().toString() +
                        "\n\n" + "Login Kembali ?"
                );
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hapusDataFireStore();
                        hapusDataRealtime();
                        simpanDataFirestore();
                        simpanDataRealtime();
                        Toast.makeText(AdminUbahAdmin.this, "Data Berhasil Diubah", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminUbahAdmin.this, ActivityLogin.class));
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
        });
    }

    private void readData() {
        Intent intent = getIntent();
        if (intent != null) {
            String namaAdmin = intent.getStringExtra("ADMIN_NAMA");
            String nomorAdmin = intent.getStringExtra("ADMIN_NOMOR");
            String passwordAdmin = intent.getStringExtra("ADMIN_PASSWORD");

            Nomor = nomorAdmin;
            binding.etNama.setText(namaAdmin);
            binding.etNomor.setText(nomorAdmin);
            binding.etPassword.setText(passwordAdmin);
        }
    }

    private void updateData() {
        final String nomorTelepon = binding.etNomor.getText().toString();
        final String nama = binding.etNama.getText().toString();
        final String password = binding.etPassword.getText().toString();

        // Jika nomor telepon tidak diubah
        if (nomorTelepon == Nomor) {

            // Update Data Firestore
            DocumentReference userDocRef = firebaseFirestore.collection("Users").document(nomorTelepon);
            userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Dokumen ditemukan, lakukan pembaruan
                            userDocRef.update("nama", nama, "password", password)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Tindakan yang dijalankan jika pembaruan berhasil
                                            } else {
                                                // Tindakan yang dijalankan jika pembaruan gagal
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(AdminUbahAdmin.this, "", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Tindakan yang dijalankan jika query gagal
                    }
                }
            });
            // Update Data Realtime
            databaseRef = FirebaseDatabase.getInstance().getReference("Login");
            DatabaseReference adminRef = databaseRef.child(nomorTelepon);
            adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        adminRef.child("nama").setValue(nama);
                        adminRef.child("password").setValue(password)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Tindakan yang dijalankan jika pembaruan berhasil
                                        } else {
                                            // Tindakan yang dijalankan jika pembaruan gagal
                                        }
                                    }
                                });
                    } else {
                        // Data dengan nomor telepon tersebut tidak ditemukan
                        // Lakukan tindakan yang sesuai, misalnya, menampilkan pesan error
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Tindakan yang dijalankan jika terjadi kesalahan dalam membaca data
                }
            });

        } else { // Jika Nomor Telepon diubah

            // Hapus data lama pada Firestore
            DocumentReference docRef = firebaseFirestore.collection("Users").document(nomorTelepon);
            docRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Tindakan yang dijalankan jika penghapusan berhasil
                    } else {
                        // Tindakan yang dijalankan jika penghapusan gagal
                    }
                }
            });


            // Hapus data lama pada Realtime
            DatabaseReference deleteAdmin = databaseRef.child(nomorTelepon);
            deleteAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Data dengan nomor telepon tersebut ditemukan, maka hapus data
                        deleteAdmin.removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Tindakan yang dijalankan jika penghapusan berhasil
                                        } else {
                                            // Tindakan yang dijalankan jika penghapusan gagal
                                        }
                                    }
                                });
                    } else {
                        // Data dengan nomor telepon tersebut tidak ditemukan
                        // Lakukan tindakan yang sesuai, misalnya, menampilkan pesan error
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Tindakan yang dijalankan jika terjadi kesalahan dalam membaca data
                }
            });


            // Simpan data pada Firestore
            firebaseFirestore.collection("Users").document(nomorTelepon).set(new Admin(nama, nomorTelepon, password))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Tindakan yang dijalankan jika penyimpanan berhasil
                            } else {
                                // Tindakan yang dijalankan jika penyimpanan gagal
                            }
                        }
                    });

            // Simpan data pada Realtime
            DatabaseReference saveAdmin = databaseRef.child(nomorTelepon);
            String sebagai = "admin";
            // Simpan data ke Firebase Realtime Database
            saveAdmin.child("nama").setValue(nama);
            saveAdmin.child("nomortelepon").setValue(nomorTelepon);
            saveAdmin.child("password").setValue(password);
            saveAdmin.child("sebagai").setValue(sebagai)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Tindakan yang dijalankan jika penyimpanan berhasil
                            } else {
                                // Tindakan yang dijalankan jika penyimpanan gagal
                            }
                        }
                    });

        }


    }

    private void simpanDataFirestore() {
        final String nama = binding.etNama.getText().toString();
        final String nomor = binding.etNomor.getText().toString();
        final String password = binding.etPassword.getText().toString();

        // Buat referensi ke document yang akan diubah namanya
        DocumentReference adminDocRef = firebaseFirestore.collection("Admin").document(nomor);

        // Ubah nama document dengan nama baru yang diinputkan
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("namaadmin", nama);
        updateData.put("nomortelepon", nomor);
        updateData.put("password", password);
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
        final String nomor = Nomor;
        DocumentReference docRef = firebaseFirestore.collection("Admin").document(nomor);
        docRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Tindakan yang dijalankan jika penghapusan berhasil
                } else {
                    // Tindakan yang dijalankan jika penghapusan gagal
                }
            }
        });
    }

    private void simpanDataRealtime(){
        final String nama = binding.etNama.getText().toString();
        final String nomor = binding.etNomor.getText().toString();
        final String password = binding.etPassword.getText().toString();
        final String sebagai = "admin";

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Login").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                        databaseReference.child("Login").child(nomor).setValue(new ModelAdmin(nama, nomor, password, sebagai)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void hapusDataRealtime(){
        final String nomor = Nomor;
        databaseRef = FirebaseDatabase.getInstance().getReference("Login");
        DatabaseReference deleteAdmin = databaseRef.child(nomor);
        deleteAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Data dengan nomor telepon tersebut ditemukan, maka hapus data
                    deleteAdmin.removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Tindakan yang dijalankan jika penghapusan berhasil
                                    } else {
                                        // Tindakan yang dijalankan jika penghapusan gagal
                                    }
                                }
                            });
                } else {
                    // Data dengan nomor telepon tersebut tidak ditemukan
                    // Lakukan tindakan yang sesuai, misalnya, menampilkan pesan error
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Tindakan yang dijalankan jika terjadi kesalahan dalam membaca data
            }
        });
    }
}