package com.example.jeevan.moviedatabase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.example.jeevan.moviedatabase.DataClass.CastData;
import com.example.jeevan.moviedatabase.DataClass.CrewData;
import com.example.jeevan.moviedatabase.DataClass.Movie;
import com.example.jeevan.moviedatabase.adapters.MyCastRecyclerViewAdapter;
import com.example.jeevan.moviedatabase.adapters.MyCrewRecyclerViewAdapter;
import com.example.jeevan.moviedatabase.adapters.MyRecommendationRecycleViewAdapter;
import com.example.jeevan.moviedatabase.utility.MySingleton;
import com.example.jeevan.moviedatabase.utility.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.graphics.Color.WHITE;

public class DetailsActivity extends AppCompatActivity {

    private static final String API_KEY = "1f69acc59bef020eb4fa9e8c3976b096";
    private static final String urlPart = "?api_key=";
    private static final String imageUrl = "https://image.tmdb.org/t/p/w780";
    private String appendToResponse = "&append_to_response=videos,credits,recommendations";
    private TextView nameOfTheMovie, date, description, genre;
    private RatingBar ratingBar;
    private NetworkImageView networkImageView;
    private Toolbar toolbar;
    private RecyclerView castRecyclerView, crewRecyclerView, recommendedRecyclerView;
    private int id;
    private StringRequest request;
    private ArrayList<CastData> castList;
    private MyCastRecyclerViewAdapter adapter;
    private String urlStart = "https://api.themoviedb.org/3/movie/";
    private MyCrewRecyclerViewAdapter crewAdapter;
    private ArrayList<CrewData> crewList;
    private NetworkImageView trailerImage;
    private ArrayList<String> genreOfMovieList;
    private ArrayList<Movie> recommendedMovieList;
    private String trailerKey;
    private String trailerImageUrl;
    private MyRecommendationRecycleViewAdapter myRecommendationRecycleViewAdapter;
    private String backdropImageUrl;
    private ProgressDialog progressDialog;
    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        findViewByIds();

        getIntentData();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(nameOfTheMovie.getText().toString());
        toolbar.setTitleTextColor(WHITE);

        getDataFromNetwork(id);

        setAdapters();


        recommendedRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Movie current = recommendedMovieList.get(position);

                nameOfTheMovie.setText(current.getName());
                date.setText(current.getReleaseDate());
                networkImageView.setImageBitmap(null);
                networkImageView.setImageUrl(imageUrl + current.getBackdropImageUrl(), MySingleton.getInstance(getApplicationContext()).getImageLoader());
                ratingBar.setRating((float) current.getRating() / 2);
                description.setText(current.getDescription());
                toolbar.setTitle(current.getName());

