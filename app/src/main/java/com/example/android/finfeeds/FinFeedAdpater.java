package com.example.android.finfeeds;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**A FinFeedAdapter knows how to create a list out of the FinFeeds, which are available in the dataset. The FinFeedAdapter
 * therefore extends the class ArrayAdapter, which provides a collection of objects placed in a specific order, e.g. predefined by a listview */

public class FinFeedAdpater extends ArrayAdapter <FinFeed> {

    //construct an new FinFeedAdapter @param context of the app, @param finFeeds: List FinFeed= data source of the Adapter

    public FinFeedAdpater(Context context, List<FinFeed> finFeeds) {
        super(context, 0, finFeeds);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (=convertView)
        // otherwise, if convertView == null, inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.finfeed_item, parent, false);
        }

        //Find the position of the FinFeed in the ListView
        FinFeed currentFinFeed = getItem(position);

        //Find the starRating with ID ratingCircle
        TextView starRating = (TextView) listItemView.findViewById(R.id.ratingCircle);

        //Find the type of the FinFeed with ID type
        TextView section = (TextView) listItemView.findViewById(R.id.section);

        //find the referenceType of the FinFeed with ID referenceType
        TextView referenceType = (TextView) listItemView.findViewById(R.id.referenceType);

        // Create a new Date object from the time in milliseconds of the earthquake
        Date useDateObject = new Date(currentFinFeed.getUseDate());
        //find the date of the FinFeed with ID date
        TextView useDateView = (TextView) listItemView.findViewById(R.id.useDate);
        // Format the date string (i.e. "Mar 3, 1984")
        String formattedDate = formatDate(useDateObject);
        // Display the date of the current earthquake in that TextView
        useDateView.setText(formattedDate);


        //find the headline of the FinFeed with ID headline
        TextView webTitle = (TextView) listItemView.findViewById(R.id.webTitle);

        return listItemView;
    }
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

}

