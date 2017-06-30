package com.example.wojtekkurylo.booklistingappgooglebooksapi;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

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
 * Created by wojtekkurylo on 30.06.2017.
 */

public class BookAsyncTaskLoader extends AsyncTaskLoader<List<Book>> {

    private String mUrl;

    public BookAsyncTaskLoader(Context context, String url) {
        super(context);

        mUrl = url;
    }

    /**
     * forceLoad(); is a required step to actually trigger the loadInBackground() method to execute.
     */
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This method runs on a background thread and performs the network request.
     * We should not update the UI from a background thread, so we return a list of
     * {@link Book}s as the result.
     */
    @Override
    public List<Book> loadInBackground() {

        ArrayList<String> urlArrayList = new ArrayList<String>();
        urlArrayList.add(mUrl);

        // Don't perform the request if there are no URLs, or the first URL is null.
        if (urlArrayList.size() < 1 || urlArrayList.get(0) == null) {
            return null;
        }

        // Create URL object
        URL url = createURL(urlArrayList.get(0));

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("BookAsyncTaskLoader", "Error with makeHttpRequest in loadInBackground", e);
        }

        List<Book> bookNewList = JSONparse.extractAndParseJsonResponse(jsonResponse);
        return bookNewList;
    }

    private URL createURL(String stringUrl) {
        URL url;
        try {

            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("BookAsyncTaskLoader", "Error in createURL", e);
            return null;
        }

        return url;
    }

    private String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";
        // If there is not url, return early
        if (url == null) {
            return jsonResponse;
        }

        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            // check the HTTP response status code
            // If the request was successful(code 200)
            // read the input stream and parse response
            int httpResponseCode = urlConnection.getResponseCode();
            if (httpResponseCode == 200) {

                InputStream inputStream = urlConnection.getInputStream();
                try {
                    jsonResponse = readFromStream(inputStream);
                } catch (IOException e) {
                    Log.e("BookAsyncTaskLoader", "Problem in makeHttpRequest method with readFromStream method", e);
                }

            } else {
                Log.e("BookAsyncTaskLoader", "httpResponseCode:" + httpResponseCode);
            }

        } catch (IOException e) {
            Log.e("BookAsyncTaskLoader", "Error in makeHttpRequest", e);
        }

        return jsonResponse;
    }

    private String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if (inputStream != null) {

            // metoda InputStreamReader poznala na tlumaczenie pojedynczych liter,
            // co wplywa negatywnie na dlugosc procesu
            // Charset.forName(„UTF-8") - instr how to decode kazdy byte na litery, wg wyznaczonego standardu - np. UTF-8
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            // BufferedReader znacznie przyspiesza proces tlumaczenie
            BufferedReader reader = new BufferedReader(inputStreamReader);
            try {
                String line = reader.readLine();
                while (line != null) {
                    output.append(line); // dodajemy do naszego StringBuilder nowe linie, zamiast tworzyć line += line …
                    line = reader.readLine();
                }
            } catch (IOException e) {
                Log.e("EarthquakeActivity", "Error in readFromStream method", e);
            }
        }
        return output.toString(); // zmieniamy StringBuilder, na String Object jsonResponse
    }


}
