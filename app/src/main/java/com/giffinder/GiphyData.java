package com.giffinder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Gene on 2/6/2015.
 */
public class GiphyData {

    private ImageView[] imageViews;
    //array of json objects - each one is a gif
    private JsonObject[] jsonGifs;
    //array of string urls - each one is for one gif
    private String[] urls;
    //json array with a bunch of jsonGifs
    private JsonArray json;
    //array of json objects - each one contains all image for one gif
    private JsonObject[] jsonGifImages;
    //array of json objects - each one contains details about one url
    private JsonObject[] jsonUrls;
    //context
    private MainActivity mainActivity;
    //images
    private Drawable[] images;


    public GiphyData(JsonArray json, MainActivity mainActivity) {
        this.json = json;
        this.mainActivity = mainActivity;
        jsonGifs = new JsonObject[json.size()+1];
        jsonGifImages = new JsonObject[json.size()+1];
        urls = new String[json.size()+1];
        jsonUrls = new JsonObject[json.size()+1];
        imageViews = new ImageView[json.size()+1];
    }

    public String[] parseGifURLs() {
        for (int i = 0; i < json.size(); i++) {
            jsonGifs[i] = json.get(i).getAsJsonObject();
            jsonGifImages[i] = jsonGifs[i].get("images").getAsJsonObject();
            jsonUrls[i] = jsonGifImages[i].get("original").getAsJsonObject();
            urls[i] = jsonUrls[i].get("url").getAsString();
        }
        return urls;
    }
}
