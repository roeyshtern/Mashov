package com.example.user.mashov;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button bt_register;
    Button bt_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init all the variables
        init();
        //login and search
        //100 - ok
        //201 - failed
        //registration
        // 202 - used username
        // 203 used email
        // 204 used phone
        // 105 - success registration
    }
    public void openLoginActivity(View v)
    {
        Intent intent  = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);

    }
    public void openRegisterActivity(View v)
    {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
    private void init()
    {
        bt_register = (Button)findViewById(R.id.am_bt_register);
        bt_login = (Button)findViewById(R.id.am_bt_login);
    }
}
