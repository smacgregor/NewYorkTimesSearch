package com.smacgregor.newyorktimessearch.networking;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

/**
 * Created by smacgregor on 2/10/16.
 */
public class ArticleProvider {

    static private final String SEARCH_URL = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
    static private final String SEARCH_KEY = "d77315d3b498a2c803a42418329c43f6:19:74340491";

    private AsyncHttpClient httpClient;

    public ArticleProvider() {
        this.httpClient = new AsyncHttpClient();
    }

    public void getArticles(final String searchQuery, TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        // TODO are we handling spaces correctly - do we need to escape searchQuery?
        params.put("q", searchQuery);
        params.put("api-key", SEARCH_KEY);
        params.put("page", 0);
        httpClient.get(SEARCH_URL, params, handler);
    }


}
