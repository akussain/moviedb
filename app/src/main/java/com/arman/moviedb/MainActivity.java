package com.arman.moviedb;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arman.moviedb.utilities.MovieDbJsonUtils;
import com.arman.moviedb.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    final static int NUMBER_OF_COLUMNS = 2;

    @BindView(R.id.rv_movies) RecyclerView mRecyclerView;
    private MovieAdapter movieAdapter;

    @BindView(R.id.tv_error_message_display) TextView mErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        movieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(movieAdapter);

        loadMovies(SortType.MOST_POPULAR);
    }

    private void loadMovies(SortType sortType) {
        SortType[] params = {sortType};
        new FetchMovies().execute(params);
    }

    @Override
    public void onClick(Movie movie) {
        Class destinationClass = DetailActivity.class;
        Intent intentDetailActivity = new Intent(this, destinationClass);
        intentDetailActivity.putExtra("movie", movie);
        startActivity(intentDetailActivity);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort, menu);
        return true;
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
                movies = MovieDbJsonUtils.getMoviesListFromJson(MainActivity.this, jsonMoviesResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if(movies != null && movies.size() > 0) {
                showMovieDataView();
                movieAdapter.setMovies(movies);
            } else {
                showErrorMessage();
            }
        }
    }
}