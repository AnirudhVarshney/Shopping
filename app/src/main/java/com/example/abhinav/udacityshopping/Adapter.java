package com.example.abhinav.udacityshopping;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ABHINAV on 19-08-2016.
 */
public class Adapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private LayoutInflater minflater;
    ArrayList<Shoppinglist> mResults;
    Context con;
    public Adapter(Context context, ArrayList<Shoppinglist> Results) {

        minflater = LayoutInflater.from(context);
        mResults = Results;
        con = context;
//        Log.d("ani",mResults.get(0).getOwner()+"In adapter");
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = minflater.inflate(R.layout.row_lists, parent, false);
        return new EventHol(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        EventHol eventHolder = (EventHol) holder;
        Shoppinglist list=mResults.get(position);
        Log.d("ani",position+list.getUser()+list.getOwner()+"in bind");
eventHolder.user.setText(list.getUser());
        eventHolder.owner.setText(list.getOwner());

    }


    @Override
    public int getItemCount() {
        Log.d("ani",mResults.size()+"size");
        return mResults.size();

    }

    private class EventHol extends RecyclerView.ViewHolder{
        TextView user;
        TextView owner;
        public EventHol(View itemView) {
            super(itemView);
            user= (TextView) itemView.findViewById(R.id.userrv);
            owner= (TextView) itemView.findViewById(R.id.ownerrv);
        }
    }
}
