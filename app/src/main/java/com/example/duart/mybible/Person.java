package com.example.duart.mybible;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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

public class Person extends AppCompatActivity {

    private ListView listViewPerson;
    private SQLiteOpenHelper dataBase;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        listViewPerson = (ListView) findViewById(R.id.list_view_contacts);
        dataBase = new mybibleDataBase(this);

        printPersonList();
        clickableListView();
    }

    //necessary to show buttons on action bar menu
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_item_box, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {

                case R.id.action_add_new_item:
                    startActivity( new Intent( Person.this, NewPerson.class ) );
                    return true;

                default:
                    // If we got here, the user's action was not recognized.
                    // Invoke the superclass to handle it.
                    return super.onOptionsItemSelected(item);

            }
        }

    public void printPersonList(){
        ArrayList<String> arrayListPerson = new ArrayList<>();
        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListPerson);

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("SELECT id, name FROM person", null);

        while (data.moveToNext()){
            arrayListPerson.add("#" + data.getString(0) + " " + data.getString(1));
        }
        listViewPerson.setAdapter(listAdapter);
    }

    public void clickableListView(){
        listViewPerson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String personSelected = adapterView.getItemAtPosition(i).toString();
                Intent intent = new Intent(Person.this, ViewPerson.class);
                intent.putExtra("personSelected", personSelected);
                startActivity( intent );
            }
        });
    }

}
