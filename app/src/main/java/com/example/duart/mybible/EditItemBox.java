package com.example.duart.mybible;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.ArrayList;

public class EditItemBox extends AppCompatActivity {

    private String itemId;
    private SQLiteOpenHelper dataBase;
    private SQLiteDatabase sqLiteDatabase;
    private ArrayList<String> arrayListItemName = new ArrayList<>();
    private EditText editTextItemName;
    private AutoCompleteTextView editTextItemLocation;
    private AutoCompleteTextView editTextItemBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item_box);

        editTextItemName = (EditText) findViewById(R.id.edit_text_item_name);
        editTextItemLocation = (AutoCompleteTextView) findViewById(R.id.edit_text_item_location);
        editTextItemBox = (AutoCompleteTextView) findViewById(R.id.edit_text_item_box);
        dataBase = new mybibleDataBase(this);

        itemId = getIntentAndTransform();

        nameSection(itemId);
        locationSection(itemId);
        boxSection(itemId);

        getLocationAutoComplete();
        getBoxAutoComplete();

    }

    //necessary to show buttons on action bar menu
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_edit_item_box, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {

                case R.id.action_edit:
                    updateItem(itemId);
                    Intent intent = new Intent( EditItemBox.this, Box.class );
                    startActivity( intent );
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
            itemId = intent.getExtras().getString("itemId");

        return itemId;
    }

    public void nameSection(String string){

        //puts item's name into edit text
            sqLiteDatabase = dataBase.getReadableDatabase();
            Cursor itemName = sqLiteDatabase.rawQuery("SELECT name FROM item WHERE id=" + string + ";", null);
            while (itemName.moveToNext()){
                editTextItemName.setText(itemName.getString(0));
            }

    }

    public void locationSection(String string){

        //puts item's location into edit text
            sqLiteDatabase = dataBase.getReadableDatabase();
            Cursor itemLocation = sqLiteDatabase.rawQuery("SELECT subdivision, id_item FROM subdivision LEFT JOIN location ON subdivision.id=location.id_subdivision WHERE id_item=" + string + ";", null);
            while (itemLocation.moveToNext()){
                editTextItemLocation.setText(itemLocation.getString(0));
            }
    }

    public void boxSection(String string){

        //puts item's location into edit text
        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor itemBox = sqLiteDatabase.rawQuery("SELECT box, id_item FROM box LEFT JOIN location ON box.id=location.id_box WHERE id_item=" + string + ";", null);
        while (itemBox.moveToNext()){
            editTextItemBox.setText(itemBox.getString(0));
        }
    }

    public void getLocationAutoComplete(){
        ArrayList<String> arrayListLocation = new ArrayList<>();
        ArrayAdapter<String> adapter  = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayListLocation);

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("SELECT DISTINCT subdivision FROM subdivision;", null);
        while (data.moveToNext()){
            arrayListLocation.add(data.getString(0));
        }
        editTextItemLocation.setAdapter(adapter);
    }

    public void getBoxAutoComplete(){
        final ArrayList<String> arrayListBox = new ArrayList<>();
        final ArrayAdapter<String> adapter  = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayListBox);

        editTextItemLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    sqLiteDatabase = dataBase.getReadableDatabase();
                    Cursor data = sqLiteDatabase.rawQuery("SELECT box, subdivision FROM box LEFT JOIN subdivision ON box.id_subdivision=subdivision.id WHERE subdivision='" + editTextItemLocation.getText() + "';", null);
                    while (data.moveToNext()){
                        arrayListBox.add(data.getString(0));
                    }
                    editTextItemBox.setAdapter(adapter);
                }
            }
        });
    }

    public void updateItem(String id){
        //updates item's name
            sqLiteDatabase = dataBase.getWritableDatabase();
            sqLiteDatabase.execSQL("UPDATE item SET name='" + editTextItemName.getText() + "', status=2 WHERE id=" + id + ";");

        //updates item's location
        String id_subdivision;
        String id_box;
            if (editTextItemLocation.getText().toString().equals("")){
                id_subdivision = "0";
                id_box = "0";
            }else {
                sqLiteDatabase = dataBase.getReadableDatabase();
                Cursor idSubdivision = sqLiteDatabase.rawQuery("SELECT id FROM subdivision WHERE subdivision='" + editTextItemLocation.getText() + "';", null);
                idSubdivision.moveToFirst();
                id_subdivision = idSubdivision.getString(0);

                sqLiteDatabase = dataBase.getReadableDatabase();
                Cursor idBox = sqLiteDatabase.rawQuery("SELECT id FROM box WHERE box='" + editTextItemBox.getText() + "';", null);
                if ( idBox.getCount()!=0 ){
                    idBox.moveToFirst();
                    id_box = idBox.getString(0);
                }else {
                    id_box = "0";
                }
            }

            sqLiteDatabase = dataBase.getWritableDatabase();
            sqLiteDatabase.execSQL("UPDATE location SET id_subdivision=" + id_subdivision + ", id_box=" + id_box + ", status=2  WHERE id_item=" + id + ";");

    }
}
