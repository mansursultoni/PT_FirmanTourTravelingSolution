package com.firmantour.travelingsolution;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class AdminTambahWisata extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    ImageView FotoProduk, TombolKembali;
    Spinner spinner;
    EditText EtKodeWisata, EtNamaWisata, EtHarga;
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
        setContentView(R.layout.activity_admintambahwisata);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));


        FotoProduk      = findViewById(R.id.imageView);
        EtKodeWisata = findViewById(R.id.et_kodewisata);
        EtNamaWisata = findViewById(R.id.et_namawisata);
        EtHarga = findViewById(R.id.et_harga);
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
    private void SimpanData(String foto, String kodewisata,String namawisata,  String harga){
        Map<String, Object> data = new HashMap<>();
        data.put("foto", foto);
        data.put("kodewisata", kodewisata);
        data.put("namawisata", namawisata);
        data.put("harga", harga);
        firebaseFirestore.collection("PaketWisata").document(kodewisata).set(data).isSuccessful();
    }
    private void ambilGambar(){
        ImagePicker.with(AdminTambahWisata.this)
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
            String kodewisata = EtKodeWisata.getText().toString();
            DocumentReference docRef = firebaseFirestore.collection("PaketWisata").document(kodewisata);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Toast.makeText(AdminTambahWisata.this, "Data wisata dengan kode "+kodewisata+" sudah ada.", Toast.LENGTH_LONG).show();
                        } else {
                            final StorageReference ref = storageReference.child(EtNamaWisata.getText().toString());
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
                                            EtKodeWisata.getText().toString(),
                                            EtNamaWisata.getText().toString(),
                                            EtHarga.getText().toString());
                                    progressBar.setProgress(0);
                                    progressBar.setVisibility(View.INVISIBLE);

                                    //--SimpanData Realtime Database
                                    String kodewisata = EtKodeWisata.getText().toString();
                                    databaseReference.child("PaketWisata").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.hasChild(kodewisata)){
                                                Toast.makeText(AdminTambahWisata.this, "Kode Wisata Sudah Terdaftar.", Toast.LENGTH_SHORT).show();
                                            }else {
                                                String txtkodewisata = EtKodeWisata.getText().toString();
                                                String txtnamawisata = EtNamaWisata.getText().toString();
                                                String txtharga = EtHarga.getText().toString();
                                                databaseReference.child("PaketWisata").child(kodewisata).setValue(new ModelWisata2(txtkodewisata,txtnamawisata,txtharga)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(AdminTambahWisata.this, "Data berhasil ditambah.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(AdminTambahWisata.this, "Gagal menyimpan data.", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(AdminTambahWisata.this, "Failed " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } else {
                    }
                }
            });

        }
    }
}