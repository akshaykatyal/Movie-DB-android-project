package com.example.jeevan.moviedatabase.DataClass;

import com.orm.SugarRecord;

/**
 * Created by jeevan on 26/3/17.
 */

public class SavedMovies extends SugarRecord {

    private String name, date;
    private double rating;
    private int movie; // movie id
    private byte[] image;

    public SavedMovies(String name, String date, double rating, int movieId,byte[] image) {
        this.name = name;
        this.date = date;
        this.rating = rating;
        this.movie = movieId;
        this.image = image;
    }

    public SavedMovies() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getMovie() {
        return movie;
    }

    public void setMovie(int movie) {
        this.movie = movie;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SavedMovies) {
            SavedMovies temp = (SavedMovies) obj;
            return this.movie == temp.movie;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return movie;
    }

}
