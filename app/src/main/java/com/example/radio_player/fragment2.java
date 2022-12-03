package com.example.radio_player;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import java.util.Timer;
import java.util.TimerTask;

//Esta classe trata do fragmento 2
public class fragment2 extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.playlist_layout, container, false);
    }

    @Override
    //Inicializa as variaveis e chama a função relógio
    public void onStart(){
        super.onStart();
        View view = getView();

    }


}
