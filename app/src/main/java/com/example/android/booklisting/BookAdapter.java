package com.example.android.booklisting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by joshua on 6/13/17.
 */

public class BookAdapter extends ArrayAdapter<Book>  {

    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Check if there is an existing view that is useable
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        //Find the Book at the given position in the list
        Book currentBook = getItem(position);

        //Find the TextView with ID Title
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        titleView.setText(currentBook.getTitle());

        //Find the TextView with ID Authors
        TextView authorView = (TextView) listItemView.findViewById(R.id.authors);
        authorView.setText(currentBook.getAuthor());

        //Find the TextView with ID Published Date
        TextView publishedDateView = (TextView) listItemView.findViewById(R.id.publishedDate);
        publishedDateView.setText(currentBook.getPublishedDate());

        return listItemView;


    }

}
