package com.example.duart.mybible;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputHTTPRequest extends AppCompatActivity {

    private static final String TAG = InputHTTPRequest.class.getName();
    String url = "http://home.localtunnel.me/android/query_2.php" ;
    EditText userID, password ;
    Button btnInputRequest;
    String tempUser, tempPassword ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_httprequest);

        userID = (EditText) findViewById(R.id.edit_text_user_id);
        password = (EditText) findViewById(R.id.edit_text_password);
        btnInputRequest = (Button) findViewById(R.id.btn_input_request);

        btnInputRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getData();

                insertData(tempUser, tempPassword);

            }
        });


    }

    public void getData(){

        tempUser = userID.getText().toString();
        tempPassword = password.getText().toString();

    }

    public void insertData(final String userID, final String password ){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String userIDHolder = userID ;
                String passwordHolder = password ;

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                nameValuePairs.add(new BasicNameValuePair("userID", userIDHolder));
                nameValuePairs.add(new BasicNameValuePair("password", passwordHolder));

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

                Toast.makeText(InputHTTPRequest.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(userID, password);


    }

}
