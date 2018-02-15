package com.example.duart.mybible;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Extract extends AppCompatActivity {

    private static final String TAG = Extract.class.getName();
    private static final String REQUESTTAG = "string request first";
    private Button btnSeeEntries;
    private ListView listViewEntries;

    private RequestQueue mRequestQueue;

    private String url = "http://home.localtunnel.me/android/read_wallet.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract);

        btnSeeEntries = (Button) findViewById(R.id.btn_see_entries);
        listViewEntries = (ListView) findViewById(R.id.list_view_entries);
        final ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.list_view_layout);
        listViewEntries.setAdapter(adapter);

        btnSeeEntries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkWallet( adapter );

            }
        });
    }

    private void checkWallet(final ArrayAdapter adapter ) {

        mRequestQueue = Volley.newRequestQueue(this);

        final DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.i(TAG, "Response: " + response.toString());

                Map<Integer, balanceClass> BalanceHashMap = new HashMap<>();
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

                ArrayList<String> entries = new ArrayList();

                for (int i = BalanceHashMap.size()-1; i > 0; i--){
                    entries.add(BalanceHashMap.get(i).getDate().substring(0, BalanceHashMap.get(i).getDate().length() -9 ) + "\n" + BalanceHashMap.get(i).getDescription() + "\n" + BalanceHashMap.get(i).getValue() + "");
                }

                adapter.clear();
                adapter.addAll(entries);
                adapter.notifyDataSetChanged();

                Log.i(TAG, "HashMap: " + BalanceHashMap.get(57).getDate());

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
