package com.example.duart.mybible;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by duart on 16/02/2018.
 */

public class dataBaseExample extends SQLiteOpenHelper{

    //constructor for database
    public dataBaseExample( Context appContext ) {
        super(appContext, "exampleDataBase.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        String createTableQuery;
        createTableQuery = "CREATE TABLE users ( userId INTEGER PRIMARY KEY, userName TEXT, status INTEGER )";
        database.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int old_version, int current_version) {
    }

    public Cursor getAllData(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor data = database.rawQuery("SELECT * FROM users", null);
        return data;
    }

    public Cursor getUnsyncData(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor data = database.rawQuery("SELECT * FROM users WHERE status = 0", null);
        return data;
    }

}
