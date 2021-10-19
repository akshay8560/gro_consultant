package com.clinicapp.ui.home;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.clinicapp.databinding.ActivityMainBinding;
import com.clinicapp.ui.common.WifiDialogFragment;
import com.clinicapp.ui.home.viewmodels.MainViewModel;
import com.clinicapp.utilities.BaseActivity;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding views;
    private MainViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        views = ActivityMainBinding.inflate(getLayoutInflater());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setContentView(views.getRoot());

        init();
    }



    private void init() {

    }

    @Override
    public void updateBatteryPercent(int battery) {
    }
}