package com.arman.moviedb;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by arman on 07/03/17.
 */

public class Review implements Parcelable {

    private String id;
    private String author;
    private String content;
    private String url;

    public Review() {}

    public static final Parcelable.Creator<Review> CREATOR = new
        Parcelable.Creator<Review>() {
            public Review createFromParcel(Parcel in) {
                return new Review(in);
            }

            public Review[] newArray(int size) {
                return new Review[size];
            }
        };

    private Review(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(author);
        out.writeString(content);
        out.writeString(url);
    }

    public void readFromParcel(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
