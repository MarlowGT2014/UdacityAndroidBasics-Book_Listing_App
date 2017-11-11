package com.example.android.booklisting;

/**
 * Created by joshua on 6/13/17.
 */

public class Book {

    private String mTitle;
    private String mAuthor;
    private String mPublishedDate;
    private String mURL;
    //private String mDescription;

    //Make a constructor
    public Book(String title, String author, String publishedDate, String URL) {
        mTitle = title;
        mAuthor = author;
        mPublishedDate = publishedDate;
        mURL = URL;
    }

    //Return Title
    public String getTitle() {
        return mTitle;
    }

    //Return Authors
    public String getAuthor() {
        return mAuthor;
    }

    //Return Published Date
    public String getPublishedDate() {
        return mPublishedDate;
    }

    public String getURL() {
        return mURL;
    }
}
