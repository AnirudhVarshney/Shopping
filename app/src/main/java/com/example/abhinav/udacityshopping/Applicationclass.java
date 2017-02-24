package com.example.abhinav.udacityshopping;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by ABHINAV on 02-09-2016.
 */
public class Applicationclass extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
