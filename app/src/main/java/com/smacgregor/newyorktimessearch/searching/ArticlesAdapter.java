package com.smacgregor.newyorktimessearch.searching;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.smacgregor.newyorktimessearch.R;
import com.smacgregor.newyorktimessearch.core.Article;
import com.smacgregor.newyorktimessearch.core.Thumbnail;
import com.smacgregor.newyorktimessearch.core.ThumbnailHelpers;
import com.smacgregor.newyorktimessearch.core.ui.DynamicHeightImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by smacgregor on 2/13/16.
 */
public class ArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int ARTICLE_WITH_THUMBNAIL = 0;
    private final int ARTICLE_WITH_NO_THUMBNAIL = 1;

    OnItemClickListener mOnItemClickListener;
    private List<Article> mArticles;

    public ArticlesAdapter(List<Article> articles) {
        mArticles = articles;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View currentView; // = inflater.inflate(R.layout.item_article_result, parent, false);

        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case ARTICLE_WITH_THUMBNAIL:
                currentView = inflater.inflate(R.layout.item_article_result, parent, false);
                viewHolder = new ThumbnailViewHolder(currentView);
                break;
            default:
                currentView = inflater.inflate(R.layout.item_article_result_no_thumbnail, parent, false);
                viewHolder = new NoThumbnailViewHolder(currentView);
                break;

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case ARTICLE_WITH_THUMBNAIL:
                configureThumbnailViewHolder((ThumbnailViewHolder) holder, position);
                break;
            default:
                configureNoThumbnailViewHolder((NoThumbnailViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    @Override
    public int getItemViewType(int position) {
        Thumbnail thumbnail = ThumbnailHelpers.getBestThumbnailForArticle(mArticles.get(position));
        return (thumbnail != null) ? ARTICLE_WITH_THUMBNAIL : ARTICLE_WITH_NO_THUMBNAIL;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener clickListener) {
        mOnItemClickListener = clickListener;
    }

    private void configureThumbnailViewHolder(ThumbnailViewHolder viewHolder, int position) {
        prepareViewForReuse(viewHolder);

        Article article = mArticles.get(position);
        viewHolder.headline.setText(article.getHeadline());

        Thumbnail thumbnail = ThumbnailHelpers.getBestThumbnailForArticle(article);
        if (thumbnail != null && !TextUtils.isEmpty(thumbnail.getUrl())) {
            viewHolder.imageThumbnail.setHeightRatio(((double) thumbnail.getHeight()) / thumbnail.getWidth());
            // Setting a disk cache policy to all isn't necessary for this simple example
            // but good practice.
            Glide.with(viewHolder.imageThumbnail.getContext()).
                    load(thumbnail.getUrl()).
                    diskCacheStrategy(DiskCacheStrategy.ALL).
                    into(viewHolder.imageThumbnail);
        }
    }

    private void configureNoThumbnailViewHolder(NoThumbnailViewHolder viewHolder, int position) {
        Article article = mArticles.get(position);
        viewHolder.headline.setText(article.getHeadline());
    }

    private void prepareViewForReuse(ThumbnailViewHolder view) {
        view.imageThumbnail.setImageResource(0);
        view.headline.setText("");
    }

    public class ThumbnailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.image_thumbnail) DynamicHeightImageView imageThumbnail;
        @Bind(R.id.text_headline) TextView headline;

        public ThumbnailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public class NoThumbnailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.text_headline) TextView headline;

        public NoThumbnailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
