package com.example.jeevan.moviedatabase.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.jeevan.moviedatabase.DataClass.Movie;
import com.example.jeevan.moviedatabase.R;
import com.example.jeevan.moviedatabase.utility.MySingleton;

import java.util.ArrayList;

/**
 * Created by jeevan on 19/3/17.
 */

public class MyRecommendationRecycleViewAdapter extends RecyclerView.Adapter<MyRecommendationRecycleViewAdapter.MyRecommendationViewHolder> {

    private static final String imageUrl = "https://image.tmdb.org/t/p/w185";
    private LayoutInflater inflater;
    private ArrayList<Movie> list;
    private Context context;


    public MyRecommendationRecycleViewAdapter(Context context, ArrayList<Movie> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
    }

    @Override
    public MyRecommendationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.recommended_list_items, parent, false);
        MyRecommendationViewHolder viewHolder = new MyRecommendationViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyRecommendationViewHolder holder, int position) {
        Movie current = list.get(position);
        holder.recommendedName.setText(current.getName());
        holder.ratingBar.setRating((float) current.getRating() / 2);
        holder.networkImageView.setImageUrl(imageUrl + current.getImageUrl(), MySingleton.getInstance(context).getImageLoader());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class MyRecommendationViewHolder extends RecyclerView.ViewHolder {

        TextView recommendedName, recommendedGenre;
        NetworkImageView networkImageView;
        RatingBar ratingBar;
        CardView cardView;

        MyRecommendationViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.my_card_view_recommended);
            recommendedName = (TextView) itemView.findViewById(R.id.recommended_movie_name);
            recommendedGenre = (TextView) itemView.findViewById(R.id.recommended_movie_genre);
            networkImageView = (NetworkImageView) itemView.findViewById(R.id.recommended_movie_image);
            ratingBar = (RatingBar) itemView.findViewById(R.id.recommended_movie_rating);
        }
    }
}
