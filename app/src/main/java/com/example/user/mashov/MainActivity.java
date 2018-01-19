package com.example.user.mashov;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button bt_register;
    Button bt_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkIfUserLoggedIn();
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
    public void checkIfUserLoggedIn()
    {
        SaveSharedPreference sp = new SaveSharedPreference();
        String userDetails = sp.getUser(getApplicationContext());
        if(!userDetails.isEmpty())
        {
            Intent intentHomeActivity = new Intent(MainActivity.this, HomeActivity.class);
            intentHomeActivity.putExtra("LoginAndRegisterANS", userDetails);
            startActivity(intentHomeActivity);
        }
    }
    public class SaveSharedPreference
    {
        static final String PREF_USER_DETAILS= "user_details";

        public SharedPreferences getSharedPreferences(Context ctx) {
            return PreferenceManager.getDefaultSharedPreferences(ctx);
        }

        public String getUser(Context ctx)
        {
            return getSharedPreferences(ctx).getString(PREF_USER_DETAILS, "");
        }
    }
    public void openLoginActivity(View v)
    {
        Intent intentLoginActivity  = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intentLoginActivity);

    }
    public void openRegisterActivity(View v)
    {
        Intent intentRegisterActivity = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intentRegisterActivity);
    }
    private void init()
    {
        bt_register = (Button)findViewById(R.id.am_bt_register);
        bt_login = (Button)findViewById(R.id.am_bt_login);
    }
}
