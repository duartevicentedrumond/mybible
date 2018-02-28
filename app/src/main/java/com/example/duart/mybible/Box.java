package com.example.duart.mybible;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Box extends AppCompatActivity {

    private ListView listView;
    private mybibleDataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box);

        listView = (ListView) findViewById(R.id.list_view);
        dataBase = new mybibleDataBase(this);

        final ArrayList<String> arrayList = new ArrayList<>();
        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);

        arrayList.add("items");
        arrayList.add("categoria");
        arrayList.add("localização");

        listView.setAdapter(listAdapter);

        clickableListView();

    }

    public void clickableListView(){
        //this detects which item was selected from listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String dataSelected = adapterView.getItemAtPosition(i).toString();

                if(dataSelected.equals("items")){
                    String stringQuery = "SELECT * FROM item WHERE status!=3";
                    Intent intent = new Intent(Box.this, ItemBox.class);
                    intent.putExtra("stringQuery", stringQuery);
                    startActivity( intent );
                }else if (dataSelected.equals("categoria")){
                    startActivity( new Intent( Box.this, CategoryBox.class ) );
                }else if (dataSelected.equals("localização")){
                    startActivity( new Intent( Box.this, LocationBox.class ) );
                }
            }
        });
    }

}
