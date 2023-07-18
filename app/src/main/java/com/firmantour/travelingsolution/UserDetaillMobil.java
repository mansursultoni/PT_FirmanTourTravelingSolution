package com.firmantour.travelingsolution;

import static android.view.View.INVISIBLE;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class UserDetaillMobil extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    ImageView FotoProduk;
    TextView TvNomorTelpon;
    EditText EtNomor, EtStatus, EtMerk, EtNama, EtWarna, EtJumlahKursi, EtHarga;
    Button BtPemesanan, BtHubungi;
    ImageButton IbKembali;
    ProgressBar progressBar;
    private Uri filePath;
    private String fotoUrl, produkId;
    private static final int IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdetailmobil);


        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        //--Inisialisasi
        FotoProduk  = findViewById(R.id.imageView);
        EtNomor = findViewById(R.id.et_platnomor);
        EtStatus = findViewById(R.id.et_status);
        EtMerk = findViewById(R.id.et_namamerk);
        EtNama = findViewById(R.id.et_namamobil);
        EtWarna = findViewById(R.id.et_warna);
        EtJumlahKursi = findViewById(R.id.et_jumlahkursi);
        EtHarga = findViewById(R.id.et_harga);
        TvNomorTelpon = findViewById(R.id.tv_nomorTelpon);
        IbKembali = findViewById(R.id.ib_back);
        progressBar = findViewById(R.id.progressBar);

        ambilDataIntent();

        //--Tombol Pemesanan--
        BtPemesanan = findViewById(R.id.button_pemesanan);
        BtPemesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirimDataIntent();
            }
        });

        //--Tombol Kembali--
        IbKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progressBar.setVisibility(INVISIBLE);
        firebaseFirestore   = FirebaseFirestore.getInstance();
        storageReference    = FirebaseStorage.getInstance().getReference();
        produkId    = getIntent().getExtras().getString("nomor");

        readData();


      /*  TombolHubungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pesan1   = EtNama.getText().toString();
                String pesan2   = EtHarga.getText().toString();
                String pesan3   = TextDeskripsi.getText().toString();

                String semuaPesan   = "Nama : " + pesan1 + "\n" + "Harga : " + pesan2 + "\n" + "Deskripsi : " + pesan3 + "\n" + "Tambahan : " + "\n";

                Intent kirimWhatsapp = new Intent(Intent.ACTION_SEND);
                kirimWhatsapp.setType("text/plain");
                kirimWhatsapp.putExtra(Intent.EXTRA_TEXT, semuaPesan);
                kirimWhatsapp.putExtra("jid", "6285842358182" + "@s.whatsapp.net");
                kirimWhatsapp.setPackage("com.whatsapp");

                startActivity(kirimWhatsapp);

            }
        });*/


    }
    private void kirimDataIntent(){
        String input1 = TvNomorTelpon.getText().toString();
        String input2 = EtNomor.getText().toString();
        Intent intent = new Intent(UserDetaillMobil.this, UserPemesanan.class);
        intent.putExtra("pltnomor",input2);
        intent.putExtra("notlep", input1);
        startActivity(intent);
    }
    private void ambilDataIntent(){
        Intent intent = getIntent();
        String nomorTelpon = intent.getStringExtra("nomortelpon");
        TvNomorTelpon.setText(nomorTelpon);
    }
    private void readData() {
        firebaseFirestore.collection("RentalMobil").whereEqualTo("platnomor", produkId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                EtNomor.setText(document.getString("platnomor"));
                                EtStatus.setText(document.getString("status"));
                                EtMerk.setText(document.getString("namamerk"));
                                EtNama.setText(document.getString("namamobil"));
                                EtWarna.setText(document.getString("warna"));
                                EtJumlahKursi.setText(document.getString("jumlahkursi"));
                                EtHarga.setText(document.getString("harga"));
                                fotoUrl = document.getString("foto");
                                if (fotoUrl != "") {
                                    Picasso.get().load(fotoUrl).fit().into(FotoProduk);
                                } else {
                                    Picasso.get().load(R.drawable.ic_user).fit().into(FotoProduk);
                                }
                            }
                        } else {
                            Toast.makeText(UserDetaillMobil.this, "Gagal Mengambil Document", Toast.LENGTH_SHORT).show();
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