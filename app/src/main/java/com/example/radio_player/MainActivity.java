package com.example.radio_player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;


import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import com.google.gson.Gson;
import wseemann.media.FFmpegMediaMetadataRetriever;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    private TabLayout bottomAppBar;
    private ViewPager2 viewPager2;
    public static MusicService serviceConnection;
    public static Context context;
    public static ArrayList<AudioData> songs;
    public static MediaSessionCompat mediaSession;
    public static MediaPlayer mediaPlayer;
    public static int position;
    public static String type;
    public static boolean isSet = false;
    public static int time = 0;
    public static NotificationChannel channel;
    public static NotificationManager notificationManager;
    public static String CHANNEL_ID = "CHANNEL_1";
    public static final String MEDIA_FINISHED_BROADCAST = "com.example.app.MEDIA_FINISHED";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



        bottomAppBar = findViewById(R.id.bottomAPPbar);
        viewPager2 = findViewById(R.id.viewpager);


        VPAdapter adapter = new VPAdapter(this);
        viewPager2.setAdapter(adapter);

        new MyTask().execute();
        serviceConnection= new MusicService();
        Intent serviceIntent = new Intent(this, MainActivity.class);
        startService(serviceIntent);
        context = getApplicationContext();
        mediaSession = new MediaSessionCompat(this, "MusicService");
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        );
        mediaPlayer = new MediaPlayer();
        position = 0;
        type = "Music";
        mediaPlayer.setOnCompletionListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Channel", NotificationManager.IMPORTANCE_LOW);
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        MyTask myTask =  new MyTask();
        myTask.execute();
        try {
            myTask.get();
        }
        catch (Exception e){

        }


        new TabLayoutMediator(bottomAppBar, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                String str = "str_tab" + (position + 1);
                String packageName = getPackageName();
                int i = getResources().getIdentifier(str, "string", packageName);
                tab.setText(getResources().getString(i));
                str = "icon_tab" + (position + 1);
                i = getResources().getIdentifier(str, "drawable", packageName);
                tab.setIcon(getResources().getDrawable(i,getTheme()));

            }
        }).attach();


/*
        Intent intent = new Intent(MainActivity.this, player.class);
        startActivity(intent);*/

    }
    class MyTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog Asycdialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            Asycdialog.setMessage("Loading...");
            Asycdialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {


            songs = new ArrayList<>();
            FFmpegMediaMetadataRetriever retriever = new FFmpegMediaMetadataRetriever();
            String[][] music = Uteis.getMusicList(MainActivity.this,getContentResolver());
            for (int i = 0; i < music.length; i++){

                retriever.setDataSource(music[i][3]);

                AudioData audio = new AudioData("Music",music[i][4],music[i][3],retriever.getEmbeddedPicture(),music[i][1],Integer.parseInt(music[i][5]));
                songs.add(audio);
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            Asycdialog.dismiss();
        }
    }
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Intent broadcastIntent = new Intent(MEDIA_FINISHED_BROADCAST);
        sendBroadcast(broadcastIntent);
    }

}