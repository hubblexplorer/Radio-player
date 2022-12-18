package com.example.radio_player.Player;



import static com.example.radio_player.Player.ApplicationClass.ACTION_NEXT;
import static com.example.radio_player.Player.ApplicationClass.ACTION_PLAY;
import static com.example.radio_player.Player.ApplicationClass.ACTION_PREVIOUS;
import static com.example.radio_player.Player.ApplicationClass.CHANNEL_ID_2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import com.example.radio_player.R;
import com.example.radio_player.Uteis;
import java.io.IOException;
import wseemann.media.FFmpegMediaMetadataRetriever;


public class player extends AppCompatActivity implements ActionPlaying, ServiceConnection, View.OnClickListener, MediaPlayer.OnCompletionListener {
    ImageView next,prev,play,repeat;
    TextView title;
    int position_of_list = 0;
    String [][] songs;
    Boolean isPlaying = false;
    MusicService musicService;
    MediaSessionCompat mediaSession;
    MediaPlayer mediaPlayer;
    Boolean isSet = false;
    int time = 0;
    Boolean isRepeating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        mediaPlayer = new MediaPlayer();
        songs = Uteis.getMusicList(this, getContentResolver());
        next = findViewById(R.id.btn_front);
        prev = findViewById(R.id.btn_back);
        play = findViewById(R.id.btn_play);
        repeat = findViewById(R.id.btn_repeat);
        title = findViewById(R.id.tv_musicname);
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
        mediaSession = new MediaSessionCompat(this, "PlayerAudio");
        next.setOnClickListener(this);
        prev.setOnClickListener(this);
        play.setOnClickListener(this);
        repeat.setOnClickListener(this);


        title.setText(songs[0][2]);
        mediaPlayer.setOnCompletionListener(this);
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
    public void nextClicked() {
        position_of_list++;
        if(position_of_list > songs.length-1){
            position_of_list=0;
        }
        title.setText(songs[position_of_list][2]);
        play.setImageResource(R.drawable.icons8_pause_48);
        showNotification(R.drawable.icons8_pause_48);
        isPlaying = true;
        time = 0;
        if (isSet){
            mediaPlayer.release();
            mediaPlayer = null;
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(this);
        }
        try {
            Uteis.MSG_Debug(songs[position_of_list][3]);
            mediaPlayer.setDataSource(songs[position_of_list][3]);
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
        position_of_list--;
        if(position_of_list < 0 ){
            position_of_list=songs.length-1;
        }
        Uteis.MSG_Debug("Position: "+ position_of_list);
        title.setText(songs[position_of_list][2]);
        play.setImageResource(R.drawable.icons8_pause_48);
        showNotification(R.drawable.icons8_pause_48);

        isPlaying = true;
        time = 0;
        if (isSet){
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);

        try {
            Uteis.MSG_Debug(songs[position_of_list][3]);
            mediaPlayer.setDataSource(songs[position_of_list][3]);
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
            play.setImageResource(R.drawable.icons8_pause_48);
            showNotification(R.drawable.icons8_pause_48);
            try {
                if (!isSet) {
                    Uteis.MSG_Debug(songs[position_of_list][3]);
                    mediaPlayer.setDataSource(songs[position_of_list][3]);
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
            play.setImageResource(R.drawable.icons8_play_48);
            showNotification(R.drawable.icons8_play_48);
        }
    }

    @Override
    public void onClick(View view) {
        Uteis.MSG_Debug("Playing " + songs[position_of_list][2]);
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
                    if(isRepeating)
                    {
                        mediaPlayer.setLooping(false);
                        isRepeating = false;
                        repeat.setImageResource(R.drawable.icons8_repeat_48);
                    }
                    else
                    {
                        mediaPlayer.setLooping(true);
                        isRepeating = true;
                        repeat.setImageResource(R.drawable.icons8_repeat_48_select);
                    }
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


    private Bitmap getImage(String[] song){
        FFmpegMediaMetadataRetriever retriever = new FFmpegMediaMetadataRetriever();
        retriever.setDataSource(song[3]);
        byte [] data = retriever.getEmbeddedPicture();

        // convert the byte array to a bitmap
        Bitmap bitmap;
        if (data != null){
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        }
        else{
            bitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
        }
        // do something with the image ...
        // mImageView.setImageBitmap(bitmap);

        retriever.release();

        return bitmap;


    }

    public void showNotification(int playPauseBtn){
        Intent intent = new Intent(this, player.class);
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


        Bitmap picture = getImage(songs[position_of_list]);



        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID_2)
                .setSmallIcon(R.drawable.icons8_musical_notes_16)
                .setLargeIcon(picture)
                .setContentTitle(songs[position_of_list][2])
                .setContentText(songs[position_of_list][1])
                .addAction(R.drawable.icons8_rewind_48, "Previous", prevPendingIntent)
                .addAction(playPauseBtn, "Play", playPendingIntent)
                .addAction(R.drawable.icons8_fast_forward_48, "Next", nextPendingIntent)
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