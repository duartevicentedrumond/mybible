package com.example.duart.mybible;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

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
import java.util.List;

public class Wallet extends AppCompatActivity {

    private static final String TAG = Wallet.class.getName();
    private static final String REQUESTTAG = "string request first";
    private TextView textViewMoney;
    private TextView textViewBalancePay;
    private TextView textViewBalanceDebt;
    private mybibleDataBase dataBase;
    private SQLiteDatabase sqLiteDatabase;

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        textViewMoney = (TextView) findViewById(R.id.text_view_money);
        textViewBalancePay = (TextView) findViewById(R.id.text_view_balance_pay);
        textViewBalanceDebt = (TextView) findViewById(R.id.text_view_balance_debts);
        dataBase = new mybibleDataBase(this);

        getDebtors();
        getBalance();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wallet, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_sync:
                syncGetNewEntryWallet();
                syncSendNewEntryWallet();
                syncSendEditedEntryWallet();
                syncSendDeletedEntryWallet();
                return true;

            case R.id.action_add:
                startActivity( new Intent( Wallet.this, NewEntryWallet.class ) );
                return true;

            case R.id.action_list:
                startActivity( new Intent( Wallet.this, SeeAllWallet.class ) );
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void syncGetNewEntryWallet() {

        mRequestQueue = Volley.newRequestQueue(this);

        String url = "http://casa.localtunnel.me/android/sync_get_new_entry_wallet_android.php";

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

    private void syncGetEditedEntryWallet() {

        mRequestQueue = Volley.newRequestQueue(this);

        String url = "http://casa.localtunnel.me/android/sync_get_edited_entry_wallet_android.php";

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
                        String updateQuery =
                                "UPDATE wallet SET date='" + balanceClassObject.getDate()
                                + "', description ='" + balanceClassObject.getDescription()
                                + "', value ='" + balanceClassObject.getValue()
                                + "', source_destination='" + balanceClassObject.getSourceDestination()
                                + "', repay='" + balanceClassObject.getRepay()
                                + "', repayment='" + balanceClassObject.getRepayment()
                                + "', type='" + balanceClassObject.getType()
                                + "', status='" + balanceClassObject.getStatus()
                                + "' WHERE id=" +balanceClassObject.getId() + ";";
                        sqLiteDatabase.execSQL(updateQuery);

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

    private void syncGetDeletedEntryWallet() {

        mRequestQueue = Volley.newRequestQueue(this);

        String url = "http://casa.localtunnel.me/android/sync_get_deleted_entry_wallet_android.php";

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
                        String deleteQuery = "DELETE FROM wallet WHERE id=" +balanceClassObject.getId() + ";";
                        sqLiteDatabase.execSQL(deleteQuery);

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

    private void syncSendNewEntryWallet(){

        sqLiteDatabase = dataBase.getReadableDatabase();
        String getUnsyncData = "SELECT * FROM wallet WHERE status=0;";
        Cursor unsyncData = sqLiteDatabase.rawQuery(getUnsyncData, null);

        String url = "http://casa.localtunnel.me/android/sync_send_new_entry_wallet_android.php";

        Log.i(TAG, "TAMANHO: " + unsyncData.getCount());

        ArrayList<String> arrayListUnsyncDataId = new ArrayList<>();
        ArrayList<String> arrayListUnsyncDataDate = new ArrayList<>();
        ArrayList<String> arrayListUnsyncDataDescription = new ArrayList<>();
        ArrayList<String> arrayListUnsyncDataValue = new ArrayList<>();
        ArrayList<String> arrayListUnsyncDataSourceDestination = new ArrayList<>();
        ArrayList<String> arrayListUnsyncDataRepay = new ArrayList<>();
        ArrayList<String> arrayListUnsyncDataRepayment = new ArrayList<>();
        ArrayList<String> arrayListUnsyncDataType = new ArrayList<>();

        while (unsyncData.moveToNext()){
            arrayListUnsyncDataId.add(unsyncData.getString(0));
            arrayListUnsyncDataDate.add(unsyncData.getString(1));
            arrayListUnsyncDataDescription.add(unsyncData.getString(2));
            arrayListUnsyncDataValue.add(unsyncData.getString(3));
            arrayListUnsyncDataSourceDestination.add(unsyncData.getString(4));
            arrayListUnsyncDataRepay.add(unsyncData.getString(5));
            arrayListUnsyncDataRepayment.add(unsyncData.getString(6));
            arrayListUnsyncDataType.add(unsyncData.getString(7));
        }

        for (int i = 0; i < arrayListUnsyncDataDate.size(); i++){
            sendWallet(arrayListUnsyncDataId.get(i).toString(), arrayListUnsyncDataDate.get(i).toString(), arrayListUnsyncDataDescription.get(i).toString(), arrayListUnsyncDataValue.get(i).toString(), arrayListUnsyncDataSourceDestination.get(i).toString() , arrayListUnsyncDataRepay.get(i).toString(), arrayListUnsyncDataRepayment.get(i).toString(), arrayListUnsyncDataType.get(i).toString(), "1", url);
        }

        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "UPDATE wallet SET status=1 WHERE status=0;";
        sqLiteDatabase.execSQL(setStatusQuery);
    }

    private void syncSendEditedEntryWallet(){
        sqLiteDatabase = dataBase.getReadableDatabase();
        String getEditedData = "SELECT * FROM wallet WHERE status=2;";
        Cursor editedData = sqLiteDatabase.rawQuery(getEditedData, null);

        String url = "http://casa.localtunnel.me/android/sync_send_edited_entry_wallet_android.php";

        ArrayList<String> arrayListEditedDataId = new ArrayList<>();
        ArrayList<String> arrayListEditedDataDate = new ArrayList<>();
        ArrayList<String> arrayListEditedDataDescription = new ArrayList<>();
        ArrayList<String> arrayListEditedDataValue = new ArrayList<>();
        ArrayList<String> arrayListEditedDataSourceDestination = new ArrayList<>();
        ArrayList<String> arrayListEditedDataRepay = new ArrayList<>();
        ArrayList<String> arrayListEditedDataRepayment = new ArrayList<>();
        ArrayList<String> arrayListEditedDataType = new ArrayList<>();

        while (editedData.moveToNext()){
            arrayListEditedDataId.add(editedData.getString(0));
            arrayListEditedDataDate.add(editedData.getString(1));
            arrayListEditedDataDescription.add(editedData.getString(2));
            arrayListEditedDataValue.add(editedData.getString(3));
            arrayListEditedDataSourceDestination.add(editedData.getString(4));
            arrayListEditedDataRepay.add(editedData.getString(5));
            arrayListEditedDataRepayment.add(editedData.getString(6));
            arrayListEditedDataType.add(editedData.getString(7));
        }

        for (int i = 0; i < arrayListEditedDataDate.size(); i++){
            sendWallet(arrayListEditedDataId.get(i).toString(), arrayListEditedDataDate.get(i).toString(), arrayListEditedDataDescription.get(i).toString(), arrayListEditedDataValue.get(i).toString(), arrayListEditedDataSourceDestination.get(i).toString() , arrayListEditedDataRepay.get(i).toString(), arrayListEditedDataRepayment.get(i).toString(), arrayListEditedDataType.get(i).toString(), "1", url);
        }

        sqLiteDatabase = dataBase.getWritableDatabase();
        String setStatusQuery = "UPDATE wallet SET status=1 WHERE status=2;";
        sqLiteDatabase.execSQL(setStatusQuery);
    }

    private void syncSendDeletedEntryWallet(){
        sqLiteDatabase = dataBase.getReadableDatabase();
        String getDeletedData = "SELECT * FROM wallet WHERE status=3;";
        Cursor deletedData = sqLiteDatabase.rawQuery(getDeletedData, null);

        String url = "http://casa.localtunnel.me/android/sync_send_deleted_entry_wallet_android.php";

        ArrayList<String> arrayListDeletedDataId = new ArrayList<>();
        ArrayList<String> arrayListDeletedDataDate = new ArrayList<>();
        ArrayList<String> arrayListDeletedDataDescription = new ArrayList<>();
        ArrayList<String> arrayListDeletedDataValue = new ArrayList<>();
        ArrayList<String> arrayListDeletedDataSourceDestination = new ArrayList<>();
        ArrayList<String> arrayListDeletedDataRepay = new ArrayList<>();
        ArrayList<String> arrayListDeletedDataRepayment = new ArrayList<>();
        ArrayList<String> arrayListDeletedDataType = new ArrayList<>();

        while (deletedData.moveToNext()){
            arrayListDeletedDataId.add(deletedData.getString(0));
            arrayListDeletedDataDate.add(deletedData.getString(1));
            arrayListDeletedDataDescription.add(deletedData.getString(2));
            arrayListDeletedDataValue.add(deletedData.getString(3));
            arrayListDeletedDataSourceDestination.add(deletedData.getString(4));
            arrayListDeletedDataRepay.add(deletedData.getString(5));
            arrayListDeletedDataRepayment.add(deletedData.getString(6));
            arrayListDeletedDataType.add(deletedData.getString(7));
        }

        for (int i = 0; i < arrayListDeletedDataDate.size(); i++){
            sendWallet(arrayListDeletedDataId.get(i).toString(), arrayListDeletedDataDate.get(i).toString(), arrayListDeletedDataDescription.get(i).toString(), arrayListDeletedDataValue.get(i).toString(), arrayListDeletedDataSourceDestination.get(i).toString() , arrayListDeletedDataRepay.get(i).toString(), arrayListDeletedDataRepayment.get(i).toString(), arrayListDeletedDataType.get(i).toString(), "1", url);
        }

        sqLiteDatabase = dataBase.getWritableDatabase();
        String deleteQuery = "DELETE FROM wallet WHERE status=3";
        sqLiteDatabase.execSQL(deleteQuery);

    }

    private void sendWallet( final String id, final String date, final String description, final String value, final String source_destination, final String repay, final String repayment, final String type, final String status, final String url){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<>();

                String idHolder = id;
                String dateHolder = date;
                String descriptionHolder = description;
                String valueHolder = value;
                String source_destinationHolder = source_destination;
                String repayHolder = repay;
                String repaymentHolder = repayment;
                String typeHolder = type;
                String statusHolder = status;

                nameValuePairs.add(new BasicNameValuePair("id", idHolder));
                nameValuePairs.add(new BasicNameValuePair("date", dateHolder));
                nameValuePairs.add(new BasicNameValuePair("description", descriptionHolder));
                nameValuePairs.add(new BasicNameValuePair("value", valueHolder));
                nameValuePairs.add(new BasicNameValuePair("source_destination", source_destinationHolder));
                nameValuePairs.add(new BasicNameValuePair("repay", repayHolder));
                nameValuePairs.add(new BasicNameValuePair("repayment", repaymentHolder));
                nameValuePairs.add(new BasicNameValuePair("type", typeHolder));
                nameValuePairs.add(new BasicNameValuePair("status", statusHolder));

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

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(id, date, description, value, source_destination, repay, repayment, type, status );
    }

    public void getDebtors(){

        sqLiteDatabase = dataBase.getReadableDatabase();
        String findDebtorsQuery = "SELECT DISTINCT source_destination FROM wallet WHERE source_destination != '';";
        Cursor debtors = sqLiteDatabase.rawQuery(findDebtorsQuery, null);

        List debtorsList = new ArrayList();

        if(debtors.getCount()!=0){
            while (debtors.moveToNext()){
                debtorsList.add(debtors.getString(0));
            }

            List showDebtorsList = new ArrayList();

            for (int i = 0; i < debtorsList.size(); i++){
                String computeDebtQuery = "SELECT SUM( value ) FROM wallet WHERE source_destination='" + debtorsList.get(i).toString() + "';";
                Cursor debtsSum = sqLiteDatabase.rawQuery(computeDebtQuery, null);
                debtsSum.moveToFirst();
                showDebtorsList.add(debtorsList.get(i).toString() + ": " + debtsSum.getString(0));
                Log.i(TAG, "Result: " + showDebtorsList.get(i));
                textViewBalancePay.append(showDebtorsList.get(i) + " €\n");
            }
        }
    }

    public void getBalance(){
        sqLiteDatabase = dataBase.getReadableDatabase();
        String balanceQuery = "SELECT SUM( value ) FROM wallet;";
        Cursor balance = sqLiteDatabase.rawQuery(balanceQuery, null);

        if (balance.getCount()!=0){
            balance.moveToFirst();
            textViewMoney.setText(balance.getString(0) + " €");
        }
    }

}
