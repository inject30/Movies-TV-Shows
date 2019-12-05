package com.free.top.tvshows.tube.api.model;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CastCredit implements Serializable, Comparable<CastCredit> {

    private boolean adult;
    private String character;
    private String creditId;
    private int id;
    private String originalTitle;
    private String posterPath;
    private String releaseDate;
    private String title;

    public boolean isAdult() {
        return adult;
    }

    public String getCharacter() {
        return character;
    }

    public String getCreditId() {
        return creditId;
    }

    public int getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int compareTo(@NonNull CastCredit castCredit) {
        if (getReleaseDate() == null && castCredit.getReleaseDate() == null)
            return 0;
        else if (getReleaseDate() == null)
            return 1;
        else if (castCredit.getReleaseDate() == null)
            return -1;
        else
            return castCredit.getReleaseDate().compareTo(getReleaseDate());
    }

    public static class Builder {

        private boolean adult;
        private String character;
        private String creditId;
        private int id;
        private String originalTitle;
        private String posterPath;
        private String releaseDate;
        private String title;

        public Builder withJSONObject(JSONObject jsonObject) {
            try {
                if (jsonObject.has("adult"))
                    adult = jsonObject.getBoolean("adult");
                if (jsonObject.has("character"))
                    character = jsonObject.getString("character");
                if (jsonObject.has("credit_id"))
                    creditId = jsonObject.getString("credit_id");
                if (jsonObject.has("id"))
                    id = jsonObject.getInt("id");
                if (jsonObject.has("original_title"))
                    originalTitle = jsonObject.getString("original_title");
                if (jsonObject.has("poster_path") && !jsonObject.isNull("poster_path"))
                    posterPath = jsonObject.getString("poster_path");
                if  (jsonObject.has("release_date") && !jsonObject.isNull("release_date"))
                    releaseDate = jsonObject.getString("release_date");
                if (jsonObject.has("title"))
                    title = jsonObject.getString("title");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return this;
        }

        public CastCredit build() {
            CastCredit castCredit = new CastCredit();
            castCredit.adult = adult;
            castCredit.character = character;
            castCredit.creditId = creditId;
            castCredit.id = id;
            castCredit.originalTitle = originalTitle;
            castCredit.posterPath = posterPath;
            castCredit.releaseDate = releaseDate;
            castCredit.title = title;

            return castCredit;
        }
    }
}