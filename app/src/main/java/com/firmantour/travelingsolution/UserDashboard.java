package com.firmantour.travelingsolution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;

import com.firmantour.travelingsolution.databinding.ActivityUserDashboardBinding;
import com.firmantour.travelingsolution.userfragment.UDetailMobil;
import com.firmantour.travelingsolution.userfragment.UPaketWisata;
import com.firmantour.travelingsolution.userfragment.UPemesanan;
import com.firmantour.travelingsolution.userfragment.UPengaturan;
import com.firmantour.travelingsolution.userfragment.URentalMobil;
import com.google.android.material.navigation.NavigationBarView;

public class UserDashboard extends AppCompatActivity implements URentalMobil.OnDataSendListener{

    private ActivityUserDashboardBinding binding;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        if (savedInstanceState == null) {
            URentalMobil uRentalMobil = new URentalMobil();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, uRentalMobil)
                    .commit();
        }


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
    @Override
    public void onBackPressed() {
        // Get the current fragment
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);

        // Check if it's Fragment C
        if (currentFragment instanceof UDetailMobil) {
            // Navigate back to Fragment B
            Fragment uRentalMobil = new URentalMobil();
            replaceFragment(uRentalMobil);
        }
        else {
            // For any other case, perform the default back press behavior
            super.onBackPressed();
        }
    }
    public String onDataReceived(String data) {
        Log.d("MyActivity", "Received data from Fragment: " + data);

        UDetailMobil uDetailMobil = new UDetailMobil();
        Bundle argsB = new Bundle();
        argsB.putString("dataKey", data);
        uDetailMobil.setArguments(argsB);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, uDetailMobil)
                .addToBackStack(null)
                .commit();


        return data;
    }
    private void keyPlatNomor(String data) {
        UDetailMobil uDetailMobil = new UDetailMobil();
        Bundle args = new Bundle();
        args.putString("keyplatnomor", data);
        uDetailMobil.setArguments(args);
    }
}