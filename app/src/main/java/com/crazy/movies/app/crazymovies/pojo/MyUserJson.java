package com.crazy.movies.app.crazymovies.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class MyUserJson implements Parcelable {
    public static final Creator<MyUserJson> CREATOR = new Creator<MyUserJson>() {
        @Override
        public MyUserJson createFromParcel(Parcel in) {
            return new MyUserJson(in);
        }

        @Override
        public MyUserJson[] newArray(int size) {
            return new MyUserJson[size];
        }
    };
    int id;
    boolean video, adult;
    float popularity;
    double vote_average;
    long vote_count;
    String title, poster_path, original_language, original_title, backdrop_path, overview, release_date;

    protected MyUserJson(Parcel in) {
        id = in.readInt();
        video = in.readByte() != 0;
        adult = in.readByte() != 0;
        popularity = in.readFloat();
        vote_average = in.readLong();
        vote_count = in.readLong();
        title = in.readString();
        poster_path = in.readString();
        original_language = in.readString();
        original_title = in.readString();
        backdrop_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
    }

    public MyUserJson(int mId, String mBackdrop, double mRating, String mPosterpath, String mTitle, String mOverview, String mReleasedate) {

        this.id = mId;
        this.backdrop_path = mBackdrop;
        this.vote_average = mRating;
        this.poster_path = mPosterpath;
        this.original_title = mTitle;
        this.overview = mOverview;
        this.release_date = mReleasedate;
    }

    public MyUserJson(int id, long vote_count, boolean video, boolean adult, double vote_average, float popularity, String title, String poster_path, String original_language, String original_title, String backdrop_path, String overview, String release_date) {
        this.id = id;
        this.vote_count = vote_count;
        this.video = video;
        this.adult = adult;
        this.vote_average = vote_average;
        this.popularity = popularity;
        this.title = title;
        this.poster_path = poster_path;
        this.original_language = original_language;
        this.original_title = original_title;
        this.backdrop_path = backdrop_path;
        this.overview = overview;
        this.release_date = release_date;
    }

    public int getId() {
        return id;
    }

    public float getVote_count() {
        return vote_count;
    }

    public boolean isVideo() {
        return video;
    }

    public boolean isAdult() {
        return adult;
    }

    public double getVote_average() {
        return vote_average;
    }

    public float getPopularity() {
        return popularity;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeFloat(popularity);
        dest.writeDouble(vote_average);
        dest.writeLong(vote_count);
        dest.writeString(title);
        dest.writeString(poster_path);
        dest.writeString(original_language);
        dest.writeString(original_title);
        dest.writeString(backdrop_path);
        dest.writeString(overview);
        dest.writeString(release_date);
    }
}
