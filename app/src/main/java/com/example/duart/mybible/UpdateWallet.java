package com.example.duart.mybible;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class UpdateWallet extends AppCompatActivity {

    private static final String TAG = UpdateWallet.class.getName();
    private String id;
    private android.database.sqlite.SQLiteDatabase sqLiteDatabase;
    private SQLiteOpenHelper dataBase;
    private Spinner spinnerRepay, spinnerRepayment, spinnerType;
    private EditText editTextDate, editTextDescription, editTextValue, editTextSourceDestination, editTextRepay, editTextRepayment, editTextType;

    String date, description, value, sourceDestination, repay, repayment, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_wallet);

        //this line removes the arrow from the action bar menu in this activity
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        editTextDate = (EditText) findViewById(R.id.edit_text_date);
        editTextDescription = (EditText) findViewById(R.id.edit_text_description);
        editTextValue = (EditText) findViewById(R.id.edit_text_value);
        editTextSourceDestination = (EditText) findViewById(R.id.edit_text_source_destination);
        editTextRepay = (EditText) findViewById(R.id.edit_text_repay);
        editTextRepayment = (EditText) findViewById(R.id.edit_text_repayment);
        editTextType = (EditText) findViewById(R.id.edit_text_type);
        spinnerRepay = (Spinner) findViewById(R.id.spinner_repay);
        spinnerRepayment = (Spinner) findViewById(R.id.spinner_repayment);
        spinnerType = (Spinner) findViewById(R.id.spinner_type);
        dataBase = new mybibleDataBase(this);

        getIntentAndTransform();

        querySelectedData(id);

    }

    //necessary to show buttons on action bar menu
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_update_wallet, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {

                case R.id.action_edit:
                    getData();
                    insertEditedData(id, date, description, value, sourceDestination, repay, repayment, type);
                    startActivity( new Intent( UpdateWallet.this, SeeAllWallet.class ) );
                    return true;

                case R.id.action_delete:
                    deleteSelectedData();
                    startActivity( new Intent( UpdateWallet.this, SeeAllWallet.class ) );
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
            String dataSelected = intent.getExtras().getString("dataSelected");

        //this gets the id from the string
            id = dataSelected.split("#")[1];
            id = id.split("\n")[0];

        return id;
    }

    public void querySelectedData( String id ){

        //queries id string
            sqLiteDatabase = dataBase.getReadableDatabase();
            String findIdQuery = "SELECT * FROM wallet WHERE id=" + id + ";";
            Cursor selectedData = sqLiteDatabase.rawQuery(findIdQuery, null);

        //gets selectedData and prints into editText
            selectedData.moveToFirst();
            editTextDate.setText( selectedData.getString(1).toString() );
            editTextDescription.setText( selectedData.getString(2).toString() );
            editTextValue.setText( selectedData.getString(3).toString() );
            editTextSourceDestination.setText( selectedData.getString(4).toString() );
            editTextRepay.setText( selectedData.getString(5).toString() );
            editTextRepayment.setText( selectedData.getString(6).toString() );
            editTextType.setText( selectedData.getString(7).toString() );

    }

    private void getData() {

        date = editTextDate.getText().toString();
        description = editTextDescription.getText().toString();
        value = editTextValue.getText().toString();
        sourceDestination = editTextSourceDestination.getText().toString();
        repay = editTextRepay.getText().toString();
        repayment = editTextRepayment.getText().toString();
        type = editTextType.getText().toString();

    }

    public void insertEditedData(String id, final String date, final String description, final String value, final String sourceDestination, final String repay, final String repayment, final String type){

        sqLiteDatabase = dataBase.getWritableDatabase();

        String insertNewEntryQuery = "UPDATE wallet SET date='" + date + "', description='" + description + "', value='" + value + "', source_destination='" + sourceDestination + "', repay='" + repay + "', repayment='" + repayment + "', type='" + type + "', status=" + 2 + " WHERE id=" + id + ";";
        sqLiteDatabase.execSQL(insertNewEntryQuery);

    }

    private void deleteSelectedData(){
        sqLiteDatabase = dataBase.getWritableDatabase();

        String updateStatusQuery = "UPDATE wallet SET status=3 WHERE id=" + id +";";
        sqLiteDatabase.execSQL(updateStatusQuery);
    }

}
