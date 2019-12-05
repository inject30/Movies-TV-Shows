package com.free.top.tvshows.tube.api.model;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TVCrewCredit implements Serializable, Comparable<TVCrewCredit> {

    private String creditId;
    private String department;
    private int episodeCount;
    private String firstAirDate;
    private int id;
    private String job;
    private String name;
    private String originalName;
    private String posterPath;

    public String getCreditId() {
        return creditId;
    }

    public String getDepartment() {
        return department;
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

    public String getJob() {
        return job;
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
    public int compareTo(@NonNull TVCrewCredit tvCrewCredit) {
        if (getFirstAirDate() == null && tvCrewCredit.getFirstAirDate() == null)
            return 0;
        else if (getFirstAirDate() == null)
            return 1;
        else if (tvCrewCredit.getFirstAirDate() == null)
            return -1;
        else
            return tvCrewCredit.getFirstAirDate().compareTo(getFirstAirDate());
    }

    public static class Builder {

        private String creditId;
        private String department;
        private int episodeCount;
        private String firstAirDate;
        private int id;
        private String job;
        private String name;
        private String originalName;
        private String posterPath;

        public Builder withJSONObject(JSONObject jsonObject) {
            try {
                if (jsonObject.has("credit_id"))
                    creditId = jsonObject.getString("credit_id");
                if (jsonObject.has("department"))
                    department = jsonObject.getString("department");
                if (jsonObject.has("episode_count"))
                    episodeCount = jsonObject.getInt("episode_count");
                if (jsonObject.has("first_air_date") && !jsonObject.isNull("first_air_date"))
                    firstAirDate = jsonObject.getString("first_air_date");
                if (jsonObject.has("id"))
                    id = jsonObject.getInt("id");
                if (jsonObject.has("job"))
                    job = jsonObject.getString("job");
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

        public TVCrewCredit build() {
            TVCrewCredit tvCrewCredit = new TVCrewCredit();
            tvCrewCredit.creditId = creditId;
            tvCrewCredit.department = department;
            tvCrewCredit.episodeCount = episodeCount;
            tvCrewCredit.firstAirDate = firstAirDate;
            tvCrewCredit.id = id;
            tvCrewCredit.job = job;
            tvCrewCredit.name = name;
            tvCrewCredit.originalName = originalName;
            tvCrewCredit.posterPath = posterPath;
            return tvCrewCredit;
        }
    }
}
