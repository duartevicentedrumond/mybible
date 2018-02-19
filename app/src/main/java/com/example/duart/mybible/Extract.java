package com.example.duart.mybible;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Extract extends AppCompatActivity {

    private static final String TAG = Extract.class.getName();
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
        setContentView(R.layout.activity_extract);

        listViewEntries = (ListView) findViewById(R.id.list_view_entries);
        dataBase = new mybibleDataBase(this);

        final ArrayList<String> arrayList = new ArrayList<>();
        final ArrayList<String> arrayListId = new ArrayList<>();
        final ArrayList<String> arrayListDate = new ArrayList<>();
        final ArrayList<String> arrayListDescription = new ArrayList<>();
        final ArrayList<String> arrayListValue = new ArrayList<>();
        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);

        Cursor data = dataBase.getAllWalletData();

        data.moveToLast();
        while (data.moveToPrevious()){
            arrayListId.add ( data.getString(0) );
            arrayListDate.add( data.getString(1) );
            arrayListDescription.add( data.getString(2) );
            arrayListValue.add( data.getString(3) );
        }

        Log.i(TAG, "ARRAYSIZE: " + arrayListDate.size());

        for ( int i = 0; i < arrayListDate.size(); i++){
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
            arrayList.add(date + " #" + arrayListId.get(i) + "\n" + arrayListDescription.get(i) + "\n" + arrayListValue.get(i) + " €");
            Log.i(TAG, "ARRAYSIZE: " + date);
            listViewEntries.setAdapter(listAdapter);
        }

        listViewEntries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getBaseContext(), adapterView.getItemAtPosition(i) + "is selected", Toast.LENGTH_SHORT).show();
                view.setSelected(true);
            }
        });

    }


}
