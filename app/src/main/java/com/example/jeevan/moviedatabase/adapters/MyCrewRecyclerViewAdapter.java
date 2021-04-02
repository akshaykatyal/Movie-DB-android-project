package com.example.jeevan.moviedatabase.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jeevan.moviedatabase.DataClass.CrewData;
import com.example.jeevan.moviedatabase.R;

import java.util.ArrayList;

/**
 * Created by jeevan on 18/3/17.
 */

public class MyCrewRecyclerViewAdapter extends RecyclerView.Adapter<MyCrewRecyclerViewAdapter.MyCrewViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<CrewData> list;

    public MyCrewRecyclerViewAdapter(Context context, ArrayList<CrewData> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public MyCrewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = inflater.inflate(R.layout.crew_recycle_items, parent, false);
        MyCrewViewHolder holder = new MyCrewViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyCrewViewHolder holder, int position) {
        CrewData current = list.get(position);
        holder.crewName.setText(current.getCrewName());
        holder.crewJob.setText(current.getCrewJob());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyCrewViewHolder extends RecyclerView.ViewHolder {

        TextView crewName, crewJob;

        MyCrewViewHolder(View itemView) {
            super(itemView);
            crewJob = (TextView) itemView.findViewById(R.id.crew_job);
            crewName = (TextView) itemView.findViewById(R.id.crew_name);
        }
    }
}
