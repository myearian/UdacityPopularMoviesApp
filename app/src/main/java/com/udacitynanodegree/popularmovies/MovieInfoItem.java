package com.udacitynanodegree.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieInfoItem implements Parcelable{
    int movieId;
    String movieTitle;
    double moviePopularity;
    double movieAvgRating;
    String moviePosterImgPath;
    String movieOverview;
    String movieReleaseDate;
    public MovieInfoItem(int id,
                         String title,
                         double popularity,
                         double avgRating,
                         String imgPath,
                         String overview,
                         String releaseDate){
        this.movieId = id;
        this.movieTitle = title;
        this.moviePopularity = popularity;
        this.movieAvgRating = avgRating;
        this.moviePosterImgPath = imgPath;
        this.movieOverview = overview;
        this.movieReleaseDate = releaseDate;
    }

    private MovieInfoItem(Parcel in){
        movieId = in.readInt();
        movieTitle = in.readString();
        moviePopularity = in.readDouble();
        movieAvgRating = in.readDouble();
        moviePosterImgPath = in.readString();
        movieOverview = in.readString();
        movieReleaseDate = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(movieTitle);
        dest.writeDouble(moviePopularity);
        dest.writeDouble(movieAvgRating);
        dest.writeString(moviePosterImgPath);
        dest.writeString(movieOverview);
        dest.writeString(movieReleaseDate);
    }

    public static final Parcelable.Creator<MovieInfoItem> CREATOR =
            new Parcelable.Creator<MovieInfoItem>() {
            @Override
            public MovieInfoItem createFromParcel(Parcel parcel) {
                return new MovieInfoItem(parcel);
            }
            @Override
            public MovieInfoItem[] newArray(int i){
                return new MovieInfoItem[i];
            }
    };
}
