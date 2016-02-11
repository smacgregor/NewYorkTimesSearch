package com.smacgregor.newyorktimessearch.core;

import android.text.TextUtils;

import org.parceler.Parcel;

/**
 * Created by smacgregor on 2/9/16.
 */
@Parcel
public class Thumbnail {

    String url;
    String type;
    int width;
    int height;

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
