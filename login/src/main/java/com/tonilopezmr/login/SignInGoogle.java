/*
 * Copyright 2016 Antonio López Marín <tonilopezmr.github.io>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tonilopezmr.login;

import android.app.Activity;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

/**
 * @author Antonio López.
 */
public class SignInGoogle implements Provider, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SING_IN = 0;
    public static final String GOOGLE_PROVIDER = "Google";

    private boolean signInClicked ;
    private boolean intentInProgress;

    private GoogleApiClient googleApiClient;
    private SignInManager signInManager;
    private SignInActivity activity;

    public SignInGoogle(SignInActivity activity) {
        this.googleApiClient =  new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PLUS_LOGIN))
                .addScope(new Scope(Scopes.PLUS_ME))
                .build();
        this.signInManager = SignInManager.getInstance(activity.getApplicationContext());
        this.activity = activity;
    }

    public void onGoogleButtonClick(View view){
        if (!googleApiClient.isConnecting()) {
            signInClicked = true;
            connect();
        }
    }

    public void onResult(int requestCode, int responseCode){
        if (requestCode == RC_SING_IN) {
            if (responseCode != Activity.RESULT_OK) {
                signInClicked = false;
            }

            intentInProgress = false;

            if (!googleApiClient.isConnected()) {
                googleApiClient.reconnect();
            }
        }
    }

    @Override
    public boolean isConnected() {
        return googleApiClient.isConnected();
    }

    public void connect(){
        googleApiClient.connect();
    }

    @Override
    public void disconnect() {
        signOutAccount();
    }

    @Override
    public String getName() {
        return GOOGLE_PROVIDER;
    }

    @Override
    public void onConnected(Bundle bundle) {
        signInManager.linkProvider(this);
        signInClicked = false;
        signInManager.storeUserLogedInPreferences(this);
        if (Plus.PeopleApi.getCurrentPerson(googleApiClient) != null) {
            final Person person = Plus.PeopleApi.getCurrentPerson(googleApiClient);
            activity.onConnectionComplete(new PersonProfile() {
                @Override
                public String getId() {
                    return person.getId();
                }

                @Override
                public String getName() {
                    return person.getDisplayName();
                }

                @Override
                public String getEmail() {
                    return Plus.AccountApi.getAccountName(googleApiClient);
                }

                @Override
                public Uri getImageUri() {
                    return Uri.parse(person.getImage().getUrl());
                }
            });
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!intentInProgress) {
            if (signInClicked && connectionResult.hasResolution()) {
                // The user has already clicked 'sign-in' so we attempt to resolve all
                // errors until the user is signed in, or they cancel.
                try {
                    connectionResult.startResolutionForResult(activity, RC_SING_IN);
                    intentInProgress = true;
                } catch (IntentSender.SendIntentException e) {
                    // The intent was canceled before it was sent.  Return to the default
                    // state and attempt to connect to get an updated ConnectionResult.
                    intentInProgress = false;
                    googleApiClient.connect();
                    Log.e(this.getClass().getCanonicalName(), e.getMessage());
                }
            }
        }
    }

    private void signOutAccount() {
        if (googleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(googleApiClient);
            googleApiClient.disconnect();
        }
        signInManager.storeUserLogedOutInPreferences();
    }

    public interface LoginGoogle {
        SignInButton getLoginGoogleButton();
    }
}
