package com.example.marcoscavalcante.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

import com.example.marcoscavalcante.popularmovies.models.Review;
import com.example.marcoscavalcante.popularmovies.models.Trailer;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by marcoscavalcante on 19/03/2018.
 */

public class FavouriteContract
{

    public static final String AUTHORITY = "com.example.marcoscavalcante.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "movies";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_TRAILERS = "trailers";
    public static final String PATH_MOVIES_REVIEWS = "movies_reviews";
    public static final String PATH_MOVIES_TRAILERS = "movies_trailers";


    public static final class FavouriteMovieEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_VOTE_COUNT = "voteCount";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_HAS_VIDEO = "hasVideo";
        public static final String COLUMN_VOTE_AVERAGE =  "voteAverage";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_BACKDROP_PATH = "backgroundImage";
        public static final String COLUMN_POSTER_PATH = "posterImage";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
    }


    public static final class FavouriteReviewEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();

        public static final String TABLE_NAME = "review";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_REVIEW_ID = "reviewId";
        public static final String COLUMN_URL = "url";
    }


    public static final class FavouriteTrailerEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS).build();

        public static final String TABLE_NAME = "trailer";
        public static final String COLUMN_TRAILER_ID = "trailerId";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SITE = "site";
        public static final String COLUMN_SIZE = "size";
        public static final String COLUMN_TYPE = "type";
    }


    public static final class FavouriteMovieReviewEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES_REVIEWS).build();

        public static final String TABLE_NAME = "movie_review";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_REVIEW_ID = "reviewId";
    }


    public static final class FavouriteMovieTrailerEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES_TRAILERS).build();

        public static final String TABLE_NAME = "movie_trailer";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_TRAILER_ID = "trailerId";
    }
}
