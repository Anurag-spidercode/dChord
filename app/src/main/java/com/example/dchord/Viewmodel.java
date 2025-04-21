package com.example.dchord;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Viewmodel extends ViewModel {
    private MutableLiveData<String> songTitle = new MutableLiveData<>();
    private MutableLiveData<String> songArtist = new MutableLiveData<>();

    public LiveData<String> getSongTitle() {
        return songTitle;
    }

    public LiveData<String> getSongArtist() {
        return songArtist;
    }

    public void setSongTitle(String title) {
        songTitle.setValue(title);
    }

    public void setSongArtist(String artist) {
        songArtist.setValue(artist);
    }
}
