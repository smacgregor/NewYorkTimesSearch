package com.smacgregor.newyorktimessearch.searching;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.smacgregor.newyorktimessearch.R;
import com.smacgregor.newyorktimessearch.core.Article;
import com.smacgregor.newyorktimessearch.core.ArticlesResponse;
import com.smacgregor.newyorktimessearch.viewing.ArticleActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    static private final String SEARCH_URL = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
    static private final String SEARCH_KEY = "d77315d3b498a2c803a42418329c43f6:19:74340491";

    @Bind(R.id.edit_search_text) EditText searchTextField;
    @Bind(R.id.button_search) Button searchButton;
    @Bind(R.id.gridView) GridView searchResultsView;

    private List<Article> mArticles;
    private ArticleArrayAdapter mArticleArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mArticles = new ArrayList<>();
        mArticleArrayAdapter = new ArticleArrayAdapter(this, mArticles);
        searchResultsView.setOnItemClickListener(this);
        searchResultsView.setAdapter(mArticleArrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onArticleSearch(View view) {
        String searchText = searchTextField.getText().toString();
        // TODO - make sure search text isn't empty - disable the search button
        search(searchText);
    }

    private void search(final String searchQuery) {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        // TODO are we handling spaces correctly - do we need to escape searchQuery?
        params.put("q", searchQuery);
        params.put("api-key", SEARCH_KEY);
        params.put("page", 0);
        httpClient.get(SEARCH_URL, params, new TextHttpResponseHandler() {
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
}
