package com.tonilopezmr.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.Arrays;

/**
 * @author Antonio López.
 */
public abstract class SignInActivity extends AppCompatActivity implements  View.OnClickListener {

    protected SignInGoogle signInGoogle;
    protected SignInFacebook signInFacebook;
    protected SignInTwitter signInTwitter;
    protected SignInManager signInManager;

    protected CallbackManager callbackManager;
    protected LoginButton loginButton;

    protected TwitterLoginButton twitterLoginButton;

      @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    protected void init(){
        this.signInManager = SignInManager.getInstance(getApplicationContext());
        this.signInGoogle = new SignInGoogle(this);

        signInManager.init(signInGoogle, signInFacebook, signInTwitter);

        if (signInManager.hasConnectedOnPhone()){
            userIsLogged();
        }else{
            userIsntLogged();
        }
    }

    protected void prepareLogin(TwitterLoginButton twitterButton){
        this.signInTwitter = new SignInTwitter(this);
        this.twitterLoginButton = twitterButton;
        //Twitter
        this.twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                signInManager.linkProvider(signInTwitter);
                signInTwitter.connect();
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
    }

    protected void prepareLogin(CallbackManager callbackManager, LoginButton button) {
        this.loginButton = button;
        this.loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        this.signInFacebook = new SignInFacebook(this, loginButton);
        signInFacebook.prepareLogin(callbackManager, new FacebookCallback<LoginResult>() {
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

            }
        });
    }

    @Override
    public void onClick(View view) {
        signInGoogle.onGoogleButtonClick(view);
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

    public abstract void userIsLogged();
    public abstract void errorOnConnect();
    public abstract void userIsntLogged();
    public abstract void onConnectionComplete(PersonProfile person);
}
