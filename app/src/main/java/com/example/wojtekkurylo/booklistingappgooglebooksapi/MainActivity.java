package com.example.wojtekkurylo.booklistingappgooglebooksapi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    /**
     * Constant value for the Book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;
    /**
     * TextView that is displayed when there is no internet
     */
    private TextView mNoInternet;
    /**
     * ProgressBar that is displayed until loadManager received result form loading List with earthquakes
     */
    private ProgressBar mLoadingSpinner;
    /**
     * SearchView is storing the user input in UI
     */
    private SearchView mSearchView;
    /**
     * Search Button
     */
    private Button mSearchButton;
    /**
     * user query
     */
    private String mUserInput;
    /**
     * URL to query the GOOGLE_BOOKS dataset for books information
     */
    private String GOOGLE_BOOKS_QUERY_URL = null;
    /**
     * Adapter for the list of Books
     */
    private BookAdapter mAdapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    /**
     * ConnectivityManager to check internet connection while clicking Search button
     */
    private ConnectivityManager cm;

    private boolean checkIfVisable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link booksListView} in the layout
        ListView booksListView = (ListView) findViewById(R.id.list_view);

        // Set TextView If no data received in onLoadFinish method
        mEmptyStateTextView = (TextView) findViewById(R.id.no_books);
        booksListView.setEmptyView(mEmptyStateTextView);

        // Create a new {@link BookAdapter} of books
        mAdapter = new BookAdapter(MainActivity.this, new ArrayList<Book>());

        // Set the adapter on the {@link booksListView}
        // so the list can be populated in the user interface
        booksListView.setAdapter(mAdapter);

        // Initialization of ConnectivityManager to check internet connection while clicking Search button
        cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        // ID to progress bar
        mLoadingSpinner = (ProgressBar) findViewById(R.id.loading_spinner);
        // Progress Bar gone until user hit Search button
        mLoadingSpinner.setVisibility(View.GONE);

        // Setting up Query Hint
        mSearchView = (SearchView) findViewById(R.id.search_view);
        String queryHint = getString(R.string.search_query);
        mSearchView.setQueryHint(queryHint);

        // Setting up Search BUTTON
        mSearchButton = (Button) findViewById(R.id.search_button);
        mSearchButton.setText(R.string.search_button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // checking the User input in UI and casting to String data type Object
                mUserInput = mSearchView.getQuery().toString().toLowerCase();
                updateUrlRequest(mUserInput);
                Log.v("Main Activity", "result:" + GOOGLE_BOOKS_QUERY_URL);
                mLoadingSpinner.setVisibility(View.VISIBLE);

                // Checking the internet connection
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                // If there is internet connection do the following
                if (isConnected) {

                    if (checkIfVisable) {
                        mNoInternet.setVisibility(View.GONE);
                        checkIfVisable = false;
                    }

                    // Get a reference to the LoaderManager, in order to interact with loaders.
                    LoaderManager loaderManager = getSupportLoaderManager();

                    // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                    // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                    // because this activity implements the LoaderCallbacks interface).
                    loaderManager.restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                    // If there is NO internet connection do the following
                }else {
                    // Do not need the progress loading bar - set GONE
                    //Display info - no internet
                    mNoInternet = (TextView) findViewById(R.id.no_internet);
                    mNoInternet.setText(R.string.no_internet);
                    if (!checkIfVisable){
                        mNoInternet.setVisibility(View.VISIBLE);
                        checkIfVisable = true;
                    }
                    mLoadingSpinner.setVisibility(View.GONE);
                    mAdapter.clear();
                }
            }
        });

        /**
         * Check If Loader has been created in previous Activity (before screen rotation)
         *
         */
        if (savedInstanceState != null) {
            // If it exists, init with null arguments (since they won't
            // be used) to reconnect the callbacks
            if (getSupportLoaderManager().getLoader(BOOK_LOADER_ID) != null) {
                getSupportLoaderManager().initLoader(BOOK_LOADER_ID, null, MainActivity.this);
            }
        }

    }

    public String updateUrlRequest(String userInput) {
        String google_books_url = "https://www.googleapis.com/books/v1/volumes?q=";

        mUserInput = userInput;

        GOOGLE_BOOKS_QUERY_URL = google_books_url + mUserInput + "&orderBy=newest&printType=books";
        return GOOGLE_BOOKS_QUERY_URL;
    }

    /**
     * This method create constructor in QuakeAsyncTaskLoader Class
     */
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {

        BookAsyncTaskLoader constructor = new BookAsyncTaskLoader(MainActivity.this, GOOGLE_BOOKS_QUERY_URL);
        return constructor;
    }

    public void onLoadFinished(Loader<List<Book>> loader, List<Book> bookNewList) {
        mLoadingSpinner.setVisibility(View.GONE);

//        String noEarthQu = new String(getString(R.string.no_books));
//        mEmptyStateTextView.setText(noEarthQu.toUpperCase());

        // Clear the adapter of previous earthquake list
        mAdapter.clear();

        //If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (!bookNewList.isEmpty()) {
            mAdapter.addAll(bookNewList);
        } else {
            String noEarthQu = new String(getString(R.string.no_books));
            mEmptyStateTextView.setText(noEarthQu.toUpperCase());
        }
    }

    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }

}
