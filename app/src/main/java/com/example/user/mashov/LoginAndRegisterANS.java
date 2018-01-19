package com.example.user.mashov;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LoginAndRegisterANS on 1/3/18.
 */

public class LoginAndRegisterANS {
    int responseCode;
    int userID;
    String username;
    String email;
    int type;
    String phoneNum;
    String firstName;
    String lastName;
    String birthDay;
    int gender;
    String image;
    ArrayList<Post> posts;
    ArrayList<Class> classes;

    @Override
    public String toString() {
        return "LoginAndRegisterANS{" +
                "birthDay='" + birthDay + '\'' +
                ", responseCode=" + responseCode +
                ", userID=" + userID +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", type=" + type +
                ", phoneNum='" + phoneNum + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", image='" + image + '\'' +
                ", posts=" + posts +
                ", classes=" + classes +
                '}';
    }
}
