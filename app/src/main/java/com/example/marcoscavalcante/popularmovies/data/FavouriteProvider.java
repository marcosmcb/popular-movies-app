package com.example.marcoscavalcante.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by marcoscavalcante on 19/03/2018.
 */

public class FavouriteProvider extends ContentProvider
{
    private FavouriteDbHelper mFavouriteDbHelper;

    public static final int MOVIES = 100;
    public static final int MOVIES_WITH_ID = 101;

    public static final int REVIEWS = 200;
    public static final int REVIEWS_WITH_ID = 201;

    public static final int TRAILERS = 300;
    public static final int TRAILERS_WITH_ID = 301;

    public static final int MOVIES_REVIEWS = 400;
    public static final int MOVIES_REVIEWS_WITH_ID = 401;

    public static final int MOVIES_TRAILERS = 500;
    public static final int MOVIES_TRAILERS_WITH_ID = 501;


    public static final UriMatcher sUriMatcher = buildUriMatcher( );

    public static UriMatcher buildUriMatcher( )
    {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI( FavouriteContract.AUTHORITY, FavouriteContract.PATH_MOVIES, MOVIES );
        uriMatcher.addURI( FavouriteContract.AUTHORITY, FavouriteContract.PATH_MOVIES + "/#", MOVIES_WITH_ID );

        uriMatcher.addURI( FavouriteContract.AUTHORITY, FavouriteContract.PATH_REVIEWS, REVIEWS );
        uriMatcher.addURI( FavouriteContract.AUTHORITY, FavouriteContract.PATH_REVIEWS + "/#", REVIEWS_WITH_ID );

        uriMatcher.addURI( FavouriteContract.AUTHORITY, FavouriteContract.PATH_TRAILERS, TRAILERS );
        uriMatcher.addURI( FavouriteContract.AUTHORITY, FavouriteContract.PATH_TRAILERS + "/#", TRAILERS_WITH_ID );

        uriMatcher.addURI( FavouriteContract.AUTHORITY, FavouriteContract.PATH_MOVIES_REVIEWS, MOVIES_REVIEWS );
        uriMatcher.addURI( FavouriteContract.AUTHORITY, FavouriteContract.PATH_MOVIES_REVIEWS + "/#", MOVIES_REVIEWS_WITH_ID );

        uriMatcher.addURI( FavouriteContract.AUTHORITY, FavouriteContract.PATH_MOVIES_TRAILERS, MOVIES_TRAILERS );
        uriMatcher.addURI( FavouriteContract.AUTHORITY, FavouriteContract.PATH_MOVIES_TRAILERS + "/#", MOVIES_TRAILERS_WITH_ID );

        return uriMatcher;
    }

    @Override
    public boolean onCreate()
    {
        Context context = getContext();
        mFavouriteDbHelper = new FavouriteDbHelper(context);

        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder)
    {
        final SQLiteDatabase db = mFavouriteDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch(match)
        {
            case MOVIES:
                retCursor = db.query(FavouriteContract.FavouriteMovieEntry.TABLE_NAME,
                            projection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            sortOrder);
            break;

            case MOVIES_WITH_ID:

                String id = uri.getPathSegments().get(1);

                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};

                retCursor = db.query(FavouriteContract.FavouriteMovieEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);

            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri)
    {

        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values)
    {
        final SQLiteDatabase db = mFavouriteDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        long id;

        switch(match)
        {
            case MOVIES:
                id = db.insert(FavouriteContract.FavouriteMovieEntry.TABLE_NAME, null, values);
                returnUri = getResponse( id, FavouriteContract.FavouriteMovieEntry.CONTENT_URI );

                break;

            case REVIEWS:
                id = db.insert(FavouriteContract.FavouriteReviewEntry.TABLE_NAME, null, values);
                returnUri = getResponse( id, FavouriteContract.FavouriteReviewEntry.CONTENT_URI );

                break;

            case TRAILERS:
                id = db.insert(FavouriteContract.FavouriteTrailerEntry.TABLE_NAME, null, values);
                returnUri = getResponse( id, FavouriteContract.FavouriteTrailerEntry.CONTENT_URI );

                break;

            case MOVIES_REVIEWS:
                id = db.insert(FavouriteContract.FavouriteMovieReviewEntry.TABLE_NAME, null, values);
                returnUri = getResponse( id, FavouriteContract.FavouriteMovieReviewEntry.CONTENT_URI );

                break;

            case MOVIES_TRAILERS:
                id = db.insert(FavouriteContract.FavouriteMovieTrailerEntry.TABLE_NAME, null, values);
                returnUri = getResponse( id, FavouriteContract.FavouriteMovieTrailerEntry.CONTENT_URI );

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        final SQLiteDatabase db = mFavouriteDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int numberOfDeletedItems;

        switch (match)
        {
            case MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mWhereClause = FavouriteContract.FavouriteMovieEntry.COLUMN_MOVIE_ID + "=?";
                String[] mWhereArgs = new String[]{id};

                numberOfDeletedItems = db.delete(FavouriteContract.FavouriteMovieEntry.TABLE_NAME,
                        mWhereClause,
                        mWhereArgs);

                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        if(numberOfDeletedItems != 0)
        {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numberOfDeletedItems;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs)
    {
        final SQLiteDatabase db = mFavouriteDbHelper.getWritableDatabase();

        // Keep track of if an update occurs
        int tasksUpdated;

        // match code
        int match = sUriMatcher.match(uri);

        switch(match)
        {
            case MOVIES_WITH_ID:

                String id = uri.getPathSegments().get(1);
                String mWhereClause = "_id=?";
                String[] mWhereArgs = new String[]{id};
                tasksUpdated = db.update(FavouriteContract.FavouriteMovieEntry.TABLE_NAME,
                        values,
                        mWhereClause,
                        mWhereArgs
                        );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(tasksUpdated != 0)
        {
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return tasksUpdated;
    }

    private Uri getResponse( long id, Uri tableUri )
    {
        if(id > 0 )
        {
            return ContentUris.withAppendedId(tableUri, id);
        }
        else
        {
            throw new android.database.SQLException("Failed to insert row into " + tableUri);
        }
    }

}
