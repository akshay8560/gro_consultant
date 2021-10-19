package com.clinicapp.providers;

import android.app.Application;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.clinicapp.models.WifiEntry;
import com.thanosfisherman.wifiutils.WifiUtils;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class WifiProvider {
    private static WifiProvider INSTANCE;

    private final Application application;
    private final MutableLiveData<List<WifiEntry>> wifiLiveData;
    final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable scanRunnable = this::scan;

    public static WifiProvider getInstance(Application app){
        synchronized (com.clinicapp.providers.prefs.PrefsProvider.class){
            if (INSTANCE == null) {
                INSTANCE = new WifiProvider(app);
            }
        }
        return INSTANCE;
    }


    private WifiProvider(Application app) {
        this.application = app;
        wifiLiveData = new MutableLiveData<>(new ArrayList<>());
    }


    public LiveData<List<WifiEntry>> startScan(){
        handler.postDelayed(scanRunnable, 1000);
        return wifiLiveData;
    }

    public void stopScan(){
        handler.removeCallbacks(scanRunnable);
    }

    private void scan(){
        WifiUtils.withContext(application)
                .scanWifi(this::getScanResults)
                .start();
    }


    private void getScanResults(List<ScanResult> scanResults) {
        wifiLiveData.setValue(getWifiEntries(scanResults));
        final long nextScanInterval = scanResults.size() > 0 ? 31000:1000;

        handler.postDelayed(scanRunnable,nextScanInterval);
    }


    private List<WifiEntry> getWifiEntries(List<ScanResult> scanResults){
        final ArrayList<WifiEntry> entries = new ArrayList<>();
        for(ScanResult scanResult:scanResults){
            final boolean isConnected = WifiUtils.withContext(application).isWifiConnected(scanResult.SSID);
            entries.add(new WifiEntry(scanResult, isConnected));
        }
        return entries;
    }


    public void connect(ScanResult scanResult, String password, ConnectionSuccessListener listener){

        WifiUtils.withContext(application)
            .connectWith(scanResult.SSID, password)
            .onConnectionResult(listener)
            .start();
    }


}
