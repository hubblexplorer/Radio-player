package com.example.radio_player;

import static com.example.radio_player.MainActivity.mediaPlayer;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class fragment2 extends Fragment implements View.OnClickListener {



    DatabaseAccess db;
    ArrayList<String> nome_r;
    ArrayList<byte[]> img_r;
    public static ArrayList<AudioData> radio;
    View view;
    ViewGroup viewGroup;
    RecyclerView gridView1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = container;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_radio, container, false);

    }

    @Override
    //Inicializar todas as variávies
    public void onStart(){
        super.onStart();



        view = getView();
        db = DatabaseAccess.getInstance(view.getContext());
        nome_r = new ArrayList<>();
        img_r = new ArrayList<>();
        radio = new ArrayList<>();
        storedata();


        Adapter_fragment3 adapter_fragment3 = new Adapter_fragment3(nome_r,img_r);
        adapter_fragment3.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Uteis.MSG_Debug(String.valueOf(position));
                MainActivity.type = "Radio";
                MainActivity.position = position;
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                MainActivity.time = 0;
                MainActivity.mediaPlayer.reset();
                try {
                    MainActivity.mediaPlayer.setDataSource(radio.get(position).getData());
                    MainActivity.mediaPlayer.prepare();
                    MainActivity.isSet = true;
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                Intent intent = new Intent(view.getContext(), music_player.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        gridView1 = view.findViewById(R.id.gridView1);
        gridView1.setAdapter(adapter_fragment3);
        gridView1.setLayoutManager(new GridLayoutManager(getContext(),2));






    }







    // Guarda os dados da base de dados em arrays de Strings para serem posteriormente mostrados
    void storedata(){
        db.open();
        ArrayList<String> radios_nome = db.getRadiosName();
        ArrayList<byte[]> radios_img = db.getRadiosImage();
        ArrayList<String> radios_url = db.getRadiosUrl();
        db.close();

        for(int i = 0;i < radios_nome.size(); i++){
            nome_r.add(radios_nome.get(i));
            img_r.add(radios_img.get(i));
            AudioData audioData = new AudioData("Radio",radios_nome.get(i),radios_url.get(i),radios_img.get(i));
            radio.add(audioData);
        }

    }


    @Override
    public void onClick(View view) {

    }
}