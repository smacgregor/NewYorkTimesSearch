package com.smacgregor.newyorktimessearch.core;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smacgregor on 2/9/16.
 */
public class ArticlesResponse {

    Response response;

    private class Response {
        List<Article> docs;

        public Response() {
            this.docs = new ArrayList<>();
        }

        public List<Article> getDocs() {
            return docs;
        }
    }

    public ArticlesResponse() {}

    /**
     * Convert a JSON response to a list of articles
     * @param response JSON response
     * @return
     */
    public static ArticlesResponse parseJSON(String response) {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.fromJson(response, ArticlesResponse.class);
    }

    /**
     *
     * @return the list of articles referenced by the JSON
     */
    public List<Article> getArticles() {
        return response.getDocs();
    }
}
