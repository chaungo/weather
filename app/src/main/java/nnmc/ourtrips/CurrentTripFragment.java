package nnmc.ourtrips;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by nnmchau on 6/16/2017.
 */

public class CurrentTripFragment extends Fragment {

    public CurrentTripFragment() {
    }

    protected String[] mDataset;
    protected RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recyclerView;

    public static CurrentTripFragment newInstance() {
        return new CurrentTripFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initDataset();
        View rootView = inflater.inflate(R.layout.tab_current_trip, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.current_trip_timeline);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        MyCurrentTripRecyclerAdapter myCurrentTripRecyclerAdapter = new MyCurrentTripRecyclerAdapter(mDataset);
        recyclerView.setAdapter(myCurrentTripRecyclerAdapter);
        return rootView;
    }

    private void initDataset() {
        mDataset = new String[10];
        for (int i = 0; i < 10; i++) {
            mDataset[i] = "This is element #" + i;
        }
    }
}