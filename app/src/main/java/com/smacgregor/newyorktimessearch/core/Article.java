package com.smacgregor.newyorktimessearch.core;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.net.URISyntaxException;
import java.util.List;

import cz.msebera.android.httpclient.client.utils.URIBuilder;

/**
 * Created by smacgregor on 2/9/16.
 */
@Parcel
public class Article {

    Headline headline;
    String webUrl;

    @SerializedName("multimedia")
    List<Thumbnail> thumbnails;

    @SerializedName("abstract")
    String shortSummary;

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

    public String getShortHeadline() {
        return headline != null ? headline.getKicker() : null;
    }

    public List<Thumbnail> getThumbnails() {
        return thumbnails;
    }

    @Parcel
    public static class Headline {
        String main;
        String kicker;
        public String getMain() {
            return main;
        }
        public String getKicker() { return kicker; }
    }
}
