package com.example.user.mashov;

import android.util.Log;

import java.util.HashMap;
import java.util.List;


/**
 * Created by User on 12/1/2017.
 */

public class ServerConnect {
    private final static String url = "http://172.20.2.74:8080/Mashov/AppHandler";
    public static String sendPost(final String command, final String jsonRequest)
    {
        final String[] responseRet = new String[1];
        HashMap<String, String> keyValueParams = new HashMap<String, String>();
        keyValueParams.put(command,jsonRequest);
        Thread connectionThread = HttpUtility.newRequest(url,HttpUtility.METHOD_POST,keyValueParams, new HttpUtility.Callback() {
            @Override
            public void OnSuccess(String response) {
                responseRet[0] = response;
            }

            @Override
            public void OnError(int status_code, String message) {
                Log.i("Error:", message);
            }
        });
        try {
            connectionThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responseRet[0];
    }
}
