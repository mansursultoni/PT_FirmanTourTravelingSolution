package com.firmantour.travelingsolution;

import static android.view.View.INVISIBLE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class UserDetailPaketWisata extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    ImageView FotoProduk;
    EditText TextNama, TextHarga, TextDeskripsi, TextStatus;
    ImageView TombolKembali;
    ProgressBar progressBar;
    private Uri filePath;
    private String fotoUrl, produkId;
    private static final int IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_paket_wisata);


        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));


        FotoProduk  = findViewById(R.id.imageView);

        TextNama    = findViewById(R.id.editTextNama);
        TextHarga   = findViewById(R.id.editTextHarga);
        TextDeskripsi = findViewById(R.id.editTextDeskripsi);
        TextStatus  = findViewById(R.id.editTextStatus);

        TombolKembali = findViewById(R.id.buttonBack);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(INVISIBLE);
        firebaseFirestore   = FirebaseFirestore.getInstance();
        storageReference    = FirebaseStorage.getInstance().getReference();
        produkId    = getIntent().getExtras().getString("nomor");
        readData();

        TombolKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    private void readData() {
        firebaseFirestore.collection("PaketWisata").whereEqualTo("nomor", produkId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TextNama.setText(document.getString("nama"));
                                TextHarga.setText(document.getString("harga"));
                                TextDeskripsi.setText(document.getString("deskripsi"));
                                TextStatus.setText(document.getString("status"));
                                fotoUrl = document.getString("foto");
                                if (fotoUrl != "") {
                                    Picasso.get().load(fotoUrl).fit().into(FotoProduk);
                                } else {
                                    Picasso.get().load(R.drawable.ic_user).fit().into(FotoProduk);
                                }
                            }
                        } else {
                            Toast.makeText(UserDetailPaketWisata.this, "Gagal Mengambil Document", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Picasso.get().load(filePath).fit().into(FotoProduk);
        } else {
            Toast.makeText(this, "Tidak ada gambar dipilih", Toast.LENGTH_SHORT).show();
        }
    }
}