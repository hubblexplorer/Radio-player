package com.example.radio_player;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.sql.Array;
import java.sql.Blob;
import java.util.ArrayList;

import java.util.Random;

//Esta classe trata de fazer as comunicações a base de dados
public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;


    //Cria uma ligação lógica a BD
    private DatabaseAccess(Context context) {

        this.openHelper = new DatabaseOpenHelper(context);
    }


    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

   //Ligação à BD é inicializada
    public void open() {

        this.database = openHelper.getWritableDatabase();
    }

    //Ligação à BD é concluida
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }




    //Retorna um tamanho de uma tabela
    //é usada na função a cima para gerar um número aleaório
    public ArrayList<String> getRadioByID(String id) {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Radio Where Id=?" , new String[]{id});
        if (cursor.moveToFirst()) {
            String[] columnNames = cursor.getColumnNames();

            for (int i = 0; i < columnNames.length; i++) {
                int w = cursor.getColumnIndex(columnNames[i]);
                list.add(cursor.getString(w));

            }
        }

        cursor.close();

        return list;
    }

    public ArrayList<String> getRadiosName() {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT Nome FROM Radio",null);
        if (cursor.moveToFirst()) {
            list.add(cursor.getString(0));
            while (cursor.moveToNext()) {
                list.add(cursor.getString(0));
            }
        }

        cursor.close();


        return list;
    }
    public ArrayList<byte[]> getRadiosImage() {
        ArrayList<byte[]> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Radio",null);
        if (cursor.moveToFirst()) {
            list.add(cursor.getBlob(4));
            while (cursor.moveToNext()) {
                list.add(cursor.getBlob(4));
            }
        }

        cursor.close();

        return list;
    }
    public ArrayList<String> getRadiosUrl() {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Radio",null);
        if (cursor.moveToFirst()) {
            list.add(cursor.getString(2));
            while (cursor.moveToNext()) {
                list.add(cursor.getString(2));
            }
        }

        cursor.close();

        return list;
    }
}
