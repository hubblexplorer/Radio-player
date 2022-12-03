package com.example.radio_player;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
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


public class Audio_Radio_api {
    MediaPlayer mediaPlayer;
    String url;
    String music;

    public Audio_Radio_api() {
        url = "";


    }

    @Nullable


    /*
    private JSONObject list_radio(){
        HttpURLConnection urlConnection;
        JSONObject jsnobject = null;
        try {
            String output = "";
            URL url = new URL("http://radio.garden/api/search?q=Lisbon");
            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                output = new BufferedReader(
                       new InputStreamReader(in, StandardCharsets.UTF_8))
                       .lines()
                       .collect(Collectors.joining("\n"));
            }
            jsnobject =new JSONObject(output);


            urlConnection.disconnect();
        }
        catch (Exception e){
            Uteis.MSG_Debug(e.toString());

        }

        return jsnobject;

    }
    JSONObject output_run;
    public JSONObject list(){
        output_run =null;
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    output_run = list_radio();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        while (thread.isAlive())
            ;
        return output_run;


    }
    */
    public void radio_switch_state(Context c){
        if (mediaPlayer.isPlaying())
        {
            Uteis.MSG(c.getApplicationContext(), "Stop");
            try {
                mediaPlayer.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }

        }
        else {
            Uteis.MSG(c.getApplicationContext(), "Start");
            try {

                // initializing media player
                mediaPlayer = new MediaPlayer();
                // below line is use to set the audio
                // stream type for our media player.
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare();
                mediaPlayer.start();

            } catch (IOException e) {

                e.printStackTrace();
            }
            catch (IllegalStateException e){
                e.printStackTrace();
            }

        }

    }

    public void playRadio(String audioUrl) {
                    url = audioUrl;


                    // initializing media player
                    mediaPlayer = new MediaPlayer();
                    // below line is use to set the audio
                    // stream type for our media player.
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                    // below line is use to set our
                    // url to our media player.
                    try {
                        mediaPlayer.setDataSource(url);
                        // below line is use to prepare
                        // and start our media player.
                        mediaPlayer.prepare();
                        mediaPlayer.start();


                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

    }
    
    public void playAudio(String audio){

        music = audio;

        // initializing media player
        mediaPlayer = new MediaPlayer();
        // below line is use to set the audio
        // stream type for our media player.
        //mediaPlayer.setAudioStreamType(AudioManager);

        // below line is use to set our
        // url to our media player.
        try {
            mediaPlayer.setDataSource(url);
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
