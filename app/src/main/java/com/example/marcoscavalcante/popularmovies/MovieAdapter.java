package com.example.marcoscavalcante.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.example.marcoscavalcante.popularmovies.models.Movie;
import com.example.marcoscavalcante.popularmovies.utils.NetworkUtils;
import com.example.marcoscavalcante.popularmovies.utils.Size;


import java.util.ArrayList;

/**
 * Created by marcoscavalcante on 30/12/2017.
 */

public final class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>
{
    private static final String TAG = MovieAdapter.class.getSimpleName();
    private ArrayList<Movie> mMovies;
    private int viewHolderCount;
    private OnEntryClickListener mOnEntryClickListener;


    public interface OnEntryClickListener
    {
        void onEntryClick(View view, int position);
    }


    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener)
    {
        mOnEntryClickListener = onEntryClickListener;
    }


    public MovieAdapter( ArrayList<Movie> movies )
    {
        this.mMovies  = movies;
        viewHolderCount = 0;
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
        int layoutIdForListItem = R.layout.movie_view_item;
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

        ImageView imageView = view.findViewById( R.id.ivMoviePoster );
        String posterPath   =  NetworkUtils.getPosterUrl( movie.getPosterPath(), Size.original );

        GlideApp
                .with( context )
                .load( posterPath )
                .thumbnail(0.1f)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .placeholder(R.drawable.ic_video_loading)
                .error(R.drawable.ic_error_loading)
                .fallback(R.drawable.ic_resource_null)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into( imageView );

    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView moviePosterView;

        public MovieViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener( this );
            moviePosterView = itemView.findViewById(R.id.ivMoviePoster);
        }


        @Override
        public void onClick(View v)
        {
            if ( mOnEntryClickListener != null )
            {
                mOnEntryClickListener.onEntryClick( v, getLayoutPosition() );
            }
        }
    }


}
