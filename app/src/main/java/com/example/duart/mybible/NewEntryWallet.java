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

    private Spinner spinnerRepay, spinnerRepayment;
    private EditText editTextDate, editTextValue;
    String date, description, value, sourceDestination, repay, repayment, type;
    private SQLiteOpenHelper dataBase;
    private SQLiteDatabase sqLiteDatabase;
    private AutoCompleteTextView editTextDescription;
    private AutoCompleteTextView editTextSourceDestination;
    private AutoCompleteTextView editTextType;


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
        editTextSourceDestination = (AutoCompleteTextView) findViewById(R.id.edit_text_source_destination);
        editTextType = (AutoCompleteTextView) findViewById(R.id.edit_text_type);
        spinnerRepay = (Spinner) findViewById(R.id.spinner_repay);
        spinnerRepayment = (Spinner) findViewById(R.id.spinner_repayment);
        dataBase = new mybibleDataBase(this);

        getDescriptionAutoComplete();
        getSourceDestinationAutoComplete();
        getTypeAutoComplete();

        getRepaySpinner();
        getRepaymentSpinner();

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
                    getData();
                    insertData(date, description, value, sourceDestination, repay, repayment, type);
                    startActivity( new Intent( NewEntryWallet.this, Wallet.class ) );
                    return true;

                default:
                    // If we got here, the user's action was not recognized.
                    // Invoke the superclass to handle it.
                    return super.onOptionsItemSelected(item);

            }
        }

    private void getData() {
        date = editTextDate.getText().toString();
        description = editTextDescription.getText().toString();
        value = editTextValue.getText().toString();
        sourceDestination = editTextSourceDestination.getText().toString();
        repay = spinnerRepay.getSelectedItem().toString();
        repayment = spinnerRepayment.getSelectedItem().toString();
        type = editTextType.getText().toString();
    }

    public void insertData(final String date, final String description, final String value, final String sourceDestination, final String repay, final String repayment, final String type ){

        sqLiteDatabase = dataBase.getWritableDatabase();

        //chooses which query should be used and inserts the query chosen
            if ( editTextDate.getText().toString().equals("") ){
                String insertNewEntryQuery = "INSERT INTO wallet (description, value, source_destination, repay, repayment, type, status) Values ('" + description + "', '" + value + "', '" + sourceDestination + "', '" + repay + "', '" + repayment + "', '" + type + "', " + 0 + ");";
                sqLiteDatabase.execSQL(insertNewEntryQuery);
            } else {
                String insertNewEntryQuery = "INSERT INTO wallet (date, description, value, source_destination, repay, repayment, type, status) Values ('" + date + "', '" + description + "', '" + value + "', '" + sourceDestination + "', '" + repay + "', '" + repayment + "', '" + type + "', " + 0 + ");";
                sqLiteDatabase.execSQL(insertNewEntryQuery);
            }

    }

    public void getRepaySpinner(){
        ArrayList<String> arrayList = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList);

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("SELECT DISTINCT repay FROM wallet;", null);
        while (data.moveToNext()){
            arrayList.add(data.getString(0));
        }
        spinnerRepay.setAdapter(adapter);
    }

    public void getRepaymentSpinner(){
        ArrayList<String> arrayList = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList);

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("SELECT DISTINCT repayment FROM wallet;", null);
        while (data.moveToNext()){
            arrayList.add(data.getString(0));
        }
        spinnerRepayment.setAdapter(adapter);
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

    public void getSourceDestinationAutoComplete(){
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayAdapter<String> adapter  = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayList);

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("SELECT DISTINCT source_destination FROM wallet;", null);
        while (data.moveToNext()){
            arrayList.add(data.getString(0));
        }
        editTextSourceDestination.setAdapter(adapter);
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

}