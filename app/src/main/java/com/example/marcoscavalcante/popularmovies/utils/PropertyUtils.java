package com.example.marcoscavalcante.popularmovies.utils;


import android.content.Context;
import android.content.res.AssetManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by marcoscavalcante on 14/11/2017.
 */

public class PropertyUtils
{
    InputStream input;
    private final String DEVEL_FILENAME = "devel.properties";
    private final String PROD_FILENAME  = "prod.properties";

    private String apiKey;


    public PropertyUtils( boolean isDebug, Context context ) throws IOException
    {
        if ( isDebug )
        {
            getProperties( DEVEL_FILENAME, context );
        }
        else
        {
            getProperties( PROD_FILENAME, context );
        }
    }

    public void getProperties( String filename, Context context ) throws IOException
    {

        try
        {
            Properties prop           = new Properties();
            AssetManager assetManager = context.getAssets();
            input                     = assetManager.open( filename );

            if (input != null)
            {
                prop.load(input);
            }
            else
            {
                throw new FileNotFoundException("property file '" + filename + "' not found in the classpath");
            }

            prop.load( input );

            apiKey = prop.getProperty("API_KEY");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            input.close();
        }

    }


    public String getApiKey()
    {
        return apiKey;
    }



}
