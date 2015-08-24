package com.giffinder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Gene on 2/3/2015.
 */
public interface IGifService {

    @GET("/search")
    void getSearchResults(@Query("q") String q, @Query("api_key") String key, @Query("limit") int limit, Callback<JsonObject> cb);

    @GET("/trending")
    void getTrending(@Query("api_key") String key, Callback<JsonObject> cb);
}
