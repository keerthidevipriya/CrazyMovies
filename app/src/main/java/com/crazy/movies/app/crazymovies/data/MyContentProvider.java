package com.crazy.movies.app.crazymovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {

    public static final int FAVMOVIEDB = 55;
    public static final int FAVMOVIEDB_WITH_ID = 102;
    private static final UriMatcher favUriMatcher = buildUriMatcher();
    private MovieDbHelper movieDbHelper;

    public MyContentProvider() {
    }

    public static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MyMovieContent.AUTHORITY, MyMovieContent.PATH_TASKS, FAVMOVIEDB);
        uriMatcher.addURI(MyMovieContent.AUTHORITY, MyMovieContent.PATH_TASKS + "/*", FAVMOVIEDB_WITH_ID);
        return uriMatcher;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase sqDatabase = movieDbHelper.getWritableDatabase();
        int match = favUriMatcher.match(uri);
        int favMoviedeleted;
        if (selection == null) {
            selection = "1";
        }
        switch (match) {
            case FAVMOVIEDB:
                favMoviedeleted = sqDatabase.delete(MyMovieContent.FavMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }
        if (favMoviedeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return favMoviedeleted;
    }


    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        long id;
        int match = favUriMatcher.match(uri);
        Uri uriMatched = null;
        switch (match) {
            case FAVMOVIEDB:
                id = db.insert(MyMovieContent.FavMovieEntry.TABLE_NAME, null, values);
                if (id > 0) {
                    uriMatched = ContentUris.withAppendedId(MyMovieContent.FavMovieEntry.CONTENT_URI, id);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uriMatched;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        movieDbHelper = new MovieDbHelper(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = movieDbHelper.getReadableDatabase();
        int match = favUriMatcher.match(uri);
        Cursor cursor = null;
        switch (match) {
            case FAVMOVIEDB:
                cursor = db.query(MyMovieContent.FavMovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case FAVMOVIEDB_WITH_ID:
                cursor = db.query(MyMovieContent.FavMovieEntry.TABLE_NAME, projection, MyMovieContent.FavMovieEntry.COLUMN_MOVIEID + "=" + selection, null, null, null, null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("No Updation done");
    }
}
