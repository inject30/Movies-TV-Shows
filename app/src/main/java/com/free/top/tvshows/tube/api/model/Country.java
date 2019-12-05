package com.free.top.tvshows.tube.api.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Country implements Serializable {

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static class Builder {

        private String code;
        private String name;

        public Builder withJSONObject(JSONObject jsonObject) {
            try {
                if (jsonObject.has("iso_3166_1"))
                    code = jsonObject.getString("iso_3166_1");
                if (jsonObject.has("name"))
                    name = jsonObject.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return this;
        }

        public Country build() {
            Country country = new Country();
            country.code = code;
            country.name = name;
            return country;
        }
    }
}
