package alijahani.myapplication;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AudioItemAdapter extends RecyclerView.Adapter<AudioItemAdapter.AudioItemViewHolder> {

    private List<AudioItem> audioItems;
    private MediaPlayer mediaPlayer;
    private int playingPosition = -1;
    private Handler uiUpdateHandler;
    private OnAudioItemClickListener onAudioItemClickListener;
    private Context context;


    public AudioItemAdapter(List<AudioItem> audioItems, Context context) {
        this.audioItems = audioItems;
        this.mediaPlayer = new MediaPlayer();
        this.uiUpdateHandler = new Handler(context.getMainLooper());
        this.context = context;
    }
    private List<AudioItem> createAudioItems() {
        List<AudioItem> items = new ArrayList<>();
        items.add(new AudioItem(R.raw.alichi, R.mipmap.ic_launcher, "Audio 1"));
        items.add(new AudioItem(R.raw.ahang, R.mipmap.ic_launcher, "Audio 2"));
        // Add more items as needed
        return items;
    }

    public interface OnAudioItemClickListener {
        void onAudioItemClick(int position);
    }
    public AudioItem getAudioItem(int position) {
        return audioItems.get(position);
    }
    public void setOnAudioItemClickListener(OnAudioItemClickListener listener) {
        this.onAudioItemClickListener = listener;
    }


    void startMediaPlayer(int position) {
        try {
            mediaPlayer.reset();
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(audioItems.get(position).audioResId);
            if (afd != null) {
                mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();
                mediaPlayer.prepare();
                mediaPlayer.start();
                playingPosition = position;
                notifyDataSetChanged();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopMediaPlayer();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void stopMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
        }
    }

    @NonNull
    @Override
    public AudioItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell, parent, false);
        return new AudioItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioItemViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return audioItems.size();
    }

    class AudioItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivPlayPause;
        TextView tvIndex;
        TextView tvAudioFileName;
        ImageView ivAlbumArt;

        AudioItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPlayPause = itemView.findViewById(R.id.ivPlayPause);
            tvIndex = itemView.findViewById(R.id.tvIndex);
            tvAudioFileName = itemView.findViewById(R.id.tvAudioFileName);
            ivAlbumArt = itemView.findViewById(R.id.ivAlbumArt);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            AudioItem audioItem = audioItems.get(position);
            tvIndex.setText(String.valueOf(position + 1));
//            tvAudioFileName.setText(audioItem.audioFileName);
            ivAlbumArt.setImageResource(audioItem.imageResId);

            if (position == playingPosition) {
                ivPlayPause.setImageResource(mediaPlayer.isPlaying() ? R.drawable.ic_pause : R.drawable.ic_play_arrow);
            } else {
                ivPlayPause.setImageResource(R.drawable.ic_play_arrow);
            }
        }
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (onAudioItemClickListener != null) {
                onAudioItemClickListener.onAudioItemClick(position);
            }
        }


    }
}
