package com.free.top.tvshows.tube.api.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Genre extends BaseModel implements Serializable {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int hashCode() {
        return id;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Genre) {
            Genre genre = (Genre) obj;
            return genre.id == id;
        } else
            return false;
    }

    public static class Builder {

        private int id;
        private String name;

        public Builder withJSONObject(JSONObject jsonObject) {
            try {
                if (jsonObject.has("id"))
                    id = jsonObject.getInt("id");
                if (jsonObject.has("name"))
                    name = jsonObject.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return this;
        }

        public Genre build() {
            Genre genre = new Genre();
            genre.id = id;
            genre.name = name;
            return genre;
        }

    }
}
