package com.tonilopezmr.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author Antonio López.
 */
public class SignInManager {

    public static final String ALREADY_LOGGED_KEY = "alreadyLogged";
    public static final String LOGGED_PROVIDER_KEY = "loggedProvider";

    private Context context;

    public SignInManager(Context context) {
        this.context = context;
    }

    public boolean hasConnectedOnPhone(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(ALREADY_LOGGED_KEY, false);
    }

    public String getProvidedLogged(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(LOGGED_PROVIDER_KEY, "");
    }

    public void storeUserLogedInPreferences(Provider provider){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(ALREADY_LOGGED_KEY, true);
        editor.putString(LOGGED_PROVIDER_KEY, provider.getName());
        editor.apply();
    }

    public void storeUserLogedOutInPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(ALREADY_LOGGED_KEY, false);
        editor.remove(LOGGED_PROVIDER_KEY);
        editor.apply();
    }
}
