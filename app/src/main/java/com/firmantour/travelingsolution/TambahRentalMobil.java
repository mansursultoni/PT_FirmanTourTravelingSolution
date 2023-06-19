package com.firmantour.travelingsolution;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class TambahRentalMobil extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    ImageView FotoProduk, TombolKembali;
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
        setContentView(R.layout.activity_tambah_rental_mobil);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        Spinner spinner = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.status, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        FotoProduk      = findViewById(R.id.imageView);

        JumlahMobil     = findViewById(R.id.tv_jumlahMobil);
        JumlahMobil2     = findViewById(R.id.tv_jumlahMobil2);
        JumlahMobil3     = findViewById(R.id.tv_jumlahMobil3);

        TextNama        = findViewById(R.id.editTextNama);
        TextNomor       = findViewById(R.id.editTextNomor);
        TextHarga       = findViewById(R.id.editTextHarga);
        TextWarna       = findViewById(R.id.editTextWarna);
        TextStatus      = findViewById(R.id.editTextStatus);
        TextMerk        = findViewById(R.id.editTextNamaMerk);
        TextKursi       = findViewById(R.id.editTextJumlahKursi);
        TombolKembali   = findViewById(R.id.buttonBack);
        TombolSimpan    = findViewById(R.id.buttonUpdate);

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
    private void SimpanData(String nomor, String status, String merk, String nama, String warna, String kursi, String harga, String foto){
        Map<String, Object> data = new HashMap<>();
        data.put("nomor", nomor);
        data.put("status", status);
        data.put("merk", merk);
        data.put("nama", nama);
        data.put("warna", warna);
        data.put("kursi", kursi);
        data.put("harga", harga);
        data.put("foto", foto);
        firebaseFirestore.collection("RentalMobil").document(nomor).set(data).isSuccessful();
    }
    private void ambilGambar(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"),IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null ){
            filePath = data.getData();
            Picasso.get().load(filePath).fit().centerInside().into(FotoProduk);
        }else{
            Toast.makeText(this, "Tidak ada gambar dipilih", Toast.LENGTH_SHORT).show();
        }
    }
    private void uploadImage(){
        if(filePath != null){
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
                    SimpanData(TextNomor.getText().toString(),
                            TextStatus.getText().toString(),
                            TextMerk.getText().toString(),
                            TextNama.getText().toString(),
                            TextWarna.getText().toString(),
                            TextKursi.getText().toString(),
                            TextHarga.getText().toString(),
                            fotoUrl);
                    progressBar.setProgress(0);
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(TambahRentalMobil.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(TambahRentalMobil.this, "Failed " + e.getMessage(), Toast.LENGTH_LONG).show();
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