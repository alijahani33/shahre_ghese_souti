// MainActivity.java
package alijahani.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AudioItemAdapter.OnAudioItemClickListener {

    private AudioItemAdapter audioItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        List<AudioItem> audioItems = createAudioItems(); // Add your audio items here

//        List<AudioItem> audioItems = createAudioItems(); // Add your audio items here
        audioItemAdapter = new AudioItemAdapter(audioItems, this);
        audioItemAdapter.setOnAudioItemClickListener(this);

        rv.setAdapter(audioItemAdapter);
    }

    private List<AudioItem> createAudioItems() {
        List<AudioItem> audioItems = new ArrayList<>();
        audioItems.add(new AudioItem(getResources().getIdentifier("alichi", "raw", getPackageName()), R.mipmap.ic_launcher, "Audio 1"));
        audioItems.add(new AudioItem(getResources().getIdentifier("ahang", "raw", getPackageName()), R.mipmap.ic_launcher, "Audio 2"));
        // Add more items as needed
        return audioItems;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioItemAdapter != null) {
            audioItemAdapter.stopMediaPlayer();
        }
    }

    @Override
    public void onAudioItemClick(int position) {
        alijahani.myapplication.AudioItem audioItem = audioItemAdapter.getAudioItem(position);
        Intent intent = new Intent(this, MusicPlayerActivity.class);
        intent.putExtra("audioResId", audioItem.audioResId);
        intent.putExtra("imageResId", audioItem.imageResId);
        startActivity(intent);
    }
}
