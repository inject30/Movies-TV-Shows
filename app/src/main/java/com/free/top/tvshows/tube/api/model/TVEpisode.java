package com.free.top.tvshows.tube.api.model;

import org.json.JSONException;
import org.json.JSONObject;

public class TVEpisode {

    private String airDate;
    private int episodeNumber;
    private String name;
    private String overview;
    private int id;
    private String productionCode;
    private int seasonNumber;
    private String stillPath;
    private double voteAverage;
    private int voteCount;

    public String getAirDate() {
        return airDate;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public int getId() {
        return id;
    }

    public String getProductionCode() {
        return productionCode;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public String getStillPath() {
        return stillPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public static class Builder {

        private String airDate;
        private int episodeNumber;
        private String name;
        private String overview;
        private int id;
        private String productionCode;
        private int seasonNumber;
        private String stillPath;
        private double voteAverage;
        private int voteCount;

        public Builder withJSONObject(JSONObject jsonObject) {
            try {
                if (jsonObject.has("air_date") && !jsonObject.isNull("air_date"))
                    airDate = jsonObject.getString("air_date");
                if (jsonObject.has("episode_number"))
                    episodeNumber = jsonObject.getInt("episode_number");
                if (jsonObject.has("name"))
                    name = jsonObject.getString("name");
                if (jsonObject.has("overview"))
                    overview = jsonObject.getString("overview");
                if (jsonObject.has("id"))
                    id = jsonObject.getInt("id");
                if (jsonObject.has("production_code"))
                    productionCode = jsonObject.getString("production_code");
                if (jsonObject.has("season_number"))
                    seasonNumber = jsonObject.getInt("season_number");
                if (jsonObject.has("still_path") && !jsonObject.isNull("still_path"))
                    stillPath = jsonObject.getString("still_path");
                if (jsonObject.has("vote_average"))
                    voteAverage = jsonObject.getDouble("vote_average");
                if (jsonObject.has("vote_count"))
                    voteCount = jsonObject.getInt("vote_count");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return this;
        }

        public TVEpisode build() {
            TVEpisode tvEpisode = new TVEpisode();
            tvEpisode.airDate = airDate;
            tvEpisode.episodeNumber = episodeNumber;
            tvEpisode.name = name;
            tvEpisode.overview = overview;
            tvEpisode.id = id;
            tvEpisode.productionCode = productionCode;
            tvEpisode.seasonNumber = seasonNumber;
            tvEpisode.stillPath = stillPath;
            tvEpisode.voteAverage = voteAverage;
            tvEpisode.voteCount = voteCount;
            return tvEpisode;
        }
    }
}
