package com.arman.moviedb.utilities;

import android.content.Context;

import com.arman.moviedb.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by arman on 28/01/17.
 */

public class MovieDbJsonUtils {

    public static ArrayList<Movie> getMoviesListFromJson(Context context, String moviesJsonString) throws JSONException {

        final String MOVIE_LIST = "results";
        final String POSTER_PATH = "poster_path";
        final String ORIGINAL_TITLE = "original_title";
        final String RELEASE_DATE = "release_date";
        final String MOVIE_ID = "id";
        final String USER_RATING = "vote_average";
        final String OVERVIEW = "overview";
        final String IMAGE_BASE_PATH = "https://image.tmdb.org/t/p/w185_and_h278_bestv2";

        ArrayList<Movie> movies = null;

        JSONObject moviesJson = new JSONObject(moviesJsonString);

        JSONArray moviesArray = moviesJson.getJSONArray(MOVIE_LIST);
        movies = new ArrayList<>(moviesArray.length());

        for(int i = 0; i < moviesArray.length(); i++) {
            JSONObject movieJson = moviesArray.getJSONObject(i);
            Movie movie = new Movie();
            movie.setId(movieJson.getInt(MOVIE_ID));
            movie.setTitle(movieJson.getString(ORIGINAL_TITLE));
            movie.setReleaseDate(movieJson.getString(RELEASE_DATE));
            movie.setOverview(movieJson.getString(OVERVIEW));
            movie.setPosterPath(IMAGE_BASE_PATH + movieJson.getString(POSTER_PATH));
            movie.setUserRating(movieJson.getString(USER_RATING));
            movies.add(movie);
        }

        return movies;
    }
}
