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

public class ViewItems extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    FirebaseRecyclerAdapter<Items,EventHol> adapter;
    DatabaseReference db;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);
        Firebase.setAndroidContext(this);
        final Intent intent=getIntent();
        id=intent.getStringExtra("id");
        mRecyclerView= (RecyclerView) findViewById(R.id.rv_recycler_items);
        db= FirebaseDatabase.getInstance().getReferenceFromUrl(constants.FIREBASE_LOCATION_ACTIVE_LIST_Items+"/"+id);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        adapter=new FirebaseRecyclerAdapter<Items, EventHol>(Items.class,R.layout.row_items,EventHol.class,db) {

            @Override
            protected void populateViewHolder(EventHol viewHolder, Items model, final int position) {
                final String name=model.getItemname();
                Log.d("ani",name+"name");
                viewHolder.mText.setText(name);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent1=new Intent(ViewItems.this,EditItems.class);
                        intent1.putExtra("itemid",getRef(position).getKey());
                        intent1.putExtra("id",id);
                        ViewItems.this.startActivity(intent1);
                    }
                });
              ;
                //  ;


            }
        };
        mRecyclerView.setAdapter(adapter);

    }
    public  static class EventHol extends RecyclerView.ViewHolder{
        TextView mText;
        TextView owner;
        public EventHol(final View itemView) {
            super(itemView);
            mText= (TextView) itemView.findViewById(R.id.itemsaddedtext);

        }

    }
}
