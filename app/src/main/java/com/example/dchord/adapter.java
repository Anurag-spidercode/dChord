package com.example.dchord;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {

    public static final String filepathurl = "com.example.dchord.playmusic";
    public static final String filepathurl2 = "com.example.dchord.playmusic2";
    public static final String filepathurl3 = "com.example.dchord.playmusic3";

    Singleton singleton = Singleton.getInstance();
    public String title;
    public String artist;
    public String filePath;
    long duration;
    int index = 0;
    private List<data> list;
    private Context context;
    ProgressBar progressBar2;

//    private Viewmodel viewmodel;

    public adapter(List<data> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter.ViewHolder holder, int position) {
        PlaybackSongs history = new PlaybackSongs(context);
        data song = list.get(position);
        int pos = holder.getAdapterPosition();
        title = song.getTitle();
        artist = song.getArtist();
        filePath = song.getFilePath();
        duration = song.getDuration();
        String titlenamemodified = "";
        String artistnamemodified = "";
        if(title.length() > 19)
        {
            titlenamemodified = title.substring(0, 19) + "...";
            holder.title.setText(titlenamemodified);
        }
        else
        {
            holder.title.setText(title);
        }

        if(artist.length() > 35)
        {
            artistnamemodified = artist.substring(0, 35) + "...";
            holder.artist.setText(artistnamemodified);
        }
        else
        {
            holder.artist.setText(artist);
        }

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(song == null){
                    return;
                }

//                viewmodel.setCurrentIndex(pos);
                history.insertSong(song.getTitle(), song.getArtist(), song.getFilePath());//inserting to database;
                singleton.setSongList(list);
                index = holder.getAdapterPosition();
                singleton.setPosition(index);
                Intent intent = new Intent(v.getContext(), foregroundservice.class);
                intent.putExtra(filepathurl, song.getTitle());
                intent.putExtra(filepathurl2, song.getArtist());
                intent.putExtra(filepathurl3, song.getFilePath());
//                intent.setAction("play");
                ContextCompat.startForegroundService(v.getContext(), intent);

                if (v.getContext() instanceof MainActivity) {
                    MainActivity swap = (MainActivity) v.getContext();
                    swap.fragmentswap(new playsonglist());
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateSongs(List<data> newSongs){
        this.list.clear();
        this.list.addAll(newSongs);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, artist;
        public ConstraintLayout constraintLayout;
        public ViewHolder(View itemview) {
            super(itemview);
            title = itemview.findViewById(R.id.titlename);
            artist = itemview.findViewById(R.id.artist);
            constraintLayout = itemview.findViewById(R.id.layout1);
        }
    }
}
