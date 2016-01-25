package com.tonilopezmr.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * @author Antonio López.
 */
public class SignInFacebook implements Provider {

    public static final String FACEBOOK_PROVIDER = "Facebook";

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private SignInManager signInManager;
    private SignInActivity activity;

    public SignInFacebook(SignInActivity signInActivity, LoginButton loginButton){
        this.loginButton = loginButton;
        this.callbackManager = CallbackManager.Factory.create();
        this.signInManager = SignInManager.getInstance(signInActivity.getApplicationContext());
        this.activity = signInActivity;
    }


    public void prepareLogin(CallbackManager callbackManager, FacebookCallback<LoginResult> facebookCallback){
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        loginButton.registerCallback(callbackManager, facebookCallback);
    }

    public void request(LoginResult loginResult) {
       connect(loginResult.getAccessToken());
    }

    private void connect(AccessToken accessToken){
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
                            activity.onConnectionComplete(new PersonProfile() {
                                @Override
                                public String getId() {
                                    return profile.getId();
                                }

                                @Override
                                public String getName() {
                                    return profile.getName();
                                }

                                @Override
                                public String getEmail() {
                                    try {
                                        return object.getString("email");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    return "email";
                                }

                                @Override
                                public Uri getImageUri() {
                                    return profile.getProfilePictureUri(100,100);
                                }
                            });
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

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public String getName() {
        return FACEBOOK_PROVIDER;
    }

}
