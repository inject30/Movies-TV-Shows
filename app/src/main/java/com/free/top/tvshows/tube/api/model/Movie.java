package com.free.top.tvshows.tube.api.model;

import com.free.top.tvshows.tube.Application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class Movie extends BaseModel implements Serializable {

    private boolean adult;
    private String backdropPath;
    private Collection belongsToCollection;
    private int budget;
    private Genre[] genres;
    private String homepage;
    private int id;
    private String imdbId;
    private String originalLanguage;
    private String originalTitle;
    private String overview;
    private double popularity;
    private String posterPath;
    private Company[] productionCompanies;
    private Country[] productionCountries;
    private String releaseDate;
    private int revenue;
    private int runtime;
    private Language[] spokenLanguages;
    private String status;
    private String tagline;
    private String title;
    private boolean video;
    private double voteAverage;
    private int voteCount;

    public boolean isAdult() {
        return adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Collection getBelongsToCollection() {
        return belongsToCollection;
    }

    public int getBudget() {
        return budget;
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

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
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

    public Country[] getProductionCountries() {
        return productionCountries;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getRevenue() {
        return revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public Language[] getSpokenLanguages() {
        return spokenLanguages;
    }

    public String getStatus() {
        return status;
    }

    public String getTagline() {
        return tagline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVideo() {
        return video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public static class Builder {

        private boolean adult;
        private String backdropPath;
        private Collection belongsToCollection;
        private int budget;
        private Genre[] genres;
        private String homepage;
        private int id;
        private String imdbId;
        private String originalLanguage;
        private String originalTitle;
        private String overview;
        private double popularity;
        private String posterPath;
        private Company[] productionCompanies;
        private Country[] productionCountries;
        private String releaseDate;
        private int revenue;
        private int runtime;
        private Language[] spokenLanguages;
        private String status;
        private String tagline;
        private String title;
        private boolean video;
        private double voteAverage;
        private int voteCount;

        public Builder withJSONObject(JSONObject jsonObject) {
            try {
                if (jsonObject.has("adult"))
                    adult = jsonObject.getBoolean("adult");
                if (jsonObject.has("backdrop_path") && !jsonObject.isNull("backdrop_path"))
                    backdropPath = jsonObject.getString("backdrop_path");
                if (jsonObject.has("belongs_to_collection") && !jsonObject.isNull("belongs_to_collection"))
                    belongsToCollection = new Collection.Builder().withJSONObject(jsonObject.getJSONObject("belongs_to_collection")).build();
                if (jsonObject.has("budget"))
                    budget = jsonObject.getInt("budget");
                if (jsonObject.has("genres")) {
                    JSONArray jGenres = jsonObject.getJSONArray("genres");
                    genres = new Genre[jGenres.length()];
                    for (int i = 0; i < jGenres.length(); i++) {
                        genres[i] = new Genre.Builder().withJSONObject(jGenres.getJSONObject(i)).build();
                    }
                }
                if (jsonObject.has("genre_ids")) {
                    List<Genre> genresList = Application.getMovieGenreList();
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
                if (jsonObject.has("imdb_id"))
                    imdbId = jsonObject.getString("imdb_id");
                if (jsonObject.has("original_language"))
                    originalLanguage = jsonObject.getString("original_language");
                if (jsonObject.has("original_title"))
                    originalTitle = jsonObject.getString("original_title");
                if (jsonObject.has("overview") && !jsonObject.isNull("overview"))
                    overview = jsonObject.getString("overview");
                if (jsonObject.has("popularity"))
                    popularity = jsonObject.getDouble("popularity");
                if (jsonObject.has("poster_path"))
                    posterPath = jsonObject.getString("poster_path");
                if (jsonObject.has("production_companies")) {
                    JSONArray jCompanies = jsonObject.getJSONArray("production_companies");
                    productionCompanies = new Company[jCompanies.length()];
                    for (int i = 0; i < jCompanies.length(); i++) {
                        productionCompanies[i] = new Company.Builder().withJSONObject(jCompanies.getJSONObject(i)).build();
                    }
                }
                if (jsonObject.has("production_countries")) {
                    JSONArray jCountries = jsonObject.getJSONArray("production_countries");
                    productionCountries = new Country[jCountries.length()];
                    for (int i = 0; i <jCountries.length(); i++) {
                        productionCountries[i] = new Country.Builder().withJSONObject(jCountries.getJSONObject(i)).build();
                    }
                }
                if (jsonObject.has("release_date"))
                    releaseDate = jsonObject.getString("release_date");
                if (jsonObject.has("revenue"))
                    revenue = jsonObject.getInt("revenue");
                if (jsonObject.has("runtime") && !jsonObject.isNull("runtime"))
                    runtime = jsonObject.getInt("runtime");
                if (jsonObject.has("spoken_languages")) {
                    JSONArray jLanguages = jsonObject.getJSONArray("spoken_languages");
                    spokenLanguages = new Language[jLanguages.length()];
                    for (int i = 0; i < jLanguages.length(); i++) {
                        spokenLanguages[i] = new Language.Builder().withJSONObject(jLanguages.getJSONObject(i)).build();
                    }
                }
                if (jsonObject.has("status"))
                    status = jsonObject.getString("status");
                if (jsonObject.has("tagline") && !jsonObject.isNull("tagline"))
                    tagline = jsonObject.getString("tagline");
                if (jsonObject.has("title"))
                    title = jsonObject.getString("title");
                if (jsonObject.has("video"))
                    video = jsonObject.getBoolean("video");
                if (jsonObject.has("vote_average"))
                    voteAverage = jsonObject.getDouble("vote_average");
                if (jsonObject.has("vote_count"))
                    voteCount = jsonObject.getInt("vote_count");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return this;
        }

        public Builder withCastCredit(CastCredit castCredit) {
            adult = castCredit.isAdult();
            id = castCredit.getId();
            originalTitle = castCredit.getOriginalTitle();
            posterPath = castCredit.getPosterPath();
            releaseDate = castCredit.getReleaseDate();
            title = castCredit.getTitle();
            return this;
        }

        public Builder withCrewCredit(CrewCredit crewCredit) {
            adult = crewCredit.isAdult();
            id = crewCredit.getId();
            originalTitle = crewCredit.getOriginalTitle();
            posterPath = crewCredit.getPosterPath();
            releaseDate = crewCredit.getReleaseDate();
            title = crewCredit.getTitle();
            return this;
        }

        public Movie build() {
            Movie movie = new Movie();
            movie.adult = adult;
            movie.backdropPath = backdropPath;
            movie.belongsToCollection = belongsToCollection;
            movie.budget = budget;
            movie.genres = genres;
            movie.homepage = homepage;
            movie.id = id;
            movie.imdbId = imdbId;
            movie.originalLanguage = originalLanguage;
            movie.originalTitle = originalTitle;
            movie.overview = overview;
            movie.popularity = popularity;
            movie.posterPath = posterPath;
            movie.productionCompanies = productionCompanies;
            movie.productionCountries = productionCountries;
            movie.releaseDate = releaseDate;
            movie.revenue = revenue;
            movie.runtime = runtime;
            movie.spokenLanguages = spokenLanguages;
            movie.status = status;
            movie.tagline = tagline;
            movie.title = title;
            movie.video = video;
            movie.voteAverage = voteAverage;
            movie.voteCount = voteCount;

            return movie;
        }
    }
}
