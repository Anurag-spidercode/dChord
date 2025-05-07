package com.example.dchord;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class Singleton {
    private static Singleton instance; // Static instance
    private String title2 = "";
    private String artist2 = "";
    private int position2 = 0;

    private boolean isPlaying = false;
    private boolean isMusicStart = false;
    private long duration;

    private String songpath = "";

    private byte[] image;

    private final MutableLiveData<String> title = new MutableLiveData<>();
    private final MutableLiveData<String> artist = new MutableLiveData<>();
    private final MutableLiveData<String> livePath = new MutableLiveData<>();

    private static List<data> songList = new ArrayList<>();

    private Singleton() {
    } // Private constructor to prevent multiple instances

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public MutableLiveData<String> getLiveTitle() {
        return title;
    }

    public MutableLiveData<String> getLiveArtist() {
        return artist;
    }

    public void setLiveTitle(String newTitle) {
        title.postValue(newTitle);
    }

    public void setLiveArtist(String newArtist) {
        artist.postValue(newArtist);
    }

    public void setLivePath(String path) {
        livePath.postValue(path);
    }

    public LiveData<String> getLivePath() {
        return livePath;
    }

    public byte[] getImage(){
        return image;
    }

    public void setImage(byte[] art){
        this.image = art;
    }

    public boolean isMusicStart() {
        return isMusicStart;
    }

    public void setMusicStart(boolean musicStart) {
        isMusicStart = musicStart;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
    public void setPlaying(Boolean state){
        isPlaying = state;
    }

    public int getPosition(){
        return position2;
    }

    public void setPosition(int position2){
        this.position2 = position2;
    }

    public static List<data> getSongList() {
        return songList;
    }

    public static void setSongList(List<data> songList) {
        Singleton.songList = songList;
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


    public void setMaxduration(long duration){
        this.duration = duration;
    }

    public long getMaxduration(){
        return duration;
    }

    public  void setSongpath(String path){
        this.songpath = path;
    }

    public  String getSongpath(){
        return songpath;
    }
}
