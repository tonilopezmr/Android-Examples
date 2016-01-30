package com.tonilopezmr.login.providers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author Antonio López.
 */
public class SignInManager {

    public static final String ALREADY_LOGGED_KEY = "alreadyLogged";
    public static final String LOGGED_PROVIDER_KEY = "loggedProvider";

    protected SignInGoogle signInGoogle;
    protected SignInFacebook signInFacebook;
    protected SignInTwitter signInTwitter;

    private Context context;
    private Provider currentProvider;

    private static SignInManager INSTANCE;

    public static SignInManager getInstance(Context context){
        if (INSTANCE == null) INSTANCE = new SignInManager(context);
        return INSTANCE;
    }

    public void init(SignInGoogle google, SignInFacebook facebook, SignInTwitter twitter){
        this.signInGoogle = google;
        this.signInFacebook = facebook;
        this.signInTwitter = twitter;
    }

    private SignInManager(Context context) {
        this.context = context;
    }

    public void linkProvider(Provider provider){
        this.currentProvider = provider;
    }

    public void unlinkProvider(){
        this.currentProvider = null;
    }

    public Provider getCurrentProvider() {
        if (currentProvider == null){
            currentProvider = switchProvider();
        }
        return currentProvider;
    }

    private Provider switchProvider() {
        Provider provider = null;
        if (hasConnectedOnPhone()){
            switch (getProvidedLogged()){
                case SignInGoogle.GOOGLE_PROVIDER:
                    provider = signInGoogle;
                    break;
                case SignInFacebook.FACEBOOK_PROVIDER:
                    provider = signInFacebook;
                    break;
                case SignInTwitter.TWITTER_PROVIDER:
                    provider = signInTwitter;
                    break;
            }
        }
        return provider;
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
