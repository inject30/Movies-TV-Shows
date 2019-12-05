package com.free.top.tvshows.tube.api.model;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Language implements Serializable{

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
                if (jsonObject.has("iso_639_1"))
                    code = jsonObject.getString("iso_639_1");
                if (jsonObject.has("name"))
                    name = jsonObject.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return this;
        }

        public Language build() {
            Language language = new Language();
            language.code = code;
            language.name = name;
            return language;
        }
    }
}
