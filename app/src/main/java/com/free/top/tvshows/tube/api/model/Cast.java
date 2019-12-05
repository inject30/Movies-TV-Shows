package com.free.top.tvshows.tube.api.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Cast implements Serializable {

    private int castId;
    private String character;
    private String creditId;
    private int id;
    private String name;
    private int order;
    private String profilePath;

    public int getCastId() {
        return castId;
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

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public static class Builder {

        private int castId;
        private String character;
        private String creditId;
        private int id;
        private String name;
        private int order;
        private String profilePath;

        public Builder withJSONObject(JSONObject jsonObject) {
            try {
                if (jsonObject.has("cast_id"))
                    castId = jsonObject.getInt("cast_id");
                if (jsonObject.has("character"))
                    character = jsonObject.getString("character");
                if (jsonObject.has("credit_id"))
                    creditId = jsonObject.getString("credit_id");
                if (jsonObject.has("id"))
                    id = jsonObject.getInt("id");
                if (jsonObject.has("name"))
                    name = jsonObject.getString("name");
                if (jsonObject.has("order"))
                    order = jsonObject.getInt("order");
                if (jsonObject.has("profile_path") && !jsonObject.isNull("profile_path"))
                    profilePath = jsonObject.getString("profile_path");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return this;
        }

        public Cast build() {
            Cast cast = new Cast();
            cast.castId = castId;
            cast.character = character;
            cast.creditId = creditId;
            cast.id = id;
            cast.name = name;
            cast.order = order;
            cast.profilePath = profilePath;
            return cast;
        }

    }
}
