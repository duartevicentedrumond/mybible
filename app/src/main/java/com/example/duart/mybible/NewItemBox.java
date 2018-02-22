package com.example.duart.mybible;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class NewItemBox extends AppCompatActivity {

    private SQLiteOpenHelper dataBase;
    private SQLiteDatabase sqLiteDatabase;
    private EditText editTextName;
    private AutoCompleteTextView editTextCategory;
    private AutoCompleteTextView editTextLocation;
    private AutoCompleteTextView editTextBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item_box);

        editTextName = (EditText) findViewById(R.id.edit_text_name);
        editTextCategory = (AutoCompleteTextView) findViewById(R.id.edit_text_category);
        editTextLocation = (AutoCompleteTextView) findViewById(R.id.edit_text_location);
        editTextBox = (AutoCompleteTextView) findViewById(R.id.edit_text_box);
        dataBase = new mybibleDataBase(this);

        //this line removes the arrow from the action bar menu in this activity
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        getCategoryAutoComplete();
        getLocationAutoComplete();
        getBoxAutoComplete();

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
                    insertNewItemBox();
                    String stringQuery = "SELECT * FROM item";
                    Intent intent = new Intent(NewItemBox.this, ItemBox.class);
                    intent.putExtra("stringQuery", stringQuery);
                    startActivity( intent );
                    return true;

                default:
                    // If we got here, the user's action was not recognized.
                    // Invoke the superclass to handle it.
                    return super.onOptionsItemSelected(item);
            }
        }


    public void insertNewItemBox(){
        sqLiteDatabase = dataBase.getWritableDatabase();
        sqLiteDatabase.execSQL("INSERT INTO item (name, category, status) VALUES ('" + editTextName.getText().toString() + "', '" + editTextCategory.getText().toString() + "', 0);");

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor item = sqLiteDatabase.rawQuery("SELECT id FROM item WHERE name='" + editTextName.getText() + "' AND category='" + editTextCategory.getText() + "';", null);
        item.moveToFirst();
        String stringItem = item.getString(0);

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor subdivision = sqLiteDatabase.rawQuery("SELECT id FROM subdivision WHERE subdivision='" + editTextLocation.getText() + "';", null);
        subdivision.moveToFirst();
        String stringSubdivision = subdivision.getString(0);

        String stringBox="";
        if (editTextBox.getText().toString().equals("")){
        }else {
            sqLiteDatabase = dataBase.getReadableDatabase();
            Cursor box = sqLiteDatabase.rawQuery("SELECT id FROM box WHERE box='" + editTextBox.getText() + "';", null);
            box.moveToFirst();
            stringBox = box.getString(0);
        }

        sqLiteDatabase = dataBase.getWritableDatabase();
        sqLiteDatabase.execSQL("INSERT INTO location (id_item, id_subdivision, id_box, status) VALUES ('" + stringItem + "', '" + stringSubdivision + "', '" + stringBox +"', 0);");

    }

    public void getCategoryAutoComplete(){
        ArrayList<String> arrayListCategory = new ArrayList<>();
        ArrayAdapter<String> adapter  = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayListCategory);

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("SELECT DISTINCT category FROM item;", null);
        while (data.moveToNext()){
            arrayListCategory.add(data.getString(0));
        }
        editTextCategory.setAdapter(adapter);
    }

    public void getLocationAutoComplete(){
        ArrayList<String> arrayListLocation = new ArrayList<>();
        ArrayAdapter<String> adapter  = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayListLocation);

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("SELECT DISTINCT subdivision FROM subdivision;", null);
        while (data.moveToNext()){
            arrayListLocation.add(data.getString(0));
        }
        editTextLocation.setAdapter(adapter);
    }

    public void getBoxAutoComplete(){
        ArrayList<String> arrayListBox = new ArrayList<>();
        ArrayAdapter<String> adapter  = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayListBox);

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("SELECT DISTINCT box FROM box;", null);
        while (data.moveToNext()){
            arrayListBox.add(data.getString(0));
        }
        editTextBox.setAdapter(adapter);
    }

}
