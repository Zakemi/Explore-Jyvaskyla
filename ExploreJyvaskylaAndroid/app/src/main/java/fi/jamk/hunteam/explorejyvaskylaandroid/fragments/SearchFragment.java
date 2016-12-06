package fi.jamk.hunteam.explorejyvaskylaandroid.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fi.jamk.hunteam.explorejyvaskylaandroid.R;
import fi.jamk.hunteam.explorejyvaskylaandroid.model.Categories;

// The search screen. Used by the Main Activity

public class SearchFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener{

    private List<String> categories;
    public List<Model> models;
    private View rootView;

    public SearchFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);

        // Setup the listview
        ListView listView = (ListView) rootView.findViewById(R.id.categories_list);
        categories = new Categories().getCategoryNames();
        models = new ArrayList<>();
        for (String category: categories){
            models.add(new Model(category));
        }
        ArrayAdapter adapter = new CategoryArrayAdapter(getContext(), categories);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        // Setup the select all and nothing buttons
        RelativeLayout selectAll = (RelativeLayout) rootView.findViewById(R.id.select_all);
        selectAll.setOnClickListener(this);
        RelativeLayout selectNothing = (RelativeLayout) rootView.findViewById(R.id.select_nothing);
        selectNothing.setOnClickListener(this);

        return rootView;
    }

    // Click on an item in the listview
    // Change the status (selected or not) of the item
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        models.get(position).changeSelected();
        ImageView image = (ImageView) view.findViewById(R.id.category_image);
        if (models.get(position).isSelected()){
            image.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLight));
            view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLight));
        } else {
            image.setBackgroundColor(Color.TRANSPARENT);
            view.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onClick(View v) {
        // Click on the select all button
        if (v.getId() == R.id.select_all){
            ListView listView = (ListView) rootView.findViewById(R.id.categories_list);
            for (int i=0; i<listView.getAdapter().getCount(); i++){
                View rowView = listView.getChildAt(i -
                        listView.getFirstVisiblePosition());
                if (rowView != null){
                    rowView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLight));
                    models.get(i).setSelected(true);
                }
            }
        }
        // click on the select nothing button
        else if (v.getId() == R.id.select_nothing){
            ListView listView = (ListView) rootView.findViewById(R.id.categories_list);
            for (int i=0; i<listView.getAdapter().getCount(); i++){
                View rowView = listView.getChildAt(i -
                        listView.getFirstVisiblePosition());
                if (rowView != null){
                    rowView.setBackgroundColor(Color.TRANSPARENT);
                    models.get(i).setSelected(false);
                }
            }
        }
    }

    // Adapter for the listview
    public class CategoryArrayAdapter extends ArrayAdapter<String>{

        private Context context;
        private List<String> categories;

        public CategoryArrayAdapter(Context context, List<String> categories) {
            super(context, R.layout.listlayout_search, R.id.category_name, categories);
            this.context = context;
            this.categories = categories;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // setup the view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.listlayout_search, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.category_name);
            textView.setText(categories.get(position));
            ImageView imageView = (ImageView) rowView.findViewById(R.id.category_image);
            int id = context.getResources().getIdentifier("@drawable/"+categories.get(position), "drawable", getActivity().getPackageName());
            imageView.setImageResource(id);
            return rowView;
        }
    }

    // Local model for the class
    // Store the name of the category in the listview and the status (selected or not)
    public class Model {

        private String name;
        private boolean selected;

        public Model(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public void changeSelected(){
            this.selected = !this.selected;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("ONPAUSE");
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("ONSTOP");
    }
}
