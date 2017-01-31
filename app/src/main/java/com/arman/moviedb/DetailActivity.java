package com.arman.moviedb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private Movie movie;
    private ImageView moviePosterImageView;
    private TextView mDisplayMovieTitle, mDisplayMovieOverview, mDisplayMovieReleaseDate, mDisplayMovieUserRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        moviePosterImageView = (ImageView) findViewById(R.id.iv_display_poster);
        mDisplayMovieTitle = (TextView) findViewById(R.id.tv_display_movie_title);
        mDisplayMovieOverview = (TextView) findViewById(R.id.tv_display_movie_overview);
        mDisplayMovieReleaseDate = (TextView) findViewById(R.id.tv_display_movie_release_date);
        mDisplayMovieUserRating = (TextView) findViewById(R.id.tv_display_movie_rating);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("movie")) {
                movie = (Movie) intentThatStartedThisActivity.getParcelableExtra("movie");
                mDisplayMovieTitle.setText(movie.getTitle());
                mDisplayMovieOverview.setText(movie.getOverview());
                mDisplayMovieReleaseDate.setText(String.format(getString(R.string.release_date), movie.getReleaseDate()));
                mDisplayMovieUserRating.setText(movie.getUserRating());
                String moviePosterPath = movie.getPosterPath();
                Picasso.with(this).load(moviePosterPath).into(moviePosterImageView);
            }
        }
    }
}
