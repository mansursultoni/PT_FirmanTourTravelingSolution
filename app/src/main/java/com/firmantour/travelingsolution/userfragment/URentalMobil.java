package com.firmantour.travelingsolution.userfragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firmantour.travelingsolution.R;
import com.firmantour.travelingsolution.databinding.FragmentURentalMobilBinding;

public class URentalMobil extends Fragment {

    private FragmentURentalMobilBinding binding;
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



        return view;
    }
}