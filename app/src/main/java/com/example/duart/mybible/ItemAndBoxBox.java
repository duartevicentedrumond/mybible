package com.example.duart.mybible;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ItemAndBoxBox extends AppCompatActivity {

    private String stringLocation;
    private SQLiteOpenHelper dataBase;
    private SQLiteDatabase sqLiteDatabase;
    private ListView listViewItemAndBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_and_box_box);

        listViewItemAndBox = (ListView) findViewById(R.id.list_view_item_and_box);
        dataBase = new mybibleDataBase(this);

        getIntentAndTransform();
        printItemAndBoxList(stringLocation);
    }

    public String getIntentAndTransform(){
        //this gets the selected string from SeeAllWallet
        Intent intent = getIntent();
        stringLocation = intent.getExtras().getString("stringLocation");

        return stringLocation;
    }

    public void printItemAndBoxList(String stringLocation){
        ArrayList<String> arrayListItemAndBox = new ArrayList<>();
        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListItemAndBox);

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor subdivisionId = sqLiteDatabase.rawQuery("SELECT id FROM subdivision WHERE subdivision='" + stringLocation + "';", null);
        subdivisionId.moveToFirst();
        String stringSubdivisionId = subdivisionId.getString(0);

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("SELECT box FROM box WHERE id_subdivision=" + stringSubdivisionId + ";", null);
        while (data.moveToNext()){
            arrayListItemAndBox.add(data.getString(0));
        }

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor itemWithoutBox = sqLiteDatabase.rawQuery("SELECT id_item FROM location WHERE id_subdivision='" + stringSubdivisionId + "' AND id_box='';", null);
        itemWithoutBox.moveToFirst();
        String itemWithoutBoxId = itemWithoutBox.getString(0);

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor itemWithoutBoxData = sqLiteDatabase.rawQuery("SELECT name FROM item WHERE id=" + itemWithoutBoxId + ";", null);
        while (itemWithoutBox.moveToNext()){
            arrayListItemAndBox.add(itemWithoutBoxData.getString(0));
        }

        listViewItemAndBox.setAdapter(listAdapter);
    }
}
