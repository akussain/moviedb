package com.arman.moviedb.utilities;

import android.content.Context;

import com.arman.moviedb.Movie;
import com.arman.moviedb.Review;
import com.arman.moviedb.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieDbJsonUtils {

    public static ArrayList<Movie> getMoviesListFromJson(Context context, String moviesJsonString) throws JSONException {

        final String MOVIE_LIST = "results";
        final String POSTER_PATH = "poster_path";
        final String BACKDROP_PATH = "backdrop_path";
        final String ORIGINAL_TITLE = "original_title";
        final String RELEASE_DATE = "release_date";
        final String MOVIE_ID = "id";
        final String USER_RATING = "vote_average";
        final String OVERVIEW = "overview";
        final String IMAGE_BASE_PATH = "https://image.tmdb.org/t/p/w342";
        final String BACKDROP_BASE_PATH = "https://image.tmdb.org/t/p/w780";

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
            movie.setBackdropPath(BACKDROP_BASE_PATH + movieJson.getString(BACKDROP_PATH));
            movie.setUserRating(movieJson.getString(USER_RATING));
            movies.add(movie);
        }

        return movies;
    }

    public static ArrayList<Video> getVideosListFromJson(Context context, String videosJsonString) throws JSONException {

        final String VIDEO_LIST = "results";
        final String MOVIE_ID = "id";
        final String VIDEO_ID = MOVIE_ID;
        final String NAME = "name";
        final String SITE = "site";
        final String SIZE = "size";
        final String TYPE = "type";
        final String KEY = "key";

        ArrayList<Video> videos = null;

        JSONObject videosJson = new JSONObject(videosJsonString);

        JSONArray videosArray = videosJson.getJSONArray(VIDEO_LIST);
        videos = new ArrayList<>(videosArray.length());

        for(int i = 0; i < videosArray.length(); i++) {
            JSONObject videoJson = videosArray.getJSONObject(i);
            Video video = new Video();
            video.setId(videoJson.getString(MOVIE_ID));
            video.setSite(videoJson.getString(SITE));
            video.setSize(videoJson.getInt(SIZE));
            video.setType(videoJson.getString(TYPE));
            video.setName(videoJson.getString(NAME));
            video.setVideoId(videoJson.getString(VIDEO_ID));
            video.setVideoKey(videoJson.getString(KEY));
            videos.add(video);
        }

        return videos;
    }

    public static ArrayList<Review> getReviewsListFromJson(Context context, String reviewsJsonString) throws JSONException {

        final String REVIEW_LIST = "results";
        final String REVIEW_ID = "id";
        final String AUTHOR = "author";
        final String CONTENT = "content";
        final String URL = "url";

        ArrayList<Review> reviews = null;

        JSONObject reviewsJson = new JSONObject(reviewsJsonString);

        JSONArray reviewsArray = reviewsJson.getJSONArray(REVIEW_LIST);
        reviews = new ArrayList<>(reviewsArray.length());

        for(int i = 0; i < reviewsArray.length(); i++) {
            JSONObject reviewJson = reviewsArray.getJSONObject(i);
            Review review = new Review();
            review.setId(reviewJson.getString(REVIEW_ID));
            review.setAuthor(reviewJson.getString(AUTHOR));
            review.setContent(reviewJson.getString(CONTENT));
            review.setUrl(reviewJson.getString(URL));
            reviews.add(review);
        }

        return reviews;
    }
}