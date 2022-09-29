package com.rajeev.android.surilamusic;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PlayingMusic extends AppCompatActivity {

    TextView songName, currentTime, totalTime;
    SeekBar seekBar;
    ImageView previous, pause, next, play,music_image;
    ArrayList<ModelClass> songList;
    ModelClass currentSong;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_music);

        songName = findViewById(R.id.song_name);
        currentTime = findViewById(R.id.current_time);
        totalTime = findViewById(R.id.total_time);
        seekBar = findViewById(R.id.seek_bar);
        previous = findViewById(R.id.prevoiuc);
        pause = findViewById(R.id.pause);
        next = findViewById(R.id.next);
        music_image = findViewById(R.id.music_image);

        songName.setSelected(true);
        songList = (ArrayList<ModelClass>) getIntent().getSerializableExtra("LIST");
        setResourceWithMusic();
        PlayingMusic.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTime.setText(convertToMMSS(mediaPlayer.getCurrentPosition() + ""));

                    if (mediaPlayer.isPlaying()) {
                        pause.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                        music_image.setRotation(x++);
                    } else {
                        pause.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                        music_image.setRotation(0);
                    }
                }
                new Handler().postDelayed(this, 700);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    void setResourceWithMusic() {
        currentSong = songList.get(MyMediaPlayer.currentIndex);
        songName.setText(currentSong.getTitle());
        totalTime.setText(convertToMMSS(currentSong.getDuration()));

        pause.setOnClickListener(v -> playPause());
        next.setOnClickListener(v -> playNextSong());
        previous.setOnClickListener(v -> playPreviousMusic());

        playMusic();
    }

    private void playMusic() {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playNextSong() {

        if (MyMediaPlayer.currentIndex == songList.size() - 1) {
            return;
        }
        MyMediaPlayer.currentIndex += 1;
        mediaPlayer.reset();
        setResourceWithMusic();
    }

    private void playPreviousMusic() {

        if (MyMediaPlayer.currentIndex == 0) {
            return;
        }
        MyMediaPlayer.currentIndex += -1;
        mediaPlayer.reset();
        setResourceWithMusic();
    }

    private void playPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    public static String convertToMMSS(String duration) {
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
}