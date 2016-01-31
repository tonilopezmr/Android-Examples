package com.tonilopezmr.login.providers.twitter;

import com.twitter.sdk.android.core.models.User;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * @author Antonio López.
 */
public interface MyTwitterService {

    @GET("/1.1/users/show.json")
    void show(@Query("user_id") Long userId,
              @Query("screen_name") String screenName,
              @Query("include_entities") Boolean includeEntities,
              Callback<User> cb);
}
