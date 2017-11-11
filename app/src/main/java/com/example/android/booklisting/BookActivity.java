package com.example.android.booklisting;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joshua on 6/15/17.
 */

public class BookActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    private static final String LOG_TAG = BookActivity.class.getName();
    private TextView mNoBooks;
    //private TextView emptyTextView;

    //Create generic url
    String GBOOK_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    private static final int BOOK_LOADER_ID = 1;

    //Adapter for the list of Books
    private BookAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this was changed from book_activity
        setContentView(R.layout.activity_main);
        // Find a reference to the {@link ListView} in the layout
        ListView bookListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);

        //Receive User Text from Search Button and append to search URL
        Bundle bundle = getIntent().getExtras();
        String userText = bundle.getString("userText");

        Log.e("Received Results", userText);
        GBOOK_REQUEST_URL = GBOOK_REQUEST_URL + userText;
        Log.e("Address", GBOOK_REQUEST_URL);

        EditText userSearch = (EditText) findViewById(R.id.userSearch);
        userSearch.setHint("Results for: " + userText);

        mNoBooks = (TextView) findViewById(R.id.emptyView);
        bookListView.setEmptyView(mNoBooks);


        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Book currentBook = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri bookUri = Uri.parse(currentBook.getURL());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
            Log.e(LOG_TAG, "Internet is connected.");

        }
        else {

            View loadIndicator = findViewById(R.id.loadingIndicator);
            loadIndicator.setVisibility(View.GONE);

            TextView emptyTextView = (TextView) findViewById(R.id.internetNote);
            emptyTextView.setText(R.string.internetNote);
            Log.e(LOG_TAG, "Internet is NOT connected.");

        }
    }

    public void submitSearch(View view) {
        EditText userSearch = (EditText) findViewById(R.id.userSearch);
        String userText = userSearch.getText().toString();
        Log.e("userText", userText);

        Intent intent = new Intent(BookActivity.this, BookActivity.class);
        intent.putExtra("userText", userText);
        startActivity(intent);

    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        //Create a new loader for the given URL
        Log.e("Current Address", GBOOK_REQUEST_URL);
        return new BookLoader(this, GBOOK_REQUEST_URL);

    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        View loadingIndicator = findViewById(R.id.loadingIndicator);
        loadingIndicator.setVisibility(View.GONE);

        mNoBooks.setText("No Books Available\nPlease try search again.");

        //Clear the adapter of previous books
        mAdapter.clear();

        //If there is a valid list of books, add them to the adapter's data set
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        //Loader reset, so we can clear out our existing data
        mAdapter.clear();
    }

    public void resubmitSearch() {
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        Log.e(LOG_TAG, "Resubmit Search was started.");
    }

}
