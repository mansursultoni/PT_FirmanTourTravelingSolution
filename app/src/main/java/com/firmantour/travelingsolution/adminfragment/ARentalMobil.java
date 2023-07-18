package com.firmantour.travelingsolution.adminfragment;

import static com.firmantour.travelingsolution.R.drawable.ic_user;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firmantour.travelingsolution.Adapter;
import com.firmantour.travelingsolution.AdminDetailMobil;
import com.firmantour.travelingsolution.AdminRentalMobil;
import com.firmantour.travelingsolution.AdminTambahMobil;
import com.firmantour.travelingsolution.R;
import com.firmantour.travelingsolution.databinding.FragmentARentalMobilBinding;
import com.firmantour.travelingsolution.model.ModelMobil;
import com.firmantour.travelingsolution.model.ModelUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ARentalMobil extends Fragment {

    private FragmentARentalMobilBinding binding;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore db;
    private Adapter adapterMobil;

    private List<ModelUser> list = new ArrayList<>();

    public ARentalMobil() {
        // Required empty public constructor
    }

    public static ARentalMobil newInstance(String param1, String param2) {
        ARentalMobil fragment = new ARentalMobil();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentARentalMobilBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        db = FirebaseFirestore.getInstance();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.addItemDecoration(decoration);
        binding.recyclerView.setAdapter(adapterMobil);

        getData();

        binding.btnTambahMobil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AdminTambahMobil.class));
            }
        });

        return view;
    }
    private void getData() {
        Query query = db.collection("RentalMobil");
        FirestoreRecyclerOptions<ModelMobil> response = new FirestoreRecyclerOptions.Builder<ModelMobil>()
                .setQuery(query, ModelMobil.class).build();
        adapter = new FirestoreRecyclerAdapter<ModelMobil, ProdukHolder>(response) {
            @NonNull
            @Override
            public ProdukHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mobil, parent, false);
                return new ProdukHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProdukHolder holder, int position, @NonNull final ModelMobil model) {
                if (model.getFoto() != null) {
                    Picasso.get().load(model.getFoto()).fit().into(holder.fotoProduk);
                } else {
                    Picasso.get().load(ic_user).fit().into(holder.fotoProduk);
                }
                holder.namaProduk.setText(model.getNamamobil());
                holder.hargaProduk.setText(model.getHarga());
                holder.statusProduk.setText(model.getStatus());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), AdminDetailMobil.class);
                        intent.putExtra("nomor", model.getPlatnomor());
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
        binding.recyclerView.setAdapter(adapter);
    }
    private class ProdukHolder extends RecyclerView.ViewHolder {
        ImageView fotoProduk;
        TextView namaProduk, hargaProduk, statusProduk;

        public ProdukHolder(@NonNull View itemView) {
            super(itemView);
            fotoProduk = itemView.findViewById(R.id.imageViewFoto);
            namaProduk = itemView.findViewById(R.id.textViewNama);
            hargaProduk = itemView.findViewById(R.id.textViewHarga);
            statusProduk = itemView.findViewById(R.id.textStatus);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        getData();
//        adapter.startListening();
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