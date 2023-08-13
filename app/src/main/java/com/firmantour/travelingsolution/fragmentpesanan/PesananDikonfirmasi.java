package com.firmantour.travelingsolution.fragmentpesanan;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firmantour.travelingsolution.R;
import com.firmantour.travelingsolution.UserDetailPesanan;
import com.firmantour.travelingsolution.databinding.FragmentPesananDibatalkanBinding;
import com.firmantour.travelingsolution.databinding.FragmentPesananDikonfirmasiBinding;
import com.firmantour.travelingsolution.model.ModelPesanan;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;


public class PesananDikonfirmasi extends Fragment {

    private FragmentPesananDikonfirmasiBinding binding;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PesananDikonfirmasi() {
        // Required empty public constructor
    }


    public static PesananDikonfirmasi newInstance(String param1, String param2) {
        PesananDikonfirmasi fragment = new PesananDikonfirmasi();
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
        binding = FragmentPesananDikonfirmasiBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getData();

        return view;
    }
    private void getData() {
        TextView nomor = getActivity().findViewById(R.id.tvNomorTelepon);
        String notlep = nomor.getText().toString();
        Query query = db.collection("Pemesanan").whereEqualTo("nomortelepon", notlep)
                .whereEqualTo("statuspesanan","Sedang Disewa");
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
            protected void onBindViewHolder(@NonNull ProdukHolder holder, int position, @NonNull final ModelPesanan model) {
                holder.nama.setText(model.getNama());
                holder.nomortelepon.setText(model.getNomortelepon());
                holder.namamobil.setText(model.getNamamobil());
                holder.platnomor.setText(model.getPlatnomor());
                holder.tanggalsewa.setText(model.getTanggalsewa());
                holder.tanggalkembali.setText(model.getTanggalkembali());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), UserDetailPesanan.class);
                        intent.putExtra("key", model.getKey());
                        intent.putExtra("status", "Sedang Disewa");
                        startActivity(intent);
//Snackbar.make(recyclerView, model.getNama()+", " +model.getTelepon(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                });
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                Log.e("Ditemukan Error: ", e.getMessage());
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