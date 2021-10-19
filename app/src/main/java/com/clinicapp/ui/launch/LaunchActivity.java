package com.clinicapp.ui.launch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class LaunchActivity extends AppCompatActivity {
    private static final long SLEEP_TIME = 3000;
    private LaunchViewModel viewModel;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(LaunchViewModel.class);
        init();
    }

    private void init(){
        viewModel.getAuthState().observe(this, response -> {
            if(!response.isError() && !response.isNotStarted())
                showNextActivity(response.value);

        });

        //Launch next activity after 3 seconds.
        new Handler(Looper.getMainLooper())
                .postDelayed((Runnable) () -> viewModel.checkAuthState(), SLEEP_TIME);
    }

    private void showNextActivity(Class nextActivity) {
        final Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
        finish();
    }


}
