package com.firmantour.travelingsolution;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class AdminDataUser extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<ModelUser> list = new ArrayList<>();
    private Adapter userAdapter;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    RecyclerView RView;
    ImageView imageView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admindatauser);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.blue));

        imageView = findViewById(R.id.ib_menuDrawer);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // --- To open Drawer ---
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        userAdapter = new Adapter(getApplicationContext(), list);
        userAdapter.setDialog(new Adapter.Dialog() {
            @Override
            public void onClick(int pos) {
                final CharSequence[] dialogItem = {"Edit", "Hapus"};
                android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(AdminDataUser.this);
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                Intent intent = new Intent(getApplicationContext(), AdminDetailUser.class);
                                intent.putExtra("nama", list.get(pos).getNama());
                                intent.putExtra("nomor", list.get(pos).getNomor());
                                startActivity(intent);
                                break;
                            case 1:
                                deleteData(list.get(pos).getNomor());
                                break;
                        }
                    }
                });
                dialog.show();
            }
        });



        RView = findViewById(R.id.recycler_view);
        RView.setHasFixedSize(true);
        RView.setLayoutManager(new LinearLayoutManager(this));

        TampilDataUser();
    }

    private void TampilDataUser() {
        database.child("Login").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                /*listUser = new ArrayList<>();
                for (DataSnapshot item : snapshot.getChildren()){
                    ModelUser user = item.getValue(ModelUser.class);
                    user.setNomor(item.getKey());
                    listUser.add(user);
                }
                adapterUser = new AdapterUser(listUser, AdminDataUser.this);
                RView.setAdapter(adapterUser);*/



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.dashboard:
                startActivity(new Intent(AdminDataUser.this, AdminDashboard.class));
                finish();
                return true;
            case R.id.rentalmobil:
                startActivity(new Intent(AdminDataUser.this, AdminRentalMobil.class));
                finish();
                return true;
            case R.id.paketwisata:
                startActivity(new Intent(AdminDataUser.this, AdminPaketWisata.class));
                finish();
                return true;
            case R.id.menunggukonfirmasi:
//                startActivity(new Intent(Dashboard.this, MenungguKonfirmasi.class));
//                finish();
                return true;
            case R.id.dataadmin:
//                startActivity(new Intent(Dashboard.this, DataAdmin.class));
//                finish();
                return true;
            case R.id.datauser:
//                startActivity(new Intent(DataUser.this, DataUser.class));
//                finish();
                return true;
            case R.id.setting:
//                startActivity(new Intent(Dashboard.this, PengaturanAdmin.class));
//                finish();
                return true;
            case R.id.laporan:
//                startActivity(new Intent(Dashboard.this, Laporan.class));
//                finish();
                return true;
            case R.id.logout:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminDataUser.this);
                alertDialog.setTitle("Keluar");
                alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
            default:
                return false;
        }
    }
    private void deleteData(String id){

        db.collection("users").document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Data gagal di hapus!", Toast.LENGTH_SHORT).show();
                        }
                        getData();
                    }
                });
    }
    private void getData(){
        Query query = db.collection("users");
        FirestoreRecyclerOptions<ModelMobil> response = new FirestoreRecyclerOptions.Builder<ModelMobil>()
                .setQuery(query, ModelMobil.class).build();
        adapter = new FirestoreRecyclerAdapter<ModelMobil, ProdukHolder>(response) {
            @NonNull
            @Override
            public ProdukHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
                return new ProdukHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProdukHolder holder, int position, @NonNull final ModelMobil model) {
                holder.nama.setText(model.getNama());
                holder.nomor.setText(model.getNomor());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AdminDataUser.this, AdminDetailUser.class);
                        intent.putExtra("nomor", model.getNomor());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                Log.e("Ditemukan Error: ", e.getMessage());
            }
        };
        adapter.notifyDataSetChanged();
        RView.setAdapter(adapter);
    }
    private class ProdukHolder extends RecyclerView.ViewHolder {
        TextView nama, nomor;
        ConstraintLayout constraintLayout;

        public ProdukHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.textViewNama);
            nomor = itemView.findViewById(R.id.textViewNomor);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        getData();
//        adapter.startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}