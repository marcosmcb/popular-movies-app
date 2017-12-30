package com.example.marcoscavalcante.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.marcoscavalcante.popularmovies.models.Movie;
import com.example.marcoscavalcante.popularmovies.utils.NetworkUtils;
import com.example.marcoscavalcante.popularmovies.utils.Size;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcoscavalcante on 30/12/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>
{
    private static final String TAG = MovieAdapter.class.getSimpleName();
    private ArrayList<Movie> mMovies;
    private static int viewHolderCount;



    public MovieAdapter( ArrayList<Movie> movies )
    {
        this.mMovies  = movies;
        this.viewHolderCount = 0;
    }

    @Override
    public int getItemCount()
    {
        return mMovies != null ? mMovies.size() : 0;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movieview_item;
        LayoutInflater inflater = LayoutInflater.from( context );
        boolean shouldAttachToParentImmediately = false;

        viewHolderCount++;

        View view = inflater.inflate( layoutIdForListItem, parent, shouldAttachToParentImmediately );
        return new MovieViewHolder( view );
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position)
    {
        Movie movie         = mMovies.get( position );
        Context context     = holder.itemView.getContext();
        View view           = holder.itemView;

        ImageView imageView = (ImageView) view.findViewById( R.id.ivMoviePoster );
        String posterPath   =  NetworkUtils.getPosterUrl( movie.getPosterPath(), Size.w185 );

        Picasso.with( context ).load( posterPath ).into( imageView );
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView moviePosterView;

        public MovieViewHolder(View itemView)
        {
            super(itemView);
            moviePosterView = (ImageView) itemView.findViewById(R.id.ivMoviePoster);
        }

    }


}
