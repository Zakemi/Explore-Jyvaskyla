package fi.jamk.hunteam.explorejyvaskylaandroid.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import fi.jamk.hunteam.explorejyvaskylaandroid.InterestingPlace;

/**
 * Created by DoubleD on 2016. 12. 01..
 */

public class Locations extends SQLiteOpenHelper{

    private SQLiteDatabase db;

    private static final String DATABASE = "database";
    private static final String TABLE = "locations";
    private static final String ID = "ID";
    private static final String LATITUDE = "Latitude";
    private static final String LONGITUDE = "Longitude";
    private static final String NAME = "Name";
    private static final String TYPE = "Type";
    private static final String ADDRESS = "Address";
    private static final String PHONE = "Phone";
    private static final String WEB = "Web";

    public Locations(Context context) {
        super(context, DATABASE, null, 1);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("____ CREATE locations TABLE");
        db.execSQL("CREATE TABLE " + TABLE
            + " ( " + ID + " INTEGER PRIMARY KEY, "
            + LATITUDE + " REAL, "
            + LONGITUDE + " REAL, "
            + NAME + " TEXT, "
            + TYPE + " TEXT, "
            + ADDRESS + " TEXT, "
            + PHONE + " TEXT, "
            + WEB + " TEXT);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public void addPlace(InterestingPlace place){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(ID, place.getId());
        content.put(LATITUDE, place.getPosition().latitude);
        content.put(LONGITUDE, place.getPosition().longitude);
        content.put(NAME, place.getName());
        content.put(TYPE, place.getType());
        content.put(ADDRESS, place.getAddress());
        content.put(PHONE, place.getPhone());
        content.put(WEB, place.getWeb());
        db.insert(TABLE, null, content);
    }

    public void addPlaces(List<InterestingPlace> places){
        for (int i=0; i<places.size(); i++){
            addPlace(places.get(i));
        }
    }

    public List<InterestingPlace> getPlaces(){
        List<InterestingPlace> places = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE, null);

        cursor.moveToFirst();
        for (int i=0; i<cursor.getCount(); i++){
            InterestingPlace place = new InterestingPlace(
                    cursor.getInt(cursor.getColumnIndex(ID)),
                    cursor.getString(cursor.getColumnIndex(NAME)),
                    cursor.getDouble(cursor.getColumnIndex(LATITUDE)),
                    cursor.getDouble(cursor.getColumnIndex(LONGITUDE)),
                    cursor.getString(cursor.getColumnIndex(TYPE)),
                    cursor.getString(cursor.getColumnIndex(ADDRESS)),
                    cursor.getString(cursor.getColumnIndex(PHONE)),
                    cursor.getString(cursor.getColumnIndex(WEB))
            );
            places.add(place);
            cursor.moveToNext();
        }
        return places;
    }

}
