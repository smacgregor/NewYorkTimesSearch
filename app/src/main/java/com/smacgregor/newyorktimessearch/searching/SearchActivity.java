package com.smacgregor.newyorktimessearch.searching;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.loopj.android.http.TextHttpResponseHandler;
import com.smacgregor.newyorktimessearch.R;
import com.smacgregor.newyorktimessearch.core.Article;
import com.smacgregor.newyorktimessearch.core.ArticlesResponse;
import com.smacgregor.newyorktimessearch.core.ui.EndlessRecyclerViewScrollListener;
import com.smacgregor.newyorktimessearch.networking.ArticleProvider;
import com.smacgregor.newyorktimessearch.viewing.ArticleActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

public class SearchActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener,
        SearchFilterDialog.OnSearchFilterFragmentInteractionListener,
        ArticlesAdapter.OnItemClickListener,
        DatePickerDialog.OnDateSetListener {

    @Bind(R.id.recycler_articles) RecyclerView searchResultsView;
    private SearchView mSearchView;
    private SearchFilterDialog mSearchFilterDialogFragment;
    private List<Article> mArticles;
    private ArticlesAdapter mArticlesAdapter;
    private ArticleProvider mArticleProvider;

    private SearchFilter mSearchFilter;
    private String mSearchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        mSearchFilter = new SearchFilter();
        mArticles = new ArrayList<>();
        mArticleProvider = new ArticleProvider();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setupSearchResultsView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_item_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        mSearchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            showSearchFilterDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinishedSavingSearchFilter(SearchFilter searchFilter) {
        mSearchFilter = searchFilter;
        // redo the search if the filter changed
        // TODO - add an equality check to searchFilter to avoid
        // running an unnecessary search if nothing changed
        search(mSearchQuery);
    }

    /**
     * Our recycler list view on item click handler
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {
        Article article = mArticles.get(position);
        Intent intent = ArticleActivity.getStartIntent(this, article);
        startActivity(intent);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        final Calendar date = Calendar.getInstance();
        date.set(year, monthOfYear, dayOfMonth);
        if (mSearchFilterDialogFragment != null) {
            mSearchFilterDialogFragment.updateDate(date);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mSearchView.clearFocus();
        mSearchQuery = query;
        search(mSearchQuery);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mArticlesAdapter.notifyItemRangeRemoved(0, mArticles.size());
            mArticles.clear();
        }
        return false;
    }

    private void setupSearchResultsView() {
        mArticlesAdapter = new ArticlesAdapter(mArticles);
        searchResultsView.setAdapter(new SlideInBottomAnimationAdapter(mArticlesAdapter));
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        searchResultsView.setLayoutManager(staggeredGridLayoutManager);
        searchResultsView.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadMoreSearchResults(page);
            }
        });
        searchResultsView.addItemDecoration(new ArticleItemDecoration(16));
        mArticlesAdapter.setOnItemClickListener(this);
    }

    private void showSearchFilterDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mSearchFilterDialogFragment = SearchFilterDialog.newInstance(mSearchFilter);
        mSearchFilterDialogFragment.show(fragmentManager, "fragment_search_filter");
    }

    private void loadMoreSearchResults(int page) {
        mArticleProvider.fetchMoreArticlesForSearch(this, mSearchQuery, mSearchFilter, page, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG", responseString);
                displayAlertMessage(getResources().getString(R.string.alert_no_internet));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("DEBUG", responseString);
                ArticlesResponse articleResponse = ArticlesResponse.parseJSON(responseString);
                if (articleResponse != null && articleResponse.getArticles().size() > 0) {
                    mArticles.addAll(articleResponse.getArticles());
                    // shouldn't mArticles.size() - 1 be the size of the array of articles we added to mArticles?
                    mArticlesAdapter.notifyItemRangeInserted(mArticlesAdapter.getItemCount(), mArticles.size() - 1);
                } else {
                        displayAlertMessage(getResources().getString(R.string.alert_no_more_search_results));
                }
            }
        });
    }

    private void search(final String searchQuery) {
        // TODO - add a progress spinner
        mArticles.clear();
        mArticlesAdapter.notifyDataSetChanged();
        if (!TextUtils.isEmpty(searchQuery)) {
            mArticleProvider.searchForArticles(this, searchQuery, mSearchFilter, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("DEBUG", responseString);
                    displayAlertMessage(getResources().getString(R.string.alert_no_internet));
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    Log.d("DEBUG", responseString);
                    ArticlesResponse articleResponse = ArticlesResponse.parseJSON(responseString);
                    if (articleResponse != null && articleResponse.getArticles().size() > 0) {
                        mArticles.addAll(articleResponse.getArticles());
                        mArticlesAdapter.notifyItemRangeInserted(0, mArticles.size() - 1);
                    } else {
                        displayAlertMessage(getResources().getString(R.string.alert_no_search_results));
                    }
                }
            });
        }
    }

    /**
     * Alert the user that their internet connection may be down /
     * the server returned an error
     */
    private void displayAlertMessage(final String alertMessage) {
        Snackbar.make(findViewById(android.R.id.content), alertMessage,
                Snackbar.LENGTH_LONG).show();
    }
}
