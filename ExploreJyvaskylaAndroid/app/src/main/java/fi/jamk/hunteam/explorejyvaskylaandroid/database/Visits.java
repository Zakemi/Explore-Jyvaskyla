package fi.jamk.hunteam.explorejyvaskylaandroid.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;

import fi.jamk.hunteam.explorejyvaskylaandroid.model.InterestingPlace;

/**
 * Connect to the visits table in the database.
 */

public class Visits extends SQLiteOpenHelper {

    private Context context;

    public static final String DATABASE = "database";
    public static final String TABLE = "visits";
    public static final String ID = "Id";
    public static final String PLACE_ID = "PlaceId";

    public Visits(Context context) {
        super(context, DATABASE, null, 1);
        this.context = context;
        try{
            onCreate(this.getWritableDatabase());
        } catch (android.database.sqlite.SQLiteException e){
            System.out.println("Visits is already created.");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE
                + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PLACE_ID + " INTEGER);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    // The user have visited a place, save it in the database
    public void addVisitedPlaceToDatabase(InterestingPlace place){
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLACE_ID, place.getId());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE, null, contentValues);
        db.close();
    }

    // Return true if the user have already visited a place
    public boolean isPlaceVisitedAlready(InterestingPlace place){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + PLACE_ID + " from " + TABLE
                + " WHERE " + PLACE_ID + " LIKE " + place.getId(), null);
        if (cursor.getCount() == 0){
            db.close();
            return false;
        }
        else {
            db.close();
            return true;
        }
    }

    // Return the number of the visited places in the categories
    public Map<String, Integer> getCategoryCount(){
        Locations locations = new Locations(context);
        Map<String, Integer> result = new HashMap<>();
        SQLiteDatabase visitsDB = this.getWritableDatabase();
        SQLiteDatabase locationsDB = locations.getReadableDatabase();
        Cursor cursor = visitsDB.rawQuery("SELECT " + locations.TYPE + ", count(" + locations.TYPE + ")"
                + " FROM " + locations.TABLE + " l"
                + " INNER JOIN " + TABLE + " v ON l." + locations.ID + "=v." + PLACE_ID
                + " WHERE l." + locations.ID + "=v." + PLACE_ID
                + " GROUP BY " + locations.TYPE, null);
        cursor.moveToFirst();
        for (int i=0; i<cursor.getCount(); i++){
            result.put(cursor.getString(0), cursor.getInt(1));
            cursor.moveToNext();
        }
        visitsDB.close();
        return result;
    }
}
