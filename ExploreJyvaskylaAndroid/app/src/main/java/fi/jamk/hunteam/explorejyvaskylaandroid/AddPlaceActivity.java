package fi.jamk.hunteam.explorejyvaskylaandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

import fi.jamk.hunteam.explorejyvaskylaandroid.model.Categories;
import fi.jamk.hunteam.explorejyvaskylaandroid.serverconnection.PostPlaceToServer;

public class AddPlaceActivity extends AppCompatActivity implements PostPlaceToServer.PostPlaceCallBack {

    static final int PLACE_PICK_REQUEST = 1;
    static Categories categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        Spinner spinner = (Spinner) findViewById(R.id.add_place_type);
        categories = new Categories();
        List<String> categoriesList = categories.getCategoryNames();
        categoriesList.add(0, getString(R.string.please_select));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, R.layout.support_simple_spinner_dropdown_item, categoriesList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void pickPlace(View view){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        builder.setLatLngBounds(new LatLngBounds.Builder().include(new LatLng(62.2401672,25.7484592)).build());
        try {
            startActivityForResult(builder.build(this), PLACE_PICK_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICK_REQUEST){
            if (resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(data, this);
                ((EditText) findViewById(R.id.add_place_id)).setText(place.getId());
                ((EditText) findViewById(R.id.add_place_name)).setText(place.getName());
                ((EditText) findViewById(R.id.add_place_address)).setText(place.getAddress());
                ((EditText) findViewById(R.id.add_place_lat)).setText((String.valueOf(place.getLatLng().latitude)));
                ((EditText) findViewById(R.id.add_place_lng)).setText((String.valueOf(place.getLatLng().longitude)));
                ((EditText) findViewById(R.id.add_place_phone)).setText(place.getPhoneNumber());
                if (place.getWebsiteUri() != null)
                    ((EditText) findViewById(R.id.add_place_web)).setText(place.getWebsiteUri().toString());
                /*if (place.getPlaceTypes().size() > 0){
                    Spinner spinner = (Spinner) findViewById(R.id.add_place_type);
                    ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
                    int position = adapter.getPosition(placeTypes.dict.get(place.getPlaceTypes().get(0)));
                    spinner.setSelection(position);
                }*/
            }
        }
    }

    private boolean validateFields(){
        if (((EditText) findViewById(R.id.add_place_name)).getText().toString().equals("")){
            return false;
        } else if (((EditText) findViewById(R.id.add_place_address)).getText().toString().equals("")){
            return false;
        } else if (((EditText) findViewById(R.id.add_place_lat)).getText().toString().equals("")){
            return false;
        } else if (((EditText) findViewById(R.id.add_place_lng)).getText().toString().equals("")){
            return false;
        } else if (((EditText) findViewById(R.id.add_place_lat)).getText().toString().equals("")){
            return false;
        } else if (((Spinner) findViewById(R.id.add_place_type)).getSelectedItemPosition() == 0){
            return false;
        }
        return true;
    }

    public void addPlace(View view){
        if (validateFields()){
            String id = ((EditText) findViewById(R.id.add_place_id)).getText().toString();
            String name = ((EditText) findViewById(R.id.add_place_name)).getText().toString();
            String address = ((EditText) findViewById(R.id.add_place_address)).getText().toString();
            String lat = ((EditText) findViewById(R.id.add_place_lat)).getText().toString();
            String lng = ((EditText) findViewById(R.id.add_place_lng)).getText().toString();
            String type = ((Spinner) findViewById(R.id.add_place_type)).getSelectedItem().toString();
            String phone = ((EditText) findViewById(R.id.add_place_phone)).getText().toString();
            String web = ((EditText) findViewById(R.id.add_place_web)).getText().toString();
            new PostPlaceToServer(this).execute(id, name, address, lat, lng, type, phone, web);
            Toast.makeText(this, "Place addiction on progress...", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Please check the fields, something is wrong.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRemoteCallComplete(String json) {
        Toast.makeText(this, "Thanks for the new place!", Toast.LENGTH_LONG).show();
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
