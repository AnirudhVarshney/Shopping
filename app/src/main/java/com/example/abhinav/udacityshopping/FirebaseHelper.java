package com.example.abhinav.udacityshopping;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by ABHINAV on 18-08-2016.
 */
public class FirebaseHelper {
    DatabaseReference db;
    DatabaseReference db1;
    Boolean saved=null;

    ArrayList<Shoppinglist> shoppinglistArrayList=new ArrayList<>();
    public FirebaseHelper(DatabaseReference db) {
        this.db = db;
    }
    public Boolean save(Shoppinglist shoppinglist)
    //public Boolean save(String shoppinglist)
    {
        if(shoppinglist==null)
        {
            saved=false;
        }else
        {
            try
            {
                db.child("List").push().setValue(shoppinglist);
                saved=true;
            }catch (DatabaseException e)
            {
                e.printStackTrace();
                saved=false;
            }
        }
        return saved;
    }

    public ArrayList<Shoppinglist> retrieve()
    {

        db1=FirebaseDatabase.getInstance().getReferenceFromUrl(constants.FIREBASE_URL);
        db1.child("ac").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return shoppinglistArrayList;
    }
    private void fetchData(DataSnapshot dataSnapshot)
    {
       // shoppinglistArrayList.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            Shoppinglist shoppinglist=ds.getValue(Shoppinglist.class);
            shoppinglistArrayList.add(shoppinglist);
        }
        Log.d("ani",shoppinglistArrayList.size()+shoppinglistArrayList.get(0).getUser()+"in helper");
    }

}
