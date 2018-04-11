package com.example.marcoscavalcante.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marcoscavalcante.popularmovies.models.Review;

import java.util.ArrayList;

/**
 * Created by marcoscavalcante on 08/04/2018.
 */

public final class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>
{
    private static final String TAG = ReviewAdapter.class.getSimpleName();
    private ArrayList<Review> mReviews;
    private int viewHolderCount;

    public interface OnEntryClickListener
    {
        void onEntryClick(View view, int position);
    }


    public ReviewAdapter( ArrayList<Review> reviews )
    {
        this.mReviews   = reviews;
        viewHolderCount = 0;
    }

    @Override
    public int getItemCount()
    {
        return mReviews != null ? mReviews.size() : 0;
    }


    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_view_item;
        LayoutInflater inflater = LayoutInflater.from( context );
        boolean shouldAttachToParentImmediately = false;

        viewHolderCount++;

        View view = inflater.inflate( layoutIdForListItem, parent, shouldAttachToParentImmediately );
        return new ReviewViewHolder( view );
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position)
    {
        Review review       = mReviews.get( position );
        Context context     = holder.itemView.getContext();
        View view           = holder.itemView;

        TextView textViewAuthor  = (TextView) view.findViewById( R.id.tv_author );
        TextView textViewContent = (TextView) view.findViewById( R.id.tv_review );

        textViewAuthor.setText( review.getAuthor() );
        textViewContent.setText( review.getContent() );
    }


    public class ReviewViewHolder extends RecyclerView.ViewHolder
    {
        public ReviewViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}
