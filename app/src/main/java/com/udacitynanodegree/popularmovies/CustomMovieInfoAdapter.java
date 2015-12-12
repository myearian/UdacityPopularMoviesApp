package com.udacitynanodegree.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomMovieInfoAdapter extends ArrayAdapter <MovieInfoItem> {

    public CustomMovieInfoAdapter(Activity context, List<MovieInfoItem> movieInfoItems){
        super(context, 0, movieInfoItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        MovieInfoItem movieInfoItem = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.grid_movie_item,parent,false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_item_image);
        String imageUrl = "http://image.tmdb.org/t/p/w342/" + movieInfoItem.moviePosterImgPath;
        Picasso.with(this.getContext()).load(imageUrl).placeholder(R.drawable.placeholder).into(imageView);

        return convertView;
    }
}
