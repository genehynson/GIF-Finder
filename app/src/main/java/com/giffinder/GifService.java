package com.giffinder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Gene on 2/3/2015.
 */
public class GifService {

    private String url;
    private static RestAdapter restAdapter;
    private static String key = "dc6zaTOxFJmzC";
    private MainActivity mainActivity;

    public GifService(String url) {
        this.url = url;
        RestAdapter.Builder builder= new RestAdapter.Builder()
                .setEndpoint(url)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestInterceptor.RequestFacade request) {
                        request.addHeader("app_key", "dc6zaTOxFJmzC");
                        //request.add
                    }
                });

        restAdapter = builder.build();

    }

    public void searchGifs(String searchParams, int limit, final MainActivity mainActivity) {
        IGifService gifService = restAdapter.create(IGifService.class);
        gifService.getSearchResults(searchParams, key, limit, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject s, Response response) {
                mainActivity.populateListView(s.toString());
            }

            @Override
            public void failure(RetrofitError error) {
                mainActivity.errorNoRecivingData(error.getBody().toString());
            }
        });

    }

    public void getTrending(final MainActivity mainActivity) {
        IGifService gifService = restAdapter.create(IGifService.class);
        gifService.getTrending(key, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject s, Response response) {
                if (s.equals("[]")) {
                    mainActivity.populateListView("No results :(");
                } else {
                    GiphyData data = new GiphyData(s.get("data").getAsJsonArray(), mainActivity);
                    mainActivity.parseImages(data.parseGifURLs());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                MainActivity mainActivity = new MainActivity();
                mainActivity.errorNoRecivingData(error.getBody().toString());
            }
        });
    }


}
