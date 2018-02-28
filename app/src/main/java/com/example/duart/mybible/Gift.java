package com.example.duart.mybible;

import android.content.Intent;
import android.database.Cursor;
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

public class Gift extends AppCompatActivity {

    private SQLiteOpenHelper dataBase;
    private SQLiteDatabase sqLiteDatabase;
    private ListView listViewGift;
    private RequestQueue mRequestQueue;
    private static final String TAG = Gift.class.getName();
    private static final String REQUESTTAG = "string get new item";
    public String IpAddress = "http://dpvdda.localtunnel.me/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);

        listViewGift = (ListView) findViewById(R.id.list_view_gift);
        dataBase = new mybibleDataBase(this);

        printGiftList();
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
                return true;

            case R.id.action_sync:
                syncGetNewGift();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void printGiftList(){
        ArrayList<String> arrayListGift = new ArrayList<>();
        ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListGift);

        sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor giftData = sqLiteDatabase.rawQuery("SELECT gift.id, item.name, wallet.value FROM gift LEFT JOIN item ON gift.id_item=item.id LEFT JOIN wallet ON gift.id_wallet=wallet.id WHERE gift.status!=3;", null);

        while (giftData.moveToNext()){
            if (giftData.getString(1)==null) {
                arrayListGift.add("#" + giftData.getString(0) + " " + giftData.getString(2));
            }else if(giftData.getString(2)==null){
                arrayListGift.add("#" + giftData.getString(0) + " " + giftData.getString(1));
            }
        }
        listViewGift.setAdapter(listAdapter);
    }

    public void clickableListView(){
        listViewGift.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String giftSelected = adapterView.getItemAtPosition(i).toString();
                Intent intent = new Intent(Gift.this, ViewGift.class);
                intent.putExtra("giftSelected", giftSelected);
                startActivity( intent );
            }
        });
    }

    private void syncGetNewGift() {

        /**sqLiteDatabase = dataBase.getWritableDatabase();
         String deleteQuery = "DELETE FROM wallet;";
         sqLiteDatabase.execSQL(deleteQuery);**/

        mRequestQueue = Volley.newRequestQueue(this);

        String url = IpAddress + "android/sync_get_new_gift_android.php";

        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {

                    try {

                        JSONObject mysqlDataUnsync = response.getJSONObject(i);

                        Integer id = Integer.parseInt(mysqlDataUnsync.getString("id"));
                        Integer id_item = Integer.parseInt(mysqlDataUnsync.getString("id_item"));
                        Integer id_wallet = Integer.parseInt(mysqlDataUnsync.getString("id_wallet"));
                        Integer id_person = Integer.parseInt(mysqlDataUnsync.getString("id_person"));
                        String date = mysqlDataUnsync.getString("date");
                        String description = mysqlDataUnsync.getString("description");
                        String category = mysqlDataUnsync.getString("category");

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        sqLiteDatabase.execSQL("INSERT INTO gift (id, id_item, id_wallet, id_person, date, description, category, status) VALUES (" + id + ", " + id_item + ", " + id_wallet + ", " + id_person + ", '" + date + "', '" +  description + "', '" + category + "', 1 );");

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