package com.example.user.mashov;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by User on 12/26/17.
 */
public class WebService extends Service {

    private final static String url = "http://10.0.0.20:8080/Mashov/AppHandler";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }
    public class MyBinder extends Binder {
        public WebService getService()
        {
            return WebService.this;
        }
    }
    public void sendPost(final String command, final String jsonRequest)
    {
        final String[] responseRet = new String[1];
        HashMap<String, String> keyValueParams = new HashMap<String, String>();
        keyValueParams.put(command,jsonRequest);
        Thread connectionThread = HttpUtility.newRequest(url,HttpUtility.METHOD_POST,keyValueParams, new HttpUtility.Callback() {
            @Override
            public void OnSuccess(String response) {
                responseRet[0] = response;
                Intent intent = new Intent(ServiceHandler.BROADCASTRECEIVER_ACTION);
                intent.putExtra("resopnse",responseRet[0]);
                sendBroadcast(intent);
            }

            @Override
            public void OnError(int status_code, String message) {
                Log.i("responseFromServer:", message);
                responseRet[0] = "";
                Intent intent = new Intent(ServiceHandler.BROADCASTRECEIVER_ACTION);
                intent.putExtra("resopnse",responseRet[0]);
                sendBroadcast(intent);
            }
        });
        connectionThread.start();

    }
}
