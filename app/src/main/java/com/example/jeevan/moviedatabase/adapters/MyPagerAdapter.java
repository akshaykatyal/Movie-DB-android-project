package com.example.jeevan.moviedatabase.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.jeevan.moviedatabase.Fragments.BrowseMovies;
import com.example.jeevan.moviedatabase.Fragments.MovieSearch;
import com.example.jeevan.moviedatabase.Fragments.MovieTopRated;

/**
 * Created by jeevan on 15/3/17.
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private int mNumOfTabs;

    public MyPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumOfTabs = numOfTabs;
    }


    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                BrowseMovies browseMovies = new BrowseMovies();
                return browseMovies;
            case 1:
                MovieTopRated movieTopRated = new MovieTopRated();
                return movieTopRated;
            case 2:
                MovieSearch movieSearch = new MovieSearch();
                return movieSearch;
            default:
                return null;
        }
    }

}
