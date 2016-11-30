package fi.jamk.hunteam.explorejyvaskylaandroid.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fi.jamk.hunteam.explorejyvaskylaandroid.R;

/**
 * Created by DoubleD on 2016. 11. 29..
 */

public class ProfileFragment extends Fragment {

    public ProfileFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_profile, container, false);
        return rootView;
    }

}
