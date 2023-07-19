package com.firmantour.travelingsolution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.firmantour.travelingsolution.adminfragment.ADashboard;
import com.firmantour.travelingsolution.adminfragment.ADataUser;
import com.firmantour.travelingsolution.adminfragment.ALaporan;
import com.firmantour.travelingsolution.adminfragment.AMenungguKonfirmasi;
import com.firmantour.travelingsolution.adminfragment.AMobilDisewa;
import com.firmantour.travelingsolution.adminfragment.APaketWisata;
import com.firmantour.travelingsolution.adminfragment.APengaturan;
import com.firmantour.travelingsolution.adminfragment.ARentalMobil;
import com.firmantour.travelingsolution.databinding.ActivityUserDashboardBinding;
import com.firmantour.travelingsolution.userfragment.UPaketWisata;
import com.firmantour.travelingsolution.userfragment.UPemesanan;
import com.firmantour.travelingsolution.userfragment.UPengaturan;
import com.firmantour.travelingsolution.userfragment.URentalMobil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class UserDashboard extends AppCompatActivity{

    private ActivityUserDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        Fragment uRentalMobil = new URentalMobil();
        replaceFragment(uRentalMobil);

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.rentalMobil:
                        Fragment uRentalMobil = new URentalMobil();
                        replaceFragment(uRentalMobil);
                        return true;
                    case R.id.paketWisata:
                        Fragment uPaketWisata = new UPaketWisata();
                        replaceFragment(uPaketWisata);
                        return true;
                    case R.id.pemesanan:
                        Fragment uPemesanan = new UPemesanan();
                        replaceFragment(uPemesanan);
                        return true;
                    case R.id.pengaturan:
                        Fragment uPengaturan = new UPengaturan();
                        replaceFragment(uPengaturan);
                        return true;
                }
                return false;
            }
        });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}