package com.clinicapp.adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.clinicapp.models.Patient;
import com.clinicapp.ui.home.PatientsResultFragment;

import java.util.List;


public class ViewPagerAdapter extends androidx.viewpager2.adapter.FragmentStateAdapter{

    private List<Patient> patients;

    public ViewPagerAdapter(@NonNull Fragment fragment, List<Patient> patients) {
        super(fragment);
        this.patients = patients;
    }



    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return PatientsResultFragment.getInstance(patients.get(position));
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }
}
