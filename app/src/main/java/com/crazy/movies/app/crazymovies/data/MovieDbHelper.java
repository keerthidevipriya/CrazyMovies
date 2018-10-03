package com.crazy.movies.app.crazymovies.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.crazy.movies.app.crazymovies.pojo.MyUserJson;

import java.util.ArrayList;


public class MovieDbHelper extends SQLiteOpenHelper {

    public static final int version = 3;

    public Context c;

    public MovieDbHelper(Context context) {
        super(context, MyMovieContent.FavMovieEntry.TABLE_NAME, null, version);
        c = context;
    }

    public Void showFavoriteMovies() {
        ArrayList<MyUserJson> moviedbinfo = new ArrayList<>();
        String queryselected = "SELECT * FROM " + MyMovieContent.FavMovieEntry.TABLE_NAME;
        SQLiteDatabase database = getReadableDatabase();
        Cursor moviedetails = database.rawQuery(queryselected, null);
        String[] data = new String[10];
        String p = "";
        if (moviedetails.moveToFirst()) {
            do {
                data[0] = String.valueOf(moviedetails.getInt(0));
                data[1] = moviedetails.getString(1);
                data[2] = moviedetails.getString(2);
                data[3] = moviedetails.getString(3);
                data[4] = moviedetails.getString(4);
                data[5] = moviedetails.getString(5);
                data[6] = moviedetails.getString(6);
                p = p + data[1] + "\n" + data[2] + "\n" + data[3] + "\n" + data[4] + "\n" + data[5] + "\n" + data[6] + "\n";
            } while (moviedetails.moveToNext());
        }
        database.close();
        return null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + MyMovieContent.FavMovieEntry.TABLE_NAME + "("
                + MyMovieContent.FavMovieEntry.COLUMN_MOVIEID + " INTEGER PRIMARY KEY" + ","
                + MyMovieContent.FavMovieEntry.COLUMN_BACKDROP + " TEXT" + ","
                + MyMovieContent.FavMovieEntry.COLUMN_RATING + " TEXT" + ","
                + MyMovieContent.FavMovieEntry.COLUMN_POSTER_PATH + " TEXT" + ","
                + MyMovieContent.FavMovieEntry.COLUMN_ORG_TITLE + " TEXT" + ","
                + MyMovieContent.FavMovieEntry.COLUMN_OVERVIEW + " TEXT" + ","
                + MyMovieContent.FavMovieEntry.COLUMN_RELEASEDATE + " TEXT" + ")";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MyMovieContent.FavMovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
