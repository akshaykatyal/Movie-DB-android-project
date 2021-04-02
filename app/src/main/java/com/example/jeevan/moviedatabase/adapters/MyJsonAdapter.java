package com.example.jeevan.moviedatabase.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.jeevan.moviedatabase.DataClass.Movie;
import com.example.jeevan.moviedatabase.R;
import com.example.jeevan.moviedatabase.utility.MySingleton;

import java.util.List;

/**
 * Created by jeevan on 16/3/17.
 */

public class MyJsonAdapter extends ArrayAdapter<Movie> {

    static final String url = "https://image.tmdb.org/t/p/w185";


    public MyJsonAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Movie> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Movie currentMovie = getItem(position);


        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_items, parent, false);
        }
        NetworkImageView imageView = (NetworkImageView) listItemView.findViewById(R.id.imageview);
        TextView name = (TextView) listItemView.findViewById(R.id.name_of_movie);
        TextView date = (TextView) listItemView.findViewById(R.id.date_of_movie);
        TextView rating = (TextView) listItemView.findViewById(R.id.rating_text_view);

        imageView.setImageUrl(url + currentMovie.getImageUrl(), MySingleton.getInstance(getContext()).getImageLoader());

        name.setText(currentMovie.getName());
        date.setText(currentMovie.getReleaseDate());
        rating.setText(String.valueOf(currentMovie.getRating()));

        return listItemView;

    }
}
