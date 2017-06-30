package com.example.wojtekkurylo.booklistingappgooglebooksapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


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

    static class ViewHolder {
        @BindView(R.id.book_author) TextView authorView;
        @BindView(R.id.book_title) TextView titleView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.signle_book, parent, false);

            holder = new ViewHolder(convertView); // to reference the child views for later actions
            // associate/ SAVE the holder with the view for later lookup
            convertView.setTag(holder);

        } else {
            // view already exists, get the holder instance from the view
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the {@link currentPlace} object located at this position in the list
        Book currentBook = getItem(position);

        holder.authorView.setText(currentBook.getAuthor());
        holder.titleView.setText(currentBook.getTitle());

        return convertView;
    }
}

// JUST FOR REFERENCE :
// Part of code without @BindView


//public class BookAdapter extends ArrayAdapter<Book> {
//
//    /**
//     * Custom constructor
//     *
//     * @param context       The current context.
//     * @param listToDisplay A List of  objects to display in a list.
//     */
//    public BookAdapter(Context context, List<Book> listToDisplay) {
//        super(context, 0, listToDisplay);
//    }
//static class ViewHolder {
//    private TextView authorView;
//    private TextView titleView;
//}
//
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        ViewHolder holder;
//
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.signle_book, parent, false);
//
//            holder = new ViewHolder(); // to reference the child views for later actions
//
//            // Find the TextView and set the Author
//            holder.authorView = (TextView) convertView.findViewById(R.id.book_author);
//            // Find the TextView and set the Title
//            holder.titleView = (TextView) convertView.findViewById(R.id.book_title);
//
//            // associate/ SAVE the holder with the view for later lookup
//            convertView.setTag(holder);
//
//        } else {
//            // view already exists, get the holder instance from the view
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        // Get the {@link currentPlace} object located at this position in the list
//        Book currentBook = getItem(position);
//
//        holder.authorView.setText(currentBook.getAuthor());
//        holder.titleView.setText(currentBook.getTitle());
//
//        return convertView;
//    }
