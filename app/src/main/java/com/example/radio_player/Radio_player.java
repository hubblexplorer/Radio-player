package com.example.radio_player;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.IBinder;
import android.view.View;


public class Radio_player extends AppCompatActivity {

    Audio_Radio_api ara;
    View view;
    Intent intent;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_music_player);
        Uteis.MSG_Debug("Aqui");
        ara = new Audio_Radio_api();
        ara.startAudio("https://radiocast.rtp.pt/antena180a.mp3");
    }


}