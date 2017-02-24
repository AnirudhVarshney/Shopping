package com.example.abhinav.udacityshopping;

/**
 * Created by ABHINAV on 12-09-2016.
 */
public class Likes {
    String uid;
    String name;


    public Likes() {
    }

    public Likes(String uid, String name) {
        this.uid = uid;
        this.name = name;

    }





    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
