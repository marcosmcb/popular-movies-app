package com.example.marcoscavalcante.popularmovies.models;

import android.content.ContentValues;
import android.database.Cursor;
import com.example.marcoscavalcante.popularmovies.data.FavouriteContract.FavouriteMovieEntry;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by marcoscavalcante on 30/12/2017.
 */

public class Movie
{
    private int voteCount;
    private int movieIdDb;
    private int id;
    private boolean hasVideo;
    private Double voteAverage;
    private String title;
    private Double popularity;
    private String posterPath;
    private String originalLanguage;
    private String originalTitle;
    private String backdropPath;
    private boolean isAdult;
    private String overview;
    private String releaseDate;
    private JSONObject movieJson;


    public Movie(Cursor movie)
    {
        this.movieIdDb          = movie.getInt( movie.getColumnIndex(FavouriteMovieEntry._ID) );
        this.voteCount          = movie.getInt( movie.getColumnIndex(FavouriteMovieEntry.COLUMN_VOTE_COUNT) );
        this.id                 = movie.getInt( movie.getColumnIndex(FavouriteMovieEntry.COLUMN_MOVIE_ID) );
        this.hasVideo           = movie.getInt( movie.getColumnIndex(FavouriteMovieEntry.COLUMN_HAS_VIDEO) ) > 0 ? true : false;
        this.voteAverage        = movie.getDouble( movie.getColumnIndex(FavouriteMovieEntry.COLUMN_VOTE_AVERAGE) );
        this.title              = movie.getString( movie.getColumnIndex(FavouriteMovieEntry.COLUMN_TITLE) );
        this.popularity         = movie.getDouble( movie.getColumnIndex(FavouriteMovieEntry.COLUMN_POPULARITY) );
        this.posterPath         = movie.getString( movie.getColumnIndex(FavouriteMovieEntry.COLUMN_POSTER_PATH) );
        this.backdropPath       = movie.getString( movie.getColumnIndex(FavouriteMovieEntry.COLUMN_BACKDROP_PATH) );
        this.overview           = movie.getString( movie.getColumnIndex(FavouriteMovieEntry.COLUMN_OVERVIEW) );
        this.releaseDate        = movie.getString( movie.getColumnIndex(FavouriteMovieEntry.COLUMN_RELEASE_DATE) );
    }


    public Movie(JSONObject movie) throws JSONException, ParseException
    {
        this.voteCount          = movie.getInt("vote_count");
        this.id                 = movie.getInt("id");
        this.hasVideo           = movie.getBoolean("video");
        this.voteAverage        = movie.getDouble("vote_average");
        this.title              = movie.getString("title");
        this.popularity         = movie.getDouble("popularity");

        this.posterPath         = movie.has("poster_path") ? movie.getString("poster_path") : null;
        this.originalLanguage   = movie.has("original_language") ? movie.getString("original_language") : null;
        this.originalTitle      = movie.has("original_title") ? movie.getString("original_title") : null;
        this.backdropPath       = movie.has("backdrop_path") ? movie.getString("backdrop_path") : null;
        this.isAdult            = movie.has("adult") ? movie.getBoolean("adult") : false;
        this.overview           = movie.getString("overview");
        this.releaseDate        = movie.getString("release_date");
        this.movieJson          = movie;
    }


    public ContentValues getContentValues( )
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(FavouriteMovieEntry.COLUMN_TITLE, this.getTitle());
        contentValues.put(FavouriteMovieEntry.COLUMN_OVERVIEW, this.getOverview());
        contentValues.put(FavouriteMovieEntry.COLUMN_HAS_VIDEO, this.hasVideo());

        contentValues.put(FavouriteMovieEntry.COLUMN_MOVIE_ID, this.getId());

        contentValues.put(FavouriteMovieEntry.COLUMN_VOTE_AVERAGE, this.getVoteAverage());
        contentValues.put(FavouriteMovieEntry.COLUMN_VOTE_COUNT, this.getVoteCount());
        contentValues.put(FavouriteMovieEntry.COLUMN_RELEASE_DATE, this.getReleaseDate());

        contentValues.put(FavouriteMovieEntry.COLUMN_POSTER_PATH, this.getPosterPath());
        contentValues.put(FavouriteMovieEntry.COLUMN_BACKDROP_PATH, this.getBackdropPath());

        return contentValues;
    }


    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean hasVideo() {
        return hasVideo;
    }

    public void setHasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


    public JSONObject getMovieJson() throws JSONException
    {
        if( this.movieJson == null )
        {
            this.movieJson = new JSONObject();
            this.movieJson.put( "vote_count", this.getVoteCount() );
            this.movieJson.put( "id", this.getId() );
            this.movieJson.put( "video", this.hasVideo() );
            this.movieJson.put( "vote_average", this.getVoteAverage() );
            this.movieJson.put( "title", this.getTitle() );
            this.movieJson.put( "popularity", this.getPopularity() );
            this.movieJson.put("overview", this.getOverview());
            this.movieJson.put("release_date", this.getReleaseDate());
            this.movieJson.put("poster_path", this.getPosterPath());
        }

        return movieJson;
    }

    public void setMovieJson(JSONObject movieJson) {
        this.movieJson = movieJson;
    }

    public boolean isHasVideo() {
        return hasVideo;
    }

}
