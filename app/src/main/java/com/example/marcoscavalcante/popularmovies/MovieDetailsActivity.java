package com.example.marcoscavalcante.popularmovies;

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
import com.example.marcoscavalcante.popularmovies.utils.NetworkUtils;
import com.example.marcoscavalcante.popularmovies.utils.Size;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by marcoscavalcante on 30/12/2017.
 */

public class MovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    private static final int MOVIE_LOADER_ID = 10;


    /* Field to store our TextView */
    private ImageView mPoster;
    private TextView  mOverview;
    private TextView  mReleaseDate;
    private TextView  mVoteAverage;
    private FloatingActionButton mFavouriteButton;
    private Movie     mMovie;
    private boolean mIsFavourite;



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

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent movieDetails = getIntent();

        if ( movieDetails != null && movieDetails.hasExtra( "movie" ) )
        {
            try
            {
                JSONObject movieJsonObject = new JSONObject( movieDetails.getStringExtra("movie"));
                mMovie = new Movie( movieJsonObject );


                String releaseDate = mMovie.getReleaseDate().substring(0,4);
                String posterPath   =  NetworkUtils.getPosterUrl( mMovie.getPosterPath(), Size.w185 );

                setTitle(mMovie.getTitle());
                mOverview.setText( mMovie.getOverview() );
                mVoteAverage.setText( mMovie.getVoteAverage().toString() + '/' + getString(R.string.max_vote_average) );
                mReleaseDate.setText( releaseDate );
                mFavouriteButton.setOnClickListener( onClickListener );

                getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);

                GlideApp
                        .with( this )
                        .load( posterPath )
                        .centerCrop()
                        .into( mPoster );

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
                                mMovie.getContentValues());

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
                String[] columns  = new String[]{ FavouriteContract.FavouriteMovieEntry.COLUMN_MOVIE_ID };
                String mSelection = FavouriteContract.FavouriteMovieEntry.COLUMN_MOVIE_ID + " = " + mMovie.getId();
                try
                {
                    Log.d(TAG, "Performing QUERY to get movie by movie ID");
                    return getContentResolver().query(FavouriteContract.FavouriteMovieEntry.CONTENT_URI,
                            columns,
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
        mIsFavourite = (data.getCount() == 1) ? true : false;
        switchIcon();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        //
    }
}
