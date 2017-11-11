package com.example.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by joshua on 6/15/17.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private static final String LOG_TAG = BookLoader.class.getName();
    private String mURL;

    public BookLoader(Context context, String url) {
        super(context);
        mURL = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if (mURL == null) {
            return null;
        }

        //Perform the network request
        List<Book> books = QueryUtils.fetchBookData(mURL);
        return books;
    }

}
