package com.example.marcoscavalcante.popularmovies.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


/**
 * Created by marcoscavalcante on 07/11/2017.
 */

public class NetworkUtils
{

    private static final boolean IS_DEBUG            = true;

    private static final String TAG                  = NetworkUtils.class.getSimpleName();

    private static final String sAPI_BASE_URI        =  "https://api.themoviedb.org/3/movie/";
    private static final String sTOP_RATED_ENDPOINT  =  "top_rated";
    private static final String sPOPULAR_ENDPOINT    =  "popular";
    private static final String sQUERYPARAM          =  "?";
    private static final String sAPI_KEY             =  "api_key";
    private static final String sLANGUAGE            =  "language";
    private static final String sENGLISH             =  "en-US";

    private static final String sPOSTER_BASE_URL     = "http://image.tmdb.org/t/p/";


    private Context context;
    private PropertyUtils properties;

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
    public URL getUrlTopRated( ) throws IOException
    {

        PropertyUtils propertyUtils = new PropertyUtils( IS_DEBUG, context );

        Uri builtUri = Uri.parse( sAPI_BASE_URI + sTOP_RATED_ENDPOINT ).buildUpon()
                .appendQueryParameter( sAPI_KEY, propertyUtils.getApiKey() )
                .appendQueryParameter( sLANGUAGE, sENGLISH )
                .build();

        URL url = new URL(builtUri.toString());

        Log.v(TAG, "Built URI TopRated = " + url);

        return url;
    }


    /**
     *
     *
     * @return The URL to use to query the weather server.
     */
    public URL getUrlPopularMovies( ) throws IOException
    {

        PropertyUtils propertyUtils = new PropertyUtils( IS_DEBUG, context);

        Uri builtUri = Uri.parse( sAPI_BASE_URI + sPOPULAR_ENDPOINT ).buildUpon()
                .appendQueryParameter( sAPI_KEY, propertyUtils.getApiKey() )
                .appendQueryParameter( sLANGUAGE, sENGLISH )
                .build();

        URL url = new URL(builtUri.toString());

        Log.v(TAG, "Built URI PopularMovies = " + url);

        return url;
    }

    public static String getPosterUrl( String path, Size size )
    {
        Uri builtUri = Uri.parse( sPOSTER_BASE_URL + size.toString() + path ).buildUpon().build();
        return builtUri.toString();
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


}
