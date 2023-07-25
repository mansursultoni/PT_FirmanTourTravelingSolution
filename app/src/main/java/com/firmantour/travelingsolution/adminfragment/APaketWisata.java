package com.firmantour.travelingsolution.adminfragment;

import static com.firmantour.travelingsolution.R.drawable.ic_user;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.firmantour.travelingsolution.AdminDetailWisata;
import com.firmantour.travelingsolution.AdminTambahWisata;
import com.firmantour.travelingsolution.R;
import com.firmantour.travelingsolution.databinding.FragmentAPaketWisataBinding;
import com.firmantour.travelingsolution.model.ModelUser;
import com.firmantour.travelingsolution.model.ModelWisata;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class APaketWisata extends Fragment {

    private FragmentAPaketWisataBinding binding;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore db;
    private Adapter adapterMobil;

    private List<ModelUser> list = new ArrayList<>();

    public APaketWisata() {
        // Required empty public constructor
    }

    public static APaketWisata newInstance(String param1, String param2) {
        APaketWisata fragment = new APaketWisata();
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
        binding = FragmentAPaketWisataBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        db = FirebaseFirestore.getInstance();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.addItemDecoration(decoration);
        binding.recyclerView.setAdapter(adapterMobil);

        getData();

        binding.btnTambahWisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AdminTambahWisata.class));
            }
        });

        return view;
    }
    private void getData() {
        Query query = db.collection("PaketWisata");
        FirestoreRecyclerOptions<ModelWisata> response = new FirestoreRecyclerOptions.Builder<ModelWisata>()
                .setQuery(query, ModelWisata.class).build();
        adapter = new FirestoreRecyclerAdapter<ModelWisata, ProdukHolder>(response) {
            @NonNull
            @Override
            public ProdukHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wisata, parent, false);
                return new ProdukHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProdukHolder holder, int position, @NonNull final ModelWisata model) {
                if (model.getFoto() != null) {
                    Picasso.get().load(model.getFoto()).fit().into(holder.fotoProduk);
                } else {
                    Picasso.get().load(ic_user).fit().into(holder.fotoProduk);
                }
                holder.namaProduk.setText(model.getNamawisata());
                holder.hargaProduk.setText(model.getHarga());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), AdminDetailWisata.class);
                        intent.putExtra("nomor", model.getKodewisata());
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
        TextView namaProduk, hargaProduk;

        public ProdukHolder(@NonNull View itemView) {
            super(itemView);
            fotoProduk = itemView.findViewById(R.id.imageViewFoto);
            namaProduk = itemView.findViewById(R.id.tvNama);
            hargaProduk = itemView.findViewById(R.id.textViewHarga);
        }
    }
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