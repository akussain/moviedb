package com.arman.moviedb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements MovieListFragment.Callback {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.moviedb_linear_layout) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_container, new MovieDetailsFragment())
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public void onMoviesLoaded(Movie movie) {
        if (mTwoPane) {
            loadMovieDetailsFragment(movie);
        }
    }

    @Override
    public void onMovieClicked(Movie movie) {
        if (mTwoPane) {
            loadMovieDetailsFragment(movie);
        } else {
            startMovieDetailsActivity(movie);
        }
    }

    private void startMovieDetailsActivity(Movie movie) {
        Class destinationClass = DetailActivity.class;
        Intent intentDetailActivity = new Intent(this, destinationClass);
        intentDetailActivity.putExtra(MovieDetailsFragment.MOVIE, movie);
        startActivity(intentDetailActivity);
    }

    private void loadMovieDetailsFragment(Movie movie) {
        MovieDetailsFragment newFragment = new MovieDetailsFragment();
        newFragment.setMovie(movie);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_container, newFragment)
                .commit();
    }
}