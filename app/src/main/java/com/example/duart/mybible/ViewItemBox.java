package com.example.duart.mybible;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewItemBox extends AppCompatActivity {

    private String itemId;
    private TextView textViewItemName;
    private SQLiteOpenHelper dataBase;
    private SQLiteDatabase sqLiteDatabase;
    private ArrayList<String> arrayListItemName = new ArrayList<>();
    private ArrayList<String> arrayListItemSubdivisionId = new ArrayList<>();
    private ArrayList<String> arrayListItemBoxId = new ArrayList<>();
    private TextView textViewItemLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item_box);

        textViewItemName = (TextView) findViewById(R.id.text_view_item_name);
        textViewItemLocation = (TextView) findViewById(R.id.text_view_item_location);
        dataBase = new mybibleDataBase(this);

        getIntentAndTransform();
        printItemInformation(itemId);
    }

    public String getIntentAndTransform(){
        //this gets the selected string from SeeAllWallet
            Intent intent = getIntent();
            String itemSelected = intent.getExtras().getString("itemSelected");

        //this gets the id from the string
            itemSelected = itemSelected.split("#")[1];
            itemId = itemSelected.split(" ")[0];

        return itemId;
    }

    public void printItemInformation(String itemId){

        //gets item's id
            sqLiteDatabase = dataBase.getReadableDatabase();
            Cursor itemNameData = sqLiteDatabase.rawQuery("SELECT * FROM item WHERE id=" + itemId + ";", null);
            while (itemNameData.moveToNext()){
                arrayListItemName.add(itemNameData.getString(1));
            }

        //gets item's location
            sqLiteDatabase = dataBase.getReadableDatabase();
            Cursor itemLocationData = sqLiteDatabase.rawQuery("SELECT id_subdivision, id_box FROM location WHERE id_item=" + itemId + ";", null);
            while (itemLocationData.moveToNext()){
                arrayListItemSubdivisionId.add(itemLocationData.getString(0));
                arrayListItemBoxId.add(itemLocationData.getString(1));
            }
            sqLiteDatabase = dataBase.getReadableDatabase();
            Cursor itemSubdivisionName = sqLiteDatabase.rawQuery("SELECT subdivision FROM subdivision WHERE id=" + arrayListItemSubdivisionId.get(0) + ";", null);
            itemSubdivisionName.moveToFirst();

            sqLiteDatabase = dataBase.getReadableDatabase();
            Cursor itemBoxName = sqLiteDatabase.rawQuery("SELECT box FROM box WHERE id='" + arrayListItemBoxId.get(0) + "';", null);

        if(itemBoxName.getCount()!=0){
            itemBoxName.moveToFirst();

            textViewItemName.setText("#" + itemId + " " + arrayListItemName.get(0));
            textViewItemLocation.setText(itemSubdivisionName.getString(0) + " > " + itemBoxName.getString(0));
        }else{
            textViewItemName.setText("#" + itemId + " " + arrayListItemName.get(0));
            textViewItemLocation.setText(itemSubdivisionName.getString(0));
        }


    }
}
