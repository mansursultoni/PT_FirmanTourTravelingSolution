package com.firmantour.travelingsolution.adminfragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firmantour.travelingsolution.R;
import com.firmantour.travelingsolution.databinding.FragmentAUbahAdminBinding;

public class AUbahAdmin extends Fragment {

    private FragmentAUbahAdminBinding binding;
    private static final String ARG_DATA1 = "data1";
    private static final String ARG_DATA2 = "data2";
    private static final String ARG_DATA3 = "data3";
    private String namaadmin, nomortelepon, password;

    public AUbahAdmin() {
        // Required empty public constructor
    }

    public static AUbahAdmin newInstance(String data1, String data2, String data3) {
        AUbahAdmin fragment = new AUbahAdmin();
        Bundle args = new Bundle();
        args.putString(ARG_DATA1, data1);
        args.putString(ARG_DATA2, data2);
        args.putString(ARG_DATA3, data3);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            namaadmin = getArguments().getString(ARG_DATA1);
            nomortelepon = getArguments().getString(ARG_DATA2);
            password = getArguments().getString(ARG_DATA3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAUbahAdminBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.etNamaAdmin.setText(namaadmin);
        binding.etNomorTelepon.setText(nomortelepon);
        binding.etPassword.setText(password);

        binding.btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Membatallkan perubahan?");
                alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Fragment aPengaturan = new APengaturan();
                        replaceFragment(aPengaturan);
                    }
                });
                alertDialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        binding.btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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