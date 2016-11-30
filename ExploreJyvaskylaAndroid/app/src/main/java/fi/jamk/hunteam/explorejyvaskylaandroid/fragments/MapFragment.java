package fi.jamk.hunteam.explorejyvaskylaandroid.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fi.jamk.hunteam.explorejyvaskylaandroid.AddPlaceActivity;
import fi.jamk.hunteam.explorejyvaskylaandroid.DatabaseHelper;
import fi.jamk.hunteam.explorejyvaskylaandroid.InterestingPlace;
import fi.jamk.hunteam.explorejyvaskylaandroid.R;
import fi.jamk.hunteam.explorejyvaskylaandroid.serverconnection.GetPlacesFromServer;


public class MapFragment extends Fragment implements GetPlacesFromServer.GetPlacesCallBack {

    Context context;
    MapView mMapView;
    private GoogleMap googleMap;
    private Marker userMarker;
    private ArrayList<InterestingPlace> interestingPlaces;
    private double epsilonLatLng = 0.001;
    private SQLiteDatabase db;

    public MapFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        context = getActivity().getApplicationContext();
        db = (new DatabaseHelper(context)).getWritableDatabase();

        View rootView = inflater.inflate(R.layout.activity_maps, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                getInterestingPlaces();
                getUserLocation();
            }
        });

        return rootView;
    }

    /**
     * Set up a Location Manager to influence what should happen when the user's location changed.
     * @throws SecurityException Need permission from the user
     */
    public void getUserLocation() throws SecurityException{
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                if (userMarker == null) {
                    userMarker = googleMap.addMarker(new MarkerOptions()
                            .position(userLocation)
                            .title("You are here")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.user))
                    );
                }
                else {
                    userMarker.setPosition(userLocation);
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14.0f));
                /* Check is there any unvisited place in the near. */
                checkNearPlaces();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    /**
     * Get the interesting places from the server and put markers on the map.
     */
    public void getInterestingPlaces(){
        interestingPlaces = new ArrayList<InterestingPlace>();
        /*for (int i=0; i<interestingPlaces.size(); i++){
            mMap.addMarker(new MarkerOptions()
                    .position(interestingPlaces.get(i).getPosition())
                    .title(interestingPlaces.get(i).getName())
            );
        }*/
        new GetPlacesFromServer(this).execute();
    }

    public void checkNearPlaces(){
        if (interestingPlaces != null){
            for (int i=0; i<interestingPlaces.size(); i++){
                InterestingPlace place = interestingPlaces.get(i);
                if (getDistance(userMarker.getPosition().latitude, place.getPosition().latitude) < epsilonLatLng
                        && getDistance(userMarker.getPosition().longitude, place.getPosition().longitude) < epsilonLatLng){
                    if (isPlaceVisitedAlready(place)){
                        Toast.makeText(context, "Already visited: " + place.getName(), Toast.LENGTH_LONG).show();
                    }
                    else {
                        addVisitedPlaceToDatabase(place);
                        Toast.makeText(context, "Add to db: " + place.getName(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    public double getDistance(double param1, double param2){
        return Math.abs(param1 - param2);
    }

    public void addVisitedPlaceToDatabase(InterestingPlace place){
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", place.getId());
        contentValues.put(DatabaseHelper.VPT_NAME, place.getName());
        contentValues.put(DatabaseHelper.VPT_LAT, place.getPosition().latitude);
        contentValues.put(DatabaseHelper.VPT_LNG, place.getPosition().longitude);
        db.insert(DatabaseHelper.VISITED_PLACES_TABLE, null, contentValues);
    }

    public boolean isPlaceVisitedAlready(InterestingPlace place){
        Cursor cursor = db.rawQuery("SELECT _id from " + DatabaseHelper.VISITED_PLACES_TABLE
                + " WHERE _id LIKE " + place.getId(), null);
        if (cursor.getCount() == 0){
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void onRemoteCallComplete(String jsonString) {
        interestingPlaces = new ArrayList<InterestingPlace>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("ID");
                double lat = jsonObject.getDouble("Latitude");
                double lng = jsonObject.getDouble("Longitude");
                String name = jsonObject.getString("Name");
                String type = jsonObject.getString("Type");
                InterestingPlace place = new InterestingPlace(id, name, lat, lng, type);
                interestingPlaces.add(place);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("No JSON string");
            e.printStackTrace();
        }
        System.out.println("Received places : " + interestingPlaces.size());
        for (int i=0; i<interestingPlaces.size(); i++){
            googleMap.addMarker(new MarkerOptions()
                    .position(interestingPlaces.get(i).getPosition())
                    .title(interestingPlaces.get(i).getName())
            );
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
