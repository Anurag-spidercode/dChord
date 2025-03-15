import android.os.Parcel;
import android.os.Parcelable;
public class data implements Parcelable{
    private final String musicId;
    private final String title;
    private final String artist;
    private final long duration;
    private final String filePath;
    private final long fileSize;
    private final String fileType;
    private final int playCount;
    private final boolean favourite;

    public data(String musicId, String title, String artist, String filePath, String fileType, long duration, long fileSize, int playCount, boolean favourite){
        this.musicId = musicId;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.filePath= filePath;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.playCount = playCount;
        this.favourite = favourite;
    }

    protected data(Parcel p) {
        musicId = p.readString();
        title = p.readString();
        artist = p.readString();
        duration = p.readLong();
        filePath = p.readString();
        fileSize = p.readLong();
        fileType = p.readString();
        playCount = p.readInt();
        favourite = p.readByte() != 0;
    }

    public static final Creator<data> CREATOR = new Creator<data>() {
        @Override
        public data createFromParcel(Parcel in) {
            return new data(in);
        }

        @Override
        public data[] newArray(int size) {
            return new data[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(musicId);
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeLong(duration);
        dest.writeString(filePath);
        dest.writeLong(fileSize);
        dest.writeString(fileType);
        dest.writeInt(playCount);
        dest.writeByte((byte) (favourite ? 1 : 0));
    }

    public String getMusicId() {
        return musicId;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public long getDuration() {
        return duration;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public int getPlayCount() {
        return playCount;
    }

    public boolean isFavourite() {
        return favourite;
    }
}
