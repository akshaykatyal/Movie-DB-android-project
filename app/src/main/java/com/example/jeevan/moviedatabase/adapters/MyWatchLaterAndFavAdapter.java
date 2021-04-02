package com.example.jeevan.moviedatabase.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jeevan.moviedatabase.DataClass.SavedMovies;
import com.example.jeevan.moviedatabase.R;
import com.example.jeevan.moviedatabase.utility.ImageConvertor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeevan on 26/3/17.
 */

public class MyWatchLaterAndFavAdapter extends RecyclerView.Adapter<MyWatchLaterAndFavAdapter.SavedDataViewHolder> {

    private ArrayList<SavedMovies> list;
    private LayoutInflater inflater;
    private Context context;

    public MyWatchLaterAndFavAdapter(Context context, ArrayList<SavedMovies> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public SavedDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.layout_saved, parent, false);
        return new SavedDataViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SavedDataViewHolder holder, int position) {

        holder.name.setText(list.get(position).getName());
        holder.date.setText(list.get(position).getDate());
        holder.rating.setText(String.valueOf(list.get(position).getRating()));
        Bitmap bitmap = ImageConvertor.getImage(list.get(position).getImage());
        holder.imageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SavedDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        ImageView imageView;
        TextView name, rating, date;
        ImageView optionsImageView;

        SavedDataViewHolder(View itemView) {
            super(itemView);
            optionsImageView = (ImageView) itemView.findViewById(R.id.options_image_view_saved);
            imageView = (ImageView) itemView.findViewById(R.id.imageview_saved);
            name = (TextView) itemView.findViewById(R.id.name_of_movie_saved);
            rating = (TextView) itemView.findViewById(R.id.rating_text_view_saved);
            date = (TextView) itemView.findViewById(R.id.date_of_movie_saved);
            optionsImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            PopupMenu popup = new PopupMenu(context, optionsImageView);
            popup.getMenuInflater().inflate(R.menu.remove_saved_movies, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();


        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.remove_saved_movie) {
                SavedMovies savedMovie = list.get(getAdapterPosition());
                List<SavedMovies> savedMovies = SavedMovies.find(SavedMovies.class, "movie = ?", String.valueOf(savedMovie.getMovie()));
                SavedMovies movieToDelete=savedMovies.get(0);
                list.remove(movieToDelete);
                movieToDelete.delete();
                notifyDataSetChanged();
                return true;
            }
            return false;
        }
    }
}
