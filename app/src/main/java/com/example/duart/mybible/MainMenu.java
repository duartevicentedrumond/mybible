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

public class MainMenu extends AppCompatActivity {

    private static final String TAG = MainMenu.class.getName();
    private static final String REQUESTTAG = "string request first";
    private SQLiteOpenHelper dataBase;
    private SQLiteDatabase sqLiteDatabase;

    private RequestQueue mRequestQueue;

    public String IpAddress = "http://dpvdda.localtunnel.me/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //this line removes the arrow from the action bar menu in this activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        dataBase = new mybibleDataBase(this);

    }

    //necessary to show buttons on action bar menu
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_main_menu, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {

                case R.id.action_sync:
                    syncGetNewBoxBox();
                    syncGetEditedBoxBox();
                    syncGetDeletedBoxBox();
                    syncSendNewBoxBox();
                    syncSendEditedBoxBox();

                    syncGetNewConsumablesBox();
                    syncGetEditedConsumablesBox();
                    syncGetDeletedConsumablesBox();
                    syncSendNewConsumablesBox();

                    syncGetNewCostBox();
                    syncGetEditedCostBox();
                    syncGetDeletedCostBox();
                    syncSendNewCostBox();

                    syncGetNewItemBox();
                    syncGetEditedItemBox();
                    syncGetDeletedItemBox();
                    syncSendNewItemBox();
                    syncSendEditedItemBox();

                    syncGetNewLocationBox();
                    syncGetEditedLocationBox();
                    syncGetDeletedLocationBox();
                    syncSendNewLocationBox();

                    syncGetNewNoConsumablesBox();
                    syncGetEditedNoConsumablesBox();
                    syncGetDeletedNoConsumablesBox();
                    syncSendNewNoConsumablesBox();

                    syncGetNewSubdivisionBox();
                    syncGetEditedSubdivisionBox();
                    syncGetDeletedSubdivisionBox();
                    syncSendNewSubdivisionBox();

                    syncGetNewEntryWallet();
                    syncGetEditedEntryWallet();
                    syncGetDeletedEntryWallet();
                    syncSendNewEntryWallet();
                    syncSendEditedEntryWallet();
                    syncSendDeletedEntryWallet();
                    return true;

                default:
                    // If we got here, the user's action was not recognized.
                    // Invoke the superclass to handle it.
                    return super.onOptionsItemSelected(item);
            }
        }

    public void moneyClick(View view){
        startActivity( new Intent( MainMenu.this, Wallet.class ) );
    }

    public void boxClick(View view){
        startActivity( new Intent( MainMenu.this, Box.class ) );
    }

    public void contactClick(View view){
        startActivity( new Intent( MainMenu.this, Person.class ) );
    }

    public void giftClick(View view){
        startActivity( new Intent( MainMenu.this, Gift.class ) );
    }




    private void syncGetNewBorrowBox(){

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_new_borrow_box_android.php";

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
                        String person = mysqlDataUnsync.getString("person");

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        sqLiteDatabase.execSQL("INSERT INTO borrow (id_item, date, state, id_person, status) VALUES (" + id_item + ", '" + date + "', " + state + ", '" + person + "', 1 );");

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

    private void syncSendNewBorrowBox(){

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM borrow WHERE status=0;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_new_borrow_box_android.php";

        ArrayList<String> arrayListIdItem = new ArrayList<>();
        ArrayList<String> arrayListDate = new ArrayList<>();
        ArrayList<String> arrayListState = new ArrayList<>();
        ArrayList<String> arrayListPerson = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListIdItem.add(unsyncData.getString(0));
            arrayListDate.add(unsyncData.getString(1));
            arrayListState.add(unsyncData.getString(2));
            arrayListPerson.add(unsyncData.getString(3));
        }

        for (int i = 0; i < arrayListIdItem.size(); i++){
            sendNewBorrowBox(
                    arrayListIdItem.get(i),
                    arrayListDate.get(i),
                    arrayListState.get(i),
                    arrayListPerson.get(i),
                    "1",
                    url);
        }
    }

        private void sendNewBorrowBox( final String id_item, final String date, final String state, final String person, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idItemHolder = id_item;
                String dateHolder = date;
                String stateHolder = state;
                String personHolder = person;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id_item", idItemHolder));
                nameValuePairs.add(new BasicNameValuePair("date", dateHolder));
                nameValuePairs.add(new BasicNameValuePair("state", stateHolder));
                nameValuePairs.add(new BasicNameValuePair("person", personHolder));
                nameValuePairs.add(new BasicNameValuePair("status", statusHolder));

                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(url);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();

                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }

                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(id_item, date, state, person, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "UPDATE borrow SET status=1 WHERE id_item=" + id_item + " AND status=0;";
        sqLiteDatabase.execSQL(setStatusQuery);
    }



    private void syncGetNewBoxBox(){

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_new_box_box_android.php";

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
                        Integer id_subdivision = Integer.parseInt(mysqlDataUnsync.getString("id_subdivision"));

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        sqLiteDatabase.execSQL("INSERT INTO box (id, box, id_subdivision, status) VALUES (" + id + ", '" + box + "', " + id_subdivision + ", 1 );");

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

    private void syncGetEditedBoxBox(){

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_edited_box_box_android.php";

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
                        Integer id_subdivision = Integer.parseInt(mysqlDataUnsync.getString("id_subdivision"));

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        sqLiteDatabase.execSQL("UPDATE box SET box='" + box + "', id_subdivision=" + id_subdivision + ", status=1 WHERE id=" + id + ";");

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

    private void syncGetDeletedBoxBox(){

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_deleted_box_box_android.php";

        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject mysqlDataUnsync = response.getJSONObject(i);

                        Integer id = Integer.parseInt(mysqlDataUnsync.getString("id"));

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        sqLiteDatabase.execSQL("UPDATE box SET status=3 WHERE id=" + id + ";");

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

    private void syncSendNewBoxBox(){

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM box WHERE status=0;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_new_box_box_android.php";

        ArrayList<String> arrayListId = new ArrayList<>();
        ArrayList<String> arrayListBox = new ArrayList<>();
        ArrayList<String> arrayListIdSubdivision = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListId.add(unsyncData.getString(0));
            arrayListBox.add(unsyncData.getString(1));
            arrayListIdSubdivision.add(unsyncData.getString(2));
        }

        for (int i = 0; i < arrayListId.size(); i++){
            sendNewBoxBox(
                    arrayListId.get(i),
                    arrayListBox.get(i),
                    arrayListIdSubdivision.get(i),
                    "1",
                    url);
        }
    }

        private void sendNewBoxBox( final String id, final String box, final String id_subdivision, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idHolder = id;
                String boxHolder = box;
                String idSubdivisionHolder = id_subdivision;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id", idHolder));
                nameValuePairs.add(new BasicNameValuePair("box", boxHolder));
                nameValuePairs.add(new BasicNameValuePair("id_subdivision", idSubdivisionHolder));
                nameValuePairs.add(new BasicNameValuePair("status", statusHolder));

                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(url);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();

                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }

                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(id, box, id_subdivision, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "UPDATE box SET status=1 WHERE id=" + id + ";";
        sqLiteDatabase.execSQL(setStatusQuery);
    }

    private void syncSendEditedBoxBox(){

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM box WHERE status=2;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_edited_box_box_android.php";

        ArrayList<String> arrayListId = new ArrayList<>();
        ArrayList<String> arrayListBox = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListId.add(unsyncData.getString(0));
            arrayListBox.add(unsyncData.getString(1));
        }

        for (int i = 0; i < arrayListId.size(); i++){
            sendEditedBoxBox(
                    arrayListId.get(i),
                    arrayListBox.get(i),
                    "1",
                    url);
        }
    }

        private void sendEditedBoxBox( final String id, final String box, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idHolder = id;
                String boxHolder = box;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id", idHolder));
                nameValuePairs.add(new BasicNameValuePair("box", boxHolder));
                nameValuePairs.add(new BasicNameValuePair("status", statusHolder));

                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(url);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();

                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }

                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(id, box, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "UPDATE box SET status=1 WHERE id=" + id + ";";
        sqLiteDatabase.execSQL(setStatusQuery);
    }




    private void syncGetNewConsumablesBox(){

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_new_consumables_box_android.php";

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

    private void syncGetEditedConsumablesBox(){

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_edited_consumables_box_android.php";

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
                        sqLiteDatabase.execSQL("UPDATE consumables SET date='" + date + "', change_stock=" + change_stock + ", status=1 WHERE id_item=" + id_item + ";");

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

    private void syncGetDeletedConsumablesBox(){

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_deleted_consumables_box_android.php";

        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject mysqlDataUnsync = response.getJSONObject(i);

                        Integer id_item = Integer.parseInt(mysqlDataUnsync.getString("id_item"));

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        sqLiteDatabase.execSQL("UPDATE consumables SET status=3 WHERE id_item=" + id_item + ";");

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

    private void syncSendNewConsumablesBox(){

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM consumables WHERE status=0;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_new_consumables_box_android.php";

        ArrayList<String> arrayListIdItem = new ArrayList<>();
        ArrayList<String> arrayListDate = new ArrayList<>();
        ArrayList<String> arrayListChangeStock = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListIdItem.add(unsyncData.getString(0));
            arrayListDate.add(unsyncData.getString(1));
            arrayListChangeStock.add(unsyncData.getString(2));
        }

        for (int i = 0; i < arrayListIdItem.size(); i++){
            sendNewConsumablesBox(
                    arrayListIdItem.get(i),
                    arrayListDate.get(i),
                    arrayListChangeStock.get(i),
                    "1",
                    url);
        }
    }

        private void sendNewConsumablesBox( final String id_item, final String date, final String change_stock, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idItemHolder = id_item;
                String dateHolder = date;
                String changeStockHolder = change_stock;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id_item", idItemHolder));
                nameValuePairs.add(new BasicNameValuePair("date", dateHolder));
                nameValuePairs.add(new BasicNameValuePair("change_stock", changeStockHolder));
                nameValuePairs.add(new BasicNameValuePair("status", statusHolder));

                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(url);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();

                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }

                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(id_item, date, change_stock, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "UPDATE consumables SET status=1 WHERE id_item=" + id_item + " AND status=0;";
        sqLiteDatabase.execSQL(setStatusQuery);
    }




    private void syncGetNewCostBox(){

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_new_cost_box_android.php";

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

    private void syncGetEditedCostBox(){

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_edited_cost_box_android.php";

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
                        sqLiteDatabase.execSQL("UPDATE cost SET date='" + date + "', cost='" + cost + "', status=1 WHERE id_item=" + id_item + ";");

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

    private void syncGetDeletedCostBox(){

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_deleted_cost_box_android.php";

        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject mysqlDataUnsync = response.getJSONObject(i);

                        Integer id_item = Integer.parseInt(mysqlDataUnsync.getString("id_item"));

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        sqLiteDatabase.execSQL("UPDATE cost SET status=3 WHERE id_item=" + id_item + ";");

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

    private void syncSendNewCostBox(){

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM cost WHERE status=0;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_new_cost_box_android.php";

        ArrayList<String> arrayListIdItem = new ArrayList<>();
        ArrayList<String> arrayListDate = new ArrayList<>();
        ArrayList<String> arrayListCost = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListIdItem.add(unsyncData.getString(0));
            arrayListDate.add(unsyncData.getString(1));
            arrayListCost.add(unsyncData.getString(2));
        }

        for (int i = 0; i < arrayListIdItem.size(); i++){
            sendNewCostBox(
                    arrayListIdItem.get(i),
                    arrayListDate.get(i),
                    arrayListCost.get(i),
                    "1",
                    url);
        }
    }

        private void sendNewCostBox( final String id_item, final String date, final String cost, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idItemHolder = id_item;
                String dateHolder = date;
                String costHolder = cost;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id_item", idItemHolder));
                nameValuePairs.add(new BasicNameValuePair("date", dateHolder));
                nameValuePairs.add(new BasicNameValuePair("cost", costHolder));
                nameValuePairs.add(new BasicNameValuePair("status", statusHolder));

                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(url);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();

                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }

                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(id_item, date, cost, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "UPDATE cost SET status=1 WHERE id_item=" + id_item + " AND status=0;";
        sqLiteDatabase.execSQL(setStatusQuery);
    }




    private void syncGetNewItemBox() {

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_new_item_box_android.php";

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

    private void syncGetEditedItemBox() {

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_edited_item_box_android.php";

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
                        sqLiteDatabase.execSQL("UPDATE item SET name='" + name + "', category='" + category + "', status=1 WHERE id=" +  id + ";");

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

    private void syncGetDeletedItemBox() {

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_deleted_item_box_android.php";

        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject mysqlDataUnsync = response.getJSONObject(i);

                        Integer id = Integer.parseInt(mysqlDataUnsync.getString("id"));

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        sqLiteDatabase.execSQL("UPDATE item SET status=3 WHERE id=" +  id + ";");

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

    private void syncSendNewItemBox(){

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM item WHERE status=0;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_new_item_box_android.php";

        ArrayList<String> arrayListId = new ArrayList<>();
        ArrayList<String> arrayListName = new ArrayList<>();
        ArrayList<String> arrayListCategory = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListId.add(unsyncData.getString(0));
            arrayListName.add(unsyncData.getString(1));
            arrayListCategory.add(unsyncData.getString(2));
        }

        for (int i = 0; i < arrayListId.size(); i++){
            sendNewItemBox(
                    arrayListId.get(i),
                    arrayListName.get(i),
                    arrayListCategory.get(i),
                    "1",
                    url);
        }
    }

        private void sendNewItemBox( final String id, final String name, final String category, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idHolder = id;
                String nameHolder = name;
                String categoryHolder = category;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id", idHolder));
                nameValuePairs.add(new BasicNameValuePair("name", nameHolder));
                nameValuePairs.add(new BasicNameValuePair("category", categoryHolder));
                nameValuePairs.add(new BasicNameValuePair("status", statusHolder));

                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(url);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();

                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }

                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(id, name, category, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "UPDATE item SET status=1 WHERE id=" + id + ";";
        sqLiteDatabase.execSQL(setStatusQuery);
    }

    private void syncSendEditedItemBox(){

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM item WHERE status=2;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_edited_item_box_android.php";

        ArrayList<String> arrayListId = new ArrayList<>();
        ArrayList<String> arrayListName = new ArrayList<>();
        ArrayList<String> arrayListCategory = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListId.add(unsyncData.getString(0));
            arrayListName.add(unsyncData.getString(1));
            arrayListCategory.add(unsyncData.getString(2));
        }

        for (int i = 0; i < arrayListId.size(); i++){
            sendEditedItemBox(
                    arrayListId.get(i),
                    arrayListName.get(i),
                    arrayListCategory.get(i),
                    "1",
                    url);
        }
    }

        private void sendEditedItemBox( final String id, final String name, final String category, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idHolder = id;
                String nameHolder = name;
                String categoryHolder = category;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id", idHolder));
                nameValuePairs.add(new BasicNameValuePair("name", nameHolder));
                nameValuePairs.add(new BasicNameValuePair("category", categoryHolder));
                nameValuePairs.add(new BasicNameValuePair("status", statusHolder));

                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(url);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();

                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }

                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(id, name, category, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "UPDATE item SET status=1 WHERE id=" + id + ";";
        sqLiteDatabase.execSQL(setStatusQuery);
    }




    private void syncGetNewLinkBox(){

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_new_link_box_android.php";

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

    private void syncSendNewLinkBox(){

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM link WHERE status=0;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_new_link_box_android.php";

        ArrayList<String> arrayListIdItem1 = new ArrayList<>();
        ArrayList<String> arrayListIdItem2 = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListIdItem1.add(unsyncData.getString(0));
            arrayListIdItem2.add(unsyncData.getString(1));
        }

        for (int i = 0; i < arrayListIdItem1.size(); i++){
            sendNewLinkBox(
                    arrayListIdItem1.get(i),
                    arrayListIdItem2.get(i),
                    "1",
                    url);
        }
    }

        private void sendNewLinkBox( final String id_item_1, final String id_item_2, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idItem1Holder = id_item_1;
                String idItem2Holder = id_item_2;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id_item_1", idItem1Holder));
                nameValuePairs.add(new BasicNameValuePair("id_item_2", idItem2Holder));
                nameValuePairs.add(new BasicNameValuePair("status", statusHolder));

                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(url);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();

                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }

                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(id_item_1, id_item_2, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "UPDATE link SET status=1 WHERE id_item_1=" + id_item_1 + " AND id_item_2=" + id_item_2 + ";";
        sqLiteDatabase.execSQL(setStatusQuery);
    }





    private void syncGetNewLocationBox(){

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_new_location_box_android.php";

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

    private void syncGetEditedLocationBox(){

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_edited_location_box_android.php";

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
                        sqLiteDatabase.execSQL("UPDATE location SET id_subdivision=" + id_subdivision + ", id_box=" + id_box + " WHERE id_item=" + id_item + ";");

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

    private void syncGetDeletedLocationBox(){

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_deleted_location_box_android.php";

        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject mysqlDataUnsync = response.getJSONObject(i);

                        Integer id_item = Integer.parseInt(mysqlDataUnsync.getString("id_item"));

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        sqLiteDatabase.execSQL("DELETE FROM location WHERE id_item=" + id_item + ";");

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

    private void syncSendNewLocationBox(){

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM location WHERE status=0;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_new_location_box_android.php";

        ArrayList<String> arrayListIdItem = new ArrayList<>();
        ArrayList<String> arrayListIdSubdivision = new ArrayList<>();
        ArrayList<String> arrayListIdBox = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListIdItem.add(unsyncData.getString(0));
            arrayListIdSubdivision.add(unsyncData.getString(1));
            arrayListIdBox.add(unsyncData.getString(2));
        }

        for (int i = 0; i < arrayListIdItem.size(); i++){
            sendNewLocationBox(
                    arrayListIdItem.get(i),
                    arrayListIdSubdivision.get(i),
                    arrayListIdBox.get(i),
                    "1",
                    url);
        }
    }

        private void sendNewLocationBox( final String id_item, final String id_subdivision, final String id_box, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idItemHolder = id_item;
                String idSubdivisionHolder = id_subdivision;
                String idBoxHolder = id_box;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id_item", idItemHolder));
                nameValuePairs.add(new BasicNameValuePair("id_subdivision", idSubdivisionHolder));
                nameValuePairs.add(new BasicNameValuePair("id_box", idBoxHolder));
                nameValuePairs.add(new BasicNameValuePair("status", statusHolder));

                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(url);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();

                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }

                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(id_item, id_subdivision, id_box, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "UPDATE location SET status=1 WHERE id_item=" + id_item + ";";
        sqLiteDatabase.execSQL(setStatusQuery);
    }

    private void syncSendEditedLocationBox(){

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM location WHERE status=2;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_edited_location_box_android.php";

        ArrayList<String> arrayListIdItem = new ArrayList<>();
        ArrayList<String> arrayListIdSubdivision = new ArrayList<>();
        ArrayList<String> arrayListIdBox = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListIdItem.add(unsyncData.getString(0));
            arrayListIdSubdivision.add(unsyncData.getString(1));
            arrayListIdBox.add(unsyncData.getString(2));
        }

        for (int i = 0; i < arrayListIdItem.size(); i++){
            sendEditedLocationBox(
                    arrayListIdItem.get(i),
                    arrayListIdSubdivision.get(i),
                    arrayListIdBox.get(i),
                    "1",
                    url);
        }
    }

        private void sendEditedLocationBox( final String id_item, final String id_subdivision, final String id_box, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idItemHolder = id_item;
                String idSubdivisionHolder = id_subdivision;
                String idBoxHolder = id_box;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id_item", idItemHolder));
                nameValuePairs.add(new BasicNameValuePair("id_subdivision", idSubdivisionHolder));
                nameValuePairs.add(new BasicNameValuePair("id_box", idBoxHolder));
                nameValuePairs.add(new BasicNameValuePair("status", statusHolder));

                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(url);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();

                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }

                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(id_item, id_subdivision, id_box, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "UPDATE location SET status=1 WHERE id_item=" + id_item + ";";
        sqLiteDatabase.execSQL(setStatusQuery);
    }




    private void syncGetNewNoConsumablesBox(){

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_new_noconsumables_box_android.php";

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
                        sqLiteDatabase.execSQL("INSERT INTO noconsumables (id_item, date, state, status) VALUES (" + id_item + ", '" + date + "', " + state + ", 1 );");

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

    private void syncGetEditedNoConsumablesBox(){

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_edited_noconsumables_box_android.php";

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
                        sqLiteDatabase.execSQL("UPDATE noconsumables SET date='" + date + "', state=" + state + ", status=1 WHERE id_item=" + id_item +";");

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

    private void syncGetDeletedNoConsumablesBox(){

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_deleted_noconsumables_box_android.php";

        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject mysqlDataUnsync = response.getJSONObject(i);

                        Integer id_item = Integer.parseInt(mysqlDataUnsync.getString("id_item"));

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        sqLiteDatabase.execSQL("UPDATE noconsumables SET status=3 WHERE id_item=" + id_item +";");

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

    private void syncSendNewNoConsumablesBox(){

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM noconsumables WHERE status=0;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_new_noconsumables_box_android.php";

        ArrayList<String> arrayListIdItem = new ArrayList<>();
        ArrayList<String> arrayListDate = new ArrayList<>();
        ArrayList<String> arrayListState = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListIdItem.add(unsyncData.getString(0));
            arrayListDate.add(unsyncData.getString(1));
            arrayListState.add(unsyncData.getString(2));
        }

        for (int i = 0; i < arrayListIdItem.size(); i++){
            sendNewNoConsumablesBox(
                    arrayListIdItem.get(i),
                    arrayListDate.get(i),
                    arrayListState.get(i),
                    "1",
                    url);
        }
    }

        private void sendNewNoConsumablesBox( final String id_item, final String date, final String state, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idItemHolder = id_item;
                String dateHolder = date;
                String stateHolder = state;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id_item", idItemHolder));
                nameValuePairs.add(new BasicNameValuePair("date", dateHolder));
                nameValuePairs.add(new BasicNameValuePair("state", stateHolder));
                nameValuePairs.add(new BasicNameValuePair("status", statusHolder));

                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(url);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();

                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }

                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(id_item, date, state, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "UPDATE noconsumables SET status=1 WHERE id_item=" + id_item + " AND status=0;";
        sqLiteDatabase.execSQL(setStatusQuery);
    }





    private void syncGetNewSubdivisionBox() {

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_new_subdivision_box_android.php";

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

    private void syncGetEditedSubdivisionBox() {

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_edited_subdivision_box_android.php";

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
                        sqLiteDatabase.execSQL("UPDATE subdivision SET subdivision='" + subdivision + "', status=1 WHERE id=" + id + ";");

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

    private void syncGetDeletedSubdivisionBox() {

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_deleted_subdivision_box_android.php";

        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject mysqlDataUnsync = response.getJSONObject(i);

                        Integer id = Integer.parseInt(mysqlDataUnsync.getString("id"));

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        sqLiteDatabase.execSQL("UPDATE subdivision SET status=3 WHERE id=" + id + ";");

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

    private void syncSendNewSubdivisionBox(){

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM subdivision WHERE status=0;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_new_subdivision_box_android.php";

        ArrayList<String> arrayListId = new ArrayList<>();
        ArrayList<String> arrayListSubdivision = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListId.add(unsyncData.getString(0));
            arrayListSubdivision.add(unsyncData.getString(1));
        }

        for (int i = 0; i < arrayListId.size(); i++){
            sendNewSubdivisionBox(
                    arrayListId.get(i),
                    arrayListSubdivision.get(i),
                    "1",
                    url);
        }
    }

        private void sendNewSubdivisionBox( final String id, final String subdivision, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idHolder = id;
                String subdivisionHolder = subdivision;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id", idHolder));
                nameValuePairs.add(new BasicNameValuePair("subdivision", subdivisionHolder));
                nameValuePairs.add(new BasicNameValuePair("status", statusHolder));

                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(url);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();

                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }

                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(id, subdivision, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "UPDATE subdivision SET status=1 WHERE id=" + id + ";";
        sqLiteDatabase.execSQL(setStatusQuery);
    }

    private void syncSendEditedSubdivisionBox(){

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM subdivision WHERE status=2;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_edited_subdivision_box_android.php";

        ArrayList<String> arrayListId = new ArrayList<>();
        ArrayList<String> arrayListSubdivision = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListId.add(unsyncData.getString(0));
            arrayListSubdivision.add(unsyncData.getString(1));
        }

        for (int i = 0; i < arrayListId.size(); i++){
            sendEditedSubdivisionBox(
                    arrayListId.get(i),
                    arrayListSubdivision.get(i),
                    "1",
                    url);
        }
    }

        private void sendEditedSubdivisionBox( final String id, final String subdivision, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idHolder = id;
                String subdivisionHolder = subdivision;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id", idHolder));
                nameValuePairs.add(new BasicNameValuePair("subdivision", subdivisionHolder));
                nameValuePairs.add(new BasicNameValuePair("status", statusHolder));

                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(url);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();

                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }

                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(id, subdivision, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "UPDATE subdivision SET status=1 WHERE id=" + id + ";";
        sqLiteDatabase.execSQL(setStatusQuery);
    }




    private void syncGetNewEntryWallet() {

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_new_entry_wallet_android.php";

        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject mysqlDataUnsync = response.getJSONObject(i);

                        Integer id = Integer.parseInt(mysqlDataUnsync.getString("id"));
                        String date = mysqlDataUnsync.getString("date");
                        String description = mysqlDataUnsync.getString("description");
                        Double value = Double.parseDouble(mysqlDataUnsync.getString("value"));
                        Integer id_person = Integer.parseInt(mysqlDataUnsync.getString("id_person"));
                        Integer id_gift = Integer.parseInt(mysqlDataUnsync.getString("id_gift"));
                        String repay = mysqlDataUnsync.getString("repay");
                        String type = mysqlDataUnsync.getString("type");

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        String insertQuery = "INSERT INTO wallet (id, date, description, value, id_person, id_gift, repay, type, status ) VALUES ("
                                + id
                                + ", '"
                                + date
                                + "', '"
                                + description
                                + "', '"
                                + value
                                + "', '"
                                + id_person
                                + "', '"
                                + id_gift
                                + "', '"
                                + repay
                                + "', '"
                                + type
                                + "', "
                                + 1
                                + ");";
                        Log.i(TAG, "MYSQL: " + insertQuery);
                        sqLiteDatabase.execSQL(insertQuery);

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

    private void syncGetEditedEntryWallet() {

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_edited_entry_wallet_android.php";

        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject mysqlDataUnsync = response.getJSONObject(i);

                        Integer id = Integer.parseInt(mysqlDataUnsync.getString("id"));
                        String date = mysqlDataUnsync.getString("date");
                        String description = mysqlDataUnsync.getString("description");
                        Double value = Double.parseDouble(mysqlDataUnsync.getString("value"));
                        Integer id_person = Integer.parseInt(mysqlDataUnsync.getString("id_person"));
                        Integer id_gift = Integer.parseInt(mysqlDataUnsync.getString("id_gift"));
                        String repay = mysqlDataUnsync.getString("repay");
                        String type = mysqlDataUnsync.getString("type");

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        String insertQuery = "UPDATE wallet SET date='"
                                + date
                                + "', description='"
                                + description
                                + "', value='"
                                + value
                                + "', id_person="
                                + id_person
                                + ", id_gift="
                                + id_gift
                                + ", repay='"
                                + repay
                                + "', type='"
                                + type
                                + "', status=1 WHERE id="
                                + id
                                + ";";
                        sqLiteDatabase.execSQL(insertQuery);

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

    private void syncGetDeletedEntryWallet() {

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_deleted_entry_wallet_android.php";

        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject mysqlDataUnsync = response.getJSONObject(i);

                        Integer id = Integer.parseInt(mysqlDataUnsync.getString("id"));

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        String deleteQuery = "UPDATE wallet SET status=3 WHERE id=" + id + ";";
                        sqLiteDatabase.execSQL(deleteQuery);

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

    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null){
            mRequestQueue.cancelAll(REQUESTTAG);
        }
    }

    private void syncSendNewEntryWallet(){

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM wallet WHERE status=0;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_new_entry_wallet_android.php";

        Log.i(TAG, "TAMANHO: " + unsyncData.getCount());

        ArrayList<String> arrayListUnsyncDataId = new ArrayList<>();
        ArrayList<String> arrayListUnsyncDataDate = new ArrayList<>();
        ArrayList<String> arrayListUnsyncDataDescription = new ArrayList<>();
        ArrayList<String> arrayListUnsyncDataValue = new ArrayList<>();
        ArrayList<String> arrayListUnsyncDataIdPerson = new ArrayList<>();
        ArrayList<String> arrayListUnsyncDataIdGift = new ArrayList<>();
        ArrayList<String> arrayListUnsyncDataRepay = new ArrayList<>();
        ArrayList<String> arrayListUnsyncDataType = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListUnsyncDataId.add(unsyncData.getString(0));
            arrayListUnsyncDataDate.add(unsyncData.getString(1));
            arrayListUnsyncDataDescription.add(unsyncData.getString(2));
            arrayListUnsyncDataValue.add(unsyncData.getString(3));
            arrayListUnsyncDataIdPerson.add(unsyncData.getString(4));
            arrayListUnsyncDataIdGift.add(unsyncData.getString(5));
            arrayListUnsyncDataRepay.add(unsyncData.getString(6));
            arrayListUnsyncDataType.add(unsyncData.getString(7));
        }

        for (int i = 0; i < arrayListUnsyncDataDate.size(); i++){
            sendNewEntryWallet(
                    arrayListUnsyncDataId.get(i),
                    arrayListUnsyncDataDate.get(i),
                    arrayListUnsyncDataDescription.get(i),
                    arrayListUnsyncDataValue.get(i),
                    arrayListUnsyncDataIdPerson.get(i),
                    arrayListUnsyncDataIdGift.get(i),
                    arrayListUnsyncDataRepay.get(i),
                    arrayListUnsyncDataType.get(i),
                    "1",
                    url);
        }
    }

        private void sendNewEntryWallet( final String id, final String date, final String description, final String value, final String id_person, final String id_gift, final String repay, final String type, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idHolder = id;
                String dateHolder = date;
                String descriptionHolder = description;
                String valueHolder = value;
                String idPersonHolder = id_person;
                String idGiftHolder = id_gift;
                String repayHolder = repay;
                String typeHolder = type;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id", idHolder));
                nameValuePairs.add(new BasicNameValuePair("date", dateHolder));
                nameValuePairs.add(new BasicNameValuePair("description", descriptionHolder));
                nameValuePairs.add(new BasicNameValuePair("value", valueHolder));
                nameValuePairs.add(new BasicNameValuePair("id_person", idPersonHolder));
                nameValuePairs.add(new BasicNameValuePair("id_gift", idGiftHolder));
                nameValuePairs.add(new BasicNameValuePair("repay", repayHolder));
                nameValuePairs.add(new BasicNameValuePair("type", typeHolder));
                nameValuePairs.add(new BasicNameValuePair("status", statusHolder));

                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(url);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();

                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }

                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(id, date, description, value, id_person, id_gift, repay, type, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "UPDATE wallet SET status=1 WHERE status=0 AND id=" + id + ";";
        sqLiteDatabase.execSQL(setStatusQuery);
    }

    private void syncSendEditedEntryWallet(){
        sqLiteDatabase = dataBase.getReadableDatabase();
        String getEditedData = "SELECT * FROM wallet WHERE status=2;";
        Cursor editedData = sqLiteDatabase.rawQuery(getEditedData, null);

        String url = IpAddress + "android/sync_send_edited_entry_wallet_android.php";

        ArrayList<String> arrayListEditedDataId = new ArrayList<>();
        ArrayList<String> arrayListEditedDataDate = new ArrayList<>();
        ArrayList<String> arrayListEditedDataDescription = new ArrayList<>();
        ArrayList<String> arrayListEditedDataValue = new ArrayList<>();
        ArrayList<String> arrayListEditedDataIdPerson = new ArrayList<>();
        ArrayList<String> arrayListEditedDataIdGift = new ArrayList<>();
        ArrayList<String> arrayListEditedDataRepay = new ArrayList<>();
        ArrayList<String> arrayListEditedDataType = new ArrayList<>();

        while (editedData.moveToNext()){
            arrayListEditedDataId.add(editedData.getString(0));
            arrayListEditedDataDate.add(editedData.getString(1));
            arrayListEditedDataDescription.add(editedData.getString(2));
            arrayListEditedDataValue.add(editedData.getString(3));
            arrayListEditedDataIdPerson.add(editedData.getString(4));
            arrayListEditedDataIdGift.add(editedData.getString(5));
            arrayListEditedDataRepay.add(editedData.getString(6));
            arrayListEditedDataType.add(editedData.getString(7));
        }

        for (int i = 0; i < arrayListEditedDataDate.size(); i++){
            sendEditedEntryWallet(
                    arrayListEditedDataId.get(i),
                    arrayListEditedDataDate.get(i),
                    arrayListEditedDataDescription.get(i),
                    arrayListEditedDataValue.get(i),
                    arrayListEditedDataIdPerson.get(i),
                    arrayListEditedDataIdGift.get(i),
                    arrayListEditedDataRepay.get(i),
                    arrayListEditedDataType.get(i),
                    "1",
                    url);
        }
    }

        private void sendEditedEntryWallet( final String id, final String date, final String description, final String value, final String id_person, final String id_gift, final String repay, final String type, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idHolder = id;
                String dateHolder = date;
                String descriptionHolder = description;
                String valueHolder = value;
                String idPersonHolder = id_person;
                String idGiftHolder = id_gift;
                String repayHolder = repay;
                String typeHolder = type;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id", idHolder));
                nameValuePairs.add(new BasicNameValuePair("date", dateHolder));
                nameValuePairs.add(new BasicNameValuePair("description", descriptionHolder));
                nameValuePairs.add(new BasicNameValuePair("value", valueHolder));
                nameValuePairs.add(new BasicNameValuePair("id_person", idPersonHolder));
                nameValuePairs.add(new BasicNameValuePair("id_gift", idGiftHolder));
                nameValuePairs.add(new BasicNameValuePair("repay", repayHolder));
                nameValuePairs.add(new BasicNameValuePair("type", typeHolder));
                nameValuePairs.add(new BasicNameValuePair("status", statusHolder));

                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(url);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();

                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }

                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(id, date, description, value, id_person, id_gift, repay, type, status );

        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "UPDATE wallet SET status=1 WHERE status=2 AND id=" + id + ";";
        sqLiteDatabase.execSQL(setStatusQuery);
    }

    private void syncSendDeletedEntryWallet(){
        sqLiteDatabase = dataBase.getReadableDatabase();
        String getDeletedData = "SELECT * FROM wallet WHERE status=3;";
        Cursor deletedData = sqLiteDatabase.rawQuery(getDeletedData, null);

        String url = IpAddress + "android/sync_send_deleted_entry_wallet_android.php";

        ArrayList<String> arrayListDeletedDataId = new ArrayList<>();
        ArrayList<String> arrayListDeletedDataDate = new ArrayList<>();
        ArrayList<String> arrayListDeletedDataDescription = new ArrayList<>();
        ArrayList<String> arrayListDeletedDataValue = new ArrayList<>();
        ArrayList<String> arrayListDeletedDataIdPerson = new ArrayList<>();
        ArrayList<String> arrayListDeletedDataIdGift = new ArrayList<>();
        ArrayList<String> arrayListDeletedDataRepay = new ArrayList<>();
        ArrayList<String> arrayListDeletedDataType = new ArrayList<>();

        while (deletedData.moveToNext()){
            arrayListDeletedDataId.add(deletedData.getString(0));
            arrayListDeletedDataDate.add(deletedData.getString(1));
            arrayListDeletedDataDescription.add(deletedData.getString(2));
            arrayListDeletedDataValue.add(deletedData.getString(3));
            arrayListDeletedDataIdPerson.add(deletedData.getString(4));
            arrayListDeletedDataIdGift.add(deletedData.getString(5));
            arrayListDeletedDataRepay.add(deletedData.getString(6));
            arrayListDeletedDataType.add(deletedData.getString(7));
        }

        for (int i = 0; i < arrayListDeletedDataDate.size(); i++){
            sendDeletedEntryWallet(
                    arrayListDeletedDataId.get(i),
                    arrayListDeletedDataDate.get(i),
                    arrayListDeletedDataDescription.get(i),
                    arrayListDeletedDataValue.get(i),
                    arrayListDeletedDataIdPerson.get(i),
                    arrayListDeletedDataIdGift.get(i),
                    arrayListDeletedDataRepay.get(i),
                    arrayListDeletedDataType.get(i),
                    "1",
                    url);
        }
    }

        private void sendDeletedEntryWallet( final String id, final String date, final String description, final String value, final String id_person, final String id_gift, final String repay, final String type, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idHolder = id;
                String dateHolder = date;
                String descriptionHolder = description;
                String valueHolder = value;
                String idPersonHolder = id_person;
                String idGiftHolder = id_gift;
                String repayHolder = repay;
                String typeHolder = type;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id", idHolder));
                nameValuePairs.add(new BasicNameValuePair("date", dateHolder));
                nameValuePairs.add(new BasicNameValuePair("description", descriptionHolder));
                nameValuePairs.add(new BasicNameValuePair("value", valueHolder));
                nameValuePairs.add(new BasicNameValuePair("id_person", idPersonHolder));
                nameValuePairs.add(new BasicNameValuePair("id_gift", idGiftHolder));
                nameValuePairs.add(new BasicNameValuePair("repay", repayHolder));
                nameValuePairs.add(new BasicNameValuePair("type", typeHolder));
                nameValuePairs.add(new BasicNameValuePair("status", statusHolder));

                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(url);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();

                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }

                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(id, date, description, value, id_person, id_gift, repay, type, status );
    }



}
