package com.free.top.tvshows.tube.api.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Collection implements Serializable {

    private int id;
    private String name;
    private String overview;
    private String posterPath;
    private String backdropBath;
    private ArrayList<Movie> parts;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropBath() {
        return backdropBath;
    }

    public ArrayList<Movie> getParts() {
        return parts;
    }

    public static class Builder {

        private int id;
        private String name;
        private String overview;
        private String posterPath;
        private String backdropBath;
        private ArrayList<Movie> parts;

        public Builder withJSONObject(JSONObject jsonObject) {
            try {
                if (jsonObject.has("id"))
                    id = jsonObject.getInt("id");
                if (jsonObject.has("name"))
                    name = jsonObject.getString("name");
                if (jsonObject.has("overview"))
                    overview = jsonObject.getString("overview");
                if (jsonObject.has("poster_path") && !jsonObject.isNull("poster_path"))
                    posterPath = jsonObject.getString("poster_path");
                if (jsonObject.has("backdrop_path") && !jsonObject.isNull("backdrop_path"))
                    backdropBath = jsonObject.getString("backdrop_path");
                if (jsonObject.has("parts")) {
                    JSONArray jParts = jsonObject.getJSONArray("parts");
                    parts = new ArrayList<>();
                    for (int i = 0; i < jParts.length(); i++) {
                        parts.add(new Movie.Builder().withJSONObject(jParts.getJSONObject(i)).build());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return this;
        }

        public Collection build() {
            Collection collection = new Collection();
            collection.id = id;
            collection.name = name;
            collection.overview = overview;
            collection.posterPath = posterPath;
            collection.backdropBath = backdropBath;
            collection.parts = parts;
            return collection;
        }
    }
}
