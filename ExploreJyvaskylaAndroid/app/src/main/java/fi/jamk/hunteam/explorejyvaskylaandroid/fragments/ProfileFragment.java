package fi.jamk.hunteam.explorejyvaskylaandroid.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fi.jamk.hunteam.explorejyvaskylaandroid.R;
import fi.jamk.hunteam.explorejyvaskylaandroid.database.Locations;
import fi.jamk.hunteam.explorejyvaskylaandroid.database.Visits;
import fi.jamk.hunteam.explorejyvaskylaandroid.model.Categories;

/**
 * Created by DoubleD on 2016. 11. 29..
 */

public class ProfileFragment extends Fragment {

    private List<Model> categories;
    private View rootView;
    private static final int GOLD_TITLE = 1;
    private static final int SILVER_TITLE = 2;
    private static final int BRONZE_TITLE = 3;
    private static final int NO_TITLE = 4;

    public ProfileFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_profile, container, false);

        categories = new ArrayList<>();
        getData();
        setTitles();
        setIcons();
        return rootView;
    }


    public void getData(){
        Locations locations = new Locations(getContext());
        Map<String, Integer> allCounts = locations.getCategoryCount();
        Visits visits = new Visits(getContext());
        Map<String, Integer> visitedCounts = visits.getCategoryCount();

        for (String key: allCounts.keySet()){
            if (visitedCounts.containsKey(key)){
                categories.add(new Model(key, allCounts.get(key), visitedCounts.get(key)));
            } else {
                categories.add(new Model(key, allCounts.get(key), 0));
            }
        }
    }

    public void setTitles(){
        Categories categoriesInfo = new Categories();
        TextView goldTitles = (TextView) rootView.findViewById(R.id.gold_titles);
        TextView silverTitles = (TextView) rootView.findViewById(R.id.silver_titles);
        TextView bronzeTitles = (TextView) rootView.findViewById(R.id.bronze_titles);
        for (int i=0; i<categories.size(); i++){
            int title_level = categories.get(i).getTitleLevel();
            switch (title_level){
                case GOLD_TITLE: {
                    CharSequence text = goldTitles.getText();
                    if (text.length() > 0){
                        text = text + "\n";
                    }
                    text = text + categoriesInfo.getGoldTitleByName(categories.get(i).category);
                    goldTitles.setText(text);
                    int id = getResources().getIdentifier("profile_" + categories.get(i).category, "id", getContext().getPackageName());
                    ImageView profileImage = (ImageView) rootView.findViewById(id);
                    profileImage.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gold));
                    break;
                }
                case SILVER_TITLE: {
                    CharSequence text = silverTitles.getText();
                    if (text.length() > 0){
                        text = text + "\n";
                    }
                    text = text + categoriesInfo.getSilverTitleByName(categories.get(i).category);
                    silverTitles.setText(text);
                    int id = getResources().getIdentifier("profile_" + categories.get(i).category, "id", getContext().getPackageName());
                    ImageView profileImage = (ImageView) rootView.findViewById(id);
                    profileImage.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.silver));
                    break;
                }
                case BRONZE_TITLE: {
                    CharSequence text = bronzeTitles.getText();
                    if (text.length() > 0){
                        text = text + "\n";
                    }
                    text = text + categoriesInfo.getBronzeTitleByName(categories.get(i).category);
                    bronzeTitles.setText(text);
                    int id = getResources().getIdentifier("profile_" + categories.get(i).category, "id", getContext().getPackageName());
                    ImageView profileImage = (ImageView) rootView.findViewById(id);
                    profileImage.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bronze));
                    break;
                }
            }

        }
    }

    public void setIcons(){

    }

    class Model {

        protected String category;
        protected Integer all;
        protected Integer visited;

        public Model(String category, Integer all, Integer visited){
            this.category = category;
            this.all = all;
            this.visited = visited;
        }

        public int getTitleLevel(){
            Double rate =  visited.doubleValue()/ all.doubleValue();
            if (rate >= 0.75){
                return GOLD_TITLE;
            } else if (rate >= 0.5){
                return SILVER_TITLE;
            } else if (rate >= 0.25){
                return BRONZE_TITLE;
            }
            return NO_TITLE;
        }
    }
}
