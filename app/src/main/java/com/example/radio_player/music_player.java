package com.example.radio_player;


import static com.example.radio_player.MainActivity.mediaPlayer;
import static com.example.radio_player.MainActivity.position;
import static com.example.radio_player.MainActivity.serviceConnection;
import static com.example.radio_player.MainActivity.songs;
import static com.example.radio_player.MainActivity.type;
import static com.example.radio_player.fragment3.radio;

import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.radio_player.Player.ActionPlaying;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;



public class music_player  extends AppCompatActivity implements ActionPlaying, View.OnClickListener, MediaPlayer.OnCompletionListener{
    private ImageView next,prev,play, img_music;
    private TextView title, timetoend;
    Handler  handler;


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

        if ( type.equals("Music")) {
            next.setVisibility(View.VISIBLE);
            prev.setVisibility(View.VISIBLE);
            timetoend.setVisibility(View.VISIBLE);
            img_music.setImageBitmap(getImage(songs.get(position).getImage()));
            if(mediaPlayer.isPlaying()){
                play.setImageResource(R.drawable.pause);
                newhandler();

            }
            next.setOnClickListener(this);
            prev.setOnClickListener(this);
            play.setOnClickListener(this);
            title.setText(songs.get(position).getDisplayname());
            newhandler();
        }
        else {
            next.setVisibility(View.INVISIBLE);
            prev.setVisibility(View.INVISIBLE);
            timetoend.setVisibility(View.INVISIBLE);
            img_music.setImageBitmap(getImage(radio.get(position).getImage()));
            if(mediaPlayer.isPlaying()){
                play.setImageResource(R.drawable.pause);
            }
            play.setOnClickListener(this);
            title.setText(radio.get(position).getDisplayname());
        }

    }
    private void newhandler() {

         Runnable updateTimeRunnable = new Runnable() {
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
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                handler.removeCallbacks(updateTimeRunnable);
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
        position++;
        if(position > MainActivity.songs.size()-1){
            position=0;
        }

        serviceConnection.skipMusic();
        title.setText(songs.get(position).getDisplayname());
        play.setImageResource(R.drawable.pause);
        img_music.setImageBitmap(getImage(songs.get(position).getImage()));



    }

    @Override
    public void prevClicked() {
        position--;
        if(position < 0 ){
            position=MainActivity.songs.size()-1;
        }
        serviceConnection.previousMusic();
        play.setImageResource(R.drawable.pause);
        img_music.setImageBitmap(getImage(songs.get(position).getImage()));


    }

    @Override
    public void playClicked() {
        if (type == "Music") {
            serviceConnection.setPosition_of_list(position);
            if (!mediaPlayer.isPlaying()) {
                serviceConnection.startMusic();
                play.setImageResource(R.drawable.pause);
            } else {
                serviceConnection.pauseMusic();
                play.setImageResource(R.drawable.play);

            }
            img_music.setImageBitmap(getImage(songs.get(position).getImage()));
        }
        else{
            serviceConnection.setPosition_of_list(position);
            if (!mediaPlayer.isPlaying()) {
                serviceConnection.startMusic();
                play.setImageResource(R.drawable.pause);
            } else {
                serviceConnection.pauseMusic();
                play.setImageResource(R.drawable.play);

            }
            img_music.setImageBitmap(getImage(radio.get(position).getImage()));
        }

    }



    public  void setPosition(int position) {
        if (type == "Music") {
            MainActivity.position = position;
            Uteis.MSG_Debug("Position: " + position);
            title.setText(songs.get(position).getDisplayname());
            play.setImageResource(R.drawable.pause);
            newhandler();
            img_music.setImageBitmap(getImage(songs.get(position).getImage()));
        }
        else{
            MainActivity.position = position;
            Uteis.MSG_Debug("Position: " + position);
            title.setText(radio.get(position).getDisplayname());
            play.setImageResource(R.drawable.pause);
            img_music.setImageBitmap(getImage(radio.get(position).getImage()));
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




    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (!mediaPlayer.isLooping()) {
            nextClicked();
        }
        newhandler();
        img_music.setImageBitmap(getImage(songs.get(position).getImage()));
    }

}