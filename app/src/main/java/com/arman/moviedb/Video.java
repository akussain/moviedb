package com.arman.moviedb;

import android.os.Parcel;
import android.os.Parcelable;

import com.arman.moviedb.utilities.NetworkUtils;

public class Video implements Parcelable {

    private String id;
    private String name;
    private String site;
    private int size;
    private String type;
    private String videoId;
    private String videoKey;

    public Video() {}

    public static final Parcelable.Creator<Video> CREATOR = new
        Parcelable.Creator<Video>() {
            public Video createFromParcel(Parcel in) {
                return new Video(in);
            }

            public Video[] newArray(int size) {
                return new Video[size];
            }
        };

    private Video(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(name);
        out.writeString(site);
        out.writeInt(size);
        out.writeString(type);
        out.writeString(videoId);
        out.writeString(videoKey);
    }

    public void readFromParcel(Parcel in) {
        id = in.readString();
        name = in.readString();
        site = in.readString();
        size = in.readInt();
        type = in.readString();
        videoId = in.readString();
        videoKey = in.readString();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
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

    public String getVideoKey() {
        return videoKey;
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

    public static String getVideoUrl(Video video) {
        return String.format(NetworkUtils.YOUTUBE_VIDEO_URL, video.getVideoKey());
    }

    public static String getThumbnailUrl(Video video) {
        return String.format(NetworkUtils.YOUTUBE_THUMBNAIL_URL, video.getVideoKey());
    }
}