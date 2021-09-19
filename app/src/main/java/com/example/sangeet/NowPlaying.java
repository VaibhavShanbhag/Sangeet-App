package com.example.sangeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class NowPlaying extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private TextView textView, start, end;
    private Button prev, play, next;
    private SeekBar seekBar;
    private ArrayList<File> songs;
    private MediaPlayer mediaPlayer;
    private String songText;
    private int position;
    private int currPos;
    Thread updateSeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textView = findViewById(R.id.songDisplay);
        prev = findViewById(R.id.previous);
        play = findViewById(R.id.play);
        next = findViewById(R.id.next);
        seekBar = findViewById(R.id.seekBar);
        start = findViewById(R.id.start);
        end = findViewById(R.id.end);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songList");
        songText = intent.getStringExtra("currentSong");
        textView.setText(songText + ".mp3");
        textView.setSelected(true);
        position = intent.getIntExtra("position", 0);

        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();
        end.setText(convertTime(mediaPlayer.getDuration()));

        updateSeek = new Thread() {
            @Override
            public void run() {
                currPos = 0;
                try {
                    while (currPos < mediaPlayer.getDuration()) {
                        sleep(600);
                        currPos = mediaPlayer.getCurrentPosition();
                        start.setText(convertTime(currPos));
                        seekBar.setProgress(currPos);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        updateSeek.start();
        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    play.setBackgroundResource(R.drawable.pause);
                } else {
                    mediaPlayer.start();
                    play.setBackgroundResource(R.drawable.play);
                }

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                next.performClick();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();

                if (position != songs.size() - 1) {
                    position += 1;

                } else {
                    position = 0;
                }

                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                songText = songs.get(position).getName();
                textView.setText(songText);
                end.setText(convertTime(mediaPlayer.getDuration()));
                seekBar.setMax(mediaPlayer.getDuration());
                currPos = 0;
                play.setBackgroundResource(R.drawable.play);
                mediaPlayer.start();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();

                if (position != 0) {
                    position -= 1;

                } else {
                    position = songs.size() - 1;
                }

                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                songText = songs.get(position).getName();
                textView.setText(songText);
                end.setText(convertTime(mediaPlayer.getDuration()));
                seekBar.setMax(mediaPlayer.getDuration());
                currPos = 0;
                play.setBackgroundResource(R.drawable.play);
                mediaPlayer.start();
            }
        });

    }

    public String convertTime(int duration)
    {
        String time = "";
        int min = duration/1000/60;
        int sec = duration/1000%60;
        time = time + min + ":";

        if (sec < 10)
        {
            time += "0";
        }
        time += sec;

        return time;
    }
}