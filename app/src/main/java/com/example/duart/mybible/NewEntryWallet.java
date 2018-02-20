package com.example.duart.mybible;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class NewEntryWallet extends AppCompatActivity {

    private Spinner spinnerRepay, spinnerRepayment, spinnerType;
    private EditText editTextDate, editTextDescription, editTextValue, editTextSourceDestination;
    String date, description, value, sourceDestination, repay, repayment, type;
    private SQLiteOpenHelper dataBase;
    private SQLiteDatabase sqLiteDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry_wallet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //this line removes the arrow from the action bar menu in this activity
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        editTextDate = (EditText) findViewById(R.id.edit_text_date);
        editTextDescription = (EditText) findViewById(R.id.edit_text_description);
        editTextValue = (EditText) findViewById(R.id.edit_text_value);
        editTextSourceDestination = (EditText) findViewById(R.id.edit_text_source_destination);
        dataBase = new mybibleDataBase(this);

        //defines dropdown's entries
            String[] arraySpinnerRepay = new String[] {"sim", "não"};
            spinnerRepay = (Spinner) findViewById(R.id.spinner_repay);
            final ArrayAdapter<String> adapterSpinnerRepay = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arraySpinnerRepay);
            spinnerRepay.setAdapter(adapterSpinnerRepay);

            String[] arraySpinnerRepayment = new String[] {"sim", "não"};
            spinnerRepayment = (Spinner) findViewById(R.id.spinner_repayment);
            final ArrayAdapter<String> adapterSpinnerRepayment = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arraySpinnerRepayment);
            spinnerRepayment.setAdapter(adapterSpinnerRepayment);

            String[] arraySpinnerType = new String[] {"dinheiro", "cartão"};
            spinnerType = (Spinner) findViewById(R.id.spinner_type);
            final ArrayAdapter<String> adapterSpinnerType = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arraySpinnerType);
            spinnerType.setAdapter(adapterSpinnerType);

    }

    //necessary to show buttons on action bar menu
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_new_entry_wallet, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {

                case R.id.action_add_new_entry:
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
        if (spinnerRepay.getSelectedItem().toString().equals("sim")){
            repay = "yes";
        } else if (spinnerRepay.getSelectedItem().toString().equals("não")){
            repay = "no";
        }
        if (spinnerRepayment.getSelectedItem().toString().equals("sim")){
            repayment = "yes";
        } else if (spinnerRepayment.getSelectedItem().toString().equals("não")){
            repayment = "no";
        }
        type = spinnerType.getSelectedItem().toString();

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

}