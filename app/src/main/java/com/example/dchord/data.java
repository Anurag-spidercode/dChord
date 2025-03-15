package com.example.dchord;

import android.os.Parcel;
import android.os.Parcelable;

public class data implements Parcelable{
    private final String title;
    private final String artist;
    private final long duration;
    private final String filePath;

    public data(String title, String artist, String filePath, long duration){
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.filePath= filePath;
    }

    protected data(Parcel p) {
        title = p.readString();
        artist = p.readString();
        duration = p.readLong();
        filePath = p.readString();
    }

    public static final Creator<data> CREATOR = new Creator<data>() {
        @Override
        public data createFromParcel(Parcel in) {
            return new data(in);
        }

        @Override
        public data[] newArray(int size) {
            return new data[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeLong(duration);
        dest.writeString(filePath);
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public long getDuration() {
        return duration;
    }

    public String getFilePath() {
        return filePath;
    }
}

