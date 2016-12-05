package fi.jamk.hunteam.explorejyvaskylaandroid.fragments;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.jamk.hunteam.explorejyvaskylaandroid.model.InterestingPlace;
import fi.jamk.hunteam.explorejyvaskylaandroid.R;
import fi.jamk.hunteam.explorejyvaskylaandroid.database.Locations;
import fi.jamk.hunteam.explorejyvaskylaandroid.database.Visits;
import fi.jamk.hunteam.explorejyvaskylaandroid.serverconnection.GetPlacesFromServer;


public class MapFragment extends Fragment implements GetPlacesFromServer.GetPlacesCallBack {

    private Context context;
    private MapView mMapView;
    private GoogleMap googleMap;
    private Marker userMarker;
    private Map<Marker, InterestingPlace> placeMarkersAndData;
    private List<InterestingPlace> interestingPlaces;
    private double epsilonLatLng = 0.001;
    private Locations locationsDatabase;
    private Visits visitsDatabase;
    private final LatLng cityLocation = new LatLng(62.2429368, 25.7476247);

    public MapFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        context = getActivity().getApplicationContext();
        visitsDatabase = new Visits(context);
        locationsDatabase = new Locations(context);

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
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cityLocation, 14.0f));
                getInterestingPlaces();
                getUserLocation();

                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        View view = getActivity().getLayoutInflater().inflate(R.layout.infowindow, null);
                        TextView name = (TextView) view.findViewById(R.id.info_name);
                        TextView address = (TextView) view.findViewById(R.id.info_address);
                        TextView phone = (TextView) view.findViewById(R.id.info_phone);
                        TextView web = (TextView) view.findViewById(R.id.info_web);
                        InterestingPlace place = placeMarkersAndData.get(marker);
                        if (place != null){
                            if (!place.getName().equals("null") && !place.getName().equals(""))
                                name.setText(place.getName());
                            else
                                name.setVisibility(View.GONE);

                            if (!place.getAddress().equals("null") && !place.getAddress().equals(""))
                                address.setText(place.getAddress());
                            else
                                address.setVisibility(View.GONE);

                            if (!place.getPhone().equals("null") && !place.getPhone().equals(""))
                                phone.setText(place.getPhone());
                            else
                                phone.setVisibility(View.GONE);

                            if (!place.getWeb().equals("null") && !place.getWeb().equals(""))
                                web.setText(place.getWeb());
                            else
                                web.setVisibility(View.GONE);
                        }else if (marker.getPosition().longitude == userMarker.getPosition().longitude &&
                                marker.getPosition().latitude == userMarker.getPosition().latitude){
                            name.setText("You");
                            address.setVisibility(View.GONE);
                            phone.setVisibility(View.GONE);
                            web.setVisibility(View.GONE);
                        }
                        return view;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        return null;
                    }
                });
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
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.user2))
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

    public void setMapCamera(){
        if (userMarker != null && userMarker.getPosition() != null){
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userMarker.getPosition(), 14.0f));
        } else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cityLocation, 14.0f));
        }
    }

    /**
     * Get the interesting places from the server and put markers on the map.
     */
    public void getInterestingPlaces(){
        // Get places from database
        interestingPlaces = locationsDatabase.getPlaces();
        System.out.println("____****____ New places");
        // Add place markers on the map
        addPlaceMarkers();

        // Try to fresh the places from the server
        new GetPlacesFromServer(this).execute();
    }

    public void getInterestingPlacesInCheckedCategories(List<String> categories){
        interestingPlaces = locationsDatabase.getPlacesInCategories(categories);
        System.out.println("____****____ New places");
        addPlaceMarkers();
    }

    public void checkNearPlaces(){
        if (interestingPlaces != null){
            for (int i=0; i<interestingPlaces.size(); i++){
                InterestingPlace place = interestingPlaces.get(i);
                if (getDistance(userMarker.getPosition().latitude, place.getPosition().latitude) < epsilonLatLng
                        && getDistance(userMarker.getPosition().longitude, place.getPosition().longitude) < epsilonLatLng){
                    if (visitsDatabase.isPlaceVisitedAlready(place)){
                        Toast.makeText(context, "Already visited: " + place.getName(), Toast.LENGTH_LONG).show();
                    }
                    else {
                        visitsDatabase.addVisitedPlaceToDatabase(place);
                        Toast.makeText(context, "Add to db: " + place.getName(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    public double getDistance(double param1, double param2){
        return Math.abs(param1 - param2);
    }

    @Override
    public void onRemoteCallComplete(String jsonString) {
        interestingPlaces = new ArrayList<InterestingPlace>();
        System.out.println("____****____ New places");
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("ID");
                double lat = jsonObject.getDouble("Latitude");
                double lng = jsonObject.getDouble("Longitude");
                String name = jsonObject.getString("Name");
                String type = jsonObject.getString("Type");
                String address = jsonObject.getString("Address");
                String phone = jsonObject.getString("Phone");
                String web = jsonObject.getString("Web");
                InterestingPlace place = new InterestingPlace(id, name, lat, lng, type, address, phone, web);
                interestingPlaces.add(place);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("No JSON string");
            e.printStackTrace();
        }
        System.out.println("Received places : " + interestingPlaces.size());
        // Save places to the database
        new Locations(context).addPlaces(interestingPlaces);
        // Add place markers to the map
        addPlaceMarkers();
    }

    private void addPlaceMarkers(){
        System.out.println("____####____ New markers");
        if (placeMarkersAndData != null){
            for (Marker marker: placeMarkersAndData.keySet()){
                marker.remove();
            }
        }
        placeMarkersAndData = new HashMap<>();
        for (int i=0; i<interestingPlaces.size(); i++){
            // TODO
            int id = context.getResources().getIdentifier("@drawable/"+interestingPlaces.get(i).getType()+"_icon", "drawable", getActivity().getPackageName());
            placeMarkersAndData.put(googleMap.addMarker(new MarkerOptions()
                    .position(interestingPlaces.get(i).getPosition())
                    .title(interestingPlaces.get(i).getName() + "(" + interestingPlaces.get(i).getType() + ")")
                    .icon(BitmapDescriptorFactory.fromResource(id))
            ), interestingPlaces.get(i));
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
