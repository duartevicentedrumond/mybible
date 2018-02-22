package com.example.duart.mybible;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ItemBox extends AppCompatActivity {

    private ListView listViewItems;
    private mybibleDataBase dataBase;
    private SQLiteDatabase sqLiteDatabase;
    private String string;
    private ArrayList<String> arrayListItems = new ArrayList<>();
    private String stringQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_box);

        listViewItems = (ListView) findViewById(R.id.list_view_items);
        dataBase = new mybibleDataBase(this);

        getIntentAndTransform();
        printItemList(stringQuery);
        clickableListView();

    }

    //necessary to show buttons on action bar menu
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_box, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {

                case R.id.action_add_new_item:
                    startActivity( new Intent( ItemBox.this, NewItemBox.class ) );
                    return true;

                default:
                    // If we got here, the user's action was not recognized.
                    // Invoke the superclass to handle it.
                    return super.onOptionsItemSelected(item);

            }
        }

    public String getIntentAndTransform(){
        //this gets the selected string from SeeAllWallet
        Intent intent = getIntent();
        stringQuery = intent.getExtras().getString("stringQuery");

        return stringQuery;
    }

    public void printItemList(String stringQuery){
        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListItems);

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor data = sqLiteDatabase.rawQuery(stringQuery, null);

        while (data.moveToNext()){
            arrayListItems.add("#" + data.getString(0) + " " + data.getString(1));
        }
        listViewItems.setAdapter(listAdapter);
    }

    public void clickableListView(){
        listViewItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String itemSelected = adapterView.getItemAtPosition(i).toString();
                Intent intent = new Intent(ItemBox.this, ViewItemBox.class);
                intent.putExtra("itemSelected", itemSelected);
                startActivity( intent );
            }
        });
    }
}
