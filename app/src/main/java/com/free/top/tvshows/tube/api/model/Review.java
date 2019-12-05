package com.free.top.tvshows.tube.api.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Review {

    private String id;
    private String author;
    private String content;
    private String url;

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public static class Builder {

        private String id;
        private String author;
        private String content;
        private String url;

        public Builder withJSONObject(JSONObject jsonObject) {
            try {
                if (jsonObject.has("id"))
                    id = jsonObject.getString("id");
                if (jsonObject.has("author"))
                    author = jsonObject.getString("author");
                if (jsonObject.has("content"))
                    content = jsonObject.getString("content");
                if (jsonObject.has("url"))
                    url = jsonObject.getString("url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return this;
        }

        public Review build() {
            Review review = new Review();
            review.id = id;
            review.author = author;
            review.content = content;
            review.url = url;
            return review;
        }

    }
}
