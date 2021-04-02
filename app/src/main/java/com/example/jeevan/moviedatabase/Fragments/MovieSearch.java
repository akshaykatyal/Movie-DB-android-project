package com.example.jeevan.moviedatabase.Fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

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


public class MovieSearch extends Fragment {

    final String url = "https://api.themoviedb.org/3/search/movie?api_key=1f69acc59bef020eb4fa9e8c3976b096&language=en-US&include_adult=false&query=";
    StringRequest request;
    RequestQueue requestQueue;
    ArrayList<Movie> movieArrayList;
    ArrayList<Bitmap> imageArrayList;
    String string, temp;
    SearchView searchView;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_movie_search, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        movieArrayList = new ArrayList<>();
        imageArrayList = new ArrayList<>();
        requestQueue = MySingleton.getInstance(this.getContext()).getRequestQueue();
        recyclerView = (RecyclerView) view.findViewById(R.id.list_view_of_search_items);

        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), movieArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.my_menu, menu);
        MenuItem item = menu.findItem(R.id.app_bar_search);
        searchView = (SearchView) item.getActionView();
        searchView.setIconified(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchMovie(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchMovie(newText);
                return false;
            }
        });

    }

    private void searchMovie(String query) {
        string = query;

        if (!string.equals("")) {
            movieArrayList.clear();
            temp = string;
            string = temp.replaceAll(" ", "%20");
            System.out.println(string);

            request = new StringRequest(Request.Method.GET, url + string, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    sendJsonRequest(response);
                    recyclerViewAdapter.notifyDataSetChanged();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error.......", error.toString());
                    error.printStackTrace();
                }
            });
            MySingleton.getInstance(getContext()).addToRequestQueue(request);
        } else {
            Toast.makeText(getContext(), "Search box cannot be empty", Toast.LENGTH_SHORT).show();
        }

    }

    private void sendJsonRequest(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONArray array = jsonObject.getJSONArray("results");
            System.out.println(response);


            for (int i = 0; i < array.length(); i++) {
                final Movie movie = new Movie();
                JSONObject object = array.getJSONObject(i);

                movie.setName(object.getString("title"));
                movie.setImageUrl(object.getString("poster_path"));
                movie.setReleaseDate(object.getString("release_date"));
                movie.setMovieId(object.getInt("id"));
                movie.setBackdropImageUrl(object.getString("backdrop_path"));
                movie.setRating(object.getDouble("vote_average"));
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
}