package fi.jamk.hunteam.explorejyvaskylaandroid;

import com.google.android.gms.location.places.Place;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DoubleD on 2016. 11. 13..
 */

public class OwnPlaceTypes {

    public Map<Integer, String> dict;
    public List<String> array;

    public OwnPlaceTypes(){
        dict = new HashMap<>();

        //dict.put(Place.TYPE_ACCOUNTING, "TYPE_ACCOUNTING");
        //dict.put(Place.TYPE_ADMINISTRATIVE_AREA_LEVEL_1, "TYPE_ADMINISTRATIVE_AREA_LEVEL_1");
        //dict.put(Place.TYPE_ADMINISTRATIVE_AREA_LEVEL_2, "TYPE_ADMINISTRATIVE_AREA_LEVEL_2");
        //dict.put(Place.TYPE_ADMINISTRATIVE_AREA_LEVEL_3, "TYPE_ADMINISTRATIVE_AREA_LEVEL_3");
        //dict.put(Place.TYPE_AIRPORT, "TYPE_AIRPORT");
        dict.put(Place.TYPE_AMUSEMENT_PARK, "TYPE_AMUSEMENT_PARK");
        //dict.put(Place.TYPE_AQUARIUM, "TYPE_AQUARIUM");
        dict.put(Place.TYPE_ART_GALLERY, "TYPE_ART_GALLERY");
        //dict.put(Place.TYPE_ATM, "TYPE_ATM");
        dict.put(Place.TYPE_BAKERY, "TYPE_BAKERY");
        //dict.put(Place.TYPE_BANK, "TYPE_BANK");
        dict.put(Place.TYPE_BAR, "TYPE_BAR");
        //dict.put(Place.TYPE_BEAUTY_SALON, "TYPE_BEAUTY_SALON");
        //dict.put(Place.TYPE_BICYCLE_STORE, "TYPE_BICYCLE_STORE");
        //dict.put(Place.TYPE_BOOK_STORE, "TYPE_BOOK_STORE");
        dict.put(Place.TYPE_BOWLING_ALLEY, "TYPE_BOWLING_ALLEY");
        //dict.put(Place.TYPE_BUS_STATION, "TYPE_BUS_STATION");
        dict.put(Place.TYPE_CAFE, "TYPE_CAFE");
        dict.put(Place.TYPE_CAMPGROUND, "TYPE_CAMPGROUND");
        //dict.put(Place.TYPE_CAR_DEALER, "TYPE_CAR_DEALER");
        //dict.put(Place.TYPE_CAR_RENTAL, "TYPE_CAR_RENTAL");
        //dict.put(Place.TYPE_CAR_REPAIR, "TYPE_CAR_REPAIR");
        //dict.put(Place.TYPE_CAR_WASH, "TYPE_CAR_WASH");
        dict.put(Place.TYPE_CASINO, "TYPE_CASINO");
        //dict.put(Place.TYPE_CEMETERY, "TYPE_CEMETERY");
        dict.put(Place.TYPE_CHURCH, "TYPE_CHURCH");
        //dict.put(Place.TYPE_CITY_HALL, "TYPE_CITY_HALL");
        //dict.put(Place.TYPE_CLOTHING_STORE, "TYPE_CLOTHING_STORE");
        //dict.put(Place.TYPE_COLLOQUIAL_AREA, "TYPE_COLLOQUIAL_AREA");
        //dict.put(Place.TYPE_CONVENIENCE_STORE, "TYPE_CONVENIENCE_STORE");
        //dict.put(Place.TYPE_COUNTRY, "TYPE_COUNTRY");
        //dict.put(Place.TYPE_COURTHOUSE, "TYPE_COURTHOUSE");
        //dict.put(Place.TYPE_DENTIST, "TYPE_DENTIST");
        //dict.put(Place.TYPE_DEPARTMENT_STORE, "TYPE_DEPARTMENT_STORE");
        //dict.put(Place.TYPE_DOCTOR, "TYPE_DOCTOR");
        //dict.put(Place.TYPE_ELECTRICIAN, "TYPE_ELECTRICIAN");
        //dict.put(Place.TYPE_ELECTRONICS_STORE, "TYPE_ELECTRONICS_STORE");
        //dict.put(Place.TYPE_EMBASSY, "TYPE_EMBASSY");
        //dict.put(Place.TYPE_ESTABLISHMENT, "TYPE_ESTABLISHMENT");
        //dict.put(Place.TYPE_FINANCE, "TYPE_FINANCE");
        //dict.put(Place.TYPE_FIRE_STATION, "TYPE_FIRE_STATION");
        //dict.put(Place.TYPE_FLOOR, "TYPE_FLOOR");
        //dict.put(Place.TYPE_FLORIST, "TYPE_FLORIST");
        dict.put(Place.TYPE_FOOD, "TYPE_FOOD");
        //dict.put(Place.TYPE_FUNERAL_HOME, "TYPE_FUNERAL_HOME");
        //dict.put(Place.TYPE_FURNITURE_STORE, "TYPE_FURNITURE_STORE");
        //dict.put(Place.TYPE_GAS_STATION, "TYPE_GAS_STATION");
        //dict.put(Place.TYPE_GENERAL_CONTRACTOR, "TYPE_GENERAL_CONTRACTOR");
        //dict.put(Place.TYPE_GEOCODE, "TYPE_GEOCODE");
        //dict.put(Place.TYPE_GROCERY_OR_SUPERMARKET, "TYPE_GROCERY_OR_SUPERMARKET");
        dict.put(Place.TYPE_GYM, "TYPE_GYM");
        //dict.put(Place.TYPE_HAIR_CARE, "TYPE_HAIR_CARE");
        //dict.put(Place.TYPE_HARDWARE_STORE, "TYPE_HARDWARE_STORE");
        //dict.put(Place.TYPE_HEALTH, "TYPE_HEALTH");
        //dict.put(Place.TYPE_HINDU_TEMPLE, "TYPE_HINDU_TEMPLE");
        //dict.put(Place.TYPE_HOME_GOODS_STORE, "TYPE_HOME_GOODS_STORE");
        //dict.put(Place.TYPE_HOSPITAL, "TYPE_HOSPITAL");
        //dict.put(Place.TYPE_INSURANCE_AGENCY, "TYPE_INSURANCE_AGENCY");
        //dict.put(Place.TYPE_INTERSECTION, "TYPE_INTERSECTION");
        //dict.put(Place.TYPE_JEWELRY_STORE, "TYPE_JEWELRY_STORE");
        //dict.put(Place.TYPE_LAUNDRY, "TYPE_LAUNDRY");
        //dict.put(Place.TYPE_LAWYER, "TYPE_LAWYER");
        dict.put(Place.TYPE_LIBRARY, "TYPE_LIBRARY");
        //dict.put(Place.TYPE_LIQUOR_STORE, "TYPE_LIQUOR_STORE");
        //dict.put(Place.TYPE_LOCALITY, "TYPE_LOCALITY");
        //dict.put(Place.TYPE_LOCAL_GOVERNMENT_OFFICE, "TYPE_LOCAL_GOVERNMENT_OFFICE");
        //dict.put(Place.TYPE_LOCKSMITH, "TYPE_LOCKSMITH");
        //dict.put(Place.TYPE_LODGING, "TYPE_LODGING");
        //dict.put(Place.TYPE_MEAL_DELIVERY, "TYPE_MEAL_DELIVERY");
        //dict.put(Place.TYPE_MEAL_TAKEAWAY, "TYPE_MEAL_TAKEAWAY");
        //dict.put(Place.TYPE_MOSQUE, "TYPE_MOSQUE");
        //dict.put(Place.TYPE_MOVIE_RENTAL, "TYPE_MOVIE_RENTAL");
        dict.put(Place.TYPE_MOVIE_THEATER, "TYPE_MOVIE_THEATER");
        //dict.put(Place.TYPE_MOVING_COMPANY, "TYPE_MOVING_COMPANY");
        dict.put(Place.TYPE_MUSEUM, "TYPE_MUSEUM");
        dict.put(Place.TYPE_NATURAL_FEATURE, "TYPE_NATURAL_FEATURE");
        //dict.put(Place.TYPE_NEIGHBORHOOD, "TYPE_NEIGHBORHOOD");
        dict.put(Place.TYPE_NIGHT_CLUB, "TYPE_NIGHT_CLUB");
        dict.put(Place.TYPE_OTHER, "TYPE_OTHER");
        //dict.put(Place.TYPE_PAINTER, "TYPE_PAINTER");
        dict.put(Place.TYPE_PARK, "TYPE_PARK");
        //dict.put(Place.TYPE_PARKING, "TYPE_PARKING");
        //dict.put(Place.TYPE_PET_STORE, "TYPE_PET_STORE");
        //dict.put(Place.TYPE_PHARMACY, "TYPE_PHARMACY");
        //dict.put(Place.TYPE_PHYSIOTHERAPIST, "TYPE_PHYSIOTHERAPIST");
        //dict.put(Place.TYPE_PLACE_OF_WORSHIP, "TYPE_PLACE_OF_WORSHIP");
        //dict.put(Place.TYPE_PLUMBER, "TYPE_PLUMBER");
        //dict.put(Place.TYPE_POINT_OF_INTEREST, "TYPE_POINT_OF_INTEREST");
        //dict.put(Place.TYPE_POLICE, "TYPE_POLICE");
        //dict.put(Place.TYPE_POLITICAL, "TYPE_POLITICAL");
        //dict.put(Place.TYPE_POSTAL_CODE, "TYPE_POSTAL_CODE");
        //dict.put(Place.TYPE_POSTAL_CODE_PREFIX, "TYPE_POSTAL_CODE_PREFIX");
        //dict.put(Place.TYPE_POSTAL_TOWN, "TYPE_POSTAL_TOWN");
        //dict.put(Place.TYPE_POST_BOX, "TYPE_POST_BOX");
        //dict.put(Place.TYPE_POST_OFFICE, "TYPE_POST_OFFICE");
        //dict.put(Place.TYPE_PREMISE, "TYPE_PREMISE");
        //dict.put(Place.TYPE_REAL_ESTATE_AGENCY, "TYPE_REAL_ESTATE_AGENCY");
        dict.put(Place.TYPE_RESTAURANT, "TYPE_RESTAURANT");
        //dict.put(Place.TYPE_ROOFING_CONTRACTOR, "TYPE_ROOFING_CONTRACTOR");
        //dict.put(Place.TYPE_ROOM, "TYPE_ROOM");
        //dict.put(Place.TYPE_ROUTE, "TYPE_ROUTE");
        //dict.put(Place.TYPE_RV_PARK, "TYPE_RV_PARK");
        //dict.put(Place.TYPE_SCHOOL, "TYPE_SCHOOL");
        //dict.put(Place.TYPE_SHOE_STORE, "TYPE_SHOE_STORE");
        //dict.put(Place.TYPE_SHOPPING_MALL, "TYPE_SHOPPING_MALL");
        //dict.put(Place.TYPE_SPA, "TYPE_SPA");
        //dict.put(Place.TYPE_STADIUM, "TYPE_STADIUM");
        //dict.put(Place.TYPE_STORAGE, "TYPE_STORAGE");
        //dict.put(Place.TYPE_STORE, "TYPE_STORE");
        //dict.put(Place.TYPE_STREET_ADDRESS, "TYPE_STREET_ADDRESS");
        //dict.put(Place.TYPE_SUBLOCALITY, "TYPE_SUBLOCALITY");
        //dict.put(Place.TYPE_SUBLOCALITY_LEVEL_1, "TYPE_SUBLOCALITY_LEVEL_1");
        //dict.put(Place.TYPE_SUBLOCALITY_LEVEL_2, "TYPE_SUBLOCALITY_LEVEL_2");
        //dict.put(Place.TYPE_SUBLOCALITY_LEVEL_3, "TYPE_SUBLOCALITY_LEVEL_3");
        //dict.put(Place.TYPE_SUBLOCALITY_LEVEL_4, "TYPE_SUBLOCALITY_LEVEL_4");
        //dict.put(Place.TYPE_SUBLOCALITY_LEVEL_5, "TYPE_SUBLOCALITY_LEVEL_5");
        //dict.put(Place.TYPE_SUBPREMISE, "TYPE_SUBPREMISE");
        //dict.put(Place.TYPE_SUBWAY_STATION, "TYPE_SUBWAY_STATION");
        //dict.put(Place.TYPE_SYNAGOGUE, "TYPE_SYNAGOGUE");
        //ict.put(Place.TYPE_SYNTHETIC_GEOCODE, "TYPE_SYNTHETIC_GEOCODE");
        //dict.put(Place.TYPE_TAXI_STAND, "TYPE_TAXI_STAND");
        //dict.put(Place.TYPE_TRAIN_STATION, "TYPE_TRAIN_STATION");
        //dict.put(Place.TYPE_TRANSIT_STATION, "TYPE_TRANSIT_STATION");
        //dict.put(Place.TYPE_TRAVEL_AGENCY, "TYPE_TRAVEL_AGENCY");
        dict.put(Place.TYPE_UNIVERSITY, "TYPE_UNIVERSITY");
        //dict.put(Place.TYPE_VETERINARY_CARE, "TYPE_VETERINARY_CARE");
        dict.put(Place.TYPE_ZOO, "TYPE_ZOO");


        array = new ArrayList<>(dict.values());
        Collections.sort(array, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
    }
}
