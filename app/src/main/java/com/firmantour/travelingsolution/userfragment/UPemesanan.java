package com.firmantour.travelingsolution.userfragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firmantour.travelingsolution.R;
import com.firmantour.travelingsolution.databinding.FragmentUPemesananBinding;


public class UPemesanan extends Fragment {

    private FragmentUPemesananBinding binding;

    public UPemesanan() {
        // Required empty public constructor
    }


    public static UPemesanan newInstance(String param1, String param2) {
        UPemesanan fragment = new UPemesanan();
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
        binding = FragmentUPemesananBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        return view;
    }
}