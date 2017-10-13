package com.arman.moviedb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            Intent intentThatStartedThisActivity = getIntent();

            if (intentThatStartedThisActivity != null) {
                if (intentThatStartedThisActivity.hasExtra(MovieDetailsFragment.MOVIE)) {
                    Movie movie = (Movie) intentThatStartedThisActivity.getParcelableExtra(MovieDetailsFragment.MOVIE);
                    if (movie != null) {
                        MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
                        movieDetailsFragment.setMovie(movie);
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.detail_container, movieDetailsFragment)
                                .commit();
                    }
                }
            }
        }
    }
}