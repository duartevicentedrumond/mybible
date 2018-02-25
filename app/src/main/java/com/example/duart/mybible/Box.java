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
                    syncGetNewBoxBox();
                    syncGetNewLocationBox();
                    syncGetNewLinkBox();
                    syncGetNewConsumablesBox();
                    syncGetNewNoConsumablesBox();
                    syncGetNewCostBox();
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

    private void syncGetNewBoxBox(){

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = "http://casa.localtunnel.me/android/sync_get_new_box_box_android.php";

        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject mysqlDataUnsync = response.getJSONObject(i);

                        Integer id = Integer.parseInt(mysqlDataUnsync.getString("id"));
                        String box = mysqlDataUnsync.getString("box");

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        sqLiteDatabase.execSQL("INSERT INTO box (id, box, status) VALUES (" + id + ", '" + box + "', 1 );");

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

    private void syncGetNewLocationBox(){

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = "http://casa.localtunnel.me/android/sync_get_new_location_box_android.php";

        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject mysqlDataUnsync = response.getJSONObject(i);

                        Integer id_item = Integer.parseInt(mysqlDataUnsync.getString("id_item"));
                        Integer id_subdivision = Integer.parseInt(mysqlDataUnsync.getString("id_subdivision"));
                        Integer id_box = Integer.parseInt(mysqlDataUnsync.getString("id_box"));

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        sqLiteDatabase.execSQL("INSERT INTO location (id_item, id_subdivision, id_box, status) VALUES (" + id_item + ", " + id_subdivision + ", " + id_box + ", 1 );");

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

    private void syncGetNewLinkBox(){

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = "http://casa.localtunnel.me/android/sync_get_new_link_box_android.php";

        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject mysqlDataUnsync = response.getJSONObject(i);

                        Integer id_item_1 = Integer.parseInt(mysqlDataUnsync.getString("id_item_1"));
                        Integer id_item_2 = Integer.parseInt(mysqlDataUnsync.getString("id_item_2"));

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        sqLiteDatabase.execSQL("INSERT INTO link (id_item_1, id_item_2, status) VALUES (" + id_item_1 + ", " + id_item_2 + ", 1 );");

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

    private void syncGetNewConsumablesBox(){

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = "http://casa.localtunnel.me/android/sync_get_new_consumables_box_android.php";

        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject mysqlDataUnsync = response.getJSONObject(i);

                        Integer id_item = Integer.parseInt(mysqlDataUnsync.getString("id_item"));
                        String date = mysqlDataUnsync.getString("date");
                        Integer change_stock = Integer.parseInt(mysqlDataUnsync.getString("change_stock"));

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        sqLiteDatabase.execSQL("INSERT INTO consumables (id_item, date, change_stock, status) VALUES (" + id_item + ", '" + date + "', " + change_stock + ", 1 );");

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

    private void syncGetNewNoConsumablesBox(){

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = "http://casa.localtunnel.me/android/sync_get_new_noconsumables_box_android.php";

        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject mysqlDataUnsync = response.getJSONObject(i);

                        Integer id_item = Integer.parseInt(mysqlDataUnsync.getString("id_item"));
                        String date = mysqlDataUnsync.getString("date");
                        Integer state = Integer.parseInt(mysqlDataUnsync.getString("state"));

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        sqLiteDatabase.execSQL("INSERT INTO noconsumables (id_item, date, change_stock, status) VALUES (" + id_item + ", '" + date + "', " + state + ", 1 );");

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

    private void syncGetNewCostBox(){

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = "http://casa.localtunnel.me/android/sync_get_new_cost_box_android.php";

        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject mysqlDataUnsync = response.getJSONObject(i);

                        Integer id_item = Integer.parseInt(mysqlDataUnsync.getString("id_item"));
                        String date = mysqlDataUnsync.getString("date");
                        String cost = mysqlDataUnsync.getString("cost");

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        sqLiteDatabase.execSQL("INSERT INTO cost (id_item, date, cost, status) VALUES (" + id_item + ", '" + date + "', '" + cost + "', 1 );");

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
