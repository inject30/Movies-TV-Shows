package com.free.top.tvshows.tube.api.model;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class CrewCredit implements Serializable, Comparable<CrewCredit> {

    private boolean adult;
    private String creditId;
    private String department;
    private int id;
    private String job;
    private String originalTitle;
    private String posterPath;
    private String releaseDate;
    private String title;

    public boolean isAdult() {
        return adult;
    }

    public String getCreditId() {
        return creditId;
    }

    public String getDepartment() {
        return department;
    }

    public int getId() {
        return id;
    }

    public String getJob() {
        return job;
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
    public int compareTo(@NonNull CrewCredit crewCredit) {
        if (getReleaseDate() == null && crewCredit.getReleaseDate() == null)
            return 0;
        else if (getReleaseDate() == null)
            return 1;
        else if (crewCredit.getReleaseDate() == null)
            return -1;
        else
            return crewCredit.getReleaseDate().compareTo(getReleaseDate());
    }

    public static class Builder {

        private boolean adult;
        private String creditId;
        private String department;
        private int id;
        private String job;
        private String originalTitle;
        private String posterPath;
        private String releaseDate;
        private String title;

        public Builder withJSONObject(JSONObject jsonObject) {
            try {
                if (jsonObject.has("adult"))
                    adult = jsonObject.getBoolean("adult");
                if (jsonObject.has("credit_id"))
                    creditId = jsonObject.getString("credit_id");
                if (jsonObject.has("department"))
                    department = jsonObject.getString("department");
                if(jsonObject.has("id"))
                    id = jsonObject.getInt("id");
                if (jsonObject.has("job"))
                    job = jsonObject.getString("job");
                if (jsonObject.has("original_title"))
                    originalTitle = jsonObject.getString("original_title");
                if (jsonObject.has("poster_path") && !jsonObject.isNull("poster_path"))
                    posterPath = jsonObject.getString("poster_path");
                if (jsonObject.has("release_date") && !jsonObject.isNull("release_date"))
                    releaseDate = jsonObject.getString("release_date");
                if (jsonObject.has("title"))
                    title = jsonObject.getString("title");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return this;
        }

        public CrewCredit build() {
            CrewCredit crewCredit = new CrewCredit();
            crewCredit.adult = adult;
            crewCredit.creditId = creditId;
            crewCredit.department = department;
            crewCredit.id = id;
            crewCredit.job = job;
            crewCredit.originalTitle = originalTitle;
            crewCredit.posterPath = posterPath;
            crewCredit.releaseDate = releaseDate;
            crewCredit.title = title;
            return crewCredit;
        }
    }
}
