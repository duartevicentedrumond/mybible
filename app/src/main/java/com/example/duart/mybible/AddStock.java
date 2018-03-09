package com.example.duart.mybible;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class AddStock extends AppCompatActivity {

    private String itemId;
    private EditText editTextCounter;
    private EditText editTextCostTotal;
    private SQLiteOpenHelper dataBase;
    private SQLiteDatabase sqLiteDatabase;
    private TextView textViewItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        dataBase = new mybibleDataBase(this);

        editTextCounter = (EditText) findViewById(R.id.edit_text_counter);
        editTextCostTotal = (EditText) findViewById(R.id.edit_text_cost_total);
        textViewItemId = (TextView) findViewById(R.id.text_view_item_id);

        itemId = getIntentAndTransform();
        textViewItemId.setText(itemId);
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

                    inputIntoCost(itemId);
                    startActivity( new Intent( AddStock.this, Box.class ) );
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

    public void inputIntoCost(String id){
        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        Integer counter = Integer.parseInt(editTextCounter.getText().toString());
        Double cost_total = Double.parseDouble(editTextCostTotal.getText().toString());
        Double cost = cost_total/counter;

        sqLiteDatabase = dataBase.getWritableDatabase();
        sqLiteDatabase.execSQL("INSERT INTO consumables (id_item, change_stock, status) VALUES (" + id + "," + counter + ", 0);");

        for (int i = 0; i < counter; i++){
            sqLiteDatabase = dataBase.getWritableDatabase();
            sqLiteDatabase.execSQL("INSERT INTO cost (id_item, cost, status) VALUES (" + id + "," + cost + ", 0);");
            Log.i("TAG", "STRING: " + "INSERT INTO cost (id_item, cost, status) VALUES (" + id + "," + cost + ", 0);");
        }
    }
}
