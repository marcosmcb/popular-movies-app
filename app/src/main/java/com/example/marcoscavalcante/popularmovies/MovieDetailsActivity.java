package com.example.marcoscavalcante.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcoscavalcante.popularmovies.data.FavouriteContract;
import com.example.marcoscavalcante.popularmovies.models.Movie;
import com.example.marcoscavalcante.popularmovies.models.Review;
import com.example.marcoscavalcante.popularmovies.models.Trailer;
import com.example.marcoscavalcante.popularmovies.utils.NetworkUtils;
import com.example.marcoscavalcante.popularmovies.utils.Size;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by marcoscavalcante on 30/12/2017.
 */

public class MovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    private static final int MOVIE_LOADER_ID = 10;
    private static final int MOVIE_REVIEWS_VIDEOS_LOADER = 11;



    /* Field to store our TextView */
    private ImageView mPoster;
    private TextView  mOverview;
    private TextView  mReleaseDate;
    private TextView  mVoteAverage;
    private FloatingActionButton mFavouriteButton;
    private Movie     mMovie;
    private boolean mIsFavourite;
    private NetworkUtils mNetworkUtils;
    private ArrayList<Review> mReviews;
    private ArrayList<Trailer> mTrailers;


    private LinearLayoutManager mLayoutManagerReviews;
    private LinearLayoutManager mLayoutManagerTrailers;

    private ReviewAdapter mReviewAdapter;
    private TrailerAdapter mTrailerAdapter;

    private RecyclerView mRecyclerViewReviews;
    private RecyclerView mRecyclerViewTrailers;




    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.movie_details, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        mPoster          = findViewById(R.id.iv_poster_details);
        mOverview        = findViewById(R.id.tv_overview_details);
        mReleaseDate     = findViewById(R.id.tv_release_date_details);
        mVoteAverage     = findViewById(R.id.tv_vote_average_details);
        mFavouriteButton = findViewById(R.id.fav_button_movie);
        mNetworkUtils    = new NetworkUtils( this );

        mRecyclerViewReviews = findViewById( R.id.rv_reviews );
        mRecyclerViewTrailers = findViewById( R.id.rv_trailer );

        mReviews = new ArrayList<Review>();
        mTrailers = new ArrayList<Trailer>();

        mReviewAdapter = new ReviewAdapter( mReviews );
        mTrailerAdapter = new TrailerAdapter( mTrailers );

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setRecyclerViewReviews();
        setRecyclerViewTrailers();
        setTrailerAdapterListener();


        Intent movieDetails = getIntent();

        if ( movieDetails != null && movieDetails.hasExtra( "movie" ) )
        {
            try
            {
                JSONObject movieJsonObject = new JSONObject( movieDetails.getStringExtra("movie"));
                mMovie = new Movie( movieJsonObject );

                String posterPath;
                String releaseDate = mMovie.getReleaseDate().substring(0,4);
                setTitle(mMovie.getTitle());
                mOverview.setText( mMovie.getOverview() );
                mVoteAverage.setText( mMovie.getVoteAverage().toString() + " / " + getString(R.string.max_vote_average) );
                mReleaseDate.setText( releaseDate );
                mFavouriteButton.setOnClickListener( onClickListener );

                getSupportLoaderManager().initLoader( MOVIE_LOADER_ID, null, this);
                getSupportLoaderManager().initLoader( MOVIE_REVIEWS_VIDEOS_LOADER, null, reviewsLoader);


                Log.i( MovieDetailsActivity.class.getName(), "MOVIE TITLE - " + mMovie.getTitle() );

                if (mMovie.getPosterPath() != null)
                {
                    posterPath = NetworkUtils.getPosterUrl(mMovie.getPosterPath(), Size.original);

                    GlideApp
                            .with( this )
                            .load( posterPath )
                            .into( mPoster );
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void setRecyclerViewTrailers()
    {
        mLayoutManagerTrailers =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        mRecyclerViewTrailers.setLayoutManager(mLayoutManagerTrailers);
        mRecyclerViewTrailers.setHasFixedSize(true);
        mRecyclerViewTrailers.setAdapter( mTrailerAdapter );
    }


    private void setTrailerAdapterListener( )
    {
        mTrailerAdapter.setOnEntryClickListener(new TrailerAdapter.OnEntryClickListener()
        {
            @Override
            public void onEntryClick(View view, int position)
            {
                String key = mTrailers.get( position ).getKey();

                openYoutube( key );
            }
        });
    }


    private void setRecyclerViewReviews()
    {
        mLayoutManagerReviews =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerViewReviews.setLayoutManager(mLayoutManagerReviews);
        mRecyclerViewReviews.setHasFixedSize(true);
        mRecyclerViewReviews.setAdapter( mReviewAdapter );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch ( item.getItemId() )
        {
            case android.R.id.home:
                getIntent().addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                finish();
                return true;

            case R.id.share_button:
                shareVideo("youtube");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private OnClickListener onClickListener = new OnClickListener()
    {
        @Override
        public void onClick(final View v)
        {
            Uri uri;

            switch(v.getId())
            {
                case R.id.fav_button_movie:

                    if( mIsFavourite )
                    {
                        uri = FavouriteContract.FavouriteMovieEntry.CONTENT_URI.buildUpon()
                                .appendPath(mMovie.getId()+"").build();

                        int movieDeleted = getContentResolver().delete(uri,null,null);

                        if(movieDeleted != -1)
                        {
                            Toast.makeText(getBaseContext(), "Movie removed from favourites", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        uri = getContentResolver().insert(FavouriteContract.FavouriteMovieEntry.CONTENT_URI,
                                mMovie.getContentValues( ));

                        if (uri != null)
                        {
                            Toast.makeText(getBaseContext(), "Movie added to favourites", Toast.LENGTH_SHORT).show();
                        }
                    }

                    switchIcon();
                    getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, MovieDetailsActivity.this);
                    break;
            }
        }
    };

    private void switchIcon()
    {
        if( mIsFavourite )  mFavouriteButton.setImageResource( R.drawable.ic_fav_selected );
        else                mFavouriteButton.setImageResource( R.drawable.ic_fav_not_selected );
    }


    private void openYoutube( String key )
    {
        Intent youtubeApp = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent youtubeWeb = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key));

        try {
            this.getApplicationContext().startActivity(youtubeApp);
        } catch (ActivityNotFoundException ex) {
            this.getApplicationContext().startActivity(youtubeWeb);
        }
    }

    private void shareVideo( String url )
    {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_VIEW);
        shareIntent.setData( Uri.parse( url ) );

        if( shareIntent.resolveActivity(getPackageManager()) != null )
        {
            startActivity( shareIntent );
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle args)
    {
        return new AsyncTaskLoader<Cursor>(this)
        {
            Cursor mMovieData = null;

            @Override
            protected void onStartLoading()
            {
                if(mMovieData != null)  deliverResult(mMovieData);

                else                    forceLoad();
            }

            @Override
            public Cursor loadInBackground()
            {
                String mSelection = FavouriteContract.FavouriteMovieEntry.COLUMN_MOVIE_ID + " = " + mMovie.getId();
                try
                {
                    Log.d(TAG, "Performing QUERY to get movie by movie ID");
                    return getContentResolver().query(FavouriteContract.FavouriteMovieEntry.CONTENT_URI,
                            null,
                            mSelection,
                            null,
                            FavouriteContract.FavouriteMovieEntry.COLUMN_MOVIE_ID);

                }
                catch(Exception e)
                {
                    Log.e(TAG, "Failed to asynchronously load data");
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(Cursor data)
            {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        mIsFavourite = false;

        if( data != null && data.moveToFirst() )
        {
            mIsFavourite      = true;
            String posterPath = NetworkUtils.getPosterUrl( mMovie.getPosterPath(), Size.original );

            GlideApp
                    .with( this )
                    .load( posterPath )
                    .into( mPoster );
        }

        switchIcon();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        //
    }


    private LoaderManager.LoaderCallbacks<ArrayList<String>> reviewsLoader = new LoaderManager.LoaderCallbacks<ArrayList<String>>()
    {
        @Override
        public Loader<ArrayList<String>> onCreateLoader(int id, final Bundle args)
        {
            return new AsyncTaskLoader<ArrayList<String>>( MovieDetailsActivity.this )
            {
                ArrayList<String> mMovieData = new ArrayList<String>();

                @Override
                protected void onStartLoading()
                {
                    if(mMovieData.size() > 0 )
                        deliverResult(mMovieData);
                    else
                        forceLoad();
                }

                @Override
                public ArrayList<String> loadInBackground()
                {
                    try
                    {
                        String movieReviews = mNetworkUtils.getResponseFromHttpUrl( mNetworkUtils.getMovieReviews( mMovie.getId() ) );
                        String movieVideos  = mNetworkUtils.getResponseFromHttpUrl( mNetworkUtils.getMovieTraillers( mMovie.getId() ) );

                        mMovieData.add( movieReviews );
                        mMovieData.add( movieVideos );

                        return mMovieData;
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                public void deliverResult(ArrayList<String> data)
                {
                    mMovieData = data;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<String>> loader, ArrayList<String> data)
        {

            String reviews   = data.get(0);
            String trailers  = data.get(1);

            try
            {
                JSONObject reviewsJson = new JSONObject(reviews);
                JSONArray reviewArray  = reviewsJson.getJSONArray("results");

                JSONObject trailersJson = new JSONObject(trailers);
                JSONArray trailerArray  = trailersJson.getJSONArray("results");


                for (int i = 0; i < reviewArray.length(); i++)
                {
                    JSONObject review = reviewArray.getJSONObject(i);

                    Log.d(TAG, review.toString());

                    mReviews.add( new Review( review ) );
                }
                mReviewAdapter.notifyDataSetChanged();


                for(int i=0; i < trailerArray.length(); i++)
                {
                    JSONObject trailer = trailerArray.getJSONObject(i);
                    Log.d(TAG, trailer.toString());
                    mTrailers.add( new Trailer(trailer) );
                }
                mTrailerAdapter.notifyDataSetChanged();

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }


        @Override
        public void onLoaderReset(Loader<ArrayList<String>> loader)
        {

        }
    };

}
