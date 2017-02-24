package com.example.abhinav.udacityshopping;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BlogMainActivity extends AppCompatActivity {
    private RecyclerView mRecyler;
    Shoppinglist shoppingList;
    private FirebaseAuth auth;
     String key;
    String count;
    EditText search;
    Query sort,searchblog;

    String mInput;
    private FloatingActionButton fab;
    FirebaseRecyclerAdapter<Blogmodel,EventHol> adapter;
    DatabaseReference db,db1,db2,db3;
Boolean prcessed=false;
    FirebaseAuth.AuthStateListener mAuthlistner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_main);
        Firebase.setAndroidContext(this);
        mRecyler= (RecyclerView) findViewById(R.id.rv_recycler_blog);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        db= FirebaseDatabase.getInstance().getReference().child("Blog");
        db.keepSynced(true);
search= (EditText) findViewById(R.id.searchblog);
        //Query sort=db.orderByChild("post");
         sort=db.orderByKey();


        auth = FirebaseAuth.getInstance();
        db1= FirebaseDatabase.getInstance().getReference().child("users");
        db2= FirebaseDatabase.getInstance().getReference().child("Likers");
        db3=FirebaseDatabase.getInstance().getReference().child("Sharewith");
        db1.keepSynced(true);
        db2.keepSynced(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        manager.setReverseLayout(true);
        mRecyler.setLayoutManager(manager);
fab.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
search.setVisibility(View.VISIBLE);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mInput = search.getText().toString().toLowerCase();
                searchblog=db.orderByChild("email").startAt(mInput).endAt(mInput + "~");
                if (adapter != null)
                    adapter.cleanup();
                if (mInput.equals("") || mInput.length() < 2) {
                    //mRecyler.setAdapter(adapter);
                    makeadapter(sort);

                }
                else {
                    makeadapter(searchblog);


                }


            }
        });
    }
});

        checkuserexists();
        mAuthlistner=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
