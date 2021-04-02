package com.example.jeevan.moviedatabase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.jeevan.moviedatabase.DataClass.SavedMovies;
import com.example.jeevan.moviedatabase.adapters.MyWatchLaterAndFavAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.WHITE;

public class WatchLater extends AppCompatActivity {
    RecyclerView recyclerView;
    MyWatchLaterAndFavAdapter recyclerViewAdapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_later);
        recyclerView = (RecyclerView) findViewById(R.id.watch_later_recycleView);

        toolbar = (Toolbar) findViewById(R.id.watch_later_toolbar);
        toolbar.setTitle("Watch Later");
        toolbar.setTitleTextColor(WHITE);

        ArrayList<SavedMovies> savedMoviesArrayList = new ArrayList<>();
        List<SavedMovies> savedMoviesList = SavedMovies.listAll(SavedMovies.class);
        savedMoviesArrayList.addAll(savedMoviesList);
        Log.e("size of list", String.valueOf(savedMoviesList.size()));

        recyclerViewAdapter = new MyWatchLaterAndFavAdapter(this, savedMoviesArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
