package com.firmantour.travelingsolution.adminfragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firmantour.travelingsolution.R;
import com.firmantour.travelingsolution.databinding.FragmentAPengaturanBinding;

public class APengaturan extends Fragment {

    private FragmentAPengaturanBinding binding;

    public APengaturan() {
        // Required empty public constructor
    }

    public static APengaturan newInstance(String param1, String param2) {
        APengaturan fragment = new APengaturan();
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
        binding = FragmentAPengaturanBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.btnUbahAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment aUbahAdmin = new AUbahAdmin();
                replaceFragment(aUbahAdmin);
            }
        });

        binding.btnUbahRekening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment aUbahRekening = new AUbahRekening();
                replaceFragment(aUbahRekening);
            }
        });

        binding.btnTambahAnggota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment aTambahAnggota = new ATambahAnggota();
                replaceFragment(aTambahAnggota);
            }
        });

        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}