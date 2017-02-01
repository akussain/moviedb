package com.arman.moviedb.utilities;

import android.net.Uri;

import com.arman.moviedb.BuildConfig;
import com.arman.moviedb.SortType;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by arman on 23/01/17.
 */

public class NetworkUtils {

    final static String API_KEY_PARAM = "api_key";
    final static String API_KEY = BuildConfig.MOVIEDB_API_KEY;

    public static URL buildUrl(SortType sortType) {
        String movieDbSearchQuery = sortType.getUrl();
        Uri builtUri = Uri.parse(movieDbSearchQuery).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}