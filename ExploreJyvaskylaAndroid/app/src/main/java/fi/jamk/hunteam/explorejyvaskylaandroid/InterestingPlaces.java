package fi.jamk.hunteam.explorejyvaskylaandroid;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by DoubleD on 2016. 10. 21..
 */

public class InterestingPlaces {

    private ArrayList<Place> interestingPlaces;

    public InterestingPlaces(){
        interestingPlaces = new ArrayList<>();
        interestingPlaces.add(new Place(0, "Keski-suomen Museo", 62.2327829, 25.7215427));
        interestingPlaces.add(new Place(1, "Jyväskylän Taidemuseo", 62.2409732, 25.7364265));
    }

    public ArrayList<Place> getInterestingPlaces() {
        return interestingPlaces;
    }

    public class Place {
        private int id;
        private String name;
        private LatLng position;

        public Place(int _id, String _name, double _lat, double _lng){
            id = _id;
            name = _name;
            position = new LatLng(_lat, _lng);
        }

        public LatLng getPosition() {
            return position;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }
}
