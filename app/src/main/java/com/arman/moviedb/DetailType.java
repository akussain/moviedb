package com.arman.moviedb;

/**
 * Created by arman on 08/03/17.
 */

public enum DetailType {

    REVIEWS("reviews"), VIDEOS("videos");

    private String path;

    DetailType(String value) {
        path = value;
    }

    public String getPath() {
        return path;
    }
}