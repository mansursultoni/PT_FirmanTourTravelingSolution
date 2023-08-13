package com.firmantour.travelingsolution;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firmantour.travelingsolution.databinding.ActivityUsercheckoutBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UserCheckout extends AppCompatActivity {

    private ActivityUsercheckoutBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    TextView TvID, TvNama, TvTelpon, TvAlamat, TvPlatnomor, TvNamamerk, TvNamamobil, TvWarna,
            TvJumlahkursi, TvTotalHarga, TvTanggalsewa, TvTanggalKembali
            , TvNamaBank, TvNomorRekening, TvAtasNama, TvTeleponRekening;
    Button BtPesan;

    ImageView FotoPembayaran, TombolKembali;
    private Uri filePath;
    private String fotoUrl;
    private static final int IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsercheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        TvID = findViewById(R.id.tv_id);
        TvNama = findViewById(R.id.tv_nama);
        TvTelpon = findViewById(R.id.tv_telepon);
        TvAlamat = findViewById(R.id.tv_alamat);
        TvPlatnomor = findViewById(R.id.tv_platNomor);
        TvNamamerk = findViewById(R.id.tv_namaMerk);
        TvNamamobil = findViewById(R.id.tv_namaMobil);
        TvWarna = findViewById(R.id.tv_warna);
        TvJumlahkursi = findViewById(R.id.tv_kursi);
        TvTotalHarga = findViewById(R.id.tv_totalHarga);
        TvTanggalsewa = findViewById(R.id.tv_tanggalSewa);
        TvTanggalKembali = findViewById(R.id.tv_tanggalKembali);
        BtPesan = findViewById(R.id.bt_pesan);
        FotoPembayaran = findViewById(R.id.iv_buktipembayaran);
        TvNamaBank = findViewById(R.id.tvNamaBank);
        TvNomorRekening = findViewById(R.id.tvNomorRekening);
        TvAtasNama = findViewById(R.id.tvAtasNama);
        TvTeleponRekening = findViewById(R.id.tvTeleponRekening);

        String notlep = TvTelpon.getText().toString();
        String tglsewa = TvTanggalsewa.getText().toString();
        String tglkembali = TvTanggalKembali.getText().toString();

        String ID = notlep+tglsewa+tglkembali;

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference    = FirebaseStorage.getInstance().getReference();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String nama = intent.getStringExtra("nama");
        String nomor = intent.getStringExtra("nomor");
        String alamat = intent.getStringExtra("alamat");
        String platnomor = intent.getStringExtra("platnomor");
        String namamerk = intent.getStringExtra("namamerk");
        String namamobil = intent.getStringExtra("namamobil");
        String warna = intent.getStringExtra("warna");
        String jumlahkursi = intent.getStringExtra("jumlahkursi");
        String totalharga = intent.getStringExtra("totalharga");
        String sewa = intent.getStringExtra("sewa");
        String kembali =intent.getStringExtra("kembali");
        TvID.setText(id);
        TvNama.setText(nama);
        TvTelpon.setText(nomor);
        TvAlamat.setText(alamat);
        TvPlatnomor.setText(platnomor);
        TvNamamerk.setText(namamerk);
        TvNamamobil.setText(namamobil);
        TvWarna.setText(warna);
        TvJumlahkursi.setText(jumlahkursi);
        TvTotalHarga.setText(totalharga);
        TvTanggalsewa.setText(sewa);
        TvTanggalKembali.setText(kembali);

        firebaseFirestore.collection("AkunBank")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TvNamaBank.setText(document.getString("namabank"));
                                TvNomorRekening.setText(document.getString("nomorrekening"));
                                TvAtasNama.setText(document.getString("atasnama"));
                                TvTeleponRekening.setText(document.getString("teleponrekening"));
                            }
                        } else {
                            Toast.makeText(UserCheckout.this, "Gagal Mengambil Document", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        BtPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWaktu();
                uploadImage();
            }
        });
        FotoPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ambilGambar();
            }
        });

    }
    private void SimpanData(String foto, String key, String nama, String nomortelepon, String alamat, String platnomor, String namamerk, String namamobil,
                            String warna, String jumlahkursi, String tanggalsewa, String tanggalkembali,
                            String harga, String waktu, String statuspesanan ){
        Map<String, Object> data = new HashMap<>();
        data.put("foto", foto);
        data.put("key", key);
        data.put("nama", nama);
        data.put("nomortelepon", nomortelepon);
        data.put("alamat", alamat);
        data.put("platnomor", platnomor);
        data.put("namamerk", namamerk);
        data.put("namamobil", namamobil);
        data.put("warna", warna);
        data.put("jumlahkursi", jumlahkursi);
        data.put("tanggalsewa", tanggalsewa);
        data.put("tanggalkembali", tanggalkembali);
        data.put("harga", harga);
        data.put("waktu", waktu);
        data.put("statuspesanan", statuspesanan);
        firebaseFirestore.collection("Pemesanan").document(key).set(data).isSuccessful();
    }
    private void ambilGambar(){
        ImagePicker.with(UserCheckout.this)
                .start();
    }
    private void uploadImage(){
        String telepon = TvTelpon.getText().toString();
        if(filePath != null){
            String key = TvID.getText().toString();
            DocumentReference docRef = firebaseFirestore.collection("Pemesanan").document(key);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Toast.makeText(UserCheckout.this, "Pemesanan anda sudah ada.", Toast.LENGTH_LONG).show();
                        } else {
                            final StorageReference ref = storageReference.child(TvID.getText().toString());
                            UploadTask uploadTask = ref.putFile(filePath);
                            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    return ref.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String statuspesan = "Belum Selesai";
                                    Uri imagePath = task.getResult();
                                    fotoUrl = imagePath.toString();
                                    SimpanData(fotoUrl,
                                            TvID.getText().toString(),
                                            TvNama.getText().toString(),
                                            TvTelpon.getText().toString(),
                                            TvAlamat.getText().toString(),
                                            TvPlatnomor.getText().toString(),
                                            TvNamamerk.getText().toString(),
                                            TvNamamobil.getText().toString(),
                                            TvWarna.getText().toString(),
                                            TvJumlahkursi.getText().toString(),
                                            TvTanggalsewa.getText().toString(),
                                            TvTanggalKembali.getText().toString(),
                                            TvTotalHarga.getText().toString(),
                                            binding.tvWaktu.getText().toString(),
                                            statuspesan);
                                    Toast.makeText(UserCheckout.this, "Pemesanan Telah Diproses.", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent intent3 = new Intent(UserCheckout.this, UserPesananBelumSelesai.class);
                                    intent3.putExtra("nomortelepon", telepon);
                                    startActivity(intent3);
                                }
                            });
                        }
                    } else {
                        Toast.makeText(UserCheckout.this, "Pemesanan Gagal.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
    private void setWaktu(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        binding.tvWaktu.setText(currentDateAndTime);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        filePath = data.getData();
        FotoPembayaran.setImageURI(filePath);
    }
}