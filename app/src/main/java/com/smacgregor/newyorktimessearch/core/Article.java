package com.smacgregor.newyorktimessearch.core;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by smacgregor on 2/9/16.
 */
public class Article {

    private Headline headline;

    private String webUrl;

    @SerializedName("multimedia")
    private List<Thumbnail> thumbnails;

    @SerializedName("abstract")
    private String shortSummary;

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline != null ? headline.getMain() : null;
    }

    public Thumbnail getThumbnail() {
        return (thumbnails != null && thumbnails.size() > 0) ? thumbnails.get(0) : null;
    }

    private class Headline {

        private String main;

        public String getMain() {
            return main;
        }
    }
}
