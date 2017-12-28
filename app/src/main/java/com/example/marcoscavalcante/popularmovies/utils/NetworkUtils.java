package com.example.marcoscavalcante.popularmovies.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;



/**
 * Created by marcoscavalcante on 07/11/2017.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String sAPI_BASE_URI        =  "https://api.themoviedb.org/3/movie/";
    private static final String sTOP_RATED_ENDPOINT  =  "top_rated";
    private static final String sPOPULAR_ENDPOINT    =  "popular";
    private static final String sAPI_KEY             =  "?api_key=";
    private static final String sLANGUAGE            =  "&language=en-US";
    private Context context;
    private PropertyUtils properties;

    private enum SIZE { w92, w154, w185, w342, w500, w780, original }


    public NetworkUtils( Context context )
    {
        setContext( context );
    }

    public void setContext( Context context)
    {
        this.context = context;
    }

    /**
     *
     * @return The URL to use to query the weather server.
     */
    public URL getUrlTopRated( ) throws MalformedURLException, IOException {

        PropertyUtils propertyUtils = new PropertyUtils( true, context );

        Uri builtUri = Uri.parse( sAPI_BASE_URI )
                .buildUpon()
                .appendPath( sTOP_RATED_ENDPOINT )
                .appendPath( sAPI_KEY + propertyUtils.getApiKey() )
                .build();

        URL url = null;

        Log.v(TAG, "Built URI TopRated = " + url);

        return url;
    }


    /**
     *
     *
     * @return The URL to use to query the weather server.
     */
    public URL getUrlPopularMovies( ) throws MalformedURLException, IOException {

        PropertyUtils propertyUtils = new PropertyUtils(true, context);

        Uri builtUri = Uri.parse( sAPI_BASE_URI )
                .buildUpon()
                .appendPath( sPOPULAR_ENDPOINT )
                .appendPath( sAPI_KEY + propertyUtils.getApiKey() )
                .build();

        URL url = null;

        Log.v(TAG, "Built URI PopularMovies = " + url);

        return url;
    }


    public String getKeyValue () throws IOException
    {
        PropertyUtils propertyUtils = new PropertyUtils(true, context);
        return propertyUtils.getApiKey() + " came from NetworkClass!!!";
    }


}
