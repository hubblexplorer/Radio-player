package com.example.radio_player;





import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

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
    public static String[][] getMusicList(Activity activity, ContentResolver contentResolver) {
        int permissionCheck = ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            int EXTERNAL_STORAGE_PERMISSION_CODE = 23;
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);
            return getMusicList(activity,contentResolver);
        }

        //Some audio may be explicitly marked as not being music                                                                        //Esta condição serve para ignorar ficheiros do whassapp
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0" + " AND " + MediaStore.Audio.Media.DURATION + " > 30000" + " AND (" + MediaStore.Audio.Media.TITLE + " NOT  LIKE  'AUD%'" + " AND " + MediaStore.Audio.Media.TITLE + " NOT  LIKE  'PTT%')";

        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
        };

        Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null);

        ArrayList<String[]> songs = new ArrayList<String[]>();
        while (cursor.moveToNext()) {
            String[] aux = {cursor.getString(0) , cursor.getString(1) , cursor.getString(2) , cursor.getString(3) , cursor.getString(4) , cursor.getString(5)};
            songs.add(aux);
        }
        /*Uteis.MSG_Debug("size: "+String.valueOf(songs.size()));
        for (int i = 0; i < songs.size(); i++) {
            Uteis.MSG_Debug(String.valueOf(songs.indexOf(i)));
        }*/
        String[][] a =  new String[songs.size()][5] ;
        for (int i = 0; i < songs.size(); i++) {
            String[] aux = songs.get(i);
            a[i] = aux;
        }
        return a;
    }












}
