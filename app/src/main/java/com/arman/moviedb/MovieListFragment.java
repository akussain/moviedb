package com.arman.moviedb;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arman.moviedb.data.FavoriteMoviesContract;
import com.arman.moviedb.data.FavoriteMoviesDbHelper;
import com.arman.moviedb.utilities.MovieDbJsonUtils;
import com.arman.moviedb.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListFragment extends Fragment implements MovieAdapter.MovieAdapterOnClickHandler {

    final static int NUMBER_OF_COLUMNS = 2;

    private MovieAdapter mMovieAdapter;
    private FavoriteMoviesDbHelper mDbHelper;
    private Callback mCallback;

    @BindView(R.id.rv_movies)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    public interface Callback {
        void onMoviesLoaded(Movie movie);
        void onMovieClicked(Movie movie);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (Callback) context;
    }

    public MovieListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        ButterKnife.bind(this, rootView);

        mDbHelper = new FavoriteMoviesDbHelper(getContext());

        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        loadMovies(SortType.MOST_POPULAR);

        return rootView;
    }

    private void loadMovies(SortType sortType) {
        if (SortType.FAVORITES == sortType) {
            loadFavoriteMovies();
        } else {
            SortType[] params = {sortType};
            new MovieListFragment.FetchMovies().execute(params);
        }
    }

    private void loadFavoriteMovies() {
        showMovieDataView();
        Cursor cursor = mDbHelper.getAllFavorites();
        ArrayList<Movie> favoriteMovies = new ArrayList<>();
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Movie movie = new Movie();
            movie.setId(cursor.getInt(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID)));
            movie.setTitle(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TITLE)));
            movie.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_OVERVIEW)));
            movie.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POSTER_PATH)));
            movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_BACKDROP_PATH)));
            movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_RELEASE_DATE)));
            movie.setUserRating(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_USER_RATING)));
            movie.setFavorite(true);
            favoriteMovies.add(movie);
        }
        cursor.close();
        mMovieAdapter.setMovies(favoriteMovies);
        if (favoriteMovies.size() > 0) {
            onMoviesLoaded(mMovieAdapter.getMovies().get(0));
        }
    }

    private void onMoviesLoaded(Movie movie) {
        mCallback.onMoviesLoaded(movie);
    }

    @Override
    public void onClick(Movie movie) {
        mCallback.onMovieClicked(movie);
    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sort, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_popular:
                loadMovies(SortType.MOST_POPULAR);
                return true;
            case R.id.sort_highest_rated:
                loadMovies(SortType.HIGHEST_RATED);
                return true;
            case R.id.sort_favorites:
                loadMovies(SortType.FAVORITES);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class FetchMovies extends AsyncTask<SortType, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(SortType... params) {
            SortType sortType = params[0];
            URL moviesRequestUrl = NetworkUtils.buildUrl(sortType);
            ArrayList<Movie> movies = null;
            try {
                String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);
                movies = MovieDbJsonUtils.getMoviesListFromJson(getActivity(), jsonMoviesResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movies != null && movies.size() > 0) {
                showMovieDataView();
                mMovieAdapter.setMovies(movies);
                onMoviesLoaded(mMovieAdapter.getMovies().get(0));
            } else {
                showErrorMessage();
            }
        }
    }
}