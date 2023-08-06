package com.firmantour.travelingsolution;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firmantour.travelingsolution.databinding.ActivityAdminDetailUserBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class AdminDetailUser extends AppCompatActivity {

    private ActivityAdminDetailUserBinding binding;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private String nomorTelepon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminDetailUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        nomorTelepon = getIntent().getExtras().getString("nomor");

        readData();

        binding.imgKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void readData() {
        firebaseFirestore.collection("Users").whereEqualTo("nomortelepon", nomorTelepon)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                binding.etNama.setText(document.getString("nama"));
                                binding.etNomor.setText(document.getString("nomortelepon"));
                                binding.etTanggalLahir.setText(document.getString("tanggallahir"));
                                binding.etJenisKelamin.setText(document.getString("jeniskelamin"));
                                binding.etAlamat.setText(document.getString("alamat"));
                            }
                        } else {
                            Toast.makeText(AdminDetailUser.this, "Gagal Mengambil data.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}