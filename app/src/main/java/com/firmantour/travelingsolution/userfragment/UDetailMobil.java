package com.firmantour.travelingsolution.userfragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firmantour.travelingsolution.R;
import com.firmantour.travelingsolution.UserDetaillMobil;
import com.firmantour.travelingsolution.adminfragment.ALaporan;
import com.firmantour.travelingsolution.adminfragment.ARentalMobil;
import com.firmantour.travelingsolution.databinding.FragmentUDetailMobilBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class UDetailMobil extends Fragment {

    private FragmentUDetailMobilBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private Uri filePath;
    private String fotoUrl, produkId;
    private static final int IMAGE_REQUEST = 1;

    public UDetailMobil() {
        // Required empty public constructor
    }

    public static UDetailMobil newInstance(String param1, String param2) {
        UDetailMobil fragment = new UDetailMobil();
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
        binding = FragmentUDetailMobilBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        firebaseFirestore   = FirebaseFirestore.getInstance();
        storageReference    = FirebaseStorage.getInstance().getReference();
        Bundle args = getArguments();
        if (args != null) {
            String getKey = args.getString("keyplatnomor");
            produkId = getKey;
        }
        readData();

        binding.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment aRentalMobil = new ARentalMobil();
                replaceFragment(aRentalMobil);
            }
        });


        return view;
    }

    private void readData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        firebaseFirestore.collection("RentalMobil").whereEqualTo("platnomor", produkId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                binding.etPlatnomor.setText(document.getString("platnomor"));
                                binding.etStatus.setText(document.getString("status"));
                                binding.etNamamerk.setText(document.getString("namamerk"));
                                binding.etNamamobil.setText(document.getString("namamobil"));
                                binding.etWarna.setText(document.getString("warna"));
                                binding.etJumlahkursi.setText(document.getString("kursi"));
                                binding.etHarga.setText(document.getString("harga"));
                                fotoUrl = document.getString("foto");
                                if (fotoUrl != "") {
                                    Picasso.get().load(fotoUrl).fit().into(binding.imageView);
                                } else {
                                    Picasso.get().load(R.drawable.ic_user).fit().into(binding.imageView);
                                }
                            }
                        } else {
                            Toast.makeText(getContext(), "Gagal Mengambil Document", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        binding.progressBar.setVisibility(View.GONE);
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}