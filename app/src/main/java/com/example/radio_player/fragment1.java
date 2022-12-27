package com.example.radio_player;

import static android.content.Context.MODE_PRIVATE;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.radio_player.Player.ActionPlaying;
import com.example.radio_player.Player.AudioData;
import com.example.radio_player.Player.MusicService;
import com.example.radio_player.Player.OnItemClickListener;
import com.example.radio_player.Player.music_player;
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
    @Override
    //Este framento contem um temporizador
    public void onStart(){
        super.onStart();
        View view = getView();
        FloatingActionButton f = view.findViewById(R.id.fab_add);
        f.setVisibility(View.INVISIBLE);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared_prefs", MODE_PRIVATE);
        String json = sharedPreferences.getString("audio_list", null);

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<AudioData>>() {}.getType();
        ArrayList<AudioData> audioList = gson.fromJson(json, type);
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



                if (!isRunning){
                    isRunning = true;
                    Intent intent = new Intent(view.getContext(), music_player.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("Type","Music");
                    intent.putExtra("Position",position);
                    startActivity(intent);
                }
                else {
                            Intent intent = new Intent("Change_position");
                            intent.putExtra("Position", position);
                            LocalBroadcastManager.getInstance(view.getContext()).sendBroadcast(intent);

                    }
                }

            }
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