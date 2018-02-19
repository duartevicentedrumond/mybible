package com.example.duart.mybible;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewEntries extends AppCompatActivity {

    private Spinner spinnerRepay;
    private Spinner spinnerRepayment;
    private Spinner spinnerType;
    private Button btnAddNewEntry;
    private EditText editTextDescription;
    private EditText editTextValue;
    private EditText editTextSourceDestination;
    private RequestQueue mRequestQueue;
    String description, value, sourceDestination, repay, repayment, type;

    private String url = "http://home.localtunnel.me/android/input_wallet.php";
    private static final String TAG = Extract.class.getName();
    private static final String REQUESTTAG = "string request first";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entries);

        editTextDescription = (EditText) findViewById(R.id.edit_text_description);
        editTextValue = (EditText) findViewById(R.id.edit_text_value);
        editTextSourceDestination = (EditText) findViewById(R.id.edit_text_source_destination);

        String[] arraySpinnerRepay = new String[] {"sim", "não"};
        spinnerRepay = (Spinner) findViewById(R.id.spinner_repay);
        final ArrayAdapter<String> adapterSpinnerRepay = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arraySpinnerRepay);
        spinnerRepay.setAdapter(adapterSpinnerRepay);

        String[] arraySpinnerRepayment = new String[] {"sim", "não"};
        spinnerRepayment = (Spinner) findViewById(R.id.spinner_repayment);
        final ArrayAdapter<String> adapterSpinnerRepayment = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arraySpinnerRepayment);
        spinnerRepayment.setAdapter(adapterSpinnerRepayment);

        String[] arraySpinnerType = new String[] {"dinheiro", "cartão"};
        spinnerType = (Spinner) findViewById(R.id.spinner_type);
        final ArrayAdapter<String> adapterSpinnerType = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arraySpinnerType);
        spinnerType.setAdapter(adapterSpinnerType);

        btnAddNewEntry = (Button) findViewById(R.id.btn_add_new_entry);
        btnAddNewEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getData();

                insertData(description, value, sourceDestination, repay, repayment, type);

            }
        });



    }

    private void getData() {

        description = editTextDescription.getText().toString();
        value = editTextValue.getText().toString();
        sourceDestination = editTextSourceDestination.getText().toString();
        if (spinnerRepay.getSelectedItem().toString().equals("sim")){
            repay = "yes";
        } else if (spinnerRepay.getSelectedItem().toString().equals("não")){
            repay = "no";
        }
        if (spinnerRepayment.getSelectedItem().toString().equals("sim")){
            repayment = "yes";
        } else if (spinnerRepayment.getSelectedItem().toString().equals("não")){
            repayment = "no";
        }
        type = spinnerType.getSelectedItem().toString();

    }


    public void insertData(final String description, final String value, final String sourceDestination, final String repay, final String repayment, final String type ){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String descriptionHolder = description ;
                String valueHolder = value;
                String sourceDestinationHolder = sourceDestination;
                String repayHolder = repay;
                String repaymentHolder = repayment;
                String typeHolder = type;

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                nameValuePairs.add(new BasicNameValuePair("description", descriptionHolder));
                nameValuePairs.add(new BasicNameValuePair("value", valueHolder));
                nameValuePairs.add(new BasicNameValuePair("sourceDestination", sourceDestinationHolder));
                nameValuePairs.add(new BasicNameValuePair("repay", repayHolder));
                nameValuePairs.add(new BasicNameValuePair("repayment", repaymentHolder));
                nameValuePairs.add(new BasicNameValuePair("type", typeHolder));

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

                Toast.makeText(NewEntries.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(description, value, sourceDestination, repay, repayment, type);

        Log.i(TAG, type);

    }

}