package nnmc.ourtrips;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by nnmchau on 6/16/2017.
 */


public class MyCurrentTripRecyclerAdapter extends RecyclerView.Adapter<MyCurrentTripRecyclerAdapter.ViewHolder> {

    private String[] mDataset;


    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView)itemView.findViewById(R.id.textView_info);
        }

        public TextView getTextView() {
            return mTextView;
        }
    }


    public MyCurrentTripRecyclerAdapter(String[] myDataset) {
        mDataset = myDataset;
    }


    @Override
    public MyCurrentTripRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_current_trip_list_item, parent, false);

        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.getTextView().setText(mDataset[position]);
        System.out.println(mDataset[position]);
    }


    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}

