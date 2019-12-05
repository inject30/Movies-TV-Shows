package com.free.top.tvshows.tube.api.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Person extends BaseModel implements Serializable {

    private boolean adult;
    private String[] alsoKnownAs;
    private String biography;
    private String birthday;
    private String deathday;
    private int gender;
    private String homepage;
    private int id;
    private String imdbId;
    private BaseModel[] knownFor;
    private String name;
    private String placeOfBirth;
    private double popularity;
    private String profilePath;

    private String facebookId;
    private String instagramId;
    private String twitterId;

    public boolean isAdult() {
        return adult;
    }

    public String[] getAlsoKnownAs() {
        return alsoKnownAs;
    }

    public String getBiography() {
        return biography;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public int getGender() {
        return gender;
    }

    public String getHomepage() {
        return homepage;
    }

    public int getId() {
        return id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public BaseModel[] getKnownFor() {
        return knownFor;
    }

    public void setKnownFor(BaseModel[] knownFor) {
        this.knownFor = knownFor;
    }

    public String getName() {
        return name;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public String getInstagramId() {
        return instagramId;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public static class Builder {

        private boolean adult;
        private String[] alsoKnownAs;
        private String biography;
        private String birthday;
        private String deathday;
        private int gender;
        private String homepage;
        private int id;
        private String imdbId;
        private BaseModel[] knownFor;
        private String name;
        private String placeOfBirth;
        private double popularity;
        private String profilePath;

        private String facebookId;
        private String instagramId;
        private String twitterId;

        public Builder withJSONObject(JSONObject jsonObject) {
            try {
                if (jsonObject.has("adult"))
                    adult = jsonObject.getBoolean("adult");
                if (jsonObject.has("also_known_as")) {
                    JSONArray jAlsoKnownAs = jsonObject.getJSONArray("also_known_as");
                    alsoKnownAs = new String[jAlsoKnownAs.length()];
                    for (int i = 0; i < jAlsoKnownAs.length(); i++) {
                        alsoKnownAs[i] = jAlsoKnownAs.getString(i);
                    }
                }
                if (jsonObject.has("biography") && !jsonObject.isNull("biography"))
                    biography = jsonObject.getString("biography");
                if (jsonObject.has("birthday") && !jsonObject.isNull("birthday"))
                    birthday = jsonObject.getString("birthday");
                if (jsonObject.has("deathday") && !jsonObject.isNull("deathday"))
                    deathday = jsonObject.getString("deathday");
                if (jsonObject.has("gender"))
                    gender = jsonObject.getInt("gender");
                if (jsonObject.has("homepage") && !jsonObject.isNull("homepage"))
                    homepage = jsonObject.getString("homepage");
                if (jsonObject.has("id"))
                    id = jsonObject.getInt("id");
                if (jsonObject.has("imdb_id"))
                    imdbId = jsonObject.getString("imdb_id");
                if (jsonObject.has("known_for")) {
                    JSONArray jKnownFor = jsonObject.getJSONArray("known_for");
                    knownFor = new BaseModel[jKnownFor.length()];
                    for (int i = 0; i < jKnownFor.length(); i++) {
                        JSONObject jObject = jKnownFor.getJSONObject(i);
                        String mediaType = jObject.getString("media_type");
                        if (mediaType.equals("movie"))
                            knownFor[i] = new Movie.Builder().withJSONObject(jObject).build();
                        else if (mediaType.equals("tv"))
                            knownFor[i] = new TVShow.Builder().withJSONObject(jObject).build();
                    }
                }
                if (jsonObject.has("name"))
                    name = jsonObject.getString("name");
                if (jsonObject.has("place_of_birth"))
                    placeOfBirth = jsonObject.getString("place_of_birth");
                if (jsonObject.has("popularity"))
                    popularity = jsonObject.getDouble("popularity");
                if (jsonObject.has("profile_path"))
                    profilePath = jsonObject.getString("profile_path");

                if (jsonObject.has("external_ids")) {
                    JSONObject jExternalIds = jsonObject.getJSONObject("external_ids");
                    if (jExternalIds.has("facebook_id") && !jExternalIds.isNull("facebook_id"))
                        facebookId = jExternalIds.getString("facebook_id");
                    if (jExternalIds.has("instagram_id") && !jExternalIds.isNull("instagram_id"))
                        instagramId = jExternalIds.getString("instagram_id");
                    if (jExternalIds.has("twitter_id") && !jExternalIds.isNull("twitter_id"))
                        twitterId = jExternalIds.getString("twitter_id");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return this;
        }

        public Builder withCast(Cast cast) {
            id = cast.getId();
            name = cast.getName();
            profilePath = cast.getProfilePath();
            return this;

        }

        public Person build() {
            Person person = new Person();
            person.adult = adult;
            person.alsoKnownAs = alsoKnownAs;
            person.biography = biography;
            person.birthday = birthday;
            person.deathday = deathday;
            person.gender  = gender;
            person.homepage = homepage;
            person.id = id;
            person.imdbId = imdbId;
            person.knownFor = knownFor;
            person.name = name;
            person.placeOfBirth = placeOfBirth;
            person.popularity = popularity;
            person.profilePath = profilePath;
            person.facebookId = facebookId;
            person.instagramId = instagramId;
            person.twitterId = twitterId;
            return person;
        }
    }
}