                getDataFromNetwork(current.getMovieId());
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }, recommendedRecyclerView));

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDataFromNetwork(int id) {


        final String url = urlStart + id + urlPart + API_KEY + appendToResponse;
        System.out.println(url);

//        queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();

        request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                parseJson(response);


                System.out.println(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error......", error.toString());
                error.printStackTrace();
            }
        });
        request.setTag(getApplicationContext());
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    @Override
    protected void onDestroy() {
        MySingleton.getInstance(getApplicationContext()).cancelPendingRequests(getApplicationContext());
        super.onDestroy();
    }

    private void parseJson(String response) {

        recommendedMovieList.clear();
        genreOfMovieList.clear();
        castList.clear();
        crewList.clear();

        try {
            JSONObject object = new JSONObject(response);
            JSONObject myObject = object.getJSONObject("credits");
            JSONArray array = myObject.getJSONArray("cast");

            CastData current;
            for (int i = 0; i < array.length(); i++) {
                current = new CastData();
                JSONObject object1 = array.getJSONObject(i);
                current.setCastName(object1.getString("name"));
                current.setCharacterName(object1.getString("character"));
                current.setImageUrl(object1.getString("profile_path"));
                castList.add(current);
                adapter.notifyDataSetChanged();

            }

            JSONArray array1 = myObject.getJSONArray("crew");
            CrewData crewData;
            for (int i = 0; i < array1.length(); i++) {
                crewData = new CrewData();
                JSONObject object1 = array1.getJSONObject(i);
                crewData.setCrewJob(object1.getString("name"));
                crewData.setCrewName(object1.getString("job"));
                crewList.add(crewData);
                adapter.notifyDataSetChanged();
            }

            JSONObject jsonObjectRecommended = object.getJSONObject("recommendations");
            JSONArray movieRecommendationsArray = jsonObjectRecommended.getJSONArray("results");
            Movie currentMovie;
            for (int i = 0; i < movieRecommendationsArray.length(); i++) {
                currentMovie = new Movie();
                JSONObject recommendedMovieObject = movieRecommendationsArray.getJSONObject(i);
                currentMovie.setName(recommendedMovieObject.getString("title"));
                currentMovie.setImageUrl(recommendedMovieObject.getString("poster_path"));
                currentMovie.setDescription(recommendedMovieObject.getString("overview"));
                currentMovie.setRating(recommendedMovieObject.getDouble("vote_average"));
                currentMovie.setBackdropImageUrl(recommendedMovieObject.getString("backdrop_path"));
                currentMovie.setMovieId(recommendedMovieObject.getInt("id"));
                currentMovie.setReleaseDate(recommendedMovieObject.getString("release_date"));

                recommendedMovieList.add(currentMovie);
                myRecommendationRecycleViewAdapter.notifyDataSetChanged();
            }


            JSONArray myGenreJsonArray = object.getJSONArray("genres");

            for (int i = 0; i < myGenreJsonArray.length(); i++) {
                JSONObject myGenreJsonObject = myGenreJsonArray.getJSONObject(i);
                genreOfMovieList.add(myGenreJsonObject.getString("name"));
            }

            String genreString = "";

            for (int i = 0; i < genreOfMovieList.size(); i++) {
                genreString = genreString + genreOfMovieList.get(i);
                if (genreOfMovieList.size() != i + 1)
                    genreString = genreString + ", ";
            }
            genre.setText(genreString);
            System.out.println(genreString);


            JSONObject videoObject = object.getJSONObject("videos");
            JSONArray videoArray = videoObject.getJSONArray("results");
            if (videoArray.length() == 0) {
                trailerKey = "null";
            } else {
                trailerKey = videoArray.getJSONObject(0).getString("key");
            }
            System.out.println(trailerKey);
            trailerImageUrl = "http://img.youtube.com/vi/" + trailerKey + "/0.jpg";

//            trailerImage.setDefaultImageResId(R.drawable.video_not_available);
            trailerImage.setImageUrl(trailerImageUrl,
                    MySingleton.getInstance(getApplicationContext()).getImageLoader());

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void openLink(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + trailerKey)));

    }

    private void setAdapters() {
        adapter = new MyCastRecyclerViewAdapter(getApplicationContext(), castList);
        castRecyclerView.setAdapter(adapter);
        castRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        crewAdapter = new MyCrewRecyclerViewAdapter(getApplicationContext(), crewList);
        crewRecyclerView.setAdapter(crewAdapter);
        crewRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        myRecommendationRecycleViewAdapter = new MyRecommendationRecycleViewAdapter(getApplicationContext(), recommendedMovieList);
        recommendedRecyclerView.setAdapter(myRecommendationRecycleViewAdapter);
        recommendedRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false));
    }

    private void getIntentData() {
        Intent intent = getIntent();
        backdropImageUrl = intent.getStringExtra("backdropImage");
        if (backdropImageUrl.equals("null")) {
            networkImageView.setImageUrl("http://cdn.shopify.com/s/files/1/0231/7685/t/3/assets/no-image-available.png?11271105939021230741", MySingleton.getInstance(this).getImageLoader());
        } else {
            networkImageView.setImageUrl(imageUrl + backdropImageUrl, MySingleton.getInstance(getApplicationContext()).getImageLoader());
        }
        id = intent.getIntExtra("id", 0);
        nameOfTheMovie.setText(intent.getStringExtra("name"));
        double rat = intent.getDoubleExtra("rating", 0) / 2;
        ratingBar.setRating((float) rat);
        description.setText(intent.getStringExtra("desc"));
        date.setText(intent.getStringExtra("date"));
    }

    private void findViewByIds() {

        toolbar = (Toolbar) findViewById(R.id.details_toolbar);
        castRecyclerView = (RecyclerView) findViewById(R.id.cast_recycle_view);
        castList = new ArrayList<>();
        crewList = new ArrayList<>();
        genreOfMovieList = new ArrayList<>();
        recommendedMovieList = new ArrayList<>();

        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24px);

        nameOfTheMovie = (TextView) findViewById(R.id.details_name_of_movie);
        date = (TextView) findViewById(R.id.details_date);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        description = (TextView) findViewById(R.id.details_description);
        networkImageView = (NetworkImageView) findViewById(R.id.details_image_view);
        genre = (TextView) findViewById(R.id.details_genre);
        crewRecyclerView = (RecyclerView) findViewById(R.id.crew_recycle_view);
        trailerImage = (NetworkImageView) findViewById(R.id.trailer_imageView);
        recommendedRecyclerView = (RecyclerView) findViewById(R.id.recommended_recycleView);
    }

}
