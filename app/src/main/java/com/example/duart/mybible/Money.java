package com.example.duart.mybible;

import android.content.Intent;
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

public class Money extends AppCompatActivity {

    Double totalValue = 0.0;
    private static final String TAG = Money.class.getName();
    private static final String REQUESTTAG = "string request first";
    private Button btnCheckWallet;
    private TextView textViewMoney;

    private RequestQueue mRequestQueue;

    private String url = "http://home.localtunnel.me/android/read_wallet.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        Intent intent3 = getIntent();

        btnCheckWallet = (Button) findViewById(R.id.btn_check_money);
        textViewMoney = (TextView) findViewById(R.id.text_view_money);

        btnCheckWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkWallet();

            }
        });
    }

    private void checkWallet() {

        mRequestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.i(TAG, "Response: " + response.toString());

                textViewMoney.setText("");

                for (int i = 0; i < response.length(); i++){

                    try {

                        JSONObject extracto = response.getJSONObject(i);
                        Double value = extracto.getDouble("value");
                        totalValue = totalValue + value;


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                textViewMoney.setText(totalValue + " €");
                totalValue = 0.0;

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
