package com.example.android.finfeeds;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FinFeedActivity extends AppCompatActivity
implements LoaderCallbacks<List<FinFeed>> {

    //Adapter for the FinFeed list
    private FinFeedAdpater mAdapter;
    //URL for the FinFeed request on the Guardian API and use its database for the app
    private static final String URL_FinFeed =
            "https://content.guardianapis.com/search?api-key=b8f4ba19-c5f0-4bed-93eb-35e974569798";

    //add a loader ID, to be able to distinguish between different loaders
    private static final int FinFeed_LOADER_ID = 1;

    //TextView, which is visible if the list is empty
    private TextView mListEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finfeed_activity);

        // Find a reference to the ListView in the layout
        ListView finFeedListView = (ListView) findViewById(R.id.list);

        // find a reference to the ListEmptyStateTextView in the layout and set the emptyView on the finfeedListView
        mListEmptyStateTextView = (TextView) findViewById(R.id.emptyView);
        finFeedListView.setEmptyView(mListEmptyStateTextView);


        // Create a new adapter that takes an empty list as input
        mAdapter = new FinFeedAdpater(this, new ArrayList<FinFeed>());

        // Set the adapter on the ListView
        // so the list can be populated in the user interface
        finFeedListView.setAdapter(mAdapter);
        // item click listener on the ListView, which sends an intent to the web browser in order to receive detailed information about the News
        finFeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                FinFeed currentFinFeed = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri finfeedUri = Uri.parse(currentFinFeed.getWebUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, finfeedUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected())

        {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(FinFeed_LOADER_ID, null, this);
        } else

        {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loadingIndicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mListEmptyStateTextView.setText(R.string.noInternetConnection);
        }
    }

    @Override
    public Loader<List<FinFeed>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader
        return new FinFeedLoader(this, URL_FinFeed);
    }

    //**@Override
    public void onLoadFinished(Loader<List<FinFeed>> loader, List<FinFeed> finFeeds) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loadingIndicator);
        loadingIndicator.setVisibility(View.GONE);
        //Set listEmptyStateTextView to "No news found"
        mListEmptyStateTextView.setText(R.string.noFinFeeds);
        //Clear the adapter
        mAdapter.clear();
        //update ListView, if there is a list of FinFeeds to add to the adapter
        if (finFeeds != null && !finFeeds.isEmpty()) {
            mAdapter.addAll(finFeeds);
        }
    }

    //reset loader to clear the data
    @Override
    public void onLoaderReset(Loader<List<FinFeed>> loader) {
        mAdapter.clear();
    }
}




