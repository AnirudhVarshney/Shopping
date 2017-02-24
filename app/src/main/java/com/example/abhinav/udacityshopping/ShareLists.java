package com.example.abhinav.udacityshopping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShareLists extends AppCompatActivity {
Button add,notification;  private FirebaseAuth auth;
    private RecyclerView mRecyler;
    Utility utility;
    Boolean f=false;
    String n="";
    String selectedblogkey;
    ArrayList<String> names;
    FirebaseRecyclerAdapter<Users, EventHol> adapter;
    DatabaseReference db, dbsharewith, db2,db3,db4;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_lists);
        add= (Button) findViewById(R.id.button_addfriend);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ShareLists.this,AddFriend.class);
                startActivity(intent);
            }
        });
names=new ArrayList<>();
        Intent intent=getIntent();
         selectedblogkey=intent.getStringExtra("key");
        notification= (Button) findViewById(R.id.button_notification);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seenotification();
            }
        });
        mRecyler = (RecyclerView) findViewById(R.id.rv_recycler_sharefriend);
        auth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);
        utility=new Utility();
        mProgress=new ProgressDialog(this);
        String currentuser=utility.encodeEmail(auth.getCurrentUser().getEmail());
        db= FirebaseDatabase.getInstance().getReference().child("Usersfriends").child(currentuser);
        dbsharewith=FirebaseDatabase.getInstance().getReference().child("Sharewith").child(selectedblogkey);
        db2=FirebaseDatabase.getInstance().getReference().child("Sharewith");
        db3= FirebaseDatabase.getInstance().getReference().child("Blog").child(selectedblogkey);
        db4= FirebaseDatabase.getInstance().getReference().child("Blog").child(selectedblogkey).child(selectedblogkey);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyler.setLayoutManager(manager);
        adapter = new FirebaseRecyclerAdapter<Users, EventHol>(Users.class, R.layout.row_sharedfriends, EventHol.class, db) {


            @Override
            protected void populateViewHolder(final EventHol viewHolder, final Users model, int position) {
                String name=model.getName();
                Log.d("ani",name+"name");
                viewHolder.mUser.setText(name);
                final String email=utility.encodeEmail(model.getEmail());
                dbsharewith.child(email).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {

                        if(dataSnapshot.getValue(Users.class)!=null){
                        viewHolder.addfriendtoshare.setImageResource(R.drawable.ic_check_circle);
                            viewHolder.addfriendtoshare.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    deletefriend(model);
                                    Log.d("ani","in delete");

                                }


                            });
                        }
                        else{
                            viewHolder.addfriendtoshare.setImageResource(R.drawable.ic_library_add);
                            viewHolder.addfriendtoshare.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.d("ani","in save");

                                    savefriend(model);



                                }


                            });
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        mRecyler.setAdapter(adapter);

    }

    private void seenotification() {
        db3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(selectedblogkey)){
                    for (DataSnapshot child : dataSnapshot.child(selectedblogkey).getChildren()) {

                        names.add(child.child("name").getValue().toString());

                    }

                  // String name=dataSnapshot.child(selectedblogkey).getChildren().getValue().toString();
                    //Log.d("ani",name+"count");
                    Toast.makeText(ShareLists.this,"exists",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ShareLists.this,"not exists",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        for(int i=0;i<names.size();i++){

             n=n+names.get(i);
        }
        Log.d("ani",n+"names who have shared this blog");
        n="";
        names.clear();
    }

    private void deletefriend(Users user) {
        final String email=utility.encodeEmail(user.getEmail());
        HashMap<String, Object> updatedItemToAddMap = new HashMap<String, Object>();

        updatedItemToAddMap.put("/" + email,null);
        dbsharewith.updateChildren(updatedItemToAddMap);
        db4.updateChildren(updatedItemToAddMap);

    }

    private void savefriend(final Users user) {
        final String email=utility.encodeEmail(user.getEmail());
        HashMap<String, Object> updatedItemToAddMap = new HashMap<String, Object>();
        HashMap<String, Object> friendsusers = (HashMap<String, Object>)
                            new ObjectMapper().convertValue(user, Map.class);
        updatedItemToAddMap.put("/" + selectedblogkey+"/"+email,friendsusers);
        db2.updateChildren(updatedItemToAddMap);
        db3.updateChildren(updatedItemToAddMap);

    }

    public  static class EventHol extends RecyclerView.ViewHolder{
        TextView mUser;
        ImageView addfriendtoshare,check;
        public EventHol(final View itemView) {
            super(itemView);
            mUser= (TextView) itemView.findViewById(R.id.row_allsharedfriends);
            addfriendtoshare= (ImageView) itemView.findViewById(R.id.row_button_add);
            check= (ImageView) itemView.findViewById(R.id.row_button_check);

        }

    }
}
