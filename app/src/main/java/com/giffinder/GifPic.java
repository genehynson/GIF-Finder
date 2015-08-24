package com.giffinder;

import android.media.Image;

import java.util.Date;

/**
 * Created by Gene on 2/3/2015.
 */
public class GifPic {

    private Image img;
    private int width;
    private int height;
    private String format;
    private Date timestamp;
    private String json;
    private String url;

    public GifPic(Image img) {
        this.img = img;
    }

    public GifPic(String json) {
        this.json = json;
    }

    public void setImg(Image img) {
    this.img = img;
}

    public void setWidth(int width) {
    this.width = width;
}

    public void setHeight(int height) {
    this.height = height;
}

    public void setFormat(String format) {
        this.format = format;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Image getImg() {
        return img;
    }

    public String getFormat() {
        return format;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
