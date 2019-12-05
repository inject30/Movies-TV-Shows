package com.free.top.tvshows.tube.api.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Video {

    private String id;
    private String language;
    private String country;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    public String getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public static class Builder {

        private String id;
        private String language;
        private String country;
        private String key;
        private String name;
        private String site;
        private int size;
        private String type;

        public Builder withJSONObject(JSONObject jsonObject) {
            try {
                if (jsonObject.has("id"))
                    id = jsonObject.getString("id");
                if (jsonObject.has("iso_639_1"))
                    language = jsonObject.getString("iso_639_1");
                if (jsonObject.has("iso_3166_1"))
                    country = jsonObject.getString("iso_3166_1");
                if (jsonObject.has("key"))
                    key = jsonObject.getString("key");
                if (jsonObject.has("name"))
                    name = jsonObject.getString("name");
                if (jsonObject.has("site"))
                    site = jsonObject.getString("site");
                if (jsonObject.has("size"))
                    size = jsonObject.getInt("size");
                if (jsonObject.has("type"))
                    type = jsonObject.getString("type");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return this;
        }

        public Video build() {
            Video video = new Video();
            video.id = id;
            video.language = language;
            video.country = country;
            video.key = key;
            video.name = name;
            video.site = site;
            video.size = size;
            video.type = type;
            return video;
        }
    }
}
