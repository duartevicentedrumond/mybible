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

public class LocationBox extends AppCompatActivity {

    private ListView listViewLocation;
    private mybibleDataBase dataBase;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_box);

        listViewLocation = (ListView) findViewById(R.id.list_view_location);
        dataBase = new mybibleDataBase(this);

        printLocationList();
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
                    startActivity( new Intent( LocationBox.this, NewLocationBox.class ) );
                    return true;

                default:
                    // If we got here, the user's action was not recognized.
                    // Invoke the superclass to handle it.
                    return super.onOptionsItemSelected(item);

            }
        }

    public void printLocationList(){
        ArrayList<String> arrayListLocation = new ArrayList<>();
        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListLocation);

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("SELECT DISTINCT subdivision FROM subdivision;", null);

        while (data.moveToNext()){
            arrayListLocation.add(data.getString(0));
        }
        listViewLocation.setAdapter(listAdapter);
    }

    public void clickableListView(){
        listViewLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String dataSelected = adapterView.getItemAtPosition(i).toString();
                String stringLocation = dataSelected;
                Intent intent = new Intent(LocationBox.this, ItemAndBoxBox.class);
                intent.putExtra("stringLocation", stringLocation);
                startActivity( intent );

            }
        });
    }
}
