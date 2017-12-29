package com.example.user.mashov;

/**
 * Created by User on 12/1/2017.
 */

import android.util.Log;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import static java.net.HttpURLConnection.HTTP_OK;

public class HttpUtility {

    public static final int METHOD_GET = 0; // METHOD GET
    public static final int METHOD_POST = 1; // METHOD POST

    // Callback interface
    public interface Callback {
        // abstract methods
        public void OnSuccess(String response);
        public void OnError(int status_code, String message);
    }

    // static method
    public static Thread newRequest(final String web_url, final int method, final HashMap < String, String > params, final Callback callback) {

        // thread for handling async task
        Thread connectionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = web_url;
                    // write GET params,append with url

                    HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
                    urlConnection.setDoOutput(true); // write POST params
                    urlConnection.setUseCaches(false);
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); // handle url encoded form data
                    urlConnection.setRequestProperty("charset", "utf-8");
                    urlConnection.setRequestMethod("POST");

                    //write POST data
                    if (method == METHOD_POST && params != null) {
                        StringBuilder postData = new StringBuilder();
                        postData.append(URLEncoder.encode(String.valueOf(params.get("jsonQuery")), "UTF-8"));
                        byte[] postDataBytes = postData.toString().getBytes("UTF-8");
                        urlConnection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                        urlConnection.getOutputStream().write(postDataBytes);
                    }
                    // server response code
                    int responseCode = urlConnection.getResponseCode();
                    if (responseCode == HTTP_OK && callback != null) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        // callback success
                        callback.OnSuccess(response.toString());
                        reader.close(); // close BufferReader
                    } else if (callback != null) {
                        // callback error
                        callback.OnError(responseCode, urlConnection.getResponseMessage());
                    }

                    urlConnection.disconnect(); // disconnect connection
                } catch (IOException e) {
                    e.printStackTrace();
                    if (callback != null) {
                        // callback error
                        callback.OnError(500, e.getLocalizedMessage());
                    }
                }
            }
        }); // start thread
        return connectionThread;
    }
}
