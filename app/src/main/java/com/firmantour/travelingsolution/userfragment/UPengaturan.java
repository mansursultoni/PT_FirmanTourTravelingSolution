package com.firmantour.travelingsolution.userfragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firmantour.travelingsolution.R;
import com.firmantour.travelingsolution.databinding.FragmentUPengaturanBinding;

public class UPengaturan extends Fragment {

    private FragmentUPengaturanBinding binding;

    public UPengaturan() {
        // Required empty public constructor
    }

    public static UPengaturan newInstance(String param1, String param2) {
        UPengaturan fragment = new UPengaturan();
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
        binding = FragmentUPengaturanBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        return view;
    }
}