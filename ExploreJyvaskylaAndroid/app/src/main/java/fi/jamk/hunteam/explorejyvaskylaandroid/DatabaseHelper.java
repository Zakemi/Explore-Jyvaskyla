package fi.jamk.hunteam.explorejyvaskylaandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DoubleD on 2016. 10. 22..
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database";
    public static final String VISITED_PLACES_TABLE = "visited_places";
    public static final String VPT_NAME = "name";
    public static final String VPT_LAT = "lat";
    public static final String VPT_LNG = "lng";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + VISITED_PLACES_TABLE
                + " (_id  INTEGER PRIMARY KEY, "
                + VPT_NAME + " TEXT,"
                + VPT_LAT + " REAL,"
                + VPT_LNG + " REAL);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + VISITED_PLACES_TABLE);
        onCreate(db);
    }
}
