package com.clinicapp.utilities;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.clinicapp.providers.Repository;

public class BaseViewModel extends AndroidViewModel {
    protected Repository repository;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance(application);
    }
}
