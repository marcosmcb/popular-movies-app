package com.example.marcoscavalcante.popularmovies;

import android.content.Intent;

import android.database.Cursor;
import android.nfc.Tag;
import android.os.PersistableBundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>
{
    private static final String TAG = MainActivity.class.getSimpleName();
    private NetworkUtils mNetworkUtils;
    private TextView mErrorMessage;
    private ProgressBar mLoadingIndicator;

    private ArrayList<Movie> mMovies;

    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private Menu mMenu;
    private URL mMovieDbQueryUrl;
    private int mGridScrollPosition;


    private static final int MOVIES_LOADER             = 1;
    private static final int FAVOURITES_LOADER         = 2;
    private static final int WIDTH_DIVIDER             = 600;

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
        setMovieAdapterListener();
        setGridLayoutManager();

        if( savedInstanceState != null )
        {
            mGridScrollPosition = savedInstanceState.getInt(getString(R.string.scroll_position));
            mMovies = savedInstanceState.getParcelableArrayList(getString(R.string.mMovies));
            if(mMovies != null) mMovieAdapter = new MovieAdapter(mMovies);


            mGridLayoutManager.onRestoreInstanceState( savedInstanceState.getParcelable("grid") );
        }
        



        setRecyclerView();

        if(savedInstanceState == null) {
            try {
                makeTheMovieDBQuery(getString(R.string.sort_most_popular));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mGridScrollPosition = mGridLayoutManager.findFirstCompletelyVisibleItemPosition();
        outState.putInt( getString(R.string.scroll_position), mGridScrollPosition );
        outState.putParcelableArrayList( getString(R.string.mMovies), mMovies );
        outState.putParcelable("grid", mGridLayoutManager.onSaveInstanceState());
    }


    private int getNumberOfColumns()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int nColumns = width / WIDTH_DIVIDER;

        if (nColumns < 2) return 2; //to keep the grid aspect

        return nColumns;
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

                String movieJson = mMovies.get(position).getMovieJson().toString();

                startMovieDetailsActivityIntent.putExtra(getString(R.string.movie_json), movieJson );

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



    private void callLoader(  )
    {
        Bundle queryBundle = new Bundle();
        queryBundle.putString( SEARCH_QUERY_URL_EXTRA, mMovieDbQueryUrl.toString() );

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
        mGridLayoutManager = new GridLayoutManager( this , getNumberOfColumns() );
        mGridLayoutManager.scrollToPosition(mGridScrollPosition);
    }


    private void makeTheMovieDBQuery( String param ) throws IOException
    {
        String popularMovies = getString( R.string.sort_most_popular );
        mMovies.clear();

        if( param.equals(popularMovies) )
        {
            mMovieDbQueryUrl = mNetworkUtils.getUrlPopularMovies();
        }
        else
        {
            mMovieDbQueryUrl = mNetworkUtils.getUrlTopRated();
        }

        callLoader( );
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

                getSupportLoaderManager().restartLoader(FAVOURITES_LOADER, null, favouriteLoader);

                break;
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
                JSONArray jsonArray  = jsonObject.getJSONArray( getString(R.string.results_json));

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
