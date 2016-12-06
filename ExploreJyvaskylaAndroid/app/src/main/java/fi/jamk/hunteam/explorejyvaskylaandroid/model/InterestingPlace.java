package fi.jamk.hunteam.explorejyvaskylaandroid.model;

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
    private String address;
    private String phone;
    private String web;
    private Double rate;

    public InterestingPlace(int _id, String _name, double _lat, double _lng, String _type,
                            String _address, String _phone, String _web, Double _rate){
        id = _id;
        name = _name;
        position = new LatLng(_lat, _lng);
        type = _type;
        address = _address;
        phone = _phone;
        web = _web;
        rate = _rate;
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

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getWeb() {
        return web;
    }

    public Double getRate() {
        return rate;
    }
}
