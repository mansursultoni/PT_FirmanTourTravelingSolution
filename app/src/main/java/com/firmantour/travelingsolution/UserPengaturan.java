package com.firmantour.travelingsolution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.firmantour.travelingsolution.databinding.ActivityUserPengaturanBinding;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class UserPengaturan extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private ActivityUserPengaturanBinding binding;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DatabaseReference databaseRef;
    String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$";
    Pattern pattern;
    private String Nomor;
    int tahun, bulan, tanggal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserPengaturanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.jeniskelamin,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        binding.spinnerJenisKelamin.setAdapter(adapter);
        binding.spinnerJenisKelamin.setOnItemSelectedListener(this);

        ambilDataIntent();
        readData();

        binding.ibEditCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                tahun = calendar.get(Calendar.YEAR);
                bulan = calendar.get(Calendar.MONTH);
                tanggal = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog;
                dialog = new DatePickerDialog(UserPengaturan.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tahun = year;
                        bulan = month+1;
                        tanggal = dayOfMonth;

                        binding.etTanggalLahir.setText(tanggal + " - " + bulan + " - " + tahun );
                    }
                },tahun, bulan, tanggal);
                dialog.show();
            }
        });
        binding.imgKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etNama.setEnabled(true);
                binding.etNomorTelepon.setEnabled(true);
                binding.etAlamat.setEnabled(true);
                binding.etPassword.setEnabled(true);
                binding.etPassword2.setEnabled(true);
                binding.etPassword2.setVisibility(View.VISIBLE);
                binding.btnUbah.setVisibility(View.INVISIBLE);
                binding.btnUbah.setEnabled(false);
                binding.btnSimpan.setVisibility(View.VISIBLE);
                binding.btnSimpan.setEnabled(true);
                binding.ibEditCalendar.setVisibility(View.VISIBLE);
                binding.spinnerJenisKelamin.setVisibility(View.VISIBLE);
                binding.spinnerJenisKelamin.setEnabled(true);
                binding.textinputPassword2.setVisibility(View.VISIBLE);
                binding.textinputPassword2.setEnabled(true);

            }
        });
        binding.btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.etNama.getText().toString().isEmpty() ||
                        binding.etNomorTelepon.getText().toString().isEmpty() ||
                        binding.etTanggalLahir.getText().toString().isEmpty() ||
                        binding.etJenisKelamin.getText().toString().isEmpty() ||
                        binding.etAlamat.getText().toString().isEmpty() ||
                        binding.etPassword.getText().toString().isEmpty()
                ){
                    Toast.makeText(UserPengaturan.this, "Data harus lengkap.", Toast.LENGTH_SHORT).show();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserPengaturan.this);
                    builder.setTitle("Ubah Data ?");
                    builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            hapusDataFireStore();
                            hapusDataRealtime();
                            simpanDataFirestore();
                            simpanDataRealtime();
                            Toast.makeText(UserPengaturan.this, "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UserPengaturan.this, ActivityLogin.class));
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
            }
        });
    }

    private void ambilDataIntent(){
        Intent intent = getIntent();
        String nomorTelpon = intent.getStringExtra("nomortelepon");
        binding.etNomorTelepon.setText(nomorTelpon);
        Nomor = nomorTelpon;
    }
    private void readData(){
        String nomor = binding.etNomorTelepon.getText().toString();
        firebaseFirestore.collection("Users").whereEqualTo("nomortelepon",nomor)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                binding.etNama.setText(document.getString("nama"));
                                binding.etNomorTelepon.setText(document.getString("nomortelepon"));
                                binding.etAlamat.setText(document.getString("alamat"));
                                binding.etJenisKelamin.setText(document.getString("jeniskelamin"));
                                binding.etTanggalLahir.setText(document.getString("tanggallahir"));
                                binding.etPassword.setText(document.getString("password"));
                            }
                        } else {
                            Toast.makeText(UserPengaturan.this, "Gagal Mengambil Document", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void simpanDataFirestore() {
        String nama = binding.etNama.getText().toString();
        String nomor = binding.etNomorTelepon.getText().toString();
        String tanggallahir = binding.etTanggalLahir.getText().toString();
        String jeniskelamin = binding.etJenisKelamin.getText().toString();
        String alamat = binding.etAlamat.getText().toString();
        String password = binding.etPassword.getText().toString();
        // Buat referensi ke document yang akan diubah namanya
        DocumentReference adminDocRef = firebaseFirestore.collection("Users").document(nomor);

        // Ubah nama document dengan nama baru yang diinputkan
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("nama", nama);
        updateData.put("nomortelepon", nomor);
        updateData.put("tanggallahir", tanggallahir);
        updateData.put("jeniskelamin", jeniskelamin);
        updateData.put("alamat", alamat);
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
        String nomor = Nomor;
        DocumentReference docRef = firebaseFirestore.collection("Users").document(nomor);
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
        String nama = binding.etNama.getText().toString();
        String nomor = binding.etNomorTelepon.getText().toString();
        String tanggallahir = binding.etTanggalLahir.getText().toString();
        String jeniskelamin = binding.etJenisKelamin.getText().toString();
        String alamat = binding.etAlamat.getText().toString();
        String password = binding.etPassword.getText().toString();
        String sebagai = "user";
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Login").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child("Login").child(nomor)
                        .setValue(new ModelUser(sebagai, nama, nomor, tanggallahir, jeniskelamin, alamat, password))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
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
        String nomor = Nomor;
        databaseRef = FirebaseDatabase.getInstance().getReference("Login");
        DatabaseReference deleteUser = databaseRef.child(nomor);
        deleteUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Data dengan nomor telepon tersebut ditemukan, maka hapus data
                    deleteUser.removeValue()
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        binding.etJenisKelamin.setText(text);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}