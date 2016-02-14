package com.smacgregor.newyorktimessearch.viewing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.smacgregor.newyorktimessearch.R;
import com.smacgregor.newyorktimessearch.core.Article;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArticleActivity extends AppCompatActivity {

    private static final String EXTRA_ARTICLE = "com.smacgregor.newyorktimessearch.viewing.article";

    @Bind(R.id.web_full_article) WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupWebView();
        Article article = (Article) Parcels.unwrap(getIntent().getParcelableExtra(ArticleActivity.EXTRA_ARTICLE));
        setTitle(article.getShortHeadline());
        loadWebView(article.getWebUrl());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_item_share);
        ShareActionProvider shareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mWebView.getUrl());
        shareAction.setShareIntent(shareIntent);
        return super.onCreateOptionsMenu(menu);
    }

    public static Intent getStartIntent(Context context, Article article) {
        Intent startIntent = new Intent(context, ArticleActivity.class);
        startIntent.putExtra(ArticleActivity.EXTRA_ARTICLE, Parcels.wrap(article));
        return startIntent;
    }

    private void setupWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setLoadsImagesAutomatically(true);
        settings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new ArticleBrowser());
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
    }

    private void loadWebView(final String articleURL) {
        if (!TextUtils.isEmpty(articleURL)) {
            mWebView.loadUrl(articleURL);
        }
    }

    private class ArticleBrowser extends WebViewClient {
        @Override
        /**
         * Load the webview inside the activity
         */
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
