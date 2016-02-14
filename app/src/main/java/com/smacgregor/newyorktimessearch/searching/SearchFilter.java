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
        RELEVANCE (0),
        DESCENDING (1),
        ASCENDING (2);

        private final int mValue;

        public static SortOrder fromInt(int value) {
            for (SortOrder sortOrder: SortOrder.values()) {
                if (value == sortOrder.toInt()) {
                    return sortOrder;
                }
            }
            return null;
        }

        SortOrder(int value) {
            mValue = value;
        }

        public int toInt() {
            return mValue;
        }
    }

    public enum NewsDesks {
        ARTS ("Arts"),
        FASHION_AND_STYLE ("Fashion & Style"),
        SPORTS ("Sports"),
        FOREIGN ("Foreign");

        private final String mName;

        NewsDesks(String name) {
            mName = name;
        }
        public String toString() {
            return this.mName;
        }
    }

    SortOrder mSortOrder;
    Calendar mStartDate;
    EnumSet<NewsDesks> mNewsDesks;

    public SearchFilter() {
        mNewsDesks = EnumSet.noneOf(NewsDesks.class);
        mSortOrder = SortOrder.RELEVANCE;
    }

    public Calendar getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Calendar startDate) {
        this.mStartDate = startDate;
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

    public void setSortOrder(SortOrder sortOrder) {
        this.mSortOrder = sortOrder;
    }
}
