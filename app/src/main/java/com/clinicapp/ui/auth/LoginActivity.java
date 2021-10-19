package com.clinicapp.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.clinicapp.databinding.ActivityLoginBinding;
import com.clinicapp.providers.AsyncResponse;
import com.clinicapp.ui.common.WifiDialogFragment;
import com.clinicapp.ui.home.MainActivity;
import com.clinicapp.utilities.BaseActivity;
import com.clinicapp.utilities.PostTextWatcher;
import com.clinicapp.utilities.Utils;

import kotlin.Pair;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding views;
    private LoginViewModel viewModel;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        views = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(views.getRoot());
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        init();
    }

    private void init(){
        new PostTextWatcher(views.email,this::handleEmailChange);
        new PostTextWatcher(views.password,this::handlePasswordChange);
        viewModel.getAuthLiveData().observe(this,this::handleLoginResponse);
    }


    private void handleLoginResponse(AsyncResponse<Boolean,Exception> response){
        setViewState(!response.isLoading(), views.email, views.password, views.signIn);
        views.progressBar.setVisibility(response.isLoading() ? View.VISIBLE:View.INVISIBLE);
        views.signIn.setVisibility(response.isLoading() ? View.INVISIBLE:View.VISIBLE);

        if(response.isSuccess() && response.value){
            launchMainActivity();
        } else if(response.isError()){
            Utils.notify(this, response.message);
        }
    }

    private void launchMainActivity() {
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void handleEmailChange(String text){
        final Pair<Boolean,String> valResult = viewModel.validateEmail(text);
        handleValidation(views.emailErr, valResult);
    }

    public void handlePasswordChange(String text){
        final Pair<Boolean,String> valResult = viewModel.validatePassword(text);
        handleValidation(views.passwordErr, valResult);
    }

    private void handleValidation(TextView error, Pair<Boolean,String> validationResult){
        error.setVisibility(validationResult.getFirst() ? View.INVISIBLE:View.VISIBLE);
        error.setText(validationResult.getSecond());
    }

    public void onClickSignIn(View v){
        final String email = views.email.getText().toString();
        final String password = views.password.getText().toString();
//        viewModel.login(email, password);
        launchMainActivity();
    }

    @Override
    public void updateBatteryPercent(int battery) {
    }
}
