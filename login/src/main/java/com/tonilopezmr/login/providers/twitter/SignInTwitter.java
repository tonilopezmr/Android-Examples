package com.tonilopezmr.login.providers.twitter;

import android.util.Log;

import com.tonilopezmr.login.SignInActivity;
import com.tonilopezmr.login.providers.Provider;
import com.tonilopezmr.login.providers.SignInManager;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import retrofit.Callback;

/**
 * @author Antonio López.
 */
public class SignInTwitter implements Provider {

    public static final String TWITTER_PROVIDER = "Twitter";

    private SignInManager manager;
    private SignInActivity signInActivity;

    private Callback<User> twitterCallback;

    public SignInTwitter(SignInActivity signInActivity) {
        this.signInActivity = signInActivity;
        this.manager = SignInManager.getInstance(signInActivity.getApplicationContext());
    }

    public void setTwitterCallback(Callback<User> twitterCallback) {
        this.twitterCallback = twitterCallback;
    }

    @Override
    public boolean isConnected() {
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        return session!=null;
    }

    private void connect(final TwitterSession session){
        if (twitterCallback != null){
            new MyTwitterApi(session).getTwitterService().show(session.getUserId(), session.getUserName(), false, twitterCallback);
        }else{
            throw new IllegalStateException("SignInTwitter: Callback must not be null, did you call setCallback?");
        }
    }

    @Override
    public void connect() {
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        connect(session);
    }

    @Override
    public void disconnect() {
        Twitter.logOut();
        manager.storeUserLogedOutInPreferences();
    }

    @Override
    public String getName() {
        return TWITTER_PROVIDER;
    }

    public TwitterCallBack getCallback(){
        return new TwitterCallBack();
    }

    public interface LoginTwitter {
        TwitterLoginButton getLoginTwitterButton();
    }

    public class TwitterCallBack extends com.twitter.sdk.android.core.Callback<TwitterSession> {
        @Override
        public void success(Result<TwitterSession> result) {
            // The TwitterSession is also available through:
            // Twitter.getInstance().core.getSessionManager().getActiveSession()
            manager.storeUserLogedInPreferences(SignInTwitter.this);
            TwitterSession session = result.data;
            manager.linkProvider(SignInTwitter.this);
            connect(session);
        }
        @Override
        public void failure(TwitterException exception) {
            Log.d("TwitterKit", "Login with Twitter failure", exception);
            signInActivity.errorOnConnect();
        }
    }
}
