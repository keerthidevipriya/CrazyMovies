package com.crazy.movies.app.crazymovies.utils;

import android.net.Uri;
import android.util.Log;

import com.crazy.movies.app.crazymovies.BuildConfig;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUrlResponse {
    final static String IMAGE_URL = "https://image.tmdb.org/t/p/w300";
    private final static String API_KEY = BuildConfig.API_KEY;

    public static URL buildImageUrl(String path) {
        String urlImage = IMAGE_URL + "" + path;
        URL url = null;
        try {
            url = new URL(urlImage.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildYoutubeUrl(String impurl, String sid, String mainPurpose) {
        Uri buildUri = Uri.parse(impurl + sid).buildUpon().appendPath(mainPurpose).appendQueryParameter("api_key", API_KEY).build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildURL(String impurl, String mainPurpose) {
        Uri buildUri = Uri.parse(impurl).buildUpon().appendPath(mainPurpose).appendQueryParameter("api_key", API_KEY).build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        String responseHttp = "";
        try {
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            responseHttp = streamStringConversion(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseHttp;
    }

    private static String streamStringConversion(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }
}

