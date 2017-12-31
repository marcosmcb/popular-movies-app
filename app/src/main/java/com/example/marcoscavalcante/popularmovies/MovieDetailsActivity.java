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
import org.w3c.dom.Text;

import java.text.ParseException;

/**
 * Created by marcoscavalcante on 30/12/2017.
 */

public class MovieDetailsActivity extends AppCompatActivity
{

    /* Field to store our TextView */
    private ImageView mPoster;
    private TextView mTitleMovie;
    private TextView mOverview;
    private TextView mReleaseDate;
    private TextView mVoteAverage;
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        mPoster         = (ImageView) findViewById(R.id.iv_poster_details);

        mTitleMovie     = (TextView) findViewById(R.id.tb_tv_title_details);
        mOverview       = (TextView) findViewById(R.id.tv_overview_details);
        mReleaseDate    = (TextView) findViewById(R.id.tv_release_date_details);
        mVoteAverage    = (TextView) findViewById(R.id.tv_vote_average_details);

        Intent movieDetails = getIntent();

        if ( movieDetails.hasExtra( "movie" ) )
        {

            try
            {
                JSONObject movieJsonObject = new JSONObject( movieDetails.getStringExtra("movie"));
                mMovie = new Movie( movieJsonObject );

                String releaseDate = mMovie.getReleaseDate().substring(0,4);
                String posterPath   =  NetworkUtils.getPosterUrl( mMovie.getPosterPath(), Size.w342 );

                mTitleMovie.setText( mMovie.getTitle() );
                mOverview.setText( mMovie.getOverview() );
                mVoteAverage.setText( mMovie.getVoteAverage().toString() + '/' + getString(R.string.max_vote_average) );
                mReleaseDate.setText( releaseDate );



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
