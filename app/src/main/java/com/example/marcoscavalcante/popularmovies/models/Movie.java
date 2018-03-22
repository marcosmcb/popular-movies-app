package com.example.marcoscavalcante.popularmovies.models;

import android.content.ContentValues;

import com.example.marcoscavalcante.popularmovies.data.FavouriteContract.FavouriteMovieEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by marcoscavalcante on 30/12/2017.
 */

public class Movie
{
    private int voteCount;
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
    private ArrayList<Review> reviews;
    private ArrayList<Trailer> trailers;


    public Movie(JSONObject movie) throws JSONException, ParseException
    {
        this.voteCount          = movie.getInt("vote_count");
        this.id                 = movie.getInt("id");
        this.hasVideo           = movie.getBoolean("video");
        this.voteAverage        = movie.getDouble("vote_average");
        this.title              = movie.getString("title");
        this.popularity         = movie.getDouble("popularity");
        this.posterPath         = movie.getString("poster_path");
        this.originalLanguage   = movie.getString("original_language");
        this.originalTitle      = movie.getString("original_title");
        this.backdropPath       = movie.getString("backdrop_path");
        this.isAdult            = movie.getBoolean("adult");
        this.overview           = movie.getString("overview");
        this.releaseDate        = movie.getString("release_date");
        this.movieJson          = movie;
    }

    public Movie(JSONObject movie, ArrayList<Trailer> trailers, ArrayList<Review> reviews )
            throws JSONException, ParseException
    {
        this.voteCount          = movie.getInt("vote_count");
        this.id                 = movie.getInt("id");
        this.hasVideo           = movie.getBoolean("video");
        this.voteAverage        = movie.getDouble("vote_average");
        this.title              = movie.getString("title");
        this.popularity         = movie.getDouble("popularity");
        this.posterPath         = movie.getString("poster_path");
        this.originalLanguage   = movie.getString("original_language");
        this.originalTitle      = movie.getString("original_title");
        this.backdropPath       = movie.getString("backdrop_path");
        this.isAdult            = movie.getBoolean("adult");
        this.overview           = movie.getString("overview");
        this.releaseDate        = movie.getString("release_date");
        this.trailers           = trailers;
        this.reviews            = reviews;

        this.movieJson          = movie;
    }

    /*
    *       FavouriteMovieEntry.COLUMN_TITLE + " TEXT," +
            FavouriteMovieEntry.COLUMN_OVERVIEW + " TEXT," +
            FavouriteMovieEntry.COLUMN_HAS_VIDEO + " INTEGER," +
            FavouriteMovieEntry.COLUMN_MOVIE_ID + " TEXT," +
            FavouriteMovieEntry.COLUMN_VOTE_AVERAGE + " REAL," +
            FavouriteMovieEntry.COLUMN_VOTE_COUNT + " REAL," +
            FavouriteMovieEntry.COLUMN_BACKDROP_PATH + " BLOB," +
            FavouriteMovieEntry.COLUMN_POPULARITY + " REAL," +
            FavouriteMovieEntry.COLUMN_RELEASE_DATE + " DATE," +
            FavouriteMovieEntry.COLUMN_POSTER_PATH + " BLOB)";
    * */

    public ContentValues getContentValues( )
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(FavouriteMovieEntry.COLUMN_TITLE, this.getTitle());
        contentValues.put(FavouriteMovieEntry.COLUMN_OVERVIEW, this.getOverview());
        contentValues.put(FavouriteMovieEntry.COLUMN_HAS_VIDEO, this.hasVideo());

        contentValues.put(FavouriteMovieEntry.COLUMN_MOVIE_ID, this.getId());

        contentValues.put(FavouriteMovieEntry.COLUMN_VOTE_AVERAGE, this.getVoteAverage());
        contentValues.put(FavouriteMovieEntry.COLUMN_VOTE_COUNT, this.getVoteCount());
        contentValues.put(FavouriteMovieEntry.COLUMN_VOTE_COUNT, this.getVoteCount());

        return contentValues;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public ArrayList<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
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


    public JSONObject getMovieJson() {
        return movieJson;
    }

    public void setMovieJson(JSONObject movieJson) {
        this.movieJson = movieJson;
    }

}
