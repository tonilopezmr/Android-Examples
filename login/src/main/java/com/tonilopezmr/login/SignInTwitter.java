package com.tonilopezmr.login;

import android.net.Uri;
import android.util.Log;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author Antonio López.
 */
public class SignInTwitter implements Provider {

    public static final String TWITTER_PROVIDER = "Twitter";

    private SignInManager manager;
    private SignInActivity signInActivity;

    public SignInTwitter(SignInActivity signInActivity) {
        this.signInActivity = signInActivity;
        this.manager = SignInManager.getInstance(signInActivity.getApplicationContext());
    }

    @Override
    public boolean isConnected() {
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        return session!=null;
    }

    private void connect(final TwitterSession session){
        manager.storeUserLogedInPreferences(this);
        new MyTwitterApi(session).getTwitterService().show(session.getUserId(), session.getUserName(), false, new Callback<User>() {
            @Override
            public void success(final User user, Response response) {
                signInActivity.onConnectionComplete(new PersonProfile() {
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
                signInActivity.errorOnConnect();
            }
        });
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
