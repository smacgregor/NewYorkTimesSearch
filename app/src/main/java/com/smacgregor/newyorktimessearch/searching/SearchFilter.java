package com.smacgregor.newyorktimessearch.searching;

import org.parceler.Parcel;

import java.util.Calendar;
import java.util.EnumSet;

/**
 * A filter that can be applied to a search.
 *
 * Created by smacgregor on 2/11/16.
 */

@Parcel
public class SearchFilter {
    public enum SortOrder {
        DESCENDING,
        ASCENDING
    }

    public enum NewsDesks {
        ARTS,
        FASHION_AND_STYLE,
        SPORTS
    }

    SortOrder mSortOrder;
    Calendar mStartDate;
    EnumSet<NewsDesks> mNewsDesks;

    public SearchFilter() {
        mNewsDesks = EnumSet.noneOf(NewsDesks.class);
    }

    public Calendar getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Calendar mStartDate) {
        this.mStartDate = mStartDate;
    }

    public void updateNewsDesk(NewsDesks newsDesk, boolean add) {
        if (add) {
            mNewsDesks.add(newsDesk);
        } else {
            mNewsDesks.remove(newsDesk);
        }
    }

    public boolean isNewsDeskEnabled(NewsDesks newsDesk) {
        return mNewsDesks.contains(newsDesk);
    }

    public SortOrder getSortOrder() {
        return mSortOrder;
    }

    public void setSortOrder(SortOrder mSortOrder) {
        this.mSortOrder = mSortOrder;
    }

}
