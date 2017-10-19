package com.arman.moviedb;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arman.moviedb.data.FavoriteMoviesDbHelper;
import com.arman.moviedb.utilities.MovieDbJsonUtils;
import com.arman.moviedb.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.arman.moviedb.Video.getThumbnailUrl;

public class MovieDetailsFragment extends Fragment implements View.OnClickListener {

    public static final String MOVIE = "movie";

    private Movie mMovie;
    private FavoriteMoviesDbHelper mDbHelper;

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
    @BindView(R.id.favorite)
    FloatingActionButton mFavoritesBtn;

    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, rootView);

        mDbHelper = new FavoriteMoviesDbHelper(getContext());

        if (mMovie != null) {
            mDisplayMovieTitle.setText(mMovie.getTitle());
            mDisplayMovieOverview.setText(mMovie.getOverview());
            mDisplayMovieReleaseDate.setText(String.format(getString(R.string.release_date), mMovie.getReleaseDate()));
            mDisplayMovieUserRating.setText(mMovie.getUserRating());
            if(isFavoriteMovie(mMovie.getId())) {
                showFavoriteButton();
                mMovie.setFavorite(true);
            }
            String moviePosterPath = mMovie.getBackdropPath();
            Picasso.with(getContext())
                    .load(moviePosterPath)
                    .placeholder(R.drawable.user_placeholder)
                    .error(R.drawable.user_placeholder_error)
                    .into(moviePosterImageView);

            new FetchMovieTrailers(String.valueOf(mMovie.getId()), DetailType.VIDEOS).execute();
            new FetchMovieReviews(String.valueOf(mMovie.getId()), DetailType.REVIEWS).execute();

            mFavoritesBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (mMovie.isFavorite()) {
                        if (removeMovieFromFavorites(mMovie.getId())) {
                            mMovie.setFavorite(false);
                            showNotFavoriteButton();
                        }
                    } else {
                        if (addMovieToFavorites(mMovie)) {
                            mMovie.setFavorite(true);
                            showFavoriteButton();
                        }
                    }
                }
            });
        }

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.video_thumb:
                playMovieTrailer(view);
                break;
            case R.id.favorite:
                addMovieToFavorites(mMovie);
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

    private boolean addMovieToFavorites(Movie movie) {
        if (movie != null) {
            return mDbHelper.insertFavoriteMovie(movie) > 0;
        }
        return false;
    }

    private boolean removeMovieFromFavorites(int movieId) {
        return mDbHelper.deleteFavoriteMovie(movieId) > 0;
    }

    private boolean isFavoriteMovie(int movieId) {
        return mDbHelper.isFavorite(movieId) > 0;
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
            LayoutInflater inflater = getActivity().getLayoutInflater();
            Picasso picasso = Picasso.with(getContext());
            for(Video trailer : trailers) {
                View trailerThumbContainer = inflater.inflate(R.layout.video, this.trailers, false);
                ImageView trailerThumbView = ButterKnife.findById(trailerThumbContainer, R.id.video_thumb);
                trailerThumbView.setTag(Video.getVideoUrl(trailer));
                trailerThumbView.requestLayout();
                trailerThumbView.setOnClickListener(this);
                picasso
                        .load(Video.getThumbnailUrl(trailer))
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
            LayoutInflater inflater = getActivity().getLayoutInflater();
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

    private void showFavoriteButton() {
        mFavoritesBtn.setImageResource(R.drawable.ic_favorite);
    }

    private void showNotFavoriteButton() {
        mFavoritesBtn.setImageResource(R.drawable.ic_non_favorite);
    }

    public void setMovie(Movie movie) {
        mMovie = movie;
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
                videos = MovieDbJsonUtils.getVideosListFromJson(getActivity(), jsonMoviesResponse);
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
                reviews = MovieDbJsonUtils.getReviewsListFromJson(getActivity(), jsonMoviesResponse);
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