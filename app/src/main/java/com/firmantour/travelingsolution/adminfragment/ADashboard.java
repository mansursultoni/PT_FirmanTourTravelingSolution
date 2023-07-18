package com.firmantour.travelingsolution.adminfragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firmantour.travelingsolution.R;


public class ADashboard extends Fragment {


    public ADashboard() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ADashboard newInstance(String param1, String param2) {
        ADashboard fragment = new ADashboard();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_a_dashboard, container, false);
    }
}