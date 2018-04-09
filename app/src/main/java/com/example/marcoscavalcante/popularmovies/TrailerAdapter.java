package com.example.marcoscavalcante.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marcoscavalcante.popularmovies.models.Review;
import com.example.marcoscavalcante.popularmovies.models.Trailer;

import java.util.ArrayList;

/**
 * Created by marcoscavalcante on 08/04/2018.
 */

public final class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>
{
    private static final String TAG = ReviewAdapter.class.getSimpleName();
    private ArrayList<Trailer> mTrailers;
    private int viewHolderCount;
    private TrailerAdapter.OnEntryClickListener mOnEntryClickListener;


    public interface OnEntryClickListener
    {
        void onEntryClick(View view, int position);
    }

    public void setOnEntryClickListener(TrailerAdapter.OnEntryClickListener onEntryClickListener)
    {
        mOnEntryClickListener = onEntryClickListener;
    }


    public TrailerAdapter( ArrayList<Trailer> trailers )
    {
        this.mTrailers  = trailers;
        viewHolderCount = 0;
    }

    @Override
    public int getItemCount()
    {
        return mTrailers != null ? mTrailers.size() : 0;
    }



    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_view_item;
        LayoutInflater inflater = LayoutInflater.from( context );
        boolean shouldAttachToParentImmediately = false;

        viewHolderCount++;

        View view = inflater.inflate( layoutIdForListItem, parent, shouldAttachToParentImmediately );
        return new TrailerViewHolder( view );
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position)
    {
        Trailer trailer     = mTrailers.get( position );
        Context context     = holder.itemView.getContext();
        View view           = holder.itemView;
    }


    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TrailerViewHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener( this );
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
