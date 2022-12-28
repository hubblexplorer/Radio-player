package com.example.radio_player.Player;




import static com.example.radio_player.Player.ApplicationClass.*;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.radio_player.MusicService;
import com.example.radio_player.Uteis;

public class NotificationRecevier extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent1 = new Intent(context, MusicService.class);
        if (intent.getAction() != null){
            switch (intent.getAction()){
                case ACTION_PLAY:
                    Uteis.MSG(context, "Play");
                    intent1.putExtra("myActionName",intent.getAction());
                    context.startService(intent1);
                    break;
                case ACTION_NEXT:
                    Uteis.MSG(context, "NEXT");
                    intent1.putExtra("myActionName",intent.getAction());
                    context.startService(intent1);
                    break;
                case ACTION_PREVIOUS:
                    Uteis.MSG(context, "PREVIOUS");
                    intent1.putExtra("myActionName",intent.getAction());
                    context.startService(intent1);
                    break;

            }
        }
    }
}
