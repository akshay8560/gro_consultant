package com.clinicapp.models;

public class WifiModel {

    String SSID;

    String BSSID;

    String password;

    boolean isConnected;


    public WifiModel(String SSID, String BSSID, boolean isConnected) {
        this.SSID = SSID;
        this.BSSID = BSSID;
        this.isConnected = isConnected;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
