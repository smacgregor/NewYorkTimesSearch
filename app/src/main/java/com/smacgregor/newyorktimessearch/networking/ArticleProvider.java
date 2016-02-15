package com.smacgregor.newyorktimessearch.networking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.smacgregor.newyorktimessearch.searching.SearchFilter;

import java.io.IOException;
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
    public void fetchMoreArticlesForSearch(Context context,
                                           final String searchQuery,
                                           final SearchFilter searchFilter,
                                           final int pageNumber,
                                           TextHttpResponseHandler handler) {
        getArticles(context, searchQuery, searchFilter, pageNumber, handler);
    }

    /**
     * Search for articles matching searchQuery and searchFilter
     * @param searchQuery
     * @param searchFilter
     * @param handler
     */
    public void searchForArticles(Context context,
                                  final String searchQuery,
                                  final SearchFilter searchFilter,
                                  TextHttpResponseHandler handler) {
        // fetch the first page of results
        getArticles(context, searchQuery, searchFilter, 0, handler);
    }

    private void getArticles(Context context,
                             final String searchQuery,
                             final SearchFilter searchFilter,
                             final int pageNumber,
                             TextHttpResponseHandler handler) {

        if (isOnline() && isNetworkAvailable(context)) {
            RequestParams params = buildRequestParams(searchQuery, searchFilter, pageNumber);
            httpClient.get(SEARCH_URL, params, handler);
        } else {
            // TODO - mock up appropriate error response data for the failed request
            // we aren't online. If I had more time I'd make up values for this
            handler.onFailure(0, null, "", null);
        }
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
                desks += "\"" + newsDesk.toString() + "\" ";
            }
        }

        if (!TextUtils.isEmpty(desks)) {
            desks = String.format("news_desk:(%s)", desks);
        }
        return desks;
    }

    /**
     * Report on the availablity of a network connection
     * @return
     */
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }

}
