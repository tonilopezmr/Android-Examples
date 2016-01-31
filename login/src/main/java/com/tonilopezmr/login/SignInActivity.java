package com.tonilopezmr.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.tonilopezmr.login.providers.Provider;
import com.tonilopezmr.login.providers.facebook.SignInFacebook;
import com.tonilopezmr.login.providers.google.SignInGoogle;
import com.tonilopezmr.login.providers.SignInManager;
import com.tonilopezmr.login.providers.twitter.SignInTwitter;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import java.util.Arrays;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
        this.signInTwitter.setTwitterCallback(new Callback<User>() {
            @Override
            public void success(final User user, Response response) {
                onConnectionComplete(new UserProfile() {
                    @Override
                    public String getId() {
                        return user.idStr;
                    }

                    @Override
                    public String getName() {
                        return user.name;
                    }

                    @Override
                    public String getEmail() {
                        return user.screenName;
                    }

                    @Override
                    public Uri getImageUri() {
                        return Uri.parse(user.profileImageUrlHttps);
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                errorOnConnect();
            }
        });
        this.twitterLoginButton = getLoginTwitterButton();
        this.twitterLoginButton.setCallback(signInTwitter.getCallback());
    }

    private void initFacebook() {
        this.callbackManager = CallbackManager.Factory.create();
        this.facebookLoginButton = getLoginFacebookButton();
        this.facebookLoginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        this.signInFacebook = new SignInFacebook(this);
        this.facebookLoginButton.registerCallback(callbackManager, signInFacebook.getCallback());
        this.signInFacebook.setOnLoginCallback(new SignInFacebook.OnLoginFacebookCallback() {
            @Override
            public void onCompleted(final Profile profile, final String email) {
                onConnectionComplete(new UserProfile() {
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
                        return email;
                    }

                    @Override
                    public Uri getImageUri() {
                        return profile.getProfilePictureUri(100, 100);
                    }
                });
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
    public abstract void onConnectionComplete(UserProfile person);
}
