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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

public class main_menu_activity extends AppCompatActivity {

    private static final String TAG = main_menu_activity.class.getName();
    private static final String REQUESTTAG = "string request first";
    private Button btnSendRequest;
    private TextView textViewRequest;

    private RequestQueue mRequestQueue;

    private StringRequest stringRequest;
    private String url = "http://www.mocky.io/v2/5a82e6352f00007d0074bc0c";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_activity);

        Intent intent_main_menu = getIntent();

        btnSendRequest = (Button) findViewById(R.id.btn_send_request);
        textViewRequest = (TextView) findViewById(R.id.text_view_request);

        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendRequestAndPrintResponse();

            }
        });

    }

    private void sendRequestAndPrintResponse() {

        mRequestQueue = Volley.newRequestQueue(this);

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i(TAG, "Reponse: " + response.toString());
                textViewRequest.setText(response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG, "Error: " + error.toString());

            }
        });

        stringRequest.setTag(REQUESTTAG);
        mRequestQueue.add(stringRequest);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null){
            mRequestQueue.cancelAll(REQUESTTAG);
        }
    }
}