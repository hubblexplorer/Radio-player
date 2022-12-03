package com.example.radio_player;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

//Esta classe trata de ligar a base de dados contida a pasta assets com o programa
public class DatabaseOpenHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "db.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
