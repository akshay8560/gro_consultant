package com.clinicapp.providers.prefs;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class PrefsProvider implements Preferences{

    public static PrefsProvider INSTANCE;
    private final SharedPreferences client;

    public static PrefsProvider getInstance(Application app){
        synchronized (PrefsProvider.class){
            if (INSTANCE == null) {
                INSTANCE = new PrefsProvider(app);
            }
        }
        return INSTANCE;
    }



    private PrefsProvider(Application application) {
        client = application.getSharedPreferences("DEFAULT", Context.MODE_PRIVATE);
    }

    @Override
    public boolean isLoggedIn(){
        return getToken().length() > 0;
    }

    @Override
    public void setToken(final String token){
        client.edit().putString("token",token).commit();
    }

    @Override
    public String getToken(){
        return client.getString("token","");
    }



}
