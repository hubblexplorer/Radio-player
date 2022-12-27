package com.example.radio_player;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class fragment3 extends Fragment implements View.OnClickListener {



    DatabaseAccess db;
    ArrayList<String> nome_r;
    ArrayList<byte[]> img_r;
    View view;
    ViewGroup viewGroup;
    RecyclerView gridView1,gridView2;


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
        storedata();


        Adapter_fragment3 adapter_fragment3_1 = new Adapter_fragment3(nome_r,img_r);
        gridView1 = view.findViewById(R.id.gridView1);
        gridView1.setAdapter(adapter_fragment3_1);
        gridView1.setLayoutManager(new GridLayoutManager(getContext(),2));





    }

    @Override
    //CLick listener do fragmento
    public void onClick(View view) {

    }






    // Guarda os dados da base de dados em arrays de Strings para serem posteriormente mostrados
    void storedata(){
        db.open();
        ArrayList<String> radios_nome = db.getRadiosName();
        ArrayList<byte[]> radios_img = db.getRadiosImage();
        db.close();

        for(int i = 0;i < radios_nome.size(); i++){
            nome_r.add(radios_nome.get(i));
            img_r.add(radios_img.get(i));
        }

    }




}