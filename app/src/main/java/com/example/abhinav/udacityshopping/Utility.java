package com.example.abhinav.udacityshopping;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ABHINAV on 29-08-2016.
 */
public class Utility {
    public static final String PREF_FILE_NAME = "userdetails";
    public static final String PREF_FILE_NAME_Profile = "userprofile";
    public static final String PREF_FILE_NAME_Firebase = "firebase";
    public static final String KEY_USER_LEARNED_DRAWER = "user";
    public void saveTopreferencesfirebase(Context context, String url) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME_Firebase, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("value", url);
        editor.apply();
    }
    public String readFrompreferencesfirebase(Context context, String preferancename, String defaultvalue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME_Firebase, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferancename, defaultvalue);
    }
    public String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }
}
