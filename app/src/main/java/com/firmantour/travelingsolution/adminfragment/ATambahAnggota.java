package com.firmantour.travelingsolution.adminfragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firmantour.travelingsolution.R;
import com.firmantour.travelingsolution.UserDaftar;
import com.firmantour.travelingsolution.databinding.FragmentATambahAnggotaBinding;
import com.firmantour.travelingsolution.model.ModelAdmin;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class ATambahAnggota extends Fragment {

    private FragmentATambahAnggotaBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$";
    private Pattern pattern;

    public ATambahAnggota() {
        // Required empty public constructor
    }

    public static ATambahAnggota newInstance(String param1, String param2) {
        ATambahAnggota fragment = new ATambahAnggota();
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
        binding = FragmentATambahAnggotaBinding.inflate(inflater, container,false);
        View view = binding.getRoot();

        pattern = Pattern.compile(PASSWORD_PATTERN);

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
                String password = binding.etPassword.getText().toString();
                validatePassword(password);
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
    private void saveData(String namaadmin, String nomortelepon, String password){
        Map<String, Object> user = new HashMap<>();
        user.put("namaadmin", namaadmin);
        user.put("nomortelepon", nomortelepon);
        user.put("password", password);

        db.collection("Admin").document(nomortelepon).set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                        }else{
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Gagal Menyimpan data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void validatePassword(String password){
        if (pattern.matcher(password).matches()) {
            // Password is valid
            if (binding.etNamaAdmin.equals("") || binding.etNomorTelepon.equals("")
                    || binding.etPassword.equals("")){
                Toast.makeText(getContext(), "Data harus lengkap.", Toast.LENGTH_SHORT).show();
            } else {
                databaseReference.child("Login").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(binding.etNomorTelepon.getText().toString())){
                            Toast.makeText(getContext(), "Nomor telepon telah terdaftar.", Toast.LENGTH_SHORT).show();
                        }else {
                            if (!binding.etPassword2.getText().toString().equals(binding.etPassword.getText().toString())){
                                Toast.makeText(getContext(), "Masukkan ulang password.", Toast.LENGTH_SHORT).show();
                            }else {
                                String sebagai = "admin";
                                databaseReference.child("Login").child(binding.etNomorTelepon.getText().toString())
                                        .setValue(new ModelAdmin(binding.etNamaAdmin.getText().toString(),
                                                binding.etNomorTelepon.getText().toString(),
                                                binding.etPassword.getText().toString(),
                                                sebagai)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                saveData(binding.etNamaAdmin.getText().toString(),
                                                        binding.etNomorTelepon.getText().toString(),
                                                        binding.etPassword.getText().toString());
                                                Toast.makeText(getContext(), "Anggota Admin Berhasil Ditambah..", Toast.LENGTH_SHORT).show();
                                                Fragment aPengaturan = new APengaturan();
                                                replaceFragment(aPengaturan);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "Gagal menyimpan data.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        } else {
            // Password is invalid
            Toast.makeText(getContext(), "Password minimal 6 karakter dengan huruf besar, huruf kecil, dan angka.", Toast.LENGTH_LONG).show();
        }
    }
}