//                Log.d("ani", user.toString() + "user");
                if (user==null)
                {
                    Intent intent = new Intent(BlogMainActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }

        };
        makeadapter(sort);




    }

    private void makeadapter(Query query) {
        adapter = new FirebaseRecyclerAdapter<Blogmodel, EventHol>(Blogmodel.class, R.layout.row_blog, EventHol.class, query) {

            @Override
            protected void populateViewHolder(final EventHol viewHolder, final Blogmodel model, final int position) {
                long millisecond = Long.parseLong(model.getTimestampLastChanged().get(constants.FIREBASE_PROPERTY_TIMESTAMP).toString());
                String dateString = new SimpleDateFormat("dd MM yyyy HH:mm:ss").format(new Date(millisecond));
                ;
                final String title = model.getPost();
                String desc = model.getDescription();
                String url = model.getUrl();
                viewHolder.mTitle.setText(title);
                viewHolder.mDescription.setText(desc);
                key = getRef(position).getKey();
                viewHolder.mUsername.setText(model.getUsername());
                viewHolder.mTime.setText(dateString);
                if (auth.getCurrentUser().getUid().equals(model.getUserid())) {
                    viewHolder.mDeletepost.setVisibility(View.VISIBLE);
                    viewHolder.mDeletepost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(BlogMainActivity.this);
                            builder.setTitle("Delete the Blog");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    db.child(getRef(position).getKey()).removeValue();
                                    db2.child(getRef(position).getKey()).removeValue();
                                    db3.child(getRef(position).getKey()).removeValue();

                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.show();

                        }
                    });
                } else if (!auth.getCurrentUser().getUid().equals(model.getUserid())) {
                    viewHolder.mDeletepost.setVisibility(View.GONE);
                }

                checklikeexists(getRef(position).getKey(), viewHolder.mLikes, viewHolder.mNooflike);
                Glide.with(getApplicationContext()).load(url).centerCrop().into(viewHolder.mImage);
                Glide.with(getApplicationContext()).load(model.getUserimage()).centerCrop().into(viewHolder.mUserdp);

                viewHolder.mImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(BlogMainActivity.this);
                        builder.setTitle("Share The blog");

                        Log.d("ani", key);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(BlogMainActivity.this, ShareLists.class);

                                intent.putExtra("key", key);
                                startActivity(intent);
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
//                        Log.d("ani",position+getRef(position).getKey()+"position");
//                        Log.d("ani",position+db.getKey()+"postioin");
                    }
                });
                viewHolder.mLikes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //key=getRef(position).getKey();
                        Likes like = new Likes(auth.getCurrentUser().getUid(), model.getUsername());
                        likes(getRef(position).getKey(), viewHolder.mLikes, like);
                    }
                });
                //  ;


            }
        };
        mRecyler.setAdapter(adapter);
    }


    private void checklikeexists(final String key, final ImageView mLikes, final TextView mNooflike) {
     //    ImageView likebtn= (ImageView) findViewById(R.id.rv_like);
        final String k=key;
        db2.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(auth.getCurrentUser().getUid())){
                    mLikes.setImageResource(R.drawable.ic_thumbs_up_down);
                     count= String.valueOf(dataSnapshot.getChildrenCount());
                    Log.d("ani",  count+k+"key and count");
                    mNooflike.setText(count);
                    Log.d("ani",  dataSnapshot.getChildrenCount()+"ncunt");
                }
                else {
                    mLikes.setImageResource(R.drawable.ic_thumb_up);
                    count= String.valueOf(dataSnapshot.getChildrenCount());
                    Log.d("ani",  count+key+"count");
                    mNooflike.setText(count);
                    Log.d("ani",  dataSnapshot.getChildrenCount()+"ncunt");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void likes(final String key, final ImageView mLikes, final Likes like) {
prcessed=true;
        db2.child(key).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (prcessed) {
                    if (!dataSnapshot.hasChild(auth.getCurrentUser().getUid())) {

                        db2.child(key).child(auth.getCurrentUser().getUid()).setValue(like);


                        mLikes.setImageResource(R.drawable.ic_thumbs_up_down);
                        prcessed = false;

                    } else {
                        db2.child(key).child(auth.getCurrentUser().getUid()).setValue(null);
                        mLikes.setImageResource(R.drawable.ic_thumb_up);
                        prcessed = false;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkuserexists() {

        if (auth.getCurrentUser() != null) {
            db1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(auth.getCurrentUser().getUid())) {
                        Intent intent = new Intent(BlogMainActivity.this, SetupAccount.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        adapter.cleanup();
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthlistner);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.add) {

Intent intent=new Intent(BlogMainActivity.this,PostActivity.class);
            startActivity(intent);

        }
        if(id==R.id.logout){
            auth.signOut();
        }
        if(id==R.id.share){
            Intent intent=new Intent(this,ShareLists.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
    public  static class EventHol extends RecyclerView.ViewHolder{
        TextView mTitle,mUsername,mNooflike;
        TextView mDescription;
        ImageView mImage;
        TextView mTime;
        ImageView mUserdp,mLikes,mDeletepost;
        public EventHol(final View itemView) {
            super(itemView);
            mTitle= (TextView) itemView.findViewById(R.id.rv_title);
            mDescription= (TextView) itemView.findViewById(R.id.rv_desc);
            mTime= (TextView) itemView.findViewById(R.id.rv_time);
            mUsername= (TextView) itemView.findViewById(R.id.rv_username);
            mImage= (ImageView) itemView.findViewById(R.id.recyler_image_blog);
            mLikes= (ImageView) itemView.findViewById(R.id.rv_like);
            mNooflike= (TextView) itemView.findViewById(R.id.rv_nolikes);
            mUserdp= (ImageView) itemView.findViewById(R.id.rv_postuserdp);
            mDeletepost= (ImageView) itemView.findViewById(R.id.deletepost);
        }

    }


}


