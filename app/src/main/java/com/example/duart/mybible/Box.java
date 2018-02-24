package com.example.duart.mybible;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Box extends AppCompatActivity {

    private ListView listView;
    private RequestQueue mRequestQueue;
    private SQLiteOpenHelper dataBase;
    private SQLiteDatabase sqLiteDatabase;
    private static final String TAG = Box.class.getName();
    private static final String REQUESTTAG = "string get new item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box);

        listView = (ListView) findViewById(R.id.list_view);

        final ArrayList<String> arrayList = new ArrayList<>();
        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);

        arrayList.add("items");
        arrayList.add("categoria");
        arrayList.add("localização");

        listView.setAdapter(listAdapter);

        clickableListView();

    }

    //necessary to show buttons on action bar menu
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_box, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {

                case R.id.action_sync:
                    syncGetNewItemBox();
                    syncGetNewSubdivisionBox();
                    return true;

                default:
                    // If we got here, the user's action was not recognized.
                    // Invoke the superclass to handle it.
                    return super.onOptionsItemSelected(item);
            }
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

    private void syncGetNewItemBox() {

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = "http://casa.localtunnel.me/android/sync_get_new_item_box_android.php";

        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject mysqlDataUnsync = response.getJSONObject(i);

                        Integer id = Integer.parseInt(mysqlDataUnsync.getString("id"));
                        String name = mysqlDataUnsync.getString("name");
                        String category = mysqlDataUnsync.getString("category");

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        sqLiteDatabase.execSQL("INSERT INTO item (id, name, category, status) VALUES (" + id + ", '" + name + "', '" + category + "', 1 );");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG, "Error: " + error.toString());

            }
        });

        jsonArrayRequest.setTag(REQUESTTAG);
        mRequestQueue.add(jsonArrayRequest);

    }

    private void syncGetNewSubdivisionBox() {

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = "http://casa.localtunnel.me/android/sync_get_new_subdivision_box_android.php";

        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject mysqlDataUnsync = response.getJSONObject(i);

                        Integer id = Integer.parseInt(mysqlDataUnsync.getString("id"));
                        String subdivision = mysqlDataUnsync.getString("subdivision");

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        sqLiteDatabase.execSQL("INSERT INTO subdivision (id, subdivision, status) VALUES (" + id + ", '" + subdivision + "', 1 );");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG, "Error: " + error.toString());

            }
        });

        jsonArrayRequest.setTag(REQUESTTAG);
        mRequestQueue.add(jsonArrayRequest);

    }

}
