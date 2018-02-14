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

public class SendHTTPRequest extends AppCompatActivity {

    private static final String TAG = SendHTTPRequest.class.getName();
    private static final String REQUESTTAG = "string request first";
    private Button btnSendRequest;
    private TextView textViewRequest;

    private RequestQueue mRequestQueue;

    private String url = "http://home.localtunnel.me/android/query.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_httprequest);

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

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.i(TAG, "Response: " + response.toString());

                textViewRequest.setText("");

                for (int i = 0; i < response.length(); i++){

                    try {

                        JSONObject user = response.getJSONObject(i);
                        String userID = user.getString("user_id");
                        String password = user.getString("password");
                        textViewRequest.append(userID + " " + password + "\n\n");

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
}
