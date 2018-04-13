package com.example.marcoscavalcante.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by marcoscavalcante on 19/03/2018.
 */

public class Review implements Parcelable
{
    private String author;
    private String content;
    private String id;
    private String url;
    private JSONObject reviewJson;


    protected Review(Parcel in)
    {
        author = in.readString();
        content = in.readString();
        id = in.readString();
        url = in.readString();
        reviewJson = getReviewJson();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(id);
        dest.writeString(url);
    }


    public Review(JSONObject reviewJson) throws JSONException
    {
        this.author = reviewJson.getString("author");
        this.content = reviewJson.getString("content");
        this.id = reviewJson.getString("id");
        this.url = reviewJson.getString("url");

        this.reviewJson = reviewJson;
    }

    public Review(String author, String content, String id, String url, JSONObject reviewJson)
    {
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

    public JSONObject getReviewJson()
    {
        if(reviewJson == null)
        {
            try {
                reviewJson = new JSONObject();
                reviewJson.put("author", author );
                reviewJson.put("content", content);
                reviewJson.put("id", id);
                reviewJson.put("url", url);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return reviewJson;
    }

    public void setReviewJson(JSONObject reviewJson) {
        this.reviewJson = reviewJson;
    }
}
