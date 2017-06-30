package com.example.wojtekkurylo.booklistingappgooglebooksapi;

/**
 * Created by wojtekkurylo on 29.06.2017.
 */

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to receive book data from Google Books Server and creating ArrayList.
 */
public class JSONparse {

    private static String mAuthor = "";

    /**
     * Create a private constructor because no one should ever create a {@link JSONparse} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name JSONparse (and an object instance of JSONparse is not needed).
     */
    private JSONparse() {
    }

    public static List<Book> extractAndParseJsonResponse(String jsonResponse) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding book to
        List<Book> booksArray = new ArrayList<Book>();

        try {

            // Create a JSONObject from the JSON response string
            JSONObject rootObject = new JSONObject(jsonResponse);

            JSONArray arrayItem = rootObject.getJSONArray("items");

            //For each Object searched in Google Books Server do following
            for (int i = 0; i < arrayItem.length(); i++) {

                // select i-th Object in Array
                JSONObject choosenItems = arrayItem.getJSONObject(i);

                // access the VolumeInfo Object with i-th book-details
                JSONObject volumeInfo = choosenItems.getJSONObject("volumeInfo");

                // Extract the value for the key called "title"
                String title = volumeInfo.getString("title");

                // access the Authors Object for i-th book
                JSONArray authorsInfo = volumeInfo.getJSONArray("authors");
                if(volumeInfo.has("authors")){
                    // access the method to build String from Array
                    updateAuthorsString(authorsInfo);
                } else{

                    int placeholder = R.string.no_author;
                    mAuthor = Integer.toString(placeholder);
                }


                // access the method to build String from Array
                updateAuthorsString(authorsInfo);

                // Create Book Object with title and author
                Book book = new Book(title, mAuthor);
                // Add created Objects to ArrayList<Book> @ i-th position
                booksArray.add(i, book);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception
            Log.e("JSONparse", "Problem parsing the book JSON results", e);
        }
        return booksArray;
    }

    /**
     * Helper method to update
     *
     * @param authorsInfo input parameter with i-th author
     * @return mAuthor String with all authors of book
     */
    private static String updateAuthorsString(JSONArray authorsInfo) {
        StringBuilder output = new StringBuilder();
        String NEW_LINE = System.getProperty("line.separator");

        for (int i = 0; i < authorsInfo.length(); i++) {
            try {
                if (authorsInfo.length() == 0) {
                    return mAuthor;

                } else if (authorsInfo.length() == 1) {
                    output.append(authorsInfo.getString(i));

                } else {
                    output.append(NEW_LINE + authorsInfo.getString(i));
                }
            } catch (JSONException e) {
                Log.e("JSONparse", "Error in updateAuthorsString", e);
            }
        }
        mAuthor = output.toString();
        return mAuthor;

    }


}
