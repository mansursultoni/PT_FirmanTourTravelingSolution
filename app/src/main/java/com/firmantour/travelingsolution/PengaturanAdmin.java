package com.firmantour.travelingsolution;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.firmantour.travelingsolution.adminfragment.ADashboard;
import com.firmantour.travelingsolution.adminfragment.ATambahAnggota;
import com.firmantour.travelingsolution.adminfragment.AUbahAdmin;
import com.firmantour.travelingsolution.adminfragment.AUbahRekening;
import com.firmantour.travelingsolution.databinding.ActivityPengaturanAdminBinding;

public class PengaturanAdmin extends AppCompatActivity {

    private ActivityPengaturanAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPengaturanAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        binding.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PengaturanAdmin.this, AdminDashboard2.class));
                finish();
            }
        });

    }
}