package com.example.duart.mybible;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.ArrayList;

public class NewLocationBox extends AppCompatActivity {

    private EditText editTextNewBox;
    private AutoCompleteTextView editTextNewLocation;
    private mybibleDataBase dataBase;
    private SQLiteDatabase sqLiteDatabase;
    private ArrayList<String> arrayListLocation = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_location_box);

        editTextNewBox = (EditText) findViewById(R.id.edit_text_new_box);
        editTextNewLocation = (AutoCompleteTextView) findViewById(R.id.edit_text_new_location);
        dataBase = new mybibleDataBase(this);

        getLocationAutoComplete();

    }

    //necessary to show buttons on action bar menu
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_new, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {

                case R.id.action_add_new:
                    insertNewLocationBox();
                    return true;

                default:
                    // If we got here, the user's action was not recognized.
                    // Invoke the superclass to handle it.
                    return super.onOptionsItemSelected(item);
            }
        }

    public void insertNewLocationBox(){
        String newBox = editTextNewBox.getText().toString();
        String newLocation = editTextNewLocation.getText().toString();

        if (newBox.equals("")){
            sqLiteDatabase = dataBase.getWritableDatabase();
            sqLiteDatabase.execSQL("INSERT INTO subdivision (subdivision, status) VALUES ('" + newLocation + "', 0);");
        }
        else {
            sqLiteDatabase = dataBase.getReadableDatabase();
            Cursor data = sqLiteDatabase.rawQuery("SELECT id FROM subdivision WHERE subdivision='" + newLocation + "';", null);

            String Location = data.getString(0);

            sqLiteDatabase = dataBase.getWritableDatabase();
            sqLiteDatabase.execSQL("INSERT INTO box (box, id_subdivision, status) VALUES ('" + newBox + "', '" + Location + "', 0);");
        }
    }

    public void getLocationAutoComplete(){
        ArrayAdapter<String> adapter  = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayListLocation);

        arrayListLocation.add("Portugal");
        arrayListLocation.add("Espanha");

        editTextNewLocation.setAdapter(adapter);
    }
}
