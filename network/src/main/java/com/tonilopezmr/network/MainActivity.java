package com.tonilopezmr.network;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.tonilopezmr.network.sync";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "com.tonilopezmr.network.sync";
    // The account name
    public static final String ACCOUNT = "com.tonilopezmr.asdf.sync";
    // Instance fields
    Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAccount = createSyncAccount(this);
    }

    private Account createSyncAccount(MainActivity mainActivity) {
        // Create the account type and default account
        Account newAccount = new Account(
                "otheracoount", ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) getApplicationContext().getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            ContentResolver.setIsSyncable(newAccount, ACCOUNT_TYPE, 1);
            ContentResolver.setSyncAutomatically(newAccount, ACCOUNT_TYPE, true);
            Log.e("MAINACTIVITY!!!","context.setIsSyncable(newAccount, AUTHORITY, 1)");
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
            //ContentResolver.setSyncAutomatically(newAccount, ACCOUNT_TYPE, true);
            //ContentResolver.addPeriodicSync(newAccount, ACCOUNT_TYPE, bundle, 60);
            Log.e("MAINACTIVITY!!!", "The account exists or some other error occurred. Log this, report it,");
        }
        Bundle bundle = new Bundle();
        bundle.putString("patata", "HELLO world!!");
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(newAccount, ACCOUNT_TYPE, bundle);
        return null;
    }

}
