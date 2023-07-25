package com.firmantour.travelingsolution.adminfragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firmantour.travelingsolution.ActivityLogin;
import com.firmantour.travelingsolution.AdminDashboard2;
import com.firmantour.travelingsolution.AdminDetailPesanan;
import com.firmantour.travelingsolution.R;
import com.firmantour.travelingsolution.databinding.FragmentAMobilDisewaBinding;
import com.firmantour.travelingsolution.model.ModelPesanan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;
import java.util.PrimitiveIterator;


public class AMobilDisewa extends Fragment {

    private FragmentAMobilDisewaBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    String produkId;

    public AMobilDisewa() {
        // Required empty public constructor
    }

    public static AMobilDisewa newInstance(String param1, String param2) {
        AMobilDisewa fragment = new AMobilDisewa();
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
        // Inflate the layout for this fragment
        binding = FragmentAMobilDisewaBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        firebaseFirestore = FirebaseFirestore.getInstance();

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        getData();

        return view;
    }
    private void getData() {
        Query query = firebaseFirestore.collection("Pemesanan").whereEqualTo("statuspesanan","Sedang Disewa");
        FirestoreRecyclerOptions<ModelPesanan> response = new FirestoreRecyclerOptions.Builder<ModelPesanan>()
                .setQuery(query, ModelPesanan.class).build();
        adapter = new FirestoreRecyclerAdapter<ModelPesanan, ProdukHolder>(response) {
            @NonNull
            @Override
            public ProdukHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pesanan, parent, false);
                return new ProdukHolder(view);
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                Log.e("Ditemukan Error: ", e.getMessage());
            }

            @Override
            protected void onBindViewHolder(@NonNull ProdukHolder holder, int position, @NonNull ModelPesanan model) {
                holder.nama.setText(model.getNama());
                holder.nomortelepon.setText(model.getNomortelepon());
                holder.namamobil.setText(model.getNamamobil());
                holder.platnomor.setText(model.getPlatnomor());
                holder.tanggalsewa.setText(model.getTanggalsewa());
                holder.tanggalkembali.setText(model.getTanggalkembali());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        produkId = model.getKey();
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        alertDialog.setTitle("Selesaikan Pesanan?");
                        alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selesaiDisewa();
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
            }
        };
        adapter.notifyDataSetChanged();
        binding.recyclerView.setAdapter(adapter);
    }
    public class ProdukHolder extends RecyclerView.ViewHolder {
        TextView nama, nomortelepon, namamobil, platnomor, tanggalsewa, tanggalkembali;

        public ProdukHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.tvNama);
            nomortelepon = itemView.findViewById(R.id.tvNomorTelepon);
            namamobil = itemView.findViewById(R.id.tvNamaMobil);
            platnomor = itemView.findViewById(R.id.tvPlatNomor);
            tanggalsewa = itemView.findViewById(R.id.tvTanggalSewa);
            tanggalkembali = itemView.findViewById(R.id.tvTanggalKembali);
        }

    }
    public void selesaiDisewa(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String key = produkId;

        Map<String, Object> updateStatus = new HashMap<>();
        updateStatus.put("statuspesanan", "Selesai");
        DocumentReference userRef = db.collection("Pemesanan").document(key);
        userRef.update(updateStatus)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Mobil Selesai Disewa", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Gagal.", Toast.LENGTH_SHORT).show();
                            // Failed to update data
                            // Handle the error
                        }
                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    public void onResume() {
        super.onResume();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}