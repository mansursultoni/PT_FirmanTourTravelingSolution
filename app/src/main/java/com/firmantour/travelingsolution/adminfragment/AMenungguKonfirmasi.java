package com.firmantour.travelingsolution.adminfragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firmantour.travelingsolution.AdminDetailPesanan;
import com.firmantour.travelingsolution.R;
import com.firmantour.travelingsolution.UserDetailPesanan;
import com.firmantour.travelingsolution.UserPesananBelumSelesai;
import com.firmantour.travelingsolution.databinding.FragmentAMenungguKonfirmasiBinding;
import com.firmantour.travelingsolution.model.ModelPesanan;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;


public class AMenungguKonfirmasi extends Fragment {

    private FragmentAMenungguKonfirmasiBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;

    public AMenungguKonfirmasi() {
        // Required empty public constructor
    }

    public static AMenungguKonfirmasi newInstance(String param1, String param2) {
        AMenungguKonfirmasi fragment = new AMenungguKonfirmasi();
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
        binding = FragmentAMenungguKonfirmasiBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        firebaseFirestore = FirebaseFirestore.getInstance();

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        getData();

        return view;
    }
    private void getData() {
        Query query = firebaseFirestore.collection("Pemesanan").whereEqualTo("statuspesanan","Belum Selesai");
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
                        Intent intent = new Intent(getActivity(), AdminDetailPesanan.class);
                        intent.putExtra("key", model.getKey());
                        startActivity(intent);
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