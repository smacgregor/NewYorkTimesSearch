package com.smacgregor.newyorktimessearch.searching;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Courtesy of http://blog.grafixartist.com/pinterest-masonry-layout-staggered-grid/
 */
public class ArticleItemDecoration extends RecyclerView.ItemDecoration {
    private final int mArticleSpacing;

    public ArticleItemDecoration(int spacing) {
        mArticleSpacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mArticleSpacing;
        outRect.right = mArticleSpacing;
        outRect.bottom = mArticleSpacing;
        // clever trick to only give the top row top spacing otherwise we end up doubling up
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = mArticleSpacing;
        }
    }
}
