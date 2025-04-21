package com.example.dchord;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class playsonglist extends Fragment {
    Singleton singleton = Singleton.getInstance();
    TextView title , artist;

    private List<data> list = new ArrayList<>();
    ImageView previous, play, pause, next;
    Context context;

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


    public playsonglist(){
        //empty
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        View view = inflater.inflate(R.layout.fragment_playsonglist, container, false);
        title = view.findViewById(R.id.titlelist);
        artist = view.findViewById(R.id.artistlist);

        previous = view.findViewById(R.id.previousplay);
        play = view.findViewById(R.id.playplay);
        pause = view.findViewById(R.id.pauseplay);
        next = view.findViewById(R.id.nextplay);

        title.setText(singleton.getTitle());
        artist.setText(singleton.getArtist());

        singleton.getInstance().getLiveTitle().observe(getViewLifecycleOwner(), newTitle -> {
            title.setText(newTitle);
        });

        singleton.getInstance().getLiveArtist().observe(getViewLifecycleOwner(), newArtist -> {
            artist.setText(newArtist);
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