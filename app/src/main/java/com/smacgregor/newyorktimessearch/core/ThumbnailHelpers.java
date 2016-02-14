package com.smacgregor.newyorktimessearch.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smacgregor on 2/14/16.
 */
public class ThumbnailHelpers {
    /**
     * Filter out thumbnails that are too small to look good on mobile.
     * Ensure an article is always mapped to the same thumbnail image
     * to avoid showing the user a different thumbnail as they scroll back
     * and forth.
     * @param article
     * @return
     */
    public static Thumbnail getBestThumbnailForArticle(Article article) {
        Thumbnail thumbnail = null;

        // Ignore subtype thumbnail as they are too low res to look good on mobile.
        // Ensure a consistent hash between article and desired thumbnail

        // unfortunately Java doesn't support collection filtering.
        // We could add utility method that will do this generically...
        List<Thumbnail> thumbnails = article.getThumbnails();
        List<Thumbnail> reducedThumbnails = new ArrayList<>(thumbnails);
        for (Thumbnail thumbs: thumbnails) {
            if (thumbs.getSubType().equals("thumbnail")) {
                reducedThumbnails.remove(thumbs);
            }
        }

        if (reducedThumbnails != null && reducedThumbnails.size() > 0) {
            // strive for consistency - we want the user to always see the same
            // sized thumbnail for the same story. So don't use random here.
            int hashCode = article.getWebUrl().hashCode();
            int listSize = reducedThumbnails.size();
            thumbnail =  reducedThumbnails.get((hashCode % listSize + listSize) % listSize);
        }
        return thumbnail;
    }
}
