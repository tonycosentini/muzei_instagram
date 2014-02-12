package com.tonycosentini.muzei.instagramsource.data;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by tonyc on 2/11/14.
 */
public interface InstagramService {
  @GET("/users/self/media/recent") public void getUserMedia(@Query("access_token") String accessToken, Callback<InstagramFeed> callback);
}
