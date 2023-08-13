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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    String[] informationArray = new String[]{"Nama", "Nomor Telpon", "Alamat", "Plat Nomor", "Nama Merk", "Nama Mobil",
            "Warna", "Jumlah Kursi", "Tanggal Sewa", "Tanggal Kembali", "Total Harga"};
    String produkId, fotoUrl;

    ProgressBar progressBar;

    Bitmap bmp, scaleBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminDetailPesananBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        produkId = getIntent().getExtras().getString("key");

        readData();

        binding.ivBuktipembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BitmapDrawable drawable = (BitmapDrawable) binding.ivBuktipembayaran.getDrawable();
                if (drawable != null && drawable.getBitmap() != null) {
                    // Gambar ada dalam ImageView
                    Bitmap bitmap = ((BitmapDrawable) binding.ivBuktipembayaran.getDrawable()).getBitmap();
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
        binding.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminDetailPesanan.this);
                alertDialog.setTitle("Buat Laporan?");
                alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createPdf(binding.tvNama.getText().toString(),
                                binding.tvTelepon.getText().toString(),
                                binding.tvAlamat.getText().toString(),
                                binding.tvPlatNomor.getText().toString(),
                                binding.tvNamaMerk.getText().toString(),
                                binding.tvNamaMobil.getText().toString(),
                                binding.tvWarna.getText().toString(),
                                binding.tvKursi.getText().toString(),
                                binding.tvTanggalSewa.getText().toString(),
                                binding.tvTanggalKembali.getText().toString(),
                                binding.tvTotalHarga.getText().toString());
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
        binding.btHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btHapus.setVisibility(View.INVISIBLE);
                binding.btHapus.setEnabled(false);
                binding.textCatatan.setEnabled(true);
                binding.textCatatan.setVisibility(View.VISIBLE);
                binding.btKonfirmasi.setVisibility(View.INVISIBLE);
                binding.btKonfirmasi.setEnabled(false);
                binding.btKonfirmasiHapus.setVisibility(View.VISIBLE);
                binding.btKonfirmasiHapus.setEnabled(true);
            }
        });
        binding.btKonfirmasiHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminDetailPesanan.this);
                alertDialog.setTitle("Batalkan Pesanan?");
                alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id = produkId;
                        DocumentReference documentReference = firebaseFirestore.collection("Pemesanan").document(id);
                        fieldBaru(documentReference);
                        batalkanPesanan();
                        Toast.makeText(AdminDetailPesanan.this, "Pemesanan Dibatalkan", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                alertDialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.btHapus.setVisibility(View.VISIBLE);
                        binding.btHapus.setEnabled(true);
                        binding.textCatatan.setEnabled(false);
                        binding.textCatatan.setVisibility(View.INVISIBLE);
                        binding.btKonfirmasiHapus.setVisibility(View.INVISIBLE);
                        binding.btKonfirmasiHapus.setEnabled(false);
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
        binding.btKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminDetailPesanan.this);
                alertDialog.setTitle("Konfirmasi Pesanan?");
                alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        konfirmasiPesanan();
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
    private void fieldBaru(DocumentReference documentReference) {
        // Buat data yang ingin ditambahkan (misalnya field "newField" dengan nilai "newValue")
        String cttn = binding.etCatatan.getText().toString();
        Map<String, Object> newData = new HashMap<>();
        newData.put("catatan", cttn);
        // Update dokumen dengan data baru
        documentReference.update(newData)
                .addOnSuccessListener(aVoid -> {
                    // Berhasil menambahkan field baru
                })
                .addOnFailureListener(e -> {
                    // Gagal menambahkan field baru
                });
    }

    private void konfirmasiPesanan() {
        // Update status pesanan
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
        String status = binding.tvPlatNomor.getText().toString();
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
        /*CollectionReference pemesanan = firebaseFirestore.collection("Pemesanan");
        kirimKonfirmasi(pemesanan);*/
        finish();
    }
    public void kirimKonfirmasi(CollectionReference pemesananCollection){
            // Membaca data dari koleksi "Pemesanan" dan menambahkannya ke koleksi baru "PemesananDibatalkan"
            pemesananCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
                for (DocumentSnapshot document : queryDocumentSnapshots) {
                    String id = document.getId();
                    Map<String, Object> data = document.getData();
                    // Membuat koleksi baru "PemesananDibatalkan"
                    firebaseFirestore.collection("PemesananDibatalkan").document(id).set(data);
                }
            });
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
        canvas.drawText(binding.tvTeleponrekening.getText().toString(), pageInfo.getPageWidth() / 2, 45, paint);

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
                                binding.tvTeleponrekening.setText(document.getString("teleponrekening"));
                            }
                        } else {
                            Toast.makeText(AdminDetailPesanan.this, "Gagal Mengambil Document", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        Intent intent = getIntent();
        String statusPesanan = intent.getStringExtra("STATUS_PESANAN");

        if (statusPesanan != null) {
            binding.btKonfirmasi.setVisibility(View.INVISIBLE);
            binding.btHapus.setVisibility(View.INVISIBLE);
            binding.layoutPembayaran.setVisibility(View.INVISIBLE);
            binding.layoutPembayaran.setEnabled(false);

            firebaseFirestore.collection("Pemesanan").whereEqualTo("key", produkId)
                    .whereEqualTo("statuspesanan", "Selesai")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    binding.tvId.setText(document.getString("key"));
                                    binding.tvNama.setText(document.getString("nama"));
                                    binding.tvTelepon.setText(document.getString("nomortelepon"));
                                    binding.tvAlamat.setText(document.getString("alamat"));
                                    binding.tvPlatNomor.setText(document.getString("platnomor"));
                                    binding.tvNamaMerk.setText(document.getString("namamerk"));
                                    binding.tvNamaMobil.setText(document.getString("namamobil"));
                                    binding.tvWarna.setText(document.getString("warna"));
                                    binding.tvKursi.setText(document.getString("jumlahkursi"));
                                    binding.tvTanggalSewa.setText(document.getString("tanggalsewa"));
                                    binding.tvTanggalKembali.setText(document.getString("tanggalkembali"));
                                    binding.tvTotalHarga.setText(document.getString("harga"));
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
                                    binding.tvId.setText(document.getString("key"));
                                    binding.tvNama.setText(document.getString("nama"));
                                    binding.tvTelepon.setText(document.getString("nomortelepon"));
                                    binding.tvAlamat.setText(document.getString("alamat"));
                                    binding.tvPlatNomor.setText(document.getString("platnomor"));
                                    binding.tvNamaMerk.setText(document.getString("namamerk"));
                                    binding.tvNamaMobil.setText(document.getString("namamobil"));
                                    binding.tvWarna.setText(document.getString("warna"));
                                    binding.tvKursi.setText(document.getString("jumlahkursi"));
                                    binding.tvTanggalSewa.setText(document.getString("tanggalsewa"));
                                    binding.tvTanggalKembali.setText(document.getString("tanggalkembali"));
                                    binding.tvTotalHarga.setText(document.getString("harga"));
                                    fotoUrl = document.getString("foto");
                                    if (fotoUrl != "") {
                                        Picasso.get().load(fotoUrl).fit().into(binding.ivBuktipembayaran);
                                    } else {
                                        Picasso.get().load(R.drawable.ic_user).fit().into(binding.ivBuktipembayaran);
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

    private void batalkanPesanan() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String key = produkId;
        Map<String, Object> updateStatus = new HashMap<>();
        updateStatus.put("statuspesanan", "Dibatalkan");
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
        Toast.makeText(this, "Pesanan telah dibatalkan.", Toast.LENGTH_SHORT).show();
        finish();

    }


}