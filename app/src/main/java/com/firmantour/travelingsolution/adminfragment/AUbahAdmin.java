package com.firmantour.travelingsolution.adminfragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firmantour.travelingsolution.ActivityLogin;
import com.firmantour.travelingsolution.AdminDashboard2;
import com.firmantour.travelingsolution.R;
import com.firmantour.travelingsolution.databinding.FragmentARentalMobilBinding;
import com.firmantour.travelingsolution.databinding.FragmentAUbahAdminBinding;

public class AUbahAdmin extends Fragment {

    private FragmentAUbahAdminBinding binding;

    public AUbahAdmin() {
        // Required empty public constructor
    }

    public static AUbahAdmin newInstance(String param1, String param2) {
        AUbahAdmin fragment = new AUbahAdmin();
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
        binding = FragmentAUbahAdminBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

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