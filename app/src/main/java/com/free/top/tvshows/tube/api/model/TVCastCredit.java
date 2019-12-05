package com.free.top.tvshows.tube.api.model;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TVCastCredit implements Serializable, Comparable<TVCastCredit> {

    private String character;
    private String creditId;
    private int episodeCount;
    private String firstAirDate;
    private int id;
    private String name;
    private String originalName;
    private String posterPath;

    public String getCharacter() {
        return character;
    }

    public String getCreditId() {
        return creditId;
    }

    public int getEpisodeCount() {
        return episodeCount;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getPosterPath() {
        return posterPath;
    }

    @Override
    public int compareTo(@NonNull TVCastCredit tvCastCredit) {
        if (getFirstAirDate() == null && tvCastCredit.getFirstAirDate() == null)
            return 0;
        else if (getFirstAirDate() == null)
            return 1;
        else if (tvCastCredit.getFirstAirDate() == null)
            return -1;
        else
            return tvCastCredit.getFirstAirDate().compareTo(getFirstAirDate());
    }

    public static class Builder {

        private String character;
        private String creditId;
        private int episodeCount;
        private String firstAirDate;
        private int id;
        private String name;
        private String originalName;
        private String posterPath;

        public Builder withJSONObject(JSONObject jsonObject) {
            try {
                if (jsonObject.has("character"))
                    character = jsonObject.getString("character");
                if (jsonObject.has("credit_id"))
                    creditId = jsonObject.getString("credit_id");
                if (jsonObject.has("episode_count"))
                    episodeCount = jsonObject.getInt("episode_count");
                if (jsonObject.has("first_air_date") && !jsonObject.isNull("first_air_date"))
                    firstAirDate = jsonObject.getString("first_air_date");
                if (jsonObject.has("id"))
                    id = jsonObject.getInt("id");
                if (jsonObject.has("name"))
                    name = jsonObject.getString("name");
                if (jsonObject.has("original_name"))
                    originalName = jsonObject.getString("original_name");
                if (jsonObject.has("poster_path") && !jsonObject.isNull("poster_path"))
                    posterPath = jsonObject.getString("poster_path");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return this;
        }

        public TVCastCredit build() {
            TVCastCredit tvCastCredit = new TVCastCredit();
            tvCastCredit.character = character;
            tvCastCredit.creditId = creditId;
            tvCastCredit.episodeCount = episodeCount;
            tvCastCredit.firstAirDate = firstAirDate;
            tvCastCredit.id = id;
            tvCastCredit.name = name;
            tvCastCredit.originalName = originalName;
            tvCastCredit.posterPath = posterPath;
            return tvCastCredit;
        }

    }
}
