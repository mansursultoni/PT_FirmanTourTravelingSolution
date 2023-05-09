package com.firmantour.travelingsolution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class DaftarActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    TextInputEditText nama, nomor, tanggalLahir, jenisKelamin, alamat, password, password2;
    ImageButton kembali, editTanggal;
    Button daftar;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://pt-firman-tour-default-rtdb.firebaseio.com/");

    int tahun, bulan, tanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        Spinner spinner = findViewById(R.id.spinnerJenisKelamin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.jeniskelamin,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        nama = findViewById(R.id.etNama);
        nomor = findViewById(R.id.etNomor);
        tanggalLahir = findViewById(R.id.etTanggalLahir);
        jenisKelamin = findViewById(R.id.etJenisKelamin);
        alamat = findViewById(R.id.etAlamat);
        password = findViewById(R.id.etPassword);
        password2 = findViewById(R.id.etPassword2);
        editTanggal = findViewById(R.id.ibEditCalendar);

        daftar = findViewById(R.id.btnDaftar);
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtnama = nama.getText().toString();
                String txtnomor = nomor.getText().toString();
                String txttanggallahir = tanggalLahir.getText().toString();
                String txtjeniskelamin = jenisKelamin.getText().toString();
                String txtalamat = alamat.getText().toString();
                String txtpassword = password.getText().toString();
                String txtpassword2 = password2.getText().toString();

                if (txtnama.isEmpty() || txtnomor.isEmpty() || txttanggallahir.isEmpty() || txtjeniskelamin.isEmpty() ||
                        txtalamat.isEmpty() || txtpassword.isEmpty()){
                    Toast.makeText(DaftarActivity.this, "Data harus lengkap.", Toast.LENGTH_SHORT).show();
                }else {
                    databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(txtnomor)){
                                Toast.makeText(DaftarActivity.this, "Nomor telepon telah terdaftar.", Toast.LENGTH_SHORT).show();
                            }else {
                                databaseReference.child("user").child(txtnomor).child("nama").setValue(txtnama);
                                databaseReference.child("user").child(txtnomor).child("tanggallahir").setValue(txttanggallahir);
                                databaseReference.child("user").child(txtnomor).child("jeniskelamin").setValue(txtjeniskelamin);
                                databaseReference.child("user").child(txtnomor).child("alamat").setValue(txtalamat);
                                databaseReference.child("user").child(txtnomor).child("password").setValue(txtpassword);

                                Toast.makeText(DaftarActivity.this, "Pendaftaran berhasil, silahkan login.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }
        });

        kembali = findViewById(R.id.imgKembali);
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DaftarActivity.this,LoginActivity.class));
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
                dialog = new DatePickerDialog(DaftarActivity.this, new DatePickerDialog.OnDateSetListener() {
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

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        jenisKelamin.setText(text);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}