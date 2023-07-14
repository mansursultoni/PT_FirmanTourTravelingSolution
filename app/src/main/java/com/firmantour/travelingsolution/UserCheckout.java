package com.firmantour.travelingsolution;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserCheckout extends AppCompatActivity {

    TextView TvID, TvNama, TvTelpon, TvAlamat, TvPlatnomor, TvNamamerk, TvNamamobil, TvWarna,
            TvJumlahkursi, TvTotalHarga, TvTanggalsewa, TvTanggalKembali;
    Button BtPesan;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercheckout);

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

        String notlep = TvTelpon.getText().toString();
        String tglsewa = TvTanggalsewa.getText().toString();
        String tglkembali = TvTanggalKembali.getText().toString();

        String ID = notlep+tglsewa+tglkembali;

        firebaseFirestore = FirebaseFirestore.getInstance();

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

        BtPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = TvID.getText().toString().trim();
                String nama = TvNama.getText().toString().trim();
                String nomor = TvTelpon.getText().toString().trim();
                String alamat = TvAlamat.getText().toString().trim();
                String platnomor = TvPlatnomor.getText().toString().trim();
                String namamerk = TvNamamerk.getText().toString().trim();
                String namamobil = TvNamamobil.getText().toString().trim();
                String warna = TvWarna.getText().toString().trim();
                String jumlahkursi = TvJumlahkursi.getText().toString().trim();
                String tanggalsewa = TvTanggalsewa.getText().toString().trim();
                String tanggalkembali = TvTanggalKembali.getText().toString().trim();
                String totalharga = TvTotalHarga.getText().toString().trim();
                KirimData(id,nama,nomor,alamat,platnomor,namamerk,namamobil,warna,jumlahkursi,
                        tanggalsewa,tanggalkembali,totalharga);
            }
        });

    }
    private void KirimData(String id, String nama, String nomor, String alamat, String platnomor, String namamerk,
                           String namamobil, String warna, String jumlahkursi, String tanggalsewa,
                           String tanggalkembali, String totalharga){
        String key = TvID.getText().toString();
        String nmr = TvTelpon.getText().toString();
        Map<String, Object> doc = new HashMap<>();
        doc.put("key",key);
        doc.put("id",id);
        doc.put("nama",nama);
        doc.put("nomor", nomor);
        doc.put("alamat", alamat);
        doc.put("platnomor", platnomor);
        doc.put("namamerk", namamerk);
        doc.put("namamobil", namamobil);
        doc.put("warna", warna);
        doc.put("jumlahkursi", jumlahkursi);
        doc.put("tanggalsewa", tanggalsewa);
        doc.put("tanggalkembali", tanggalkembali);
        doc.put("totalharga", totalharga);
        firebaseFirestore.collection("PesananBelumSelesai").document(key).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
        firebaseFirestore.collection("PesananUser").document(nmr).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(UserCheckout.this, "Pemesanan diproses.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserCheckout.this, "Pemesanan gagal.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}