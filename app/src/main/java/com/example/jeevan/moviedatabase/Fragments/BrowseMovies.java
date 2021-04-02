package com.example.jeevan.moviedatabase.Fragments;import android.app.ProgressDialog;import android.content.Context;import android.graphics.Bitmap;import android.graphics.BitmapFactory;import android.os.AsyncTask;import android.os.Bundle;import android.support.annotation.Nullable;import android.support.v4.app.Fragment;import android.support.v7.widget.LinearLayoutManager;import android.support.v7.widget.RecyclerView;import android.util.Log;import android.view.LayoutInflater;import android.view.Menu;import android.view.MenuInflater;import android.view.MenuItem;import android.view.View;import android.view.ViewGroup;import android.widget.Toast;import com.android.volley.Request;import com.android.volley.RequestQueue;import com.android.volley.Response;import com.android.volley.VolleyError;import com.android.volley.toolbox.ImageRequest;import com.android.volley.toolbox.StringRequest;import com.example.jeevan.moviedatabase.DataClass.Movie;import com.example.jeevan.moviedatabase.R;import com.example.jeevan.moviedatabase.adapters.RecyclerViewAdapter;import com.example.jeevan.moviedatabase.utility.MySingleton;import com.squareup.picasso.Picasso;import com.squareup.picasso.Target;import org.json.JSONArray;import org.json.JSONException;import org.json.JSONObject;import java.io.IOException;import java.text.SimpleDateFormat;import java.util.ArrayList;import java.util.Date;import static android.widget.Toast.LENGTH_SHORT;public class BrowseMovies extends Fragment {    static final String IMGURL = "https://image.tmdb.org/t/p/w185";    private static final String API_KEY = "1f69acc59bef020eb4fa9e8c3976b096";    private static final int startPage = 1;    private ArrayList<Movie> movieArrayList;    private Movie currentMovie;    private RecyclerViewAdapter recyclerViewAdapter;    private RequestQueue requestQueue;    private int page = 1;    private String id = "";    private String sortBy = "popularity.desc";    private String language = "en";    private static final String TAG = "BROWSE MOVIES";    private Context context;    private Target mTarget;    private ProgressDialog progressDialog;    @Override    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {        super.onViewCreated(view, savedInstanceState);        context = this.getContext();        movieArrayList = new ArrayList<>();        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_movie_popular);        setHasOptionsMenu(true);        progressDialog = new ProgressDialog(getContext());        progressDialog.setMessage("Please wait...");        progressDialog.setCancelable(true);        progressDialog.show();        requestQueue = MySingleton.getInstance(this.getContext()).getRequestQueue();        sendJsonQuery(page);        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), movieArrayList);        recyclerView.setAdapter(recyclerViewAdapter);        recyclerView.setLayoutManager(mLinearLayoutManager);        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {            @Override            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {                super.onScrolled(recyclerView, dx, dy);                if (!recyclerView.canScrollVertically(1)) {                    page++;                    sendJsonQuery(page);                }            }        });    }    private void sendJsonQuery(int page) {        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");        Date d = new Date();        String date = sdf.format(d.getTime());        Log.v("Date", date);        String tempUrl =                "https://api.themoviedb.org/3/discover/movie?" +                        "api_key=" +                        API_KEY +                        "&sort_by=" +                        sortBy +                        "&include_adult=true&include_video=true" +                        "&include_adult=false"+                        "&page=" +                        page +                        "&release_date.lte=" +                        date +                        "&with_genres=" +                        id +                        "&with_original_language=" +                        language;        String url = "https://api.themoviedb.org/3/movie/now_playing?"                + "api_key="                + API_KEY                + "&language=en-US&page="                + page;        System.out.println(url);        System.out.println("tempUrl: " + tempUrl);        StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl, new Response.Listener<String>() {            @Override            public void onResponse(String response) {                parseJson(response);                recyclerViewAdapter.notifyDataSetChanged();            }        }, new Response.ErrorListener() {            @Override            public void onErrorResponse(VolleyError error) {                Log.e("error.......", error.toString());                error.printStackTrace();            }        });        MySingleton.getInstance(this.getContext()).addToRequestQueue(stringRequest);    }    private void parseJson(final String response) {        try {            JSONObject jsonObject = new JSONObject(response);            JSONArray array = jsonObject.getJSONArray("results");            for (int i = 0; i < array.length(); i++) {                final Movie movie = new Movie();                JSONObject object = array.getJSONObject(i);                movie.setImageUrl(object.getString("poster_path"));                movie.setName(object.getString("title"));                movie.setMovieId(object.getInt("id"));                movie.setBackdropImageUrl(object.getString("backdrop_path"));                movie.setReleaseDate(object.getString("release_date"));                movie.setRating(object.getDouble("vote_average"));                movie.setDescription(object.getString("overview"));                String finalUrl = IMGURL + object.getString("poster_path");                ImageRequest imageRequest = new ImageRequest(finalUrl, new Response.Listener<Bitmap>() {                    @Override                    public void onResponse(Bitmap bitmap) {                        movie.setBitmap(bitmap);                    }                }, 0, 0, null, null, new Response.ErrorListener() {                    @Override                    public void onErrorResponse(VolleyError volleyError) {                        Log.e("VOLLEY BITMAP ERROR", "True");                        Bitmap errorBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.leak_canary_icon);                        movie.setBitmap(errorBitmap);                    }                });                MySingleton.getInstance(getContext()).addToRequestQueue(imageRequest);                movieArrayList.add(movie);                if (progressDialog.isShowing()) {                    progressDialog.dismiss();                }            }        } catch (JSONException e) {            e.printStackTrace();        }    }    private class getBitmaps extends AsyncTask<String, Void, Bitmap> {        @Override        protected Bitmap doInBackground(String... strings) {            Bitmap bitmap = null;            try {                bitmap = Picasso.with(getContext()).load(strings[0]).get();            } catch (IOException e) {                e.printStackTrace();            }            return bitmap;        }        @Override        protected void onPostExecute(Bitmap bitmap) {            super.onPostExecute(bitmap);        }    }    @Override    public View onCreateView(LayoutInflater inflater, ViewGroup container,                             Bundle savedInstanceState) {        return inflater.inflate(R.layout.fragment_movie_popular, container, false);    }    @Override    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {        super.onCreateOptionsMenu(menu, inflater);        inflater.inflate(R.menu.genre_menu, menu);    }    @Override    public boolean onOptionsItemSelected(MenuItem item) {        switch (item.getItemId()) {            case R.id.horror_item:                id = "27";                changeListAfterMenuChange();                Toast.makeText(getContext(), "horror", LENGTH_SHORT).show();                break;            case R.id.comedy_item:                id = "35";                changeListAfterMenuChange();                Toast.makeText(getContext(), "comedy", LENGTH_SHORT).show();                break;            case R.id.thriller_item:                id = "53";                changeListAfterMenuChange();                Toast.makeText(getContext(), "thriller", LENGTH_SHORT).show();                break;            case R.id.adventure_item:                id = "12";                changeListAfterMenuChange();                Toast.makeText(getContext(), "adventure", LENGTH_SHORT).show();                break;            case R.id.animation_item:                id = "16";                changeListAfterMenuChange();                Toast.makeText(getContext(), "animation", LENGTH_SHORT).show();                break;            case R.id.crime_item:                id = "80";                changeListAfterMenuChange();                Toast.makeText(getContext(), "crime", LENGTH_SHORT).show();                break;            case R.id.music_item:                id = "10402";                changeListAfterMenuChange();                Toast.makeText(getContext(), "music", LENGTH_SHORT).show();                break;            case R.id.fantasy_item:                id = "14";                Toast.makeText(getContext(), "fantasy", LENGTH_SHORT).show();                break;            case R.id.drama_item:                id = "18";                changeListAfterMenuChange();                Toast.makeText(getContext(), "drama", LENGTH_SHORT).show();                break;            case R.id.science_item:                id = "878";                changeListAfterMenuChange();                Toast.makeText(getContext(), "science fiction", LENGTH_SHORT).show();                break;            case R.id.mystery_item:                id = "9648";                changeListAfterMenuChange();                Toast.makeText(getContext(), "mystery", LENGTH_SHORT).show();                break;            case R.id.romantic_item:                id = "10749";                Toast.makeText(getContext(), "romantic", LENGTH_SHORT).show();                changeListAfterMenuChange();                break;            case R.id.family_item:                id = "10751";                changeListAfterMenuChange();                Toast.makeText(getContext(), "family", LENGTH_SHORT).show();                break;            case R.id.documentary_item:                id = "99";                changeListAfterMenuChange();                Toast.makeText(getContext(), "documentary", LENGTH_SHORT).show();                break;        }        switch (item.getItemId()) {            case R.id.hindi_item:                language = "hi";                changeListAfterMenuChange();                break;            case R.id.english_item:                language = "en";                changeListAfterMenuChange();                break;            case R.id.punjabi_item:                language = "pa";                changeListAfterMenuChange();                break;        }        switch (item.getItemId()) {            case R.id.pop_desc_item:                sortBy = "popularity.desc";                changeListAfterMenuChange();                break;            case R.id.rel_date_item:                sortBy = "release_date.desc";                changeListAfterMenuChange();                break;            case R.id.rating_item:                sortBy = "vote_average.desc";                changeListAfterMenuChange();                break;        }        return super.onOptionsItemSelected(item);    }    private void changeListAfterMenuChange() {        page = startPage;        movieArrayList.clear();        sendJsonQuery(startPage);    }}