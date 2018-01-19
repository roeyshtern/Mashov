package com.example.user.mashov;

/**
 * Created by LoginAndRegisterANS on 1/3/18.
 */

public class Post {
    int postID;
    String data;
    String image;

    /*
    public Post(int postID, String data, String image) {
        this.postID = postID;
        this.data = data;
        this.image = image;
    }
    */

    @Override
    public String toString() {
        return "Post{" +
                "data='" + data + '\'' +
                ", postID=" + postID +
                ", image='" + image + '\'' +
                '}';
    }
}
