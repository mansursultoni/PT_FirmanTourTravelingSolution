package com.firmantour.travelingsolution.userfragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firmantour.travelingsolution.AdminDetailMobil;
import com.firmantour.travelingsolution.R;
import com.firmantour.travelingsolution.UserDetaillMobil;
import com.firmantour.travelingsolution.UserPemesanan;
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

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Fragment uRentalMobil = new URentalMobil();
                    replaceFragment(uRentalMobil);
                    return true;
                }
                return false;
            }
        });

        firebaseFirestore   = FirebaseFirestore.getInstance();
        storageReference    = FirebaseStorage.getInstance().getReference();


        readData();

        binding.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment uRentalMobil = new URentalMobil();
                replaceFragment(uRentalMobil);
            }
        });

        binding.buttonPemesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserPemesanan.class);
                intent.putExtra("platnomor", binding.etPlatnomor.getText().toString());
                startActivity(intent);
            }
        });


        return view;
    }

    private void readData() {
        Bundle args = getArguments();
        if (args != null) {
            String data = args.getString("dataKey");
            produkId = data;
        }
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
    private void keyPlatNomor(String data) {
        UPemesanan uPemesanan = new UPemesanan();
        Bundle args = new Bundle();
        args.putString("keyplatnomor", data);
        uPemesanan.setArguments(args);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, uPemesanan)
                .commit();
    }
    @Override
    public void onStart() {
        super.onStart();
        readData();
    }
    @Override
    public void onResume() {
        super.onResume();
        readData();;
    }
    @Override
    public void onStop() {
        super.onStop();
        readData();
    }
}