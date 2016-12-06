package fi.jamk.hunteam.explorejyvaskylaandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fi.jamk.hunteam.explorejyvaskylaandroid.fragments.MapFragment;
import fi.jamk.hunteam.explorejyvaskylaandroid.fragments.ProfileFragment;
import fi.jamk.hunteam.explorejyvaskylaandroid.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private final int ADD_PLACE_REQUEST_CODE = 1;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        private MapFragment mapFragment;
        private SearchFragment searchFragment;
        private ProfileFragment profileFragment;

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                if (searchFragment == null)
                    searchFragment = new SearchFragment();
                return searchFragment;
            }
            if (position == 1){
                if (mapFragment == null)
                    mapFragment = new MapFragment();
                return mapFragment;
            }
            if (position == 2){
                if (profileFragment == null)
                    profileFragment = new ProfileFragment();
                return profileFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    // Click on the add place button on map fragment. Start the activity
    public void addPlace(View view){
        Intent intent = new Intent(this, AddPlaceActivity.class);
        startActivityForResult(intent, ADD_PLACE_REQUEST_CODE);
    }

    // Click on the search button on search fragment
    public void search(View view){
        SearchFragment searchFragment = (SearchFragment) mSectionsPagerAdapter.getItem(0);
        MapFragment mapFragment = (MapFragment) mSectionsPagerAdapter.getItem(1);
        List<String> categories = new ArrayList<>();
        // Get the categories from the search fragment
        for (SearchFragment.Model model: searchFragment.models){
            if (model.isSelected()){
                categories.add(model.getName());
            }
        }
        // Give the categories to the map fragment
        mapFragment.getInterestingPlacesInCheckedCategories(categories);
        // Change the screen to the map view
        mViewPager.setCurrentItem(1);
    }

    // Return result from an activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PLACE_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                MapFragment fragment = (MapFragment) getSupportFragmentManager().findFragmentByTag("SECTION 2");

                System.out.println(fragment);
                System.out.println("Refresh the markers!");
            }
        }
    }

    // Click on the set camera button on the map fragment
    // Call the map fragment's function
    public void setMapCamera(View v){
        MapFragment mapFragment = (MapFragment) mSectionsPagerAdapter.getItem(1);
        mapFragment.setMapCamera();
    }


    // Click on the right arrow on the map fragment
    // Change screen to profile fragment
    public void goToProfile(View v){
        mViewPager.setCurrentItem(2);
    }

    // Click on the left arrow on the map fragment
    // Change screen to search fragment
    public void goToSearch(View v){
        mViewPager.setCurrentItem(0);
    }

}
