package com.example.android.finfeeds;

//Helper Method for accessing the data from the Guardian API and translating it in order to make the data accessible for the app

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    //Log_Tag in order to check and handle the state of the app.
    // Private, because the Logtag should not be accessible outside the class; Final because it should not be modified
    //Static to save performance, and to make the LogTag accessible without initializing it
    //Log_Tag consists of letters, therefore string;
    //getSimpleName(): Strips down the package name, shows logs referring to actions in the class, when applied
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    //create private constructor which can be just accessed by the Class QueryUtils and not outside in order to secure the request.
    private QueryUtils() {
    }

    //Query Guardian Dataset
    public static List<FinFeed> fetchFinFeedData(String requestUrl) {
        //Create URL object
        URL url = createUrl(requestUrl);
        //Perform HTTP request to the URL and receive JSON response
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with creating the HTTP request", e);
        }
        //Extract relevant field from the JSON response and create a FinFeed List
        List<FinFeed> finFeeds = extractFeaturesFromJson(jsonResponse);
        //Return the list of FinFeeds
        return finFeeds;
    }

    //Return new URL object from String URL
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem with building the URL", e);
        }
        return url;
    }

    //Make HTTP request to the given URL and return String as response.
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        //if the URL = null, then return early;
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /*milliseconds*/);
            urlConnection.setConnectTimeout(15000 /*milliseconds*/);
            urlConnection.setRequestMethod("Get");
            urlConnection.connect();
            //If request = successful: response code 200, read input stream & parse response
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with retrieving FinFeed JSON results", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                //close input stream
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    //Convert the input Stream into a String which contains the whole JSON response from the server.
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //return list of FinFeed objects that has been built up from parsing the JSON response.
    private static List<FinFeed> extractFeaturesFromJson(String finfeedJSON) {
        //if the JSON string = empty or null, return early.
        if (TextUtils.isEmpty(finfeedJSON)) {
            return null;
        }
        //Create an empty ArrayList, to which we can add FinFeeds
        List<FinFeed> finFeeds = new ArrayList<>();
        //Try to parse the JSON response string. if there is a problem, throw JSONException.
        try {
            JSONObject basicJsonResponse = new JSONObject(finfeedJSON);
            JSONArray finfeedArray = basicJsonResponse.getJSONArray("results");
            //For each FinFeed in the finfeedArray, create an FinFeed object
            for (int i = 0; i < finfeedArray.length(); i++) {
                //get a single FinFeed at position i within the list of finFeeds
                JSONObject currentFinFeed = finfeedArray.getJSONObject(i);
                //for a given FinFeed, extract the JSONObject associated with the key called "content"
                JSONObject content = currentFinFeed.getJSONObject("content");
                //Extract the value for the key called "section"
                String section = content.getString("section");
                //Extract the value for the key called "reference-type"
                String referenceType = content.getString("reference-type");
                //Extract the value for the key called "starRating",ranging from 1= low to 5 = high
                int starRating = content.getInt("star-rating");
                //Extract the value for the key called "webTitle"
                String webTitle = content.getString("webTitle");
                //Extract the value for the key called "webUrl"
                String webUrl = content.getString("webUrl");
                String useDate = content.getString("use-date");

                // Create a new FinFeed object with the section, reference-type, star-rating, webTitle, webUrl and use-date
                FinFeed finFeed = new FinFeed(section, referenceType, starRating, webTitle, webUrl, useDate);
                // Add the new {@link Earthquake} to the list of earthquakes.
                finFeeds.add(finFeed);
            }
        } catch (JSONException e) {
            //if there is an error thrown when executing JSON request & parsing, cath the exception in order to avoid a crash.
            //print log message with exception in order to handle the exception.
            Log.e("QueryUtils", "Problem with parsing the FinFeed JSON results", e);
        }
        // return FinFeed List
        return finFeeds;
    }
}



