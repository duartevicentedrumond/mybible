package com.example.duart.mybible;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    private RequestQueue mRequestQueue;

    private String url = "http://home.localtunnel.me/android/read_wallet.php";
    private Double totalValueRepay = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        btnCheckWallet = (Button) findViewById(R.id.btn_check_money);
        textViewMoney = (TextView) findViewById(R.id.text_view_money);
        textViewBalancePay = (TextView) findViewById(R.id.text_view_balance_pay);

        btnCheckWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkWallet();

            }
        });
    }

    private void checkWallet() {

        mRequestQueue = Volley.newRequestQueue(this);

        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.i(TAG, "Response: " + response.toString());

                textViewMoney.setText("");

                HashMap<Integer, balanceClass> BalanceHashMap = new HashMap<>();
                for (int i = 0; i < response.length(); i++){

                    try {

                        JSONObject balance = response.getJSONObject(i);

                        String date = balance.getString("date");
                        String description = balance.getString("description");
                        Double value = balance.getDouble("value");
                        String source_destination = balance.getString("source_destination");
                        String repay = balance.getString("repay");
                        String repayment = balance.getString("repayment");
                        String type = balance.getString("type");

                        BalanceHashMap.put(i, new balanceClass(date, description, value, source_destination, repay, repayment, type));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                /**

                    This code computes the sum of all entries in our database's column "value" and puts its value in the variable "totalValue"
                 **/

                Collection<balanceClass> values = BalanceHashMap.values();
                List list_source_destination = new ArrayList();

                for (balanceClass i : values ){

                    //Computes the sum of all entries of database's column "value"
                    totalValue += i.getValue();

                    //Filters all names of database's column "source_destination"
                    if( i.getRepay().equals("yes") ){
                        list_source_destination.add(i.getSourceDestination());
                    }

                }

                Set<String> hashSet = new HashSet<>();

                hashSet.addAll(list_source_destination);
                list_source_destination.clear();
                list_source_destination.addAll(hashSet);

                for ( int i = 0; i < list_source_destination.size(); i++ ){

                    for (balanceClass j : values ){

                        if( j.getSourceDestination().equals(list_source_destination.get(i)) ){

                            totalValueRepay += j.getValue();

                        }

                    }

                    if (totalValueRepay < 0 ){
                        textViewBalancePay.setText(list_source_destination.get(i) + ": " + df.format(totalValueRepay) + " €\n\n");
                        textViewBalancePay.setTextColor(getResources().getColor(R.color.tropical_rain_forest));
                    } else if (totalValueRepay>0){
                        textViewBalancePay.setText(list_source_destination.get(i) + ": " + df.format(totalValueRepay) + " €\n\n");
                        textViewBalancePay.setTextColor(getResources().getColor(R.color.deep_carmine));
                    }

                }

                Log.i(TAG, "HashMap: " + list_source_destination);

                textViewMoney.setText(df.format(totalValue) + " €");


                if (totalValue>0){
                    textViewMoney.setTextColor(getResources().getColor(R.color.tropical_rain_forest));
                }else if (totalValue<0){
                    textViewMoney.setTextColor(getResources().getColor(R.color.deep_carmine));
                }

                totalValue = 0.0;
                totalValueRepay = 0.0;
                totalValueToReceive = 0.0;

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

    public void seeEntries(View view){

        Intent intent = new Intent( Money.this, Extract.class );
        startActivity( intent );

    }

    public void addNewEntries(View view){

        Intent intent2 = new Intent( Money.this, NewEntries.class );
        startActivity( intent2 );

    }

}
