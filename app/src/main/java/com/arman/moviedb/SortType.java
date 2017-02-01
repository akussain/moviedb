package com.arman.moviedb;

/**
 * Created by arman on 31/01/17.
 */

public enum SortType {

    MOST_POPULAR("https://api.themoviedb.org/3/movie/popular"), HIGHEST_RATED("https://api.themoviedb.org/3/movie/top_rated");

    private String url;

    SortType(String value) {
        url = value;
    }

    public String getUrl() {
        return url;
    }
}