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
import android.widget.EditText;

public class EditPerson extends AppCompatActivity {

    private String personId;
    private SQLiteOpenHelper dataBase;
    private SQLiteDatabase sqLiteDatabase;
    private EditText editTextName;
    private EditText editTextCompleteName;
    private EditText editTextBirthday;
    private EditText editTextMobile;
    private EditText editTextHome;
    private EditText editTextWork;
    private EditText editTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_person);

        editTextName = (EditText) findViewById(R.id.edit_text_name);
        editTextCompleteName = (EditText) findViewById(R.id.edit_text_complete_name);
        editTextBirthday = (EditText) findViewById(R.id.edit_text_birthday);
        editTextMobile = (EditText) findViewById(R.id.edit_text_mobile);
        editTextHome = (EditText) findViewById(R.id.edit_text_home);
        editTextWork = (EditText) findViewById(R.id.edit_text_work);
        editTextEmail = (EditText) findViewById(R.id.edit_text_email);
        dataBase = new mybibleDataBase(this);

        personId = getIntentAndTransform();

        printName(personId);
        printBirthday(personId);
        printMobileNumber(personId);
        printHomeNumber(personId);
        printWorkNumber(personId);
        printEmailNumber(personId);
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
                    updatePerson(personId);
                    startActivity( new Intent( EditPerson.this, Person.class ) );
                    return true;

                case R.id.action_delete:
                    deletePerson(personId);
                    startActivity( new Intent( EditPerson.this, Person.class ) );
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
        personId = intent.getExtras().getString("personId");

        return personId;
    }

    public void updatePerson(String id){
        //updates person's name
        sqLiteDatabase = dataBase.getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE person SET name='" + editTextName.getText() + "', complete_name='" + editTextCompleteName.getText() + "', status=2 WHERE id=" + id + ";");

        //updates person's birthday
        sqLiteDatabase = dataBase.getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE birthday SET date='" + editTextBirthday.getText() + "', status=2 WHERE id_person=" + id + ";");

        //updates person's mobile number
        if (editTextMobile.getText().equals("")){
        }else {
            sqLiteDatabase = dataBase.getWritableDatabase();
            sqLiteDatabase.execSQL("UPDATE phone SET number='" + editTextMobile.getText() + "', status=2 WHERE id_person=" + id + " AND category='telemóvel';");
        }

        //updates person's home number
        if (editTextHome.getText().equals("")){
        }else {
            sqLiteDatabase = dataBase.getWritableDatabase();
            sqLiteDatabase.execSQL("UPDATE phone SET number='" + editTextHome.getText() + "', status=2 WHERE id_person=" + id + " AND category='telefone';");
        }

        //updates person's work number
        if (editTextWork.getText().equals("")){
        }else {
            sqLiteDatabase = dataBase.getWritableDatabase();
            sqLiteDatabase.execSQL("UPDATE phone SET number='" + editTextWork.getText() + "', status=2 WHERE id_person=" + id + " AND category='trabalho';");
        }

        //updates person's email number
        if (editTextEmail.getText().equals("")){
        }else {
            sqLiteDatabase = dataBase.getWritableDatabase();
            sqLiteDatabase.execSQL("UPDATE email SET email='" + editTextEmail.getText() + "', status=2 WHERE id_person=" + id + ";");
        }
    }

    public void deletePerson(String id){
        sqLiteDatabase = dataBase.getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE person SET status=3 WHERE id=" + id + ";");
    }

    public void printName(String id){
        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor personNameData = sqLiteDatabase.rawQuery("SELECT name, complete_name FROM person WHERE id=" + id + ";", null);
        while (personNameData.moveToNext()){
            editTextName.setText(personNameData.getString(0));
            editTextCompleteName.setText(personNameData.getString(1));
        }
    }

    public void printBirthday(String id){
        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor personBirthdayData = sqLiteDatabase.rawQuery("SELECT date FROM birthday WHERE id_person=" + id + ";", null);

        if (personBirthdayData.getCount()!=0){
            while (personBirthdayData.moveToNext()){
                editTextBirthday.setText(personBirthdayData.getString(0));
            }
        }

    }

    public void printMobileNumber(String id){
        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor personMobileNumberData = sqLiteDatabase.rawQuery("SELECT number FROM phone WHERE category='telemóvel' AND id_person=" + id + ";", null);

        if (personMobileNumberData.getCount()!=0){
            while (personMobileNumberData.moveToNext()){
                editTextMobile.setText(personMobileNumberData.getString(0));
            }
        }

    }

    public void printHomeNumber(String id){
        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor personHomeNumberData = sqLiteDatabase.rawQuery("SELECT number FROM phone WHERE category='casa' AND id_person=" + id + ";", null);

        if (personHomeNumberData.getCount()!=0){
            while (personHomeNumberData.moveToNext()){
                editTextHome.setText(personHomeNumberData.getString(0));
            }
        }

    }

    public void printWorkNumber(String id){
        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor personWorkNumberData = sqLiteDatabase.rawQuery("SELECT number FROM phone WHERE category='trabalho' AND id_person=" + id + ";", null);

        if (personWorkNumberData.getCount()!=0){
            while (personWorkNumberData.moveToNext()){
                editTextWork.setText(personWorkNumberData.getString(0));
            }
        }

    }

    public void printEmailNumber(String id){
        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor personEmailData = sqLiteDatabase.rawQuery("SELECT email FROM email WHERE id_person=" + id + ";", null);

        if (personEmailData.getCount()!=0){
            while (personEmailData.moveToNext()){
                editTextEmail.setText(personEmailData.getString(0));
            }
        }

    }

}
