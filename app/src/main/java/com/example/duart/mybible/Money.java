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
import java.text.DecimalFormat;

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
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        Intent intent3 = getIntent();

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

                for (int i = 0; i < response.length(); i++){

                    try {

                        JSONObject balance = response.getJSONObject(i);
                        Double value = balance.getDouble("value");
                        String repay = balance.getString("repay");
                        String repayment = balance.getString("repayment");
                        totalValue = totalValue + value;

                        if(repay=="yes" && repayment=="no"){
                            totalValueToReceive = totalValueToReceive + value;
                        } else if (repay=="yes" && repayment=="yes"){
                            totalValueToReceive = totalValueToReceive + value;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                textViewMoney.setText(df.format(totalValue) + " €");
                textViewBalancePay.setText(df.format(totalValueToReceive) + " €");

                if (totalValue>0){
                    textViewMoney.setTextColor(getResources().getColor(R.color.tropical_rain_forest));
                }else if (totalValue<0){
                    textViewMoney.setTextColor(getResources().getColor(R.color.deep_carmine));
                }

                totalValue = 0.0;
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
}
