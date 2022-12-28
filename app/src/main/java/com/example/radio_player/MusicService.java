package com.example.radio_player;

import static com.example.radio_player.MainActivity.CHANNEL_ID;
import static com.example.radio_player.MainActivity.isSet;
import static com.example.radio_player.MainActivity.mediaPlayer;
import static com.example.radio_player.MainActivity.notificationManager;
import static com.example.radio_player.MainActivity.position;
import static com.example.radio_player.MainActivity.songs;
import static com.example.radio_player.MainActivity.type;
import static com.example.radio_player.fragment2.radio;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;

public class MusicService extends Service {


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
        BroadcastReceiver mediaFinishedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!MainActivity.mediaPlayer.isLooping()) {
                    skipMusic();
                }
            }
        };
        IntentFilter filter = new IntentFilter(MainActivity.MEDIA_FINISHED_BROADCAST);
        registerReceiver(mediaFinishedReceiver, filter);
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
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(MainActivity.songs.get(position).getData());
                mediaPlayer.prepare();
                mediaPlayer.seekTo(MainActivity.time);
                mediaPlayer.start();
            } catch (Exception e) {

            }
        }else{
            mediaPlayer.reset();
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
        position++;
        if(position > MainActivity.songs.size()-1){
            position=0;
        }

        MainActivity.time = 0;
        mediaPlayer.reset();


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
        position--;
        if(position < 0 ){
            position=MainActivity.songs.size()-1;
        }


        MainActivity.time = 0;
        mediaPlayer.reset();


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
            bitmap = BitmapFactory.decodeResource(MainActivity.context.getResources(), R.drawable.icons8_musical_80);
        }

        return bitmap;


    }


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
            Intent contentIntent = new Intent(MainActivity.context, MainActivity.class);
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
        public static void repeat(){
            mediaPlayer.setLooping(!mediaPlayer.isLooping());
        }







}