package com.example.marcoscavalcante.popularmovies;


import android.content.Intent;
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
import android.widget.Toast;

import com.example.marcoscavalcante.popularmovies.models.Movie;
import com.example.marcoscavalcante.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
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

    private static final int NUM_COLUMNS_PORTRAIT  = 2;
    private static final int NUM_COLUMNS_LANDSCAPE = 4;
    private static final int LOADER_MOVIES = 11;
    private Menu menu;


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
        setRecyclerView();


        getSupportLoaderManager().initLoader( LOADER_MOVIES, null, this);

        callLoader();
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

    private void callLoader( )
    {
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> movieLoader = loaderManager.getLoader(LOADER_MOVIES);

        if( movieLoader == null )
        {
            loaderManager.initLoader( LOADER_MOVIES, null, this );
        }
        else
        {
            loaderManager.restartLoader( LOADER_MOVIES, null, this );
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

        new TheMovieDBTask( this ).execute( movieDbQueryUrl );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate( R.menu.main, menu );
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        menu.getItem(0).setIcon( getApplicationContext().getDrawable(R.drawable.ic_sort));

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
            String mMovieJson;

            @Override
            protected void onStartLoading()
            {
                if(args == null)
                {
                    return;
                }

                mLoadingIndicator.setVisibility(View.VISIBLE);

                if(mMovieJson != null)
                {
                    deliverResult(mMovieJson);
                }
                else
                {
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground()
            {
                String searchQueryUrlString = args.getString( "" );

                if( searchQueryUrlString == null || searchQueryUrlString.isEmpty() )
                {
                    return null;
                }

                try
                {
                    URL movieUrl = new URL( searchQueryUrlString );
                    String movieSearchResults = NetworkUtils.getPosterUrl(null,null);
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

        if( data == null )  showErrorMessage();
        else                showJsonDataView();
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
