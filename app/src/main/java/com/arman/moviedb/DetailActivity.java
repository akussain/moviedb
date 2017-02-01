package com.arman.moviedb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    private Movie movie;
    @BindView(R.id.iv_display_poster) ImageView moviePosterImageView;
    @BindView(R.id.tv_display_movie_title) TextView mDisplayMovieTitle;
    @BindView(R.id.tv_display_movie_overview) TextView mDisplayMovieOverview;
    @BindView(R.id.tv_display_movie_release_date) TextView mDisplayMovieReleaseDate;
    @BindView(R.id.tv_display_movie_rating) TextView mDisplayMovieUserRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("movie")) {
                movie = (Movie) intentThatStartedThisActivity.getParcelableExtra("movie");
                mDisplayMovieTitle.setText(movie.getTitle());
                mDisplayMovieOverview.setText(movie.getOverview());
                mDisplayMovieReleaseDate.setText(String.format(getString(R.string.release_date), movie.getReleaseDate()));
                mDisplayMovieUserRating.setText(movie.getUserRating());
                String moviePosterPath = movie.getPosterPath();
                Picasso.with(this)
                        .load(moviePosterPath)
                        .placeholder(R.drawable.user_placeholder)
                        .error(R.drawable.user_placeholder_error)
                        .into(moviePosterImageView);
            }
        }
    }
}