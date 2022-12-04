package com.example.radio_player;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class fragment1 extends Fragment  implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    //Este framento contem um temporizador
    public void onStart(){
        super.onStart();
        View view = getView();
        FloatingActionButton f = view.findViewById(R.id.fab_add);
        f.setVisibility(View.INVISIBLE);
    }

    @Override
    //esta função controla o funcionamento do butão
    public void onClick(View view) {

    }
}