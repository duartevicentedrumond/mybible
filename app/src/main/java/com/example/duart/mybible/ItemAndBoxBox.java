package com.example.duart.mybible;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ItemAndBoxBox extends AppCompatActivity {

    private String stringLocation;
    private SQLiteOpenHelper dataBase;
    private SQLiteDatabase sqLiteDatabase;
    private ListView listViewItemAndBox;
    private String boxId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_and_box_box);

        listViewItemAndBox = (ListView) findViewById(R.id.list_view_item_and_box);
        dataBase = new mybibleDataBase(this);

        getIntentAndTransform();
        printItemAndBoxList(stringLocation);
        clickableListView();
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

        //selects all boxes inside the subdivision selected
            sqLiteDatabase = dataBase.getReadableDatabase();
            Cursor data = sqLiteDatabase.rawQuery("SELECT box, subdivision FROM box LEFT JOIN subdivision ON box.id_subdivision=subdivision.id WHERE subdivision='" + stringLocation + "' AND box.status!=3;", null);
            while (data.moveToNext()){
                arrayListItemAndBox.add(data.getString(0));
            }

        //selects all items which are not inside any box but are inside the subdivision selected
            sqLiteDatabase = dataBase.getReadableDatabase();
            Cursor itemWithoutBox = sqLiteDatabase.rawQuery("SELECT item.id, name, subdivision, id_box  FROM item LEFT JOIN (SELECT id, subdivision, id_item, id_box FROM subdivision LEFT JOIN location ON subdivision.id=location.id_subdivision) A ON item.id=A.id_item WHERE subdivision='" + stringLocation + "' AND id_box='' AND item.status!=3;", null);
            if (itemWithoutBox.getCount()!=0){
                while (itemWithoutBox.moveToNext()){
                    arrayListItemAndBox.add("#" + itemWithoutBox.getString(0) + " " + itemWithoutBox.getString(1));
                }
            }

        //prints the items and boxes inside the subdivision selected
            listViewItemAndBox.setAdapter(listAdapter);
    }

    public void clickableListView(){
        listViewItemAndBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String itemSelected = adapterView.getItemAtPosition(i).toString();

                //checks if the selected list item belongs to table item
                    sqLiteDatabase = dataBase.getReadableDatabase();
                    Cursor item = sqLiteDatabase.rawQuery("SELECT * FROM box WHERE box='" + itemSelected + "' AND status!=3;", null);
                    Log.i("TAG", "SIZE: " + item.getCount());
                    if ( item.getCount()!=0 ){
                        //if the selected element is not an item
                        String stringQuery = "SELECT item.id, name, box FROM item LEFT JOIN (SELECT id_box, id_item, box FROM location LEFT JOIN box ON location.id_box=box.id ) A ON A.id_item=item.id WHERE box='" + itemSelected + "' AND status !=3;";
                        Intent intent = new Intent(ItemAndBoxBox.this, ItemBox.class);
                        intent.putExtra("stringQuery", stringQuery);
                        startActivity( intent );
                    }else {
                        //if the selected element is an item
                        Intent intent = new Intent(ItemAndBoxBox.this, ViewItemBox.class);
                        intent.putExtra("itemSelected", itemSelected);
                        startActivity( intent );
                    }

            }
        });
    }
}
