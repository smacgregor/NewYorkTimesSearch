package com.smacgregor.newyorktimessearch.searching;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smacgregor.newyorktimessearch.R;
import com.smacgregor.newyorktimessearch.core.Article;
import com.smacgregor.newyorktimessearch.core.Thumbnail;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by smacgregor on 2/13/16.
 */
public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    OnItemClickListener mOnItemClickListener;
    private List<Article> mArticles;

    public ArticlesAdapter(List<Article> articles) {
        mArticles = articles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View currentView = inflater.inflate(R.layout.item_article_result, parent, false);
        return new ViewHolder(currentView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        prepareViewForReuse(holder);

        Article article = mArticles.get(position);
        holder.headline.setText(article.getHeadline());
        Thumbnail thumbnail = article.getThumbnail();
        if (thumbnail != null && !TextUtils.isEmpty(thumbnail.getUrl())) {
            Picasso.with(holder.imageThumbnail.getContext()).
                    load(thumbnail.getUrl()).
                    into(holder.imageThumbnail);
        }
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    private void prepareViewForReuse(ViewHolder view) {
        view.imageThumbnail.setImageResource(0);
        view.headline.setText("");
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.image_thumbnail) ImageView imageThumbnail;
        @Bind(R.id.text_headline) TextView headline;

        public ViewHolder(View itemView) {
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

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener clickListener) {
        mOnItemClickListener = clickListener;
    }
}
