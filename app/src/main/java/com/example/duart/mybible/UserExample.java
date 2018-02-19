package com.example.duart.mybible;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.gson.JsonArray;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
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
import java.io.UnsupportedEncodingException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserExample extends AppCompatActivity {

    private static final String TAG = UserExample.class.getName();
    private static final String REQUESTTAG = "string request first";
    private Button btnSync;
    private ListView listViewUsers;
    private EditText editTextName;
    private EditText editTextStatus;
    private Button btnAddData;
    private Button btnReadData;
    private dataBaseExample myDataBaseExample;
    private SQLiteDatabase objectDataBaseExample;

    String userName, status;

    private RequestQueue mRequestQueue;
    private String sendStatus;
    private String sendUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_example);

        editTextName = (EditText) findViewById(R.id.edit_text_name);
        editTextStatus = (EditText) findViewById(R.id.edit_text_status);
        btnAddData = (Button) findViewById(R.id.btn_add_data);
        listViewUsers = (ListView) findViewById(R.id.list_view_users);
        myDataBaseExample = new dataBaseExample(this);
        btnSync = (Button) findViewById(R.id.btn_sync);

        ArrayList<String> entries = new ArrayList<>();
        Cursor data = myDataBaseExample.getAllData();

        while (data.moveToNext()){
            entries.add(data.getString(1) + " | " + data.getString(2));
            ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, entries);
            listViewUsers.setAdapter(listAdapter);
        }

        final ArrayList<String> arrayListUserName = new ArrayList<>();
        Cursor data_2 = myDataBaseExample.getUnsyncData();

        while (data_2.moveToNext()){
            arrayListUserName.add(data_2.getString(1));
        }

        Log.i(TAG, String.valueOf(arrayListUserName));

        btnSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getUnsyncUsers();

                for ( int i = 0; i < arrayListUserName.size(); i++){
                    userName = arrayListUserName.get(i);
                    status = "1";

                    sendUnsyncUsers(userName, status);
                }
            }
        });

    }

    public void insertData(View view){

        objectDataBaseExample = myDataBaseExample.getWritableDatabase();
        String insertQuery = "INSERT INTO users (userName, status) Values ('" + editTextName.getText().toString() + "', " + Integer.parseInt(editTextStatus.getText().toString()) + ");";
        objectDataBaseExample.execSQL(insertQuery);

        Intent intent = new Intent( UserExample.this, UserExample.class );
        startActivity( intent );

    }

    public void getUnsyncUsers() {

        mRequestQueue = Volley.newRequestQueue(this);

        String urlGet = "http://home.localtunnel.me/teste/getusers.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGet, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {
                        JSONObject balance = response.getJSONObject(i);

                        String userName = balance.getString("userName");
                        columnsClass objectColumnsClass = new columnsClass(userName, "1");

                        objectDataBaseExample = myDataBaseExample.getWritableDatabase();
                        String insertQuery = "INSERT INTO users (userName, status) Values ('" + objectColumnsClass.getName() + "', " + Integer.parseInt(objectColumnsClass.getStatus()) + ");";
                        objectDataBaseExample.execSQL(insertQuery);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.i(TAG, "RESPOSTA: " + response);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        jsonArrayRequest.setTag(REQUESTTAG);
        mRequestQueue.add(jsonArrayRequest);
    }

    public void sendUnsyncUsers ( final String userName, final String status ){

        final String urlSend = "http://home.localtunnel.me/teste/inputExample.php";

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String userNameHolder = userName;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("userName", userNameHolder));
                nameValuePairs.add(new BasicNameValuePair("status", statusHolder));

                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(urlSend);

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
        sendPostReqAsyncTask.execute(sendUserName, sendStatus);

        objectDataBaseExample = myDataBaseExample.getWritableDatabase();
        objectDataBaseExample.execSQL("UPDATE users SET status = 1 WHERE status = 0");

    }

}