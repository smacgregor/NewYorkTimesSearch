package com.smacgregor.newyorktimessearch.searching;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.loopj.android.http.TextHttpResponseHandler;
import com.smacgregor.newyorktimessearch.R;
import com.smacgregor.newyorktimessearch.core.Article;
import com.smacgregor.newyorktimessearch.core.ArticlesResponse;
import com.smacgregor.newyorktimessearch.networking.ArticleProvider;
import com.smacgregor.newyorktimessearch.viewing.ArticleActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements
        AdapterView.OnItemClickListener,
        SearchView.OnQueryTextListener,
        SearchFilterDialog.OnFragmentInteractionListener
{

    @Bind(R.id.gridView) GridView searchResultsView;
    SearchView mSearchView;

    private List<Article> mArticles;
    private ArticleArrayAdapter mArticleArrayAdapter;
    private ArticleProvider mArticleProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mArticles = new ArrayList<>();
        mArticleArrayAdapter = new ArticleArrayAdapter(this, mArticles);
        mArticleProvider = new ArticleProvider();

        searchResultsView.setOnItemClickListener(this);
        searchResultsView.setAdapter(mArticleArrayAdapter);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showSearchFilterDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void showSearchFilterDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SearchFilterDialog searchFilterDialog = SearchFilterDialog.newInstance("foo", "cheese");
        searchFilterDialog.show(fragmentManager, "fragment_search_filter");
    }

    private void search(final String searchQuery) {
        mArticleProvider.getArticles(searchQuery, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG", responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d("DEBUG", responseString);
                ArticlesResponse articleResponse = ArticlesResponse.parseJSON(responseString);
                if (articleResponse != null) {
                    mArticleArrayAdapter.addAll(articleResponse.getArticles());
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Article article = mArticleArrayAdapter.getItem(position);
        Intent intent = ArticleActivity.getStartIntent(this, article);
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mSearchView.clearFocus();
        if (!TextUtils.isEmpty(query)) {
            search(query);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
