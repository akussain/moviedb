package com.arman.moviedb.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.arman.moviedb.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akussainov on 3/11/17.
 */

public class FavoritesService {

    public List<Movie> getFavoriteMovies(SQLiteDatabase favoritesDb) {
        Cursor cursor = favoritesDb.query(FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        List<Movie> favoriteMovies = new ArrayList<>(cursor.getCount());

        return favoriteMovies;
    }

    public void addMovieToFavorites(SQLiteDatabase favoritesDb, Movie movie) {
        if(movie != null) {
            ContentValues cv = new ContentValues();
            cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID, movie.getId());
            cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_OVERVIEW, movie.getOverview());
            cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
            cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
            cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TITLE, movie.getTitle());
            cv.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_USER_RATING, movie.getUserRating());
            favoritesDb.insert(FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME, null, cv);
        }
    }
}
