package com.arman.moviedb;

public enum SortType {

    MOST_POPULAR("https://api.themoviedb.org/3/movie/popular"), HIGHEST_RATED("https://api.themoviedb.org/3/movie/top_rated"), FAVORITES("favorites");

    private String url;

    SortType(String value) {
        url = value;
    }

    public String getUrl() {
        return url;
    }
}