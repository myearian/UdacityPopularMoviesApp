package com.udacitynanodegree.popularmovies;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Bundle bundle = getActivity().getIntent().getExtras();

        MovieInfoItem selectedItem = bundle.getParcelable("selectedMovieItem");

        ImageView imageView = (ImageView) rootView.findViewById(R.id.posterView);
        TextView titleView = (TextView) rootView.findViewById(R.id.movieTitle);
        TextView releaseDateView = (TextView) rootView.findViewById(R.id.releaseDate);
        TextView userRatingView = (TextView) rootView.findViewById(R.id.userRating);
        TextView moviePlotView = (TextView) rootView.findViewById(R.id.plot);

        releaseDateView.setTypeface(releaseDateView.getTypeface(), Typeface.BOLD);
        userRatingView.setTypeface(userRatingView.getTypeface(), Typeface.BOLD);
        titleView.setTypeface(userRatingView.getTypeface(), Typeface.BOLD);
        moviePlotView.setMovementMethod(new ScrollingMovementMethod());

        String imageUrl = "http://image.tmdb.org/t/p/w500/" + selectedItem.moviePosterImgPath;
        Picasso.with(this.getContext()).load(imageUrl).into(imageView);
        titleView.setText(selectedItem.movieTitle);
        releaseDateView.setText("Released: " + selectedItem.movieReleaseDate);
        userRatingView.setText("User Rating: " + Double.toString(selectedItem.movieAvgRating) + "/10");
        moviePlotView.setText(selectedItem.movieOverview);

        return rootView;
    }
}