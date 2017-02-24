package com.example.abhinav.udacityshopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Active extends AppCompatActivity {
    Shoppinglist shoppinglist;
    Items items;
    String owner;
    EditText text;
    String newtitle;
    String id;
    EditText newitem;
    Button additem;
    Button editbutton;
    Boolean saved;
    Button viewitems;
    String itemId;
    DatabaseReference db;
    DatabaseReference newdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        text= (EditText) findViewById(R.id.newtitle);
        db= FirebaseDatabase.getInstance().getReferenceFromUrl(constants.FIREBASE_URL);
        editbutton= (Button) findViewById(R.id.editbutton);
        newitem= (EditText) findViewById(R.id.additem);
        additem= (Button) findViewById(R.id.itembutton);
viewitems= (Button) findViewById(R.id.listfitems);
        viewitems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Active.this,ViewItems.class);
                intent.putExtra("id",id);
                intent.putExtra("itemid",itemId);
                startActivity(intent);
            }
        });
        additem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                items=new Items(newitem.getText().toString(),"anirudh");
                saveitems(items);
            }
        });
        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edittitle();
            }
        });
        Intent i=getIntent();
         id=i.getStringExtra("id");
        String name=i.getStringExtra("name");
        toolbar.setTitle(name);
        Log.d("ani",id+"id"+name);

//        Firebase ref1=new Firebase(constants.FIREBASE_URL).child("List");
//        ref1.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                shoppinglist=  dataSnapshot.getValue(Shoppinglist.class);
//                if(shoppinglist==null){
//                    finish();
//                }
//                long millisecond = Long.parseLong(shoppinglist.getCreatedTimestamp().toString());
//                String dateString = new SimpleDateFormat("dd MM yyyy HH:mm:ss").format(new Date(millisecond));
//                owner=shoppinglist.getOwner().toString();
//                toolbar.setTitle(shoppinglist.getOwner().toString()+dateString);
//            }
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });



    }


        public void saveitems(Items items)
        //public Boolean save(String shoppinglist)
        {
//            if(items==null)
//            {
//                saved=false;                               one way
//            }else
//            {
//                try
//                {
//                    db.child("ShoppingListItems").child(id).push().setValue(items);
//                    saved=true;
//                }catch (DatabaseException e)
//                {
//                    e.printStackTrace();
//                    saved=false;
//                }
//            }
//            return saved;

            HashMap<String, Object> updatedItemToAddMap = new HashMap<String, Object>(); //second way
           newdb= db.child("ShoppingListItems").child(id).push();
            itemId = newdb.getKey();
            HashMap<String, Object> itemToAdd =
                                        (HashMap<String, Object>) new ObjectMapper().convertValue(items,Map.class);
            updatedItemToAddMap.put("/"+"ShoppingListItems"+"/"+id+"/"+itemId,itemToAdd);
            db.updateChildren(updatedItemToAddMap);


        }

    public void edittitle(){
        newtitle=text.getText().toString();
       // Firebase ref1=new Firebase(constants.FIREBASE_URL).child("List");
        HashMap<String, Object> movieUpdatedProperties = new HashMap<>();
        movieUpdatedProperties.put("owner",newtitle);
       // movieUpdatedProperties.put("created", ServerValue.TIMESTAMP);
      db.child(id).updateChildren(movieUpdatedProperties);

    }

}
