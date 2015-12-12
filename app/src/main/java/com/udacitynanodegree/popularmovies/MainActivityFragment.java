package com.udacitynanodegree.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivityFragment extends Fragment {

    private ArrayAdapter<MovieInfoItem> movieInfoAdapter;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstancedState) {
        super.onCreate(savedInstancedState);
        setHasOptionsMenu(true);
    }

    private void updateMovieInfo() {
        GetMovieData refreshData = new GetMovieData();
        refreshData.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Setting up adapter for gridview
        List<MovieInfoItem> movieItems = new ArrayList<MovieInfoItem>();
        movieInfoAdapter = new CustomMovieInfoAdapter(getActivity(), movieItems);
        GridView movieGridView = (GridView) rootView.findViewById(R.id.mainGridView);
        movieGridView.setAdapter(movieInfoAdapter);

        //click listener for detail activity
        movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getActivity();
                Intent openDetailActivity = new Intent(context, DetailActivity.class);
                openDetailActivity.putExtra("selectedMovieItem", movieInfoAdapter.getItem(position));
                startActivity(openDetailActivity);
            }
        });
        return rootView;
    }

    public class GetMovieData extends AsyncTask<String, Void, List<MovieInfoItem>> {
        private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

        @Override
        protected List<MovieInfoItem> doInBackground(String... params) {
            //Setting up and executing query for movie info
            List<MovieInfoItem> retrievedMovieInfoItems = null;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieDbJson = null;
            String apiKey = "See Readme to obtain API KEY";
            String typeOfQuery = PreferenceManager.getDefaultSharedPreferences(getActivity())
                    .getString(getString(R.string.sort_by_key), getString(R.string.default_sort));
            try {
                final String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String QUERY_SORT = "sort_by";
                final String QUERY_APIKEY = "api_key";
                Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_SORT, typeOfQuery)
                        .appendQueryParameter(QUERY_APIKEY, apiKey)
                        .build();
                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                movieDbJson = buffer.toString();

                try {
                    retrievedMovieInfoItems = getMovieDataFromJson(movieDbJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(LOG_TAG, "Couldn't get data from API.");
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return retrievedMovieInfoItems;
        }

        private List<MovieInfoItem> getMovieDataFromJson(String movieDbJsonStr)
                throws JSONException {
            List<MovieInfoItem> movieItemsFromJson = new ArrayList<>();
            final String MDB_RESULT = "results";
            final String MDB_MOVIEID = "id";
            final String MDB_TITLE = "original_title";
            final String MDB_POPULARITY = "popularity";
            final String MDB_RATING_AVG = "vote_average";
            final String MDB_IMG_PATH = "poster_path";
            final String MDB_OVERVIEW = "overview";
            final String MDB_RELEASEDATE = "release_date";

            JSONObject movieJsonObject = new JSONObject(movieDbJsonStr);

            JSONArray movieInfoJsonArray = movieJsonObject.getJSONArray(MDB_RESULT);

            //Transferring JSON data into MovieInfoItem object
            for (int i = 0; i < movieInfoJsonArray.length(); i++) {
                JSONObject movieObject = movieInfoJsonArray.getJSONObject(i);
                int movieId = movieObject.getInt(MDB_MOVIEID);
                String movieTitle = movieObject.getString(MDB_TITLE);
                double moviePop = movieObject.getDouble(MDB_POPULARITY);
                double movieRating = movieObject.getDouble(MDB_RATING_AVG);
                String movieImgPath = movieObject.getString(MDB_IMG_PATH);
                String movieOverview = movieObject.getString(MDB_OVERVIEW);
                String movieReleaseDate = movieObject.getString(MDB_RELEASEDATE);
                MovieInfoItem movieInfo = new MovieInfoItem(movieId, movieTitle, moviePop,
                        movieRating, movieImgPath, movieOverview, movieReleaseDate);
                movieItemsFromJson.add(movieInfo);
            }
            return movieItemsFromJson;
        }

        @Override
        protected void onPostExecute(List<MovieInfoItem> items) {
            movieInfoAdapter.clear();
            if (items != null && !items.isEmpty()) {
                movieInfoAdapter.addAll(items);
            } else {
                Log.d(LOG_TAG, "Couldn't get data from API.");
            }
        }

    }

}
