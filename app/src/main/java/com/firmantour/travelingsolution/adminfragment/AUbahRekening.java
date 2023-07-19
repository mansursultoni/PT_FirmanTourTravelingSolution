package com.firmantour.travelingsolution.adminfragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firmantour.travelingsolution.R;
import com.firmantour.travelingsolution.databinding.FragmentAUbahRekeningBinding;
import com.firmantour.travelingsolution.model.ModelRekening;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class AUbahRekening extends Fragment {

    private FragmentAUbahRekeningBinding binding;
    private FirebaseFirestore db;

    public AUbahRekening() {
        // Required empty public constructor
    }

    public static AUbahRekening newInstance(String param1, String param2) {
        AUbahRekening fragment = new AUbahRekening();
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
        binding = FragmentAUbahRekeningBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        db = FirebaseFirestore.getInstance();

        binding.btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Membatallkan perubahan?");
                alertDialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Fragment aPengaturan = new APengaturan();
                        replaceFragment(aPengaturan);
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

        binding.btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData(binding.etNamaBank.getText().toString(),
                        binding.etRekening.getText().toString(),
                        binding.etAtasNama.getText().toString());
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
    private void saveData(String namabank, String nomorrekening, String atasnama) {
        Map<String, Object> user = new HashMap<>();
        user.put("namabank", namabank);
        user.put("nomorrekening", nomorrekening);
        user.put("atasnama", atasnama);

        db.collection("AkunBank").document(atasnama).set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Rekening berhasil diperbarui.", Toast.LENGTH_SHORT).show();
                            Fragment aPengaturan = new APengaturan();
                            replaceFragment(aPengaturan);
                        } else {
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Gagal menyimpan data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}