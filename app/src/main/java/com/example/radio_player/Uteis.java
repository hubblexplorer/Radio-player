package com.example.radio_player;





import android.content.Context;
import android.content.Intent;

import android.util.Log;

import android.widget.Toast;

//Class uteis é utilizada para debug do programa
//Esta classe foi desenvolvida pelo professor José Francisco Monteiro Morgado
public class Uteis {

    public static void MSG(Context Cont, String txt)
    {
        Toast.makeText(Cont, txt, Toast.LENGTH_LONG).show();
    }
    public static void MSG_Debug(String txt)
    {

        Log.i("DEBUG", txt);
    }

    public static double ToDouble(String s){
        return Double.parseDouble(s);
    }

    public void sendMessage(Context Cont, String msg){
        Intent iSendMsg = new Intent(Intent.ACTION_SEND);
        iSendMsg.putExtra(Intent.EXTRA_TEXT,msg);
        iSendMsg.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        iSendMsg.setType("text/pain");
        androidx.core.content.ContextCompat.startActivity(Cont, iSendMsg, null);
    }













}
