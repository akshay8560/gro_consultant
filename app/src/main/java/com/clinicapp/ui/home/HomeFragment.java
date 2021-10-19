package com.clinicapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.clinicapp.R;
import com.clinicapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
private FragmentHomeBinding views;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        views = FragmentHomeBinding.inflate(getLayoutInflater(),container,false);
        return  views.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        views.newPatient.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_newPatientFragment));
        views.existingPatient.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_searchPatientFragment));
    }
}