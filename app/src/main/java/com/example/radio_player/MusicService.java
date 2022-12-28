package com.example.radio_player;

import static com.example.radio_player.MainActivity.CHANNEL_ID;
import static com.example.radio_player.MainActivity.isSet;
import static com.example.radio_player.MainActivity.mediaPlayer;
import static com.example.radio_player.MainActivity.notificationManager;
import static com.example.radio_player.MainActivity.position;
import static com.example.radio_player.MainActivity.songs;
import static com.example.radio_player.MainActivity.time;
import static com.example.radio_player.MainActivity.type;
import static com.example.radio_player.fragment3.radio;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {


    private NotificationCompat.Builder notificationBuilder;




    private final IBinder binder = new MusicServiceBinder();
    private int notificationId = 1;



    public MusicService() {
    }

    public class MusicServiceBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        MainActivity.mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                startMusic();
            }

            @Override
            public void onPause() {
                pauseMusic();
            }

            @Override
            public void onSkipToNext() {
                skipMusic();
            }

            @Override
            public void onSkipToPrevious(){
                previousMusic();
            }


        });



        mediaPlayer.setOnCompletionListener(this);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case "play":
                    startMusic();
                    break;
                case "pause":
                    pauseMusic();
                    break;
                case "skip":
                    skipMusic();
                    break;
                case "previous":
                    previousMusic();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void startMusic() {

        if (type == "Music") {
            mediaPlayer.release();
            mediaPlayer = null;
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(this);
            try {
                mediaPlayer.setDataSource(MainActivity.songs.get(position).getData());
                mediaPlayer.prepare();
                mediaPlayer.seekTo(MainActivity.time);
                mediaPlayer.start();
            } catch (Exception e) {

            }
        }else{
            mediaPlayer.release();
            mediaPlayer = null;
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(this);
            try {
                mediaPlayer.setDataSource(radio.get(position).getData());
                mediaPlayer.prepare();
                mediaPlayer.seekTo(MainActivity.time);
                mediaPlayer.start();
            } catch (Exception e) {

            }
        }
        showNotification();

    }



    public void pauseMusic() {
        MainActivity.time = mediaPlayer.getCurrentPosition();
        mediaPlayer.stop();
        showNotification();
    }

    public void skipMusic() {
        MainActivity.time = 0;
        mediaPlayer.release();
        mediaPlayer = null;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);

        try {
            Uteis.MSG_Debug(MainActivity.songs.get(position).getDisplayname());
            mediaPlayer.setDataSource(MainActivity.songs.get(position).getData());
            mediaPlayer.prepare();
            mediaPlayer.start();
            isSet = true;
        }
        catch (Exception e){
            Uteis.MSG_Debug(e.toString());
        }
        showNotification();
    }
    public void previousMusic() {



        MainActivity.time = 0;
        mediaPlayer.release();
        mediaPlayer = null;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);

        try {
            mediaPlayer.setDataSource(MainActivity.songs.get(position).getData());
            mediaPlayer.prepare();
            mediaPlayer.start();
            isSet = true;
        }
        catch (Exception e){
            Uteis.MSG_Debug(e.toString());
        }
        showNotification();


    }

    public void setPosition_of_list(int position){
        MainActivity.position = position;
    }

    private Bitmap getImage(byte[] img){

        // convert the byte array to a bitmap
        Bitmap bitmap;
        if (img != null){
            bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
        }
        else{
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icons8_musical_80);
        }

        return bitmap;


    }
    /*
    private void buildNotification() {


        // Add actions to the notification, if desired
        Intent playIntent = new Intent(MainActivity.context, music_player.class);
        playIntent.setAction("play");
        PendingIntent playPendingIntent = PendingIntent.getService(MainActivity.context, 0, playIntent, 0);

        Intent pauseIntent = new Intent(MainActivity.context, music_player.class);
        pauseIntent.setAction("pause");
        PendingIntent pausePendingIntent = PendingIntent.getService(MainActivity.context, 0, pauseIntent, 0);


        // Add image to the notification, if desired
        Bitmap image = getImage(MainActivity.songs.get(position).getImage());

        // Set the notification's content intent to open the app when clicked
        Intent contentIntent = new Intent(MainActivity.context, music_player.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(MainActivity.context, 0, contentIntent, 0);

        notificationBuilder = new NotificationCompat.Builder(MainActivity.context, "CHANNEL_1")
                .setSmallIcon(R.drawable.icons8_musical_notes_16)
                .setContentTitle(MainActivity.songs.get(position).getDisplayname())
                .setOngoing(true)
                .setSmallIcon(R.drawable.icons8_musical_notes_16)
                .setLargeIcon(image)
                .setContentTitle(MainActivity.songs.get(position).getDisplayname())
                .setContentText(MainActivity.songs.get(position).getAutor())
                .addAction(R.drawable.play, "Play", playPendingIntent)
                .addAction(R.drawable.pause, "Pause", pausePendingIntent)
                //.addAction(R.drawable.previous, "Previous", prevPendingIntent)
                //.addAction(R.drawable.next, "Next", nextPendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(MainActivity.mediaSession.getSessionToken()))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentPendingIntent)
                .setOnlyAlertOnce(true);
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CHANNEL_1";
            String description = "NotificationChannel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("CHANNEL_1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }*/


    private void showNotification() {
        if (type == "Music") {
            Bitmap image = getImage(MainActivity.songs.get(position).getImage());
            Intent contentIntent = new Intent(MainActivity.context, music_player.class);
            PendingIntent contentPendingIntent = PendingIntent.getActivity(MainActivity.context, 0, contentIntent, PendingIntent.FLAG_MUTABLE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.context, CHANNEL_ID)
                    .setContentTitle(songs.get(position).getDisplayname())
                    .setContentText(songs.get(position).getAutor())
                    .setSmallIcon(R.drawable.icons8_musical_notes_16)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                            .setMediaSession(MainActivity.mediaSession.getSessionToken()))
                    .setLargeIcon(image)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(contentPendingIntent)
                    .setOnlyAlertOnce(true);


            Intent playIntent = new Intent(MainActivity.context, MusicService.class);
            playIntent.setAction("play");
            PendingIntent playPendingIntent = PendingIntent.getService(MainActivity.context, 0, playIntent,  PendingIntent.FLAG_MUTABLE);
            builder.addAction(R.drawable.play, "Play", playPendingIntent);

            Intent pauseIntent = new Intent(MainActivity.context, MusicService.class);
            pauseIntent.setAction("pause");
            PendingIntent pausePendingIntent = PendingIntent.getService(MainActivity.context, 0, pauseIntent,  PendingIntent.FLAG_MUTABLE);
            builder.addAction(R.drawable.pause, "Pause", pausePendingIntent);

            notificationManager.notify(notificationId, builder.build());
        }
        else{
            Bitmap image = getImage(radio.get(position).getImage());
            Intent contentIntent = new Intent(MainActivity.context, music_player.class);
            PendingIntent contentPendingIntent = PendingIntent.getActivity(MainActivity.context, 0, contentIntent,  PendingIntent.FLAG_MUTABLE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.context, CHANNEL_ID)
                    .setContentTitle(radio.get(position).getDisplayname())
                    .setContentText(radio.get(position).getAutor())
                    .setSmallIcon(R.drawable.icons8_musical_notes_16)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                            .setMediaSession(MainActivity.mediaSession.getSessionToken()))
                    .setLargeIcon(image)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(contentPendingIntent)
                    .setOnlyAlertOnce(true);


            Intent playIntent = new Intent(MainActivity.context, MusicService.class);
            playIntent.setAction("play");
            PendingIntent playPendingIntent = PendingIntent.getService(MainActivity.context, 0, playIntent,  PendingIntent.FLAG_MUTABLE);
            builder.addAction(R.drawable.play, "Play", playPendingIntent);

            Intent pauseIntent = new Intent(MainActivity.context, MusicService.class);
            pauseIntent.setAction("pause");
            PendingIntent pausePendingIntent = PendingIntent.getService(MainActivity.context, 0, pauseIntent,  PendingIntent.FLAG_MUTABLE);
            builder.addAction(R.drawable.pause, "Pause", pausePendingIntent);

            notificationManager.notify(notificationId, builder.build());

        }
        }




    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (!mediaPlayer.isLooping()) {
            skipMusic();
        }
    }


}