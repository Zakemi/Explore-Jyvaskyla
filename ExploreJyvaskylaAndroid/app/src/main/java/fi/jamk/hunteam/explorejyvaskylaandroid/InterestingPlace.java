package fi.jamk.hunteam.explorejyvaskylaandroid;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by DoubleD on 2016. 10. 21..
 */

public class InterestingPlace {
    private int id;
    private String name;
    private LatLng position;
    private String type;

    public InterestingPlace(int _id, String _name, double _lat, double _lng, String _type){
        id = _id;
        name = _name;
        position = new LatLng(_lat, _lng);
        type = _type;
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

    public String getType() {
        return type;
    }
}
