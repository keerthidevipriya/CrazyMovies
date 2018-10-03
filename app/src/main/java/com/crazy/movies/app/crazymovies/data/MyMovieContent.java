package com.crazy.movies.app.crazymovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MyMovieContent {
    public static final String AUTHORITY = "com.crazy.movies.app.crazymovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_TASKS = "favouritesTable";

    public static final class FavMovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();
        public static final String TABLE_NAME = "favouritesTable";
        public static final String COLUMN_MOVIEID = "MovieId";
        public static final String COLUMN_BACKDROP = "Backdrop";
        public static final String COLUMN_RATING = "Rating";
        public static final String COLUMN_POSTER_PATH = "PosterPath";
        public static final String COLUMN_ORG_TITLE = "OriginalTitle";
        public static final String COLUMN_OVERVIEW = "Overview";
        public static final String COLUMN_RELEASEDATE = "ReleaseDate";
    }
}
