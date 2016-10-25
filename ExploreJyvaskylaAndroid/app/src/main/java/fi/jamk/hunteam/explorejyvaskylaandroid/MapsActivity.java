package fi.jamk.hunteam.explorejyvaskylaandroid;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ServerConnection.GetJSONData {

    private GoogleMap mMap;
    private Marker userMarker;
    private ArrayList<InterestingPlace> interestingPlaces;
    private double epsilonLatLng = 0.001;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        db = (new DatabaseHelper(this)).getWritableDatabase();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getInterestingPlaces();
        getUserLocation();
    }

    /**
     * Set up a Location Manager to influence what should happen when the user's location changed.
     * @throws SecurityException Need permission from the user
     */
    public void getUserLocation() throws SecurityException{
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                if (userMarker == null) {
                    userMarker = mMap.addMarker(new MarkerOptions()
                            .position(userLocation)
                            .title("You are here")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.user))
                    );
                }
                else {
                    userMarker.setPosition(userLocation);
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 14.0f));
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
        new ServerConnection(this).execute();
    }

    public void checkNearPlaces(){
        if (interestingPlaces != null){
            for (int i=0; i<interestingPlaces.size(); i++){
                InterestingPlace place = interestingPlaces.get(i);
                if (getDistance(userMarker.getPosition().latitude, place.getPosition().latitude) < epsilonLatLng
                        && getDistance(userMarker.getPosition().longitude, place.getPosition().longitude) < epsilonLatLng){
                    if (isPlaceVisitedAlready(place)){
                        Toast.makeText(getApplicationContext(), "Already visited: " + place.getName(), Toast.LENGTH_LONG).show();
                    }
                    else {
                        addVisitedPlaceToDatabase(place);
                        Toast.makeText(getApplicationContext(), "Add to db: " + place.getName(), Toast.LENGTH_LONG).show();
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
        System.out.println(jsonString);
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
        }
        System.out.println("Received places : " + interestingPlaces.size());
        for (int i=0; i<interestingPlaces.size(); i++){
            mMap.addMarker(new MarkerOptions()
                    .position(interestingPlaces.get(i).getPosition())
                    .title(interestingPlaces.get(i).getName())
            );
        }
    }
}
