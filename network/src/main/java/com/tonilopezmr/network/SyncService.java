package com.tonilopezmr.network;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * @author Antonio López.
 */
public class SyncService extends Service{

    private static SyncAdapter mySyncAdapter = null;
    private static final Object sSyncAdapterLock = new Object();

    @Override
    public void onCreate() {
        super.onCreate();

        synchronized (sSyncAdapterLock){
            if (mySyncAdapter == null){
                mySyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mySyncAdapter.getSyncAdapterBinder();
    }
}
