package com.example.dchord;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public class history extends Fragment {

    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private PlaybackSongs playbackSongs;

    public history(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = view.findViewById(R.id.recyclerview2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        playbackSongs = new PlaybackSongs(getContext());

        List<HistorySong> songList = playbackSongs.getAllSongs();

        historyAdapter = new HistoryAdapter(songList, getContext());
        recyclerView.setAdapter(historyAdapter);
        return view;
    }
}