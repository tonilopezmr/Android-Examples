package com.tonilopezmr.login.providers;

import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterApiClient;

/**
 * @author Antonio López.
 */
public class MyTwitterApi extends TwitterApiClient{

    public MyTwitterApi(Session session) {
        super(session);
    }

    public MyTwitterService getTwitterService(){
        return getService(MyTwitterService.class);
    }
}
