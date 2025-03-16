package com.example.dchord;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class Viewmodel extends AndroidViewModel {
    private final MutableLiveData<List<data>> songlist = new MutableLiveData<>();

    private final MutableLiveData<String> songname = new MutableLiveData<>("");

    private final MutableLiveData<String> artist = new MutableLiveData<>("");

    public Viewmodel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<data>> getSongList(){  // getter method to observe data
        return songlist;
    }

    public void setSonglist(List<data> songs){      //setter method to update data
        if(songs != null) {
            songlist.postValue(songs);
        }
    }

    public LiveData<String> getsongName(){
        return songname;
    }

    public void setSongname(String name){
        if(name != null) {
            songname.postValue(name);
        }
    }

    public  LiveData<String> getArtist(){
        return artist;
    }

    public void setArtist(String artistname){
        if(artist != null){
            artist.postValue(artistname);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        songlist.postValue(null);
        songname.postValue("");
        artist.postValue("");
    }
}
