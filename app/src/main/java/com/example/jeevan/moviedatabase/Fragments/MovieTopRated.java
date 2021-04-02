package com.example.jeevan.moviedatabase.Fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.jeevan.moviedatabase.DataClass.Movie;
import com.example.jeevan.moviedatabase.R;
import com.example.jeevan.moviedatabase.adapters.RecyclerViewAdapter;
import com.example.jeevan.moviedatabase.utility.MySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.jeevan.moviedatabase.Fragments.BrowseMovies.IMGURL;


public class MovieTopRated extends Fragment {

    private static final String API_KEY = "1f69acc59bef020eb4fa9e8c3976b096";
    ArrayList<Movie> movieArrayList;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    private RequestQueue requestQueue;
    private int page = 1;
    private StringRequest stringRequest;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        movieArrayList = new ArrayList<>();
        requestQueue = MySingleton.getInstance(this.getContext()).getRequestQueue();
        recyclerView = (RecyclerView) view.findViewById(R.id.list_movie_top_rated);

        sendJsonQuery(page);

        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), movieArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    page++;
                    sendJsonQuery(page);
                }
            }
        });

    }


    private void sendJsonQuery(int page) {

        String url = "https://api.themoviedb.org/3/movie/top_rated?"
                + "api_key="
                + API_KEY
                + "&include_adult=false"
                + "&language=en-US&page="
                + page;
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                parseJson(response);
                recyclerViewAdapter.notifyDataSetChanged();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error.......", error.toString());
                error.printStackTrace();

            }
        });
        MySingleton.getInstance(this.getContext()).addToRequestQueue(stringRequest);
    }

    private void parseJson(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONArray array = jsonObject.getJSONArray("results");

            for (int i = 0; i < array.length(); i++) {
                final Movie movie = new Movie();
                JSONObject object = array.getJSONObject(i);

                movie.setName(object.getString("title"));
                movie.setImageUrl(object.getString("poster_path"));
                movie.setReleaseDate(object.getString("release_date"));
                movie.setRating(object.getDouble("vote_average"));
                movie.setMovieId(object.getInt("id"));
                movie.setBackdropImageUrl(object.getString("backdrop_path"));
                movie.setDescription(object.getString("overview"));

                String finalUrl = IMGURL + object.getString("poster_path");

                ImageRequest imageRequest = new ImageRequest(finalUrl, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        movie.setBitmap(bitmap);
                    }
                }, 0, 0, null, null, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("VOLLEY BITMAP ERROR", "True");
                        Bitmap errorBitmap = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.leak_canary_icon);
                        movie.setBitmap(errorBitmap);
                    }
                });
                MySingleton.getInstance(getContext()).addToRequestQueue(imageRequest);
                movieArrayList.add(movie);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_top_rated, container, false);
    }


}
