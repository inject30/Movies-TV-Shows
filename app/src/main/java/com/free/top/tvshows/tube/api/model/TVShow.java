package com.free.top.tvshows.tube.api.model;

import com.free.top.tvshows.tube.Application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class TVShow extends BaseModel implements Serializable {

    private String backdropPath;
    private Person[] createdBy;
    private int[] episodeRunTime;
    private String firstAirDate;
    private Genre[] genres;
    private String homepage;
    private int id;
    private String imdbId;
    private boolean inProduction;
    private String[] languages;
    private String lastAirDate;
    private String name;
    private Network[] networks;
    private int numberOfEpisodes;
    private int numberOfSeasons;
    private String originalLanguage;
    private String originalName;
    private String overview;
    private double popularity;
    private String posterPath;
    private Company[] productionCompanies;
    private TVSeason[] seasons;
    private String status;
    private String type;
    private double voteAverage;
    private int voteCount;

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Person[] getCreatedBy() {
        return createdBy;
    }

    public int[] getEpisodeRunTime() {
        return episodeRunTime;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public Genre[] getGenres() {
        return genres;
    }

    public void setGenres(Genre[] genres) {
        this.genres = genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public boolean isInProduction() {
        return inProduction;
    }

    public String[] getLanguages() {
        return languages;
    }

    public String getLastAirDate() {
        return lastAirDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Network[] getNetworks() {
        return networks;
    }

    public int getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getOverview() {
        return overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Company[] getProductionCompanies() {
        return productionCompanies;
    }

    public TVSeason[] getSeasons() {
        return seasons;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public static class Builder {

        private String backdropPath;
        private Person[] createdBy;
        private int[] episodeRunTime;
        private String firstAirDate;
        private Genre[] genres;
        private String homepage;
        private int id;
        private String imdbId;
        private boolean inProduction;
        private String[] languages;
        private String lastAirDate;
        private String name;
        private Network[] networks;
        private int numberOfEpisodes;
        private int numberOfSeasons;
        private String originalLanguage;
        private String originalName;
        private String overview;
        private double popularity;
        private String posterPath;
        private Company[] productionCompanies;
        private TVSeason[] seasons;
        private String status;
        private String type;
        private double voteAverage;
        private int voteCount;

        public Builder withJSONObject(JSONObject jsonObject) {
            try {
                if (jsonObject.has("backdrop_path"))
                    backdropPath = jsonObject.getString("backdrop_path");
                if (jsonObject.has("created_by")) {
                    JSONArray jCreatedBy = jsonObject.getJSONArray("created_by");
                    createdBy = new Person[jCreatedBy.length()];
                    for (int i = 0; i < jCreatedBy.length(); i++) {
                        createdBy[i] = new Person.Builder().withJSONObject(jCreatedBy.getJSONObject(i)).build();
                    }
                }
                if (jsonObject.has("episode_run_time")) {
                    JSONArray jEpisodeRunTime = jsonObject.getJSONArray("episode_run_time");
                    episodeRunTime = new int[jEpisodeRunTime.length()];
                    for(int i = 0; i < jEpisodeRunTime.length(); i++) {
                        episodeRunTime[i] = jEpisodeRunTime.getInt(i);
                    }
                }
                if (jsonObject.has("first_air_date"))
                    firstAirDate = jsonObject.getString("first_air_date");
                if (jsonObject.has("genres")) {
                    JSONArray jGenres = jsonObject.getJSONArray("genres");
                    genres = new Genre[jGenres.length()];
                    for (int i = 0; i < jGenres.length(); i++) {
                        genres[i] = new Genre.Builder().withJSONObject(jGenres.getJSONObject(i)).build();
                    }
                }
                if (jsonObject.has("genre_ids")) {
                    List<Genre> genresList = Application.getGenres();
                    JSONArray jGenresIds = jsonObject.getJSONArray("genre_ids");
                    genres = new Genre[jGenresIds.length()];
                    for (int i = 0; i < jGenresIds.length(); i++) {
                        int genreId = jGenresIds.getInt(i);
                        for (int j = 0; j < genresList.size(); j++) {
                            if (genresList.get(j).getId() == genreId) {
                                genres[i] = genresList.get(j);
                                break;
                            }
                        }
                    }
                }
                if (jsonObject.has("homepage"))
                    homepage = jsonObject.getString("homepage");
                if (jsonObject.has("id"))
                    id = jsonObject.getInt("id");
                if (jsonObject.has("external_ids")) {
                    JSONObject jExternalIds = jsonObject.getJSONObject("external_ids");
                    if (jExternalIds.has("imdb_id"))
                        imdbId = jExternalIds.getString("imdb_id");
                }
                if (jsonObject.has("in_production"))
                    inProduction = jsonObject.getBoolean("in_production");
                if (jsonObject.has("languages")) {
                    JSONArray jLanguages = jsonObject.getJSONArray("languages");
                    languages = new String[jLanguages.length()];
                    for (int i = 0; i < jLanguages.length(); i++) {
                        languages[i] = jLanguages.getString(i);
                    }
                }
                if (jsonObject.has("last_air_date"))
                    lastAirDate = jsonObject.getString("last_air_date");
                if (jsonObject.has("name"))
                    name = jsonObject.getString("name");
                if (jsonObject.has("networks")) {
                    JSONArray jNetworks = jsonObject.getJSONArray("networks");
                    networks = new Network[jNetworks.length()];
                    for (int i = 0; i < jNetworks.length(); i++) {
                        networks[i] = new Network.Builder().withJSONObject(jNetworks.getJSONObject(i)).build();
                    }
                } else
                if (jsonObject.has("number_of_episodes"))
                    numberOfEpisodes = jsonObject.getInt("number_of_episodes");
                if (jsonObject.has("number_of_seasons"))
                    numberOfSeasons = jsonObject.getInt("number_of_seasons");
                if (jsonObject.has("original_language"))
                    originalLanguage = jsonObject.getString("original_language");
                if (jsonObject.has("original_name"))
                    originalName = jsonObject.getString("original_name");
                if (jsonObject.has("overview") && !jsonObject.isNull("overview"))
                    overview = jsonObject.getString("overview");
                if (jsonObject.has("popularity"))
                    popularity = jsonObject.getDouble("popularity");
                if (jsonObject.has("poster_path") && !jsonObject.isNull("poster_path"))
                    posterPath = jsonObject.getString("poster_path");
                if (jsonObject.has("production_companies")) {
                    JSONArray jProductionCompanies = jsonObject.getJSONArray("production_companies");
                    productionCompanies = new Company[jProductionCompanies.length()];
                    for (int i = 0; i < jProductionCompanies.length(); i++) {
                        productionCompanies[i] = new Company.Builder().withJSONObject(jProductionCompanies.getJSONObject(i)).build();
                    }
                }
                if (jsonObject.has("seasons")) {
                    JSONArray jSeasons = jsonObject.getJSONArray("seasons");
                    seasons = new TVSeason[jSeasons.length()];
                    for (int i = 0; i < jSeasons.length(); i++) {
                        seasons[i] = new TVSeason.Builder().withJSONObject(jSeasons.getJSONObject(i)).build();
                    }
                }
                if (jsonObject.has("status"))
                    status = jsonObject.getString("status");
                if (jsonObject.has("type"))
                    type = jsonObject.getString("type");
                if (jsonObject.has("vote_average"))
                    voteAverage = jsonObject.getDouble("vote_average");
                if (jsonObject.has("vote_count"))
                    voteCount = jsonObject.getInt("vote_count");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return this;
        }

        public Builder withTVCastCredit(TVCastCredit tvCastCredit) {
            firstAirDate = tvCastCredit.getFirstAirDate();
            id = tvCastCredit.getId();
            name = tvCastCredit.getName();
            originalName = tvCastCredit.getOriginalName();
            posterPath = tvCastCredit.getPosterPath();
            return this;
        }

        public Builder withTVCrewCredit(TVCrewCredit tvCrewCredit) {
            firstAirDate = tvCrewCredit.getFirstAirDate();
            id = tvCrewCredit.getId();
            name = tvCrewCredit.getName();
            originalName = tvCrewCredit.getOriginalName();
            posterPath = tvCrewCredit.getPosterPath();
            return this;
        }

        public TVShow build() {
            TVShow tvShow = new TVShow();
            tvShow.backdropPath = backdropPath;
            tvShow.createdBy = createdBy;
            tvShow.episodeRunTime = episodeRunTime;
            tvShow.firstAirDate = firstAirDate;
            tvShow.genres = genres;
            tvShow.homepage = homepage;
            tvShow.id = id;
            tvShow.imdbId = imdbId;
            tvShow.inProduction = inProduction;
            tvShow.languages = languages;
            tvShow.lastAirDate = lastAirDate;
            tvShow.name = name;
            tvShow.networks = networks;
            tvShow.numberOfEpisodes = numberOfEpisodes;
            tvShow.numberOfSeasons = numberOfSeasons;
            tvShow.originalLanguage= originalLanguage;
            tvShow.originalName = originalName;
            tvShow.overview = overview;
            tvShow.popularity = popularity;
            tvShow.posterPath = posterPath;
            tvShow.productionCompanies = productionCompanies;
            tvShow.seasons = seasons;
            tvShow.status = status;
            tvShow.type = type;
            tvShow.voteAverage = voteAverage;
            tvShow.voteCount = voteCount;
            return tvShow;
        }
    }
}
