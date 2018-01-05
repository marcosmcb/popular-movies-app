package com.example.marcoscavalcante.popularmovies;

import android.content.Intent;
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


public class MainActivity extends AppCompatActivity
{

    private NetworkUtils mNetworkUtils;
    private TextView mTestMessage;
    private TextView mErrorMessage;
    private ProgressBar mLoadingIndicator;

    private ArrayList<Movie> mMovies;

    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;

    private static final int NUM_COLUMNS_PORTRAIT  = 2;
    private static final int NUM_COLUMNS_LANDSCAPE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNetworkUtils     = new NetworkUtils( getApplicationContext() );
        mErrorMessage     = findViewById(R.id.error_message);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mRecyclerView = findViewById( R.id.rv_movies );

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            mGridLayoutManager = new GridLayoutManager( this , NUM_COLUMNS_PORTRAIT);
        }
        else
        {
            mGridLayoutManager = new GridLayoutManager( this , NUM_COLUMNS_LANDSCAPE);
        }

        mRecyclerView.setLayoutManager( mGridLayoutManager );
        mRecyclerView.setHasFixedSize(true);

        mMovies = new ArrayList<>();
        mMovieAdapter = new MovieAdapter( mMovies );

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

        mRecyclerView.setAdapter( mMovieAdapter );

        try
        {
            makeTheMovieDBQuery( getString( R.string.sort_most_popular ) );
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void showJsonDataView( )
    {
        mErrorMessage.setVisibility(View.INVISIBLE);
    }

    public void showErrorMessage( )
    {
        mErrorMessage.setVisibility(View.VISIBLE);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
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

            default:
                Toast.makeText( MainActivity.this,
                                 getString(R.string.error_message_menu),
                                 Toast.LENGTH_LONG ).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public NetworkUtils getmNetworkUtils() {
        return mNetworkUtils;
    }

    public void setmNetworkUtils(NetworkUtils mNetworkUtils) {
        this.mNetworkUtils = mNetworkUtils;
    }

    public TextView getmTestMessage() {
        return mTestMessage;
    }

    public void setmTestMessage(TextView mTestMessage) {
        this.mTestMessage = mTestMessage;
    }

    public TextView getmErrorMessage() {
        return mErrorMessage;
    }

    public void setmErrorMessage(TextView mErrorMessage) {
        this.mErrorMessage = mErrorMessage;
    }

    public ProgressBar getmLoadingIndicator() {
        return mLoadingIndicator;
    }

    public void setmLoadingIndicator(ProgressBar mLoadingIndicator) {
        this.mLoadingIndicator = mLoadingIndicator;
    }

    public ArrayList<Movie> getmMovies() {
        return mMovies;
    }

    public void setmMovies(ArrayList<Movie> mMovies) {
        this.mMovies = mMovies;
    }

    public MovieAdapter getmMovieAdapter() {
        return mMovieAdapter;
    }

    public void setmMovieAdapter(MovieAdapter mMovieAdapter) {
        this.mMovieAdapter = mMovieAdapter;
    }

    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }

    public void setmRecyclerView(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }

    public GridLayoutManager getmGridLayoutManager() {
        return mGridLayoutManager;
    }

    public void setmGridLayoutManager(GridLayoutManager mGridLayoutManager) {
        this.mGridLayoutManager = mGridLayoutManager;
    }
}
