package com.example.marcoscavalcante.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private NetworkUtils mNetworkUtils;
    private TextView mTestMessage;
    private TextView mErrorMessage;
    private ProgressBar mLoadingIndicator;

    private ArrayList<Movie> mMovies;

    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;

    private static final int NUM_COLUMNS = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNetworkUtils     = new NetworkUtils( getApplicationContext() );
        mErrorMessage     = (TextView) findViewById(R.id.error_message);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mRecyclerView = (RecyclerView) findViewById( R.id.rv_movies );

        mGridLayoutManager = new GridLayoutManager( this , NUM_COLUMNS);


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

    private void showJsonDataView( )
    {
        mErrorMessage.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage( )
    {
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    public class TheMovieDBTask extends AsyncTask<URL, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
            showJsonDataView();
        }

        @Override
        protected String doInBackground(URL... urls)
        {
            URL queryUrl = urls[0];
            String theMovieDbResults =  null;

            try
            {
                theMovieDbResults = mNetworkUtils.getResponseFromHttpUrl( queryUrl );
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return theMovieDbResults;
        }

        @Override
        protected void onPostExecute(String rawContent)
        {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if( rawContent != null && !rawContent.equals("") )
            {
                showJsonDataView();
                try
                {
                    JSONObject jsonObject = new JSONObject(rawContent);
                    JSONArray  jsonArray  = jsonObject.getJSONArray("results");

                    for( int i=0; i < jsonArray.length(); i++ )
                    {
                        JSONObject movieJson = jsonArray.getJSONObject(i);
                        mMovies.add( new Movie( movieJson ) );
                    }

                    mMovieAdapter.notifyDataSetChanged();
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

        new TheMovieDBTask().execute( movieDbQueryUrl );
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
        Context context;
        String message;

        switch( menuItemThatWasSelected )
        {
            case R.id.action_sort_most_popular:
                context = MainActivity.this;
                // message = "most popular clicked";
                // Toast.makeText( context, message, Toast.LENGTH_LONG ).show();
                try
                {
                    makeTheMovieDBQuery( getString( R.string.sort_most_popular ) );
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                break;


            case R.id.action_sort_top_rated:
                context = MainActivity.this;
//                message = "top popular clicked";
//                Toast.makeText( context, message, Toast.LENGTH_LONG ).show();
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
                context = MainActivity.this;
                message = "couldnt find selection!";
                Toast.makeText( context, message, Toast.LENGTH_LONG ).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
