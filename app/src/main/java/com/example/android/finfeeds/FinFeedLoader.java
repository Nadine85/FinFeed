package com.example.android.finfeeds;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class FinFeedLoader extends AsyncTaskLoader<List<FinFeed>> {
    //Query URL
    private String mUrl;

    //Construct New FinFeedLoader, @param context of the activity, @param url for loading data of Guardian
    public FinFeedLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    //Logtag for messages
    private static final String LOG_TAG = FinFeedLoader.class.getName();

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    //Background thread
    @Override
    public List<FinFeed> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        //request network, parse response, extract list of FinFeeds
        List<FinFeed> finFeeds = QueryUtils.fetchFinFeedData(mUrl);
        return finFeeds;
    }
}