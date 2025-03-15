import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private final List<Data> musicList;

    // Constructor to initialize data
    public MusicAdapter(List<Data> musicList) {
        this.musicList = musicList;
    }

    // ViewHolder class to hold item views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, artist, duration;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.songTitle);
            artist = itemView.findViewById(R.id.songArtist);
            duration = itemView.findViewById(R.id.songDuration);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Data song = musicList.get(position);
        holder.title.setText(song.getTitle());
        holder.artist.setText(song.getArtist());
        holder.duration.setText(formatDuration(song.getDuration()));
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    // Format duration from milliseconds to mm:ss
    private String formatDuration(long durationMs) {
        long minutes = (durationMs / 1000) / 60;
        long seconds = (durationMs / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
