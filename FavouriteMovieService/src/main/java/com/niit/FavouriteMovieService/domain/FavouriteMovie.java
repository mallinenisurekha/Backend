package com.niit.FavouriteMovieService.domain;

import org.springframework.data.annotation.Id;

import java.util.List;
public class FavouriteMovie {
    @Id
    private String movieId;
    private String movieName;
    private double rating;
    private String genre;
    private String yearOfRelease;
    private List<String> language;
    private List<String> cast;
    private String movieSynopsis;
    private String moviePosterUrl;
    private boolean isFavorite;

    public FavouriteMovie(String movieId, String movieName, double rating, String genre, String yearOfRelease, List<String> language,List<String> cast, String movieSynopsis, String moviePosterUrl, Boolean isFavorite) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.rating = rating;
        this.genre = genre;
        this.yearOfRelease = yearOfRelease;
        this.language = language;
        this.cast = cast;
        this.movieSynopsis = movieSynopsis;
        this.moviePosterUrl = moviePosterUrl;
        this.isFavorite = isFavorite;
    }

    public FavouriteMovie() {
    }

    @Override
    public String toString() {
        return "FavouriteMovie{" +
                "movieId='" + movieId + '\'' +
                ", movieName='" + movieName + '\'' +
                ", rating=" + rating +
                ", genre='" + genre + '\'' +
                ", yearOfRelease='" + yearOfRelease + '\'' +
                ", language=" + language + '\'' +
                ", cast=" + cast +
                ", movieSynopsis='" + movieSynopsis + '\'' +
                ", moviePosterUrl='" + moviePosterUrl + '\'' +
                ", isFavorite=" + isFavorite +
                '}';
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getYearOfRelease() {
        return yearOfRelease;
    }

    public void setYearOfRelease(String yearOfRelease) {
        this.yearOfRelease = yearOfRelease;
    }

    public List<String> getLanguage() {
        return language;
    }

    public void setLanguage(List<String> language) {
        this.language = language;
    }

    public List<String> getCast() {
        return cast;
    }

    public void setCast(List<String> cast) {
        this.cast = cast;
    }

    public String getMovieSynopsis() {
        return movieSynopsis;
    }

    public void setMovieSynopsis(String movieSynopsis) {
        this.movieSynopsis = movieSynopsis;
    }

    public String getMoviePosterUrl() {
        return moviePosterUrl;
    }

    public void setMoviePosterUrl(String moviePosterUrl) {
        this.moviePosterUrl = moviePosterUrl;
    }

    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}
