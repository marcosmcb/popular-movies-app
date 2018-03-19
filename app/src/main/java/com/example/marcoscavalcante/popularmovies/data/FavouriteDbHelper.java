package com.example.marcoscavalcante.popularmovies.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.marcoscavalcante.popularmovies.data.FavouriteContract.*;

/**
 * Created by marcoscavalcante on 19/03/2018.
 */

public class FavouriteDbHelper extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PopularMovies.db";


    private static final String SQL_CREATE_MOVIE =
            "CREATE TABLE " + FavouriteMovieEntry.TABLE_NAME + " (" +
            FavouriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            FavouriteMovieEntry.COLUMN_TITLE + " TEXT," +
            FavouriteMovieEntry.COLUMN_OVERVIEW + " TEXT," +
            FavouriteMovieEntry.COLUMN_HAS_VIDEO + " INTEGER," +
            FavouriteMovieEntry.COLUMN_MOVIE_ID + " TEXT," +
            FavouriteMovieEntry.COLUMN_VOTE_AVERAGE + " REAL," +
            FavouriteMovieEntry.COLUMN_VOTE_COUNT + " REAL," +
            FavouriteMovieEntry.COLUMN_BACKDROP_PATH + " BLOB," +
            FavouriteMovieEntry.COLUMN_POPULARITY + " REAL," +
            FavouriteMovieEntry.COLUMN_RELEASE_DATE + " DATE," +
            FavouriteMovieEntry.COLUMN_POSTER_PATH + " BLOB)";


    private static final String SQL_CREATE_REVIEW =
            "CREATE TABLE " + FavouriteReviewEntry.TABLE_NAME + " (" +
            FavouriteReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            FavouriteReviewEntry.COLUMN_AUTHOR + " TEXT," +
            FavouriteReviewEntry.COLUMN_CONTENT + " TEXT," +
            FavouriteReviewEntry.COLUMN_REVIEW_ID + " TEXT,"+
            FavouriteReviewEntry.COLUMN_URL + " TEXT)";


    private static final String SQL_CREATE_TRAILER =
            "CREATE TABLE " + FavouriteTrailerEntry.TABLE_NAME + " (" +
            FavouriteTrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            FavouriteTrailerEntry.COLUMN_TRAILER_ID + " TEXT," +
            FavouriteTrailerEntry.COLUMN_KEY + " TEXT," +
            FavouriteTrailerEntry.COLUMN_NAME + " TEXT," +
            FavouriteTrailerEntry.COLUMN_SITE + " TEXT," +
            FavouriteTrailerEntry.COLUMN_SIZE + " TEXT," +
            FavouriteTrailerEntry.COLUMN_TYPE + " TEXT)";

    private static final String SQL_CREATE_MOVIE_REVIEW =
            "CREATE TABLE " + FavouriteMovieReviewEntry.TABLE_NAME + " (" +
                    FavouriteMovieReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FavouriteMovieReviewEntry.COLUMN_MOVIE_ID + " INTEGER," +
                    FavouriteMovieReviewEntry.COLUMN_REVIEW_ID + " INTEGER)";


    private static final String SQL_CREATE_MOVIE_TRAILER =
            "CREATE TABLE " + FavouriteMovieTrailerEntry.TABLE_NAME + " (" +
                    FavouriteMovieTrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FavouriteMovieTrailerEntry.COLUMN_MOVIE_ID + " INTEGER," +
                    FavouriteMovieTrailerEntry.COLUMN_TRAILER_ID + " INTEGER)";


    private static final String SQL_DELETE_MOVIES =
            "DROP TABLE IF EXISTS " + FavouriteMovieEntry.TABLE_NAME;

    private static final String SQL_DELETE_REVIEWS =
            "DROP TABLE IF EXISTS " + FavouriteReviewEntry.TABLE_NAME;

    private static final String SQL_DELETE_TRAILERS =
            "DROP TABLE IF EXISTS " + FavouriteTrailerEntry.TABLE_NAME;

    private static final String SQL_DELETE_MOVIES_REVIEWS =
            "DROP TABLE IF EXISTS " + FavouriteMovieReviewEntry.TABLE_NAME;

    private static final String SQL_DELETE_MOVIES_TRAILERS =
            "DROP TABLE IF EXISTS " + FavouriteMovieTrailerEntry.TABLE_NAME;

    public FavouriteDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_MOVIE);
        db.execSQL(SQL_CREATE_REVIEW);
        db.execSQL(SQL_CREATE_TRAILER);
        db.execSQL(SQL_CREATE_MOVIE_REVIEW);
        db.execSQL(SQL_CREATE_MOVIE_TRAILER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(SQL_DELETE_MOVIES);
        db.execSQL(SQL_DELETE_REVIEWS);
        db.execSQL(SQL_DELETE_TRAILERS);
        db.execSQL(SQL_DELETE_MOVIES_REVIEWS);
        db.execSQL(SQL_DELETE_MOVIES_TRAILERS);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
