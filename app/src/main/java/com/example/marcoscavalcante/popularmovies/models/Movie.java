package com.example.marcoscavalcante.popularmovies.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    private Date releaseDate;
    private JSONObject movieJson;

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
        this.releaseDate        = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).parse(movie.getString("release_date"));
        this.movieJson          = movie;
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

    public boolean isHasVideo() {
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

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }


    public JSONObject getMovieJson() {
        return movieJson;
    }

    public void setMovieJson(JSONObject movieJson) {
        this.movieJson = movieJson;
    }

}
