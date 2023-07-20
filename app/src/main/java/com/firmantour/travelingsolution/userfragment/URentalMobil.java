package com.firmantour.travelingsolution.userfragment;

import static com.firmantour.travelingsolution.R.drawable.ic_user;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firmantour.travelingsolution.Adapter;
import com.firmantour.travelingsolution.R;
import com.firmantour.travelingsolution.databinding.FragmentURentalMobilBinding;
import com.firmantour.travelingsolution.model.ModelMobil;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class URentalMobil extends Fragment {

    private FragmentURentalMobilBinding binding;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore db;
    private Adapter adapterMobil;
    private String dataFragment;

    private OnDataSendListener dataSendListener;

    public interface OnDataSendListener {
        String onDataReceived(String data);
    }



    public URentalMobil() {
        // Required empty public constructor
    }

    public static URentalMobil newInstance(String param1, String param2) {
        URentalMobil fragment = new URentalMobil();
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
        binding = FragmentURentalMobilBinding.inflate(inflater, container, false);
        View view  = binding.getRoot();

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getParentFragmentManager().popBackStackImmediate();
                    return true;
                }
                return false;
            }
        });

        db = FirebaseFirestore.getInstance();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.addItemDecoration(decoration);
        binding.recyclerView.setAdapter(adapterMobil);

        getData();


        return view;
    }
    private void getData() {
        binding.progressBar.setVisibility(View.VISIBLE);
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
            public void onError(@NonNull FirebaseFirestoreException e) {
                Log.e("Ditemukan Error: ", e.getMessage());
            }

            @Override
            protected void onBindViewHolder(@NonNull ProdukHolder holder, int position, @NonNull ModelMobil model) {
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
                        Fragment uDetailMobil = new UDetailMobil();
                        replaceFragment(uDetailMobil);

                        String key = model.getPlatnomor();
                        dataFragment = key;
                        sendDataToActivityOnClick();
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        binding.recyclerView.setAdapter(adapter);
        binding.progressBar.setVisibility(View.GONE);
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
    private void keyPlatNomor(String data) {
        UDetailMobil uDetailMobil = new UDetailMobil();
        Bundle args = new Bundle();
        args.putString("keyplatnomor", data);
        uDetailMobil.setArguments(args);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, uDetailMobil)
                .commit();
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnDataSendListener) {
            dataSendListener = (OnDataSendListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnDataSendListener");
        }
    }
    private void sendDataToActivity(String data) {
        if (dataSendListener != null) {
            dataSendListener.onDataReceived(data);
        }
    }
    private void sendDataToActivityOnClick() {
        String data = dataFragment;
        sendDataToActivity(data);
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}