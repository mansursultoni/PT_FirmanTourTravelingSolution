package com.firmantour.travelingsolution;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.firmantour.travelingsolution.databinding.ActivityFotoBinding;

public class ActivityFoto extends AppCompatActivity {

    private ActivityFotoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFotoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.grey));

        // Menerima data gambar dari Activity A
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Bitmap bitmap = (Bitmap) extras.get("imageBitmap");

            if (bitmap != null) {
                // Menampilkan gambar di ImageView
                binding.imageView.setImageBitmap(bitmap);
            } else {
                // Jika gambar tidak ada, tampilkan pesan toast
                Toast.makeText(this, "Gambar tidak tersedia.", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            // Jika data tidak ada, tampilkan pesan toast
            Toast.makeText(this, "Data tidak tersedia.", Toast.LENGTH_SHORT).show();
            finish();
        }
        binding.btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}