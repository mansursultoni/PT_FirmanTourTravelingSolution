package com.firmantour.travelingsolution;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firmantour.travelingsolution.model.ModelUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UserPemesanan extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    TextView TvID, TvJumlahHari, TvNama, TvNomorTelpon, TvAlamat, TvPlatNomor,
            TvNamaMerk, TvNamaMobil, TvWarna, TvJumlahKursi, TvHarga, TvTotalHarga;
    EditText EtTanggalSewa, EtTanggalKembali;
    Button BtPesan;
    ImageButton IbSewa, IbKembali;
    ArrayList<ModelUser> listUser;
    String platnomor, namamerk, namamobil;
    int tahun, bulan, tanggal;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpemesanan);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        TvID = findViewById(R.id.tv_id);
        TvNama = findViewById(R.id.tv_nama);
        TvNomorTelpon = findViewById(R.id.tv_telepon);
        TvAlamat = findViewById(R.id.tv_alamat);
        TvPlatNomor = findViewById(R.id.tv_platNomor);
        TvNamaMerk = findViewById(R.id.tv_namaMerk);
        TvNamaMobil = findViewById(R.id.tv_namaMobil);
        TvWarna = findViewById(R.id.tv_warna);
        TvJumlahKursi = findViewById(R.id.tv_kursi);
        TvHarga = findViewById(R.id.tv_harga);
        EtTanggalSewa = findViewById(R.id.et_tanggalSewa);
        EtTanggalKembali = findViewById(R.id.et_tanggalKembali);
        IbSewa = findViewById(R.id.ib_sewa);
        IbKembali = findViewById(R.id.ib_kembali);;
        TvJumlahHari = findViewById(R.id.tv_jumlahHari);
        TvTotalHarga =findViewById(R.id.tv_totalHarga);
        BtPesan = findViewById(R.id.bt_pesan);

        firebaseFirestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();

        if (intent!=null){
            String notelpon = intent.getStringExtra("nomortelepon");
            String pltnomor = intent.getStringExtra("platnomor");
            TvNomorTelpon.setText(notelpon);
            TvPlatNomor.setText(pltnomor);
        }





        getUser();
        getMobil();


        IbSewa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EtTanggalKembali.setText("");
                TvJumlahHari.setText("");

                Calendar calendar = Calendar.getInstance();
                tahun = calendar.get(Calendar.YEAR);
                bulan = calendar.get(Calendar.MONTH);
                tanggal = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog;
                dialog = new DatePickerDialog(UserPemesanan.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tahun = year;
                        bulan = month+1;
                        tanggal = dayOfMonth;

                        EtTanggalSewa.setText(tanggal + "-" + bulan + "-" + tahun );
                        String tglSewa = String.valueOf(tanggal);
//                        TvTglSewa.setText(tglSewa);
                    }
                },tahun, bulan, tanggal);
                dialog.show();
            }
        });
        IbKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                tahun = calendar.get(Calendar.YEAR);
                bulan = calendar.get(Calendar.MONTH);
                tanggal = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog;
                dialog = new DatePickerDialog(UserPemesanan.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tahun = year;
                        bulan = month+1;
                        tanggal = dayOfMonth;

                        EtTanggalKembali.setText(tanggal + "-" + bulan + "-" + tahun );
                        String tglKembali = EtTanggalKembali.getText().toString();

                        String id1 = TvNomorTelpon.getText().toString();
                        String id2 = TvPlatNomor.getText().toString();
                        String sDate = EtTanggalSewa.getText().toString();
                        String eDate = EtTanggalKembali.getText().toString();
                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            // converting it to date format
                            Date date1 = simpleDateFormat1.parse(sDate);
                            Date date2 = simpleDateFormat1.parse(eDate);

                            long startdate = date1.getTime();
                            long endDate = date2.getTime();

                            // condition
                            if (startdate <= endDate) {
                                org.joda.time.Period period = new Period(startdate, endDate, PeriodType.yearMonthDay());
                                int years = period.getYears();
                                int months = period.getMonths();
                                int days = period.getDays();

                                // show the final output
                                //TvHasil.setText(years + " Years |" + months + "Months |" + days + "Days");
                                String jumlahHari = String.valueOf(days);
                                TvJumlahHari.setText(jumlahHari);
                            } else {
                                // show message
                                Toast.makeText(UserPemesanan.this, "BirthDate should not be larger than today's date!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        int hargaAwal = Integer.parseInt(TvHarga.getText().toString());
                        int jumlahHari = Integer.parseInt(TvJumlahHari.getText().toString());
                        int plus = 1;
                        int total = hargaAwal*(jumlahHari+1);
                        String id = id2+" "+id1+sDate+eDate;
                        String totalHarga = String.valueOf(total);
                        TvID.setText(id);
                        TvTotalHarga.setText(totalHarga);;
                    }
                },tahun, bulan, tanggal);
                dialog.show();
            }
        });
        BtPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = TvID.getText().toString();
                DocumentReference docRef = firebaseFirestore.collection("PesananBelumSelesai").document(id);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Toast.makeText(UserPemesanan.this, "Pemesanan sudah ada.", Toast.LENGTH_SHORT).show();
                            }else{
                                String id = TvID.getText().toString();
                                String nama = TvNama.getText().toString();
                                String nomor = TvNomorTelpon.getText().toString();
                                String alamat = TvAlamat.getText().toString();
                                String platnomor = TvPlatNomor.getText().toString();
                                String namamerk = TvNamaMerk.getText().toString();
                                String namamobil = TvNamaMobil.getText().toString();
                                String warna = TvWarna.getText().toString();
                                String jumlahkursi = TvJumlahKursi.getText().toString();
                                String totalharga = TvTotalHarga.getText().toString();
                                String sewa = EtTanggalSewa.getText().toString();
                                String kembali = EtTanggalKembali.getText().toString();
                                Intent intent = new Intent(UserPemesanan.this, UserCheckout.class);
                                intent.putExtra("id",id);
                                intent.putExtra("nama",nama);
                                intent.putExtra("nomor",nomor);
                                intent.putExtra("alamat",alamat);
                                intent.putExtra("platnomor",platnomor);
                                intent.putExtra("namamerk",namamerk);
                                intent.putExtra("namamobil",namamobil);
                                intent.putExtra("warna",warna);
                                intent.putExtra("jumlahkursi",jumlahkursi);
                                intent.putExtra("totalharga",totalharga);
                                intent.putExtra("sewa",sewa);
                                intent.putExtra("kembali",kembali);
                                startActivity(intent);
                            }
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserPemesanan.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });


                /*String nama = TvNama.getText().toString();
                String notlep = TvNomorTelpon.getText().toString();
                String alamat = TvAlamat.getText().toString();
                String platnomor = TvPlatNomor.getText().toString();
                String namamerk = TvNamaMerk.getText().toString();
                String namamobil = TvNamaMobil.getText().toString();
                String warna = TvWarna.getText().toString();
                String jumlahkursi = TvJumlahKursi.getText().toString();
                String totalharga = TvTotalHarga.getText().toString();
                String sewa = EtTanggalSewa.getText().toString();
                String kembali = EtTanggalKembali.getText().toString();
                if (sewa.isEmpty() || kembali.isEmpty()){
                    Toast.makeText(UserPemesanan.this, "Masukkan tanggal pemesanan.", Toast.LENGTH_SHORT).show();
                }else{
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserPemesanan.this);
                    alertDialog.setTitle("Detail Pemesanan");
                    alertDialog.setMessage(
                              "Nama Pemesan    : " + nama +
                            "\nNomor Telpon      : " +notlep+
                            "\nAlamat                  : " +alamat+
                            "\n\nDetail Mobil\n"+
                            "\nPlat Nomor           : " +platnomor+
                            "\nNama Merk           : " +namamerk+
                            "\nNama Mobil          : " +namamobil+
                            "\nWarna                    : " +warna+
                            "\nJumlah Kursi         : " +jumlahkursi+
                            "\nTanggal Sewa        : " +sewa+
                            "\nTanggal Kembali   :  " +kembali+
                            "\nTotal Harga            : " +totalharga);
                    alertDialog.setPositiveButton("Pesan", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String nama = TvNama.getText().toString();
                            String notlep = TvNomorTelpon.getText().toString();
                            String alamat = TvAlamat.getText().toString();
                            String platnomor = TvPlatNomor.getText().toString();
                            String namamerk = TvNamaMerk.getText().toString();
                            String namamobil = TvNamaMobil.getText().toString();
                            String warna = TvWarna.getText().toString();
                            String jumlahkursi = TvJumlahKursi.getText().toString();
                            String totalharga = TvTotalHarga.getText().toString();
                            String sewa = EtTanggalSewa.getText().toString();
                            String kembali = EtTanggalKembali.getText().toString();

                            firebaseFirestore.collection("Pesan").document(platnomor).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                                }
                            });

                            database.child("Pemesanan").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    database.child("Pemesanan").push()
                                            .setValue(new ModelPemesanan(nama, notlep, alamat, platnomor, namamerk, namamobil, warna, jumlahkursi, totalharga, sewa, kembali))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(UserPemesanan.this, "Pemesanan berhasil.", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(UserPemesanan.this, "Pemesanan gagal.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            Intent intent = new Intent(UserPemesanan.this,UserRentalMobil.class);
                            startActivity(intent);
                        }
                    });
                    alertDialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }*/

            }
        });

    }

    private void getMobil() {
        final String platnomor = TvPlatNomor.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Mobil");
        Query cekMobil = reference.orderByChild("platnomor").equalTo(platnomor);
        cekMobil.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String nomorplat = dataSnapshot.child(platnomor).child("platnomor").getValue(String.class);
                    String namamerk = dataSnapshot.child(platnomor).child("namamerk").getValue(String.class);
                    String namamobil = dataSnapshot.child(platnomor).child("namamobil").getValue(String.class);
                    String warna = dataSnapshot.child(platnomor).child("warna").getValue(String.class);
                    String jumlahkursi = dataSnapshot.child(platnomor).child("jumlahkursi").getValue(String.class);
                    String harga = dataSnapshot.child(platnomor).child("harga").getValue(String.class);
                    TvPlatNomor.setText(nomorplat);
                    TvNamaMerk.setText(namamerk);
                    TvNamaMobil.setText(namamobil);
                    TvWarna.setText(warna);
                    TvJumlahKursi.setText(jumlahkursi);
                    TvHarga.setText(harga);
                    TvTotalHarga.setText(harga);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUser() {
        final String nomortlep = TvNomorTelpon.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Login");
        Query cekMobil = reference.orderByChild("nomor").equalTo(nomortlep);
        cekMobil.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String nama = dataSnapshot.child(nomortlep).child("nama").getValue(String.class);
                    String alamat = dataSnapshot.child(nomortlep).child("alamat").getValue(String.class);
                    TvNama.setText(nama);
                    TvAlamat.setText(alamat);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}