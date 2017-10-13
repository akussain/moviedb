package com.arman.moviedb;

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