package com.example.android.booklisting;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshua on 6/13/17.
 */

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();

    //Creating the Private Constructor
    private QueryUtils() {

    }

    //Query the Google Books Database to return a search of a particular topic
    public static List<Book> fetchBookData(String requestUrl) {
        // Create a URL Object
        URL url = createUrl(requestUrl);

        //Perform the HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        //Extract the relevant fields from the JSON response and create a list of books
        List<Book> books = extractFeatureFromJson(jsonResponse);

        //Return the list of @link books
        return books;


    }

    //Return the URL object from the given string url
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }

    //Make an HTTP Request to the given URL and return a String as the response
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Send a 200 code if the response was successful
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    private static List<Book> extractFeatureFromJson(String bookJSON) {
        //Return early, if the JSON string is empty
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        //Create an empty array to add books
        List<Book> books = new ArrayList<>();

        //Attempt to parse the JSON
        try {
            JSONObject baseJsonResponse = new JSONObject(bookJSON);
            Log.e("FULL JSON OUTPUT", baseJsonResponse.toString());

            //Extract the JSON associated with the keys
            JSONArray bookArray = baseJsonResponse.getJSONArray("items");

            //For each book item in the array, create an link object
            for (int i = 0; i < bookArray.length(); i++) {
                //Get a single book a position i within the list of books
                JSONObject currentBook = bookArray.getJSONObject(i);

                //For the item, extract the JSONObject associated with the key given
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                Log.e(LOG_TAG, "JSON for volumeInfo has been read...");

                //Extract the title from the "title" key
                String title = volumeInfo.getString("title");
                String titleFormatted = "Book: " + title;

                //Extract the authors from the "author" key
                JSONArray authorList = volumeInfo.getJSONArray("authors");
                String author = authorList.getString(0);
                String authorFormatted = "Author: " + author;

                //Extract the date published from the key
                String publishedDate = volumeInfo.getString("publishedDate");
                String pdFormatted = "Published: " + publishedDate;

                //Get the Self Link for the book
                String bookURL = currentBook.getString("selfLink");

                //Extract the description from the key
                //String description = volumeInfo.getString("description");

                Book book = new Book(titleFormatted, authorFormatted, pdFormatted, bookURL);
                books.add(book);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the book JSON results.", e);
        }

        //return the list of books
        return books;
    }


}
