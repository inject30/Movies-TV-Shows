package com.free.top.tvshows.tube.api.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class TVSeason extends BaseModel implements Serializable {

    private String _id;
    private String airDate;
    private TVEpisode[] episodes;
    private int episodeCount;
    private String name;
    private String overview;
    private int id;
    private String posterPath;
    private int seasonNumber;

    public String get_id() {
        return _id;
    }

    public String getAirDate() {
        return airDate;
    }

    public TVEpisode[] getEpisodes() {
        return episodes;
    }

    public int getEpisodeCount() {
        return episodeCount;
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

    public String getPosterPath() {
        return posterPath;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public static class Builder {

        private String _id;
        private String airDate;
        private TVEpisode[] episodes;
        private int episodeCount;
        private String name;
        private String overview;
        private int id;
        private String posterPath;
        private int seasonNumber;

        public Builder withJSONObject(JSONObject jsonObject) {
            try {
                if (jsonObject.has("_id"))
                    _id = jsonObject.getString("_id");
                if (jsonObject.has("air_date"))
                    airDate = jsonObject.getString("air_date");
                if (jsonObject.has("episodes")) {
                    JSONArray jEpisodes = jsonObject.getJSONArray("episodes");
                    episodes = new TVEpisode[jEpisodes.length()];
                    for (int i = 0; i < jEpisodes.length(); i++) {
                        episodes[i] = new TVEpisode.Builder().withJSONObject(jEpisodes.getJSONObject(i)).build();
                    }
                    episodeCount = episodes.length;
                }
                if (jsonObject.has("episode_count"))
                    episodeCount = jsonObject.getInt("episode_count");
                if (jsonObject.has("name"))
                    name = jsonObject.getString("name");
                if (jsonObject.has("overview"))
                    overview = jsonObject.getString("overview");
                if (jsonObject.has("id"))
                    id = jsonObject.getInt("id");
                if (jsonObject.has("poster_path"))
                    posterPath = jsonObject.getString("poster_path");
                if (jsonObject.has("season_number"))
                    seasonNumber = jsonObject.getInt("season_number");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return this;
        }

        public TVSeason build() {
            TVSeason tvSeason = new TVSeason();
            tvSeason._id = _id;
            tvSeason.airDate = airDate;
            tvSeason.episodes = episodes;
            tvSeason.episodeCount = episodeCount;
            tvSeason.name = name;
            tvSeason.overview = overview;
            tvSeason.id = id;
            tvSeason.posterPath = posterPath;
            tvSeason.seasonNumber = seasonNumber;
            return tvSeason;
        }
    }
}
