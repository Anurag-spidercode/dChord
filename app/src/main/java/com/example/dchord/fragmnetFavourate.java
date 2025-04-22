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

public class fragmnetFavourate extends Fragment {

    private RecyclerView recyclerView;
    private favoriteAdapter historyAdapter;
    private favoriteSong fav;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragmnet_favourate, container, false);
        recyclerView = view.findViewById(R.id.recyclerview2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

         fav = new favoriteSong(getContext());

        List<HistorySong> songList = fav.getAllFavorites();

        historyAdapter = new favoriteAdapter(songList, getContext());
        recyclerView.setAdapter(historyAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        favoriteSong favDb = new favoriteSong(requireContext());
        List<HistorySong> fresh = favDb.getAllFavorites();
        historyAdapter.updateList(fresh);
    }

}