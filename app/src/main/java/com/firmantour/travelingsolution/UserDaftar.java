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

import com.firmantour.travelingsolution.model.ModelUser;
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
import java.util.regex.Pattern;

public class UserDaftar extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference storageReference;
    TextInputEditText Nama, NomorTelepon, TanggalLahir, JenisKelamin, Alamat, Password, Password2;
    TextView Sebagai;
    ImageButton Kembali, Tanggal;
    Button Daftar;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$";
    Pattern pattern;


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

        Sebagai = findViewById(R.id.tv_user);
        Nama = findViewById(R.id.etNama);
        NomorTelepon = findViewById(R.id.etNomorTelepon);
        TanggalLahir = findViewById(R.id.etTanggalLahir);
        JenisKelamin = findViewById(R.id.etJenisKelamin);
        Alamat = findViewById(R.id.etAlamat);
        Password = findViewById(R.id.etPassword);
        Password2 = findViewById(R.id.etPassword2);
        Tanggal = findViewById(R.id.ibEditCalendar);
        Daftar = findViewById(R.id.btnDaftar);
        Kembali = findViewById(R.id.imgKembali);

        Intent intent = getIntent();
        if (intent!=null){
            Nama.setText(intent.getStringExtra("nama"));
            NomorTelepon.setText(intent.getStringExtra("nomortelepon"));
            TanggalLahir.setText(intent.getStringExtra("tanggallahir"));
            JenisKelamin.setText(intent.getStringExtra("jeniskelamin"));
            Alamat.setText(intent.getStringExtra("alamat"));
            Password.setText(intent.getStringExtra("password"));
        }

        Kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDaftar.this, ActivityLogin.class));
                finish();
            }
        });
        Tanggal.setOnClickListener(new View.OnClickListener() {
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

                        TanggalLahir.setText(tanggal + " - " + bulan + " - " + tahun );
                    }
                },tahun, bulan, tanggal);
                dialog.show();
            }
        });

        pattern = Pattern.compile(PASSWORD_PATTERN);

        Daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = Password.getText().toString();
                validatePassword(password);
            }
        });


    }

    private void validatePassword(String password) {
        if (pattern.matcher(password).matches()) {
            // Password is valid
            String txtsebagai = Sebagai.getText().toString();
            String txtnama = Nama.getText().toString();
            String txtnomor = NomorTelepon.getText().toString();
            String txttanggallahir = TanggalLahir.getText().toString();
            String txtjeniskelamin = JenisKelamin.getText().toString();
            String txtalamat = Alamat.getText().toString();
            String txtpassword = Password.getText().toString();
            String txtpassword2 = Password2.getText().toString();

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
                                        saveData(Nama.getText().toString(),
                                                NomorTelepon.getText().toString(),
                                                TanggalLahir.getText().toString(),
                                                JenisKelamin.getText().toString(),
                                                Alamat.getText().toString(),
                                                Password.getText().toString());
                                        Toast.makeText(UserDaftar.this, "Pendaftaran berhasil, silahkan login.", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UserDaftar.this, "Gagal menyimpan data.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

        } else {
            // Password is invalid
            Toast.makeText(this, "Password minimal 6 karakter dengan huruf besar, huruf kecil, dan angka.", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        JenisKelamin.setText(text);
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void saveData(String nama, String nomor, String tanggallahir, String jeniskelamin, String alamat, String password){
        Map<String, Object> user = new HashMap<>();
        user.put("nama", nama);
        user.put("nomortelepon", nomor);
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