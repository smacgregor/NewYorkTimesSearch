package com.smacgregor.newyorktimessearch.core;

import android.text.TextUtils;

/**
 * Created by smacgregor on 2/9/16.
 */
public class Thumbnail {

    private String url;
    private String type;
    private int width;
    private int height;

    public String getUrl() {
        // TODO - find a better way to construct the thumbnail url
        return !TextUtils.isEmpty(url) ? "http://www.nytimes.com/" + url : "";
    }

    public String getType() {
        return type;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
