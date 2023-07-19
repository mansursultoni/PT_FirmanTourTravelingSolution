package com.firmantour.travelingsolution.userfragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firmantour.travelingsolution.R;
import com.firmantour.travelingsolution.databinding.FragmentUPaketWisataBinding;


public class UPaketWisata extends Fragment {

    private FragmentUPaketWisataBinding binding;
    public UPaketWisata() {
        // Required empty public constructor
    }

    public static UPaketWisata newInstance(String param1, String param2) {
        UPaketWisata fragment = new UPaketWisata();
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
        binding = FragmentUPaketWisataBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        return view;
    }
}