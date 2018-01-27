package com.example.user.mashov;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.util.HashMap;

public class LoginActivity extends ServiceHandler implements IJson<String> {

    Button bt_login;
    EditText et_input_username;
    EditText et_input_password;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        //init all the variables
        init();

    }

    @Override
    public void onReceiveResult(String response) {
        progressBar.setVisibility(View.GONE);
        JsonObject<LoginAndRegisterANS> jsonObject = null;
        int responseCode = 0;
        Gson gson = new Gson();
        try {
            jsonObject = gson.fromJson(response, new TypeToken<JsonObject<LoginAndRegisterANS>>() {}.getType());
        }catch (Exception e)
        {

        }
        if(jsonObject!=null)
        {
            responseCode = jsonObject.data.responseCode;
        }
        switch (responseCode)
        {
            case 100:
            {
                Intent intentHomeActivity = new Intent(LoginActivity.this, HomeActivity.class);
                intentHomeActivity.putExtra("LoginAndRegisterANS", response);
                SaveSharedPreference sp = new SaveSharedPreference();
                sp.setUser(getApplicationContext(), response);
                startActivity(intentHomeActivity);
                break;
            }
            case 201:
            {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                dlgAlert.setMessage("your username or password was incorrect");
                dlgAlert.setTitle("Login Failed");
                dlgAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //dismiss the dialog
                            }
                        });
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                break;
            }
            default:
            {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                dlgAlert.setMessage(response);
                dlgAlert.setTitle("Login Failed");
                dlgAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //dismiss the dialog
                            }
                        });
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                break;
            }
        }
        Log.i("responseFromServer:",  "login activity " + response);
    }

    private void init()
    {
        bt_login = (Button)findViewById(R.id.ll_bt_login);
        et_input_username = (EditText)findViewById(R.id.ll_input_username);
        et_input_password = (EditText)findViewById(R.id.ll_input_password);
        progressBar = (ProgressBar) findViewById(R.id.pb);
    }
    public void sumbitLogin(View v)
    {
        HashMap<String, String> params = new HashMap<>();
        boolean flagUsername = isUsernameValid();
        boolean flagPassword = isPasswordValid();
        if(flagPassword && flagUsername)
        {
            params.put("username", et_input_username.getText().toString());
            params.put("password", et_input_password.getText().toString());
            String json = makeJson(params);
            try {
                progressBar.setVisibility(View.VISIBLE);
                webService.sendPost("jsonQuery", json);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            Log.i("responseFromServer:", json);
        }

        //Intent i = new Intent(LoginActivity.this, HomeActivity.class);
        //i.putExtra("username", et_input_username.getText().toString());
        //startActivity(i);

    }
    public class SaveSharedPreference
    {
        static final String PREF_USER_DETAILS= "user_details";

        public SharedPreferences getSharedPreferences(Context ctx) {
            return PreferenceManager.getDefaultSharedPreferences(ctx);
        }

        public void setUser(Context ctx, String userDetails)
        {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.putString(PREF_USER_DETAILS, userDetails);
            editor.commit();
        }

    }
    private boolean isPasswordValid()
    {
        boolean flag = true;
        //password empty
        flag = isFieldEmpty(et_input_password);
        return flag;
    }
    private boolean isUsernameValid()
    {
        boolean flag = true;
        //username empty
        flag = isFieldEmpty(et_input_username);
        return flag;
    }
    private boolean isFieldEmpty(EditText et)
    {
        boolean flag = true;
        if(et.getText().toString().trim().equalsIgnoreCase(""))
        {
            et.setError(getResources().getString(R.string.error_empty));
            et.requestFocus();
            flag = false;
        }
        return flag;
    }
    @Override
    public String makeJson(HashMap<String, String> params) {
        JsonObject<LoginREQ> jsonObject = new JsonObject<>();
        jsonObject.command = "login";
        LoginREQ loginREQ = new LoginREQ();
        loginREQ.username = params.get("username");
        loginREQ.password = params.get("password");
        jsonObject.data = loginREQ;

        Gson gson = new Gson();
        String json = gson.toJson(jsonObject, new TypeToken<JsonObject<LoginREQ>>(){}.getType());

        /*
        String command = "{";
        command+="\"command\":\"login\",";
        command+="\"data\":{";
        command+="\"username\":\"" + params.get("username") + "\",";
        command+="\"password\":\"" + params.get("password") + "\"";
        command+="}";
        command+="}";
        */
        return json;
    }
}
