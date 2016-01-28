package com.tonilopezmr.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.Arrays;

/**
 * @author Antonio López.
 */
public abstract class SignInActivity extends AppCompatActivity 
        implements SignInFacebook.LoginFacebook, SignInTwitter.LoginTwitter, SignInGoogle.LoginGoogle {

    protected SignInGoogle signInGoogle;
    protected SignInFacebook signInFacebook;
    protected SignInTwitter signInTwitter;
    protected SignInManager signInManager;

    protected CallbackManager callbackManager;
    protected LoginButton facebookLoginButton;

    protected TwitterLoginButton twitterLoginButton;

    protected SignInButton signInGoogleButton;

      @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    protected void init(){
        this.signInManager = SignInManager.getInstance(getApplicationContext());
        
        initFacebook();
        initTwitter();
        initGoogle();
        
        signInManager.init(signInGoogle, signInFacebook, signInTwitter);

        if (signInManager.hasConnectedOnPhone()){
            userIsLogged();
        }else{
            userIsntLogged();
        }
    }

    private void initGoogle(){
        this.signInGoogle = new SignInGoogle(this);

        this.signInGoogleButton = getLoginGoogleButton();
        this.signInGoogleButton.setOnClickListener(new OnGoogleClickListener());
    }

    private void initTwitter(){
        this.signInTwitter = new SignInTwitter(this);
        this.twitterLoginButton = getLoginTwitterButton();
        this.twitterLoginButton.setCallback(signInTwitter.getCallback());
    }

    private void initFacebook() {
        this.callbackManager = CallbackManager.Factory.create();
        this.facebookLoginButton = getLoginFacebookButton();
        this.facebookLoginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        this.signInFacebook = new SignInFacebook(this);
        this.facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                signInFacebook.request(loginResult);
                signInManager.linkProvider(signInFacebook);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                errorOnConnect();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        signInGoogle.onResult(requestCode, resultCode);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    protected void connect(){
        if (signInManager.hasConnectedOnPhone()){
            Provider provider = signInManager.getCurrentProvider();
            if (provider != null){
                provider.connect();
            }
        }
    }

    protected void disconnect(){
        Provider provider = signInManager.getCurrentProvider();
        if (signInManager.hasConnectedOnPhone() && provider!=null){
            provider.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
        signInManager.unlinkProvider();
    }

    final private class OnGoogleClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            signInGoogle.onGoogleButtonClick(view);
        }
    }

    public abstract void userIsLogged();
    public abstract void errorOnConnect();
    public abstract void userIsntLogged();
    public abstract void onConnectionComplete(PersonProfile person);
}
