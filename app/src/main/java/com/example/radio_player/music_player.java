package com.example.radio_player;


import static com.example.radio_player.MainActivity.mediaPlayer;
import static com.example.radio_player.MainActivity.position;
import static com.example.radio_player.MainActivity.serviceConnection;
import static com.example.radio_player.MainActivity.songs;
import static com.example.radio_player.MainActivity.type;
import static com.example.radio_player.fragment2.radio;

import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class music_player  extends AppCompatActivity implements ActionPlaying, View.OnClickListener{
    private ImageView next,prev,play, img_music;
    private TextView title, timetoend,music_size;
    Handler  handler;
    Runnable updateTimeRunnable;
    ImageButton btn_repeat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        next = findViewById(R.id.btn_front);
        prev = findViewById(R.id.btn_back);
        play = findViewById(R.id.btn_play);
        title = findViewById(R.id.tv_musicname);
        timetoend = findViewById(R.id.tv_time);
        img_music = findViewById(R.id.img_music);
        music_size = findViewById(R.id.tv_size);
        btn_repeat = findViewById(R.id.btn_repeat);

        if ( type.equals("Music")) {
            next.setVisibility(View.VISIBLE);
            prev.setVisibility(View.VISIBLE);
            timetoend.setVisibility(View.VISIBLE);
            music_size.setVisibility(View.VISIBLE);
            btn_repeat.setVisibility(View.VISIBLE);
            btn_repeat.setOnClickListener(this);
            img_music.setImageBitmap(getImage(songs.get(position).getImage()));
            if(mediaPlayer.isPlaying()){
                play.setImageResource(R.drawable.icons8_pause_48);
                newhandler();

            }
            next.setOnClickListener(this);
            prev.setOnClickListener(this);
            play.setOnClickListener(this);
            title.setText(songs.get(position).getDisplayname());
            int duration = songs.get(position).getDuration();
            int minutes = duration / 1000 / 60;
            int seconds = (duration / 1000) % 60;
            timetoend.setText("0:00");
            String time = String.format("%d:%02d", minutes, seconds);
            music_size.setText(time);
            newhandler();
            BroadcastReceiver mediaFinishedReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    handler.removeCallbacks(updateTimeRunnable);
                    if (!MainActivity.mediaPlayer.isLooping()) {
                        nextClicked();
                    }
                    newhandler();
                    img_music.setImageBitmap(getImage(songs.get(position).getImage()));
                    int duration = songs.get(position).getDuration();
                    int minutes = duration / 1000 / 60;
                    int seconds = (duration / 1000) % 60;
                    String time = String.format("%d:%02d", minutes, seconds);
                    music_size.setText(time);

                }
            };
            IntentFilter filter = new IntentFilter(MainActivity.MEDIA_FINISHED_BROADCAST);
            registerReceiver(mediaFinishedReceiver, filter);
        }
        else {
            next.setVisibility(View.INVISIBLE);
            prev.setVisibility(View.INVISIBLE);
            timetoend.setVisibility(View.INVISIBLE);
            music_size.setVisibility(View.INVISIBLE);
            btn_repeat.setVisibility(View.INVISIBLE);
            img_music.setImageBitmap(getImage(radio.get(position).getImage()));
            if(mediaPlayer.isPlaying()){
                play.setImageResource(R.drawable.icons8_pause_48);
            }
            play.setOnClickListener(this);
            title.setText(radio.get(position).getDisplayname());
        }

    }
    private void newhandler() {

        updateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                // Update the TextView with the current time of the song
                int currentPosition = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();
                // Calculate the current time in minutes and seconds
                int minutes = currentPosition / 1000 / 60;
                int seconds = (currentPosition / 1000) % 60;
                String time = String.format("%d:%02d", minutes, seconds);
                // Set the TextView text
                timetoend.setText(time);
                // Post the runnable again after a delay
                handler.postDelayed(this, 1000);
            }
        };
        handler = new Handler();

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                handler.post(updateTimeRunnable);
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                handler.removeCallbacks(updateTimeRunnable);
                return false;
            }
        });
    }



    @Override
    public void nextClicked() {
        serviceConnection.skipMusic();
        title.setText(songs.get(position).getDisplayname());
        play.setImageResource(R.drawable.icons8_pause_48);
        img_music.setImageBitmap(getImage(songs.get(position).getImage()));
        int duration = songs.get(position).getDuration();
        int minutes = duration / 1000 / 60;
        int seconds = (duration / 1000) % 60;
        String time = String.format("%d:%02d", minutes, seconds);
        music_size.setText(time);

    }

    @Override
    public void prevClicked() {
        serviceConnection.previousMusic();
        title.setText(songs.get(position).getDisplayname());
        play.setImageResource(R.drawable.icons8_pause_48);
        img_music.setImageBitmap(getImage(songs.get(position).getImage()));
        int duration = songs.get(position).getDuration();
        int minutes = duration / 1000 / 60;
        int seconds = (duration / 1000) % 60;
        String time = String.format("%d:%02d", minutes, seconds);
        music_size.setText(time);


    }

    @Override
    public void playClicked() {
        if (type == "Music") {
            serviceConnection.setPosition_of_list(position);
            if (!mediaPlayer.isPlaying()) {
                serviceConnection.startMusic();
                play.setImageResource(R.drawable.icons8_pause_48);
            } else {
                serviceConnection.pauseMusic();
                play.setImageResource(R.drawable.icons8_play_48);

            }
            img_music.setImageBitmap(getImage(songs.get(position).getImage()));
        }
        else{
            serviceConnection.setPosition_of_list(position);
            if (!mediaPlayer.isPlaying()) {
                serviceConnection.startMusic();
                play.setImageResource(R.drawable.icons8_pause_48);
            } else {
                serviceConnection.pauseMusic();
                play.setImageResource(R.drawable.icons8_play_48);

            }
            img_music.setImageBitmap(getImage(radio.get(position).getImage()));
        }

    }
    public void repeat(){
        MusicService.repeat();
        if (mediaPlayer.isLooping()){
            btn_repeat.setImageResource(R.drawable.icons8_repeat_48_select);
        }
        else{
            btn_repeat.setImageResource(R.drawable.icons8_repeat_48);
        }
    }

    @Override
    public void onClick(View view) {
        Uteis.MSG_Debug("Playing " + songs.get(position).getDisplayname());
        switch (view.getId()){

            case R.id.btn_front:
                nextClicked();
                break;
            case R.id.btn_back:
                prevClicked();
                break;
            case R.id.btn_play:
                playClicked();
                break;
            case R.id.btn_repeat:
                repeat();
                break;

        }
        if (type =="Music") {
            newhandler();
            img_music.setImageBitmap(getImage(songs.get(position).getImage()));
        }
        else {
            img_music.setImageBitmap(getImage(radio.get(position).getImage()));
        }
    }


    private Bitmap getImage(byte[] img){

        // convert the byte array to a bitmap
        Bitmap bitmap;
        if (img != null){
            bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
        }
        else{
          bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.icons8_musical_80);
        }

        return bitmap;


    }

}