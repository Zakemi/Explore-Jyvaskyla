package fi.jamk.hunteam.explorejyvaskylaandroid;

import android.content.Context;
import android.content.SharedPreferences;

// Helper class to save and get shared preferences

public class ManageSharedPreferences {

    public static class Manager{

        private static final String PREFS_NAME = "MyPrefsFile";
        private String id;
        private String name;
        private String picture;
        private SharedPreferences settings;

        // Get the stored data
        public Manager(Context context){
            settings = context.getSharedPreferences(PREFS_NAME, 0);
            id = settings.getString("Id", "");
            name = settings.getString("Name", "");
            picture = settings.getString("Picture", "");
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPicture() {
            return picture;
        }

        // Set the variables and store the data
        public void setIdNamePicture(String id, String name, String picture) {
            this.id = id;
            this.name = name;
            this.picture = picture;
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("Id", id);
            editor.putString("Name", name);
            editor.putString("Picture", picture);
            editor.commit();
        }
    }

}
