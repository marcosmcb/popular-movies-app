package com.example.marcoscavalcante.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    /* Field to store our TextView */
    private ImageView mPoster;
    private TextView  mOverview;
    private TextView  mReleaseDate;
    private TextView  mVoteAverage;
    private Movie     mMovie;


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.movie_details, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_tv_title_details);

        mPoster         = findViewById(R.id.iv_poster_details);
        mOverview       = findViewById(R.id.tv_overview_details);
        mReleaseDate    = findViewById(R.id.tv_release_date_details);
        mVoteAverage    = findViewById(R.id.tv_vote_average_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        Intent movieDetails = getIntent();

        if ( movieDetails != null && movieDetails.hasExtra( "movie" ) )
        {
            try
            {
                JSONObject movieJsonObject = new JSONObject( movieDetails.getStringExtra("movie"));
                mMovie = new Movie( movieJsonObject );


                String releaseDate = mMovie.getReleaseDate().substring(0,4);
                String posterPath   =  NetworkUtils.getPosterUrl( mMovie.getPosterPath(), Size.w185 );

                setTitle(mMovie.getTitle());
                mOverview.setText( mMovie.getOverview() );
                mVoteAverage.setText( mMovie.getVoteAverage().toString() + '/' + getString(R.string.max_vote_average) );
                mReleaseDate.setText( releaseDate );

                GlideApp
                        .with( this )
                        .load( posterPath )
                        .centerCrop()
                        .into( mPoster );

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch ( item.getItemId() )
        {
            case android.R.id.home:
                getIntent().addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                finish();
                return true;

            case R.id.share_button:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_VIEW);
                shareIntent.setData( Uri.parse("https://www.youtube.com/watch?v=NZRlo1Caz7U") );

                startActivity( shareIntent );
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
