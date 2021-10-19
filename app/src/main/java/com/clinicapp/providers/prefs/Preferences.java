package com.clinicapp.providers.prefs;

public interface Preferences {
    boolean isLoggedIn();
    void setToken(final String token);
    String getToken();

}
