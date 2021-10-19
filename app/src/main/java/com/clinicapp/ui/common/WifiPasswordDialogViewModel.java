package com.clinicapp.ui.common;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.clinicapp.models.WifiEntry;
import com.clinicapp.providers.AsyncResponse;
import com.clinicapp.utilities.BaseViewModel;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionErrorCode;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener;

public class WifiPasswordDialogViewModel extends BaseViewModel {

    private MutableLiveData<AsyncResponse<Boolean,Exception>> wifiStateLiveData;
    private WifiEntry wifiEntry;

    public WifiPasswordDialogViewModel(@NonNull Application application) {
        super(application);
        wifiStateLiveData = new MutableLiveData<>(AsyncResponse.notStarted());
    }

    public void init(WifiEntry wifiEntry){
        this.wifiEntry = wifiEntry;
    }

    public LiveData<AsyncResponse<Boolean, Exception>> getWifiStateLiveData() {
        return wifiStateLiveData;
    }

    public void connect( String password){
        wifiStateLiveData.setValue(AsyncResponse.loading());
        repository.wifi.connect(wifiEntry.getDetails(), password, new ConnectionSuccessListener() {
            @Override
            public void success() {
                wifiStateLiveData.setValue(AsyncResponse.success(true));

            }

            @Override
            public void failed(@NonNull ConnectionErrorCode errorCode) {
                wifiStateLiveData.setValue(AsyncResponse.error(errorCode.name()));
            }
        });
    }
}
