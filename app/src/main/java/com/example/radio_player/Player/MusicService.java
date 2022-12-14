package com.example.radio_player.Player;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import static com.example.radio_player.Player.ApplicationClass.*;

import com.example.radio_player.Uteis;

public class MusicService extends Service {
    private IBinder mBinder = new MyBinder();
    ActionPlaying actionPlaying;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Uteis.MSG_Debug("Bind Method");
        return mBinder;
    }

    public class MyBinder extends Binder{
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Uteis.MSG_Debug("Start Bind Method");
        String actionName = intent.getStringExtra("myActionName");
        if (actionName != null) {
            switch (actionName) {
                case ACTION_PLAY:
                    if(actionPlaying != null){
                        actionPlaying.playClicked();
                    }
                    break;
                case ACTION_NEXT:
                    if(actionPlaying != null){
                        actionPlaying.nextClicked();
                    }

                    break;
                case ACTION_PREVIOUS:
                    if(actionPlaying != null){
                        actionPlaying.prevClicked();
                    }
                    break;

            }
        }
        return START_STICKY;
    }

    public void  setCallBack(ActionPlaying actionPlaying){
        this.actionPlaying = actionPlaying;
    }
}