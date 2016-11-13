package fi.jamk.hunteam.explorejyvaskylaandroid;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.awareness.snapshot.internal.PlacesData;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceTypes;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AddPlaceActivity extends AppCompatActivity {

    static final int PLACE_PICK_REQUEST = 1;
    static OwnPlaceTypes placeTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        Spinner spinner = (Spinner) findViewById(R.id.add_place_type);
        placeTypes = new OwnPlaceTypes();
        List<String> types = placeTypes.array;
        types.add(0, getString(R.string.please_select));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, R.layout.support_simple_spinner_dropdown_item, types);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void pickPlace(View view){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
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
                ((EditText) findViewById(R.id.add_place_name)).setText(place.getName());
                ((EditText) findViewById(R.id.add_place_address)).setText(place.getAddress());
                ((EditText) findViewById(R.id.add_place_lat)).setText((String.valueOf(place.getLatLng().latitude)));
                ((EditText) findViewById(R.id.add_place_lng)).setText((String.valueOf(place.getLatLng().longitude)));
                ((EditText) findViewById(R.id.add_place_phone)).setText(place.getPhoneNumber());
                if (place.getWebsiteUri() != null)
                    ((EditText) findViewById(R.id.add_place_web)).setText(place.getWebsiteUri().toString());
                if (place.getPlaceTypes().size() > 0){
                    Spinner spinner = (Spinner) findViewById(R.id.add_place_type);
                    ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
                    int position = adapter.getPosition(placeTypes.dict.get(place.getPlaceTypes().get(0)));
                    spinner.setSelection(position);
                }
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
            //todo send data to server
            Toast.makeText(this, "Thanks for the new place!", Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            Toast.makeText(this, "Please check the fields, something is wrong.", Toast.LENGTH_LONG).show();
        }
    }
}
