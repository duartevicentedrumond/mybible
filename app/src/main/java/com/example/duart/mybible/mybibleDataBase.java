package com.example.duart.mybible;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by duart on 18/02/2018.
 */

public class mybibleDataBase extends SQLiteOpenHelper {

    //constructor for database
    public mybibleDataBase( Context appContext ) {
        super(appContext, "mybible.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableQuery;
        createTableQuery = "CREATE TABLE wallet ( id INTEGER PRIMARY KEY, date DATETIME DEFAULT CURRENT_TIMESTAMP, description TEXT, value TEXT, source_destination TEXT, repay TEXT, repayment TEXT, type TEXT, status INTEGER)";
        sqLiteDatabase.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public Cursor getAllWalletData(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor data = database.rawQuery("SELECT * FROM wallet", null);
        return data;
    }

    public Cursor getUnsyncWalletData(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor data = database.rawQuery("SELECT * FROM wallet WHERE status=0", null);
        return data;
    }

}
