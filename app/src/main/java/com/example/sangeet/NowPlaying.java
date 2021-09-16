package com.example.sangeet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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

    private TextView textView, start, end;
    private Button prev, play, next;
    private SeekBar seekBar;
    private ArrayList<File> songs;
    private MediaPlayer mediaPlayer;
    private String songText;
    private int position;
    Thread updateSeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
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
        textView.setText(songText);
        textView.setSelected(true);
        position = intent.getIntExtra("position", 0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        start.setText("00:00");
        end.setText(String.valueOf(mediaPlayer.getDuration()));


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

        updateSeek = new Thread(){
            @Override
            public void run() {
                int currPos = 0;
                try {
                    while(currPos < mediaPlayer.getDuration())
                    {
                        currPos = mediaPlayer.getCurrentPosition();
                        start.setText(String.valueOf(currPos));
                        seekBar.setProgress(currPos);
                        sleep(800);
                    }
                }

                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };

        updateSeek.start();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    play.setBackgroundResource(R.drawable.pause);
                }

                else{
                    mediaPlayer.start();
                    play.setBackgroundResource(R.drawable.play);
                }

            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();

                if(position != 0)
                {
                    position -= 1;

                }

                else
                {
                    position = songs.size() - 1;
                }

                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                end.setText(String.valueOf(mediaPlayer.getDuration()));
                play.setBackgroundResource(R.drawable.play);
                seekBar.setMax(mediaPlayer.getDuration());
                songText = songs.get(position).getName();
                textView.setText(songText);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();

                if(position != songs.size() - 1)
                {
                    position += 1;

                }

                else
                {
                    position = 0;
                }

                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                end.setText(String.valueOf(mediaPlayer.getDuration()));
                play.setBackgroundResource(R.drawable.play);
                seekBar.setMax(mediaPlayer.getDuration());
                songText = songs.get(position).getName();
                textView.setText(songText);
            }
        });

    }
}