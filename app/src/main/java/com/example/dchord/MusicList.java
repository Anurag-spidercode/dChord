package com.example.dchord;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.dchord.search;

import java.util.ArrayList;
import java.util.List;

public class MusicList extends Fragment {

    public MusicList() {
        // empty
    }

    ImageView play, pause, previous, next;
    private RecyclerView recyclerView;
    private adapter adapter;
    private List<data> list = new ArrayList<>();

    Context context;
    Singleton singleton = Singleton.getInstance();

    TextView title, artist;
    ConstraintLayout constraintLayout, player;

    SearchView search;

    private final BroadcastReceiver playbackReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("ACTION_PLAY_PAUSE".equals(action)) {
                play.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
            } else if ("ACTION_PAUSE_PAUSE".equals(action)) {
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.INVISIBLE);
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.inflate(R.layout.fragment_music_list,container,false);
        recyclerView = view.findViewById(R.id.recyclerview2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list = MusicFetch.fetchAllSongs(requireContext().getContentResolver());
        list.sort((song1, song2) -> song1.getTitle().compareToIgnoreCase(song2.getTitle()));

        adapter = new adapter(list, requireContext());
        recyclerView.setAdapter(adapter);
        title = view.findViewById(R.id.libraryTitle);
        artist = view.findViewById(R.id.libraryArtist);
        player = view.findViewById(R.id.constraintLayout2);

        search = view.findViewById(R.id.search);

        play = view.findViewById(R.id.playlist);
        pause = view.findViewById(R.id.pauselist);
        previous = view.findViewById(R.id.previouslist);
        next = view.findViewById(R.id.nextlist);

        Singleton singleton = Singleton.getInstance();
        constraintLayout = view.findViewById(R.id.constraintLayout2);
        if(singleton.isMusicStart()){
            constraintLayout.setVisibility(View.VISIBLE);
            title.setText(singleton.getTitle());
            artist.setText(singleton.getArtist());


            singleton.getInstance().getLiveTitle().observe(getViewLifecycleOwner(), newTitle -> {
                title.setText(newTitle);
            });

            singleton.getInstance().getLiveArtist().observe(getViewLifecycleOwner(), newArtist -> {
                artist.setText(newArtist);
            });

        }
        else{
            constraintLayout.setVisibility(View.GONE);
        }

        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    MainActivity swap = (MainActivity) v.getContext();
                    swap.fragmentswap(new search());
                }
            }
        });


        player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity swap = (MainActivity) v.getContext();
                swap.fragmentswap(new playsonglist());
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
                message("start");
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.INVISIBLE);
                message("pause");
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
                message("next");
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
                message("previous");
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if(singleton.isPlaying()){
            play.setVisibility(View.INVISIBLE);
            pause.setVisibility(View.VISIBLE);
        }
        else
        {
            play.setVisibility(View.VISIBLE);
            pause.setVisibility(View.INVISIBLE);
        }

        // for notification

        IntentFilter filter = new IntentFilter();
        filter.addAction("ACTION_PLAY_PAUSE");
        filter.addAction("ACTION_PAUSE_PAUSE");
        requireContext().registerReceiver(playbackReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireContext().unregisterReceiver(playbackReceiver);
    }
    private void message(String action){
        Intent intent = new Intent(context, foregroundservice.class);
        intent.putExtra("action", action);
        context.startService(intent);
    }
}