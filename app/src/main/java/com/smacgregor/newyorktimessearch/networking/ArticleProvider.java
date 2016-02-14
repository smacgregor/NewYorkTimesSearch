package com.smacgregor.newyorktimessearch.networking;

import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.smacgregor.newyorktimessearch.searching.SearchFilter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    /**
     * Fetch another page of articles for an existing search.
     * @param searchQuery
     * @param searchFilter
     * @param pageNumber
     * @param handler
     */
    public void fetchMoreArticlesForSearch(final String searchQuery, final SearchFilter searchFilter, final int pageNumber, TextHttpResponseHandler handler) {
        getArticles(searchQuery, searchFilter, pageNumber, handler);
    }

    /**
     * Search for articles matching searchQuery and searchFilter
     * @param searchQuery
     * @param searchFilter
     * @param handler
     */
    public void searchForArticles(final String searchQuery, final SearchFilter searchFilter, TextHttpResponseHandler handler) {
        // fetch the first page of results
        getArticles(searchQuery, searchFilter, 0, handler);
    }

    private void getArticles(final String searchQuery, final SearchFilter searchFilter, final int pageNumber, TextHttpResponseHandler handler) {
        RequestParams params = buildRequestParams(searchQuery, searchFilter, pageNumber);
        httpClient.get(SEARCH_URL, params, handler);
    }

    private RequestParams buildRequestParams(final String searchQuery, final SearchFilter searchFilter, int pageNumber) {
        RequestParams params = new RequestParams();
        params.put("q", searchQuery);
        params.put("api-key", SEARCH_KEY);
        params.put("page", pageNumber);

        final String encodedStartDate = encodedStartDate(searchFilter.getStartDate());
        if (!TextUtils.isEmpty(encodedStartDate)) {
            params.put("begin_date", encodedStartDate);
        }

        final String encodedDesks = encodeNewsDesks(searchFilter);
        if (!TextUtils.isEmpty(encodedDesks)) {
            params.put("fq", encodedDesks);
        }

        final String encodedSortOrder = encodeSortOrder(searchFilter.getSortOrder());
        if (!TextUtils.isEmpty(encodedSortOrder)) {
            params.put("sort", encodedSortOrder);
        }

        return params;
    }

    private final String encodeSortOrder(SearchFilter.SortOrder sortOrder) {
        if (sortOrder != SearchFilter.SortOrder.RELEVANCE) {
            return sortOrder == SearchFilter.SortOrder.ASCENDING ? "oldest" : "newest";
        } else {
            return null;
        }
    }

    private final String encodedStartDate(final Calendar calendar) {
        String encodedDate = null;
        if (calendar != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            encodedDate = dateFormat.format(calendar.getTime());
        }
        return encodedDate;
    }

    private final String encodeNewsDesks(final SearchFilter searchFilter) {
        String desks = new String();
        for (SearchFilter.NewsDesks newsDesk : SearchFilter.NewsDesks.values()) {
            if (searchFilter.isNewsDeskEnabled(newsDesk)) {
                desks += newsDesk.toString() + " ";
            }
        }

        if (!TextUtils.isEmpty(desks)) {
            desks = String.format("news_desk:(%s)", desks);
        }
        return desks;
    }
}
