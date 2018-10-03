package com.crazy.movies.app.crazymovies;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.Toast;

import com.crazy.movies.app.crazymovies.adapter.MyMoviesList;
import com.crazy.movies.app.crazymovies.data.MyMovieContent;
import com.crazy.movies.app.crazymovies.pojo.MyUserJson;
import com.crazy.movies.app.crazymovies.utils.HttpUrlResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    ArrayList<MyUserJson> jsonMovieImages;
    String BASEURLMENU;
    String bundleKey = "MyBKey";
    String bundleValue = "popular";
    @BindView(R.id.grid_main_recycler)
    RecyclerView rvmMovieList;
    @BindView(R.id.main_grid_layout)
    GridLayout gridLayout;
    private Cursor cursorData;
    private int mIdCol, mBackdropCol, mRatingCol, mPosterpathCol, mTitleCol, mOverviewCol, mReleasedateCol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        BASEURLMENU = getString(R.string.base_url);
        MovieData movieData = new MovieData();
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage(R.string.internet_error_no_fav)
                    .setPositiveButton(R.string.accepted, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        } else {
            Snackbar.make(gridLayout, R.string.loading, Snackbar.LENGTH_SHORT).show();
            if (savedInstanceState != null) {
                if (savedInstanceState.getString(bundleKey) != getString(R.string.myfavouritescheck)) {
                    bundleValue = savedInstanceState.getString(bundleKey);
                    new MovieData().execute(bundleValue);
                } else if (savedInstanceState.getString(bundleKey) == getString(R.string.myfavouritescheck)) {
                    bundleValue = savedInstanceState.getString(bundleKey);
                    new CursorMovieData().execute();
                }
            } else {
                new MovieData().execute(bundleValue);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(bundleKey, bundleValue);

    }



    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemThatClicked = menuItem.getItemId();
        switch (itemThatClicked) {
            case R.id.popularmovies:
                bundleValue = getString(R.string.popular);
                MovieData movieData = new MovieData();
                movieData.execute(getString(R.string.popular));
                break;
            case R.id.menu_toprated:
                bundleValue = getString(R.string.top_rated);
                MovieData movieData1 = new MovieData();
                movieData1.execute(getString(R.string.top_rated));
                break;
            case R.id.menu_myfavourites:
                bundleValue = getString(R.string.myfavouritescheck);
                new CursorMovieData().execute();
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public class MovieData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            jsonMovieImages = new ArrayList<MyUserJson>();
            HttpUrlResponse responseConnection = new HttpUrlResponse();
            URL url = responseConnection.buildURL(BASEURLMENU, strings[0]);
            String response = null;
            try {
                response = responseConnection.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray(getString(R.string.results));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject movieDetail = jsonArray.getJSONObject(i);
                    int id = movieDetail.getInt(getString(R.string.id));
                    long vote_count = movieDetail.getLong(getString(R.string.vote_count));
                    boolean video = movieDetail.getBoolean(getString(R.string.video));
                    boolean adult = movieDetail.getBoolean(getString(R.string.adult));
                    double vote_average = movieDetail.getDouble(getString(R.string.vote_average));
                    long popularity = movieDetail.getLong(getString(R.string.popularity));
                    String title = movieDetail.getString(getString(R.string.title));
                    String poster_path = movieDetail.getString(getString(R.string.poster_path));
                    String original_language = movieDetail.getString(getString(R.string.original_language));
                    String original_title = movieDetail.getString(getString(R.string.original_title));
                    String backdrop_path = movieDetail.getString(getString(R.string.backdrop_path));
                    String overview = movieDetail.getString(getString(R.string.overview));
                    String release_date = movieDetail.getString(getString(R.string.release_date));
                    jsonMovieImages.add(new MyUserJson(id, vote_count, video, adult, vote_average, popularity, title, poster_path, original_language, original_title, backdrop_path, overview, release_date));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            rvmMovieList.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            rvmMovieList.setAdapter(new MyMoviesList(MainActivity.this, jsonMovieImages));
        }
    }

    public class CursorMovieData extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected Cursor doInBackground(Void... voids) {
            ContentResolver resolver = getContentResolver();
            Cursor cursor = resolver.query(MyMovieContent.FavMovieEntry.CONTENT_URI,
                    null, null, null, null);
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            ArrayList<MyUserJson> cursorMovieData = new ArrayList<MyUserJson>();
            super.onPostExecute(cursor);
            cursorData = cursor;
            mIdCol = cursorData.getColumnIndex(MyMovieContent.FavMovieEntry.COLUMN_MOVIEID);
            mBackdropCol = cursorData.getColumnIndex(MyMovieContent.FavMovieEntry.COLUMN_BACKDROP);
            mRatingCol = cursorData.getColumnIndex(MyMovieContent.FavMovieEntry.COLUMN_RATING);
            mPosterpathCol = cursorData.getColumnIndex(MyMovieContent.FavMovieEntry.COLUMN_POSTER_PATH);
            mTitleCol = cursorData.getColumnIndex(MyMovieContent.FavMovieEntry.COLUMN_ORG_TITLE);
            mOverviewCol = cursorData.getColumnIndex(MyMovieContent.FavMovieEntry.COLUMN_OVERVIEW);
            mReleasedateCol = cursorData.getColumnIndex(MyMovieContent.FavMovieEntry.COLUMN_RELEASEDATE);
            while (cursorData.moveToNext()) {
                String mId = cursorData.getString(mIdCol);
                String mBackdrop = cursorData.getString(mBackdropCol);
                String mRating = cursorData.getString(mRatingCol);
                String mPosterpath = cursorData.getString(mPosterpathCol);
                String mTitle = cursorData.getString(mTitleCol);
                String mOverview = cursorData.getString(mOverviewCol);
                String mReleasedate = cursorData.getString(mReleasedateCol);
                cursorMovieData.add(new MyUserJson(Integer.parseInt(mId), mBackdrop, Double.parseDouble(mRating), mPosterpath, mTitle, mOverview, mReleasedate));
            }
            cursorData.close();
            if (cursorMovieData.isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.app_name);
                builder.setMessage(R.string.no_favourites_display);
                builder.setPositiveButton(R.string.accepted, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.show();
            } else {
                rvmMovieList.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                rvmMovieList.setAdapter(new MyMoviesList(MainActivity.this, cursorMovieData));
                Toast.makeText(MainActivity.this, getString(R.string.no_of_favourites) + cursorData.getCount(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
