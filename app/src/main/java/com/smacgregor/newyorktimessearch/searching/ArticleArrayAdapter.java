package com.smacgregor.newyorktimessearch.searching;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
 * Created by smacgregor on 2/9/16.
 */
public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    static class ViewHolder {
        @Bind(R.id.image_thumbnail) ImageView imageThumbnail;
        @Bind(R.id.text_headline) TextView headline;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Article article = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            // inflate
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_article_result, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            prepareViewForReuse(viewHolder);
        }

        Thumbnail thumbnail = article.getThumbnail();
        if (thumbnail != null && !TextUtils.isEmpty(thumbnail.getUrl())) {
            Picasso.with(getContext()).
                    load(thumbnail.getUrl()).
                    into(viewHolder.imageThumbnail);
        }
        viewHolder.headline.setText(article.getHeadline());

        return convertView;
    }

    private void prepareViewForReuse(ViewHolder view) {
        view.imageThumbnail.setImageResource(0);
        view.headline.setText("");
    }
}
