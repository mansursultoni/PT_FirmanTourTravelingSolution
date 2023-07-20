package com.firmantour.travelingsolution.adminfragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firmantour.travelingsolution.Adapter;
import com.firmantour.travelingsolution.AdminDashboard2;
import com.firmantour.travelingsolution.AdminDetailMobil;
import com.firmantour.travelingsolution.R;
import com.firmantour.travelingsolution.databinding.FragmentAPengaturanBinding;
import com.firmantour.travelingsolution.model.ModelAdmin;
import com.firmantour.travelingsolution.model.ModelRekening;
import com.firmantour.travelingsolution.model.ModelUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class APengaturan extends Fragment {

    private FragmentAPengaturanBinding binding;
    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;
    private Adapter adapterAdmin;

    public APengaturan() {
        // Required empty public constructor
    }

    public static APengaturan newInstance(String data) {
        APengaturan fragment = new APengaturan();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAPengaturanBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        Bundle args = getArguments();
        String receivedData = args.getString("FRAGMENT_DATA");
        binding.etNomorTelepon.setText(receivedData);

        db = FirebaseFirestore.getInstance();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.addItemDecoration(decoration);
        binding.recyclerView.setAdapter(adapterAdmin);

        binding.btnUbahAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment aUbahAdmin = new AUbahAdmin();
                replaceFragment(aUbahAdmin);
            }
        });

        binding.btnUbahRekening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment aUbahRekening = new AUbahRekening();
                replaceFragment(aUbahRekening);
            }
        });

        binding.btnTambahAnggota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment aTambahAnggota = new ATambahAnggota();
                replaceFragment(aTambahAnggota);
            }
        });

        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
    private void getDataAdmin() {
        String collection = "Admin";
        String document = binding.etNomorTelepon.getText().toString();
        db.collection(collection).document(document)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                binding.etNamaAdmin.setText(document.getString("namaadmin"));
                                binding.etPassword.setText(document.getString("password"));
                            } else {
                                // Document does not exist
                                Log.d("GetDataActivity", "No such document");
                            }
                        } else {
                            // An error occurred
                            Log.e("GetDataActivity", "Error getting document", task.getException());
                        }
                    }
                });
    }
    private void getDataRekening() {
        db.collection("AkunBank")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                binding.etNamaBank.setText(document.getString("namabank"));
                                binding.etRekening.setText(document.getString("nomorrekening"));
                                binding.etAtasNama.setText(document.getString("atasnama"));
                            }
                        } else {
                            Toast.makeText(getContext(), "Gagal memuat data.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void getDataAnggota() {
        Query query = db.collection("Admin");
        FirestoreRecyclerOptions<ModelAdmin> response = new FirestoreRecyclerOptions.Builder<ModelAdmin>()
                .setQuery(query, ModelAdmin.class).build();
        adapter = new FirestoreRecyclerAdapter<ModelAdmin, ProdukHolder>(response) {
            @NonNull
            @Override
            public ProdukHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin, parent, false);
                return new ProdukHolder(view);
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                Log.e("Ditemukan Error: ", e.getMessage());
            }

            @Override
            protected void onBindViewHolder(@NonNull ProdukHolder holder, int position, @NonNull ModelAdmin model) {
                holder.namaAdmin.setText(model.getNamaadmin());
                holder.nomorTelepon.setText(model.getNomortelepon());
            }


        };
        adapter.notifyDataSetChanged();
        binding.recyclerView.setAdapter(adapter);
    }
    private class ProdukHolder extends RecyclerView.ViewHolder {
        TextView namaAdmin, nomorTelepon;

        public ProdukHolder(@NonNull View itemView) {
            super(itemView);
            namaAdmin = itemView.findViewById(R.id.textViewNama);
            nomorTelepon = itemView.findViewById(R.id.textViewNomor);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        getDataAnggota();
        getDataRekening();
        getDataAdmin();
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