package com.arman.moviedb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by arman on 23/01/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private ArrayList<Movie> movies;

    private final MovieAdapterOnClickHandler mClickHandler;
    Context mContext;

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;

    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView movieTextView;
        public final ImageView moviePosterImageView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            movieTextView = (TextView) view.findViewById(R.id.tv_movie_data);
            moviePosterImageView = (ImageView) view.findViewById(R.id.iv_movie_poster);//
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie movie = movies.get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        //int layoutIdForListItem = R.layout.movie_list_item;
        int layoutIdForListItem = R.layout.movie_list_view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if(movies == null) return 0;
        return movies.size();
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        String movieTitle = movies.get(position).getTitle();
        String moviePosterPath = movies.get(position).getPosterPath();
        //movieAdapterViewHolder.movieTextView.setText(movieTitle);
        Picasso.with(mContext).load(moviePosterPath).into(movieAdapterViewHolder.moviePosterImageView);
    }

    public void setMovies(ArrayList<Movie> movieList) {
        movies = movieList;
        notifyDataSetChanged();
    }
}
