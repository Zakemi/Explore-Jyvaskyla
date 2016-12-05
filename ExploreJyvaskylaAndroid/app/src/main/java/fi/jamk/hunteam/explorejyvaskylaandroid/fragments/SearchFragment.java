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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.fitness.request.ListClaimedBleDevicesRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fi.jamk.hunteam.explorejyvaskylaandroid.R;
import fi.jamk.hunteam.explorejyvaskylaandroid.model.Categories;

public class SearchFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener{

    private List<String> categories;
    public List<Model> models;
    private View rootView;

    public SearchFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_search, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.categories_list);
        categories = new Categories().getCategoryNames();
        models = new ArrayList<>();
        for (String category: categories){
            models.add(new Model(category));
        }
        ArrayAdapter adapter = new CategoryArrayAdapter(getContext(), categories);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        RelativeLayout selectAll = (RelativeLayout) rootView.findViewById(R.id.select_all);
        selectAll.setOnClickListener(this);
        RelativeLayout selectNothing = (RelativeLayout) rootView.findViewById(R.id.select_nothing);
        selectNothing.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // change the state of the checkbox
        models.get(position).changeSelected();
        ImageView image = (ImageView) view.findViewById(R.id.category_image);
        if (models.get(position).isSelected()){
            System.out.println(models.get(position).getName() + " with view id: " + view);
            image.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLight));
            view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLight));
        } else {
            image.setBackgroundColor(Color.TRANSPARENT);
            view.setBackgroundColor(Color.TRANSPARENT);
        }
        System.out.println("Clicked item!!!" + models.get(position).isSelected());
    }

    @Override
    public void onClick(View v) {
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
        } else if (v.getId() == R.id.select_nothing){
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
            // get row
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.listlayout_search, parent, false);
            // show phone name
            TextView textView = (TextView) rowView.findViewById(R.id.category_name);
            textView.setText(categories.get(position));
            // show phone icon/image
            ImageView imageView = (ImageView) rowView.findViewById(R.id.category_image);

            Resources resources = context.getResources();
            int id = context.getResources().getIdentifier("@drawable/"+categories.get(position), "drawable", getActivity().getPackageName());
            imageView.setImageResource(id);
            // return row view
            return rowView;
        }
    }

    public class Model {

        private String name;
        private boolean selected;
        private RelativeLayout view;

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
