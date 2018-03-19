package com.example.marcoscavalcante.popularmovies.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by marcoscavalcante on 19/03/2018.
 */

public class Review
{
    private String author;
    private String content;
    private String id;
    private String url;
    private JSONObject reviewJson;


    public Review(JSONObject reviewJson) throws JSONException
    {
        this.author = reviewJson.getString("author");
        this.content = reviewJson.getString("content");
        this.id = reviewJson.getString("id");
        this.url = reviewJson.getString("url");

        this.reviewJson = reviewJson;
    }

    public Review(String author, String content, String id, String url, JSONObject reviewJson) {
        this.author = author;
        this.content = content;
        this.id = id;
        this.url = url;
        this.reviewJson = reviewJson;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public JSONObject getReviewJson() {
        return reviewJson;
    }

    public void setReviewJson(JSONObject reviewJson) {
        this.reviewJson = reviewJson;
    }
}
