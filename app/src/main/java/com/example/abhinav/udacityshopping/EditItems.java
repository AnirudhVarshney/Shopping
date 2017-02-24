package com.example.abhinav.udacityshopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditItems extends AppCompatActivity {
EditText edititem;
    String id;
    String itemid;
    Button save;
    Button delete;
    DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_items);
        edititem= (EditText) findViewById(R.id.edititem);
        delete= (Button) findViewById(R.id.do_delete);
        save= (Button) findViewById(R.id.edititembutton);
        Firebase.setAndroidContext(this);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });
        db= FirebaseDatabase.getInstance().getReferenceFromUrl(constants.FIREBASE_URL);
        Intent i=getIntent();
       id=i.getStringExtra("id");
        itemid=i.getStringExtra("itemid");
        Log.d("ani",id+itemid+"BTh ");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            update();

            }
        });

    }
    public  void  update(){
        String nameInput = edititem.getText().toString();
        Log.d("ani",nameInput);
        HashMap<String, Object> updatedItemToAddMap = new HashMap<String, Object>();
        updatedItemToAddMap.put("/" + "ShoppingListItems" + "/" + id + "/" + itemid + "/" +"itemname",
                nameInput);
db.updateChildren(updatedItemToAddMap);

    }
    public  void  delete(){
        String nameInput = edititem.getText().toString();
        Log.d("ani",nameInput);
        HashMap<String, Object> updatedItemToAddMap = new HashMap<String, Object>();
       // updatedItemToAddMap.put("/" + "ShoppingListItems" + "/" + id + "/" + itemid + "/" +"itemname", //to delete itemname
         //       null);
        updatedItemToAddMap.put("/" + "ShoppingListItems" + "/" + id + "/" + itemid , //to delete itemname
                null);
        db.updateChildren(updatedItemToAddMap);

    }
}
