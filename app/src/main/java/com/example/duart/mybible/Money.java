package com.example.duart.mybible;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.lang.Math;

public class Money extends AppCompatActivity {

    Double totalValueToReceive = 0.0;
    Double totalValue = 0.0;
    private static final String TAG = Money.class.getName();
    private static final String REQUESTTAG = "string request first";
    private Button btnCheckWallet;
    private TextView textViewMoney;
    private TextView textViewBalancePay;
    private TextView textViewBalanceDebt;
    private mybibleDataBase dataBase;
    private SQLiteDatabase sqLiteDatabase;

    private RequestQueue mRequestQueue;

    private Double totalValueRepay = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        btnCheckWallet = (Button) findViewById(R.id.btn_check_money);
        textViewMoney = (TextView) findViewById(R.id.text_view_money);
        textViewBalancePay = (TextView) findViewById(R.id.text_view_balance_pay);
        textViewBalanceDebt = (TextView) findViewById(R.id.text_view_balance_debts);
        dataBase = new mybibleDataBase(this);

        btnCheckWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getWalletData();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sync, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_favorite:
                startActivity( new Intent( Money.this, Extract.class ) );
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void getWalletData() {

        mRequestQueue = Volley.newRequestQueue(this);

        String url = "http://home.localtunnel.me/android/sync_wallet_mysql_sqlite.php";

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
                        String source_destination = mysqlDataUnsync.getString("source_destination");
                        String repay = mysqlDataUnsync.getString("repay");
                        String repayment = mysqlDataUnsync.getString("repayment");
                        String type = mysqlDataUnsync.getString("type");

                        balanceClass balanceClassObject = new balanceClass(id, date, description, value, source_destination, repay, repayment, type, 1 );

                        sqLiteDatabase = dataBase.getWritableDatabase();
                        String insertQuery = "INSERT INTO wallet (id, date, description, value, source_destination, repay, repayment, type, status ) Values (" + balanceClassObject.getId() + ", '" + balanceClassObject.getDate() + "', '" + balanceClassObject.getDescription() + "', '" + balanceClassObject.getValue() + "', '" + balanceClassObject.getSourceDestination() + "', '" + balanceClassObject.getRepay() + "', '" + balanceClassObject.getRepayment() + "', '" + balanceClassObject.getType() + "', " + 1 + ");";
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

    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null){
            mRequestQueue.cancelAll(REQUESTTAG);
        }
    }

    public void addNewEntries(View view){

        Intent intent2 = new Intent( Money.this, NewEntries.class );
        startActivity( intent2 );

    }

}
