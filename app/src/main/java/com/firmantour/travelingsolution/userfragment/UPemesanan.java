package com.firmantour.travelingsolution.userfragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firmantour.travelingsolution.R;
import com.firmantour.travelingsolution.databinding.FragmentUDetailMobilBinding;
import com.firmantour.travelingsolution.databinding.FragmentUPemesananBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;


public class UPemesanan extends Fragment {

    private FragmentUPemesananBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private String produkId;

    public UPemesanan() {
        // Required empty public constructor
    }


    public static UPemesanan newInstance(String param1, String param2) {
        UPemesanan fragment = new UPemesanan();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUPemesananBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        firebaseFirestore   = FirebaseFirestore.getInstance();

        Bundle args = getArguments();
        if (args != null) {
            String getKey = args.getString("keyplatnomor");
            produkId = getKey;
        }

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    UDetailMobil uDetailMobil = new UDetailMobil();
                    replaceFragment(uDetailMobil);
                    return true;
                }
                return false;
            }
        });

        readData();

        binding.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kirimDetail = binding.tvPlatNomor.getText().toString();
                keyPlatNomor(kirimDetail);
            }
        });

        return view;
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
    private void readData() {
        firebaseFirestore.collection("RentalMobil").whereEqualTo("platnomor", produkId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                binding.tvPlatNomor.setText(document.getString("platnomor"));
                                binding.tvNamaMerk.setText(document.getString("namamerk"));
                                binding.tvNamaMobil.setText(document.getString("namamobil"));
                                binding.tvWarna.setText(document.getString("warna"));
                                binding.tvKursi.setText(document.getString("kursi"));
                                binding.tvHarga.setText(document.getString("harga"));
                            }
                        } else {
                            Toast.makeText(getContext(), "Gagal Mengambil Document", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    private void keyPlatNomor(String data) {
        UDetailMobil uDetailMobil = new UDetailMobil();
        Bundle args = new Bundle();
        args.putString("keyplatnomor", data);
        uDetailMobil.setArguments(args);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, uDetailMobil)
                .commit();
    }
}