package com.firmantour.travelingsolution.adminfragment.ActivityPengaturanAdmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.firmantour.travelingsolution.R;
import com.firmantour.travelingsolution.databinding.ActivityAdminUbahDataAdminBinding;

public class Admin_UbahDataAdmin extends AppCompatActivity {

    private ActivityAdminUbahDataAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminUbahDataAdminBinding.inflate(getLayoutInflater());
        binding.getRoot();

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));



    }
    private void ambilDataIntent(){
        Intent intent = getIntent();
        String nomorTelpon = intent.getStringExtra("nomortelepon");
    }
}