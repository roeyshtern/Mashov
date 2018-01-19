package com.example.user.mashov;

/**
 * Created by User on 1/9/18.
 */

public class RegisterREQ {
    String firstName;
    String lastName;
    String username;
    String birthday;
    String email;
    String password;
    String phoneNum;
    int gender;
    String image;
    int type;

    @Override
    public String toString() {
        return "RegisterREQ{" +
                "birthday='" + birthday + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", gender=" + gender +
                ", image='" + image + '\'' +
                ", type=" + type +
                '}';
    }
}
