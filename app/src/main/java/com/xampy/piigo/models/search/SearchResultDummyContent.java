package com.xampy.piigo.models.search;

import com.xampy.piigo.views.menu.MainMenuFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchResultDummyContent {


    public static List<SearchResultDummyItem> SEARCH_RESULTS_ITEMS =
            new ArrayList<SearchResultDummyItem>();


    public static Map<String, SearchResultDummyItem> SEARCH_RESULTS_ITEMS_MAP =
            new HashMap<String, SearchResultDummyItem>();


    public static int SEARCH_RESULT_COUNT = 10;

    /*static {
        // Add some sample items.
        for (int i = 0; i < SEARCH_RESULT_COUNT; i++) {
            addSearchResultItem(
                    createDummyItem(String.valueOf(i), "TG " + i + 1000 , "url")
            );
        }
    }*/


    public static void addSearchResultItem(SearchResultDummyItem item){
        SEARCH_RESULTS_ITEMS.add(item);
        SEARCH_RESULTS_ITEMS_MAP.put(item.mId, item);
    }


    public static  SearchResultDummyItem createDummyItem(String id, String identifier, String url,
                                                         double mlong, double mlal,
                                                         String minAgo, int distance){
        return new SearchResultDummyItem(
                id, identifier,
                url, mlong, mlal, minAgo, distance);
    }


    public static  class SearchResultDummyItem {

        public final String mId;
        public final String mImageUrl;
        public final String mIdentifier;
        public final double mLal;
        public final double mLong;
        public final int mDistane;
        public final String mMinAgo;

        public SearchResultDummyItem(String id, String identifier,
                                     String image_url, double mlong, double mlal,
                                     String minAgo, int distance){
            mId = id;
            mIdentifier = identifier;
            mImageUrl = image_url;
            mLong = mlong; mLal = mlal;
            mMinAgo = minAgo;

            mDistane = distance;
        }
    }
}
