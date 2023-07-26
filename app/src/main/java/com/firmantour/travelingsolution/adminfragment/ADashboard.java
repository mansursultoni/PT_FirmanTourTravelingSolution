package com.firmantour.travelingsolution.adminfragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firmantour.travelingsolution.R;
import com.firmantour.travelingsolution.databinding.FragmentADashboardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


public class ADashboard extends Fragment {

    private FragmentADashboardBinding binding;
    private FirebaseFirestore firebaseFirestore;


    public ADashboard() {
        // Required empty public constructor
    }

    public static ADashboard newInstance(String param1, String param2) {
        ADashboard fragment = new ADashboard();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentADashboardBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        firebaseFirestore = FirebaseFirestore.getInstance();

        getJumlahMobil();
        getJumlahUser();
        getJumlahPemesanan();
        getJumlahWisata();
        getMobilDisewa();

        return view;
    }
    private void getJumlahMobil(){
        CollectionReference query = firebaseFirestore.collection("RentalMobil");
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Count fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    binding.tvJumlahmobil.setText(String.valueOf(snapshot.getCount()));
                } else {
                }
            }
        });
    }
    private void getJumlahUser(){
        CollectionReference query = firebaseFirestore.collection("Users");
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Count fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    binding.tvJumlahuser.setText(String.valueOf(snapshot.getCount()));
                } else {

                }
            }
        });
    }
    private void getJumlahPemesanan(){
        CollectionReference pesananCollectionRef = firebaseFirestore.collection("Pemesanan");

        // Query untuk mendapatkan dokumen dengan field "STATUS" yang memiliki nilai "BELUM_SELESAI"
        Task<QuerySnapshot> query = pesananCollectionRef.whereEqualTo("statuspesanan", "Belum Selesai").get();

        // Tambahkan listener untuk menangani hasil query
        query.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Dapatkan jumlah total dokumen yang sesuai dengan kriteria
                    int totalData = task.getResult().size();
                    String total = String.valueOf(totalData);
                    // Tampilkan totalData dalam TextView
                    binding.tvPermintaanpesan.setText(total);
                } else {
                    // Jika query gagal, tampilkan pesan kesalahan
                    binding.tvPermintaanpesan.setText("0");
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }
    private void getJumlahWisata(){
        CollectionReference query = firebaseFirestore.collection("PaketWisata");
        AggregateQuery countQuery = query.count();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Count fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    binding.txtJumlahWisata.setText(String.valueOf(snapshot.getCount()));
                } else {
                    binding.txtJumlahWisata.setText("0");
                }
            }
        });
    }
    private void getMobilDisewa(){
        CollectionReference pesananCollectionRef = firebaseFirestore.collection("Pemesanan");

        // Query untuk mendapatkan dokumen dengan field "STATUS" yang memiliki nilai "BELUM_SELESAI"
        Task<QuerySnapshot> query = pesananCollectionRef.whereEqualTo("statuspesanan", "Sedang Disewa").get();

        // Tambahkan listener untuk menangani hasil query
        query.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Dapatkan jumlah total dokumen yang sesuai dengan kriteria
                    int totalData = task.getResult().size();
                    String total = String.valueOf(totalData);
                    // Tampilkan totalData dalam TextView
                    binding.tvMobildisewa.setText(total);
                } else {
                    // Jika query gagal, tampilkan pesan kesalahan
                    binding.tvMobildisewa.setText("0");
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}