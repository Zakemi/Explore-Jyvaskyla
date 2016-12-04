package fi.jamk.hunteam.explorejyvaskylaandroid.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import fi.jamk.hunteam.explorejyvaskylaandroid.R;
import fi.jamk.hunteam.explorejyvaskylaandroid.database.Locations;
import fi.jamk.hunteam.explorejyvaskylaandroid.database.Visits;

/**
 * Created by DoubleD on 2016. 11. 29..
 */

public class ProfileFragment extends Fragment {

    private Map<String, Integer> allCounts;
    private Map<String, Integer> visitedCounts;

    public ProfileFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_profile, container, false);

        getData();
        return rootView;
    }


    public void getData(){
        Locations locations = new Locations(getContext());
        allCounts = locations.getCategoryCount();
        for (String key: allCounts.keySet()){
            System.out.println("____ " + key + ": " + allCounts.get(key));
        }
        Visits visits = new Visits(getContext());
        visitedCounts = visits.getCategoryCount();
        for (String key: visitedCounts.keySet()){
            System.out.println("____ " + key + ": " + visitedCounts.get(key));
        }
    }
}
