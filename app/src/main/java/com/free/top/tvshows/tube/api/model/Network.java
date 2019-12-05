package com.free.top.tvshows.tube.api.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Network implements Serializable {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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

        public Network build() {
            Network network = new Network();
            network.id = id;
            network.name = name;
            return network;
        }
    }
}
