package com.example.abhinav.udacityshopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecyclerActivity extends AppCompatActivity {
    private ActiveListAdapter mActiveListAdapter;
    private RecyclerView mListView;
    Shoppinglist shoppingList;
    //Adapter adapter;
    FirebaseRecyclerAdapter<Shoppinglist,EventHol> adapter;
    DatabaseReference db;
    FirebaseHelper helper;
    private TextView mTextViewListName, mTextViewListOwner;
       private TextView mTextViewEditTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        Firebase.setAndroidContext(this);
       // mTextViewListName= (TextView) findViewById(R.id.userrv);
        //final TextView textViewownerName = (TextView) findViewById(R.id.ownerrv);
        mListView= (RecyclerView) findViewById(R.id.rv_recycler);
        db= FirebaseDatabase.getInstance().getReferenceFromUrl(constants.FIREBASE_LOCATION_ACTIVE_LIST);

      //  helper=new FirebaseHelper(db);
       // adapter=new Adapter(this,helper.retrieve());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mListView.setLayoutManager(manager);


     //   adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,helper.retrieve());
//        final Firebase refListName = new Firebase(constants.FIREBASE_LOCATION_ACTIVE_LIST).child("activeList");
//        final Firebase activeListsRef = new Firebase(constants.FIREBASE_LOCATION_ACTIVE_LIST_Up);
//        refListName.addValueEventListener(new ValueEventListener(){
//
//                                              @Override
//                                              public void onDataChange(DataSnapshot dataSnapshot) {
//                                                   shoppingList = dataSnapshot.getValue(Shoppinglist.class);
//                                                  mTextViewListName.setText(shoppingList.getUser());
//                                                  textViewownerName.setText(shoppingList.getOwner());
//
//                                              }
//
//
//                                              @Override
//                                              public void onCancelled(FirebaseError firebaseError) {
//
//                                              }
//                                          });
     //   mActiveListAdapter = new ActiveListAdapter(Shoppinglist.class,
              //  R.layout.row_lists,EventHol.class,db);
      //  mListView.setAdapter(adapter);

        adapter=new FirebaseRecyclerAdapter<Shoppinglist, EventHol>(Shoppinglist.class,R.layout.row_lists,EventHol.class,db) {

            @Override
            protected void populateViewHolder(EventHol viewHolder, Shoppinglist model, final int position) {
                final String name=model.getUser();
                viewHolder.mText.setText(model.getUser());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("ani",position+getRef(position).getKey()+"position");
                        Intent intent=new Intent(RecyclerActivity.this,Active.class);
                        intent.putExtra("id",getRef(position).getKey());
                        intent.putExtra("name",name);
                        Log.d("ani",name+"user");
                        RecyclerActivity.this.startActivity(intent);
                    }
                });
              //  ;


            }
        };

        mListView.setAdapter(adapter);
       // adapter.cleanup(); shuld be used onDestroy() t prevent memry leakage
     //   db.removeEventListener(); to remve event listner

    }

    public  static class EventHol extends RecyclerView.ViewHolder{
        TextView mText;
        TextView owner;
        public EventHol(final View itemView) {
            super(itemView);
            mText= (TextView) itemView.findViewById(R.id.userrv);
            owner= (TextView) itemView.findViewById(R.id.ownerrv);
 }

    }
}
