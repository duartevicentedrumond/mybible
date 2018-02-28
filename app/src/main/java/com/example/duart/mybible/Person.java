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

public class Person extends AppCompatActivity {

    private static final String TAG = Person.class.getName();
    private static final String REQUESTTAG = "string request person";
    private ListView listViewPerson;
    private SQLiteOpenHelper dataBase;
    private SQLiteDatabase sqLiteDatabase;
    private RequestQueue mRequestQueue;

    public String IpAddress = "http://incubo.serveo.net/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        listViewPerson = (ListView) findViewById(R.id.list_view_contacts);
        dataBase = new mybibleDataBase(this);

        printPersonList();
        clickableListView();
    }

    //necessary to show buttons on action bar menu
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_person, menu);
            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {

                case R.id.action_add_new_item:
                    startActivity( new Intent( Person.this, NewPerson.class ) );
                    return true;

                case R.id.action_sync:
                    syncGetNewPerson();
                    syncGetNewBirthday();
                    syncGetNewPhoneNumber();
                    syncGetNewEmail();
                    syncSendNewPerson();
                    syncSendNewBirthday();
                    syncSendNewPhoneNumber();
                    syncSendNewEmail();
                    syncSendEditedPerson();
                    syncSendEditedBirthday();
                    syncSendEditedPhoneNumber();
                    syncSendEditedEmail();
                    syncSendDeletedPerson();
                    syncSendDeletedBirthday();
                    syncSendDeletedPhoneNumber();
                    syncSendDeletedEmail();
                    return true;

                default:
                    // If we got here, the user's action was not recognized.
                    // Invoke the superclass to handle it.
                    return super.onOptionsItemSelected(item);

            }
        }

    public void printPersonList(){
        ArrayList<String> arrayListPerson = new ArrayList<>();
        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListPerson);

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor data = sqLiteDatabase.rawQuery("SELECT id, name FROM person WHERE status!=3", null);

        while (data.moveToNext()){
            arrayListPerson.add("#" + data.getString(0) + " " + data.getString(1));
        }
        listViewPerson.setAdapter(listAdapter);
    }

    public void clickableListView(){
        listViewPerson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String personSelected = adapterView.getItemAtPosition(i).toString();
                Intent intent = new Intent(Person.this, ViewPerson.class);
                intent.putExtra("personSelected", personSelected);
                startActivity( intent );
            }
        });
    }

    private void syncGetNewPerson() {

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_new_person_android.php";

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
                        String complete_name = mysqlDataUnsync.getString("complete_name");

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        String insertQuery = "INSERT INTO person (id, name, complete_name, status ) VALUES ("
                                + id
                                + ", '"
                                + name
                                + "', '"
                                + complete_name
                                + "', "
                                + 1
                                + ");";
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

    private void syncGetNewBirthday() {

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_new_birthday_android.php";

        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject mysqlDataUnsync = response.getJSONObject(i);

                        Integer id_person = Integer.parseInt(mysqlDataUnsync.getString("id_person"));
                        String date = mysqlDataUnsync.getString("date");

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        String insertQuery = "INSERT INTO birthday (id_person, date, status ) VALUES ("
                                + id_person
                                + ", '"
                                + date
                                + "', "
                                + 1
                                + ");";
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

    private void syncGetNewPhoneNumber() {

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_new_phone_number_android.php";

        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject mysqlDataUnsync = response.getJSONObject(i);

                        Integer id_person = Integer.parseInt(mysqlDataUnsync.getString("id_person"));
                        String number = mysqlDataUnsync.getString("number");
                        String category = mysqlDataUnsync.getString("category");

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        String insertQuery = "INSERT INTO phone (id_person, number, category, status ) VALUES ("
                                + id_person
                                + ", '"
                                + number
                                + "', '"
                                + category
                                + "', "
                                + 1
                                + ");";
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

    private void syncGetNewEmail() {

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_new_email_android.php";

        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject mysqlDataUnsync = response.getJSONObject(i);

                        Integer id_person = Integer.parseInt(mysqlDataUnsync.getString("id_person"));
                        String email = mysqlDataUnsync.getString("number");
                        String category = mysqlDataUnsync.getString("category");

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        String insertQuery = "INSERT INTO email (id_person, email, category, status ) VALUES ("
                                + id_person
                                + ", '"
                                + email
                                + "', '"
                                + category
                                + "', "
                                + 1
                                + ");";
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

    private void syncSendNewPerson() {

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM person WHERE status=0;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_new_person_android.php";

        ArrayList<String> arrayListId = new ArrayList<>();
        ArrayList<String> arrayListName = new ArrayList<>();
        ArrayList<String> arrayListCompleteName = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListId.add(unsyncData.getString(0));
            arrayListName.add(unsyncData.getString(1));
            arrayListCompleteName.add(unsyncData.getString(2));
        }

        for (int i = 0; i < arrayListId.size(); i++){
            sendNewPerson(
                    arrayListId.get(i),
                    arrayListName.get(i),
                    arrayListCompleteName.get(i),
                    "1",
                    url);
        }

    }

        private void sendNewPerson( final String id, final String name, final String complete_name, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idHolder = id;
                String nameHolder = name;
                String completeNameHolder = complete_name;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id", idHolder));
                nameValuePairs.add(new BasicNameValuePair("name", nameHolder));
                nameValuePairs.add(new BasicNameValuePair("complete_name", completeNameHolder));
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
        sendPostReqAsyncTask.execute(id, name, complete_name, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "UPDATE person SET status=1 WHERE status=0 AND id=" + id + ";";
        sqLiteDatabase.execSQL(setStatusQuery);
    }

    private void syncSendNewBirthday() {

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM birthday WHERE status=0;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_new_birthday_android.php";

        ArrayList<String> arrayListIdPerson = new ArrayList<>();
        ArrayList<String> arrayListDate = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListIdPerson.add(unsyncData.getString(0));
            arrayListDate.add(unsyncData.getString(1));
        }

        for (int i = 0; i < arrayListIdPerson.size(); i++){
            sendNewBirthday(
                    arrayListIdPerson.get(i),
                    arrayListDate.get(i),
                    "1",
                    url);
        }

    }

        private void sendNewBirthday( final String id_person, final String date, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idPersonHolder = id_person;
                String dateHolder = date;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id_person", idPersonHolder));
                nameValuePairs.add(new BasicNameValuePair("date", dateHolder));
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
        sendPostReqAsyncTask.execute(id_person, date, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "UPDATE birthday SET status=1 WHERE status=0 AND id_person=" + id_person + ";";
        sqLiteDatabase.execSQL(setStatusQuery);
    }

    private void syncSendNewPhoneNumber() {

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM phone WHERE status=0;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_new_phone_number_android.php";

        ArrayList<String> arrayListIdPerson = new ArrayList<>();
        ArrayList<String> arrayListNumber = new ArrayList<>();
        ArrayList<String> arrayListCategory = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListIdPerson.add(unsyncData.getString(0));
            arrayListNumber.add(unsyncData.getString(1));
            arrayListCategory.add(unsyncData.getString(2));
        }

        for (int i = 0; i < arrayListIdPerson.size(); i++){
            sendNewPhoneNumber(
                    arrayListIdPerson.get(i),
                    arrayListNumber.get(i),
                    arrayListCategory.get(i),
                    "1",
                    url);
        }

    }

        private void sendNewPhoneNumber( final String id_person, final String number, final String category, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idPersonHolder = id_person;
                String numberHolder = number;
                String categoryHolder = category;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id_person", idPersonHolder));
                nameValuePairs.add(new BasicNameValuePair("number", numberHolder));
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
        sendPostReqAsyncTask.execute(id_person, number, category, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "UPDATE phone SET status=1 WHERE status=0 AND id_person=" + id_person + ";";
        sqLiteDatabase.execSQL(setStatusQuery);
    }

    private void syncSendNewEmail() {

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM email WHERE status=0;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_new_email_android.php";

        ArrayList<String> arrayListIdPerson = new ArrayList<>();
        ArrayList<String> arrayListEmail = new ArrayList<>();
        ArrayList<String> arrayListCategory = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListIdPerson.add(unsyncData.getString(0));
            arrayListEmail.add(unsyncData.getString(1));
            arrayListCategory.add(unsyncData.getString(2));
        }

        for (int i = 0; i < arrayListIdPerson.size(); i++){
            sendNewEmail(
                    arrayListIdPerson.get(i),
                    arrayListEmail.get(i),
                    arrayListCategory.get(i),
                    "1",
                    url);
        }

    }

        private void sendNewEmail( final String id_person, final String email, final String category, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idPersonHolder = id_person;
                String emailHolder = email;
                String categoryHolder = category;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id_person", idPersonHolder));
                nameValuePairs.add(new BasicNameValuePair("email", emailHolder));
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
        sendPostReqAsyncTask.execute(id_person, email, category, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "UPDATE email SET status=1 WHERE status=0 AND id_person=" + id_person + ";";
        sqLiteDatabase.execSQL(setStatusQuery);
    }

    private void syncSendEditedPerson() {

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM person WHERE status=2;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_edited_person_android.php";

        ArrayList<String> arrayListId = new ArrayList<>();
        ArrayList<String> arrayListName = new ArrayList<>();
        ArrayList<String> arrayListCompleteName = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListId.add(unsyncData.getString(0));
            arrayListName.add(unsyncData.getString(1));
            arrayListCompleteName.add(unsyncData.getString(2));
        }

        for (int i = 0; i < arrayListId.size(); i++){
            sendEditedPerson(
                    arrayListId.get(i),
                    arrayListName.get(i),
                    arrayListCompleteName.get(i),
                    "1",
                    url);
        }

    }

        private void sendEditedPerson( final String id, final String name, final String complete_name, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idHolder = id;
                String nameHolder = name;
                String completeNameHolder = complete_name;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id", idHolder));
                nameValuePairs.add(new BasicNameValuePair("name", nameHolder));
                nameValuePairs.add(new BasicNameValuePair("complete_name", completeNameHolder));
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
        sendPostReqAsyncTask.execute(id, name, complete_name, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "UPDATE person SET status=1 WHERE status=2 AND id=" + id + ";";
        sqLiteDatabase.execSQL(setStatusQuery);
    }

    private void syncSendEditedBirthday() {

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM birthday WHERE status=2;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_edited_birthday_android.php";

        ArrayList<String> arrayListIdPerson = new ArrayList<>();
        ArrayList<String> arrayListDate = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListIdPerson.add(unsyncData.getString(0));
            arrayListDate.add(unsyncData.getString(1));
        }

        for (int i = 0; i < arrayListIdPerson.size(); i++){
            sendEditedBirthday(
                    arrayListIdPerson.get(i),
                    arrayListDate.get(i),
                    "1",
                    url);
        }

    }

        private void sendEditedBirthday( final String id_person, final String date, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idPersonHolder = id_person;
                String dateHolder = date;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id_person", idPersonHolder));
                nameValuePairs.add(new BasicNameValuePair("date", dateHolder));
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
        sendPostReqAsyncTask.execute(id_person, date, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "UPDATE birthday SET status=1 WHERE status=2 AND id_person=" + id_person + ";";
        sqLiteDatabase.execSQL(setStatusQuery);
    }

    private void syncSendEditedPhoneNumber() {

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM phone WHERE status=2;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_edited_phone_number_android.php";

        ArrayList<String> arrayListIdPerson = new ArrayList<>();
        ArrayList<String> arrayListNumber = new ArrayList<>();
        ArrayList<String> arrayListCategory = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListIdPerson.add(unsyncData.getString(0));
            arrayListNumber.add(unsyncData.getString(1));
            arrayListCategory.add(unsyncData.getString(2));
        }

        for (int i = 0; i < arrayListIdPerson.size(); i++){
            sendEditedPhoneNumber(
                    arrayListIdPerson.get(i),
                    arrayListNumber.get(i),
                    arrayListCategory.get(i),
                    "1",
                    url);
        }

    }

        private void sendEditedPhoneNumber( final String id_person, final String number, final String category, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idPersonHolder = id_person;
                String numberHolder = number;
                String categoryHolder = category;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id_person", idPersonHolder));
                nameValuePairs.add(new BasicNameValuePair("number", numberHolder));
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
        sendPostReqAsyncTask.execute(id_person, number, category, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "UPDATE phone SET status=1 WHERE status=2 AND id_person=" + id_person + ";";
        sqLiteDatabase.execSQL(setStatusQuery);
    }

    private void syncSendEditedEmail() {

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM email WHERE status=2;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_edited_email_android.php";

        ArrayList<String> arrayListIdPerson = new ArrayList<>();
        ArrayList<String> arrayListEmail = new ArrayList<>();
        ArrayList<String> arrayListCategory = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListIdPerson.add(unsyncData.getString(0));
            arrayListEmail.add(unsyncData.getString(1));
            arrayListCategory.add(unsyncData.getString(2));
        }

        for (int i = 0; i < arrayListIdPerson.size(); i++){
            sendEditedEmail(
                    arrayListIdPerson.get(i),
                    arrayListEmail.get(i),
                    arrayListCategory.get(i),
                    "1",
                    url);
        }

    }

        private void sendEditedEmail( final String id_person, final String email, final String category, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idPersonHolder = id_person;
                String emailHolder = email;
                String categoryHolder = category;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id_person", idPersonHolder));
                nameValuePairs.add(new BasicNameValuePair("email", emailHolder));
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
        sendPostReqAsyncTask.execute(id_person, email, category, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "UPDATE email SET status=1 WHERE status=2 AND id_person=" + id_person + ";";
        sqLiteDatabase.execSQL(setStatusQuery);
    }

    private void syncSendDeletedPerson() {

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM person WHERE status=3;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_deleted_person_android.php";

        ArrayList<String> arrayListId = new ArrayList<>();
        ArrayList<String> arrayListName = new ArrayList<>();
        ArrayList<String> arrayListCompleteName = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListId.add(unsyncData.getString(0));
            arrayListName.add(unsyncData.getString(1));
            arrayListCompleteName.add(unsyncData.getString(2));
        }

        for (int i = 0; i < arrayListId.size(); i++){
            sendDeletedPerson(
                    arrayListId.get(i),
                    arrayListName.get(i),
                    arrayListCompleteName.get(i),
                    "1",
                    url);
        }

    }

        private void sendDeletedPerson( final String id, final String name, final String complete_name, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idHolder = id;
                String nameHolder = name;
                String completeNameHolder = complete_name;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id", idHolder));
                nameValuePairs.add(new BasicNameValuePair("name", nameHolder));
                nameValuePairs.add(new BasicNameValuePair("complete_name", completeNameHolder));
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
        sendPostReqAsyncTask.execute(id, name, complete_name, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "DELETE FROM person WHERE status=3 AND id=" + id + ";";
        sqLiteDatabase.execSQL(setStatusQuery);
    }

    private void syncSendDeletedBirthday() {

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM birthday WHERE status=3;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_deleted_birthday_android.php";

        ArrayList<String> arrayListIdPerson = new ArrayList<>();
        ArrayList<String> arrayListDate = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListIdPerson.add(unsyncData.getString(0));
            arrayListDate.add(unsyncData.getString(1));
        }

        for (int i = 0; i < arrayListIdPerson.size(); i++){
            sendDeletedBirthday(
                    arrayListIdPerson.get(i),
                    arrayListDate.get(i),
                    "1",
                    url);
        }

    }

        private void sendDeletedBirthday( final String id_person, final String date, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idPersonHolder = id_person;
                String dateHolder = date;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id_person", idPersonHolder));
                nameValuePairs.add(new BasicNameValuePair("date", dateHolder));
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
        sendPostReqAsyncTask.execute(id_person, date, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "DELETE FROM birthday WHERE status=3 AND id_person=" + id_person + ";";
        sqLiteDatabase.execSQL(setStatusQuery);
    }

    private void syncSendDeletedPhoneNumber() {

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM phone WHERE status=3;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_deleted_phone_number_android.php";

        ArrayList<String> arrayListIdPerson = new ArrayList<>();
        ArrayList<String> arrayListNumber = new ArrayList<>();
        ArrayList<String> arrayListCategory = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListIdPerson.add(unsyncData.getString(0));
            arrayListNumber.add(unsyncData.getString(1));
            arrayListCategory.add(unsyncData.getString(2));
        }

        for (int i = 0; i < arrayListIdPerson.size(); i++){
            sendDeletedPhoneNumber(
                    arrayListIdPerson.get(i),
                    arrayListNumber.get(i),
                    arrayListCategory.get(i),
                    "1",
                    url);
        }

    }

        private void sendDeletedPhoneNumber( final String id_person, final String number, final String category, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idPersonHolder = id_person;
                String numberHolder = number;
                String categoryHolder = category;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id_person", idPersonHolder));
                nameValuePairs.add(new BasicNameValuePair("number", numberHolder));
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
        sendPostReqAsyncTask.execute(id_person, number, category, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "DELETE FROM phone WHERE status=3 AND id_person=" + id_person + " AND category='" + category + "';";
        sqLiteDatabase.execSQL(setStatusQuery);
    }

    private void syncSendDeletedEmail() {

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM email WHERE status=3;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = IpAddress + "android/sync_send_deleted_email_android.php";

        ArrayList<String> arrayListIdPerson = new ArrayList<>();
        ArrayList<String> arrayListEmail = new ArrayList<>();
        ArrayList<String> arrayListCategory = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListIdPerson.add(unsyncData.getString(0));
            arrayListEmail.add(unsyncData.getString(1));
            arrayListCategory.add(unsyncData.getString(2));
        }

        for (int i = 0; i < arrayListIdPerson.size(); i++){
            sendDeletedEmail(
                    arrayListIdPerson.get(i),
                    arrayListEmail.get(i),
                    arrayListCategory.get(i),
                    "1",
                    url);
        }

    }

        private void sendDeletedEmail( final String id_person, final String email, final String category, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idPersonHolder = id_person;
                String emailHolder = email;
                String categoryHolder = category;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id_person", idPersonHolder));
                nameValuePairs.add(new BasicNameValuePair("email", emailHolder));
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
        sendPostReqAsyncTask.execute(id_person, email, category, status );
        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "DELETE FROM email WHERE status=3 AND id_person=" + id_person + " AND category='" + category + "';";
        sqLiteDatabase.execSQL(setStatusQuery);
    }

}
