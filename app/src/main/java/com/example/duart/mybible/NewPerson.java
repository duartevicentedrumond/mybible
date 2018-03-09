package com.example.duart.mybible;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class NewPerson extends AppCompatActivity {

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
        setContentView(R.layout.activity_new_person);

        editTextName = (EditText) findViewById(R.id.edit_text_name);
        editTextCompleteName = (EditText) findViewById(R.id.edit_text_complete_name);
        editTextBirthday = (EditText) findViewById(R.id.edit_text_birthday);
        editTextMobile = (EditText) findViewById(R.id.edit_text_mobile);
        editTextHome = (EditText) findViewById(R.id.edit_text_home);
        editTextWork = (EditText) findViewById(R.id.edit_text_work);
        editTextEmail = (EditText) findViewById(R.id.edit_text_email);
        dataBase = new mybibleDataBase(this);

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
                    insertPerson();
                    startActivity( new Intent(NewPerson.this, Person.class) );
                    return true;

                default:
                    // If we got here, the user's action was not recognized.
                    // Invoke the superclass to handle it.
                    return super.onOptionsItemSelected(item);
            }
        }

    public void insertPerson(){
        //insert person's name
        sqLiteDatabase = dataBase.getWritableDatabase();
        sqLiteDatabase.execSQL("INSERT INTO person (name, complete_name, status) VALUES ('"+ editTextName.getText().toString() + "', '" + editTextCompleteName.getText().toString() + "', 0);");

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor personId = sqLiteDatabase.rawQuery("SELECT id FROM person WHERE name='" + editTextName.getText().toString() + "' AND complete_name='" + editTextCompleteName.getText().toString() + "';", null);
        personId.moveToFirst();

        String id = personId.getString(0);

        //insert person's birthday
            sqLiteDatabase = dataBase.getWritableDatabase();
            sqLiteDatabase.execSQL("INSERT INTO birthday (id_person, date, status) VALUES (" + id + ", '" + editTextBirthday.getText().toString() + "', 0);");

        //insert person's mobile number
        if (editTextMobile.getText().toString().equals("")){
        }else {
            sqLiteDatabase = dataBase.getWritableDatabase();
            sqLiteDatabase.execSQL("INSERT INTO phone (id_person, number, category, status) VALUES (" + id + ", '" + editTextMobile.getText().toString() + "', 'telemóvel', 0);");
        }

        //insert person's home number
        if (editTextHome.getText().toString().equals("")){
        }else {
            sqLiteDatabase = dataBase.getWritableDatabase();
            sqLiteDatabase.execSQL("INSERT INTO phone (id_person, number, category, status) VALUES (" + id + ", '" + editTextHome.getText().toString() + "', 'telefone', 0);");
        }

        //insert person's work number
        if (editTextWork.getText().toString().equals("")){
        }else {
            sqLiteDatabase = dataBase.getWritableDatabase();
            sqLiteDatabase.execSQL("INSERT INTO phone (id_person, number, category, status) VALUES (" + id + ", '" + editTextWork.getText().toString() + "', 'trabalho', 0);");
        }

        //insert person's email number
        if (editTextEmail.getText().toString().equals("")){
        }else {
            sqLiteDatabase = dataBase.getWritableDatabase();
            sqLiteDatabase.execSQL("INSERT INTO email (id_person, email, category, status) VALUES (" + id + ", '" + editTextEmail.getText().toString() + "', 'geral', 0);");
        }
    }


}