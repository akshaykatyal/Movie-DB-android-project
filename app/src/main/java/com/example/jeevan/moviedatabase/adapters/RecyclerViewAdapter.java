package com.example.jeevan.moviedatabase.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.example.jeevan.moviedatabase.DataClass.Movie;
import com.example.jeevan.moviedatabase.DataClass.SavedMovies;
import com.example.jeevan.moviedatabase.DetailsActivity;
import com.example.jeevan.moviedatabase.R;
import com.example.jeevan.moviedatabase.utility.ImageConvertor;
import com.example.jeevan.moviedatabase.utility.MySingleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeevan on 23/3/17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MovieViewHolder> {

    private final String url = "https://image.tmdb.org/t/p/w185";
    private LayoutInflater inflater;
    private ArrayList<Movie> list;
    private Context context;
    private Movie currentMovie;

    public RecyclerViewAdapter(Context context, ArrayList<Movie> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_items, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {

        currentMovie = list.get(position);

        if (currentMovie.getImageUrl().equals("null")) {
            Log.e("image null for ", currentMovie.getName());
            holder.imageView.setDefaultImageResId(R.drawable.leak_canary_icon);

        } else {

            holder.imageView.setImageUrl(url + currentMovie.getImageUrl(), MySingleton.getInstance(context).getImageLoader());
        }
        holder.name.setText(currentMovie.getName());
        holder.date.setText(currentMovie.getReleaseDate());
        holder.rating.setText(String.valueOf(currentMovie.getRating()));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {


        NetworkImageView imageView;
        TextView name;
        TextView date;
        TextView rating;
        ImageView optionsImageView;
        RelativeLayout layout;

        MovieViewHolder(View itemView) {
            super(itemView);
            imageView = (NetworkImageView) itemView.findViewById(R.id.imageview);
            name = (TextView) itemView.findViewById(R.id.name_of_movie);
            date = (TextView) itemView.findViewById(R.id.date_of_movie);
            rating = (TextView) itemView.findViewById(R.id.rating_text_view);
            optionsImageView = (ImageView) itemView.findViewById(R.id.options_image_view);
            layout = (RelativeLayout) itemView.findViewById(R.id.relative_layout_items);

            layout.setOnClickListener(this);
            optionsImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            currentMovie = list.get(getAdapterPosition());
            if (v.getId() == R.id.relative_layout_items) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("name", currentMovie.getName());
                intent.putExtra("desc", currentMovie.getDescription());
                intent.putExtra("rating", currentMovie.getRating());
                intent.putExtra("date", currentMovie.getReleaseDate());
                intent.putExtra("id", currentMovie.getMovieId());
                intent.putExtra("backdropImage", currentMovie.getBackdropImageUrl());
                context.startActivity(intent);
            } else if (v.getId() == R.id.options_image_view) {

                PopupMenu popup = new PopupMenu(context, optionsImageView);
                popup.getMenuInflater().inflate(R.menu.option_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(this);
                popup.show();
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.watch_later_item) {
                Movie moviePosition = list.get(getAdapterPosition());

                List<SavedMovies> myList = SavedMovies.find(SavedMovies.class,
                        "movie = ?",
                        String.valueOf(moviePosition.getMovieId()));

                if (myList.size() != 0) {
                    Toast.makeText(context, "Movie already present", Toast.LENGTH_SHORT).show();
                    return true;
                }
                SavedMovies savedMovies = new SavedMovies();
                savedMovies.setDate(moviePosition.getReleaseDate());
                savedMovies.setName(moviePosition.getName());
                savedMovies.setRating(moviePosition.getRating());
                savedMovies.setMovie(moviePosition.getMovieId());

                byte[] imageToByte = ImageConvertor.getBytes(moviePosition.getBitmap());
                savedMovies.setImage(imageToByte);
                savedMovies.save();
            }
            return true;
        }
    }
}
