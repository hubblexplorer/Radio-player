package com.example.radio_player.Player;

import static com.example.radio_player.Player.ApplicationClass.*;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.radio_player.R;
import com.example.radio_player.Uteis;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class music_player  extends AppCompatActivity implements ActionPlaying, ServiceConnection, View.OnClickListener, MediaPlayer.OnCompletionListener{
    ImageView next,prev,play;
    TextView title;
    int position_of_list = 0;
    ArrayList<AudioData> songs;
    Boolean isPlaying = false;
    MusicService musicService;
    MediaSessionCompat mediaSession;
    MediaPlayer mediaPlayer;
    Boolean isSet = false;
    int time = 0;
    Boolean isRepeating = false;
    String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        mediaPlayer = new MediaPlayer();
        Intent intent = getIntent();
        type = intent.getStringExtra("Type");
        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE);

        if ( type.equals("Music")) {

            String json = sharedPreferences.getString("audio_list", null);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<AudioData>>() {}.getType();
            songs = gson.fromJson(json, type);


            position_of_list = intent.getIntExtra("Position",0);
            next = findViewById(R.id.btn_front);
            prev = findViewById(R.id.btn_back);
            play = findViewById(R.id.btn_play);
            title = findViewById(R.id.tv_musicname);
            intent = new Intent(this, MusicService.class);
            bindService(intent, this, BIND_AUTO_CREATE);


            mediaSession = new MediaSessionCompat(this, "PlayerAudio");
            next.setOnClickListener(this);
            prev.setOnClickListener(this);
            play.setOnClickListener(this);
            title.setText(songs.get(position_of_list).getDisplayname());
            mediaPlayer.setOnCompletionListener(this);

            LocalBroadcastManager.getInstance(this).registerReceiver(
                    new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            setPosition(intent.getIntExtra("Position",0));
                        }
                    },
                    new IntentFilter("Change_position")
            );
            playClicked();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this,MusicService.class);
        bindService(intent,this,BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause(){
        super.onPause();
        unbindService(this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    Uteis.MSG_Debug("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

    }

    @Override
    public void nextClicked() {
        if(position_of_list > songs.size()-1){
            position_of_list=0;
        }
        else position_of_list++;
        title.setText(songs.get(position_of_list).getDisplayname());
        play.setImageResource(R.drawable.pause);
        showNotification(R.drawable.pause);
        isPlaying = true;
        time = 0;
        if (isSet){
            mediaPlayer.release();
            mediaPlayer = null;
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(this);
        }
        try {
            Uteis.MSG_Debug(songs.get(position_of_list).getDisplayname());
            mediaPlayer.setDataSource(songs.get(position_of_list).getData());
            mediaPlayer.prepare();
            mediaPlayer.start();
            isSet = true;
        }
        catch (Exception e){
            Uteis.MSG_Debug(e.toString());
        }

    }

    @Override
    public void prevClicked() {

        if(position_of_list == 0 ){
            position_of_list=songs.size()-1;
        }
        position_of_list--;
        Uteis.MSG_Debug("Position: "+ position_of_list);
        title.setText(songs.get(position_of_list).getDisplayname());
        play.setImageResource(R.drawable.pause);
        showNotification(R.drawable.pause);

        isPlaying = true;
        time = 0;
        if (isSet){
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);

        try {
            mediaPlayer.setDataSource(songs.get(position_of_list).getData());
            mediaPlayer.prepare();
            mediaPlayer.start();
            isSet = true;
        }
        catch (Exception e){
            Uteis.MSG_Debug(e.toString());
        }


    }

    @Override
    public void playClicked() {
        if(!isPlaying){
            isPlaying=true;
            play.setImageResource(R.drawable.pause);
            showNotification(R.drawable.pause);
            try {
                if (!isSet) {
                    mediaPlayer.setDataSource(songs.get(position_of_list).getData());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    isSet = true;
                    mediaPlayer.setOnCompletionListener(this);
                }
                else{

                    mediaPlayer.prepare();
                    mediaPlayer.seekTo(time);
                    mediaPlayer.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            time = mediaPlayer.getCurrentPosition();
            mediaPlayer.stop();

            isPlaying=false;
            play.setImageResource(R.drawable.play);
            showNotification(R.drawable.play);
        }
    }



    public  void setPosition(int position) {

            this.position_of_list = position;
            Uteis.MSG_Debug("Position: " + position_of_list);
            title.setText(songs.get(position_of_list).getDisplayname());
            play.setImageResource(R.drawable.pause);
            showNotification(R.drawable.pause);

            isPlaying = true;
            time = 0;
            if (isSet) {
                mediaPlayer.release();
                mediaPlayer = null;
            }

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(this);

            try {
                mediaPlayer.setDataSource(songs.get(position_of_list).getData());
                mediaPlayer.prepare();
                mediaPlayer.start();
                isSet = true;
            } catch (Exception e) {
                Uteis.MSG_Debug(e.toString());
            }


        }


    @Override
    public void onClick(View view) {
        Uteis.MSG_Debug("Playing " + songs.get(position_of_list).getDisplayname());
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
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        MusicService.MyBinder binder = (MusicService.MyBinder) iBinder;
        musicService = binder.getService();
        musicService.setCallBack(this);
        Uteis.MSG_Debug("Connected "+ musicService + " MusicService");
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        musicService = null;
        Uteis.MSG_Debug("Disconnected "+ musicService + " MusicService");

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

    public void showNotification(int playPauseBtn){
        Intent intent = new Intent(this, music_player.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_MUTABLE);

        Intent prevIntent= new Intent(this, NotificationRecevier.class)
                .setAction(ACTION_PREVIOUS);
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(this,0,prevIntent,PendingIntent.FLAG_MUTABLE);

        Intent playIntent= new Intent(this, NotificationRecevier.class)
                .setAction(ACTION_PLAY);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(this,0,playIntent,PendingIntent.FLAG_MUTABLE);

        Intent nextIntent= new Intent(this, NotificationRecevier.class)
                .setAction(ACTION_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(this,0,nextIntent,PendingIntent.FLAG_MUTABLE);

        Bitmap picture = getImage(songs.get(position_of_list).getImage());



        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID_2)
                .setSmallIcon(R.drawable.icons8_musical_notes_16)
                .setLargeIcon(picture)
                .setContentTitle(songs.get(position_of_list).getDisplayname())
                .setContentText(songs.get(position_of_list).getAutor())
                .addAction(R.drawable.previous, "Previous", prevPendingIntent)
                .addAction(playPauseBtn, "Play", playPendingIntent)
                .addAction(R.drawable.next, "Next", nextPendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken()))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent)
                .setOnlyAlertOnce(true)
                .build();


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0,notification);

    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (!isRepeating) {
            nextClicked();
        }
    }

}