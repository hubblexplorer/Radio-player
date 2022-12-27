package com.example.radio_player;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.radio_player.Player.AudioData;
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
        musiclist = view.findViewById(R.id.music_list);
        musiclist.setAdapter(adapter_fragment1);
        musiclist.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    //esta função controla o funcionamento do butão
    public void onClick(View view) {

    }
}