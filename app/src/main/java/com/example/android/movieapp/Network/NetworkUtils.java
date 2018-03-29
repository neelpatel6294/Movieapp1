package com.example.android.movieapp.Network;

import android.net.Uri;
import android.text.TextUtils;

import com.example.android.movieapp.Adapter.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by PATEL on 2/11/2018.
 */

public class NetworkUtils {

    final static String MOVIE_DB_URL = "https://api.themoviedb.org/3/discover/movie";


    final static String API_KEY = "api_key";

    // Paste your Api key below......
    //Example final static String api_key="123456b8ghg68ca54g58155b4bd37dff";
    final static String api_key = "398df9fa06ef9233262ebc5d74a35f65";


    final static String LANGUAGE = "language";
    final static String language = "en-US";
    final static String SORT_BY = "sort_by";
    final static String INCLUDE_ADULT = "include_adult";
    final static String include_adult = "false";
    final static String INCLUDE_VIDEO = "include_video";
    final static String include_video = "false";
    final static String PAGE = "page";
    final static String page = "1";


    //Fetching the json response

    public static List<Movie> fetchMovieData(URL url) {

        String jsonResponse = null;
        try {
            jsonResponse = getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Movie> movies = extractFeaturesFromJson(jsonResponse);
        return movies;

    }


    //Building URL used to query MOVIEDB

    public static URL buildURl(String sort) {
        Uri builtUri = Uri.parse(MOVIE_DB_URL).buildUpon()
                .appendQueryParameter(API_KEY, api_key)
                .appendQueryParameter(LANGUAGE, language)
                .appendQueryParameter(SORT_BY, sort)
                .appendQueryParameter(INCLUDE_ADULT, include_adult)
                .appendQueryParameter(INCLUDE_VIDEO, include_video)
                .appendQueryParameter(PAGE, page)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        return url;
    }

    //Method for getting response from Network

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    //Method for json parsing

    private static List<Movie> extractFeaturesFromJson(String movieJson) {

        if (TextUtils.isEmpty((movieJson))) {
            return null;
        }

        //creating empty array list to add the movies
        List<Movie> movie = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(movieJson);


            JSONArray movieArray = baseJsonResponse.getJSONArray("results");


            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject currentMovie = movieArray.getJSONObject(i);

                String img_path = currentMovie.getString("poster_path");

                String vote_average = currentMovie.getString("vote_average");

                String release_date = currentMovie.getString("release_date");

                String plot = currentMovie.getString("overview");

                String title = currentMovie.getString("title");


                Movie movie1 = new Movie(img_path, title, release_date, vote_average, plot);

                movie.add(movie1);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movie;
    }


}
