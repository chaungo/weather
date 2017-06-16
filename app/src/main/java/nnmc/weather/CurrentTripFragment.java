package nnmc.weather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    public static CurrentTripFragment newInstance() {
        return new CurrentTripFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tabs, container, false);
//        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.current_trip_timeline);
//        recyclerView.setHasFixedSize(true);
//        MyCurrentTripRecyclerAdapter myCurrentTripRecyclerAdapter = new MyCurrentTripRecyclerAdapter(new String[]{"vgfdsag", "ewgeagra", "graeghsd", "sgahdsfh"});
//        recyclerView.setAdapter(myCurrentTripRecyclerAdapter);
        return rootView;
    }
}