package com.example.marcoscavalcante.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.marcoscavalcante.popularmovies.data.FavouriteContract;
import com.example.marcoscavalcante.popularmovies.data.FavouriteDbHelper;
import com.example.marcoscavalcante.popularmovies.models.Movie;
import com.example.marcoscavalcante.popularmovies.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>
{
    private NetworkUtils mNetworkUtils;
    private TextView mErrorMessage;
    private ProgressBar mLoadingIndicator;

    private ArrayList<Movie> mMovies;

    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private Menu mMenu;


    private static final int NUM_COLUMNS_PORTRAIT      = 2;
    private static final int NUM_COLUMNS_LANDSCAPE     = 4;
    private static final int MOVIES_LOADER             = 1;
    private static final String SEARCH_QUERY_URL_EXTRA = "query";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNetworkUtils     = new NetworkUtils( getApplicationContext() );
        mErrorMessage     = findViewById(R.id.error_message);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mMovies           = new ArrayList<>();
        mMovieAdapter     = new MovieAdapter( mMovies );
        mRecyclerView     = findViewById( R.id.rv_movies );

        reloadSavedInstance( savedInstanceState );
        setMovieAdapterListener();
        setGridLayoutManager();
        setRecyclerView();

        Class mDbHelperClass = FavouriteDbHelper.class;


        Field f = null;
        try {
            f = mDbHelperClass.getDeclaredField("DATABASE_NAME");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        f.setAccessible(true);
        try {
            this.deleteDatabase((String)f.get(null));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        getSupportLoaderManager().initLoader(MOVIES_LOADER, null, this);
    }


    private void reloadSavedInstance(Bundle savedInstanceState)
    {
        if(savedInstanceState != null)
        {
            String queryUrl = savedInstanceState.getString(SEARCH_QUERY_URL_EXTRA);
        }
    }

    private void setMovieAdapterListener( )
    {
        mMovieAdapter.setOnEntryClickListener(new MovieAdapter.OnEntryClickListener()
        {
            @Override
            public void onEntryClick(View view, int position)
            {
                Class movieDetails = MovieDetailsActivity.class;
                Intent startMovieDetailsActivityIntent = new Intent(MainActivity.this, movieDetails);

                String movieJson = null;
                try
                {
                    movieJson = mMovies.get(position).getMovieJson().toString();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                startMovieDetailsActivityIntent.putExtra("movie", movieJson );

                startActivity(startMovieDetailsActivityIntent);
            }
        });
    }

    private void setRecyclerView( )
    {
        mRecyclerView.setLayoutManager( mGridLayoutManager );
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter( mMovieAdapter );
    }



    private void callLoader( URL movieQuery )
    {
        Bundle queryBundle = new Bundle();
        queryBundle.putString( SEARCH_QUERY_URL_EXTRA, movieQuery.toString() );

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> movieLoader = loaderManager.getLoader(MOVIES_LOADER);

        if( movieLoader == null )
        {
            loaderManager.initLoader(MOVIES_LOADER, queryBundle, this );
        }
        else
        {
            loaderManager.restartLoader(MOVIES_LOADER, queryBundle, this );
        }
    }

    private void setGridLayoutManager()
    {
        if( this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT )
        {
            mGridLayoutManager = new GridLayoutManager( this , NUM_COLUMNS_PORTRAIT);
        }
        else
        {
            mGridLayoutManager = new GridLayoutManager( this , NUM_COLUMNS_LANDSCAPE);
        }
    }


    private void makeTheMovieDBQuery( String param ) throws IOException
    {
        URL movieDbQueryUrl;
        String popularMovies = getString( R.string.sort_most_popular );
        mMovies.clear();

        if( param.equals(popularMovies) )
        {
            movieDbQueryUrl = mNetworkUtils.getUrlPopularMovies();
        }
        else
        {
            movieDbQueryUrl = mNetworkUtils.getUrlTopRated();
        }

        callLoader( movieDbQueryUrl );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate( R.menu.main, menu );
        this.mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        mMenu.getItem(0).setIcon( getApplicationContext().getDrawable(R.drawable.ic_sort));

        int menuItemThatWasSelected = item.getItemId();

        switch( menuItemThatWasSelected )
        {
            case R.id.action_sort_most_popular:
                try
                {
                    makeTheMovieDBQuery( getString( R.string.sort_most_popular ) );
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                break;

            case R.id.action_sort_top_rated:
                try
                {
                    makeTheMovieDBQuery( getString( R.string.sort_top_rated ) );
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                break;

            case R.id.action_sort_favourites:

                getSupportLoaderManager().restartLoader(12, null, favouriteLoader);

                break;

            /*
            default:
                Toast.makeText( MainActivity.this,
                        getString(R.string.error_message_menu) + " [" + menuItemThatWasSelected + "]",
                                 Toast.LENGTH_LONG ).show();
                break;
            */
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args)
    {
        return new AsyncTaskLoader<String>(this)
        {
            // It will store the raw json, so we don't need to query again
            String mMovieJson;

            @Override
            protected void onStartLoading()
            {
                if(args == null)    return;

                mLoadingIndicator.setVisibility(View.VISIBLE);

                if(mMovieJson != null)  deliverResult(mMovieJson);
                else                    forceLoad();
            }

            @Override
            public String loadInBackground()
            {
                String searchStr = args.getString( SEARCH_QUERY_URL_EXTRA );

                if( searchStr == null || searchStr.isEmpty() )  return null;

                try
                {
                    URL movieUrl = new URL( searchStr );
                    String movieSearchResults = mNetworkUtils.getResponseFromHttpUrl( movieUrl );
                    return movieSearchResults;
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                    return null;
                }

            }
        };
    }


    @Override
    public void onLoadFinished(Loader<String> loader, String data)
    {
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        if( data != null && !data.isEmpty() )
        {
            showJsonDataView();
            try
            {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray  = jsonObject.getJSONArray("results");

                for( int i=0; i < jsonArray.length(); i++ )
                {
                    JSONObject movieJson = jsonArray.getJSONObject(i);
                    getmMovies().add( new Movie( movieJson ) );
                }

                getmMovieAdapter().notifyDataSetChanged();
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            showErrorMessage();
        }
    }

    public NetworkUtils getmNetworkUtils() { return mNetworkUtils; }

    public ProgressBar getmLoadingIndicator() { return mLoadingIndicator; }

    public ArrayList<Movie> getmMovies() { return mMovies; }

    public MovieAdapter getmMovieAdapter() { return mMovieAdapter; }

    public void showJsonDataView( ) { mErrorMessage.setVisibility(View.INVISIBLE); }

    public void showErrorMessage( ) { mErrorMessage.setVisibility(View.VISIBLE); }

    @Override
    public void onLoaderReset(Loader<String> loader) { }

    @Override
    public void onSaveInstanceState(Bundle outState) { super.onSaveInstanceState(outState); }

    @Override
    protected void onResume() {
        super.onResume();
        //getSupportLoaderManager().restartLoader()
    }


    private LoaderManager.LoaderCallbacks<Cursor> favouriteLoader = new LoaderManager.LoaderCallbacks<Cursor>()
    {
        @Override
        public Loader<Cursor> onCreateLoader(int id, final Bundle args)
        {
            return new AsyncTaskLoader<Cursor>(MainActivity.this)
            {
                Cursor mMovieData = null;

                @Override
                protected void onStartLoading()
                {
                    MainActivity.this.mLoadingIndicator.setVisibility(View.VISIBLE);
                    getmMovies().clear();

                    if(mMovieData != null)
                        deliverResult(mMovieData);
                    else
                        forceLoad();
                }

                @Override
                public Cursor loadInBackground()
                {
                    try
                    {
                        return getContentResolver().query(FavouriteContract.FavouriteMovieEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                null);
                    }
                    catch(Exception e)
                    {
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
            MainActivity.this.mLoadingIndicator.setVisibility(View.INVISIBLE);

            while(data.moveToNext())
            {
                Movie mMovie = new Movie(data);
                getmMovies().add(mMovie);
            }

            getmMovieAdapter().notifyDataSetChanged();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

}
