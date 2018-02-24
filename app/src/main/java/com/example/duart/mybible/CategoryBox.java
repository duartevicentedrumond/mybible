package com.example.duart.mybible;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class CategoryBox extends AppCompatActivity {

    private mybibleDataBase dataBase;
    private ListView listViewCategories;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_box);

        listViewCategories = (ListView) findViewById(R.id.list_view_categories);
        dataBase = new mybibleDataBase(this);
        final ArrayList<String> arrayListItems = new ArrayList<>();
        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListItems);

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("SELECT DISTINCT category FROM item;", null);

        while (data.moveToNext()){
            arrayListItems.add(data.getString(0).toString());
        }
        listViewCategories.setAdapter(listAdapter);

        clickableListView();

    }

    public void clickableListView(){
        listViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String dataSelected = adapterView.getItemAtPosition(i).toString();
                String stringQuery = "SELECT * FROM item WHERE category='" + dataSelected + "' AND status!=3;";
                Intent intent = new Intent(CategoryBox.this, ItemBox.class);
                intent.putExtra("stringQuery", stringQuery);
                startActivity( intent );

            }
        });
    }

}
