package com.example.duart.mybible;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login_validation(View view) {

        TextView text_view_error_message = (TextView) findViewById(R.id.error_message); //define text_view_password with the element whose id is error_message
        EditText password = (EditText) findViewById(R.id.password_edittext); //define password with the element whose id is password_edittext

        if (password.getText().toString().equals("")) {
            /*if password is equal to "admin", then it will:
                -start a new intent called intent_main_menu
            */

            Intent intent_main_menu = new Intent( MainActivity.this, MainMenu.class );
            startActivity( intent_main_menu );

        }else{
            /*if password is not equal to "admin", then it will:
                -show an error message
                -clean the input password's field
            */

            text_view_error_message.setText("this password is wrong.\nplease input the correct one.");
            password.setText("");

        }
    }

}
