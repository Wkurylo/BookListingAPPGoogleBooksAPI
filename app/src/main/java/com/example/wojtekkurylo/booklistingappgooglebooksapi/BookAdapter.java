package com.example.wojtekkurylo.booklistingappgooglebooksapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


/**
 * Created by wojtekkurylo on 29.06.2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    /**
     * Custom constructor
     *
     * @param context       The current context.
     * @param listToDisplay A List of  objects to display in a list.
     */
    public BookAdapter(Context context, List<Book> listToDisplay) {
        super(context, 0, listToDisplay);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.signle_book, parent, false);
        }

        // Get the {@link currentPlace} object located at this position in the list
        Book currentBook = getItem(position);

        // Find the TextView and set the Author
        TextView authorView = (TextView) convertView.findViewById(R.id.book_author);
        authorView.setText(currentBook.getAuthor());

        // Find the TextView and set the Author
        TextView titleView = (TextView) convertView.findViewById(R.id.book_title);
        titleView.setText(currentBook.getTitle());


        return convertView;
    }
}
