package com.example.user.mashov;

/**
 * Created by User on 1/9/18.
 */

public class JsonObject<T> {
    String command;
    T data;

    @Override
    public String toString() {
        return "JsonObject{" +
                "command='" + command + '\'' +
                ", data=" + data +
                '}';
    }
}
