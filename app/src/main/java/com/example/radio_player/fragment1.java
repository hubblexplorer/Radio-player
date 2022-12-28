package com.example.radio_player;

import static android.content.Context.MODE_PRIVATE;

import static com.example.radio_player.MainActivity.mediaPlayer;
import static com.example.radio_player.MainActivity.position;
import static com.example.radio_player.MainActivity.type;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class fragment1 extends Fragment  implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }
    RecyclerView musiclist;
    Boolean isRunning = false;
    SharedPreferences.Editor editor;
    @Override
    //Este framento contem um temporizador
    public void onStart(){
        super.onStart();
        View view = getView();


        ArrayList<AudioData> audioList = MainActivity.songs;
        ArrayList<String> titulos = new ArrayList<>();
        ArrayList<byte[]> imgs = new ArrayList<>();
        ArrayList<Integer> position = new ArrayList<>();
        ArrayList<String> autores = new ArrayList<>();
        for (int i = 0; i < audioList.size(); i++){
            titulos.add(audioList.get(i).getDisplayname());
            imgs.add(audioList.get(i).getImage());
            position.add(i);
            autores.add(audioList.get(i).getAutor());

        }

        Adapter_fragment1 adapter_fragment1 = new Adapter_fragment1(titulos,imgs,position,autores);
        adapter_fragment1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Uteis.MSG_Debug(String.valueOf(position));
                MainActivity.position = position;
                type = "Music";
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                MainActivity.time = 0;
                MainActivity.mediaPlayer.reset();
                try {
                    MainActivity.mediaPlayer.setDataSource(MainActivity.songs.get(position).getData());
                    MainActivity.mediaPlayer.prepare();
                    MainActivity.isSet = true;
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                Intent intent = new Intent(view.getContext(), music_player.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);

            }}
        );

        musiclist = view.findViewById(R.id.music_list);
        musiclist.setAdapter(adapter_fragment1);
        musiclist.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    @Override
    //esta função controla o funcionamento do butão
    public void onClick(View view) {

    }
}