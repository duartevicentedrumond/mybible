package com.example.duart.mybible;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.SystemClock;
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

    private TextView textViewMoney;
    private TextView textViewBalancePay;
    private TextView textViewBalanceDebt;
    private mybibleDataBase dataBase;
    private SQLiteDatabase sqLiteDatabase;

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

    public void getDebtors(){

        sqLiteDatabase = dataBase.getReadableDatabase();
        String findDebtorsQuery = "SELECT DISTINCT id_person FROM wallet WHERE id_person !=0 AND repay='sim' AND status!=3;";
        Cursor debtors = sqLiteDatabase.rawQuery(findDebtorsQuery, null);

        List debtorsList = new ArrayList();

        if(debtors.getCount()!=0){
            while (debtors.moveToNext()){
                debtorsList.add(debtors.getString(0));
            }

            List showDebtorsList = new ArrayList();

            for (int i = 0; i < debtorsList.size(); i++){
                String computeDebtQuery = "SELECT SUM( value ) FROM wallet WHERE id_person='" + debtorsList.get(i).toString() + "' AND repay='sim' AND status!=3;";
                Cursor debtsSum = sqLiteDatabase.rawQuery(computeDebtQuery, null);
                debtsSum.moveToFirst();

                sqLiteDatabase = dataBase.getReadableDatabase();
                String string = "SELECT name FROM person WHERE id=" + debtorsList.get(i) + ";";
                Cursor personName = sqLiteDatabase.rawQuery(string, null);
                personName.moveToFirst();

                showDebtorsList.add(personName.getString(0) + ": " + debtsSum.getString(0));
                if ( Double.parseDouble(debtsSum.getString(0)) < 0 ){
                    textViewBalanceDebt.append(showDebtorsList.get(i) + " €\n");
                } else if ( Double.parseDouble(debtsSum.getString(0)) > 0 ){
                    textViewBalancePay.append(showDebtorsList.get(i) + " €\n");
                }

            }
        }
    }

    public void getBalance(){
        sqLiteDatabase = dataBase.getReadableDatabase();
        String balanceQuery = "SELECT SUM( value ) FROM wallet WHERE status!=3;";
        Cursor balance = sqLiteDatabase.rawQuery(balanceQuery, null);

        if (balance.getCount()!=0){
            balance.moveToFirst();
            textViewMoney.setText(balance.getString(0) + " €");
        }
    }

}
