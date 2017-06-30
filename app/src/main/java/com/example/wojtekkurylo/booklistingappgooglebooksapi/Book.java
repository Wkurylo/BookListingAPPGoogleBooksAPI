package com.example.wojtekkurylo.booklistingappgooglebooksapi;

/**
 * Created by wojtekkurylo on 29.06.2017.
 */

public class Book {

    /**
     * title of requested book
     */
    private String mTitle;

    /**
     * author of requested book
     */
    private String mAuthor;

    /**
     * Public Constructor to create single Book Object in CustomArrayList
     *
     * @param author author of requested book
     * @param title  title of requested book
     */
    public Book(String title, String author) {

        mTitle = title;
        mAuthor = author;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }


}
