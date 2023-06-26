package com.firmantour.travelingsolution;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UserDaftar extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference storageReference;
    TextInputEditText nama, nomor, tanggalLahir, jenisKelamin, alamat, password, password2;
    TextView sebagai;
    ImageButton kembali, editTanggal;
    Button daftar;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    int tahun, bulan, tanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdaftar);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        Spinner spinner = findViewById(R.id.spinnerJenisKelamin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.jeniskelamin,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        sebagai = findViewById(R.id.tv_user);
        nama = findViewById(R.id.etNama);
        nomor = findViewById(R.id.etNomor);
        tanggalLahir = findViewById(R.id.etTanggalLahir);
        jenisKelamin = findViewById(R.id.etJenisKelamin);
        alamat = findViewById(R.id.etAlamat);
        password = findViewById(R.id.etPassword);
        password2 = findViewById(R.id.etPassword2);
        editTanggal = findViewById(R.id.ibEditCalendar);
        daftar = findViewById(R.id.btnDaftar);
        kembali = findViewById(R.id.imgKembali);

        Intent intent = getIntent();
        if (intent!=null){
            nama.setText(intent.getStringExtra("nama"));
            nomor.setText(intent.getStringExtra("nomor"));
            tanggalLahir.setText(intent.getStringExtra("tanggallahir"));
            jenisKelamin.setText(intent.getStringExtra("jeniskelamin"));
            alamat.setText(intent.getStringExtra("alamat"));
            password.setText(intent.getStringExtra("password"));
        }

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDaftar.this, ActivityLogin.class));
                finish();
            }
        });
        editTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                tahun = calendar.get(Calendar.YEAR);
                bulan = calendar.get(Calendar.MONTH);
                tanggal = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog;
                dialog = new DatePickerDialog(UserDaftar.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tahun = year;
                        bulan = month+1;
                        tanggal = dayOfMonth;

                        tanggalLahir.setText(tanggal + " - " + bulan + " - " + tahun );
                    }
                },tahun, bulan, tanggal);
                dialog.show();
            }
        });
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtsebagai = sebagai.getText().toString();
                String txtnama = nama.getText().toString();
                String txtnomor = nomor.getText().toString();
                String txttanggallahir = tanggalLahir.getText().toString();
                String txtjeniskelamin = jenisKelamin.getText().toString();
                String txtalamat = alamat.getText().toString();
                String txtpassword = password.getText().toString();
                String txtpassword2 = password2.getText().toString();

                if (txtnama.isEmpty() || txtnomor.isEmpty() || txttanggallahir.isEmpty() || txtjeniskelamin.isEmpty() ||
                        txtalamat.isEmpty() || txtpassword.isEmpty()){
                    Toast.makeText(UserDaftar.this, "Data harus lengkap.", Toast.LENGTH_SHORT).show();
                }else {
                    databaseReference.child("Login").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(txtnomor)){
                                Toast.makeText(UserDaftar.this, "Nomor telepon telah terdaftar.", Toast.LENGTH_SHORT).show();
                            }else {
                                if (!txtpassword2.equals(txtpassword)){
                                    Toast.makeText(UserDaftar.this, "Masukkan ulang password.", Toast.LENGTH_SHORT).show();
                                }else {
                                    databaseReference.child("Login").child(txtnomor).setValue(new ModelUser(txtsebagai, txtnama, txtnomor, txttanggallahir, txtjeniskelamin, txtalamat, txtpassword)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            saveData(nama.getText().toString(),
                                                    nomor.getText().toString(),
                                                    tanggalLahir.getText().toString(),
                                                    jenisKelamin.getText().toString(),
                                                    alamat.getText().toString(),
                                                    password.getText().toString());
                                            Toast.makeText(UserDaftar.this, "Pendaftaran berhasil, silahkan login.", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(UserDaftar.this, "Gagal menyimpan data.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }
        });


    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        jenisKelamin.setText(text);
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void saveData(String nama, String nomor, String tanggallahir, String jeniskelamin, String alamat, String password){
        Map<String, Object> user = new HashMap<>();
        user.put("nama", nama);
        user.put("nomor", nomor);
        user.put("tanggallahir", tanggallahir);
        user.put("jeniskelamin", jeniskelamin);
        user.put("alamat", alamat);
        user.put("password", password);

        db.collection("Users").document(nomor).set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                        }else{
                        }
                    }
                });
    }
}