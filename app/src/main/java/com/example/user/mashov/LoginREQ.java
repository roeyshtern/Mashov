package com.example.user.mashov;

/**
 * Created by User on 1/9/18.
 */

public class LoginREQ {
    String username;
    String password;

    @Override
    public String toString() {
        return "LoginREQ{" +
                "password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
