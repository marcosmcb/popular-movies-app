package com.example.marcoscavalcante.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.marcoscavalcante.popularmovies.data.FavouriteContract;
import com.example.marcoscavalcante.popularmovies.data.FavouriteDbHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by marcoscavalcante on 19/03/2018.
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTest
{
    private final Context mContext = InstrumentationRegistry.getTargetContext();
    private final Class mDbHelperClass = FavouriteDbHelper.class;

    @Before
    public void setUp()
    {
        deleteDatabase();
    }

    @Test
    public void createDatabaseTest( ) throws Exception
    {
        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String databaseIsNotOpen = "The database should be open and isn't";
        assertEquals( databaseIsNotOpen, true, database.isOpen() );

        Cursor tableNameCursor = database.rawQuery("SELECT NAME FROM sqlite_master WHERE type ='table' and name IN ('" +
                        FavouriteContract.FavouriteMovieEntry.TABLE_NAME + "','" +
                        FavouriteContract.FavouriteReviewEntry.TABLE_NAME + "','" +
                        FavouriteContract.FavouriteTrailerEntry.TABLE_NAME + "','" +
                        FavouriteContract.FavouriteMovieTrailerEntry.TABLE_NAME + "','" +
                        FavouriteContract.FavouriteMovieReviewEntry.TABLE_NAME + "')", null);


        String errorInCreatingDatabase =
                "Error: This means that the database has not been created correctly";

        assertTrue(errorInCreatingDatabase, tableNameCursor.getCount() == 5 );

        tableNameCursor.close();
    }

    @Test
    public void insertSingleRecordTest() throws Exception
    {
        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor( Context.class ).newInstance(mContext);

        /* Use WaitlistDbHelper to get access to a writable database */
        SQLiteDatabase database = dbHelper.getWritableDatabase();


        ContentValues testValues = new ContentValues();
        testValues.put(FavouriteContract.FavouriteMovieEntry.COLUMN_TITLE, "title");
        testValues.put(FavouriteContract.FavouriteMovieEntry.COLUMN_OVERVIEW, "overview");
        testValues.put(FavouriteContract.FavouriteMovieEntry.COLUMN_HAS_VIDEO, 1);
        testValues.put(FavouriteContract.FavouriteMovieEntry.COLUMN_MOVIE_ID, "fhskudhfiusdh32y4587y34875t8347gfgeryfg");
        testValues.put(FavouriteContract.FavouriteMovieEntry.COLUMN_VOTE_AVERAGE, 84.43);
        testValues.put(FavouriteContract.FavouriteMovieEntry.COLUMN_POPULARITY, 4323.432);
        testValues.put(FavouriteContract.FavouriteMovieEntry.COLUMN_RELEASE_DATE, "24/12/1992");


        /* Insert ContentValues into database and get first row ID back */
        long firstRowId = database.insert(
                FavouriteContract.FavouriteMovieEntry.TABLE_NAME,
                null,
                testValues);

        /* If the insert fails, database.insert returns -1 */
        assertNotEquals("Unable to insert into the table MOVIES", -1, firstRowId);


        testValues = new ContentValues();
        testValues.put(FavouriteContract.FavouriteReviewEntry.COLUMN_AUTHOR, "author");
        testValues.put(FavouriteContract.FavouriteReviewEntry.COLUMN_CONTENT, "content");
        testValues.put(FavouriteContract.FavouriteReviewEntry.COLUMN_REVIEW_ID, "24234234gdfgfdbdf");
        testValues.put(FavouriteContract.FavouriteReviewEntry.COLUMN_URL, "http://androidopentutorials.com/android-sqlite-join-multiple-tables-example/");

        firstRowId = database.insert(
                FavouriteContract.FavouriteReviewEntry.TABLE_NAME,
                null,
                testValues);

        /* If the insert fails, database.insert returns -1 */
        assertNotEquals("Unable to insert into the table Review", -1, firstRowId);


        testValues = new ContentValues();
        testValues.put(FavouriteContract.FavouriteTrailerEntry.COLUMN_KEY, "key");
        testValues.put(FavouriteContract.FavouriteTrailerEntry.COLUMN_NAME, "name");
        testValues.put(FavouriteContract.FavouriteTrailerEntry.COLUMN_SITE, "youtube");
        testValues.put(FavouriteContract.FavouriteTrailerEntry.COLUMN_TYPE, "video");

        firstRowId = database.insert(
                FavouriteContract.FavouriteTrailerEntry.TABLE_NAME,
                null,
                testValues);

        /* If the insert fails, database.insert returns -1 */
        assertNotEquals("Unable to insert into the table Trailer", -1, firstRowId);


        testValues = new ContentValues();
        testValues.put(FavouriteContract.FavouriteMovieReviewEntry.COLUMN_MOVIE_ID, 1);
        testValues.put(FavouriteContract.FavouriteMovieReviewEntry.COLUMN_REVIEW_ID, 1);

        firstRowId = database.insert(
                FavouriteContract.FavouriteMovieReviewEntry.TABLE_NAME,
                null,
                testValues);

        /* If the insert fails, database.insert returns -1 */
        assertNotEquals("Unable to insert into the table MovieReview", -1, firstRowId);


        testValues = new ContentValues();
        testValues.put(FavouriteContract.FavouriteMovieTrailerEntry.COLUMN_MOVIE_ID, 1);
        testValues.put(FavouriteContract.FavouriteMovieTrailerEntry.COLUMN_TRAILER_ID, 1);

        firstRowId = database.insert(
                FavouriteContract.FavouriteMovieTrailerEntry.TABLE_NAME,
                null,
                testValues);

        /* If the insert fails, database.insert returns -1 */
        assertNotEquals("Unable to insert into the table MovieTrailer", -1, firstRowId);


        /*
         * Query the database and receive a Cursor. A Cursor is the primary way to interact with
         * a database in Android.
         */
        Cursor wCursor = database.query(
                /* Name of table on which to perform the query */
                FavouriteContract.FavouriteMovieEntry.TABLE_NAME,
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                null,
                /* Values for "where" clause */
                null,
                /* Columns to group by */
                null,
                /* Columns to filter by row groups */
                null,
                /* Sort order to return in Cursor */
                null);

        /* Cursor.moveToFirst will return false if there are no records returned from your query */
        String emptyQueryError = "Error: No Records returned from MOVIE query";
        assertTrue(emptyQueryError, wCursor.moveToFirst());

        wCursor.close();

        wCursor = database.query(
                FavouriteContract.FavouriteReviewEntry.TABLE_NAME, null,null,
                null,null, null, null);

        emptyQueryError = "Error: No Records returned from REVIEW query";
        assertTrue(emptyQueryError, wCursor.moveToFirst());

        wCursor.close();

        wCursor = database.query(
                FavouriteContract.FavouriteTrailerEntry.TABLE_NAME, null,null,
                null,null, null, null);

        emptyQueryError = "Error: No Records returned from TRAILER query";
        assertTrue(emptyQueryError, wCursor.moveToFirst());

        wCursor.close();

        wCursor = database.query(
                FavouriteContract.FavouriteMovieReviewEntry.TABLE_NAME, null,null,
                null,null, null, null);

        emptyQueryError = "Error: No Records returned from MOVIE REVIEW query";
        assertTrue(emptyQueryError, wCursor.moveToFirst());

        wCursor.close();

        wCursor = database.query(
                FavouriteContract.FavouriteMovieTrailerEntry.TABLE_NAME, null,null,
                null,null, null, null);

        emptyQueryError = "Error: No Records returned from MOVIE TRAILER query";
        assertTrue(emptyQueryError, wCursor.moveToFirst());

        /* Close cursor and database */
        wCursor.close();
        dbHelper.close();
    }

    /**
     * Deletes the entire database.
     */
    private void deleteDatabase()
    {
        try
        {
            /* Use reflection to get the database name from the db helper class */
            Field f = mDbHelperClass.getDeclaredField("DATABASE_NAME");
            f.setAccessible(true);
            mContext.deleteDatabase((String)f.get(null));
        }
        catch (NoSuchFieldException ex){
            fail("Make sure you have a member called DATABASE_NAME in the WaitlistDbHelper");
        }catch (Exception ex){
            fail(ex.getMessage());
        }

    }

}
