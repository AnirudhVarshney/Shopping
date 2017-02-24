package com.example.abhinav.udacityshopping;

/**
 * Created by ABHINAV on 23-08-2016.
 */
public class Users {
    String name;
    String phone;
    String imageurl;
    String email;

    public Users() {
    }



    public Users(String name, String phone, String image, String email) {
        this.name = name;
        this.phone = phone;

        this.imageurl=image;
        this.email=email;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
