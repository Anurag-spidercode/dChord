package com.example.dchord;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class search extends Fragment {
    private RecyclerView recyclerView;
    private adapter adapter;
    private List<data> fullList = new ArrayList<>();
    private List<data> filteredList = new ArrayList<>();
    private SearchView search;

    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.recyclerview2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch all songs and initialize full and filtered lists
        fullList = MusicFetch.fetchAllSongs(requireContext().getContentResolver());
        filteredList = new ArrayList<>(fullList);

        // Set up the adapter
        adapter = new adapter(filteredList, requireContext());
        recyclerView.setAdapter(adapter);

        // Initially hide RecyclerView
        recyclerView.setVisibility(View.GONE);

        search = view.findViewById(R.id.search);

        // Set up SearchView to auto-focus and show keyboard
        search.setIconified(false);
        search.requestFocus();
        showKeyboard(search);

        // Set up search listener
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // No action needed when submit is pressed
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter songs based on search query and update RecyclerView visibility
                filterSongs(newText);
                return true;
            }
        });

        return view;
    }

    private void filterSongs(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            // Hide RecyclerView if no query is entered
            recyclerView.setVisibility(View.GONE);
        } else {
            // Show RecyclerView when user starts typing
            recyclerView.setVisibility(View.VISIBLE);

            String lowerQuery = query.toLowerCase();
            for (data song : fullList) {
                // Check if the title or artist contains the search query (case-insensitive)
                if (song.getTitle().toLowerCase().contains(lowerQuery) || song.getArtist().toLowerCase().contains(lowerQuery)) {
                    filteredList.add(song);
                }
            }
        }
        adapter.notifyDataSetChanged(); // Update the RecyclerView with filtered data
    }

    private void showKeyboard(View view) {
        // Make the keyboard appear automatically when the fragment is loaded
        view.post(() -> {
            InputMethodManager imm = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }
}
