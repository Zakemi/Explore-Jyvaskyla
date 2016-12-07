package fi.jamk.hunteam.explorejyvaskylaandroid.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Model for the categories. They are created only here.

public class Categories {

    Map<String, Category> categories;

    // Create the categories with the titles
    public Categories(){
        categories = new HashMap<>();
        categories.put("museum", new Category("museum", "Knower of the Past", "Researcher of the Past", "Interested in the Past"));
        categories.put("cafe", new Category("cafe", "Drinks Coffee Like a Finnish", "Coffee Lover", "Knows What Coffee Is"));
        categories.put("church", new Category("church", "Close to God", "Seeking God", "Believer"));
        categories.put("entertainment", new Category("entertainment", "Eternal Child", "On the Move", "Playful"));
        categories.put("bar", new Category("bar", "Just one more beer", "Makes Friends in bar", "Bar visitor"));
        categories.put("bakery", new Category("bakery", "Pulla Lover", "Cookies researcher", "Sweet-toothed"));
        categories.put("food", new Category("food", "Chow Hound", "Gobbler", "Hungry"));
        categories.put("other", new Category("other", "Unkonwn Knower", "Going to Unusual Places", "Dissatisfied with the Known"));
        categories.put("nature", new Category("nature", "Hippy", "Nature-Lover", "Touring"));
        categories.put("park", new Category("park", "Park-ing", "Sitting on bench", "Park finder"));
    }

    // Return the name of the categories in a sorted list
    public List<String> getCategoryNames(){
        Set<String> set =  categories.keySet();
        List<String> result = new ArrayList<>(set);
        Collections.sort(result);
        return result;
    }

    // Return the gold title of a category
    public String getGoldTitleByName(String name){
        return categories.get(name).getGoldTitle();
    }

    // Return the silver title of a category
    public String getSilverTitleByName(String name){
        return categories.get(name).getSilverTitle();
    }

    // Return the bronze title of a category
    public String getBronzeTitleByName(String name){
        return categories.get(name).getBronzeTitle();
    }

    // Model for a single category
    public static class Category {
        private String name;
        private String goldTitle;
        private String silverTitle;
        private String bronzeTitle;

        public Category(String name, String goldTitle, String silverTitle, String bronzeTitle) {
            this.name = name;
            this.goldTitle = goldTitle;
            this.silverTitle = silverTitle;
            this.bronzeTitle = bronzeTitle;
        }

        public String getName() {
            return name;
        }

        public String getGoldTitle() {
            return goldTitle;
        }

        public String getSilverTitle() {
            return silverTitle;
        }

        public String getBronzeTitle() {
            return bronzeTitle;
        }
    }

}
