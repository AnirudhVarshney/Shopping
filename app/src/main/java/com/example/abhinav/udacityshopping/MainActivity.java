package com.example.abhinav.udacityshopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * Represents the home screen of the app which
 * /
 */
public class MainActivity extends AppCompatActivity {
    EditText text;
    EditText owner;
    EditText newuser;
    EditText newowner;
    TextView textview;
    TextView ownertext;
    EditText updateusername;
    EditText updateuserphno;
    TextView time;
    Shoppinglist shoppinglist;
    Button send;
    Button Add;
    Button UpdateDetails;
    Button list;
    DatabaseReference db,db1;
    String uid;
    private FirebaseAuth auth;

    FirebaseHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);

        /* Enable disk persistence  */
//        Firebase.getDefaultConfig().setPersistenceEnabled(true);
        send= (Button) findViewById(R.id.SendButton);
        auth = FirebaseAuth.getInstance();
        Utility utility=new Utility();
        String email=utility.readFrompreferencesfirebase(this,"email","");
        Log.d("ani",email);
        text= (EditText) findViewById(R.id.EditText);
        owner= (EditText) findViewById(R.id.owner);
        newuser= (EditText) findViewById(R.id.newuser);
        newowner= (EditText) findViewById(R.id.newowner);
        updateusername= (EditText) findViewById(R.id.editusername);
        updateuserphno= (EditText) findViewById(R.id.edituserphone);
        UpdateDetails= (Button) findViewById(R.id.updatedetails);
        Intent intent=getIntent();
       // uid=intent.getStringExtra("uid");
        uid=auth.getCurrentUser().getUid();
        Log.d("ani",uid+"Hi"+auth.getCurrentUser());
        Add= (Button) findViewById(R.id.Addlist);
        list= (Button) findViewById(R.id.list);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,RecyclerActivity.class);
                startActivity(intent);
            }
        });
        UpdateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });
        db= FirebaseDatabase.getInstance().getReference();
        db1= FirebaseDatabase.getInstance().getReference().child("users");

        helper=new FirebaseHelper(db);
        textview= (TextView) findViewById(R.id.textview);
        time= (TextView) findViewById(R.id.timestamp);
        ownertext= (TextView) findViewById(R.id.ownertext);
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activelist();
            }


        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });
    }

    private void Addlist() {
        Firebase ref = new Firebase(constants.FIREBASE_URL);
        String userEnteredName = newuser.getText().toString();
        String owner=newowner.getText().toString();
      //  shoppinglist=new Shoppinglist(userEnteredName,owner);
       // ref.child("List").setValue(shoppinglist);

        if (!userEnteredName.equals("")) {


            Firebase listsRef = new Firebase(constants.FIREBASE_LOCATION_ACTIVE_LIST);
            Firebase newListRef = listsRef.push();
            final String listId = newListRef.getKey();
            Shoppinglist newShoppingList = new Shoppinglist(userEnteredName,owner);
            Log.d("ani",newShoppingList.getOwner()+"Hii");
            newListRef.setValue(newShoppingList);

        }
    }
    public void update(){
        Log.d("ani",uid+"Hi");
        Log.d("ani",updateusername.getText().toString()+"Hi");
        Log.d("ani",updateuserphno.getText().toString()+"Hi");

        HashMap<String, Object> movieUpdatedProperties = new HashMap<>();
      //  movieUpdatedProperties.put("name",updateusername.getText().toString());
        movieUpdatedProperties.put("phone",updateuserphno.getText().toString());
        // movieUpdatedProperties.put("created", ServerValue.TIMESTAMP);
        db1.child(uid).updateChildren(movieUpdatedProperties);
    }

    private void activelist() {
        Intent intent=new Intent(MainActivity.this, Active.class);
        startActivity(intent);
    }

    public void add() {
        Shoppinglist newShoppingList = new Shoppinglist(newuser.getText().toString(),newowner.getText().toString());
//        String s=newuser.getText().toString();
       // helper.save(newShoppingList);
        Firebase ref=new Firebase(constants.FIREBASE_LOCATION_ACTIVE_LIST);
        //Firebase ref=new Firebase(constants.FIREBASE_URL); if wanted to add directly nt inside smething
        shoppinglist=new Shoppinglist(text.getText().toString(),owner.getText().toString());
        ref.child("activeList").setValue(shoppinglist);


       Firebase ref1=new Firebase(constants.FIREBASE_LOCATION_ACTIVE_LIST).child("activeList");
//        ref1.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                shoppinglist = dataSnapshot.getValue(Shoppinglist.class);
//                Log.d("ani", shoppinglist.getOwner() + "Hii");
//                textview.setText(shoppinglist.getOwner().toString());
//                SimpleDateFormat dateformatYYYYMMDD = new SimpleDateFormat("yyyy MM dd");
//                long millisecond = Long.parseLong(shoppinglist.getCreatedTimestamp().toString());
//                String dateString = new SimpleDateFormat("dd MM yyyy HH:mm:ss").format(new Date(millisecond));
//                ownertext.setText(shoppinglist.getUser().toString());
//                time.setText(dateString);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//
//
//        });
//
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shoppinglist = dataSnapshot.getValue(Shoppinglist.class);
                Log.d("ani",shoppinglist.getCreatedTimestamp());
                SimpleDateFormat dateformatYYYYMMDD = new SimpleDateFormat("yyyy MM dd");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

}
