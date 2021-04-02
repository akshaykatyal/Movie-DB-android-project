package com.example.jeevan.moviedatabase.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.jeevan.moviedatabase.DataClass.CastData;
import com.example.jeevan.moviedatabase.R;
import com.example.jeevan.moviedatabase.utility.MySingleton;

import java.util.ArrayList;

/**
 * Created by jeevan on 18/3/17.
 */

public class MyCastRecyclerViewAdapter extends RecyclerView.Adapter<MyCastRecyclerViewAdapter.MyViewHolder> {

    private static final String imageUrl = "https://image.tmdb.org/t/p/w185";
    private LayoutInflater inflater;
    private ArrayList<CastData> list;
    private Context context;

    public MyCastRecyclerViewAdapter(Context context, ArrayList<CastData> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.cast_recycle_view, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CastData current = list.get(position);
        holder.name.setText(current.getCastName());
        holder.character.setText(current.getCharacterName());
        holder.castImage.setImageUrl(imageUrl + current.getImageUrl(), MySingleton.getInstance(context).getImageLoader());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, character;
        NetworkImageView castImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.cast_name);
            character = (TextView) itemView.findViewById(R.id.cast_character);
            castImage = (NetworkImageView) itemView.findViewById(R.id.cast_image);
        }
    }

}
