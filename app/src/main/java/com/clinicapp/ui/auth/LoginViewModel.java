package com.clinicapp.ui.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.clinicapp.models.LoginRequest;
import com.clinicapp.models.LoginResponse;
import com.clinicapp.providers.AsyncResponse;
import com.clinicapp.providers.Repository;

import kotlin.Pair;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    private Repository repository;
    private MutableLiveData<AsyncResponse<Boolean, Exception>> authLiveData;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance(application);
        authLiveData = new MutableLiveData<>(AsyncResponse.success(repository.prefs.isLoggedIn()));
    }

    public LiveData<AsyncResponse<Boolean, Exception>> getAuthLiveData() {
        return authLiveData;
    }

    public Pair<Boolean, String> validateEmail(String email){
        return LoginRequest.validateEmail(email);
    }

    public Pair<Boolean, String> validatePassword(String password){
        return LoginRequest.validatePassword(password);
    }

    public void login(String email, String password){
        //Check if we have valid email or password.
        if(!validateEmail(email).getFirst() || !validatePassword(password).getFirst())
            return;
        authLiveData.postValue(AsyncResponse.loading());
        final LoginRequest loginRequest = new LoginRequest(email, password);
        repository.api.login(loginRequest)
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        final LoginResponse result = response.body();
                        if(!result.getSuccess()) {
                            onFailure(call, new Exception(result.getMessage()));
                        } else {
                            repository.prefs.setToken(result.getToken());
                            authLiveData.postValue(AsyncResponse.success(true));
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        authLiveData.postValue(AsyncResponse.error(t.getMessage()));
                    }
                });
    }

}
