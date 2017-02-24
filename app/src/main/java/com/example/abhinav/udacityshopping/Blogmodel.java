package com.example.abhinav.udacityshopping;

import com.firebase.client.ServerValue;

import java.util.HashMap;

/**
 * Created by ABHINAV on 01-09-2016.
 */
public class Blogmodel {
    String post;
    String description;
    String url;
    String username;
    String userid;
    String userimage;
    String email;


    private HashMap<String, Object> dateCreated;
    private HashMap<String, Object> timestampLastChanged;

    public Blogmodel() {
    }


    public Blogmodel(String post, String description, String url, String username, String userid,String userurl,String email) {
        this.description = description;
        this.post = post;
        this.username = username;
        this.userid = userid;
        this.url = url;
        this.email=email;
        this.userimage=userurl;
        HashMap<String, Object> timestampLastChangedObj = new HashMap<String, Object>();
        timestampLastChangedObj.put(constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        this.timestampLastChanged = timestampLastChangedObj;

    }

    public HashMap<String, Object> getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(HashMap<String, Object> dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTimestampLastChanged(HashMap<String, Object> timestampLastChanged) {
        this.timestampLastChanged = timestampLastChanged;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPost() {
        return post;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap<String, Object> getTimestampLastChanged() {
        return timestampLastChanged;
    }
}
