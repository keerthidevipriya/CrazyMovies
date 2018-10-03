package com.crazy.movies.app.crazymovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crazy.movies.app.crazymovies.adapter.MyVideoList;
import com.crazy.movies.app.crazymovies.data.MovieDbHelper;
import com.crazy.movies.app.crazymovies.data.MyMovieContent;
import com.crazy.movies.app.crazymovies.pojo.MyReviewUser;
import com.crazy.movies.app.crazymovies.pojo.MyYoutubeUserJson;
import com.crazy.movies.app.crazymovies.utils.HttpUrlResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SecondMovieDetails extends AppCompatActivity {

    ContentValues moviecontentValues;
    @BindView(R.id.second_tv_moviename)
    TextView orgTitle;
    @BindView(R.id.second_tv_rating)
    TextView rating;
    @BindView(R.id.second_overview)
    TextView overView;
    @BindView(R.id.second_releaseDate)
    TextView releaseDate;
    @BindView(R.id.second_backgraph)
    ImageView backgraphImage;
    @BindView(R.id.second_image_poster)
    ImageView posterImage;
    @BindView(R.id.second_fav_img)
    ImageView favImage;
    @BindView(R.id.second_rating_bar)
    RatingBar rbar;
    @BindView(R.id.heading_trailer_second)
    TextView trailerHeading;
    @BindView(R.id.second_trailer_recycler)
    RecyclerView rvYoutubeList;
    @BindView(R.id.tv_review_author)
    TextView review_author;
    @BindView(R.id.second_layout_scrollview)
    NestedScrollView nestedScrollView;
    String mvURL = "https://image.tmdb.org/t/p/w500/";
    String BASEURL = "https://api.themoviedb.org/3/movie/";
    ArrayList<String> mvDetails;
    String sid, reviewAuthor;
    boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_movie_details);
        ButterKnife.bind(this);
        mvDetails = getIntent().getStringArrayListExtra("jTitle");
        orgTitle.setText(mvDetails.get(0));
        Picasso.with(this).load(mvURL + "" + mvDetails.get(1)).placeholder(R.mipmap.ic_launcher_round).into(backgraphImage);
        Picasso.with(this).load(mvURL + mvDetails.get(2).toString()).placeholder(R.mipmap.ic_launcher_round).into(posterImage);
        sid = mvDetails.get(6);
        rating.setText(mvDetails.get(3));
        rbar.setRating(Float.parseFloat(mvDetails.get(3)));
        overView.setText(mvDetails.get(4));
        releaseDate.setText(mvDetails.get(5));
        Uri urisel = Uri.parse(MyMovieContent.FavMovieEntry.CONTENT_URI + "/*");
        Cursor selcur = getContentResolver().query(urisel, null, sid, null, null);
        if (selcur.getCount() > 0) {
            check = true;
            favImage.setImageResource(R.mipmap.coloredfavimg);
        } else {
            check = false;
            favImage.setImageResource(R.mipmap.favimage);
        }
        new MovieData().execute(getString(R.string.videos_url));
        new MovieReview().execute(getString(R.string.reviews_second_url));
    }

    public void addFavListImg(View view) {
        if (!check) {
            check = true;
            moviecontentValues = new ContentValues();
            moviecontentValues.put(MyMovieContent.FavMovieEntry.COLUMN_MOVIEID, mvDetails.get(6));
            moviecontentValues.put(MyMovieContent.FavMovieEntry.COLUMN_BACKDROP, mvDetails.get(1));
            moviecontentValues.put(MyMovieContent.FavMovieEntry.COLUMN_ORG_TITLE, mvDetails.get(0));
            moviecontentValues.put(MyMovieContent.FavMovieEntry.COLUMN_POSTER_PATH, mvDetails.get(2));
            moviecontentValues.put(MyMovieContent.FavMovieEntry.COLUMN_RATING, mvDetails.get(3));
            moviecontentValues.put(MyMovieContent.FavMovieEntry.COLUMN_RELEASEDATE, mvDetails.get(5));
            moviecontentValues.put(MyMovieContent.FavMovieEntry.COLUMN_OVERVIEW, mvDetails.get(4));
            Uri uri = getContentResolver().insert(Uri.parse(MyMovieContent.FavMovieEntry.CONTENT_URI + ""), moviecontentValues);
            if (uri != null) {
                favImage.setImageResource(R.mipmap.coloredfavimg);
                new MovieDbHelper(this).showFavoriteMovies();
                Toast.makeText(this, R.string.favadingsucess, Toast.LENGTH_SHORT).show();
            }
        } else {
            check = false;
            getContentResolver().delete(MyMovieContent.FavMovieEntry.CONTENT_URI, MyMovieContent.FavMovieEntry.COLUMN_MOVIEID + " =? ", new String[]{mvDetails.get(6)});
            favImage.setImageResource(R.mipmap.favimage);
            Toast.makeText(this, R.string.favdeletionsucess, Toast.LENGTH_SHORT).show();
        }
    }

    public class MovieData extends AsyncTask<String, Void, Void> {
        ArrayList<MyYoutubeUserJson> jsonYoutubeLinks = new ArrayList<MyYoutubeUserJson>();

        @Override
        protected Void doInBackground(String... strings) {
            HttpUrlResponse responseConnection = new HttpUrlResponse();
            URL url = responseConnection.buildYoutubeUrl(BASEURL, sid, strings[0]);
            String response = "";
            try {
                response = responseConnection.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.optJSONArray(getString(R.string.results_video_url));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject movieTrailer = jsonArray.optJSONObject(i);
                    String id = movieTrailer.optString(getString(R.string.id_video_url));
                    String videoKey = movieTrailer.optString(getString(R.string.key_video_url));
                    String videoUrl = "https://www.youtube.com/watch?v=" + "" + videoKey;
                    String name = movieTrailer.optString(getString(R.string.name_video_url));
                    MyYoutubeUserJson myYoutubeUserJson = new MyYoutubeUserJson(id, videoKey, name, videoUrl);
                    jsonYoutubeLinks.add(myYoutubeUserJson);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!jsonYoutubeLinks.isEmpty()) {
                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null) {
                    Snackbar.make(nestedScrollView, R.string.internet_error, Snackbar.LENGTH_SHORT).show();
                    trailerHeading.setText("");
                } else {
                    rvYoutubeList.setLayoutManager(new LinearLayoutManager(SecondMovieDetails.this));
                    rvYoutubeList.setAdapter(new MyVideoList(SecondMovieDetails.this, jsonYoutubeLinks));
                }
            } else {
                trailerHeading.setText(R.string.no_trailers_diaplay);
                rvYoutubeList.setVisibility(View.INVISIBLE);
                Snackbar.make(nestedScrollView, R.string.no_videos_errmsg, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    public class MovieReview extends AsyncTask<String, Void, Void> {
        ArrayList<MyReviewUser> jsonReviewLink = new ArrayList<MyReviewUser>();

        @Override
        protected Void doInBackground(String... strings) {
            HttpUrlResponse responseConnection = new HttpUrlResponse();
            URL reviewUrl = responseConnection.buildYoutubeUrl(BASEURL, sid, getString(R.string.reviews_url_response));
            String reviewResponse = "";
            try {
                reviewResponse = responseConnection.getResponseFromHttpUrl(reviewUrl);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            try {
                JSONObject jsonObject = new JSONObject(reviewResponse);
                JSONArray jsonArray = jsonObject.optJSONArray(getString(R.string.results_video_url));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject mvReview = jsonArray.optJSONObject(i);
                    String author = mvReview.optString(getString(R.string.author_review_url));
                    String content = mvReview.optString(getString(R.string.content_review_url));
                    String url = mvReview.optString(getString(R.string.url_review));
                    MyReviewUser myReviewUser = new MyReviewUser(author, content, null, url);
                    jsonReviewLink.add(myReviewUser);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            reviewAuthor = "";
            for (int i = 0; i < jsonReviewLink.size(); i++) {
                String s1 = getString(R.string.author_display) + jsonReviewLink.get(i).getAuthor() + "\n";
                String s2 = getString(R.string.content_display) + jsonReviewLink.get(i).getContent() + "\n\n";
                reviewAuthor += s1 + s2;
            }
            if (!jsonReviewLink.isEmpty()) {
                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo == null) {
                    Snackbar.make(nestedScrollView, R.string.internet_error, Snackbar.LENGTH_SHORT).show();
                } else {
                    review_author.setText(reviewAuthor);
                }
            }
        }
    }
}
