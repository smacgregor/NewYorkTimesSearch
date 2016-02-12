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
    Calendar mBeginDate;
    EnumSet<NewsDesks> mNewsDesks;

    public Calendar getmBeginDate() {
        return mBeginDate;
    }

    public EnumSet<NewsDesks> getmNewsDesks() {
        return mNewsDesks;
    }

    public SortOrder getSortOrder() {
        return mSortOrder;
    }
}
