package com.example.duart.mybible;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

public class UpdateWallet extends AppCompatActivity {

    private String id;
    private android.database.sqlite.SQLiteDatabase sqLiteDatabase;
    private SQLiteOpenHelper dataBase;
    private EditText editTextDate, editTextDescription, editTextValue, editTextPersonName, editTextRepay, editTextType;
    private String personId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_wallet);

        //this line removes the arrow from the action bar menu in this activity
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        editTextDate = (EditText) findViewById(R.id.edit_text_date);
        editTextDescription = (EditText) findViewById(R.id.edit_text_description);
        editTextValue = (EditText) findViewById(R.id.edit_text_value);
        editTextPersonName = (EditText) findViewById(R.id.edit_text_person);
        editTextRepay = (EditText) findViewById(R.id.edit_text_repay);
        editTextType = (EditText) findViewById(R.id.edit_text_type);
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
                    insertEditedData(id);
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

            sqLiteDatabase = dataBase.getReadableDatabase();
            Cursor personNameData = sqLiteDatabase.rawQuery("SELECT name FROM person WHERE id=" + selectedData.getString(4) + ";", null);
            personNameData.moveToFirst();
            editTextPersonName.setText( personNameData.getString(0) );

            editTextRepay.setText( selectedData.getString(6).toString() );
            editTextType.setText( selectedData.getString(7).toString() );

    }

    public void insertEditedData(String id){

        String date = editTextDate.getText().toString();
        String description = editTextDescription.getText().toString();
        String value = editTextValue.getText().toString();

        if (editTextPersonName.getText().toString().equals("")){
            personId = "0";
        }else {
            sqLiteDatabase = dataBase.getWritableDatabase();
            Cursor personIdData = sqLiteDatabase.rawQuery("SELECT id FROM person WHERE name='" + editTextPersonName.getText().toString() + "';", null);
            personIdData.moveToFirst();
            personId = personIdData.getString(0);
        }

        String repay = editTextRepay.getText().toString();
        String type = editTextType.getText().toString();

        sqLiteDatabase = dataBase.getWritableDatabase();
        String insertNewEntryQuery = "UPDATE wallet SET date='" + date + "', description='" + description + "', value='" + value + "', id_person='" + personId + "', repay='" + repay + "', type='" + type + "', status=2 WHERE id=" + id + ";";
        sqLiteDatabase.execSQL(insertNewEntryQuery);

    }

    private void deleteSelectedData(){
        sqLiteDatabase = dataBase.getWritableDatabase();

        String updateStatusQuery = "UPDATE wallet SET status=3 WHERE id=" + id +";";
        sqLiteDatabase.execSQL(updateStatusQuery);
    }

}
