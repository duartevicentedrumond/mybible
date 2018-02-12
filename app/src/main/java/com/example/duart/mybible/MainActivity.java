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

        TextView text_view_password = (TextView) findViewById(R.id.passwordTextView);
        TextView text_view_error_message = (TextView) findViewById(R.id.error_message);
        Button btn_login_validation = (Button) findViewById(R.id.button);
        EditText password = (EditText) findViewById(R.id.passWordEditText);

        if (password.getText().toString().equals("admin")) {

            Intent intent_main_menu = new Intent( MainActivity.this, main_menu_activity.class );
            startActivity( intent_main_menu );

        }else{
            text_view_error_message.setText("this password is wrong.\nplease input the correct one.");
            password.setText("");
        }
    }

}
