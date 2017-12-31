package com.example.marcoscavalcante.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marcoscavalcante.popularmovies.models.Movie;
import com.example.marcoscavalcante.popularmovies.utils.NetworkUtils;
import com.example.marcoscavalcante.popularmovies.utils.Size;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by marcoscavalcante on 30/12/2017.
 */

public class MovieDetailsActivity extends AppCompatActivity
{

    /* Field to store our TextView */
    private TextView mTitleMovie;
    private ImageView mPoster;
    private TextView mOverview;
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);


        mTitleMovie = (TextView)  findViewById(R.id.tb_tv_title_details);
        mPoster     = (ImageView) findViewById(R.id.iv_poster_details);
        mOverview   = (TextView)  findViewById(R.id.tv_overview_details);

        Intent movieDetails = getIntent();

        if ( movieDetails.hasExtra( "movie" ) )
        {

            try
            {
                JSONObject movieJsonObject = new JSONObject( movieDetails.getStringExtra("movie"));
                mMovie = new Movie( movieJsonObject );

                mTitleMovie.setText( mMovie.getTitle() );
                mOverview.setText( mMovie.getOverview() );

                String posterPath   =  NetworkUtils.getPosterUrl( mMovie.getPosterPath(), Size.w342 );

                Picasso.with( this ).load( posterPath ).into( mPoster );

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }


        }
    }
}
