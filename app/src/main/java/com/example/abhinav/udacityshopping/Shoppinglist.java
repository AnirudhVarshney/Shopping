package com.example.abhinav.udacityshopping;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.firebase.client.ServerValue;

/**
 * Created by ABHINAV on 08-08-2016.
 */
public class Shoppinglist {
    String user;
    String owner;
    @JsonProperty
    private Object created;

    public Shoppinglist() {
    }

    public Shoppinglist(String owner, String user) {
        this.owner = owner;
        this.user = user;
        this.created = ServerValue.TIMESTAMP;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getUser() {
        return user;
    }
    @JsonIgnore
    public String getCreatedTimestamp() {
        if (created instanceof String) {
            return (String) created;
        }
        else {
            return null;
        }
    }


    public void setUser(String user) {
        this.user = user;
    }
}
