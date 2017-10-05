package com.arman.moviedb;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private Integer id;
    private String title;
    private String releaseDate;
    private String userRating;
    private String posterPath;
    private String overview;
    private boolean favorite;

    public Movie(Integer mId, String mTitle, String mReleaseDate, String mUserRating, String mPoster, String mOverview, boolean mFavorite) {
        id = mId;
        title = mTitle;
        releaseDate = mReleaseDate;
        userRating = mUserRating;
        posterPath = mPoster;
        overview = mOverview;
        favorite = mFavorite;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new
        Parcelable.Creator<Movie>() {
            public Movie createFromParcel(Parcel in) {
                return new Movie(in);
            }

            public Movie[] newArray(int size) {
                return new Movie[size];
            }
        };

    public Movie() {}

    private Movie(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(title);
        out.writeString(releaseDate);
        out.writeString(userRating);
        out.writeString(posterPath);
        out.writeString(overview);
        out.writeByte((byte) (favorite ? 1 : 0));
    }

    public void readFromParcel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        releaseDate = in.readString();
        userRating = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        favorite = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}