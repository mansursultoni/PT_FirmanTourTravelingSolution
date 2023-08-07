package com.firmantour.travelingsolution;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.firmantour.travelingsolution.databinding.ActivityAdmindetailmobilBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
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

public class AdminDetailMobil extends AppCompatActivity implements AdapterView.OnItemSelectedListener{


    private ActivityAdmindetailmobilBinding binding;
    private FirebaseFirestore firebaseFirestore;
    DatabaseReference firebaseDatabase;
    private StorageReference storageReference;
    ImageView FotoMobil;
    Spinner spinner;
    EditText TvPlatNomor, TextStatus, TvNamaMerk, TvNamaMobil,  TvWarna, TvJumlahKursi, TextHarga ;
    Button TombolEdit, TombolHapus;
    ImageView TombolKembali;
    ProgressBar progressBar;
    private Uri filePath;
    private String fotoUrl, produkId;
    private static final int IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdmindetailmobilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        /*spinner     = findViewById(R.id.spinner2);
        FotoMobil = findViewById(R.id.imageView);
        TvPlatNomor = findViewById(R.id.et_platnomor);
        TextStatus  = findViewById(R.id.et_status);
        TvNamaMerk  = findViewById(R.id.et_namamerk);
        TvNamaMobil = findViewById(R.id.et_namamobil);
        TvWarna     = findViewById(R.id.et_warna);
        TvJumlahKursi= findViewById(R.id.et_jumlahkursi);
        TextHarga   = findViewById(R.id.et_harga);
        TombolHapus = findViewById(R.id.bt_delete);
        TombolEdit  = findViewById(R.id.bt_update);
        TombolKembali = findViewById(R.id.ib_back);*/

        binding.progressBar.setVisibility(INVISIBLE);
        firebaseFirestore   = FirebaseFirestore.getInstance();
        storageReference    = FirebaseStorage.getInstance().getReference();
        produkId    = getIntent().getExtras().getString("nomor");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.status, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner2.setAdapter(adapter);
        binding.spinner2.setOnItemSelectedListener(this);

        readData();

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ambilGambar();
            }
        });
        binding.btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminDetailMobil.this);
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
        binding.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminDetailMobil.this);
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
        binding.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void readData() {
        firebaseFirestore.collection("RentalMobil").whereEqualTo("platnomor", produkId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                fotoUrl = document.getString("foto");
                                binding.etPlatnomor.setText(document.getString("platnomor"));
                                binding.etStatus.setText(document.getString("status"));
                                binding.etNamamerk.setText(document.getString("namamerk"));
                                binding.etNamamobil.setText(document.getString("namamobil"));
                                binding.etWarna.setText(document.getString("warna"));
                                binding.etJumlahkursi.setText(document.getString("kursi"));
                                binding.etHarga.setText(document.getString("harga"));
                                if (fotoUrl != "") {
                                    Picasso.get().load(fotoUrl).fit().into(binding.imageView);
                                } else {
                                    Picasso.get().load(R.drawable.ic_user).fit().into(binding.imageView);
                                }
                            }
                        } else {
                            Toast.makeText(AdminDetailMobil.this, "Gagal Mengambil Document", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    private void SimpanData(String foto, String platnomor, String status, String namamerk, String namamobil, String warna, String jumlahkursi, String harga) {
        Map<String, Object> data = new HashMap<>();
        data.put("foto", foto);
        data.put("platnomor", platnomor);
        data.put("status", status);
        data.put("namamerk", namamerk);
        data.put("namamobil", namamobil);
        data.put("warna", warna);
        data.put("kursi", jumlahkursi);
        data.put("harga", harga);
        firebaseFirestore.collection("RentalMobil").document(platnomor).set(data).isSuccessful();
    }

    private void ambilGambar() {
        ImagePicker.with(AdminDetailMobil.this)
                .crop(1f,1f)
                .start();
        /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), IMAGE_REQUEST);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Picasso.get().load(filePath).fit().into(binding.imageView);
        } else {
            Toast.makeText(this, "Tidak ada gambar dipilih", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage() {
        if (filePath != null) {
            final StorageReference ref = storageReference.child(binding.etPlatnomor.getText().toString());
            UploadTask uploadTask = ref.putFile(filePath);
            Task<Uri> uriTask = uploadTask.continueWithTask(task -> {
                return ref.getDownloadUrl();
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri imagePath = task.getResult();
                    fotoUrl = imagePath.toString();
                    SimpanData(fotoUrl,
                            binding.etPlatnomor.getText().toString(),
                            binding.etStatus.getText().toString(),
                            binding.etNamamerk.getText().toString(),
                            binding.etNamamobil.getText().toString(),
                            binding.etWarna.getText().toString(),
                            binding.etJumlahkursi.getText().toString(),
                            binding.etHarga.getText().toString());
                    binding.progressBar.setProgress(0);
                    binding.progressBar.setVisibility(INVISIBLE);
                    Toast.makeText(AdminDetailMobil.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    binding.progressBar.setVisibility(VISIBLE);
                    double progres = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    binding.progressBar.setProgress((int) progres);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    binding.progressBar.setVisibility(INVISIBLE);
                    Toast.makeText(AdminDetailMobil.this, "Gagal " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            SimpanData(fotoUrl,
                    binding.etPlatnomor.getText().toString(),
                    binding.etStatus.getText().toString(),
                    binding.etNamamerk.getText().toString(),
                    binding.etNamamobil.getText().toString(),
                    binding.etWarna.getText().toString(),
                    binding.etJumlahkursi.getText().toString(),
                    binding.etHarga.getText().toString());
            Toast.makeText(this, "Produk telah diubah", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void hapusData() {
        String platnomor = binding.etPlatnomor.getText().toString();
        firebaseFirestore.collection("RentalMobil").document(produkId).delete();
        storageReference.child(produkId).delete();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference()
                .child("Mobil").child(platnomor);
        firebaseDatabase.removeValue();
        Toast.makeText(this, "Produk telah dihapus", Toast.LENGTH_SHORT).show();
        finish();

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        binding.etStatus.setText(text);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}