package com.example.marcoscavalcante.popularmovies;

import android.os.AsyncTask;
import android.view.View;

import com.example.marcoscavalcante.popularmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;


/**
 * Created by marcos.cavalcante on 05/01/2018.
 */

public class TheMovieDBTask extends AsyncTask<URL, Void, String>
{
    private static final String TAG = "TheMovieDBTask";
    private MainActivity activity;


    public TheMovieDBTask(MainActivity activity)
    {
        this.activity  = activity;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        activity.getmLoadingIndicator().setVisibility(View.VISIBLE);
        activity.showJsonDataView();
    }


    @Override
    protected String doInBackground(URL... urls)
    {
        URL queryUrl = urls[0];
        String theMovieDbResults =  null;

        try
        {
            theMovieDbResults = activity.getmNetworkUtils().getResponseFromHttpUrl( queryUrl );
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
        activity.getmLoadingIndicator().setVisibility(View.INVISIBLE);
        if( rawContent != null && !rawContent.equals("") )
        {
            activity.showJsonDataView();
            try
            {
                JSONObject jsonObject = new JSONObject(rawContent);
                JSONArray jsonArray  = jsonObject.getJSONArray("results");

                for( int i=0; i < jsonArray.length(); i++ )
                {
                    JSONObject movieJson = jsonArray.getJSONObject(i);
                    activity.getmMovies().add( new Movie( movieJson ) );
                }

                activity.getmMovieAdapter().notifyDataSetChanged();
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
            activity.showErrorMessage();
        }
    }

}
