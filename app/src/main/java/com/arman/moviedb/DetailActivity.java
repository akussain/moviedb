package com.arman.moviedb;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arman.moviedb.data.FavoriteMoviesContract;
import com.arman.moviedb.data.FavoriteMoviesDbHelper;
import com.arman.moviedb.utilities.MovieDbJsonUtils;
import com.arman.moviedb.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Movie movie;
    private SQLiteDatabase favoritesDb;

    @BindView(R.id.iv_display_poster)
    ImageView moviePosterImageView;
    @BindView(R.id.tv_display_movie_title)
    TextView mDisplayMovieTitle;
    @BindView(R.id.tv_display_movie_overview)
    TextView mDisplayMovieOverview;
    @BindView(R.id.tv_display_movie_release_date)
    TextView mDisplayMovieReleaseDate;
    @BindView(R.id.tv_display_movie_rating)
    TextView mDisplayMovieUserRating;
    @BindView(R.id.tvTrailersLabel)
    TextView mTrailersLabel;
    @BindView(R.id.trailers_container)
    HorizontalScrollView horizontalScrollView;
    @BindView(R.id.trailers)
    LinearLayout trailers;
    @BindView(R.id.tvReviewsLabel)
    TextView mReviewsLabel;
    @BindView(R.id.reviews)
    LinearLayout reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        FavoriteMoviesDbHelper dbHelper = new FavoriteMoviesDbHelper(this);
        favoritesDb = dbHelper.getWritableDatabase();

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

                new FetchMovieTrailers(String.valueOf(movie.getId()), DetailType.VIDEOS).execute();
                new FetchMovieReviews(String.valueOf(movie.getId()), DetailType.REVIEWS).execute();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_thumb:
                playMovieTrailer(view);
                break;
            case R.id.favorite:
                addMovieToFavorites(movie);
                break;
            default:
                break;
        }
    }

    private void playMovieTrailer(View view) {
        String videoUrl = (String) view.getTag();
        Intent playTrailerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        startActivity(playTrailerIntent);
    }

    private void addMovieToFavorites(Movie movie) {
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

    private void showTrailers(List<Video> trailers) {
        if(trailers == null || trailers.isEmpty()) {
            mTrailersLabel.setVisibility(View.GONE);
            this.trailers.setVisibility(View.GONE);
            horizontalScrollView.setVisibility(View.GONE);
        } else {
            mTrailersLabel.setVisibility(View.VISIBLE);
            this.trailers.setVisibility(View.VISIBLE);
            horizontalScrollView.setVisibility(View.VISIBLE);

            this.trailers.removeAllViews();
            LayoutInflater inflater = this.getLayoutInflater();
            Picasso picasso = Picasso.with(this);
            for(Video trailer : trailers) {
                View trailerThumbContainer = inflater.inflate(R.layout.video, this.trailers, false);
                ImageView trailerThumbView = ButterKnife.findById(trailerThumbContainer, R.id.video_thumb);
                trailerThumbView.setTag(trailer.getVideoUrl(trailer));
                trailerThumbView.requestLayout();
                trailerThumbView.setOnClickListener(this);
                picasso
                        .load(trailer.getVideoId())
                        .resizeDimen(R.dimen.video_item_size, R.dimen.video_item_size)
                        .centerCrop()
                        .placeholder(R.color.colorPrimary)
                        .into(trailerThumbView);
                this.trailers.addView(trailerThumbContainer);
            }
        }
    }

    private void showReviews(List<Review> reviews) {
        if(reviews == null || reviews.isEmpty()) {
            mReviewsLabel.setVisibility(View.GONE);
            this.reviews.setVisibility(View.GONE);
        } else {
            mReviewsLabel.setVisibility(View.VISIBLE);
            this.reviews.setVisibility(View.VISIBLE);

            this.reviews.removeAllViews();
            LayoutInflater inflater = this.getLayoutInflater();
            for(Review review : reviews ) {
                ViewGroup reviewContainer = (ViewGroup) inflater.inflate(R.layout.review, this.reviews, false);
                TextView tvReviewAuthor = ButterKnife.findById(reviewContainer, R.id.review_auhtor);
                TextView tvReviewContent = ButterKnife.findById(reviewContainer, R.id.review_content);
                tvReviewAuthor.setText(review.getAuthor());
                tvReviewContent.setText(review.getContent());
                this.reviews.addView(reviewContainer);
            }
        }
    }

    public class FetchMovieTrailers extends AsyncTask<Void, Void, ArrayList<Video>> {
        String movieIdString;
        DetailType detailType;

        public FetchMovieTrailers(String mMovieIdString, DetailType mDetailType) {
            this.movieIdString = mMovieIdString;
            this.detailType = mDetailType;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Video> doInBackground(Void... params) {
            URL movieDetailsRequestUrl = NetworkUtils.buildMovieDetailsUrl(movieIdString, detailType);
            ArrayList<Video> videos = null;
            try {
                String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(movieDetailsRequestUrl);
                videos = MovieDbJsonUtils.getVideosListFromJson(DetailActivity.this, jsonMoviesResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return videos;
        }

        @Override
        protected void onPostExecute(ArrayList<Video> videos) {
            showTrailers(videos);
        }
    }

    public class FetchMovieReviews extends AsyncTask<Void, Void, ArrayList<Review>> {
        String movieIdString;
        DetailType detailType;

        public FetchMovieReviews(String mMovieIdString, DetailType mDetailType) {
            this.movieIdString = mMovieIdString;
            this.detailType = mDetailType;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Review> doInBackground(Void... params) {
            URL movieDetailsRequestUrl = NetworkUtils.buildMovieDetailsUrl(movieIdString, detailType);
            ArrayList<Review> reviews = null;
            try {
                String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(movieDetailsRequestUrl);
                reviews = MovieDbJsonUtils.getReviewsListFromJson(DetailActivity.this, jsonMoviesResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return reviews;
        }

        @Override
        protected void onPostExecute(ArrayList<Review> reviews) {
            showReviews(reviews);
        }
    }
}