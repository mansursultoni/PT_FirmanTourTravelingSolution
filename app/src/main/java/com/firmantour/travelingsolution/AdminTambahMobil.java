package com.firmantour.travelingsolution;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firmantour.travelingsolution.model.ModelMobil2;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AdminTambahMobil extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    ImageView FotoProduk, TombolKembali;
    Spinner spinner;
    EditText TextNama, TextNomor, TextHarga, TextWarna, TextStatus, TextKursi, TextMerk;
    TextView JumlahMobil, JumlahMobil2, JumlahMobil3;
    Button TombolSimpan;
    ProgressBar progressBar;
    private Uri filePath;
    private String fotoUrl;
    private static final int IMAGE_REQUEST = 1;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pt-firman-tour-default-rtdb.firebaseio.com/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admintambahmobil);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        spinner = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.status, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        FotoProduk      = findViewById(R.id.imageView);
        TextNomor       = findViewById(R.id.et_platnomor);
        TextMerk        = findViewById(R.id.et_namamerk);
        TextNama        = findViewById(R.id.et_namamobil);
        TextWarna       = findViewById(R.id.et_warna);
        TextStatus      = findViewById(R.id.et_status);
        TextKursi       = findViewById(R.id.et_jumlahkursi);
        TextHarga       = findViewById(R.id.et_harga);
        TombolKembali   = findViewById(R.id.ib_back);
        TombolSimpan    = findViewById(R.id.bt_update);
        progressBar     = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        firebaseFirestore   = FirebaseFirestore.getInstance();
        storageReference    = FirebaseStorage.getInstance().getReference();
        FotoProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ambilGambar();
            }
        });
        TombolSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }



        });
        TombolKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void SimpanData(String foto, String platnomor, String status, String namamerk, String namamobil, String warna, String kursi, String harga){
        Map<String, Object> data = new HashMap<>();
        data.put("foto", foto);
        data.put("platnomor", platnomor);
        data.put("status", status);
        data.put("namamerk", namamerk);
        data.put("namamobil", namamobil);
        data.put("warna", warna);
        data.put("kursi", kursi);
        data.put("harga", harga);
        firebaseFirestore.collection("RentalMobil").document(platnomor).set(data).isSuccessful();
    }
    private void ambilGambar(){
        ImagePicker.with(AdminTambahMobil.this)
                .crop(1f,1f)
                .start();
       /* Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"),IMAGE_REQUEST);*/
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        filePath = data.getData();
        FotoProduk.setImageURI(filePath);

        /* if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null ){
            filePath = data.getData();
            Picasso.get().load(filePath).fit().centerCrop().into(FotoProduk);
        }else{
            Toast.makeText(this, "Tidak ada gambar dipilih", Toast.LENGTH_SHORT).show();
        }*/
    }
    private void uploadImage(){
        if(filePath != null){
            String platnomor = TextNomor.getText().toString();
            DocumentReference docRef = firebaseFirestore.collection("RentalMobil").document(platnomor);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Toast.makeText(AdminTambahMobil.this, "Data mobil dengan plat nomor "+platnomor+" sudah ada.", Toast.LENGTH_LONG).show();
                        } else {
                            final StorageReference ref = storageReference.child(TextNomor.getText().toString());
                            UploadTask uploadTask = ref.putFile(filePath);
                            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    return ref.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri imagePath = task.getResult();
                                    fotoUrl = imagePath.toString();
                                    SimpanData(fotoUrl,
                                            TextNomor.getText().toString(),
                                            TextStatus.getText().toString(),
                                            TextMerk.getText().toString(),
                                            TextNama.getText().toString(),
                                            TextWarna.getText().toString(),
                                            TextKursi.getText().toString(),
                                            TextHarga.getText().toString());
                                    progressBar.setProgress(0);
                                    progressBar.setVisibility(View.INVISIBLE);

                                    //--SimpanData Realtime Database
                                    String nomor = TextNomor.getText().toString();
                                    databaseReference.child("Mobil").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.hasChild(nomor)){
                                                Toast.makeText(AdminTambahMobil.this, "Plat Nomor Sudah Terdaftar.", Toast.LENGTH_SHORT).show();
                                            }else {
                                                String txtplat = TextNomor.getText().toString();
                                                String txtmerk = TextMerk.getText().toString();
                                                String txtnama = TextNama.getText().toString();
                                                String txtwarna = TextWarna.getText().toString();
                                                String txtkursi = TextKursi.getText().toString();
                                                String txtharga = TextHarga.getText().toString();
                                                databaseReference.child("Mobil").child(nomor).setValue(new ModelMobil2(txtplat, txtmerk, txtnama, txtwarna, txtkursi, txtharga)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(AdminTambahMobil.this, "Data berhasil ditambah.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(AdminTambahMobil.this, "Gagal menyimpan data.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    finish();
                                }
                            });
                            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    double progres = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                                    progressBar.setProgress((int)progres);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(AdminTambahMobil.this, "Failed " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } else {
                    }
                }
            });

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        TextStatus.setText(text);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}