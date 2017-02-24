package com.example.abhinav.udacityshopping;

/**
 * Created by ABHINAV on 21-08-2016.
 */
public class Items {
    String itemname;
    String owner;

    public Items() {
    }

    public Items(String itemname, String owner) {
        this.itemname = itemname;
        this.owner = owner;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
