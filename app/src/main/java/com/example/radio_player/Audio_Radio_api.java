package com.example.radio_player;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.io.BufferedInputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


public class Audio_Radio_api extends AsyncTask<Void, Void, Void> {
    MediaPlayer mediaPlayer;
    String music;


    public Audio_Radio_api() {

    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    public void switch_state(Context c){

            if (mediaPlayer.isPlaying()) {
                Uteis.MSG(c.getApplicationContext(), "Stop");
                try {
                    mediaPlayer.stop();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }

            } else {
                Uteis.MSG(c.getApplicationContext(), "Start");
                try {

                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(music);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                } catch (IOException e) {

                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }

            }



    }

    
    public void startAudio(String audio){

        music = audio;

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(music);
            // below line is use to prepare
            // and start our media player.
            mediaPlayer.prepare();
            mediaPlayer.start();


        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
