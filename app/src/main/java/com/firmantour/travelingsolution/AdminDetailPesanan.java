package com.firmantour.travelingsolution;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminDetailPesanan extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    String[] informationArray = new String[]{"Nama","Nomor Telpon","Alamat","Plat Nomor","Nama Merk","Nama Mobil",
            "Warna","Jumlah Kursi","Tanggal Sewa","Tanggal Kembali","Total Harga"};
    ImageView FotoProduk;
    String produkId;
    Spinner spinner;
    TextView TvID, TvNama, TvNomor, TvAlamat, TvPlatNomor, TvNamaMerk, TvNamaMobil, TvWarna, TvJumlahKursi, TvTanggalSewa, TvTanggalKembali, TvTotalHarga;
    Button BtKonfirmasi, BtHapus;
    ImageView TombolKembali;
    ProgressBar progressBar;

    int pageHeight = 1120;
    int pagewidth = 792;

    Bitmap bmp, scaleBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail_pesanan);

        TvID = findViewById(R.id.tv_id);
        TvNama = findViewById(R.id.tv_nama);
        TvNomor = findViewById(R.id.tv_telepon);
        TvAlamat = findViewById(R.id.tv_alamat);
        TvPlatNomor = findViewById(R.id.tv_platNomor);
        TvNamaMerk = findViewById(R.id.tv_namaMerk);
        TvNamaMobil = findViewById(R.id.tv_namaMobil);
        TvWarna = findViewById(R.id.tv_warna);
        TvJumlahKursi = findViewById(R.id.tv_kursi);
        TvTanggalSewa = findViewById(R.id.tv_tanggalSewa);
        TvTanggalKembali = findViewById(R.id.tv_tanggalKembali);
        TvTotalHarga = findViewById(R.id.tv_totalHarga);
        BtHapus = findViewById(R.id.bt_hapus);
        BtKonfirmasi = findViewById(R.id.bt_konfirmasi);


        firebaseFirestore   = FirebaseFirestore.getInstance();
        storageReference    = FirebaseStorage.getInstance().getReference();
        produkId    = getIntent().getExtras().getString("key");

        readData();

        BtHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String platnomor = TvPlatNomor.getText().toString();
                firebaseFirestore.collection("RentalMobil").document(platnomor)
                        .update("status","tersedia");

                /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminDetailPesanan.this);
                alertDialog.setTitle("Hapus Pesanan");
                alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hapusData();
                    }
                });
                alertDialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();*/
            }
        });
        BtKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = TvID.getText().toString().trim();
                String nama = TvNama.getText().toString().trim();
                String nomor = TvNomor.getText().toString().trim();
                String alamat = TvAlamat.getText().toString().trim();
                String platnomor = TvPlatNomor.getText().toString().trim();
                String namamerk = TvNamaMerk.getText().toString().trim();
                String namamobil = TvNamaMobil.getText().toString().trim();
                String warna = TvWarna.getText().toString().trim();
                String jumlahkursi = TvJumlahKursi.getText().toString().trim();
                String tanggalsewa = TvTanggalSewa.getText().toString().trim();
                String tanggalkembali = TvTanggalKembali.getText().toString().trim();
                String totalharga = TvTotalHarga.getText().toString().trim();
                KirimData(id,nama,nomor,alamat,platnomor,namamerk,namamobil,warna,jumlahkursi,
                        tanggalsewa,tanggalkembali,totalharga);

                kirimLaporan();

                createPdf(TvNama.getText().toString(),
                        TvNomor.getText().toString(),
                        TvAlamat.getText().toString(),
                        TvPlatNomor.getText().toString(),
                        TvNamaMerk.getText().toString(),
                        TvNamaMobil.getText().toString(),
                        TvWarna.getText().toString(),
                        TvJumlahKursi.getText().toString(),
                        TvTanggalSewa.getText().toString(),
                        TvTanggalKembali.getText().toString(),
                        TvTotalHarga.getText().toString());
            }
        });

    }
    private void KirimData(String id, String nama, String nomor, String alamat, String platnomor, String namamerk,
                           String namamobil, String warna, String jumlahkursi, String tanggalsewa,
                           String tanggalkembali, String totalharga) {
        String key = TvID.getText().toString();
        Map<String, Object> doc = new HashMap<>();
        doc.put("key", key);
        doc.put("id", id);
        doc.put("nama", nama);
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
        firebaseFirestore.collection("PesananSelesai").document(key).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
        firebaseFirestore.collection("Laporan").document(key).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    private void createPdf(String nama,String nomortelpon,String alamat,String platnomor,String namamerk,String namamobil,
                           String warna,String jumlahkursi,String tanggalsewa,String tanggalkembali,String totalharga){
        // create a new document
        PdfDocument document = new PdfDocument();
        Paint paint = new Paint();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(210, 297, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(9.0f);
        paint.setColor(Color.rgb(0,0,0));
        canvas.drawText("FIRMAN TOUR TRAVELING SOLUTION",pageInfo.getPageWidth()/2,30,paint);
        paint.setTextSize(6.0f);
        paint.setColor(Color.rgb(0,0,0));
        canvas.drawText("Karangtalun Lor, RT 02/04, Purwojati, Banyumas, 53175",pageInfo.getPageWidth()/2,38,paint);
        paint.setColor(Color.rgb(0,0,0));
        canvas.drawText("085842358182",pageInfo.getPageWidth()/2,45,paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(7.0f);
        paint.setColor(Color.rgb(0,0,0));
        canvas.drawText("Detail Pemesanan :",10,70,paint);

        int startXPosition = 10;
        int endXPosition = pageInfo.getPageWidth()-10;
        int startYPosition = 85;
        for (int i=0 ; i<11 ; i++){
            canvas.drawText(informationArray[i],startXPosition,startYPosition,paint);
            canvas.drawLine(startXPosition,startYPosition+3, endXPosition, startYPosition+3, paint);
            startYPosition+=12;
        }
        canvas.drawLine(70,80,70,215,paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(7.0f);
        paint.setColor(Color.rgb(0,0,0));
        canvas.drawText("Admin :",160,230,paint);

        canvas.drawLine(130,270,endXPosition,270,paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(7.0f);
        paint.setColor(Color.rgb(0,0,0));
        canvas.drawText(nama,80,85,paint);
        canvas.drawText(nomortelpon,80,97,paint);
        canvas.drawText(alamat,80,109,paint);
        canvas.drawText(platnomor,80,121,paint);
        canvas.drawText(namamerk,80,133,paint);
        canvas.drawText(namamobil,80,145,paint);
        canvas.drawText(warna,80,157,paint);
        canvas.drawText(jumlahkursi,80,169,paint);
        canvas.drawText(tanggalsewa,80,181,paint);
        canvas.drawText(tanggalkembali,80,193,paint);
        canvas.drawText(totalharga,80,205,paint);

        document.finishPage(page);
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Documents/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"test-3.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        document.close();
    }

    private void readData() {
        firebaseFirestore.collection("PesananBelumSelesai").whereEqualTo("key", produkId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TvID.setText(document.getString("key"));
                                TvNama.setText(document.getString("nama"));
                                TvNomor.setText(document.getString("nomor"));
                                TvAlamat.setText(document.getString("alamat"));
                                TvPlatNomor.setText(document.getString("platnomor"));
                                TvNamaMerk.setText(document.getString("namamerk"));
                                TvNamaMobil.setText(document.getString("namamobil"));
                                TvWarna.setText(document.getString("warna"));
                                TvJumlahKursi.setText(document.getString("jumlahkursi"));
                                TvTanggalSewa.setText(document.getString("tanggalsewa"));
                                TvTanggalKembali.setText(document.getString("tanggalkembali"));
                                TvTotalHarga.setText(document.getString("totalharga"));


                            }
                        } else {
                            Toast.makeText(AdminDetailPesanan.this, "Gagal Mengambil Document", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }
    private void hapusData() {
        String nomor = TvNomor.getText().toString();
        String id = TvID.getText().toString();
        firebaseFirestore.collection("ProdukBelumSelesai").document(id).delete();
        storageReference.child(produkId).delete();
        firebaseFirestore.collection("PesananUser").document(nomor).delete();
        storageReference.child(produkId).delete();
        Toast.makeText(this, "Pesanan telah dihapus", Toast.LENGTH_SHORT).show();
        finish();

    }
    private void kirimLaporan() {
        String nomor = TvNomor.getText().toString();
        String id = TvID.getText().toString();
        firebaseFirestore.collection("PesananBelumSelesai").document(id).delete();
        firebaseFirestore.collection("PesananUser").document(nomor).delete();

    }
}