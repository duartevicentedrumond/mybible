package com.example.duart.mybible;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //this line removes the arrow from the action bar menu in this activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }

    public void moneyClick(View view){
        startActivity( new Intent( MainMenu.this, Wallet.class ) );
    }

    public void boxClick(View view){
        startActivity( new Intent( MainMenu.this, Box.class ) );
    }

    public void contactClick(View view){
        startActivity( new Intent( MainMenu.this, Person.class ) );
    }

}
