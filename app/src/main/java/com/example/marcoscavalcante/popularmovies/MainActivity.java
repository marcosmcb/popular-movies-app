package com.example.marcoscavalcante.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcoscavalcante.popularmovies.utils.NetworkUtils;
import com.example.marcoscavalcante.popularmovies.utils.PropertyUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private NetworkUtils networkUtils;
    private TextView tv_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkUtils = new NetworkUtils( getApplicationContext() );

        tv_test = (TextView) findViewById(R.id.debug_tv);

    }



    public class TheMovieDBTask extends AsyncTask<URL, Void, String>
    {

        @Override
        protected String doInBackground(URL... urls)
        {
            URL queryUrl = urls[0];
            String theMovieDbResults =  null;

            try
            {
                theMovieDbResults = networkUtils.getResponseFromHttpUrl( queryUrl );
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return theMovieDbResults;
        }

        @Override
        protected void onPostExecute(String s)
        {
            if( s != null && !s.equals("") )
            {
                tv_test.setText( s );
            }
        }
    }

    private void makeTheMovieDBQuery( ) throws IOException
    {
        URL movieDbQueryUrl = networkUtils.getUrlPopularMovies();
        tv_test.setText( movieDbQueryUrl.toString() );
        String theMovieDBSearchResults = null;
        new TheMovieDBTask().execute( movieDbQueryUrl );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();
        Context context;
        String message;

        switch( menuItemThatWasSelected )
        {
            case R.id.action_sort_most_popular:
                context = MainActivity.this;
                message = "most popular clicked";
                //Toast.makeText( context, message, Toast.LENGTH_LONG ).show();
                try {
                    makeTheMovieDBQuery( );
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;


            case R.id.action_sort_top_rated:
                context = MainActivity.this;
                message = "top popular clicked";
                Toast.makeText( context, message, Toast.LENGTH_LONG ).show();
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
