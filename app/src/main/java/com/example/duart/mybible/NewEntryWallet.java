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
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class NewEntryWallet extends AppCompatActivity {

    private Spinner spinnerRepay;
    private EditText editTextDate, editTextValue;
    String date, description, value, person, repay, type, gift, personGift, categoryGift;
    private SQLiteOpenHelper dataBase;
    private SQLiteDatabase sqLiteDatabase;
    private AutoCompleteTextView editTextDescription;
    private AutoCompleteTextView editTextPerson;
    private AutoCompleteTextView editTextType;
    private Spinner spinnerGift;
    private AutoCompleteTextView editTextPersonGift;
    private AutoCompleteTextView editTextCategoryGift;
    private String personId;
    private String personGiftId;
    private String id_gift;
    private String id_wallet;
    private String date_gift;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry_wallet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //this line removes the arrow from the action bar menu in this activity
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        editTextDate = (EditText) findViewById(R.id.edit_text_date);
        editTextDescription = (AutoCompleteTextView) findViewById(R.id.edit_text_description);
        editTextValue = (EditText) findViewById(R.id.edit_text_value);
        editTextPerson = (AutoCompleteTextView) findViewById(R.id.edit_text_person);
        editTextType = (AutoCompleteTextView) findViewById(R.id.edit_text_type);
        editTextPersonGift = (AutoCompleteTextView) findViewById(R.id.edit_text_person_gift);
        editTextCategoryGift = (AutoCompleteTextView) findViewById(R.id.edit_text_category_gift);
        spinnerRepay = (Spinner) findViewById(R.id.spinner_repay);
        spinnerGift = (Spinner) findViewById(R.id.spinner_gift);
        dataBase = new mybibleDataBase(this);

        getDescriptionAutoComplete();
        getPersonAutoComplete();
        getTypeAutoComplete();
        getCategoryGiftAutoComplete();

        getRepaySpinner();
        getGiftSpinner();

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
                    personId = getPersonId();
                    personGiftId = getPersonGiftId();
                    insertData(personId, personGiftId);
                    startActivity( new Intent( NewEntryWallet.this, Wallet.class ) );
                    return true;

                default:
                    // If we got here, the user's action was not recognized.
                    // Invoke the superclass to handle it.
                    return super.onOptionsItemSelected(item);

            }
        }

    private String getPersonId() {
        if (editTextPerson.getText().equals("")){
            personId = "0";
        }else{
            sqLiteDatabase = dataBase.getReadableDatabase();
            String insertNewEntryQuery = "SELECT id FROM person WHERE name='" + editTextPerson.getText() + "';";
            Cursor data = sqLiteDatabase.rawQuery(insertNewEntryQuery, null);
            data.moveToFirst();
            personId = data.getString(0);
        }
        return personId;
    }

    private String getPersonGiftId() {
        if (spinnerGift.getSelectedItem().toString().equals("sim")){
            sqLiteDatabase = dataBase.getReadableDatabase();
            String insertNewEntryQuery = "SELECT id FROM person WHERE name='" + editTextPersonGift.getText() + "';";
            Cursor data = sqLiteDatabase.rawQuery(insertNewEntryQuery, null);
            data.moveToFirst();
            personGiftId = data.getString(0);
        }
        else{
            personGiftId = "0";
        }
        return personGiftId;
    }

    public void insertData(String personId, String personGiftId){

        date = editTextDate.getText().toString();
        description = editTextDescription.getText().toString();
        value = editTextValue.getText().toString();
        repay = spinnerRepay.getSelectedItem().toString();
        type = editTextType.getText().toString();
        categoryGift = editTextCategoryGift.getText().toString();


        if (personGiftId.equals("0")){
            sqLiteDatabase = dataBase.getWritableDatabase();

            //chooses which query should be used and inserts the query chosen
            if ( date.equals("") ){
                String insertNewEntryQuery = "INSERT INTO wallet (description, value, id_person, id_gift, repay, type, status) Values ('" + description + "', '" + value + "', " + personId + ", 0, '" + repay + "', '" + type + "', " + 0 + ");";
                sqLiteDatabase.execSQL(insertNewEntryQuery);
            } else {
                String insertNewEntryQuery = "INSERT INTO wallet (date, description, value, id_person, id_gift, repay, type, status) VALUES ('" + date + "', '" + description + "', '" + value + "', " + personId + ", 0, '" + repay + "', '" + type + "', " + 0 + ");";
                sqLiteDatabase.execSQL(insertNewEntryQuery);
            }
        }else{
            sqLiteDatabase = dataBase.getReadableDatabase();
            Cursor idGiftData = sqLiteDatabase.rawQuery("SELECT id FROM gift ORDER BY id DESC LIMIT 1;", null);

            if (idGiftData.getCount()==0){
                id_gift = "1";
            }else {
                idGiftData.moveToFirst();
                String stringIdGift = idGiftData.getString(0);
                Integer idGift = Integer.parseInt(stringIdGift) + 1;
                id_gift = String.valueOf(idGift);
            }

            sqLiteDatabase = dataBase.getWritableDatabase();

            //chooses which query should be used and inserts the query chosen
            if ( date.equals("") ){
                String insertNewEntryQuery = "INSERT INTO wallet (description, value, id_person, id_gift, repay, type, status) VALUES ('" + description + "', '" + value + "', " + personId + "," + id_gift + ", '" + repay + "', '" + type + "', " + 0 + ");";
                sqLiteDatabase.execSQL(insertNewEntryQuery);
            } else {
                String insertNewEntryQuery = "INSERT INTO wallet (date, description, value, id_person, id_gift, repay, type, status) VALUES ('" + date + "', '" + description + "', '" + value + "', " + personId + "," + id_gift + ", '" + repay + "', '" + type + "', " + 0 + ");";
                sqLiteDatabase.execSQL(insertNewEntryQuery);
            }

            sqLiteDatabase = dataBase.getReadableDatabase();
            Cursor idWalletData = sqLiteDatabase.rawQuery("SELECT id, date FROM wallet WHERE id_gift=" + id_gift + ";", null);
            idWalletData.moveToFirst();

            id_wallet = idWalletData.getString(0);
            date_gift = idWalletData.getString(1).split(" ")[0];

            sqLiteDatabase.execSQL("INSERT INTO gift (id_item, id_wallet, id_person, date, description, category, status) VALUES (0, " + id_wallet + ", " + personGiftId + ", '" + date_gift + "', '" + description + "', '" + categoryGift + "', " + 0 + ");");
        }

    }

    public void getRepaySpinner(){
        ArrayList<String> arrayList = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList);

        arrayList.add("sim");
        arrayList.add("não");
        spinnerRepay.setAdapter(adapter);
    }

    public void getGiftSpinner(){
        ArrayList<String> arrayList = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList);

        arrayList.add("sim");
        arrayList.add("não");
        spinnerGift.setAdapter(adapter);
    }

    public void getDescriptionAutoComplete(){
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayAdapter<String> adapter  = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayList);

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("SELECT DISTINCT description FROM wallet;", null);
        while (data.moveToNext()){
            arrayList.add(data.getString(0));
        }
        editTextDescription.setAdapter(adapter);
    }

    public void getPersonAutoComplete(){
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayAdapter<String> adapter  = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayList);

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("SELECT name FROM person;", null);
        while (data.moveToNext()){
            arrayList.add(data.getString(0));
        }
        editTextPerson.setAdapter(adapter);
        editTextPersonGift.setAdapter(adapter);
    }

    public void getTypeAutoComplete(){
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayAdapter<String> adapter  = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayList);

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("SELECT DISTINCT type FROM wallet;", null);
        while (data.moveToNext()){
            arrayList.add(data.getString(0));
        }
        editTextType.setAdapter(adapter);
    }

    public void getCategoryGiftAutoComplete(){
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayAdapter<String> adapter  = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayList);

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("SELECT DISTINCT category FROM gift;", null);
        while (data.moveToNext()){
            arrayList.add(data.getString(0));
        }
        editTextCategoryGift.setAdapter(adapter);
    }

}