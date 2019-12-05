package com.free.top.tvshows.tube.api;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.free.top.tvshows.tube.Application;

import org.json.JSONException;
import org.json.JSONObject;

public class OMDb {

    private static final String API_KEY = "b7919019";

    public static void getRating(String id, final RatingListener listener) {
        String url = "http://www.omdbapi.com/?i=" + id + "&apikey=" + API_KEY;

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("OMDb.getRating", response.toString());
                    double imdbRating = -1;
                    int metascore = -1;
                    if (response.has("imdbRating") && !response.isNull("imdbRating")) {
                        String imdbRatingStr = response.getString("imdbRating");
                        if (!imdbRatingStr.equals("N/A"))
                            imdbRating = response.getDouble("imdbRating");
                    }
                    if  (response.has("Metascore") && !response.isNull("Metascore")) {
                        String metascoreStr = response.getString("Metascore");
                        if (!metascoreStr.equals("N/A"))
                            metascore = response.getInt("Metascore");
                    }
                    listener.onSuccess(imdbRating, metascore);
                } catch (JSONException e) {
                    Log.e("OMDb.getRating", e.toString());
                    listener.onError();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("OMDb.getRating", error.toString());
                listener.onError();
            }
        };

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, responseListener, errorListener);
        Application.getRequestQueue().add(jsonObjectRequest);
    }

    public interface RatingListener {
        void onSuccess(double IMDbRating, int metascore);
        void onError();
    }
}
