package com.example.marcoscavalcante.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.example.marcoscavalcante.popularmovies.models.Trailer;
import com.example.marcoscavalcante.popularmovies.utils.NetworkUtils;

import java.util.ArrayList;

/**
 * Created by marcoscavalcante on 08/04/2018.
 */

public final class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>
{
    private static final String TAG = TrailerAdapter.class.getSimpleName();
    private ArrayList<Trailer> mTrailers;
    private int viewHolderCount;
    private TrailerAdapter.OnEntryClickListener mOnEntryClickListener;



    public interface OnEntryClickListener
    {
        void onEntryClick(View view, int position);
    }

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener)
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
        int layoutIdForListItem = R.layout.trailer_view_item;
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

        ImageView imageView = view.findViewById( R.id.iv_trailer_thumbnail );
        String youtubeThumbnail   =  NetworkUtils.getYoutubeVideoThumbnail( trailer.getKey() );

        GlideApp
                .with( context )
                .load( youtubeThumbnail )
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .placeholder(R.drawable.ic_video_loading)
                .error(R.drawable.ic_error_loading)
                .fallback(R.drawable.ic_resource_null)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into( imageView );

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
