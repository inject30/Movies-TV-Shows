package com.free.top.tvshows.tube.api.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Company implements Serializable {

    private String description;
    private String headquarters;
    private String homepage;
    private int id;
    private String name;

    public String getDescription() {
        return description;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public String getHomepage() {
        return homepage;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static class Builder {

        private String description;
        private String headquarters;
        private String homepage;
        private int id;
        private String name;

        public Builder withJSONObject(JSONObject jsonObject) {
            try {
                if (jsonObject.has("description") && !jsonObject.isNull("description"))
                    description = jsonObject.getString("description");
                if (jsonObject.has("headquarters"))
                    headquarters = jsonObject.getString("headquarters");
                if (jsonObject.has("homepage"))
                    homepage = jsonObject.getString("homepage");
                if (jsonObject.has("id"))
                    id = jsonObject.getInt("id");
                if (jsonObject.has("name"))
                    name = jsonObject.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return this;
        }

        public Company build() {
            Company company = new Company();
            company.description = description;
            company.headquarters = headquarters;
            company.homepage = homepage;
            company.id = id;
            company.name = name;
            return company;
        }

    }
}
