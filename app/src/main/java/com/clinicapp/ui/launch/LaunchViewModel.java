package com.clinicapp.ui.launch;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.clinicapp.providers.AsyncResponse;
import com.clinicapp.ui.auth.LoginActivity;
import com.clinicapp.ui.home.MainActivity;
import com.clinicapp.utilities.BaseViewModel;

public class LaunchViewModel extends BaseViewModel {



    private MutableLiveData<AsyncResponse<Class,Exception>> authStateLiveData;

    public LaunchViewModel(@NonNull Application application) {
        super(application);
        authStateLiveData = new MutableLiveData<>(AsyncResponse.notStarted());
    }

    public LiveData<AsyncResponse<Class,Exception>> getAuthState(){
        return authStateLiveData;
    }

    public void checkAuthState(){
        Class nextActivity;
        if(repository.prefs.isLoggedIn()){
            nextActivity = MainActivity.class;
        } else {
            nextActivity = LoginActivity.class;
        }
        Log.e( "checkAuthState: ","in" +nextActivity.getName());
        authStateLiveData.postValue(AsyncResponse.success(nextActivity));
    }

}
