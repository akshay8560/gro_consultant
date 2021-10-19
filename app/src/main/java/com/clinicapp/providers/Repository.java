package com.clinicapp.providers;

import android.app.Application;

import com.clinicapp.providers.api.ApiProvider;
import com.clinicapp.providers.api.ApiResource;
import com.clinicapp.providers.prefs.Preferences;
import com.clinicapp.providers.prefs.PrefsProvider;

public class Repository {


        public static Repository INSTANCE;
        public final Preferences prefs;
        public final ApiResource api;
        public final WifiProvider wifi;


        public static Repository getInstance(Application app){
            synchronized (com.clinicapp.providers.prefs.PrefsProvider.class){
                if (INSTANCE == null) {
                    INSTANCE = new Repository(app);
                }
            }
            return INSTANCE;
        }

        private Repository(Application app){
            prefs = PrefsProvider.getInstance(app);
            api = ApiProvider.getInstance(app).resource;
            wifi = WifiProvider.getInstance(app);
        }
}
