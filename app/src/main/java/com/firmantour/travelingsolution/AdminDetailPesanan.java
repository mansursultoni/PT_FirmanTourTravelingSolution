package com.firmantour.travelingsolution;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firmantour.travelingsolution.databinding.ActivityAdminDetailPesananBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminDetailPesanan extends AppCompatActivity {

    private ActivityAdminDetailPesananBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    LinearLayout LayoutPembayaran;
    String[] informationArray = new String[]{"Nama", "Nomor Telpon", "Alamat", "Plat Nomor", "Nama Merk", "Nama Mobil",
            "Warna", "Jumlah Kursi", "Tanggal Sewa", "Tanggal Kembali", "Total Harga"};
    String produkId, fotoUrl;
    TextView TvID, TvNama, TvNomor, TvAlamat, TvPlatNomor, TvNamaMerk, TvNamaMobil, TvWarna,
            TvJumlahKursi, TvTanggalSewa, TvTanggalKembali, TvTotalHarga, TvTeleponRekening;
    Button BtKonfirmasi, BtHapus, BtHapusSelesai;
    ImageView BtKembali, BtPrint, FotoPembayaran;
    ProgressBar progressBar;

    Bitmap bmp, scaleBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail_pesanan);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        LayoutPembayaran = findViewById(R.id.layoutPembayaran);
        FotoPembayaran = findViewById(R.id.iv_buktipembayaran);
        TvID = findViewById(R.id.tv_id);
        TvTeleponRekening = findViewById(R.id.tv_teleponrekening);
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
        BtPrint = findViewById(R.id.btnPrint);
        BtKembali = findViewById(R.id.ib_back);
        BtHapusSelesai = findViewById(R.id.bt_hapusSelesai);


        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        produkId = getIntent().getExtras().getString("key");

        readData();

        FotoPembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BitmapDrawable drawable = (BitmapDrawable) FotoPembayaran.getDrawable();
                if (drawable != null && drawable.getBitmap() != null) {
                    // Gambar ada dalam ImageView
                    Bitmap bitmap = ((BitmapDrawable) FotoPembayaran.getDrawable()).getBitmap();
                    Intent intent = new Intent(AdminDetailPesanan.this, ActivityFoto.class);
                    intent.putExtra("imageBitmap", bitmap);
                    // Jalankan Activity B
                    startActivity(intent);
                } else {
                    // Gambar tidak ada dalam ImageView
                    Toast.makeText(AdminDetailPesanan.this, "Gambar tidak ada.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        BtKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        BtPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminDetailPesanan.this);
                alertDialog.setTitle("Buat Laporan?");
                alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
                alertDialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
        BtHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminDetailPesanan.this);
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
                alertDialog.show();
            }
        });
        BtKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                konfirmasiPesanan();


                /*String id = TvID.getText().toString().trim();
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

                firebaseFirestore.collection("RentalMobil").document(platnomor)
                        .update("status","Tersedia");
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
                        TvTotalHarga.getText().toString());*/
            }
        });
        BtHapusSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminDetailPesanan.this);
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
                alertDialog.show();
            }
        });

    }

    private void konfirmasiPesanan() {
        // Membuat Database Pemesanan
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String key = produkId;
        Map<String, Object> updateStatus = new HashMap<>();
        updateStatus.put("statuspesanan", "Sedang Disewa");
        DocumentReference userRef = db.collection("Pemesanan").document(key);
        userRef.update(updateStatus)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminDetailPesanan.this, "Pemesanan Dikonfirmasi", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AdminDetailPesanan.this, "Gagal.", Toast.LENGTH_SHORT).show();
                            // Failed to update data
                            // Handle the error
                        }
                    }
                });

        // Mengubah Status Mobil
        String status = TvPlatNomor.getText().toString();
        DocumentReference rentalRef = db.collection("RentalMobil").document(status);
        rentalRef.update("status", "Sedang Disewa")
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
        finish();
    }

    private void createPdf(String nama, String nomortelpon, String alamat, String platnomor, String namamerk, String namamobil,
                           String warna, String jumlahkursi, String tanggalsewa, String tanggalkembali, String totalharga) {
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
        paint.setColor(Color.rgb(0, 0, 0));
        canvas.drawText("FIRMAN TOUR TRAVELING SOLUTION", pageInfo.getPageWidth() / 2, 30, paint);
        paint.setTextSize(6.0f);
        paint.setColor(Color.rgb(0, 0, 0));
        canvas.drawText("Karangtalun Lor, RT 02/04, Purwojati, Banyumas, 53175", pageInfo.getPageWidth() / 2, 38, paint);
        paint.setColor(Color.rgb(0, 0, 0));
        canvas.drawText(TvTeleponRekening.getText().toString(), pageInfo.getPageWidth() / 2, 45, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(7.0f);
        paint.setColor(Color.rgb(0, 0, 0));
        canvas.drawText("Detail Pemesanan :", 10, 70, paint);

        int startXPosition = 10;
        int endXPosition = pageInfo.getPageWidth() - 10;
        int startYPosition = 85;
        for (int i = 0; i < 11; i++) {
            canvas.drawText(informationArray[i], startXPosition, startYPosition, paint);
            canvas.drawLine(startXPosition, startYPosition + 3, endXPosition, startYPosition + 3, paint);
            startYPosition += 12;
        }
        canvas.drawLine(70, 80, 70, 215, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(7.0f);
        paint.setColor(Color.rgb(0, 0, 0));
        canvas.drawText("Admin :", 160, 230, paint);

        canvas.drawLine(130, 270, endXPosition, 270, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(7.0f);
        paint.setColor(Color.rgb(0, 0, 0));
        canvas.drawText(nama, 80, 85, paint);
        canvas.drawText(nomortelpon, 80, 97, paint);
        canvas.drawText(alamat, 80, 109, paint);
        canvas.drawText(platnomor, 80, 121, paint);
        canvas.drawText(namamerk, 80, 133, paint);
        canvas.drawText(namamobil, 80, 145, paint);
        canvas.drawText(warna, 80, 157, paint);
        canvas.drawText(jumlahkursi, 80, 169, paint);
        canvas.drawText(tanggalsewa, 80, 181, paint);
        canvas.drawText(tanggalkembali, 80, 193, paint);
        canvas.drawText(totalharga, 80, 205, paint);

        document.finishPage(page);
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Documents/RentalMobil/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String name = produkId;
        String targetPdf = directory_path + name + ".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Laporan berhasil dibuat.", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        document.close();
    }

    private void readData() {
        firebaseFirestore.collection("AkunBank")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TvTeleponRekening.setText(document.getString("teleponrekening"));
                            }
                        } else {
                            Toast.makeText(AdminDetailPesanan.this, "Gagal Mengambil Document", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        Intent intent = getIntent();
        String statusPesanan = intent.getStringExtra("STATUS_PESANAN");

        if (statusPesanan != null) {
            BtKonfirmasi.setVisibility(View.INVISIBLE);
            BtHapus.setVisibility(View.INVISIBLE);
            BtHapusSelesai.setVisibility(View.VISIBLE);
            BtHapusSelesai.setEnabled(true);
            LayoutPembayaran.setVisibility(View.INVISIBLE);
            LayoutPembayaran.setEnabled(false);

            firebaseFirestore.collection("Pemesanan").whereEqualTo("key", produkId)
                    .whereEqualTo("statuspesanan", "Selesai")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    TvID.setText(document.getString("key"));
                                    TvNama.setText(document.getString("nama"));
                                    TvNomor.setText(document.getString("nomortelepon"));
                                    TvAlamat.setText(document.getString("alamat"));
                                    TvPlatNomor.setText(document.getString("platnomor"));
                                    TvNamaMerk.setText(document.getString("namamerk"));
                                    TvNamaMobil.setText(document.getString("namamobil"));
                                    TvWarna.setText(document.getString("warna"));
                                    TvJumlahKursi.setText(document.getString("jumlahkursi"));
                                    TvTanggalSewa.setText(document.getString("tanggalsewa"));
                                    TvTanggalKembali.setText(document.getString("tanggalkembali"));
                                    TvTotalHarga.setText(document.getString("harga"));


                                }
                            } else {
                                Toast.makeText(AdminDetailPesanan.this, "Gagal Mengambil Document", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
        } else {
            firebaseFirestore.collection("Pemesanan").whereEqualTo("key", produkId)
                    .whereEqualTo("statuspesanan", "Belum Selesai")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    TvID.setText(document.getString("key"));
                                    TvNama.setText(document.getString("nama"));
                                    TvNomor.setText(document.getString("nomortelepon"));
                                    TvAlamat.setText(document.getString("alamat"));
                                    TvPlatNomor.setText(document.getString("platnomor"));
                                    TvNamaMerk.setText(document.getString("namamerk"));
                                    TvNamaMobil.setText(document.getString("namamobil"));
                                    TvWarna.setText(document.getString("warna"));
                                    TvJumlahKursi.setText(document.getString("jumlahkursi"));
                                    TvTanggalSewa.setText(document.getString("tanggalsewa"));
                                    TvTanggalKembali.setText(document.getString("tanggalkembali"));
                                    TvTotalHarga.setText(document.getString("harga"));
                                    fotoUrl = document.getString("foto");
                                    if (fotoUrl != "") {
                                        Picasso.get().load(fotoUrl).fit().into(FotoPembayaran);
                                    } else {
                                        Picasso.get().load(R.drawable.ic_user).fit().into(FotoPembayaran);
                                    }

                                }
                            } else {
                                Toast.makeText(AdminDetailPesanan.this, "Gagal Mengambil Document", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
        }

    }

    private void hapusData() {
        String nomor = TvNomor.getText().toString();
        String id = TvID.getText().toString();
        firebaseFirestore.collection("Pemesanan").document(id).delete();
        storageReference.child(produkId).delete();
        Toast.makeText(this, "Pesanan telah dihapus", Toast.LENGTH_SHORT).show();
        finish();

    }


}