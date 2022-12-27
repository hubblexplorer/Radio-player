package com.example.radio_player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.radio_player.Player.AudioData;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;


import com.example.radio_player.Player.AudioData;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import com.google.gson.Gson;
import wseemann.media.FFmpegMediaMetadataRetriever;

public class MainActivity extends AppCompatActivity {
    private TabLayout bottomAppBar;
    private ViewPager2 viewPager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



        bottomAppBar = findViewById(R.id.bottomAPPbar);
        viewPager2 = findViewById(R.id.viewpager);


        VPAdapter adapter = new VPAdapter(this);
        viewPager2.setAdapter(adapter);

        new MyTask().execute();




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
        Uteis.getMusicList(this,getContentResolver());

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


            ArrayList<AudioData> musics = new ArrayList<>();
            FFmpegMediaMetadataRetriever retriever = new FFmpegMediaMetadataRetriever();
            String[][] music = Uteis.getMusicList(MainActivity.this,getContentResolver());
            for (int i = 0; i < music.length; i++){

                retriever.setDataSource(music[i][3]);

                AudioData audio = new AudioData("Music",music[i][4],music[i][3],retriever.getEmbeddedPicture(),music[i][1],Integer.parseInt(music[i][5]));
                musics.add(audio);
            }


            SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            Gson gson = new Gson();
            String json = gson.toJson(musics);
            editor.putString("audio_list", json);
            editor.apply();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            Asycdialog.dismiss();
        }
    }
}