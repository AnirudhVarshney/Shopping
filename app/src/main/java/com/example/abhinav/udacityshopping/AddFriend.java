package com.example.abhinav.udacityshopping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddFriend extends AppCompatActivity {
    private FirebaseAuth auth;
    private RecyclerView mRecyler,mRecylerSearch;
    Utility utility;
    String mInput;
    private ProgressDialog mProgress;
    EditText  mEditTextAddFriendEmail;
    FirebaseRecyclerAdapter<Users, EventHol> adapter;
    FirebaseRecyclerAdapter<Users,SearchHol> adaptersearch;
    DatabaseReference db, db1, db2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        mRecyler = (RecyclerView) findViewById(R.id.rv_recycler_addfriend);
        mRecylerSearch = (RecyclerView) findViewById(R.id.rv_recycler_search);
        auth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);
         utility=new Utility();
        mEditTextAddFriendEmail = (EditText) findViewById(R.id.edit_text_add_friend_email);
        mProgress=new ProgressDialog(this);
        String currentuser=utility.encodeEmail(auth.getCurrentUser().getEmail());
        db=FirebaseDatabase.getInstance().getReference().child("Usersfriends").child(currentuser);
        db1= FirebaseDatabase.getInstance().getReference().child("users");
        db1.keepSynced(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyler.setLayoutManager(manager);
        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        mRecylerSearch.setLayoutManager(manager1);
        adapter = new FirebaseRecyclerAdapter<Users, EventHol>(Users.class, R.layout.row_friends, EventHol.class, db1) {


            @Override
            protected void populateViewHolder(EventHol viewHolder, final Users model, int position) {
                Log.d("ani",model.getImageurl()+"url");
                viewHolder.mUser.setText(model.getEmail());
                viewHolder.mUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isnotuser(model)){
                              addfriend(model);
                        }
                        else {
                            Toast.makeText(AddFriend.this,"Cant Add user itself",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        };
//        adaptersearch = new FirebaseRecyclerAdapter<Users, SearchHol>(Users.class, R.layout.row_search, SearchHol.class, db1.orderByChild("email")) {
//     This can be used if not using search only results are coming in ordered way directly along with this wdit text can be used which will craete the new adapter
//
//            @Override
//            protected void populateViewHolder(SearchHol viewHolder, final Users model, int position) {
//                Log.d("ani",model.getImageurl()+"url");
//                viewHolder.mUser.setText(model.getEmail());

//
//
//            }
//        };
       // mRecylerSearch.setAdapter(adaptersearch);
        mEditTextAddFriendEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mInput = mEditTextAddFriendEmail.getText().toString().toLowerCase();
                if (adaptersearch != null)
                    adaptersearch.cleanup();
                if (mInput.equals("") || mInput.length() < 2) {
                    mRecylerSearch.setAdapter(null);


                }
                else {
                    adaptersearch = new FirebaseRecyclerAdapter<Users, SearchHol>(Users.class, R.layout.row_search, SearchHol.class, db1.orderByChild("email").startAt(mInput).endAt(mInput + "~").limitToFirst(5)) {


                        @Override
                        protected void populateViewHolder(SearchHol viewHolder, final Users model, int position) {

                            viewHolder.mUser.setText(model.getEmail());


                        }
                    };
                    mRecylerSearch.setAdapter(adaptersearch);
                }
            }
        });

        mRecyler.setAdapter(adapter);


    }

    private boolean isnotuser(Users user) {
      if(auth.getCurrentUser().getEmail().equals(user.getEmail())){
            return false;

        }
        else{return true;}

    }

    private void addfriend(final Users users) {
      final String eEmail=  utility.encodeEmail(users.getEmail());
        mProgress.setMessage("Adding friend in database");
        mProgress.show();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.hasChild(eEmail)){

                    db.child(eEmail).setValue(users);
                    Intent intent=new Intent(AddFriend.this,ShareLists.class);
                    startActivity(intent);
                    mProgress.dismiss();
                }
                else {
                    Toast.makeText(AddFriend.this,"Friend already exists",Toast.LENGTH_SHORT).show();
                    mProgress.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }


    public  static class EventHol extends RecyclerView.ViewHolder{
        TextView mUser;

        public EventHol(final View itemView) {
            super(itemView);
            mUser= (TextView) itemView.findViewById(R.id.row_allfriends);

        }

    }
    public  static class SearchHol extends RecyclerView.ViewHolder{
        TextView mUser;

        public SearchHol(final View itemView) {
            super(itemView);
            mUser= (TextView) itemView.findViewById(R.id.searchfriends);

        }

    }

}
