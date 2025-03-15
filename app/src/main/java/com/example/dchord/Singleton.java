package com.example.dchord;

public class Singleton {
    private static Singleton instance; // Static instance
    private String title2 = "";
    private String artist2 = "";
    private boolean update;
    private long duration;
    private long progress;

    private Singleton() {
    } // Private constructor to prevent multiple instances

    public static Singleton getInstance() { // Static method to get instance
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public void setTitle(String title) { // Follow camelCase naming conventions
        this.title2 = title;
    }

    public String getTitle() {
        return title2;
    }

    public void setArtist(String artist) {
        this.artist2 = artist;
    }

    public String getArtist() {
        return artist2;
    }

    public void setUpdate(boolean update){
        this.update = update;
    }

    public boolean getUpdate() {
        return update;
    }

    public void setMaxduration(long duration){
        this.duration = duration;
    }

    public long getMaxduration(){
        return duration;
    }

    public void setProgress(long progress){
        this.progress = progress;
    }

    public long getProgress(){
        return progress;
    }
}
