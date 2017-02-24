package com.example.abhinav.udacityshopping;

import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by ABHINAV on 17-08-2016.
 */
public class ActiveListAdapter extends FirebaseRecyclerAdapter<Shoppinglist,RecyclerActivity.EventHol> {

    public ActiveListAdapter(Class<Shoppinglist> modelClass, int modelLayout, Class<RecyclerActivity.EventHol> viewHolderClass, DatabaseReference ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
    }


    @Override
    protected void populateViewHolder(RecyclerActivity.EventHol viewHolder, Shoppinglist model, int position) {
        viewHolder.mText.setText(model.getUser());

        Log.d("ani",viewHolder.getAdapterPosition()+"position");
    }




}
