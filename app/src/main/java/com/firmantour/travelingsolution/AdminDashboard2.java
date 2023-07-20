package com.firmantour.travelingsolution;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.firmantour.travelingsolution.adminfragment.ADashboard;
import com.firmantour.travelingsolution.adminfragment.ADataUser;
import com.firmantour.travelingsolution.adminfragment.ALaporan;
import com.firmantour.travelingsolution.adminfragment.AMenungguKonfirmasi;
import com.firmantour.travelingsolution.adminfragment.AMobilDisewa;
import com.firmantour.travelingsolution.adminfragment.APaketWisata;
import com.firmantour.travelingsolution.adminfragment.APengaturan;
import com.firmantour.travelingsolution.adminfragment.ARentalMobil;
import com.firmantour.travelingsolution.databinding.ActivityAdminDashboard2Binding;
import com.google.android.material.navigation.NavigationView;

public class AdminDashboard2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityAdminDashboard2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminDashboard2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        // Open fragment ADashboard
        Fragment aDashboard = new ADashboard();
        replaceFragment(aDashboard);

        binding.navigationDrawer.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout,R.string.open,R.string.close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navigationDrawer.setNavigationItemSelectedListener(this);

        binding.ibMenuDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.dashboard:
                Fragment aDashboard = new ADashboard();
                replaceFragment(aDashboard);
                closeDrawer();
                return true;
            case R.id.rentalmobil:
                Fragment aRentalMobil = new ARentalMobil();
                replaceFragment(aRentalMobil);
                closeDrawer();
                return true;
            case R.id.paketwisata:
                Fragment aPaketWisata = new APaketWisata();
                replaceFragment(aPaketWisata);
                closeDrawer();
                return true;
            case R.id.mobildisewa:
                Fragment aMobilDisewa = new AMobilDisewa();
                replaceFragment(aMobilDisewa);
                closeDrawer();
                return true;
            case R.id.menunggukonfirmasi:
                Fragment aMenungguKonfirmasi = new AMenungguKonfirmasi();
                replaceFragment(aMenungguKonfirmasi);
                closeDrawer();
                return true;
            case R.id.datauser:
                Fragment aDatUser = new ADataUser();
                replaceFragment(aDatUser);
                closeDrawer();
                return true;
            case R.id.setting:
                Intent intent = getIntent();
                String ambildata = intent.getExtras().getString("nomortelepon");
                // Create the Fragment instance
                APengaturan fragment = new APengaturan();
                // Pass the data as arguments to the Fragment
                Bundle args = new Bundle();
                args.putString("FRAGMENT_DATA", ambildata);
                fragment.setArguments(args);
                // Perform the Fragment transaction
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, fragment)
                        .commit();



                closeDrawer();
                return true;
            case R.id.laporan:
                Fragment aLaporan = new ALaporan();
                replaceFragment(aLaporan);
                closeDrawer();
                return true;
            case R.id.logout:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminDashboard2.this);
                alertDialog.setTitle("Keluar");
                alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Logout();
                        startActivity(new Intent(AdminDashboard2.this, ActivityLogin.class));
                        finish();
                    }
                });
                alertDialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
                return true;
        }

        return true;
    }
    private void closeDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
    private void Logout(){
        LoginSesson.clearData(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}