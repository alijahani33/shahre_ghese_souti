package alijahani.myapplication;

public class AudioItem {
    public final int audioResId;
    public final int imageResId;
    public final String title;

    public AudioItem(int audioResId, int imageResId, String title) {
        this.audioResId = audioResId;
        this.imageResId = imageResId;
        this.title = title;
    }
}