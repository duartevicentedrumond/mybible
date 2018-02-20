package com.example.duart.mybible;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

public class SeeAllWallet extends AppCompatActivity {

    private static final String TAG = SeeAllWallet.class.getName();
    private static final String REQUESTTAG = "string request first";
    private Button btnSeeEntries;
    private ListView listViewEntries;
    private TextView textViewEntry;
    private mybibleDataBase dataBase;
    private SQLiteDatabase sqLiteDatabase;

    private RequestQueue mRequestQueue;

    private String url = "http://home.localtunnel.me/android/read_wallet.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_wallet);

        //this line removes the arrow from the action bar menu in this activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        listViewEntries = (ListView) findViewById(R.id.list_view_entries);
        dataBase = new mybibleDataBase(this);

        final ArrayList<String> arrayList = new ArrayList<>();
        final ArrayList<String> arrayListId = new ArrayList<>();
        final ArrayList<String> arrayListDate = new ArrayList<>();
        final ArrayList<String> arrayListDescription = new ArrayList<>();
        final ArrayList<String> arrayListValue = new ArrayList<>();
        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("SELECT * FROM wallet WHERE status !=3", null);

        while (data.moveToNext()){
            arrayListId.add ( data.getString(0) );
            arrayListDate.add( data.getString(1) );
            arrayListDescription.add( data.getString(2) );
            arrayListValue.add( data.getString(3) );
        }

        for ( int i = arrayListDate.size()-1; i > -1 ; i--){
            String date = arrayListDate.get(i).toString().substring(8, arrayListDate.get(i).toString().length() - 9)
                    + "."
                    + arrayListDate.get(i).toString().substring(5, arrayListDate.get(i).toString().length() - 12)
                    + "."
                    + arrayListDate.get(i).toString().substring(2, arrayListDate.get(i).toString().length() - 15)
                    + " "
                    + arrayListDate.get(i).toString().substring(10, arrayListDate.get(i).toString().length() - 6)
                    + "h"
                    + arrayListDate.get(i).toString().substring(14, arrayListDate.get(i).toString().length() - 3)
                    + "min";
            arrayList.add(" #" + arrayListId.get(i) + "\n" + date + "\n" + arrayListDescription.get(i) + "\n" + arrayListValue.get(i) + " €");
            listViewEntries.setAdapter(listAdapter);
        }

        //this detects which item was selected from listview
        listViewEntries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String dataSelected = adapterView.getItemAtPosition(i).toString();

                Intent intent = new Intent( SeeAllWallet.this, UpdateWallet.class );
                intent.putExtra("dataSelected", dataSelected);

                startActivity( intent );
            }
        });

    }

    //necessary to show buttons on action bar menu
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_see_all_wallet, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {

                case R.id.action_wallet:
                    startActivity( new Intent( SeeAllWallet.this, Wallet.class ) );
                    return true;

                default:
                    // If we got here, the user's action was not recognized.
                    // Invoke the superclass to handle it.
                    return super.onOptionsItemSelected(item);

            }
        }


}
