package com.example.user.mashov;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

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
        switch (getResponseCode(response))
        {
            case 100:
            {
                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                i.putExtra("username", et_input_username.getText().toString());
                startActivity(i);
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
                dlgAlert.setMessage("Unknown error");
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
        Log.i("responseFromServer:", response);
    }
    public int getResponseCode(String json)
    {
        int responseCode = 0;
        try {
            JSONObject obj = new JSONObject(json);
            if(obj.getString("command").equals("loginANS")) {
                responseCode = Integer.parseInt(obj.getJSONObject("data").getString("responseCode"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return responseCode;
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
        String command = "{";
        command+="\"command\":\"login\",";
        command+="\"data\":{";
        command+="\"username\":\"" + params.get("username") + " \",";
        command+="\"password\":\"" + params.get("password") + " \"";
        command+="}";
        command+="}";
        return command;
    }
}
