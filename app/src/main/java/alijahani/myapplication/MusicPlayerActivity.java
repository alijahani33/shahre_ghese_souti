// MusicPlayerActivity.java
package alijahani.myapplication;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MusicPlayerActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private Button btnPlayPause;
    private Handler uiUpdateHandler;
    private ImageView ivAlbumArt;  // Add this line


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        // Initialize MediaPlayer, SeekBar, TextViews, and Button
        mediaPlayer = new MediaPlayer();
        seekBar = findViewById(R.id.seekBar);
        tvStartTime = findViewById(R.id.tvStartTime);
        tvEndTime = findViewById(R.id.tvEndTime);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        ivAlbumArt = findViewById(R.id.ivAlbumArt);
        uiUpdateHandler = new Handler(getMainLooper());

        // Get audioResId from the intent
        int audioResId = getIntent().getIntExtra("audioResId", 0);
        int imageResId = getIntent().getIntExtra("imageResId", R.drawable.ic_launcher_foreground);


        if (audioResId != 0) {
            startMediaPlayer(audioResId);
        }
        if (audioResId != 0) {
            startMediaPlayer(audioResId);
            ivAlbumArt.setImageResource(imageResId);
        }

        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                    updateSeekBar();
                }
                updatePlayPauseButton();
            }
        });
    }

    private void startMediaPlayer(int audioResId) {
        try {
            mediaPlayer.reset();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), audioResId);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    updateSeekBar();
                    updatePlayPauseButton();
                    updateEndTime();
                }
            });

            seekBar.setMax(mediaPlayer.getDuration());
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mediaPlayer.seekTo(progress);
                        updateStartTime();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateSeekBar() {
        uiUpdateHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    updateStartTime();
                    updateSeekBar();
                }
            }
        }, 100);
    }

    private void updateStartTime() {
        tvStartTime.setText(getTimeString(mediaPlayer.getCurrentPosition()));
    }

    private void updateEndTime() {
        tvEndTime.setText(getTimeString(mediaPlayer.getDuration()));
    }

    private void updatePlayPauseButton() {
        if (mediaPlayer.isPlaying()) {
            btnPlayPause.setText("Pause");
        } else {
            btnPlayPause.setText("Play");
        }
    }

    private String getTimeString(int milliseconds) {
        int seconds = milliseconds / 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
