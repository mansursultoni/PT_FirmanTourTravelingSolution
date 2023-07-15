package com.firmantour.travelingsolution;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class AdminDetailWisata extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    DatabaseReference firebaseDatabase;
    private StorageReference storageReference;
    ImageView FotoMobil;
    Spinner spinner;
    EditText EtKodeWisata, EtNamaWisata, TextHarga ;
    Button TombolEdit, TombolHapus;
    ImageView TombolKembali;
    ProgressBar progressBar;
    private Uri filePath;
    private String fotoUrl, produkId;
    private static final int IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail_wisata);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        FotoMobil = findViewById(R.id.imageView);
        EtKodeWisata = findViewById(R.id.et_kodewisata);
        EtNamaWisata = findViewById(R.id.et_namawisata);
        TextHarga   = findViewById(R.id.et_harga);
        TombolHapus = findViewById(R.id.bt_delete);
        TombolEdit  = findViewById(R.id.bt_update);
        TombolKembali = findViewById(R.id.ib_back);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(INVISIBLE);
        firebaseFirestore   = FirebaseFirestore.getInstance();
        storageReference    = FirebaseStorage.getInstance().getReference();
        produkId    = getIntent().getExtras().getString("nomor");

        readData();

        FotoMobil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ambilGambar();
            }
        });
        TombolEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminDetailWisata.this);
                alertDialog.setTitle("Edit");
                alertDialog.setMessage("Yakin mengedit data?");
                alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uploadImage();
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
        TombolHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminDetailWisata.this);
                alertDialog.setTitle("Hapus");
                alertDialog.setMessage("Yakin menghapus data?");
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
        TombolKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    private void readData() {
        firebaseFirestore.collection("PaketWisata").whereEqualTo("kodewisata", produkId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                fotoUrl = document.getString("foto");
                                EtKodeWisata.setText(document.getString("kodewisata"));
                                EtNamaWisata.setText(document.getString("namawisata"));
                                TextHarga.setText(document.getString("harga"));
                                if (fotoUrl != "") {
                                    Picasso.get().load(fotoUrl).fit().into(FotoMobil);
                                } else {
                                    Picasso.get().load(R.drawable.ic_user).fit().into(FotoMobil);
                                }
                            }
                        } else {
                            Toast.makeText(AdminDetailWisata.this, "Gagal Mengambil Document", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    private void SimpanData(String foto, String kodewisata, String namawisata, String harga) {
        Map<String, Object> data = new HashMap<>();
        data.put("foto", foto);
        data.put("kodewisata", kodewisata);
        data.put("namawisata", namawisata);
        data.put("harga", harga);
        firebaseFirestore.collection("RentalMobil").document(kodewisata).set(data).isSuccessful();
    }

    private void ambilGambar() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Picasso.get().load(filePath).fit().into(FotoMobil);
        } else {
            Toast.makeText(this, "Tidak ada gambar dipilih", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage() {
        if (filePath != null) {
            final StorageReference ref = storageReference.child(EtKodeWisata.getText().toString());
            UploadTask uploadTask = ref.putFile(filePath);
            Task<Uri> uriTask = uploadTask.continueWithTask(task -> {
                return ref.getDownloadUrl();
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri imagePath = task.getResult();
                    fotoUrl = imagePath.toString();
                    SimpanData(fotoUrl,
                            EtKodeWisata.getText().toString(),
                            EtNamaWisata.getText().toString(),
                            TextHarga.getText().toString());
                    progressBar.setProgress(0);
                    progressBar.setVisibility(INVISIBLE);
                    Toast.makeText(AdminDetailWisata.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(VISIBLE);
                    double progres = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int) progres);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(INVISIBLE);
                    Toast.makeText(AdminDetailWisata.this, "Gagal " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            SimpanData(fotoUrl,
                    EtKodeWisata.getText().toString(),
                    EtNamaWisata.getText().toString(),
                    TextHarga.getText().toString());
            Toast.makeText(this, "Produk telah diubah", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void hapusData() {
        String kodewisata = EtKodeWisata.getText().toString();
        firebaseFirestore.collection("PaketWisata").document(produkId).delete();
        storageReference.child(produkId).delete();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference()
                .child("PaketWisata").child(kodewisata);
        firebaseDatabase.removeValue();
        Toast.makeText(this, "Produk telah dihapus", Toast.LENGTH_SHORT).show();
        finish();

    }
}