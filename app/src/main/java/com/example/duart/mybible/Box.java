package com.example.duart.mybible;

import android.content.Intent;
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

public class Box extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box);

        listView = (ListView) findViewById(R.id.list_view);

        final ArrayList<String> arrayList = new ArrayList<>();
        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);

        arrayList.add("items");
        arrayList.add("categoria");
        arrayList.add("localização");

        listView.setAdapter(listAdapter);

        //this detects which item was selected from listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String dataSelected = adapterView.getItemAtPosition(i).toString();

                if(dataSelected.equals("items")){
                    String stringQuery = "SELECT * FROM item WHERE status!=3";
                    Intent intent = new Intent(Box.this, ItemBox.class);
                    intent.putExtra("stringQuery", stringQuery);
                    startActivity( intent );
                }else if (dataSelected.equals("categoria")){
                    startActivity( new Intent( Box.this, CategoryBox.class ) );
                }else if (dataSelected.equals("localização")){
                    startActivity( new Intent( Box.this, LocationBox.class ) );
                }
            }
        });

    }

}
