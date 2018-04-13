package com.example.marcoscavalcante.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by marcoscavalcante on 19/03/2018.
 */

public class Trailer implements Parcelable
{
    private String id;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;
    private JSONObject trailerJson;

    protected Trailer(Parcel in)
    {
        id = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        size = in.readInt();
        type = in.readString();
        trailerJson = getTrailerJson();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>()
    {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(id);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeInt(size);
        dest.writeString(type);
    }

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

    public JSONObject getTrailerJson()
    {
        if(trailerJson == null)
        {
            try {
                trailerJson = new JSONObject();
                trailerJson.put("id", id );
                trailerJson.put("key", key);
                trailerJson.put("name", name);
                trailerJson.put("site", site);
                trailerJson.put("size", size);
                trailerJson.put("type", type);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return trailerJson;
    }

    public void setTrailerJson(JSONObject trailerJson) {
        this.trailerJson = trailerJson;
    }


}
