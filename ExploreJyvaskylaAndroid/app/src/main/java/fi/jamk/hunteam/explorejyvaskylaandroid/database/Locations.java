package fi.jamk.hunteam.explorejyvaskylaandroid.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.jamk.hunteam.explorejyvaskylaandroid.model.InterestingPlace;

/**
 * Created by DoubleD on 2016. 12. 01..
 */

public class Locations extends SQLiteOpenHelper{

    private SQLiteDatabase db;

    public static final String DATABASE = "database";
    public static final String TABLE = "locations";
    public static final String ID = "ID";
    public static final String LATITUDE = "Latitude";
    public static final String LONGITUDE = "Longitude";
    public static final String NAME = "Name";
    public static final String TYPE = "Type";
    public static final String ADDRESS = "Address";
    public static final String PHONE = "Phone";
    public static final String WEB = "Web";
    public static final String RATE = "Rate";

    public Locations(Context context) {
        super(context, DATABASE, null, 1);
        try{
            onCreate(this.getWritableDatabase());
        } catch (android.database.sqlite.SQLiteException e){
            System.out.println("Locations is already created.");
        }
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
            + WEB + " TEXT, "
            + RATE + " REAL);"
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
        content.put(RATE, place.getRate());
        db.insert(TABLE, null, content);
        db.close();
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
                    cursor.getString(cursor.getColumnIndex(WEB)),
                    cursor.getDouble(cursor.getColumnIndex(RATE))
            );
            places.add(place);
            cursor.moveToNext();
        }
        db.close();
        return places;
    }

    public List<InterestingPlace> getPlacesInCategories(List<String> categories){
        List<InterestingPlace> places = new ArrayList<>();
        if (categories.size() == 0){
            return places;
        }
        String whereClause = TYPE + " = ?";
        for (int i=1; i<categories.size(); i++){
            whereClause += " OR " + TYPE + " = ?";
        }
        System.out.println(categories.size() + " : " + whereClause);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE, null, whereClause, categories.toArray(new String[0]), null, null, null);

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
                    cursor.getString(cursor.getColumnIndex(WEB)),
                    cursor.getDouble(cursor.getColumnIndex(RATE))
            );
            places.add(place);
            cursor.moveToNext();
        }
        db.close();
        return places;
    }

    public Map<String, Integer> getCategoryCount(){
        Map<String, Integer> result = new HashMap<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + TYPE + ", count(" + TYPE + ") as CatCount FROM " + TABLE + " GROUP BY " + TYPE, null);
        cursor.moveToFirst();
        for (int i=0; i<cursor.getCount(); i++){
            result.put(cursor.getString(0), cursor.getInt(1));
            cursor.moveToNext();
        }
        db.close();
        return result;
    }

}
