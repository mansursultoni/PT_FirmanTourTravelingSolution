package com.firmantour.travelingsolution.adminfragment;

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
import com.firmantour.travelingsolution.AdminDetailUser;
import com.firmantour.travelingsolution.R;
import com.firmantour.travelingsolution.databinding.FragmentADataUserBinding;
import com.firmantour.travelingsolution.model.ModelUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ADataUser extends Fragment {

    private FragmentADataUserBinding binding;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<ModelUser> list = new ArrayList<>();
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    RecyclerView RecyclerView;

    public ADataUser() {
        // Required empty public constructor
    }

    public static ADataUser newInstance(String param1, String param2) {
        ADataUser fragment = new ADataUser();
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
        binding = FragmentADataUserBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getData();

        return view;
    }

    private void getData(){
        Query query = db.collection("Users");
        FirestoreRecyclerOptions<ModelUser> response = new FirestoreRecyclerOptions.Builder<ModelUser>()
                .setQuery(query, ModelUser.class).build();
        adapter = new FirestoreRecyclerAdapter<ModelUser, ProdukHolder>(response) {
            @NonNull
            @Override
            public ProdukHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
                return new ProdukHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProdukHolder holder, int position, @NonNull final ModelUser model) {
                holder.nama.setText(model.getNama());
                holder.nomor.setText(model.getNomortelepon());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), AdminDetailUser.class);
                        intent.putExtra("nomor", model.getNomortelepon());
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
        TextView nama, nomor;

        public ProdukHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.tvNama);
            nomor = itemView.findViewById(R.id.textViewNomor);
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