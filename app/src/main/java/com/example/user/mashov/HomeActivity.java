package com.example.user.mashov;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HomeActivity extends ServiceHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);


    }

    @Override
    public void onReceiveResult(String response) {

    }
}
