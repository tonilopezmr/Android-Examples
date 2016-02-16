package com.tonilopezmr.network;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

/**
 * @author Antonio López.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String TAG = "SyncAdapter";

    /**
     * Network connection timeout, in milliseconds.
     */
    private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000;  // 15 seconds

    /**
     * Network read timeout, in milliseconds.
     */
    private static final int NET_READ_TIMEOUT_MILLIS = 10000;  // 10 seconds

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        //initiailze my sqlite db that i think
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);



    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        String patata = extras.getString("patata");
        Log.e(TAG, "Start sync: "+patata);

        try {
            for (int i = 0 ; i < 20; i++){
                Thread.sleep(1000);
                Log.e(TAG, "Update to server item: "+i);
                syncResult.stats.numUpdates++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
