package com.tonilopezmr.login.providers.facebook;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.tonilopezmr.login.SignInActivity;
import com.tonilopezmr.login.providers.Provider;
import com.tonilopezmr.login.providers.SignInManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Antonio López.
 */
public class SignInFacebook implements Provider {

    public static final String FACEBOOK_PROVIDER = "Facebook";

    private SignInManager signInManager;
    private SignInActivity activity;

    private OnLoginFacebookCallback loginCallback;

    public SignInFacebook(SignInActivity signInActivity) {
        this.signInManager = SignInManager.getInstance(signInActivity.getApplicationContext());
        this.activity = signInActivity;
    }

    public void connect(LoginResult loginResult) {
       connect(loginResult.getAccessToken());
    }

    private void connect(final AccessToken accessToken){
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            final JSONObject object,
                            GraphResponse response) {
                        final Profile profile = Profile.getCurrentProfile();
                        if (profile != null){
                            signInManager.storeUserLogedInPreferences(SignInFacebook.this);
                            if (loginCallback != null){
                                try {
                                    loginCallback.onCompleted(profile, object.getString("email"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    activity.errorOnConnect();
                                }
                            }
                        }else{
                            activity.errorOnConnect();
                        }
                        Log.v("LoginActivity", response.toString());
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }


    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void connect() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        connect(accessToken);
    }

    @Override
    public void disconnect() {
        LoginManager.getInstance().logOut();
        signInManager.storeUserLogedOutInPreferences();
    }

    @Override
    public String getName() {
        return FACEBOOK_PROVIDER;
    }

    public void setOnLoginCallback(OnLoginFacebookCallback loginCallback) {
        this.loginCallback = loginCallback;
    }

    public FacebookCallback getCallback(){
        return new FacebookCallback();
    }

    public interface LoginFacebook {
        LoginButton getLoginFacebookButton();
    }

    public interface OnLoginFacebookCallback {
        void onCompleted(Profile profile, String email);
    }

    private class FacebookCallback implements com.facebook.FacebookCallback<LoginResult> {
        @Override
        public void onSuccess(LoginResult loginResult) {
            connect(loginResult);
            signInManager.linkProvider(SignInFacebook.this);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    }
}
