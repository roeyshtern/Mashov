package com.example.user.mashov;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.*;

public abstract class ServiceHandler extends AppCompatActivity {

    public static final String BROADCASTRECEIVER_ACTION = "ServiceHandler.intent.action.RESULT_RECEIVED";
    public WebReceiever webReceiever;
    protected WebService webService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void onResume() {
        super.onResume();
        //register broadcastreciever
        IntentFilter event = new IntentFilter(BROADCASTRECEIVER_ACTION);
        webReceiever = new WebReceiever();
        registerReceiver(webReceiever,event);

        Intent intent = new Intent(this,WebService.class);
        if(!isMyServiceRunning(WebService.class))
        {
            startService(intent);
        }
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                webService = ((WebService.MyBinder) service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(webReceiever!=null)
        {
            unregisterReceiver(webReceiever);
        }

    }

    private boolean isMyServiceRunning(java.lang.Class<?> service)
    {
        ActivityManager manager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if(service.getName().equals(serviceInfo.service.getClassName())){
                return true;
            }
        }
        return false;
    }
    public class WebReceiever extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if(intent.getAction().equals(BROADCASTRECEIVER_ACTION))
            {
                if(bundle!=null)
                {
                    onReceiveResult(bundle.getString("resopnse"));
                }
            }
        }
    }
    public abstract void onReceiveResult(String response);
}
