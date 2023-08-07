package com.firmantour.travelingsolution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.firmantour.travelingsolution.adminfragment.APengaturan;
import com.firmantour.travelingsolution.databinding.ActivityAdminTambahAnggotaBinding;
import com.firmantour.travelingsolution.databinding.FragmentATambahAnggotaBinding;
import com.firmantour.travelingsolution.model.ModelAdmin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class AdminTambahAnggota extends AppCompatActivity {

    private ActivityAdminTambahAnggotaBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$";
    private Pattern pattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminTambahAnggotaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        pattern = Pattern.compile(PASSWORD_PATTERN);

        binding.imgKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = binding.etPassword.getText().toString();
                validatePassword(password);
            }
        });


    }

    private void validatePassword(String password){
        if (pattern.matcher(password).matches()) {
            // Password is valid
            if (binding.etNama.getText().toString().isEmpty() ||
                    binding.etNomorTelepon.getText().toString().isEmpty() ||
                    binding.etPassword.getText().toString().isEmpty()){
                Toast.makeText(this, "Data harus lengkap.", Toast.LENGTH_SHORT).show();
            } else {
                databaseReference.child("Login").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(binding.etNomorTelepon.getText().toString())){
                            Toast.makeText(AdminTambahAnggota.this, "Nomor telepon telah terdaftar.", Toast.LENGTH_SHORT).show();
                        }else {
                            if (!binding.etPassword2.getText().toString().equals(binding.etPassword.getText().toString())){
                                Toast.makeText(AdminTambahAnggota.this, "Masukkan ulang password.", Toast.LENGTH_SHORT).show();
                            }else {
                                String sebagai = "admin";
                                databaseReference.child("Login").child(binding.etNomorTelepon.getText().toString())
                                        .setValue(new ModelAdmin(binding.etNama.getText().toString(),
                                                binding.etNomorTelepon.getText().toString(),
                                                binding.etPassword.getText().toString(),
                                                sebagai)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                saveData(binding.etNama.getText().toString(),
                                                        binding.etNomorTelepon.getText().toString(),
                                                        binding.etPassword.getText().toString());
                                                Toast.makeText(AdminTambahAnggota.this, "Anggota Admin Berhasil Ditambah..", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(AdminTambahAnggota.this, "Gagal menyimpan data.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        } else {
            // Password is invalid
            Toast.makeText(AdminTambahAnggota.this, "Password minimal 6 karakter dengan huruf besar, huruf kecil, dan angka.", Toast.LENGTH_LONG).show();
        }
    }

    private void saveData(String namaadmin, String nomortelepon, String password){
        Map<String, Object> user = new HashMap<>();
        user.put("namaadmin", namaadmin);
        user.put("nomortelepon", nomortelepon);
        user.put("password", password);

        db.collection("Admin").document(nomortelepon).set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                        }else{
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminTambahAnggota.this, "Gagal Menyimpan data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}