package com.smacgregor.newyorktimessearch.core;

import com.google.gson.annotations.SerializedName;

import java.net.URISyntaxException;
import java.util.List;

import cz.msebera.android.httpclient.client.utils.URIBuilder;

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
        // The NY Times API returns non mobile article urls.
        String sanitizedUrl = webUrl;
        try {
            URIBuilder builder = new URIBuilder(sanitizedUrl);
            builder.setHost("mobile.nytimes.com");
            sanitizedUrl = builder.toString();
        } catch (URISyntaxException ex) {}
        return sanitizedUrl;
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
