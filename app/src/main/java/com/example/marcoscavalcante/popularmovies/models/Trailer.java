package com.example.marcoscavalcante.popularmovies.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by marcoscavalcante on 19/03/2018.
 */

public class Trailer
{
    private String id;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;
    private JSONObject trailerJson;

    public Trailer(JSONObject trailerJson) throws JSONException
    {
        this.id = trailerJson.getString("id");
        this.key = trailerJson.getString("key");
        this.name = trailerJson.getString("name");
        this.site = trailerJson.getString("site");
        this.size = trailerJson.getInt("size");
        this.type = trailerJson.getString("type");

        this.trailerJson = trailerJson;
    }

    public Trailer(String id, String key, String name, String site, int size, String type, JSONObject trailerJson)
    {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
        this.trailerJson = trailerJson;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSONObject getTrailerJson() {
        return trailerJson;
    }

    public void setTrailerJson(JSONObject trailerJson) {
        this.trailerJson = trailerJson;
    }
}
