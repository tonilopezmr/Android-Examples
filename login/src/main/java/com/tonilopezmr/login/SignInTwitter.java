package com.tonilopezmr.login;

import android.net.Uri;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterSession;
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
                        return user.email;
                    }

                    @Override
                    public Uri getImageUri() {
                        return Uri.parse(user.profileImageUrlHttps);
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {

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
}
